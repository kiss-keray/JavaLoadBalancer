package com.nix;

import com.nix.pack.ObtainPackage;
import com.nix.pack.obtain.JpcapObtainPackage;
import com.nix.pack.obtain.ObtainPackageFactory;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import org.junit.Test;

public class Main {
    @Test
    public void ObtainTest() {
        NetworkInterface[] networkInterfaces = JpcapCaptor.getDeviceList();
        ObtainPackage obtainPackage = ObtainPackageFactory.getJpcapGetPackage(networkInterfaces[2]);
        obtainPackage.start();
    }
}
