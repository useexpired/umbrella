package com.rangefinder2022;

import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class DistCSVhandle {

    AppCompatActivity acti;
    MainActivity.DistCSVhandleListener listener;
    final Timer timer = new Timer();
    private final DistCSV distCSV;

    private final int RECORD_INTERVAL=100;

    public DistCSVhandle(AppCompatActivity acti, MainActivity.DistCSVhandleListener listener) {

        this.listener=listener;
        this.acti=acti;
        UtilsFile aaa=new UtilsFile();

        distCSV=new DistCSV();
        distCSV.timeStamp_=aaa.getNow2string();
        distCSV.interval_ms_=RECORD_INTERVAL;
        distCSV.distance_=new float[65536]; //600 for 1 min
        distCSV.total_=0;
    }


    public void startRecording(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                listener.onRequestDistance();
                // have a long run to MainActivity, and call back update() here with its "dist"
            }
        }, 0,RECORD_INTERVAL);
    }
    public void stopRecording(){
        timer.cancel();
    }

    public void stopRecNwrite() {

        stopRecording();

        UtilsFile util = new UtilsFile();
        final String csvFilename =
                util.getAudioFileFolder(
                        acti.getApplicationContext()) + "/" +
                        "ssp5_"+getTimeStamp() + ".csv";

        writeToFile(csvFilename, convertToCSVstring());
    }

    public void update(float dist){
        distCSV.distance_[distCSV.total_++]=dist;
    }

    public DistCSV getDistanceCSV(){
        return distCSV;
    }

    public int getTotal(){
        return distCSV.total_;
    }

    public String report() {
        String msg=distCSV.timeStamp_;
        msg+="\n"+distCSV.interval_ms_+"\n";
        for (int i = 0; i < distCSV.total_; i++) {
             msg+="\n"+distCSV.distance_[i];
        }
        return msg;
    }

    public String convertToCSVstring(){
        String comma=",", newline="\n";
        String data=distCSV.timeStamp_;
        data+=newline+ distCSV.interval_ms_;
        for(int i=0; i < distCSV.total_; i++) {
            data += newline + distCSV.distance_[i];
        }
        return data;
    }

    public String getTimeStamp(){
        return distCSV.timeStamp_;
    }

    public void writeToFile(String filename, String data) {
        try {
            File file = new File(filename);
            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(data.getBytes());
            } finally {
                stream.close();
            }
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
