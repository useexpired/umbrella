#ifndef RANGEFINDER2022_PCMRECORDER_H
#define RANGEFINDER2022_PCMRECORDER_H
#include <atomic>
#include <stdint.h>
#include <math.h>
#include "PCM16data.h"
#include "FileWriter.h"
#define BYTEPCM_SIZE 65536

class PCMrecorder {

public:

    PCM16data   PCM16data_;

    void pack4wav(float *audioFloatData, int32_t numFrames);
    void write(int32_t numFrames);
    void byteSplitter(int32_t numFrames);

    void startRecord(string fileFolderPath, int sampleRate);
    void stopRecord();

    bool isRecording();

    void addRecFramesTotal(int framesNew);
    int getRecFramesTotal();
    int getWavChunkTotal();
    void setFileName(string str);
    string getFileName();

    void setSampleRate(int rate);
    int32_t getSampleRate();

    //void setPCMsize(int size);
    //int getPCMsize();

    const int PCM_BYTE_ARR_SIZE=(65536/2);

private:
    //
    //
    // after resampling, values are 16-bit signed integer
    // which is -32,768 (2^15* -1) to +32767 (2^15-1)
    //
    // bytePCM is a byte Array of unsigned characters.
    // 2 bytes are used to contain each value of 16bit signed integer.
    //
    FileWriter  fileWriter_;

    char bytePCM_[BYTEPCM_SIZE]; // unsigned character here is a character array

    int bytePCMsize; // size of bytePCM[]

    std::atomic<bool>isRecordin_{false};
    int32_t recFramesTotal_{0};
    string filePathName_="";
    int32_t sampleRate_=0;

};


#endif //RANGEFINDER2022_PCMRECORDER_H
