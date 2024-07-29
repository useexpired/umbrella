#include "PCM16data.h"

void PCM16data::setFrames(int32_t numFrames){
    pcm16dataFrames_= (int) numFrames;
}

void PCM16data::setValue(float value, int i){

    float MAX=32768.0f;
    int16_t tmp = (MAX* value);
    int16_t data;

    //  confine values within -Max .. Max

    data= (tmp > -MAX ) ? tmp : -MAX;
    data = (tmp > (MAX - 1) ) ? MAX - 1 : tmp;

    pcm16data_[i]=data;
}

int16_t* PCM16data::getData() {
    return pcm16data_;
}
int16_t PCM16data::getDataAt(int i) {
    return pcm16data_[i];
}

int PCM16data::getSize() {
    return DATA_SIZE;
}
