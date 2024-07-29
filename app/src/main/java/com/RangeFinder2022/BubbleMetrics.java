package com.rangefinder2022;

import android.util.DisplayMetrics;

class BubbleMetrics {

    private int scrDPI;
    private float scrDensity;
    private final int bubbleLevelWidthDP;

    public BubbleMetrics(MainActivity v){
        getScreenData(v);
        bubbleLevelWidthDP =inch2DP(2); // 2.0 inches wide
    }
    public int getBubbleLevelWidthDP(){
        return bubbleLevelWidthDP;
    }

    private void getScreenData(MainActivity v){
        DisplayMetrics metrics = v.getResources().getDisplayMetrics();
        scrDPI = (int)(metrics.density * 160f);
        scrDensity=metrics.density;
    }

    public int inch2DP(int inch){
        //
        // set bubble level size by dp
        //
        final int dp=(int)( (float)(inch)*(float)(scrDPI)/scrDensity);
        return dp;
    }

    public int dp2px(int dp) {
        return Math.round((float) dp * scrDensity);
    }

    public int axis2dp(float dd, final int bubbleWidthDP){
        //
        //  update bubble offset (in dp) according to accelerometer reading
        //
        //  dd : -10 to +10
        //
        //  dp2move  =   widthDP
        //    dd          20
        //
        final int dp2move= Math.round(dd *(bubbleWidthDP/2f/10f));
        return dp2move;
    }


    public int axis2px(float dd){
        //
        //  update bubble offset (in pixels) according to accelerometer reading
        //
        //       final int dp= (int)(dd *(160f/10f));
        final int dp= (int)(dd *(80f/10f));  // total dots / axis range (0-10.0)
        final int px= (int)((float)(dp)*scrDensity);
        return px;
    }

}
