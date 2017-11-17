//
// Created by 11723 on 2017/11/17.
//
#include "com_nix_jpcap_JpcapCaptor.h"

/*
 * Class:     com_nix_jpcap_JpcapCaptor
 * Method:    getDeviceList
 * Signature: ()[Lcom/nix/jpcap/NetworkInterface;
 */
JNIEXPORT jobjectArray JNICALL Java_com_nix_jpcap_JpcapCaptor_getDeviceList(JNIEnv * env, jclass obj)
{
    return NULL;
}

/*
 * Class:     com_nix_jpcap_JpcapCaptor
 * Method:    setPacketReadTimeout
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_nix_jpcap_JpcapCaptor_setPacketReadTimeout(JNIEnv * env, jclass obj, jint time)
{
    return JNI_FALSE;
}

/*
 * Class:     com_nix_jpcap_JpcapCaptor
 * Method:    loopPacket
 * Signature: (ILcom/nix/jpcap/PacketReceiver;)I
 */
JNIEXPORT jint JNICALL Java_com_nix_jpcap_JpcapCaptor_loopPacket(JNIEnv * env, jclass obj, jint time, jobject packetReceiver)
{
    return 0;
}

/*
 * Class:     com_nix_jpcap_JpcapCaptor
 * Method:    nativeOpenLive
 * Signature: (Ljava/lang/String;III)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_nix_jpcap_JpcapCaptor_nativeOpenLive(JNIEnv * env, jclass obj, jstring intrface, jint snaplen, jint promisc, jint to_ms)
{
    return NULL;
}