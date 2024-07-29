package com.rangefinder2022;

public class DistCSV {
    //
    //  CSV file of distances measured by LIDAR sensor
    //
    String timeStamp_;      // start time mmdd_HH:mm:ss
    int interval_ms_;       // interval in milliseconds (default 100ms)
    float[] distance_;      // distance (2bytes)
    int total_;             // total distance_
}
