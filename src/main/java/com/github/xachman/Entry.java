package com.github.xachman;

/**
 * Created by xach on 5/7/17.
 */
public class Entry {
    private Column column;
    private Object value;

    public Entry(Column column, Object value) {
        this.column = column;
        this.value = value;
    }


    public Column getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }
}
