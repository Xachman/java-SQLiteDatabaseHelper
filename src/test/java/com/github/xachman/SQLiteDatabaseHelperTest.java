package com.github.xachman;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xach on 5/7/17.
 */
public class SQLiteDatabaseHelperTest {
    private String filePath;
    private SQLiteDatabaseHelper dbh;

    @Before
    public void before() {
        filePath = System.getProperty("user.dir")+"/src/test/mocks/database.db";
        dbh = new SQLiteDatabaseHelper(filePath);
    }

    @Test
    public void createTable() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        try {
            SQLiteDBC dbc = new SQLiteDBC(new File(filePath));
            List<Row> rows = dbc.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
            Row row = rows.get(0);

            Assert.assertEquals(testTable.tableName(), row.getEntry(0).getValue());

            List<Row> pRows = dbc.executeQuery("PRAGMA table_info(users)");
            int count = 0;
            for(Row pRow: pRows) {
                Assert.assertEquals(pRow.getEntry(2).getValue(), testTable.columns().get(count).type());
                count++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void dropTableRemovesTable() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        dbh.dropTable(testTable);


        try {
            SQLiteDBC dbc = new SQLiteDBC(new File(filePath));
            List<Row> rows = dbc.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
            Assert.assertEquals(1, rows.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void insertMultipleValues() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        Row row = dbh.insert(testTable, new ArrayList<String>(Arrays.asList("test1","test2","test3")));

        Assert.assertEquals(row.getEntry(0).getValue(), "test1");


    }
    @After
    public void deleteDatabaseFileAndCreate() {
        String filePath = System.getProperty("user.dir")+"/src/test/mocks/database.db";
        File dbFile = new File(filePath);
        dbFile.delete();
        try {
            dbFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
