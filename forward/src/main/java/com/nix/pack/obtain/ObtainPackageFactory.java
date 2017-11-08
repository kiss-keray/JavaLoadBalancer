package com.nix.pack.obtain;

import com.nix.pack.ObtainPackage;
import jpcap.NetworkInterface;

public final class ObtainPackageFactory {

    private static ObtainPackage obtainPackage = null;


    public static ObtainPackage getJpcapGetPackage(NetworkInterface networkInterface) {
        if (obtainPackage == null) {
            synchronized (obtainPackage) {
                if (obtainPackage == null) {
                    obtainPackage = JpcapObtainPackage.getObtainPackage(networkInterface);
                }
            }
        }
        return obtainPackage;
    }


}
