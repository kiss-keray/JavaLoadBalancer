package com.nix.jpcap;

import jpcap.NetworkInterface;
import jpcap.packet.TCPPacket;

import java.io.IOException;

/**
 * @author 11723
 */
public class JpcapSender {

    static {
        System.loadLibrary("libjpcap");
    }
    private JpcapSender(){}

    private native String nativeOpenDevice(String var1);

    public native void nativeSendPacket(byte[] data);

    public void sendPacket(TCPPacket packet) {
//        if (packet.data != null && packet.data.length > 0) {
//            synchronized (System.out) {
//                System.out.println("length==" + packet.data.length );
//                for (int i = 0; i < packet.data.length; i++) {
//                    System.out.print((char) packet.data[i]);
//                }
//                System.out.println("iden==" + packet.ident);
//            }
//        }
        byte[] data = new byte[packet.header.length + (packet.data != null ? packet.data.length : 0)];
        System.arraycopy(packet.header,0,data,0,packet.header.length);
        if (packet.data != null) {
            System.arraycopy(packet.data, 0, data, packet.header.length, packet.data.length);
        }

//        synchronized (System.out) {
//            System.out.println();
//            for (int i = 0; i < data.length; i++) {
//                System.out.printf("%-4d,",data[i]);
//            }
//            System.out.println();
//        }
        nativeSendPacket(data);
    }

    public static JpcapSender openDevice(NetworkInterface device) throws IOException {
        JpcapSender sender = new JpcapSender();
        String ret = sender.nativeOpenDevice(device.name);
        if (ret == null ) {
            return sender;
        } else {
            throw new IOException(ret);
        }
    }
}
