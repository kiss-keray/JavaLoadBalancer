package com.nix.pack;

/**
 * @author 11723
 */
public interface Process<P extends Object> {

    /**
     * 分发数据包到负载节点
     * */
    void distributionPackToNote(P o);
}
