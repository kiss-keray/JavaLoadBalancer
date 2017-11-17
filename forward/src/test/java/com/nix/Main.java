package com.nix;

import com.nix.jpcap.JpcapCaptor;
import com.nix.jpcap.JpcapSender;
import jpcap.NetworkInterface;
import org.junit.Test;

import java.io.IOException;

public class Main {
    @Test
    public void ObtainTest() throws IOException {
        NetworkInterface[] networkInterfaces = jpcap.JpcapCaptor.getDeviceList();
        JpcapCaptor jpcapCaptor = new JpcapCaptor();
        System.out.println(jpcapCaptor.setPacketReadTimeout(100));
        System.out.println(JpcapSender.openDevice(networkInterfaces[1]));
    }
}
