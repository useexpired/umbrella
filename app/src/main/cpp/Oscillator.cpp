#include <__locale>
#include <jni.h>
#include <string.h>
#include "Oscillator.h"

#define TWO_PI (3.14159 * 2)
#define TAG "OSCILLATOR"
/*
        There are 2 sets of waveform generators
        1. createTriangle(...)
        2. createPhaseTriangle(...)

        The array size (numFrames) of audioData[] buffer differs in actual running

          variables numFrames 64,128,192
          sampleRate 48,000

        The createPhaseTri...(...) would fix the glitch caused in createTriangle(...)
*/

//  debug only
using namespace std;
//#include <android/log.h>
//#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,     TAG, __VA_ARGS__)

/*
void Oscillator::setFrames(int32_t numFrames){
    pcm16dataFrames_= (int) numFrames;
}*/
/*
void Oscillator::setValue(float value, int i){

    float MAX=32768.0f;
    int16_t tmp = (MAX* value);
    int16_t dataResampled;
    //
    //  shorten value to within the range of -Max .. Max
    //
    dataResampled= ( tmp > -MAX ) ? tmp : -MAX;
    dataResampled = ( tmp > (MAX-1) ) ? MAX-1 : tmp;
    pcm16data_[i]=dataResampled;
}*/

void Oscillator::setParams(int32_t sampleRateIn, float frequency) {
    sampleRate_=sampleRateIn;
    frequency_ = frequency;
    //resetID();
}

void Oscillator::render(float *audioData, int32_t numFrames){

    PCMrecorder_.PCM16data_.setFrames(numFrames); // byteSplitter and file output
    renderWaveForm(audioData, numFrames, getWaveForm());
    PCMrecorder_.pack4wav(audioData, numFrames);
}

void Oscillator::renderWaveForm(float *audioData, int32_t numFrames, int32_t waveEnum) {

    switch(waveEnum){
        case 0:  Oscillator::sine(audioData, numFrames); break;
        case 1:  Oscillator::triangle(audioData, numFrames); break;
        case 2:  Oscillator::sawTooth(audioData, numFrames); break;
        case 3:  Oscillator::square(audioData, numFrames); break;
    }
    waveEnumPrev_=waveEnum;
}


void Oscillator::sine(float *audioData, int32_t numFrames) {

    if (!isWaveOn_.load())   phase_ = 0;

    double ampAdjusted=amplitude_* 2.66;  // 0.8=0.3 * 2.66
    phaseIncrement_ = (TWO_PI *  frequency_  ) / (double) sampleRate_;

    for (int i = 0; i < numFrames; i++) {
        if (isWaveOn_.load()){
            audioData[i] = (float) (sin(phase_) * ampAdjusted);
            phase_ += phaseIncrement_;
            phase_ = (phase_ > TWO_PI) ? phase_ - TWO_PI : phase_;
        } else {
            audioData[i] = 0; // mute the output
        }
        PCMrecorder_.PCM16data_.setValue(audioData[i], i);
    }
}


void Oscillator::triangle(float *audioData, int32_t numFrames) {

    if (!isWaveOn_.load())   phase_ = 0;

    phaseIncrement_ = (TWO_PI *  frequency_  ) / (double) sampleRate_;

    for (int i = 0; i < numFrames; i++) {
        if (isWaveOn_.load()){
            audioData[i]=(float)(asin(cos(phase_))/1.5708 );
            //audioData[i] = (float) (sin(phase_) * AMPLITUDE_);
            phase_ += phaseIncrement_;
            phase_ = (phase_ > TWO_PI) ? phase_ - TWO_PI : phase_;
        } else {
            audioData[i] = 0;
        }
        PCMrecorder_.PCM16data_.setValue(audioData[i], i);
         
    }
}


void Oscillator::sawTooth(float *audioData, int32_t numFrames) {

    if (!isWaveOn_.load())   phase_ = 0;
    
    double ampAdjusted= amplitude_ * 0.8;
    phaseIncrement_ = (TWO_PI *  frequency_  ) / (double) sampleRate_;

    for (int i = 0; i < numFrames; i++) {
        if (isWaveOn_.load()){
            audioData[i] = fmod(phase_,1) * ampAdjusted;
            // fmod(float p,q)  does not return consistent values at online compiler
            phase_ += phaseIncrement_;
            phase_ = (phase_ > TWO_PI) ? phase_ - TWO_PI : phase_;
        } else {
            audioData[i] = 0;
        }
        PCMrecorder_.PCM16data_.setValue(audioData[i], i);
    }
}



void Oscillator::square(float *audioData, int32_t numFrames) {

    if (!isWaveOn_.load())   phase_ = 0;
    const double ampAdjusted= amplitude_ * 0.6;
    phaseIncrement_ = (TWO_PI *  frequency_  ) / (double) sampleRate_;
    const double MAX_AMP=ampAdjusted;

    for (int i = 0; i < numFrames; i++) {
        if (isWaveOn_.load()){

            float value= (float) (sin(phase_)); // * ampAdjusted);
            audioData[i] = (value < 0)? -MAX_AMP : MAX_AMP;

            phase_ += phaseIncrement_;
            phase_ = (phase_ > TWO_PI) ? phase_ - TWO_PI : phase_;
        } else {
            audioData[i] = 0;
        }
        PCMrecorder_.PCM16data_.setValue(audioData[i], i);
    }
}
void Oscillator::setFreqRange(int32_t freqMin, int32_t freqMax){
    // never used
    //oscFreqMin_=freqMin;
    //oscFreqMax_=freqMax;
}

void Oscillator::setWaveOn(bool isWaveOn) {
    isWaveOn_.store(isWaveOn);
}

void Oscillator::setWaveForm(int32_t waveEnum) {
    waveForm_ = waveEnum;
}

int32_t Oscillator::getWaveForm() {
    return waveForm_;
}