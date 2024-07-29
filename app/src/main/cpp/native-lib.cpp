#include <jni.h>
#include <android/input.h>
#include <android/log.h>
#include <string.h>
#include "AAudioEngine.h"

static AAudioEngine *aaudioEngine = new AAudioEngine();
bool engineRunOnce = false;

extern "C" {
JNIEXPORT void JNICALL
Java_com_proj2021sspaaudio5_MainActivity_updateAAudioFreq(JNIEnv *env, jobject obj,
                                          float freq) {
    if (engineRunOnce) {
        aaudioEngine->setFrequency(freq);
    }
}


JNIEXPORT void JNICALL
Java_com_proj2021sspaaudio5_MainActivity_syncNativeFreqRange(
        JNIEnv *env, jobject obj,
        int freqFrom, int freqTo) {
    aaudioEngine->setFreqRange(freqFrom, freqTo);
}

JNIEXPORT void JNICALL
Java_com_proj2021sspaaudio5_MainActivity_updateWaveFormNative(JNIEnv *env, jobject obj,
                                              int waveEnum) {
    if (engineRunOnce) {
        aaudioEngine->setWaveForm(waveEnum);
    }
}

JNIEXPORT void JNICALL
Java_com_proj2021sspaaudio5_MainActivity_aaudioEngineStart(JNIEnv *env,  jobject obj) {

    aaudioEngine->setToneOn(true);
    if (!engineRunOnce) {
        aaudioEngine->start(240.0f);
    }
    engineRunOnce = true;
}


JNIEXPORT void JNICALL
Java_com_proj2021sspaaudio5_MainActivity_aaudioEngineStop(JNIEnv  *env, jobject obj /* this */) {
    aaudioEngine->setToneOn(false); // aaudio stream still on but byte array zero out
}

JNIEXPORT void JNICALL
Java_com_proj2021sspaaudio5_RecordEngine_PCMrecorderStart(JNIEnv *env,
                              jobject obj,
                              jstring fileFolder) {
    //
    //  conversion before passing filePath to stopRecord()
    //
    //  jstring folder path (which could contain utf8 code) is converted to
    //  UTF-8 compatible C++ string
    //
    jboolean isCopy;
    const char *strUTF = (env)->GetStringUTFChars(fileFolder, &isCopy);
    std::string strPass = std::string(strUTF, strlen(strUTF));
    aaudioEngine->startRecord(strPass);
}

JNIEXPORT void JNICALL
Java_com_proj2021sspaaudio5_RecordEngine_PCMrecorderStop( JNIEnv *env,
                              jobject obj /* this */) {
    aaudioEngine->stopRecord();
}

}
extern "C"
JNIEXPORT void JNICALL
Java_com_rangefinder2022_MainActivity_updateAAudioFreq(JNIEnv *env, jobject thiz,
                                                       float frequency) {
    // TODO: implement updateAAudioFreq()
}