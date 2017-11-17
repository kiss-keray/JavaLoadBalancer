package com.nix.jpcap;

import jpcap.NetworkInterface;
import jpcap.packet.TCPPacket;

import java.io.IOException;

/**
 * @author 11723
 */
public class JpcapSender {

    private native String nativeOpenDevice(String var1);

    private native void nativeSendPacket(TCPPacket var1);

    public static JpcapSender openDevice(NetworkInterface device) throws IOException {
        JpcapSender sender = new JpcapSender();
        System.out.println(device.name);
        String ret = sender.nativeOpenDevice(device.name);
        if (ret == null) {
            return sender;
        } else {
            throw new IOException(ret);
        }
    }
}
