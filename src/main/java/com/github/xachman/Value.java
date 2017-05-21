package com.github.xachman;

/**
 * Created by xach on 5/21/17.
 */
public class Value {
    private ValueType type;
    private String value;

    public Value(ValueType type, String value) {
        this.type = type;
        this.value = value;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toSql() {
        if(getType() == ValueType.INTEGER || getType() == ValueType.NUMBER) {
            return value;
        }else if(getType() == ValueType.STRING) {
            return "'"+value+"'";
        }
        return "";
    }

    @Override
    public String toString() {
        return value;
    }
}
