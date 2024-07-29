package com.rangefinder2022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.slider.RangeSlider;

import java.util.List;
//
//      RangerFinder2022
//      2022
//
//
//      app to implement Terabee rangefinder and
//      transform live distance data to waveform
//
//
public class MainActivity extends AppCompatActivity
        implements SettinDialogFrag.SettinDialogListener{ //implements SensorEventListener {

    public interface DistCSVhandleListener {
        void onRequestDistance();
    }

    private float distFromLIDAR=0; // in meter; distance / 1000

    private UtilsWeird utilsWeird;
    private UtilDist2freq utilDist2freq;
    //
    //      LIDAR Distance Range setting Panel
    //
    private RangeSlider distSlider;
    private TextView txtDistMin, txtDistMax;
    private DistDialog distanceDialog; // distance maximum value custom dialog
    //
    //      LIDAR
    //
    private LIDARsensor lidarSensor;
    public interface LIDARsensorListener {
        void onStateChanged(Boolean status);
        void onDistanceReceived(int distCM);
        void onWeirdUtilsNeeded(TextView tv, String msg, UtilsWeird.TxtType msgType);
    }
    //
    //      Bubble Level
    //
    private BubbleEngine bubbleEngine;
    //
    //      sound recorder engine
    //
    RecordEngine recordEngine;
    //
    //      wave form selector
    //
    private ImageButton butWaveForm;
    private int waveFormEnum=0;
    //
    //      native stuff
    //
    static {
        System.loadLibrary("jni-lib");
    }
    private native void updateAAudioFreq(float frequency);
    private native void syncNativeFreqRange(int freqFrom, int freqTo);
    private native void aaudioEngineStart();
    private native void aaudioEngineStop();
    private native void updateWaveFormNative(int waveformEnum);
    //
    //      frequency View
    //
    private FreqView freqView;
    public interface FreqViewListener {
        void onFreqRangeChanged(int freqFm, int freqTo);
        void onLIDARneeded(String msg);
        void onWeirdUtilsNeeded(TextView tv, String msg, UtilsWeird.TxtType msgType);
    }

    private void freqViewBorn(){
        FreqViewListener hearFreqView = new FreqViewListener() {
            @Override
            public void onFreqRangeChanged(int freqFm, int freqTo) {
                utilDist2freq.setFreqMinMax(freqFm, freqTo);
                syncNativeFreqRange(freqFm, freqTo);
            }

            @Override
            public void onLIDARneeded(String msg) {
                lidarSensor.log(msg);
            }

            @Override
            public void onWeirdUtilsNeeded(TextView tv, String msg, UtilsWeird.TxtType msgType) {
                utilsWeird.setMultiple(tv,msg,msgType);
            }
        };
        freqView=new FreqView(this, hearFreqView);
    }

    private void LIDARborn(){
        //
        // wonderful the android studio editor pull
        // all these codes to us automatically
        //
        LIDARsensorListener hearLIDAR = new LIDARsensorListener() {
            @Override
            public void onStateChanged(Boolean isOn) {
                if (isOn) {
                    aaudioEngineStart();
                } else {
                    aaudioEngineStop();
                }
            }

            @Override
            public void onDistanceReceived(int distCM) {
                distFromLIDAR=(float)distCM/1000f;
                dist2tone(distCM);
            }

            @Override
            public void onWeirdUtilsNeeded(TextView tv, String msg, UtilsWeird.TxtType msgType) {
                utilsWeird.setMultiple(tv, msg, msgType);
            }
        };

        lidarSensor = new LIDARsensor(this, hearLIDAR);
    }


    private void bubbleBorn(){
        bubbleEngine=new BubbleEngine(this);
        bubbleEngine.initBubble();
        bubbleEngine.initAccelerometer();
        bubbleEngine.initBubbleLayout(this);
    }

    private void recorderBorn(){
        //  init sound recorder engine

        DistCSVhandleListener hearDistCSV = new DistCSVhandleListener(){
            @Override
            public void onRequestDistance() {
                recordEngine.distCSVfile.update(getDistanceFromLIDAR());
            }
        };
        recordEngine =new RecordEngine(this, utilDist2freq, hearDistCSV);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utilsWeird =new UtilsWeird();
        utilDist2freq=new UtilDist2freq(this);

        distancePanelBorn();
        freqViewBorn();
        LIDARborn();
        bubbleBorn();
        recorderBorn();
        waveFormBorn();
    }


    protected void onResume() {
        super.onResume();
        bubbleEngine.setAcceleorRegistStatus(true);
    }

    protected void onPause() {
        super.onPause();
        bubbleEngine.setAcceleorRegistStatus(false);
    }

    protected void onDestroy(){
        super.onDestroy();
        lidarSensor.stop();
    }

    private void dist2tone(final int distCM){
        //
        // distance (int) in centimeters will  be translated into frequency
        //
        final int freq= utilDist2freq.distance2freq(distCM);
        final int max= utilDist2freq.getFreqMax()- utilDist2freq.getFreqMin();
        final int prog=freq- utilDist2freq.getFreqMin();

        updateAAudioFreq(freq);
        freqView.showFreq(freq);
        freqView.updateBarFreq( max, prog);
     }

    public void distancePanelBorn(){

        txtDistMin=findViewById(R.id.txtDistMin);
        txtDistMax=findViewById(R.id.txtDistMax);
        txtDistMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distanceDialog= new DistDialog(MainActivity.this, distSlider, txtDistMax);
                distanceDialog.fireDialog(getSupportFragmentManager(),true,0); // last argu unused if true
            }
        });
        //
        //  range slider
        //
        distSlider=findViewById(R.id.distSlider);
        distSlider.addOnChangeListener(new RangeSlider.OnChangeListener(){
            @Override
            public void onValueChange( RangeSlider slider, float value, boolean fromUser) {}
        });

        distSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener(){
            //
            //      user stop scrolling slider
            //
            @Override
            public void onStartTrackingTouch(RangeSlider slider) {}
            @Override
            public void onStopTrackingTouch(RangeSlider slider) {
                List<Float> val=slider.getValues();

                int distFrom = Math.round(val.get(0));
                int distTo = Math.round(val.get(1));

                txtDistMin.setText(distFrom +"m");
                txtDistMax.setText(distTo +"m");

                utilDist2freq.setDistMinMax(distFrom, distTo);

            }
        });
    }

    private void waveFormBorn() {
        waveFormEnum=0;
        butWaveForm = findViewById(R.id.butWaveForm);
        butWaveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveFormEnum = (waveFormEnum + 1) % 4;
                updateWaveFormUI(waveFormEnum);
                updateWaveFormNative(waveFormEnum);
            }
        });

        updateWaveFormUI(waveFormEnum);
        updateWaveFormNative(waveFormEnum);
    }

    private void updateWaveFormUI(int waveFormEnum){
        int waveDrawable;
        switch(waveFormEnum) {
            case 0: waveDrawable = R.drawable.ic_svg_sine;     break;
            case 1: waveDrawable = R.drawable.ic_svg_triangle; break;
            case 2: waveDrawable = R.drawable.ic_svg_sawtooth; break;
            default:waveDrawable = R.drawable.ic_svg_square;   break;

        }
        butWaveForm.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        butWaveForm.setBackgroundResource(waveDrawable);
    }
    @Override
    public void onDialogPos(DialogFragment dialog, int freqMax, int distMax, boolean status) {
        if(status) {
            distanceDialog.sleepyPositiveClick(dialog, freqMax, distMax );
        }else{
            distanceDialog.fireDialog(getSupportFragmentManager(), false, distMax);
        }
    }

    public float getDistanceFromLIDAR(){
        return distFromLIDAR;
    }
}