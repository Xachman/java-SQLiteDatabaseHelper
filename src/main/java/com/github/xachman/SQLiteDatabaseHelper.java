package com.github.xachman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xach on 5/5/17.
 */


public class SQLiteDatabaseHelper {
    SQLiteDBCI dbc = null;

    public SQLiteDatabaseHelper(String dbname) {

        try {
            dbc = new SQLiteDBC(new File(dbname));
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
            List<Row> rows = dbc.executeQuery("SELECT * FROM " + table.tableName() + " ORDER BY id DESC LIMIT 1");
            return rows.get(0);
    }

    public void close() {
        dbc.close();
    }

    public List<Row> getRows(Table table) {
        List<Row> rows = dbc.executeQuery("SELECT * FROM " + table.tableName());
        return rows;
    }

    public Row getRowById(Table table, int id) {
        String sql = "SELECT * FROM " + table.tableName() + " WHERE id=" + id;
        List<Row> rows = dbc.executeQuery(sql);
        return rows.get(0);
    }

    public Row updateById(Table table, int id, List<String> values) {
            String sql = "UPDATE " + table.tableName() + " SET " + updateValuesSQL(table, values) + " WHERE id=" + id;
            System.out.println(sql);
            dbc.execute(sql);
            List<Row> rows = dbc.executeQuery("SELECT * FROM " + table.tableName() + " WHERE id=" + id);
            return rows.get(0);
    }

    public void removeById(Table table, int id) {
            String sql = "DELETE FROM " + table.tableName() + " WHERE id= " + id;
            dbc.execute(sql);
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

    private String updateValuesSQL(Table table, List<String> values) {
        String sqlString = "";
        for (int i = 0; i < table.columns().size(); i++) {
            Column column = table.columns().get(i);
            if(i < values.size()) {
                if(values.get(i) == null) continue;
                String value = values.get(i);
                sqlString += column.name() + "=";
                if (column.type() == "TEXT" || column.type().equals("VARCHAR")) {
                    sqlString += "'" + value + "'";
                } else {
                    sqlString += value;
                }
                if ((i + 1) < values.size()) {
                    sqlString += ",";
                }
            }
        }

        return sqlString;
    }

    public ArrayList<HashMap<String, String>> searchTable(Table table, String column, String value) {
        String sql = "SELECT * FROM " + table.tableName() + " WHERE " + column + " LIKE '%" + value + "%'";
        ArrayList<HashMap<String, String>> returnArray = new ArrayList<HashMap<String, String>>();
            List<Row> rows = dbc.executeQuery(sql);

            for (Row row: rows) {
                int count = 1;
                HashMap<String, String> returnMap = new HashMap<String, String>();

                for (Column tableColumn : table.columns()) {
                   // returnMap.put(tableColumn.name(), row.getString(count));
                    count++;
                }

                returnArray.add(returnMap);
            }

        return returnArray;
    }
}
