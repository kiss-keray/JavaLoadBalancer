package com.nix.note.data.supper;

import com.nix.note.data.Note;
import com.nix.note.data.NoteCache;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author 11723
 */
public class MemoryNoteCache implements NoteCache {
    /**
     * 缓存一直保持单例
     * */
    private static MemoryNoteCache cache = null;
    private static final Object clock = new Object();

    private MemoryNoteCache(){
        Note note = new Note();
        note.setIp("59.110.234.213");
        note.setMac("00:16:3e:30:30:5a");
        try {
            add(note);
            System.out.println(note.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一个内存节点缓存
     * */
    public static NoteCache getNoteCache() {
        if (cache == null) {
            synchronized (clock) {
                if (cache == null) {
                    cache = new MemoryNoteCache();
                }
            }
        }
        return cache;
    }

    private static final BlockingDeque<Note> NOTE_CAHCE = new LinkedBlockingDeque();


    @Override
    public void add(Note note) throws Exception {
        NOTE_CAHCE.put(note);
    }

    @Override
    public void remove(Note note) {
        NOTE_CAHCE.remove(note);
    }

    @Override
    public Note getExcellentNote() throws Exception {
        //简便的负载算法 队列循环
        Note n = NOTE_CAHCE.poll();
        if (n == null){
            return null;
        }
        n.addConnect();
        NOTE_CAHCE.put(n);
        return n;
    }

}
