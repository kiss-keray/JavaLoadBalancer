package com.nix.pack.process;

import com.nix.jpcap.JpcapSender;
import com.nix.note.data.supper.MemoryNoteCache;
import com.nix.note.data.Note;
import com.nix.note.data.NoteCache;
import com.nix.pack.Process;
import com.nix.pack.obtain.JpcapObtainPackage;
import com.nix.util.TcpUtil;
import jpcap.NetworkInterface;
import jpcap.packet.EthernetPacket;
import jpcap.packet.TCPPacket;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 11723
 * 加工jpcap抓取到的http数据包
 */
public class JpcapHttpPackageProcess implements Process<TCPPacket>{

    /**
     * 数据包发送网卡句柄
     * */
    protected final JpcapSender sender;

    protected final Note note;

    public JpcapHttpPackageProcess(NetworkInterface networkInterface,Note note) {
        JpcapSender j = null;
        try {
            j = JpcapSender.openDevice(networkInterface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sender = j;
        this.note = note;
    }


    /**
     * 节点缓存引用
     * */
    private NoteCache noteCache = MemoryNoteCache.getNoteCache();


    /**
     * 对分片ip包的缓存
     * */
    private ConcurrentHashMap<String,List<TCPPacket>> packetCache = new ConcurrentHashMap<>();
    /**
     * 保存分片数据包缓存时间 便于清除超时还没传送完整的缓存
     * */
    private ConcurrentHashMap<String,Long> packetCacheTime = new ConcurrentHashMap<>();

    /**
     * 抓包线程{@link JpcapObtainPackage}获取到一个http数据包交给此方法处理
     * */
    public void addHttpPackage(TCPPacket packet) {
        try {
            // 当ip数据包为不分片的单包时直接转发到节点
            if (!packet.more_frag && packet.offset == 0) {
                distributionPackToNote(note,packet);
            }else {
                if (packetCache.containsKey(String.valueOf(packet.ident))) {
                    packetCache.get(String.valueOf(packet.ident)).add(packet);
                    process(packetCache.get(String.valueOf(packet.ident)));
                }else {
                    /**
                     * 分组数据包第一次被获取 添加到packet缓存中
                     * Collections.synchronizedList(new ArrayList<IPPacket>()) 保证list操作线程安全
                     */
                    List<TCPPacket> packets = Collections.synchronizedList(new ArrayList<TCPPacket>());
                    packets.add(packet);
                    //添加一个分组的缓存
                    packetCache.put(String.valueOf(packet.ident),packets);
                    //保存缓存创建时间
                    packetCacheTime.put(String.valueOf(packet.ident),System.nanoTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void process(List<TCPPacket> packetList){
        TCPPacket[] packets = new TCPPacket[packetList.size()];
        packetList.toArray(packets);
        //对同一标志的ip数据包检查接收是否完整
        //需要重新优化不需要排序完成
        for(int i = 1;i < packets.length; i ++){
            TCPPacket k = packets[i];
            int j = i;
            for (;j > 0 && packets[j - 1].offset > k.offset;j--) {
                packets[j] = packets[j - 1];
            }
            packets[j] = k;
        }
        //如果接收完整
        if (!packets[packets.length - 1].more_frag) {
            distributionPackToNote(note,packets);
            packetCache.remove(packetList.get(0).ident);
            packetCacheTime.remove(packetList.get(0).ident);
        }

    }


    @Override
    public void distributionPackToNote(Note note,TCPPacket ... packets) {
        try {
            if (note == null) {
               note = noteCache.getExcellentNote();
               if (note == null) {
                   return;
               }
            }
            for (TCPPacket packet:packets) {
                //更改ip数据包的目的ip地址
                packet.dst_ip = Inet4Address.getByName(note.getIp());
                packet.header[30] = packet.dst_ip.getAddress()[0];
                packet.header[31] = packet.dst_ip.getAddress()[1];
                packet.header[32] = packet.dst_ip.getAddress()[2];
                packet.header[33] = packet.dst_ip.getAddress()[3];
                //更改ip数据包的目的mac地址
                ((EthernetPacket) packet.datalink).dst_mac = note.getByteMac();
                packet.header[0] = ((EthernetPacket) packet.datalink).dst_mac[0];
                packet.header[1] = ((EthernetPacket) packet.datalink).dst_mac[1];
                packet.header[2] = ((EthernetPacket) packet.datalink).dst_mac[2];
                packet.header[3] = ((EthernetPacket) packet.datalink).dst_mac[3];
                packet.header[4] = ((EthernetPacket) packet.datalink).dst_mac[4];
                packet.header[5] = ((EthernetPacket) packet.datalink).dst_mac[5];
                TcpUtil.flushCheckCode(packet);

                sender.sendPacket(packet);

                System.out.println("发送数据包：" + packet + "  给：" + note);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
