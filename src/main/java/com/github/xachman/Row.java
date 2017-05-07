package com.github.xachman;

import java.util.List;

/**
 * Created by xach on 5/5/17.
 */
public class Row {
    private List<Entry> entries;
    public Row(List<Entry> entries) {
       this.entries = entries;
    }
    public Entry getEntry(int num) {
        return entries.get(num);
    }

    public List<Entry> getEntries() {
       return entries;
    }

}
