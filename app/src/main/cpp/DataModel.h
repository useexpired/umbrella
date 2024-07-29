#ifndef RANGEFINDER2022_DATAMODEL_H
#define RANGEFINDER2022_DATAMODEL_H

#include <stdint.h>

class DataModel {
public:
    /*
    void setFreqRange(int32_t min, int32_t max);
    int32_t getFreqMax();
    int32_t getFreqMin();

    int32_t getFrequencyNow();
    void setFrequencyNow(int32_t freq);

    int32_t getSampleRate();
    void setSampleRate(int32_t sampleRate);
     */

private:
    enum WaveForms { sine,triangle, sawtooth, square };
    /*
    int32_t freqMin_;
    int32_t freqMax_;
    int32_t frequency_;

    int32_t sampleRate_;
    */
};

#endif //RANGEFINDER2022_DATAMODEL_H
