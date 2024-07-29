#include <android/log.h>
#include "AAudioEngine.h"
#include <thread>
#include <mutex>
//
// Double-buffering offers a good tradeoff
// between latency and protection against glitches.
//
constexpr int32_t kBufferSizeInBursts = 2;

aaudio_data_callback_result_t dataCallback(
        AAudioStream *stream,
        void *userData,
        void *audioData,
        int32_t numFrames) {

     ((Oscillator *) (userData))->render(static_cast<float *>(audioData),
                                        numFrames);

    return AAUDIO_CALLBACK_RESULT_CONTINUE;
}

void errorCallback(AAudioStream *stream,
                   void *userData,
                   aaudio_result_t error){
    if (error == AAUDIO_ERROR_DISCONNECTED){
        std::function<void(void)> restartFunction = std::bind(&AAudioEngine::restart,
                                                              static_cast<AAudioEngine *>(userData));
        new std::thread(restartFunction);
    }
}

void AAudioEngine ::startRecord(string filePathNname){
    isEngineRecord=true;
    oscillator_.PCMrecorder_.startRecord(filePathNname, sampleRate4pcmRecorderOnly);
}

void AAudioEngine ::stopRecord(){
    isEngineRecord=false;
    oscillator_.PCMrecorder_.stopRecord();
}

bool AAudioEngine::start(float freq) {

    AAudioStreamBuilder *streamBuilder;
    AAudio_createStreamBuilder(&streamBuilder);
    AAudioStreamBuilder_setFormat(streamBuilder, AAUDIO_FORMAT_PCM_FLOAT);
    AAudioStreamBuilder_setChannelCount(streamBuilder, 1);
    AAudioStreamBuilder_setPerformanceMode(streamBuilder, AAUDIO_PERFORMANCE_MODE_LOW_LATENCY);
    AAudioStreamBuilder_setDataCallback(streamBuilder, ::dataCallback, &oscillator_);
    AAudioStreamBuilder_setErrorCallback(streamBuilder, ::errorCallback, this);

    // Opens the stream.
    aaudio_result_t result = AAudioStreamBuilder_openStream(streamBuilder, &stream_);
    if (result != AAUDIO_OK) {
        __android_log_print(ANDROID_LOG_ERROR, "AudioEngine", "Error opening stream %s",
                            AAudio_convertResultToText(result));
        return false;
    }

    // Retrieves the sample rate of the stream for our oscillator.
    sampleRate4pcmRecorderOnly= AAudioStream_getSampleRate(stream_);
    int32_t sampleRate =sampleRate4pcmRecorderOnly;
    oscillator_.setParams(sampleRate, freq);

    // Sets the buffer size.
    AAudioStream_setBufferSizeInFrames(
            stream_, AAudioStream_getFramesPerBurst(stream_) * kBufferSizeInBursts);

    // Starts the stream.
    result = AAudioStream_requestStart(stream_);
    if (result != AAUDIO_OK) {
        __android_log_print(ANDROID_LOG_ERROR, "AudioEngine", "Error starting stream %s",
                            "fuck");//AAudio_convertResultToText(result));
        return false;
    }

    AAudioStreamBuilder_delete(streamBuilder);
    return true;
}

void AAudioEngine::restart(){

    static std::mutex restartingLock;
    if (restartingLock.try_lock()){
        stop();
        start(engineFreq_);
        restartingLock.unlock();
    }
}


void AAudioEngine::stop() {
    aaudio_result_t result;

    if (stream_ != nullptr) {

        AAudioStream_requestStop(stream_);
        result = AAudioStream_close(stream_);

        if (result != AAUDIO_OK) {
            __android_log_print(ANDROID_LOG_ERROR, "AudioEngine",
                                "Error closing stream %s", "fuck");
        }
    }
}


void AAudioEngine::setToneOn(bool isToneOn) {
    oscillator_.setWaveOn(isToneOn);
}
void AAudioEngine::setFrequency(float freq) {
    engineFreq_=freq;
    oscillator_.setParams(
            AAudioStream_getSampleRate(stream_),
            engineFreq_);
}

void AAudioEngine::setFreqRange(int32_t freqFm, int32_t freqTo) {
    oscillator_.setFreqRange(freqFm, freqTo);
}

void AAudioEngine::setWaveForm(int32_t waveEnum){
    oscillator_.setWaveForm(waveEnum);
}

