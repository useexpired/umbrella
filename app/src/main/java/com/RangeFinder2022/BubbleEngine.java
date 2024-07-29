package com.rangefinder2022;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BubbleEngine implements SensorEventListener{
    //
    //  1. sensor
    //
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener senEvtList;
    BubbleMetrics bubbleMetrics;
    //
    //  2. UI
    //
    private float lastX, lastY, lastZ;
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private TextView bubbleDx, bubbleDy; // debug only
    private ImageView imgBubble, imgWallOnly, imgBubbleWall;
    private LinearLayout panelBubble;
    private float xLevel=0f, yLevel=0f; // bubble level values

    private Switch swMeter;

    private Boolean isBubbleSeen=true;

    private final AppCompatActivity acti;
    //
    //  listener was to catch its level change and sound is based on this level
    //

    public BubbleEngine(AppCompatActivity acti){
        this.acti=acti;
    }


    public void initBubble(){
        //
        //  Bubble Meter
        //
        panelBubble = acti.findViewById(R.id.layoutBubblePanel);
        //
        //  bubble Level
        //
        imgBubble = acti.findViewById(R.id.imgBubble);
        imgWallOnly = acti.findViewById(R.id.imgWallOnly);
        imgBubbleWall= acti.findViewById(R.id.imgBubbleWall);
        //
        //  Bubble Level x y info
        //
        bubbleDx = acti.findViewById(R.id.dx);
        bubbleDy = acti.findViewById(R.id.dy);
        //
        // turn accelerometer on
        //
        panelBubble.setVisibility(LinearLayout.VISIBLE);
        //
        //
        //  switch Bubble Meter
        //
        swMeter = acti.findViewById(R.id.switchBubble);
        swMeter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                panelBubble.setVisibility(View.VISIBLE);
                isBubbleSeen=isChecked;  // update bubble visibility  flag

                if(isChecked){
                    panelBubble.setVisibility(View.VISIBLE);
                    sensorManager.registerListener(senEvtList,
                            accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                }
                else {
                    panelBubble.setVisibility(View.GONE);
                    sensorManager.unregisterListener(senEvtList);
                }
            }
        });
    }


    public void initBubbleLayout(MainActivity v){
        //
        // ideal fix :
        // use the panelBubbleLevel to get real DP dimensions
        //
        LinearLayout.LayoutParams paraIdeal;
        LinearLayout layoutIdeal=acti.findViewById(R.id.panelBubbleLevel);
        paraIdeal=(LinearLayout.LayoutParams) layoutIdeal.getLayoutParams();

        RelativeLayout.LayoutParams para;

        bubbleMetrics = new BubbleMetrics(v);
        //
        //  set Bubble Level size
        //
        para=(RelativeLayout.LayoutParams)imgWallOnly.getLayoutParams();
        //
        //  ideal fix
        //
        para.width=paraIdeal.width;
        para.height=para.width;
        imgWallOnly.setLayoutParams(para);
        //
        //  set hollow bubble
        //
        para=(RelativeLayout.LayoutParams)imgBubbleWall.getLayoutParams();
        //  ideal fix
        para.width=paraIdeal.width / 2;
        para.height=para.width;

        para.leftMargin=para.width/2;
        para.rightMargin=para.leftMargin;
        para.topMargin=para.height/2;
        para.bottomMargin=para.topMargin;

        imgBubbleWall.setLayoutParams(para);
        //
        //  set THE bubble inside the meter
        //
        para=(RelativeLayout.LayoutParams)imgBubble.getLayoutParams();
        // ideal fix
        para.width=paraIdeal.width/2;
        para.height=para.width;

        para.leftMargin=para.width/2;
        para.rightMargin=para.leftMargin;
        para.topMargin=para.height/2;
        para.bottomMargin=para.topMargin;

        imgBubble.setLayoutParams(para);
    }


    public void setAcceleorRegistStatus(final Boolean status){
        if(status){
            sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            sensorManager.unregisterListener(this);
        }

    }

    public void initAccelerometer(){

        sensorManager = (SensorManager) acti.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            //
            // accelerometer detected
            //
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer,  SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // No accelerometer Detected
            Toast.makeText(acti,"no Accelerometer found",Toast.LENGTH_SHORT).show();
        }

    }


    private void moveBubble(float dx, float dy) {

        // ideal fix
        LinearLayout layoutIdeal=acti.findViewById(R.id.panelBubbleLevel);
        LinearLayout.LayoutParams paraIdeal=(LinearLayout.LayoutParams) layoutIdeal.getLayoutParams();
        //
        //  get central position of bubble
        //
        final int moveXdp=bubbleMetrics.axis2dp(dx, paraIdeal.width/2);
        final int moveYdp=bubbleMetrics.axis2dp(dy, paraIdeal.width/2);

        RelativeLayout.LayoutParams para=(RelativeLayout.LayoutParams)imgBubble.getLayoutParams();

        para.width = paraIdeal.width / 2;
        para.height= para.width;

        para.leftMargin=(para.width/2)+moveXdp;
        para.rightMargin=(para.width/2)-moveXdp;
        para.topMargin=(para.height/2)-moveYdp;
        para.bottomMargin=(para.height/2)+moveYdp;

        imgBubble.setLayoutParams(para);

        bubbleDx.setText( String.format("%.2f", dx)+"x");
        bubbleDy.setText( String.format("%.2f", dy)+"y");
    }

    private void moveBubble_TRASH(float dx, float dy) {

        RelativeLayout.LayoutParams para=(RelativeLayout.LayoutParams)imgBubble.getLayoutParams();
        //
        //  get central position of bubble
        //
        final int moveXpx=bubbleMetrics.axis2px(dx);
        final int moveYpx=bubbleMetrics.axis2px(dy);

        para.width =
                bubbleMetrics.dp2px(bubbleMetrics.getBubbleLevelWidthDP())/2;
        para.height= para.width;

        para.leftMargin=(para.width/2)+moveXpx;
        para.rightMargin=(para.width/2)-moveXpx;
        para.topMargin=(para.height/2)-moveYpx;
        para.bottomMargin=(para.height/2)+moveYpx;

        imgBubble.setLayoutParams(para);

        bubbleDx.setText( String.format("%.2f", dx)+"x");
        bubbleDy.setText( String.format("%.2f", dy)+"y");
    }



    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent evt) {

        xLevel=evt.values[0];
        yLevel=evt.values[1];

        moveBubble(evt.values[0],evt.values[1]);
        deltaX = Math.abs(lastX - evt.values[0]);
        deltaY = Math.abs(lastY - evt.values[1]);
        deltaZ = Math.abs(lastZ - evt.values[2]);

        lastX=evt.values[0];
        lastY=evt.values[1];
        lastZ=evt.values[2];

        final int MOVED_DX=(int)(deltaX *10f);

        // if the change is below 2, it is just plain noise
        if(MOVED_DX >=2 ) {
            /*
            if(listener !=null) {
                listener.onLevelReported(xLevel);
            }*/
        }
    }

}

