# Android APP for Art Project "Umbrella"
 (2020-2024) created by Qohtaiwoo

## OBJECTIVE

When one walks pass any Tong Lau (唐樓, pre-WWII residential buildings) in Hong Kong, a device is used to measure the distance between one's own body and the ceilings of
erected portion which constructs the canopy of the footpath above one's head. The hardware solution provided here is to use the manufactured range finder which provides API for smartphones.
The custom APP then transforms the live measured data into audio expression based on spatial experiences. 

<img src="https://i.ibb.co/s6hPfV3/illustrate-wav.jpg" width="400" border="0">

## The APP
 
<img src="https://i.ibb.co/NsGNQdx/app-screenshot.jpg" width="250" border="0">

_APP screenshot_

||  |
|---|--------------- |
|1. | sensor on/off |
|2. | detected distance|
|3. | bubble level|
|4. | bubble level on/off |
|5. | wavelength frequency gain |
|6. | wave form |
|7. | generated wavelength frequency range |
|8. | detected distance range |
|9. | maximum distance |
|10. | live frequency wavelength |
|11. | sound recorder |
|12. | message log |


### A. Distance Sensor _(1)(2)_

Terabee Distance sensor is connected to the smartphone via USB cable. The tested sensor has a maximum range of 60 meter. Personal observation concluded that the height of the extended roof of Chinese tenements in Hong Kong is below 12 meters.

### B. Bubble Level _(3)(4)_

As the user needs to walk in a constant pace to operate the app, the bubble level is to guide the user to keep phone lay as flat as possible in order to acquire relatively accurate data. The bubble level is based on internal built-in accelerometer of Android smartphones. 

### C. Sonic Input / Output Parameters _(7)(8)(9),(6)_

Live distance data feed whose range of distance (in meters) is estimated in advance at sites.
The range of distance will be corresponded straightforwardly to desirable range of sound determined solely by its wavelength.
The waveform has the options of sine, triangular, sawtooth and pulse. Users could change the option by touching on the waveform image _(6)_.

### D. Recording _(11)(7)_

Two files are created when the reocording feature is invoked, which
are accessible at _/android/data/[project-path]/files/audio/_ :

### 1. .wav audio file _(7)(8)(5)_
>This is a simple audio file in uncompressed .wav format which was recorded by the APP
>  
>Filename example:
>_aud211130_081256d0106f00402500g3.wav_
>
>|aud| 211130_081256|d|0106|f|00402500|g|3|.wav |
>|---|--------------|-|----|-|---------|-|-|---- |
>|   | 2021 Nov 30 8:12:56|distance|1-6m|frequency|40 - 2580Hz|amplitude gain|x3| |
>
### 2. .csv file  
>The csv file contains distance data from live feed via distance sensor.
>   
>Filename example:
>_dis211130_081256.csv_
>
>|dis| 211130_081256|.csv |
>|---|--------------|---- |
>|   | 2021 Nov 30 8:12:56| |
>   
>Regardless of data bandwidth, distance in meter will be constantly
>recorded 600 times per minute.

>Data format of the csv  is simply as follows:
>   
>|line| value |
>|---|---------- |
>| 1  | Creation Timestamp (hhmm_hhssmm) |
>| 2  |  distance 1 |   
>| 3  |  distance 2 |   
>| 4  |  ... |   
>   
>Heights of the roof of different Chinese tenements along the pedestrian pathways could thus be visualized
>as distance data could be remapped, easily by Google Sheets, as chart like the following:
>
>#### Example Visualized Distance Chart (based on .csv file)
><img src="https://i.ibb.co/R4DTWF4/sketch-1662554043749.jpg" height="250">
>
>


## Tested Platform and Hardware

Operating System: Android 8.0.0

Distance Sensor : Terabee TeraRange Evo 60m

## References

>[AAudio Engine](https://developer.android.com/ndk/guides/audio/aaudio/aaudio)

>[.WAV Soundfile Format ](http://soundfile.sapp.org/doc/WaveFormat/)

>[TeraRanger](https://www.terabee.com/shop/lidar-tof-range-finders/teraranger-evo-60m/)




