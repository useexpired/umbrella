#include "WavWriter.h"
#include "DataTypeUtils.h"
using namespace std;
#define HEADER_LENGTH 44;
#define WAV_FORMAT 1;
#define SAMPLE_RATE 48000;

WavWriter::WavWriter(){
    setHeaderDefault();
}
void WavWriter::setHeaderDefault(){

    mFormat=WAV_FORMAT; // WAV not AIFF or others
    mNumChannels=1;
    mSampleRate=SAMPLE_RATE;
    mBitsPerSample=16;
    mNumBytes=0;
    //
    // Byte Rate
    //      default = 96,000 = (1* 48,000 * 2) 0x00017700 bytes(00 07 01 00)
    //
    // Block Align - (mNumChannels * mBitsPerSample / 8)
    //      default = 2 = 1 * 16 / 8 bytes(02 00)
    //
}

int WavWriter::writeHeader(ofstream& out){

    // RIFF header

    writeId(out, "RIFF");
    writeInt(out, 36 + mNumBytes);
    writeId(out, "WAVE");

    // fmt sub-chunk
    //  int 32 bits, short 16 bits

    writeId(out, "fmt ");
    writeInt(out, 16);
    writeShort(out, mFormat);
    writeShort(out, mNumChannels);
    writeInt(out, mSampleRate);
    writeInt(out, mNumChannels * mSampleRate * mBitsPerSample / 8); // Byte Rate
    writeShort(out, (short) (mNumChannels * mBitsPerSample / 8)); // Block Align
    writeShort(out, mBitsPerSample);

    // data sub-chunk

    writeId(out, "data");
    writeInt(out, mNumBytes);

    return HEADER_LENGTH;
}
void WavWriter::writeId(ofstream& out, string strID) {

    char id[sizeof(strID)];
    dataTypeUtils.str2char(id, strID);
    for (int i = 0; i < 4; i++) out.write(&id[i],1);
}

void WavWriter::writeInt(ofstream& out, uint32_t val) {
    uint32_t v1;
    v1= val >> 0;    out.write((char*)&v1,1);
    v1= val >> 8;    out.write((char*)&v1,1);
    v1= val >> 16;    out.write((char*)&v1,1);
    v1= val >> 24;    out.write((char*)&v1,1);
}

void WavWriter::writeShort(ofstream& out, short val) {
    uint32_t v1;
    v1= val >> 0;    out.write((char*)&v1,1);
    v1= val >> 8;   out.write((char*)&v1,1);
}

int WavWriter::setSampleRate(int sampleRate) {
    mSampleRate = sampleRate;
    return sampleRate;
}
int WavWriter::setNumBytes(int numBytes) {
    mNumBytes = numBytes;
    return numBytes;
}
int WavWriter::getNumBytes() { return mNumBytes;}

