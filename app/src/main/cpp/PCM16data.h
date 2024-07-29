#ifndef RANGEFINDER2022_PCM16DATA_H
#define RANGEFINDER2022_PCM16DATA_H
#include <stdint.h>
#define DATA_SIZE 65536

class PCM16data {
public:
    void setFrames(int32_t numFrames);
    void setValue(float value, int i);
    int16_t* getData();
    int16_t getDataAt(int i);
    int getSize();
private:
    int16_t pcm16data_[DATA_SIZE];
    int pcm16dataFrames_;
};


#endif //RANGEFINDER2022_PCM16DATA_H
