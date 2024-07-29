package com.rangefinder2022;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class AudioGainHandle {

    private Button butGainLess, butGainMore;
    private TextView txtGain;
    private final AppCompatActivity acti;

    private int amplitudeGainFactor;

    private final Context ctx;

    public AudioGainHandle(AppCompatActivity acti){
        this.ctx= acti;
        this.acti=(AppCompatActivity)ctx;
        born();
    }

    public int getGainFactor(){
        return amplitudeGainFactor;
    }

    private void born() {
        amplitudeGainFactor = 1;

        txtGain = acti.findViewById(R.id.txtGain);
        txtGain.setText(Integer.toString(amplitudeGainFactor));

        butGainLess = acti.findViewById(R.id.butGainLess);
        butGainLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amplitudeGainFactor--;
                if (amplitudeGainFactor <= 1) amplitudeGainFactor = 1;
                txtGain.setText(Integer.toString(amplitudeGainFactor));
            }
        });

        butGainMore = acti.findViewById(R.id.butGainMore);
        butGainMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amplitudeGainFactor++;
                if( amplitudeGainFactor > 100) amplitudeGainFactor=100;
                txtGain.setText(Integer.toString(amplitudeGainFactor));
                Toast.makeText(acti,"gain="+amplitudeGainFactor,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
