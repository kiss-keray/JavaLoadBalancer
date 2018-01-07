package com.nix.pack.obtain;

import com.nix.pack.ObtainPackage;
import jpcap.NetworkInterface;

/**
 * @author 11723
 */
public final class ObtainPackageFactory {

    private volatile static ObtainPackage obtainPackage = null;


    public static ObtainPackage getJpcapGetPackage(NetworkInterface networkInterface) {
        if (obtainPackage == null) {
            synchronized (ObtainPackageFactory.class) {
                if (obtainPackage == null) {
                    obtainPackage = JpcapObtainPackage.getObtainPackage(networkInterface);
                }
            }
        }
        return obtainPackage;
    }


}
