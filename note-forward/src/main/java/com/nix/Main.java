package com.nix;

import com.nix.pack.ObtainPackage;
import com.nix.pack.obtain.JpcapObtainPackage;
import com.nix.pack.obtain.ObtainPackageFactory;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

/**
 * @author 11723
 */
public class Main {
    public static void main(String[] args) {
        NetworkInterface[] networkInterfaces = JpcapCaptor.getDeviceList();
        System.out.println(networkInterfaces[1].description);
        ObtainPackage obtainPackage = ObtainPackageFactory.getJpcapGetPackage(networkInterfaces[2]);
        obtainPackage.start();
    }
}
