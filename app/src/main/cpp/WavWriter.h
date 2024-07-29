#ifndef RANGEFINDER2022_WAVWRITER_H
#define RANGEFINDER2022_WAVWRITER_H
#include <iostream>
#include <fstream>
#include <cstdint>
#include "DataTypeUtils.h"
using namespace std;
/*

    This class create new .WAV file with header setting

*/
class WavWriter {
public:
    WavWriter();
    void setHeaderDefault();
    int writeHeader(ofstream& oStream);
    int setSampleRate(int sampleRate);
    int setNumBytes(int sampleRate);
    int getNumBytes();
    string headerReport();

private :
    short mFormat;
    short mNumChannels;
    int mSampleRate;
    short mBitsPerSample;
    int mNumBytes;
    DataTypeUtils dataTypeUtils;

private :
    void writeId(ofstream& oStream, string id);
    void writeInt(ofstream& oStream, uint32_t num);
    void writeShort(ofstream& oStream, short mFormat);
};

#endif //RANGEFINDER2022_WAVWRITER_H
