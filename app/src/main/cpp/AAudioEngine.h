#ifndef RANGEFINDER2022_AAUDIOENGINE_H
#define RANGEFINDER2022_AAUDIOENGINE_H
#include <aaudio/AAudio.h>
#include "Oscillator.h"

class AAudioEngine {
public:
    bool start(float frequency);
    void stop();
    void restart();
    void setToneOn(bool isToneOn);
    void setFreqRange(int32_t freqMin, int32_t freqMax);
    void setFrequency(float freq);
    void setWaveForm(int32_t waveform);
    //
    //  for PCMrecorder
    //
    void startRecord(string filePath);
    void stopRecord();
private:
    Oscillator oscillator_;
    AAudioStream *stream_;
    int32_t engineFreq_;
    //
    // for PCM recorder
    //
    bool isEngineRecord=false;
    int sampleRate4pcmRecorderOnly=44000;
};

#endif //RANGEFINDER2022_AAUDIOENGINE_H
