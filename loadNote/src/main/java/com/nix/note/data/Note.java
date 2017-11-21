package com.nix.note.data;

import javax.validation.constraints.Pattern;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 11723
 * 负载节点对象
 */
public class Note {

    public Note(String ip,String mac) {
        this.ip = ip;
        this.mac = mac;
    }

    /**
     * 负载节点ip地址
     * */
    @Pattern(regexp = "[\\d]{0,3}\\.[\\d]{0,3}\\.[\\d]{0,3}\\.[\\d]{0,3}",message = "ip格式不合法")
    private String ip;
    /**
     * 负载节点mac地址
     * */
    @Pattern(regexp = "[\\w]{2}:[\\w]{2}:[\\w]{2}:[\\w]{2}:[\\w]{2}:[\\w]{2}",message = "mac格式不合法")
    private String mac;

    /**
     * 链接数
     * */
    private AtomicInteger workCount = new AtomicInteger(0);

    public void addConnect() {
        workCount.getAndIncrement();
    }

    public void subtract(){
        workCount.getAndAdd(-10);
    }

    public int getWorkCount() {
        return workCount.intValue();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public byte[] getByteMac() {
        byte[] bytes = new byte[6];
        String[] macs = getMac().split(":");
        for (int i = 0;i < bytes.length;i ++) {
            bytes[i] = (byte) ((int)(Integer.decode("0x" + macs[i])));
        }
        return bytes;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "Note{" +
                "ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                '}';
    }
}
