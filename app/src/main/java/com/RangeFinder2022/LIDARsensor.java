package com.rangefinder2022;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import com.terabee.sdk.TerabeeSdk;

public class LIDARsensor implements View.OnClickListener {
    //
    //  DEBUG facilities
    //
    private final Boolean IS_DEBUG= false;
    //
    //  LIDAR Sensor control
    //
    private LIDARstate lidarState;
    private Boolean isLIDARon = false; // for pause() exclusively
    private final MainActivity.LIDARsensorListener listener;
    //  UI
    private final AppCompatActivity acti;
    private ImageButton butLIDAR;
    private TextView txtLIDARstatus, txtLIDARdist, txtCallback, txtMsg;
    //
    //  2 types of callbacks :
    //
    //          1. data Sensors
    //          2. data Distance
    //
    //      1. sensor callback (is used in this demo)
    //
    private final TerabeeSdk.DataSensorCallback
            terabeeSensorCallback = new TerabeeSdk.DataSensorCallback() {

        @Override
        public void onDistanceReceived(int distance, int dataBandwidth, int dataSpeed) {
           updateDistance(distance, dataBandwidth, dataSpeed, CallbackFrom.Sensor);
        }

        @Override    public void onReceivedData(byte[] data, int dataBandwidth, int dataSpeed) {}
        @Override    public void onMatrixReceived(List<List<Integer>> list, int dataBandwidth, int dataSpeed) {}
        @Override    public void onDistancesReceived(List<Integer> list, int dataBandwidth, int dataSpeed) {}
    };
    //
    //    2.  distance (NOT distances) callbacks
    //
    private final TerabeeSdk.DataDistanceCallback
            terabeeDistanceCallback = new TerabeeSdk.DataDistanceCallback() {
        @Override
        public void onDistanceReceived(int distance, int dataBandwidth, int dataSpeed) {
            updateDistance(distance, dataBandwidth, dataSpeed, CallbackFrom.dist);
        }

        @Override
        public void onReceivedData(byte[] data, int dataBandwidth, int dataSpeed) {
        }
    };
    //
    //  constructor
    //
    public LIDARsensor(AppCompatActivity acti, MainActivity.LIDARsensorListener listener ){
        this.acti=acti;
        this.listener=listener;

        initUI();
        init(this.acti);
        start();
    }

    private void initUI(){

        isLIDARon =false;

        butLIDAR = acti.findViewById(R.id.butLIDAR);
        butLIDAR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isLIDARon =!isLIDARon;
                changeState(isLIDARon);
                updateLIDARui();
            }
        });
        updateLIDARui(); // update UI icon according to sensor status

        txtLIDARdist = acti.findViewById(R.id.txtLIDARdist);
        txtMsg = acti.findViewById(R.id.txtMsg);
        //
        //  Terabee setup
        //
        txtLIDARstatus = acti.findViewById(R.id.txtLIDARstatus);
        updateLIDARstatus(LIDARstate.Disconnected);
    }


    public void init(Context ctx){

        listener.onWeirdUtilsNeeded(txtMsg,"initLIDAR...", UtilsWeird.TxtType.MSG);
        //
        // init Terabee Sdk
        //
        TerabeeSdk.getInstance().init(ctx);
        TerabeeSdk.getInstance().registerDataReceive(terabeeSensorCallback);

        listener.onWeirdUtilsNeeded( txtMsg,"after registerDataReceive", UtilsWeird.TxtType.MSG);
        listener.onWeirdUtilsNeeded( txtMsg,"initLIDAR...ended", UtilsWeird.TxtType.MSG);
    }

    public void log(String msg){
        listener.onWeirdUtilsNeeded(txtMsg, msg, UtilsWeird.TxtType.MSG);
    }

    public void start(){
        listener.onWeirdUtilsNeeded( txtMsg,"startLIDAR...", UtilsWeird.TxtType.MSG);

        updateLIDARstatus(LIDARstate.Connecting);

        if(!IS_DEBUG) { // do not connect to the LIDAR sensor when starting the APP

            Thread connectThread = new Thread(() -> {
                try {
                    TerabeeSdk.getInstance().connect(
                            new TerabeeSdk.IUsbConnect() {
                                @Override
                                public void connected(boolean success, TerabeeSdk.DeviceType deviceType) {
                                    if (success) {
                                        listener.onWeirdUtilsNeeded(txtMsg, "IUSBconnect() succeed", UtilsWeird.TxtType.MSG);
                                        updateLIDARstatus(LIDARstate.Connected);
                                    } else {
                                        listener.onWeirdUtilsNeeded(txtMsg, "IUSBconnect() unable to connect to" + deviceType.name(), UtilsWeird.TxtType.MSG);
                                        updateLIDARstatus(LIDARstate.Disconnected);
                                    }
                                }

                                @Override
                                public void disconnected() {
                                    updateLIDARstatus(LIDARstate.Disconnected);
                                    listener.onWeirdUtilsNeeded(txtMsg, "IUSBconnect() disconnected", UtilsWeird.TxtType.MSG);
                                }

                                @Override
                                public void permission(boolean granted) {
                                    listener.onWeirdUtilsNeeded(txtMsg, "permission : " + ((granted) ? "OK" : "failed"), UtilsWeird.TxtType.MSG);
                                }
                            }, TerabeeSdk.DeviceType.EVO_60M);
                    //
                    //  hardcoded model Evo 60m
                    //  for auto detection uses :
                    //  TerabeeSdk.DeviceType.AUTO_DETECT
                    //
                } catch (Exception e) {
                    listener.onWeirdUtilsNeeded(txtMsg, "Exception of connectThread() when attempting at .connect()", UtilsWeird.TxtType.MSG);
                    Log.e("ERROR LIDARsensor", "error connecting Devices");
                }
            });
            connectThread.start();
        }

        listener.onWeirdUtilsNeeded( txtMsg,"after .connect() thread", UtilsWeird.TxtType.MSG);
        listener.onWeirdUtilsNeeded( txtMsg,"after .connect() LIDAR state =" + lidarState, UtilsWeird.TxtType.MSG);
    }


    public void changeState(final Boolean isOn){
        if(isOn){
            TerabeeSdk.getInstance().registerDataReceive(terabeeSensorCallback);
        }else {
            TerabeeSdk.getInstance().unregisterDataReceive(terabeeSensorCallback);
        }
        listener.onStateChanged(isOn);
        isLIDARon =isOn;
    }

    public void stop(){
        //
        // release Terabee Sdk resources
        //
        TerabeeSdk.getInstance().unregisterDataReceive(terabeeSensorCallback);
        TerabeeSdk.getInstance().dispose();
        updateLIDARstatus(LIDARstate.Disconnected);
        txtLIDARdist.setText("");
    }

    @Override
    public void onClick(View v) {
    }

    private enum LIDARstate {
        Disconnected,
        Connecting,
        Connected,
        Paused,
    }
    private enum CallbackFrom{
        Sensor,
        dist,
    }

    private void updateLIDARstatus(final LIDARstate status) {

        if (status == LIDARstate.Connected) {

            lidarState = LIDARstate.Connected;
            listener.onWeirdUtilsNeeded(txtLIDARstatus,"CONNECTED", UtilsWeird.TxtType.VIEW);
            listener.onWeirdUtilsNeeded( txtMsg,"LIDAR status : connected.", UtilsWeird.TxtType.MSG);

        }
        if (status == LIDARstate.Connecting) {
            lidarState = LIDARstate.Connecting;
            listener.onWeirdUtilsNeeded(txtLIDARstatus,"CONNECTING...", UtilsWeird.TxtType.VIEW);
            listener.onWeirdUtilsNeeded( txtMsg,"LIDAR status : connecting...", UtilsWeird.TxtType.MSG);
        }
        if (status == LIDARstate.Disconnected) {
            lidarState = LIDARstate.Disconnected;
            listener.onWeirdUtilsNeeded(txtLIDARstatus,"DISCONNECTED", UtilsWeird.TxtType.VIEW);
            listener.onWeirdUtilsNeeded( txtMsg,"LIDAR status : disconnected.", UtilsWeird.TxtType.MSG);
        }
        if (status == LIDARstate.Paused) {
            lidarState = LIDARstate.Paused;
            listener.onWeirdUtilsNeeded(txtLIDARstatus,"DISCONNECTED", UtilsWeird.TxtType.VIEW);
            listener.onWeirdUtilsNeeded( txtMsg,"LIDAR status : paused.", UtilsWeird.TxtType.MSG);
        }
    }

    private void updateDistance(int distCM, int dataBandwidth, int dataSpeed, CallbackFrom src) {
        if (lidarState == LIDARstate.Connected) {
          listener.onWeirdUtilsNeeded(txtLIDARdist,
                    String.format("%.3f", distCM/1000f) + "m", UtilsWeird.TxtType.VIEW);
          listener.onDistanceReceived(distCM);
        }
    }

    private void updateLIDARui() {
        butLIDAR.setBackgroundResource(
                isLIDARon ?
                        R.drawable.lidar300_on :
                        R.drawable.lidar300_off
        );
    }
}


