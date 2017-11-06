package com.nix.pack.obtain;

import com.nix.config.Config;
import com.nix.pack.ObtainPackage;
import com.nix.pack.process.JpcapHttpPackageProcess;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

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


    protected JpcapObtainPackage(NetworkInterface networkInterface){
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
                    // 设置只抓取80端口的数据包
                    captor = JpcapCaptor.openDevice(networkInterface, 65535, true, 10);
                    captor.setFilter("http",true);
                    captor.processPacket(1, new PacketReceiver() {
                        @Override
                        public void receivePacket(Packet packet) {
                            if (packet instanceof IPPacket) {
                                // 处理数据包放到另外的工作线程处理
                                threadPool.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        process.addHttpPackage((IPPacket) packet);
                                    }
                                });
                            }
                        }
                    });
                } catch (IOException e) {
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
}
