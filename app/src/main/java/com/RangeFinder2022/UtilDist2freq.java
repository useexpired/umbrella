package com.rangefinder2022;
import androidx.appcompat.app.AppCompatActivity;

public class UtilDist2freq {
    /*
          Distance reported from LIDAR corresponds to audible frequencies
    */
    private final float DIST_METER_MAX_SENSOR=60.0f;
    private int freqMin =110;
    private int freqMax =1100;
    private float distanceMin =1.0f;
    private float distanceMax =DIST_METER_MAX_SENSOR;

    private void setDefaultMinMax(AppCompatActivity acti){
        String[] val;

        val= acti.getResources().getStringArray(R.array.distSliderValues);
        setDistMinMax(Integer.parseInt(val[0]),Integer.parseInt(val[1]));
        val= acti.getResources().getStringArray(R.array.freqSliderValues);
        setFreqMinMax(Integer.parseInt(val[0]),Integer.parseInt(val[1]));
    }

    public UtilDist2freq(AppCompatActivity acti){
        setDefaultMinMax(acti);
    }

    public UtilDist2freq(int dm1, int dm2, int f1, int f2){
        setDistMinMax(dm1, dm2);
        setFreqMinMax(f1,f2);
    }

    public void setDistMinMax(final int minM, final int maxM){
        distanceMin =minM*1.0f;
        distanceMax =maxM*1.0f;
    }

    public void setFreqMinMax(final int min, final int max){
        freqMin =min;
        freqMax =max;
    }

    public int getFreqMin(){return freqMin;}
    public int getFreqMax(){return freqMax;}
    public float getDistMin(){return distanceMin; }
    public float getDistMax(){return distanceMax;}

    public int distance2freq(int dist){

        float distCM=dist/1000.0f;
        //
        // distance in centimeter
        //
        int freq= freqMin;

        if(distCM >= distanceMax){
            freq= freqMax;
        }else
        if(distCM <= distanceMin){
            freq= freqMin;
        }else {
            freq = Math.round(
                    ((freqMax - freqMin) /
                    (distanceMax - distanceMin) *
                    (distCM - distanceMin)) + freqMin);
        }
        return freq;
    }

}
