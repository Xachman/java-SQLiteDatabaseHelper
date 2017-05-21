package com.github.xachman;

import com.github.xachman.Where.Where;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xach on 5/5/17.
 */


public class SQLiteDatabaseHelper {
    private SQLiteDBCI dbc = null;
    private String dbpath;

    public SQLiteDatabaseHelper(String dbpath) {
        this.dbpath = dbpath;
        open();
    }

    public void open() {
        try {
            dbc = new SQLiteDBC(new File(dbpath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createTable(Table table) {
            dbc.execute(table.createTableSQL());
    }

    public void dropTable(Table table) {
            dbc.execute("DROP TABLE IF EXISTS " + table.tableName());
    }

    public Row insert(Table table, ArrayList<String> values) {
            String sql = "INSERT INTO " + table.tableName() + insertValuesSQL(table, values);
            dbc.execute(sql);
            List<Row> rows = dbc.prepareStatement("SELECT * FROM " + table.tableName() + " ORDER BY id DESC LIMIT 1", null);
            return rows.get(0);
    }

    public void close() {
        dbc.close();
    }

    public List<Row> getRows(Table table) {
        List<Row> rows = dbc.prepareStatement("SELECT * FROM " + table.tableName(), null);
        return rows;
    }

    public Row getRowById(Table table, int id) {
        String sql = "SELECT * FROM " + table.tableName() + " WHERE id=?";
        List<Row> rows = dbc.prepareStatement(sql, new ArrayList<Value>(Arrays.asList(new Value(ValueType.NUMBER, ""+id))));
        return rows.get(0);
    }

    public Row updateById(Table table, int id, List<Value> values) {
            String sql = "UPDATE " + table.tableName() + " SET " + updateValuesSQL(table, values) + " WHERE id=?";
            List<Value> prepareValues = new ArrayList<Value>(values);

            prepareValues.add(new Value(ValueType.NUMBER, Integer.toString(id)));
            System.out.println(sql);
            dbc.prepareStatement(sql, prepareValues);
            List<Row> rows = dbc.executeQuery("SELECT * FROM " + table.tableName() + " WHERE id=" + id);
            return rows.get(0);
    }

    public boolean removeById(Table table, int id) {
            String sql = "DELETE FROM " + table.tableName() + " WHERE id= " + id;
            dbc.execute(sql);
            return true;
    }

    private String insertValuesSQL(Table table, List<String> values) {
        String valuesString = "";
        String columnsString = "";
        for (int i = 0; i < table.columns().size(); i++) {
            Column column = table.columns().get(i);
            if(i < values.size()) {
                if(values.get(i) == null) continue;
                String value = values.get(i);
                if (column.type().equals("TEXT") || column.type().equals("VARCHAR")) {
                    valuesString += "'" + value + "'";
                } else {
                    valuesString += value;
                }
                columnsString += column.name();
                if ((i + 1) < values.size()) {
                    valuesString += ",";
                    columnsString += ",";
                }
            }
        }
        String sql = "("
                + columnsString
                + ") VALUES ("
                + valuesString
                + ")";

        return sql;
    }

    private String updateValuesSQL(Table table, List<Value> values) {
        String sqlString = "";
        for (int i = 0; i < table.columns().size(); i++) {
            Column column = table.columns().get(i);
            if(i < values.size()) {
                if(values.get(i) == null) continue;
                sqlString += column.name() + "=?";
                if ((i + 1) < values.size()) {
                    sqlString += ",";
                }
            }
        }

        return sqlString;
    }

    public List<Row> searchTable(Table table, Where where) {
        String sql = "SELECT * FROM " + table.tableName() + " WHERE " + where.toString();
        List<Row> rows = dbc.executeQuery(sql);
        return rows;
    }
}
