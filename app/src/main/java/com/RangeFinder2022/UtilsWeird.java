package com.rangefinder2022;

import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UtilsWeird extends AppCompatActivity{

    private static final int MAX_MSG_LINES=18;
    private int msgLines=0;

    public enum TxtType{
        MSG,
        VIEW
    }


    public UtilsWeird(){
        this.msgLines=0;
    }

    public void setMsg(AppCompatActivity activ, TextView tv, String txt){
        try {
            activ.runOnUiThread(() -> {
                msgLines++;
                if(msgLines >= MAX_MSG_LINES){
                    msgLines--;
                    final String str=tv.getText().toString();
                    tv.setText(str.substring(0,str.lastIndexOf("\n")));
                }
                tv.setText(txt + "\n" + tv.getText());
            });
        }catch (Exception ex) {
            Log.e("LIDAR UI","LIDAR UI setMsgText() ERROR");
        }
    }
    public void setMsg(TextView tv, String txt){
        try {
            runOnUiThread(() -> {
                msgLines++;
                if(msgLines >= MAX_MSG_LINES){
                    msgLines--;
                    final String str=tv.getText().toString();
                    tv.setText(str.substring(0,str.lastIndexOf("\n")));
                }
                tv.setText(txt + "\n" + tv.getText());
            });
        }catch (Exception ex) {
            Log.e("LIDAR UI","LIDAR UI setMsgText() ERROR");
        }
    }

    public void setText(TextView tv, String txt){
        try {
            runOnUiThread(() -> {
                tv.setText(txt);
            });
        }catch (Exception ex) {
            Log.e("LIDAR UI","LIDAR UI settext() ERROR");
        }
    }

    public void setMultiple(TextView tv, String msg, TxtType msgType) {

        if (msgType == TxtType.VIEW) {
            setText(tv, msg);
        }
        if (msgType == TxtType.MSG){
            setMsg(tv, msg);
        }
    }


    public void setText(AppCompatActivity activ, TextView tv, String txt){
        try {
            activ.runOnUiThread(() -> {
                tv.setText(txt);
            });
        }catch (Exception ex) {
            Log.e("LIDAR UI","LIDAR UI settext() ERROR");
        }
    }
}
