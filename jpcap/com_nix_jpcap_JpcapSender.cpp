//
// Created by 11723 on 2017/11/17.
//

#include "com_nix_jpcap_JpcapSender.h"
#include "pcap.h"
#pragma comment(lib,"wpcap.lib")
#pragma comment(lib,"ws2_32.lib")
pcap_t *fp;
/*
 * Class:     com_nix_jpcap_JpcapSender
 * Method:    nativeOpenDevice
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_nix_jpcap_JpcapSender_nativeOpenDevice
        (JNIEnv * env, jobject obj, jstring network) {
    char errbuf[256];
//    struct pcap_if * alldevs;//存储网卡信息
//
//    if (pcap_findalldevs(&alldevs, errbuf) == -1){
//        return env->NewStringUTF("获取网卡列表失败");
//    }
    jboolean b = 1;
    const char * name = env->GetStringUTFChars(network,&b);
    if ( (fp= pcap_open_live(name, 65535, 0, 1000, errbuf
    ) ) == NULL){
        return env->NewStringUTF("打开失败");
    }
    return NULL;
}

/*
 * Class:     com_nix_jpcap_JpcapSender
 * Method:    nativeSendPacket
 * Signature: (Ljpcap/packet/TCPPacket;)V
 */
JNIEXPORT void JNICALL Java_com_nix_jpcap_JpcapSender_nativeSendPacket
        (JNIEnv * env, jobject obj, jbyteArray data) {
    jbyte* receivedbyte = env->GetByteArrayElements(data, 0);
    jsize size = env->GetArrayLength(data);
    char chars[size];
    for (int i = 0;i < size;i ++) {
        chars[i] = receivedbyte[i];
    }
    if (pcap_sendpacket(fp, reinterpret_cast<const u_char *>(chars), size ) != 0) {
        return ;
    }
}