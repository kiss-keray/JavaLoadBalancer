package com.nix.pack.obtain;

import com.nix.config.Config;
import com.nix.pack.ObtainPackage;
import com.nix.pack.process.JpcapHttpPackageProcess;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author 11723
 * 抓包工作类
 */
public class JpcapObtainPackage implements ObtainPackage {

    private final BlockingDeque blockingDeque = new LinkedBlockingDeque();
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.CONFIG.getWorkThreadCount(),
            Config.CONFIG.getWorkThreadCount(), 0, TimeUnit.SECONDS, blockingDeque, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("package");
            return thread;
        }
    });

    private static JpcapObtainPackage obtainPackage = null;

    /**
     * 单例模式
     * */
    public static ObtainPackage getObtainPackage(NetworkInterface networkInterface) {
        if (obtainPackage == null) {
            synchronized (obtainPackage) {
                if (obtainPackage == null) {
                    obtainPackage = new JpcapObtainPackage(networkInterface);
                }
            }
        }
        return obtainPackage;
    }

    private JpcapObtainPackage(NetworkInterface networkInterface){
        this.networkInterface = networkInterface;
        this.process = new JpcapHttpPackageProcess(networkInterface);
    }





    /**
     * 工作网卡
     * */
    private final NetworkInterface networkInterface;


    /**
     * 数据包处理器
     * */
    private final JpcapHttpPackageProcess process;

    /**
     * 工作状态
     * */
    private boolean status = false;

    private JpcapCaptor captor = null;
    /**
     * 开始抓包
     * */
    @Override
    public void start() {
        if (status) {
            return;
        }
        status = true;
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 设置只抓取tcp端口的数据包
                    captor = JpcapCaptor.openDevice(networkInterface, 65535, true, 20);
                    captor.setFilter("tcp",true);
                    captor.loopPacket(-1, new PacketReceiver() {
                        @Override
                        public void receivePacket(Packet packet) {
                            if (((IPPacket)packet).dst_ip.equals(networkInterface.addresses[1].address)) {
                                if (packet instanceof TCPPacket && ((TCPPacket) packet).dst_port == 80) {
                                    // 处理数据包放到另外的工作线程处理
                                    threadPool.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            process.addHttpPackage((IPPacket) packet);
                                        }
                                    });
                                }
                            }

                        }
                    });
                } catch (Exception e) {
                    System.out.println("执行失败");
                    e.printStackTrace();
                }
            }
        });
    }


    public NetworkInterface getNetworkInterface() {
        return networkInterface;
    }

    @Override
    public void stop() {
        status = false;
        captor.close();
    }

    public static void main(String[] args) {
        NetworkInterface[] networkInterfaces = JpcapCaptor.getDeviceList();
        ObtainPackage obtainPackage = ObtainPackageFactory.getJpcapGetPackage(networkInterfaces[2]);
        obtainPackage.start();
    }
}
