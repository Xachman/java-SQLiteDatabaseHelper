package com.github.xachman;

/**
 * Created by xach on 5/5/17.
 */
public class Column {
    private String type;
    private String name;
    private boolean primaryKey;
    private boolean autoincrement;

    public Column(String type, String name, boolean primaryKey, boolean autoincrement) {
        this.name = name;
        this.type = type.toUpperCase();
        this.primaryKey = primaryKey;
        this.autoincrement = autoincrement;
    }

    public Column(String type, String name, boolean primaryKey) {
        this(type, name, primaryKey, false);
    }
    public Column(String type, String name) {
        this(type, name, false, false);
    }

    public String name() {
        return name;
    }

    public String type() {
        return type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean isAutoincrement() {
        return autoincrement;
    }
}
