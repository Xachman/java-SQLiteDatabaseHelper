package com.github.xachman;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xach on 5/5/17.
 */
public abstract class Table {

    public Table() {

    }

    public String createTableSQL() {

        String columnSQL = "";
        int count = 0;
        for(Column column: columns()) {
            count++;
            columnSQL += column.name()+" "+column.type();
            if(column.isPrimaryKey()) {
                columnSQL += " PRIMARY KEY";
            }
            if(column.isAutoincrement()) {
                columnSQL += " AUTOINCREMENT";
            }

            if(count < columns().size()) {
                columnSQL += ",";
            }
        }

        return "CREATE TABLE IF NOT EXISTS "+tableName()+" (" +
                columnSQL+
                ") ";
    }


    public abstract List<Column> columns();

    public abstract String tableName();

}
