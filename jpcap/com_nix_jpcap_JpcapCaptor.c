//
// Created by 11723 on 2017/11/17.
//
#include "com_nix_jpcap_JpcapCaptor.h"
#include <pcap.h>


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
void got_packet(u_char *args, const struct pcap_pkthdr *header, const u_char*packet)
{
    u_char * pack = (u_char*)packet;
}

int caught()
{
//    pcap_t * handle;
//    char errbuf[PCAP_ERRBUF_SIZE];
//    bpf_u_int32 mask;
//    bpf_u_int32 net;
//    pcap_if_t *it;
//    char error[PCAP_ERRBUF_SIZE];
//    pcap_findalldevs(&it, error);
//    char * dev = it->next->name;
//    if (pcap_lookupnet(dev, &net, &mask, errbuf) == -1) {
//        fprintf(stderr, "Can't get netmask fordevice %s\n", dev);
//        net = 0;
//        mask = 0;
//    }
//    handle = pcap_open_live(dev, 65536, 1, 0, errbuf);
//    if (handle == NULL) {
//        fprintf(stderr, "Couldn't open device %s:%s\n", dev, errbuf);
//        return(2);
//    }
//    //异步调用got_packet函数
//    pcap_loop(handle, 0, got_packet, NULL);
//    pcap_freealldevs(it);
}