package com.nix.pack.process;

import com.nix.jpcap.JpcapSender;
import com.nix.note.data.Note;
import com.nix.note.data.NoteCache;
import com.nix.note.data.supper.MemoryNoteCache;
import com.nix.pack.Process;
import com.nix.pack.obtain.JpcapObtainPackage;
import jpcap.NetworkInterface;
import jpcap.packet.EthernetPacket;
import jpcap.packet.TCPPacket;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 11723
 * 加工jpcap抓取到的http数据包
 */
public class ClientJpcapHttpPackageProcess extends JpcapHttpPackageProcess{

    public ClientJpcapHttpPackageProcess(NetworkInterface networkInterface, Note note) {
        super(networkInterface,note);
    }

    @Override
    public void distributionPackToNote(Note note,TCPPacket ... packets) {
        try {
            if (note == null) {
                return;
            }
            for (TCPPacket packet:packets) {
                //更改ip数据包的目的ip地址
                packet.src_ip = Inet4Address.getByName(note.getIp());
                packet.header[26] = packet.src_ip.getAddress()[0];
                packet.header[27] = packet.src_ip.getAddress()[1];
                packet.header[28] = packet.src_ip.getAddress()[2];
                packet.header[29] = packet.src_ip.getAddress()[3];
                //更改ip数据包的目的mac地址
                ((EthernetPacket) packet.datalink).src_mac = note.getByteMac();
                packet.header[0] = ((byte) ~packet.header[0]);
                packet.header[1] = ((byte) ~packet.header[1]);
                packet.header[2] = ((byte) ~packet.header[2]);
                packet.header[3] = ((byte) ~packet.header[3]);
                packet.header[4] = ((byte) ~packet.header[4]);
                packet.header[5] = ((byte) ~packet.header[5]);
                packet.header[6] = ((EthernetPacket) packet.datalink).src_mac[0];
                packet.header[7] = ((EthernetPacket) packet.datalink).src_mac[1];
                packet.header[8] = ((EthernetPacket) packet.datalink).src_mac[2];
                packet.header[9] = ((EthernetPacket) packet.datalink).src_mac[3];
                packet.header[10] = ((EthernetPacket) packet.datalink).src_mac[4];
                packet.header[11] = ((EthernetPacket) packet.datalink).src_mac[5];
                flushCheckCode(packet);

                sender.sendPacket(packet);

                System.out.println("发送数据包：" + packet + "  给：" + note);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
