package com.github.xachman;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xach on 5/5/17.
 */
public class SQLiteDBC implements SQLiteDBCI {
    private Connection connection;
    public SQLiteDBC(File file) throws FileNotFoundException {
        if(!file.exists()) {
            throw new FileNotFoundException();
        }
        String url = "jdbc:sqlite:"+file.getPath();
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean execute(String sql) {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Row> executeQuery(String sql) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery(sql);
            List<Row> rows = convertResultsToRows(results);
            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Row> prepareStatement(String sql, List<Value> values) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            setStmtToValues(stmt, values);
            return convertResultsToRows(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void prepareUpdateStatement(String sql, List<Value> values) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            setStmtToValues(stmt, values);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Row> convertResultsToRows(ResultSet results) {
        ResultSetMetaData rsmd;
        List<Row> rows = new ArrayList<Row>();
        try {
            rsmd = results.getMetaData();
            while (results.next()) {
                List<Entry> ententries = new ArrayList<Entry>();
                for( int i = 0; i < rsmd.getColumnCount(); i ++) {
                    int index = i+1;
                    String columnType = rsmd.getColumnTypeName(index);
                    Value value;
                    if (columnType.equals("INT") || columnType.equals("INTEGER") || columnType.equals("DOUBLE") || columnType.equals("FLOAT")) {
                       value = new Value(ValueType.NUMBER, results.getString(index));
                    } else {
                        value = new Value(ValueType.STRING, results.getString(index));
                    }
                    ententries.add(new Entry(new Column(rsmd.getColumnTypeName(index), rsmd.getColumnName(index)), value));
                }
                rows.add(new Row(ententries));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    private void setStmtToValues(PreparedStatement stmt, List<Value> values) throws SQLException {
        int count = 1;
        if(values == null) return;
       for (Value value: values) {
           if(value == null) {
               count++;
               continue;
           }
           System.out.println(value.getValue());
           if(value.getType() == ValueType.INTEGER || value.getType() == ValueType.NUMBER) {
               stmt.setInt(count, Integer.parseInt(value.getValue()));
           }else if(value.getType() == ValueType.STRING) {
               stmt.setString(count, value.getValue());
           }
           count ++;
       }
    }
}
