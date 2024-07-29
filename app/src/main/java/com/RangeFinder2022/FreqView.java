package com.rangefinder2022;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.slider.RangeSlider;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

public class FreqView {
    //
    //      frequency setting Panel
    //
    private RangeSlider freqSlider;
    private TextView txtFreqMin, txtFreqMax;
    private TextView txtFreq;
    private ProgressBar barFreq;
    private final Context ctx;

    private int freq=440;
    private int delayCtr =0;
    private static final int DELAY_INTERVAL=20;
    private final MainActivity.FreqViewListener listener;

    public int getFreq(){
        return freq;
    }
    public FreqView(AppCompatActivity acti, MainActivity.FreqViewListener listener ){

        this.ctx= acti;
        this.listener=listener;
        freq=440;
        init();
    }


    private void init(){
        AppCompatActivity mActivity=(AppCompatActivity)ctx;

        txtFreq=mActivity.findViewById(R.id.txtFreq);
        showFreq(freq);
        //
        //  frequency visualizer (as progress bar)
        //
        barFreq=mActivity.findViewById(R.id.barFreq);
        //
        //  frequency UI values
        //
        txtFreqMin=mActivity.findViewById(R.id.txtFreqMin);
        txtFreqMax=mActivity.findViewById(R.id.txtFreqMax);
        //
        //  freq RangeSlider
        //
        freqSlider= mActivity.findViewById(R.id.freqSlider);


        freqSlider.addOnChangeListener(new RangeSlider.OnChangeListener(){
            @Override
            public void onValueChange( RangeSlider slider, float value, boolean fromUser) {}
        });

        freqSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener(){
            //
            //      user stop scrolling slider
            //
            @Override
            public void onStartTrackingTouch(RangeSlider slider) {}
            @Override
            public void onStopTrackingTouch(RangeSlider slider) {
                List<Float> val=slider.getValues();
                int freqFrom = Math.round(val.get(0));
                int freqTo = Math.round(val.get(1));

                txtFreqMin.setText(freqFrom +"Hz");
                txtFreqMax.setText(freqTo +"Hz");

                listener.onFreqRangeChanged(freqFrom, freqTo);
            }
        });
    }

    public void showFreq(final int freq){
        listener.onWeirdUtilsNeeded(txtFreq, freq+"Hz", UtilsWeird.TxtType.VIEW);
    }

    public void updateBarFreq(final int max, final int prog ){
        delayCtr++;
        if(delayCtr > DELAY_INTERVAL ) {
            barFreq.setMax(max);
            barFreq.setProgress(prog);
            delayCtr =0;
        }
    }
}
