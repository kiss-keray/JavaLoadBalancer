package com.nix.pack.obtain;

import com.nix.config.Config;
import com.nix.jpcap.JpcapSender;
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
    private static final Object clock = new Object();

    /**
     * 单例模式
     * */
    public static ObtainPackage getObtainPackage(NetworkInterface networkInterface) {
        if (obtainPackage == null) {
            synchronized (clock) {
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

    int i = 0;

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
                                            process.addHttpPackage((TCPPacket)packet);

                                            i++;
                                            if (i > 5) {
                                                captor.close();
                                            }

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
        ObtainPackage obtainPackage = ObtainPackageFactory.getJpcapGetPackage(networkInterfaces[3]);
        obtainPackage.start();
    }

//    static JpcapSender sender;
//    public static void main(String[] args) throws Exception {
//        NetworkInterface[] networkInterfaces = jpcap.JpcapCaptor.getDeviceList();
//
//        new Thread(() ->{
//            try {
//                sender = JpcapSender.openDevice(networkInterfaces[2]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//        Thread.sleep(1000);
//        TCPPacket packet = new TCPPacket(155,122,155,122,true,true,true,true,
//                true,true,true,true,1,1);
//        packet.header = new byte[]{-56,-45,-1,-45,-91,-84,-92,-54,-96,34,-97,-46,8,0,69,0,0,60,
//                -119,84,64,0,64,6,47,78,-64,-88,0,101,-64,-88,0,100,-97,-114,0,80,-13,
//                -19,-54,40,0,0,0,0,-96,2,-1,-1,36,-70,0,0,2,4,5,80,4,2,8,10,0,32,67,124,0,0,0,0,1,3,3,6};
//        sender.sendPacket(packet);
//    }
}
