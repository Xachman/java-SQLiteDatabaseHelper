package com.github.xachman;

/**
 * Created by xach on 5/5/17.
 */
public class Column {
    private String type;
    private String name;

    public Column(String type, String name) {
        this.name = name;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public String type() {
        return type;
    }
}
