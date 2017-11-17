package com.nix.jpcap;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 11723
 */
public class NetworkInterfaceAddress {
    public InetAddress address;
    public InetAddress subnet;
    public InetAddress broadcast;
    public InetAddress destination;

    public NetworkInterfaceAddress(byte[] address, byte[] subnet, byte[] broadcast, byte[] destination) {
        try {
            if (address != null) {
                this.address = InetAddress.getByAddress(address);
            }

            if (subnet != null) {
                this.subnet = InetAddress.getByAddress(subnet);
            }

            if (broadcast != null) {
                this.broadcast = InetAddress.getByAddress(broadcast);
            }

            if (destination != null) {
                this.destination = InetAddress.getByAddress(destination);
            }
        } catch (UnknownHostException var6) {
            ;
        }

    }
}