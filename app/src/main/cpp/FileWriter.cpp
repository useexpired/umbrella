#include "FileWriter.h"
using namespace std;

//  debug only
#include <android/log.h>
#define TAG "FILEWRITER"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,     TAG, __VA_ARGS__)

int FileWriter::debugReportBufSize(){  return debugByteSize_; }
int FileWriter::debugReportWriteCount(){ return debugWriteCount_; }

void FileWriter::openStream(){
    LOGI("file will saved as %s ", &pcmFilePath_[0]);
    fileOut_.open(pcmFilePath_);
}

void FileWriter::closeStream(){
    fileOut_.close();
    LOGI("********* %s succeed in closure", &pcmFilePath_[0]);
}

bool FileWriter::isOpen(){
    return (fileOut_.is_open());
}

void FileWriter::writeWavHeader(int sampleRate, int numBytes){
    wavWriter.setNumBytes(numBytes);
    wavWriter.setSampleRate(sampleRate);
    wavWriter.writeHeader(fileOut_);
}

void FileWriter::writeWavHeader(int sampleRate) {
    wavWriter.setSampleRate(sampleRate);
    wavWriter.writeHeader(fileOut_);
}

void FileWriter::writeWavHeader(){
    wavWriter.writeHeader(fileOut_);
}

void FileWriter::writePCMdata(char *bytePCM, int bytePCMsize){

    if (fileOut_.is_open()) {// write audio PCM content

        fileOut_.write((char *)&bytePCM[0], bytePCMsize);

        if(debugByteSize_ == -1){
            debugByteSize_ = bytePCMsize;
        }
        debugWriteCount_++;
      }

}

void FileWriter::setFilePath(string filePathNname) {
    pcmFilePath_ =filePathNname;
}

bool FileWriter::overwriteWavChunkTotal(string filePathName, int32_t numFrames){

    fstream wavFile;
    bool succeed=false;

    wavFile.open(filePathName, ios::binary|ios::in|ios::out);

    if (wavFile.is_open()){

        succeed=true;
        char buf[4];

        wavFile.seekp(40);
        int32toChar(buf, numFrames);
        wavFile.write(buf,4);

        numFrames +=36;
        wavFile.seekp(4);
        int32toChar(buf, numFrames);
        wavFile.write(buf,4);
    }
    wavFile.close();
    return succeed;
}

void FileWriter::int32toChar(char *buf, int32_t numFrames){
    uint32_t uval = numFrames;
    buf[0] = uval;
    buf[1] = uval >> 8;
    buf[2] = uval >> 16;
    buf[3] = uval >> 24;
}



