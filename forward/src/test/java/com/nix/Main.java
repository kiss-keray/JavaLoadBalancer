package com.nix;

import com.nix.jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.TCPPacket;
import org.junit.Test;

import java.io.IOException;

public class Main {
    @Test
    public void ObtainTest() throws IOException {
        NetworkInterface[] networkInterfaces = jpcap.JpcapCaptor.getDeviceList();
        JpcapSender sender = JpcapSender.openDevice(networkInterfaces[2]);
        sender.nativeSendPacket(new byte[]{-56,-45,-1,-45,-91,-84,-92,-54,-96,34,-97,-46,8,0,69,0,0,60,
                -119,84,64,0,64,6,47,78,-64,-88,0,101,-64,-88,0,100,-97,-114,0,80,-13,
                -19,-54,40,0,0,0,0,-96,2,-1,-1,36,-70,0,0,2,4,5,80,4,2,8,10,0,32,67,124,0,0,0,0,1,3,3,6});
    }

    public void SenderTcpPackTest() throws IOException {
        NetworkInterface[] networkInterfaces = jpcap.JpcapCaptor.getDeviceList();
        JpcapSender sender = JpcapSender.openDevice(networkInterfaces[2]);
        TCPPacket packet = new TCPPacket(155,122,155,122,true,true,true,true,
                true,true,true,true,1,1);
        packet.header = new byte[]{-56,-45,-1,-45,-91,-84,-92,-54,-96,34,-97,-46,8,0,69,0,0,60,
                -119,84,64,0,64,6,47,78,-64,-88,0,101,-64,-88,0,100,-97,-114,0,80,-13,
                -19,-54,40,0,0,0,0,-96,2,-1,-1,36,-70,0,0,2,4,5,80,4,2,8,10,0,32,67,124,0,0,0,0,1,3,3,6};
        sender.sendPacket(packet);
    }
}
