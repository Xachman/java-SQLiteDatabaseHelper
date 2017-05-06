package com.github.xachman;

import java.util.ArrayList;

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

            if(count < columns().size()) {
                columnSQL += ",";
            }
        }

        return "CREATE TABLE IF NOT EXISTS "+tableName()+" (" +
                ((autoincrement())? "id INTEGER PRIMARY KEY AUTOINCREMENT," : "" ) +
                columnSQL+
                ") ";
    }


    public abstract ArrayList<Column> columns();

    public abstract String tableName();

    public abstract boolean autoincrement();
}
