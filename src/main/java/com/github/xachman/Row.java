package com.github.xachman;

import java.util.List;

/**
 * Created by xach on 5/5/17.
 */
public class Row {
    private List<Column> columns;
    private List<Object> values;
    public Row(List<Column> columns, List<Object> values) {
       this.columns = columns;
       this.values = values;
    }
    public int getInteger(int num) {
        return 0;
    }

    public String getString(int num) {
       return null;
    }

    public String getColumn(String name) {
        for(Column column: columns) {
            if(column.name().equals(name)) {
                return column.name();
            }
        }
        return null;
    }
}
