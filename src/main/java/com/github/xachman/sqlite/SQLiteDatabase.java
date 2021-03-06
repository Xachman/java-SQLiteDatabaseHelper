package com.github.xachman.sqlite;

import com.github.xachman.Column;
import com.github.xachman.DatabaseConnectionI;
import com.github.xachman.DatabaseI;
import com.github.xachman.Entry;
import com.github.xachman.Row;
import com.github.xachman.Table;
import com.github.xachman.TableClass;
import com.github.xachman.Value;
import com.github.xachman.ValueType;
import com.github.xachman.Where.Comparison;
import com.github.xachman.Where.Condition;
import com.github.xachman.Where.Where;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by xach on 5/5/17.
 */


public class SQLiteDatabase implements DatabaseI {
    private DatabaseConnectionI dbc = null;
    private String dbpath;

    public SQLiteDatabase(String dbpath) {
        this.dbpath = dbpath;
        open();
    }

    public void open() {
        try {
            dbc = new SQLiteDatabaseConnection(new File(dbpath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTable(Table table) {
            dbc.execute(table.createTableSQL());
    }

    @Override
    public void dropTable(Table table) {
            dbc.execute("DROP TABLE IF EXISTS " + table.tableName());
    }

    @Override
    public Row insert(Table table, Map<String,String> values) {
            String sql = "INSERT INTO " + table.tableName() +" " + insertValuesSQL(table, values);
            dbc.prepareUpdateStatement(sql, convertColumnMapToValueList(table, values));
            List<Row> rows = dbc.prepareStatement("SELECT * FROM " + table.tableName() + " ORDER BY id DESC LIMIT 1", null);
            return rows.get(0);
    }

    public void close() {
        dbc.close();
    }

    @Override
    public List<Row> getRows(Table table) {
        List<Row> rows = dbc.prepareStatement("SELECT * FROM " + table.tableName(), null);
        return rows;
    }

    @Override
    public Row getRowById(Table table, int id) {
        String sql = "SELECT * FROM " + table.tableName() + " WHERE id=?";
        List<Row> rows = dbc.prepareStatement(sql, new ArrayList<Value>(Arrays.asList(new Value(ValueType.NUMBER, ""+id))));
        return rows.get(0);
    }

    @Override
    public Row updateById(Table table, int id, Map<String, String> values) {
            String sql = "UPDATE " + table.tableName() + " SET " + updateValuesSQL(table, values) + " WHERE id=?";
            List<Value> prepareValues = convertColumnMapToValueList(table, values);

            prepareValues.add(new Value(ValueType.NUMBER, Integer.toString(id)));
            dbc.prepareUpdateStatement(sql, prepareValues);
            List<Row> rows = dbc.executeQuery("SELECT * FROM " + table.tableName() + " WHERE id=" + id);
            return rows.get(0);
    }

    @Override
    public boolean removeById(Table table, int id) {
            String sql = "DELETE FROM " + table.tableName() + " WHERE id= " + id;
            dbc.execute(sql);
            return true;
    }

    @Override
    public List<Row> searchTable(Table table, List<Map<String, String>> maps) {
            List<Condition> conditions = new ArrayList<Condition>();
            for(Map<String, String> map:  maps) {
                try {
                    conditions.add(new Condition(new Entry(getColumnFromTable(table, map.get("column")), map.get("value")), getComparisonFromString(map.get("operator"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        Where where = new Where(conditions);
        String sql = "SELECT * FROM " + table.tableName() + " WHERE " + where.toPreparedString();
        List<Row> rows = dbc.prepareStatement(sql, where.values());
        return rows;
    }
    @Override
    public void createTable(Class c) {
        createTable(new TableClass(c));
    }

    @Override
    public void dropTable(Class c) {
        dropTable(new TableClass(c));
    }

    @Override
    public Row insert(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> List<T> getRows(Class c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> List<T> getRowById(Class c, int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T updateById(Class c, int id, Object object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeById(Class c, int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> List<T> searchTable(Class c, List<Map<String, String>> maps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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


    private Column getColumnFromTable(Table table, String columnName) throws Exception{
        for(Column column: table.columns()) {
            if(column.name().equals(columnName)) {
                return column;
            }
        }

        throw new Exception("Column Not Found");
    }

    private Comparison getComparisonFromString(String operator) throws Exception {
        switch (operator) {
            case "=":
                return Comparison.EQUALS;
            case "LIKE":
                return Comparison.LIKE;
            case ">":
                return Comparison.GREATERTHAN;
            case "<":
                return Comparison.LESSTHAN;
            default:
                throw new Exception("Operator not found");
        }
    }

}
