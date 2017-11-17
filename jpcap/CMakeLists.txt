cmake_minimum_required(VERSION 3.9)
project(jpcap)

set(CMAKE_CXX_STANDARD 11)
##添加头文件搜索路径
include_directories(D:/WpdPack/include)
##添加库文件搜索路径
link_directories(D:/WpdPack/Lib)
#
set(TARGET_LIBS
        "D:/WpdPack/Lib/libpacket.a"
        "D:/WpdPack/Lib/libwpcap.a")
add_library(jpcap SHARED library.cpp library.h jni_md.h jni.h com_nix_jpcap_JpcapCaptor.h com_nix_jpcap_JpcapCaptor.c)