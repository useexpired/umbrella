#include <string>
#include "DataTypeUtils.h"
using namespace std;

void DataTypeUtils::str2char(char *c, string s){
    strcpy(c, s.c_str());
}


char *DataTypeUtils::jStr2char(jstring js, JNIEnv* ev) {
    jboolean isCopy;
    return ((char *)(ev)->GetStringUTFChars(js, &isCopy));
}

string DataTypeUtils::char2str(char *carr) {
    string aPath = string(carr, strlen(carr));
    return aPath;
}

string DataTypeUtils::jStr2str(jstring js, JNIEnv* ev) {
    jboolean isCopy;
    return (char2str((char *)(ev)->GetStringUTFChars(js, &isCopy)));
}

jstring DataTypeUtils::str2jstr(string s, JNIEnv *ev) {
    return ev->NewStringUTF(s.c_str());
}

string DataTypeUtils::int2str(int i){
    string s="";
    return to_string(i);
}
