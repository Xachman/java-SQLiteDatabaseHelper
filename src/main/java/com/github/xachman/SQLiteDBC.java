package com.github.xachman;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
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
        System.out.println(url);
        try {
            connection = DriverManager.getConnection(url);
            System.out.println(connection.isClosed());
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
}
