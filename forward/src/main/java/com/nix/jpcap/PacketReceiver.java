package com.nix.jpcap;


/**
 * @author 11723
 */
public interface PacketReceiver {
    void receivePacket(TcpPacket packet);
}
