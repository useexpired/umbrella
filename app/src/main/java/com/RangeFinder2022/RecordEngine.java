package com.rangefinder2022;

import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

class RecordEngine {
    //
    //  UI
    //
    private ImageButton butRecorder;
    private Chronometer chrono;
    private final AppCompatActivity acti;
    //
    //  Audio Recorder Parameter
    //
    AudioGainHandle audioGainHandle;
    UtilDist2freq utilDist2freq;
    //
    //      distances CSV file
    //
    public DistCSVhandle distCSVfile;
    public boolean recordDist=true;
    //
    //
    private Boolean isRecordingSound;
    private String filePathNname_="";
    private String fileName_="";

    private native void PCMrecorderStart(String filepath);
    private native void PCMrecorderStop();

    private final MainActivity.DistCSVhandleListener listenCSVhandle;

    public RecordEngine(AppCompatActivity acti, UtilDist2freq ut, MainActivity.DistCSVhandleListener hearDistCSV ){

        this.acti=acti;
        this.listenCSVhandle=hearDistCSV;
        this.utilDist2freq=ut;

        init();
        audioGainHandle = new AudioGainHandle(acti);
    }

    private void init(){
        //
        //  recorder UI
        //
        chrono=acti.findViewById(R.id.chrono);
        butRecorder = acti.findViewById(R.id.butRec);
        isRecordingSound =false;

        updateRecorderUi();

        butRecorder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                isRecordingSound =!isRecordingSound;
                if(isRecordingSound){

                    isRecordingSound =true;

                    prepareWavFileName();
                    PCMrecorderStart(filePathNname_);
                    Toast.makeText(acti, "Recording...",  Toast.LENGTH_SHORT).show();
                    //
                    //  prepare CSV file of distances
                    //
                    if(recordDist) {
                        distCSVfile = new DistCSVhandle(acti, listenCSVhandle);
                        distCSVfile.startRecording();
                    }
                    //
                    //  UI chronometer start
                    //
                    chrono.setBase(SystemClock.elapsedRealtime());
                    chrono.start();

                }else{
                    chrono.stop();
                    isRecordingSound =false;
                    //
                    //  write .wav file to SDcard
                    //
                    Toast.makeText(acti, fileName_+" saved",
                             Toast.LENGTH_LONG).show();

                    PCMrecorderStop();

                    if(recordDist) {
                        distCSVfile.stopRecNwrite();
                    }
                }
                updateRecorderUi();
            }
        });
    }

    private void updateRecorderUi(){
        butRecorder.setBackgroundResource(
                isRecordingSound ?
                        R.drawable.recorder_red :
                        R.drawable.recorder_blue
        );
    }

    private void prepareWavFileName(){

        UtilsFile utilsFile =new UtilsFile();
        String thePath= utilsFile.getAudioFileFolder(
                acti.getApplicationContext());

        String suffix=utilsFile.composeSuffix(
                utilDist2freq.getDistMin(),
                utilDist2freq.getDistMax(),
                utilDist2freq.getFreqMin(),
                utilDist2freq.getFreqMax(),
                audioGainHandle.getGainFactor()
        );

        filePathNname_= utilsFile.getAudioFilePathNname(
                acti.getApplicationContext(), suffix);

        fileName_=utilsFile.strFileName;
    }
}
