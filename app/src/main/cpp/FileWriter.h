#ifndef RANGEFINDER2022_FILEWRITER_H
#define RANGEFINDER2022_FILEWRITER_H
#include <iostream>
#include <fstream>
#include <string>
#include <cstdint>
#include "WavWriter.h"
using namespace std;

class FileWriter {
public:
    void setFilePath( string filePath);
    void openStream();
    void writePCMdata(char *bytePCM, int bytePCMsize);
    void closeStream();

    void writeWavHeader(int sampleRate, int numBytes);
    void writeWavHeader(int sampleRate);
    void writeWavHeader();

    bool overwriteWavChunkTotal(string filePathName, int32_t numFrames);
    void int32toChar(char* buf, int32_t numFrames); // dirty char array address, huh ?

    bool isOpen();

    int debugReportBufSize();
    int debugReportWriteCount();

    WavWriter wavWriter;

private:
    string pcmFilePath_="hello.txt";
    int dumb=100;
    ofstream fileOut_;
    //
    //  debug stats only
    //
    int debugByteSize_=-1;
    int debugWriteCount_=0;
};

#endif //PRANGEFINDER2022_FILEWRITER_H


