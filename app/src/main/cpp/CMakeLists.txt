cmake_minimum_required(VERSION 3.10.2)
project("rangefinder2022")
add_library(
        jni-lib
        SHARED
        native-lib.cpp
        AAudioEngine.cpp
        Oscillator.cpp
        PCMrecorder.cpp
        PCM16data.cpp
        FileWriter.cpp
        DataTypeUtils.cpp
        DataModel.cpp

)
find_library(
        log-lib
        log )
target_link_libraries(
        jni-lib
        ${log-lib}
        aaudio)

cmake_minimum_required(VERSION 3.10.2)
