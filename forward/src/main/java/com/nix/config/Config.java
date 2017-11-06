package com.nix.config;

/**
 * @author 11723
 * 转发包模块的基本配置
 */
public final class Config {
    public static final Config CONFIG = new Config();

    /**
     * 处理抓包的线程
     * */
    private final int workThreadCount;




    private Config() {
        this.workThreadCount = 100;
    }

    public int getWorkThreadCount() {
        return workThreadCount;
    }
}
