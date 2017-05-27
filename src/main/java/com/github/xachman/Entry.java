package com.github.xachman;

/**
 * Created by xach on 5/7/17.
 */
public class Entry {
    private Column column;
    private Value value;

    public Entry(Column column, Object value) {
        this.column = column;
        if(column.type().equals("INT") || column.type().equals("INTEGER")) {
            this.value = new Value(ValueType.INTEGER, value.toString());
        } else {
            this.value = new Value(ValueType.STRING, value.toString());
        }

    }

    public Entry(Column column, Value value) {
        this.column = column;
        this.value = value;
    }


    public Column getColumn() {
        return column;
    }

    public Value getValue() {
        return value;
    }
}
