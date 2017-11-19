cmake_minimum_required(VERSION 3.8.2)
project(jpcap)
set(BUILD_USE_64BITS on)
set(CMAKE_CXX_STANDARD 11)
#添加头文件搜索路径
include_directories(C:/WpdPack/include)
#添加库文件搜索路径

link_directories(C:\\WpdPack\\Lib\\x64)

set(LIBHELLO_SRC jni_md.h jni.h com_nix_jpcap_JpcapSender.h com_nix_jpcap_JpcapSender.cpp C:/WpdPack/include)

add_library(jpcap SHARED  ${LIBHELLO_SRC})
target_link_libraries(jpcap wpcap Packet)