package com.rangefinder2022;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilsFile {

    public String strFileName;
    public String strFilePath;

    private static final String TAG="UTILS-FILE-IO";

    UtilsFile(){}

    public final String getAudioFileFolder(Context context){
       return  getAudioFilePointer(context).getAbsolutePath();
    }

    private final File getAudioFilePointer(Context ctx){
        File fileAudio = new File(ctx.getExternalFilesDir(null), "audio");
        if (!fileAudio.exists()) {
            boolean dirStatus = fileAudio.mkdirs();
        }
        return fileAudio;
    }

    public final String getAudioFilePathNname(Context ctx, String suffix) {

        File audioFilePtr = getAudioFilePointer(ctx);

        strFileName="ssp5_"+getNow2string()+suffix+".wav";
        File newFile = new File(audioFilePtr, strFileName);
        strFilePath = newFile.getAbsolutePath();

        Log.d(TAG, "getAudioRecordingFilePath: filePath: " + strFilePath +
             ", fileExists: " + newFile.exists());

        boolean fileStatus;

        if (newFile.exists()) {
            fileStatus = newFile.delete();
        }

        if (!newFile.exists()) {
            try {
                fileStatus = newFile.createNewFile();
            } catch (IOException ioe) {
                Log.d(TAG, "cannot create new file");
            }
        }

        return strFilePath;
    }

    public String getNow2string(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMdd_HHmmss");
        final Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    public String composeSuffix(float dist1, float d2, int freq1, int f2, int gain) {
        String suf="";
        suf+="d"+new DecimalFormat("00").format((int)dist1)
                +new DecimalFormat("00").format((int)d2)
                +"f"
                +new DecimalFormat("0000").format(freq1)
                +new DecimalFormat("0000").format(f2)
                +"g"+gain;

        return suf;
    }
}