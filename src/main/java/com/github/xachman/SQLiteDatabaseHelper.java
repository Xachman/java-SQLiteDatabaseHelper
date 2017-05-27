package com.github.xachman;

import com.github.xachman.Where.Where;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

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

    public Row insert(Table table, Map<String,String> values) {
            String sql = "INSERT INTO " + table.tableName() + insertValuesSQL(table, values);
            dbc.prepareUpdateStatement(sql, convertColumnMapToValueList(table, values));
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

    public Row updateById(Table table, int id, Map<String, String> values) {
            String sql = "UPDATE " + table.tableName() + " SET " + updateValuesSQL(table, values) + " WHERE id=?";
            List<Value> prepareValues = convertColumnMapToValueList(table, values);

            prepareValues.add(new Value(ValueType.NUMBER, Integer.toString(id)));
            dbc.prepareUpdateStatement(sql, prepareValues);
            List<Row> rows = dbc.executeQuery("SELECT * FROM " + table.tableName() + " WHERE id=" + id);
            return rows.get(0);
    }

    public boolean removeById(Table table, int id) {
            String sql = "DELETE FROM " + table.tableName() + " WHERE id= " + id;
            dbc.execute(sql);
            return true;
    }

    private String insertValuesSQL(Table table, Map<String, String> values) {
        String valuesString = "";
        String columnsString = "";
        int count = 0;
        for (Column column: table.columns()) {
            if(values.containsKey(column.name())) {
                valuesString += "?";
                columnsString += column.name();
                if ((count + 1) < values.size()) {
                    valuesString += ",";
                    columnsString += ",";
                }
                count++;
            }
        }
        String sql = "("
                + columnsString
                + ") VALUES ("
                + valuesString
                + ")";

        return sql;
    }

    private String updateValuesSQL(Table table, Map<String, String> values) {
        String sqlString = "";

        int count = 1;
        for(Column column: table.columns()) {
            if(values.containsKey(column.name())) {
                sqlString += column.name() + "=?";
                if(count < values.size()) {
                    sqlString += ",";
                }
                count++;
            }
        }
        return sqlString;
    }

    private List<Value> convertColumnMapToValueList(Table table, Map<String, String> map) {
        List<Value> values = new ArrayList<Value>();
        for(Column column: table.columns()) {
           if(map.containsKey(column.name())) {
              values.add(new Value(getValueTypeFromColumn(column), map.get(column.name())));
           }
        }

        return values;
    }

    private ValueType getValueTypeFromColumn(Column column) {
        String columnType = column.type();
        if (columnType.equals("INT") || columnType.equals("INTEGER") || columnType.equals("DOUBLE") || columnType.equals("FLOAT")) {
            return ValueType.NUMBER;
        }
        return ValueType.STRING;
    }

    public List<Row> searchTable(Table table, Where where) {
        String sql = "SELECT * FROM " + table.tableName() + " WHERE " + where.toPreparedString();
        List<Row> rows = dbc.prepareStatement(sql, where.values());
        return rows;
    }
}
