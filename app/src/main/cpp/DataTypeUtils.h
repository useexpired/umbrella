#ifndef RANGEFINDER2022_DATATYPEUTILS_H
#define RANGEFINDER2022_DATATYPEUTILS_H

#include <string>
#include <jni.h>
using namespace std;
class DataTypeUtils {

public:
    void    str2char(char *c, string s);
    string  char2str(char *chr);
    char    *jStr2char(jstring s, JNIEnv* jenv);
    string  jStr2str(jstring s, JNIEnv* jenv);
    jstring str2jstr(string s, JNIEnv* jenv);
    string  int2str(int n);
};

#endif //RANGEFINDER2022_DATATYPEUTILS_H
