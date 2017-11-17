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
        (JNIEnv * env, jobject obj, jstring network)
{
    char errbuf[65535];
    struct pcap_if * alldevs;//存储网卡信息

    if (pcap_findalldevs(&alldevs, errbuf) == -1)//获取网卡列表  存储在alldevs里
    {
        return NULL;
    }

    /* Open the output device */
    if ( (fp= pcap_open_live(alldevs->next->name,            // name of the device
                        65535,                // portion of the packet to capture (only the first 100 bytes)
                        0,  // promiscuous mode
                        1000,               // read timeout
                        errbuf              // error buffer
    ) ) == NULL)
    {
        return (*env)->NewStringUTF(env,"ok");
    }
    return NULL;
}

/*
 * Class:     com_nix_jpcap_JpcapSender
 * Method:    nativeSendPacket
 * Signature: (Ljpcap/packet/TCPPacket;)V
 */
JNIEXPORT void JNICALL Java_com_nix_jpcap_JpcapSender_nativeSendPacket
        (JNIEnv * env, jobject obj, jobject packet)
{

}