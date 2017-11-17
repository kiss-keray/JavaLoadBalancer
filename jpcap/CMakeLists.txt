cmake_minimum_required(VERSION 3.9)
project(jpcap C)

set(CMAKE_C_STANDARD 99)

add_library(jpcap com_nix_jpcap_JpcapCaptor.c com_nix_jpcap_JpcapCaptor.h jni_md.h jni.h)