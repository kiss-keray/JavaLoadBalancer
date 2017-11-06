package com.nix.pack.obtain;

import com.nix.pack.ObtainPackage;
import jpcap.NetworkInterface;

public final class ObtainPackageFactory {
    private static ObtainPackage obtainPackage = null;


    public static synchronized ObtainPackage getJpcapGetPackage(NetworkInterface networkInterface) {
        if (obtainPackage == null) {
            return new JpcapObtainPackage(networkInterface);
        }else{
            return obtainPackage;
        }
    }
}
