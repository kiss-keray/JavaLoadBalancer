package com.nix;

import com.nix.jpcap.JpcapCaptor;
import org.junit.Test;

public class Main {
    @Test
    public void ObtainTest() {
        JpcapCaptor jpcapCaptor = new JpcapCaptor();
        System.out.println(jpcapCaptor.setPacketReadTimeout(100));
    }
}
