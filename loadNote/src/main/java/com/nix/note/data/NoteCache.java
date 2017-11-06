package com.nix.note.data;

/**
 * @author 11723
 * 节点缓存
 */
public interface NoteCache {
    /**
     * 增加一个节点
     * @param note 增加的节点
     * */
    void add(Note note) throws Exception;
    /**
     * 移除节点
     * @param note 移除的节点
     * */
    void remove(Note note);

    /**
     * 获取最优节点
     * @return
     * 获取目前链接最少的节点 实现负载均衡
     * */
    Note getExcellentNote() throws Exception;
}
