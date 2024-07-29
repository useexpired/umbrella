//
// Created by matthew on 2021-05-16.
//
#ifndef RANGEFINDER2022_OSCILLATOR_H
#define RANGEFINDER2022_OSCILLATOR_H

#include <atomic>
#include <stdint.h>
#include <math.h>
#include "DataModel.h"
#include "PCMrecorder.h"

class Oscillator {

public:

    PCMrecorder PCMrecorder_;
    // officially not suggested to run fileIO within AAudiostream.dataCallback
    // let see if it causes glitches with this simple app

    void setWaveOn(bool isWaveOn);
    void setParams(int32_t sampleRate, float frequency);
    void setFreqRange(int32_t freqMin, int32_t freqMax);

    void setWaveForm(int32_t waveForm0123);
    int32_t getWaveForm();

    void render(float *audioData, int32_t numFrames);
    void renderWaveForm(float *audioData, int32_t numFrames, int32_t theWave);

    void sine(float *audioData, int32_t numFrames);
    void triangle(float *audioData, int32_t numFrames);
    void sawTooth(float *audioData, int32_t numFrames);
    void square(float *audioData, int32_t numFrames);

private:

    std::atomic<bool> isWaveOn_{false};

    double phase_ = 0.0;
    double phaseIncrement_ = 0.0;
    int sampleRate_;
    double frequency_ =0.0;
    double amplitude_=0.3;  // 0.3 in other demo
    //
    // keep track of previous render() info
    // EXCLUSIVELY for triangle bug
    //
    int32_t waveForm_;
    std::atomic<int> waveEnumPrev_{0};
    std::atomic<bool> isPositiveIncPrev_{false};

    //int oscID_ =0;
     /*
     * WaveForms waveForm=sine;
     */
    //
    //  when oscillator create waveform, values in *audioData is resampled to
    //  pcm16data, and pcm16dataFrames is numFrames of *audioData
    //
//    int16_t pcm16data_[65536];
//    int pcm16dataFrames_;
    //
    //  sorry need more time for c++ basics
    //  DataModel dataModel;
    //
   // int32_t oscFreqMin_, oscFreqMax_, oscFreq_;
    //int32_t oscSampleRate_;
    //int32_t numFramesDebug=0;

};

#endif //RANGEFINDER2022_OSCILLATOR_H
