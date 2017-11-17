package com.nix.jpcap;

/**
 * @author 11723
 */
public class JpcapCaptor {
    public static native NetworkInterface[] getDeviceList();
    public native boolean setPacketReadTimeout(int var1);
    public native int loopPacket(int var1, PacketReceiver var2);
    private native String nativeOpenLive(String var1, int var2, int var3, int var4);
}
