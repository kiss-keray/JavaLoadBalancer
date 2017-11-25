package com.nix.util;

import jpcap.packet.TCPPacket;
import sun.security.provider.Sun;

import javax.sound.midi.Soundbank;
import java.net.InetAddress;
import java.time.OffsetDateTime;

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
        return packet.ack_num == 0 && packet.syn;
    }


    /**
     * 封装一个回应请求握手的tcp确认数据包
     * */
    public static TCPPacket getResponseShakeHandTcpPacket(TCPPacket shakePacket) {
        TCPPacket targetPacket = new TCPPacket(shakePacket.dst_port,shakePacket.src_port,0,shakePacket.sequence,
                shakePacket.urg,true,shakePacket.psh,shakePacket.rst,shakePacket.syn,shakePacket.fin,shakePacket.rsv1,
                shakePacket.rsv2,shakePacket.window,shakePacket.urgent_pointer);
        //tcp头部在数据包中偏移位置 （目前默认ip头部为20 + 以太头部14 ）34
        int tcpOffset = 34;
        targetPacket.header = new byte[shakePacket.header.length];
        for (int i = 0;i < targetPacket.header.length;i ++) {
            targetPacket.header[i] = shakePacket.header[i];
        }
        //修改ip数据包总长度内容 header长度减去以太帧头部14
        targetPacket.length = (short) (targetPacket.header.length - 14);
        targetPacket.header[16] = (byte) (targetPacket.length >> 8);
        targetPacket.header[17] = (byte) targetPacket.length;
        //修改ip数据包的标识（随便在原先的iden加1000）
        targetPacket.ident = shakePacket.ident + 1000;
        targetPacket.header[18] = (byte) (targetPacket.ident >> 8);
        targetPacket.header[18] = (byte) (targetPacket.ident);

        //更改目的mac地址
        targetPacket.header[0] = shakePacket.header[6];
        targetPacket.header[1] = shakePacket.header[7];
        targetPacket.header[2] = shakePacket.header[8];
        targetPacket.header[3] = shakePacket.header[9];
        targetPacket.header[4] = shakePacket.header[10];
        targetPacket.header[5] = shakePacket.header[11];
        //更改源mac地址
        targetPacket.header[6] = shakePacket.header[0];
        targetPacket.header[7] = shakePacket.header[1];
        targetPacket.header[8] = shakePacket.header[2];
        targetPacket.header[9] = shakePacket.header[3];
        targetPacket.header[10] = shakePacket.header[4];
        targetPacket.header[11] = shakePacket.header[5];
        //更改源IP地址
        targetPacket.src_ip = shakePacket.dst_ip;
        targetPacket.header[26] = targetPacket.src_ip.getAddress()[0];
        targetPacket.header[27] = targetPacket.src_ip.getAddress()[1];
        targetPacket.header[28] = targetPacket.src_ip.getAddress()[2];
        targetPacket.header[29] = targetPacket.src_ip.getAddress()[3];
        //更改目的IP地址
        targetPacket.dst_ip = shakePacket.src_ip;
        targetPacket.header[30] = targetPacket.dst_ip.getAddress()[0];
        targetPacket.header[31] = targetPacket.dst_ip.getAddress()[1];
        targetPacket.header[32] = targetPacket.dst_ip.getAddress()[2];
        targetPacket.header[33] = targetPacket.dst_ip.getAddress()[3];
        //更改源端口
        targetPacket.src_port = shakePacket.dst_port;
        targetPacket.header[tcpOffset] = (byte) (targetPacket.src_port >> 8);
        targetPacket.header[tcpOffset + 1] = (byte) targetPacket.src_port;
        //更改目的端口
        targetPacket.dst_port = shakePacket.src_port;
        targetPacket.header[tcpOffset + 2] = (byte) (targetPacket.dst_port >> 8);
        targetPacket.header[tcpOffset + 3] = (byte) targetPacket.dst_port;
        //添加确认号
        targetPacket.ack_num = shakePacket.sequence + 1;
        targetPacket.header[tcpOffset + 8] = (byte) (targetPacket.ack_num >> 24);
        targetPacket.header[tcpOffset + 9] = (byte) (targetPacket.ack_num >> 16);
        targetPacket.header[tcpOffset + 10] = (byte) (targetPacket.ack_num >> 8);
        targetPacket.header[tcpOffset + 11] = (byte) targetPacket.ack_num;
        //添加序号
        long seq = (long) (Math.random() * Integer.MAX_VALUE);
        targetPacket.sequence = seq;
        targetPacket.header[tcpOffset + 4] = (byte) (seq >> 24);
        targetPacket.header[tcpOffset + 5] = (byte) (seq >> 16);
        targetPacket.header[tcpOffset + 6] = (byte) (seq >> 8);
        targetPacket.header[tcpOffset + 7] = (byte) seq;
        //修改ack为1
        targetPacket.ack = true;
        targetPacket.header[tcpOffset + 13] = (byte) (targetPacket.header[tcpOffset + 13] | 0x10);

        targetPacket.header[tcpOffset + 14] = (byte) 0xff;
        targetPacket.header[tcpOffset + 15] = (byte) 0xff;

        //将校验和设置为0
        targetPacket.header[tcpOffset + 16] = 0;
        targetPacket.header[tcpOffset + 17] = 0;
        //修改tcp数据偏移数据
        targetPacket.header[tcpOffset + 12] = (byte) (((targetPacket.header.length - tcpOffset)/4) << 4);
//        targetPacket.header[tcpOffset + 12] = (byte) 0x80;

        System.out.println("sigm==" + targetPacket.header[tcpOffset + 12]);

        computeTcpCheckSum(targetPacket, tcpOffset);
        flushCheckCode(targetPacket);
        return targetPacket;
    }

    /**
     * 计算tcp数据包校验码
     * @param targetPacket tcp数据包
     * @param tcpOffset tcp头部在以太帧的偏移位置
     * */
    public static void computeTcpCheckSum(TCPPacket targetPacket,int tcpOffset) {
        //计算tcp校验和
        int sum = 0,count = 0;
        //加上源ip与目的ip
        for (int i = 26;i < 34;i += 2) {
            sum += (((((int) targetPacket.header[i]) << 8) & 0x0000ff00) + (((int)targetPacket.header[i + 1]) & 0x000000ff));
            if (sum > 65535) {
                count ++;
            }
            sum &= 0x0000ffff;
        }
        //添加协议类型（TCP 0x06）
        sum += 0x00000006;
        if (sum > 65535) {
            count ++;
        }
        sum &= 0x0000ffff;
        //添加tcp包数据长度（头部+数据）
        sum += (targetPacket.length - 20);
        if (sum > 65535) {
            count ++;
        }
        sum &= 0x0000ffff;
        for (int i = tcpOffset;i < targetPacket.header.length;i += 2) {

            if (i == tcpOffset + 16) {
                continue;
            }

            sum += (((((int) targetPacket.header[i]) << 8) & 0x0000ff00) + (((int)targetPacket.header[i + 1]) & 0x000000ff));
            if (sum > 65535) {
                count ++;
            }
            sum &= 0x0000ffff;
        }
        //反码运算
        sum = (~sum - count);
        targetPacket.header[tcpOffset + 16] = (byte) (sum >> 8);
        targetPacket.header[tcpOffset + 17] = (byte) sum;

    }
}
