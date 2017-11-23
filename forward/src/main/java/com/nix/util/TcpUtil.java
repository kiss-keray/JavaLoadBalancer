package com.nix.util;

import jpcap.packet.TCPPacket;

public final class TcpUtil {
    /**
     * 重新计算ip数据包的校验码
     * */
    public static void flushCheckCode(TCPPacket packet) {
        //14-33位ip数据包固定20位 24 25两位位校验码
        short checkSum;
        int sum = 0;
        int count = 0;
        for (int i = 14;i < 34;i += 2) {
            if (i == 24) {
                sum += 0;
            }else {
                sum += (((((int) packet.header[i]) << 8) & 0x0000ff00) + (((int)packet.header[i + 1]) & 0x000000ff));
                if (sum > 65535) {
                    count ++;
                }
                sum &= 0x0000ffff;
            }
        }
        checkSum = (short) (~sum - count);
/*        if (packet.header[24] != (byte) (checkSum >> 8) && packet.header[25] != (byte)checkSum) {
            System.out.println(packet.header[24] + "---" + (byte) (checkSum >> 8));
            System.out.println(packet.header[25] + "---" + (byte)checkSum);
        }*/
        packet.header[24] = (byte) (checkSum >> 8);
        packet.header[25] = (byte)checkSum;
    }


    /**
     * 判断一个tcp数据包是否是请求连接的握手数据包
     * */
    public static boolean isReqShakHandPacket(TCPPacket packet) {
        return packet.sequence == 0 && packet.syn;
    }


    /**
     * 封装一个回应请求握手的tcp确认数据包
     * */
    public static TCPPacket getResponseShakeHandTcpPacket(TCPPacket shakePacket) {
        return null;
    }
}
