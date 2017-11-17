package com.nix.jpcap;


/**
 * @author 11723
 */
public class NetworkInterface {
    public String name;
    public String description;
    public boolean loopback;
    public String datalink_name;
    public String datalink_description;
    public byte[] mac_address;
    public NetworkInterfaceAddress[] addresses;

    public NetworkInterface(String name, String description, boolean loopback, String datalink_name, String datalink_description, byte[] mac, NetworkInterfaceAddress[] addresses) {
        this.name = name;
        this.description = description;
        this.loopback = loopback;
        this.datalink_name = datalink_name;
        this.datalink_description = datalink_description;
        this.mac_address = mac;
        this.addresses = addresses;
    }
}