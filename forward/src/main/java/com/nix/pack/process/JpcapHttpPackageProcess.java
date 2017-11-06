package com.nix.pack.process;

import com.nix.note.data.supper.MemoryNoteCache;
import com.nix.note.data.Note;
import com.nix.note.data.NoteCache;
import com.nix.pack.Process;
import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * @author 11723
 * 加工jpcap抓取到的http数据包
 */
public class JpcapHttpPackageProcess implements Process<IPPacket>{

    /**
     * 数据包发送网卡句柄
     * */
    private final JpcapSender sender;

    public JpcapHttpPackageProcess(NetworkInterface networkInterface) {
        JpcapSender j = null;
        try {
            j = JpcapSender.openDevice(networkInterface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sender = j;
    }

    /**
     * 节点缓存引用
     * */
    private NoteCache noteCache = MemoryNoteCache.getNoteCache();

    public void addHttpPackage(IPPacket packet) {


    }



    @Override
    public void distributionPackToNote(IPPacket packet) {
        try {
            Note note = noteCache.getExcellentNote();
            //更改ip数据包的目的ip地址
            packet.dst_ip = Inet4Address.getByName(note.getIp());
            //更改ip数据包的目的mac地址
            ((EthernetPacket)packet.datalink).dst_mac = note.getByteMac();

            sender.sendPacket(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
