#include <__locale>
#include <jni.h>
#include <string.h>
#include <thread>
#include "PCMrecorder.h"

//  debug only
using namespace std;

//#include <android/log.h>
//#define TAG "GLITCH_TEST"
//#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,     TAG, __VA_ARGS__)
//
//  there are glitches when audio is being recorded
//  my guess is that the thread here itself is slowing than the oscillator and thus caused the glitches
//
//  glitches exist when no file I/O
//  which means this class is most probably the source of glitches
//
//  less glitches after all LOGI are commented
//
//
//  solution?
//
//  calls Class methods normally without using thread()
//
//
void PCMrecorder::startRecord(string filePathNname, int sampleRate) {
    isRecordin_=true;
    //LOGI("startRecord() called");
    //LOGI("startRecord() file 2 write=%s", &getFileName()[0]); // string converted to char *

    setFileName(filePathNname);
    setSampleRate(sampleRate);

    fileWriter_.setFilePath(getFileName());
    fileWriter_.openStream();
    fileWriter_.writeWavHeader(sampleRate);

    //LOGI("721831 %s file opened", &getFileName()[0]);
    //
    // if( reinit.causeLatency() ){
    //      @write() set value to zero if ( !isRecording())
    // }else{
    //     reset arrPCMp[] to 0s
    //}
    //
}

void PCMrecorder::pack4wav(float *audioFloatData, int32_t numFrames){
    if( isRecording()) {
        addRecFramesTotal(numFrames);
        write(numFrames);
    }else{
            if(fileWriter_.isOpen()){
                fileWriter_.closeStream();
                fileWriter_.overwriteWavChunkTotal(getFileName(), getWavChunkTotal());
            }
    }
}

void PCMrecorder::write(int32_t numFrames){
    //
    //   no thread, no threats to Oscillator ?
    //
    //   tested with ZERO code within the thread, and still there are occasional glitches
    //   every 4-8 seconds
    //
    //   If calling the same method threadPreparePCMfileData as regular method without
    //   the use of thread, obviously ZERO glitches occur
    /*
     * thread thread1 (&PCMrecorder::threadPreparePCMfileData,
                     this,
                     audioPcm16data, numFrames);
    */
   // threadPreparePCMfileData( audioPcm16data, numFrames);

   byteSplitter(numFrames);
    fileWriter_.writePCMdata(bytePCM_, numFrames * 2);

    //thread1.join();
}
//
//  prepare bytePCM[] for output audio file
//
void PCMrecorder::byteSplitter(int32_t numFrames){

    char byteLow, byteHigh;
    int16_t data2bytes; // 16bit signed integer

    //  LOGI("file written [%d]__________________",fileCount);

    int byteIdx=0;

    for(int i=0; i < numFrames; i++){
        //
        //      prepare PCM file format :
        //      convert 1-byte char to 16bit PCM 2 bytes format
        //
        data2bytes=PCM16data_.getDataAt(i);
        byteLow = (unsigned char) (data2bytes & 0x00ff);
        byteHigh = (unsigned char) ((data2bytes & 0xff00) >> 8);
        // byte splitted
        if(byteIdx < (PCM_BYTE_ARR_SIZE * 2)){
            bytePCM_[byteIdx++]=byteLow;
            bytePCM_[byteIdx++]=byteHigh;
        }
        //LOGI("[%d] pcm16= %d resampled=%d  b4=%d",i, audioPCM16data[i], dataResampled, tmp);
    }
}


void PCMrecorder::setFileName(string str){
   filePathName_=str;
}
string PCMrecorder::getFileName(){
    return filePathName_;
}


bool PCMrecorder::isRecording() {
     if(isRecordin_) return true; else return false;
}
void PCMrecorder::stopRecord() {
    //
    // right now the algorithm is
    // the saved arrPCM buffer are resampled and write to file
    // only when user press the stop button.
    //
    // But what should happen is
    // when arrPCM buffer once are all filled,
    // its pcm data should be sent to a thread to be resampled
    // and and write to file stream
    //
    isRecordin_=false;
    //LOGI("stopRecord() arrPCM total=%d", pcmArrMax);
    //
    // do not close file here
    //
    // let the thread() performs the last resampling.
    // After the thread.join(), close the file if the "isRecord" flag is on
    //
}

void PCMrecorder::addRecFramesTotal(int framesNew) {
    recFramesTotal_+=framesNew;
}
int32_t PCMrecorder::getRecFramesTotal() {
    return (int32_t) recFramesTotal_;
}
int PCMrecorder::getWavChunkTotal(){
    return (recFramesTotal_ * 2);
}
void PCMrecorder::setSampleRate(int rate){
    sampleRate_=rate;
}
int32_t PCMrecorder::getSampleRate(){
    return sampleRate_;
}