package com.github.xachman.sqlite;

import com.github.xachman.Row;
import com.github.xachman.Table;
import com.github.xachman.mocks.TestTableMock;
import com.github.xachman.mocks.UsersAnnotation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by xach on 5/7/17.
 */
public class SQLiteDatabaseTest {
    private String filePath;
    private SQLiteDatabase dbh;

    @Before
    public void before() {
        filePath = System.getProperty("user.dir")+"/src/test/mocks/database.db";
        dbh = new SQLiteDatabase(filePath);
    }

    @Test
    public void createTable() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        try {
            SQLiteDatabaseConnection dbc = new SQLiteDatabaseConnection(new File(filePath));
            List<Row> rows = dbc.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
            Row row = rows.get(0);

            Assert.assertEquals(testTable.tableName(), row.getEntry(0).getValue().toString());

            List<Row> pRows = dbc.executeQuery("PRAGMA table_info(users)");
            int count = 0;
            for(Row pRow: pRows) {
                Assert.assertEquals(pRow.getEntry(2).getValue().toString(), testTable.columns().get(count).type());
                count++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void createTableClass() {
        dbh.createTable(UsersAnnotation.class);
        try {
            SQLiteDatabaseConnection dbc = new SQLiteDatabaseConnection(new File(filePath));
            List<Row> rows = dbc.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
            Row row = rows.get(0);

            Assert.assertEquals("users_table", row.getEntry(0).getValue().toString());

            List<Row> pRows = dbc.executeQuery("PRAGMA table_info(users_table)");

            String[] types = {"INTEGER", "VARCHAR(255)", "VARCHAR(255)","VARCHAR(255)", "VARCHAR(30)", "VARCHAR(255)", "DATE", "CHARACTER(1)", "TINYINT"};
            
            Assert.assertTrue(pRows.size() == types.length);
            int count = 0;
            for(Row pRow: pRows) {
                Assert.assertEquals(pRow.getEntry(2).getValue().toString(), types[count]);
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
            SQLiteDatabaseConnection dbc = new SQLiteDatabaseConnection(new File(filePath));
            List<Row> rows = dbc.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
            Assert.assertEquals(1, rows.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void dropTableRemovesTableClass() {
        dbh.createTable(UsersAnnotation.class);

        dbh.dropTable(UsersAnnotation.class);


        try {
            SQLiteDatabaseConnection dbc = new SQLiteDatabaseConnection(new File(filePath));
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

        Map<String, String> map1 = new HashMap<>();
        map1.put("first_name", "test1");
        map1.put("last_name", "test2");
        map1.put("age", "23");

        Map<String, String> map2 = new HashMap<>();
        map2.put("first_name", "row2test1");
        map2.put("last_name", "row2test2");
        map2.put("age", "27");




        Row row = dbh.insert(testTable, map1);

        Assert.assertEquals("1", row.getEntry(0).getValue().toString());
        Assert.assertEquals("test1", row.getEntry(1).getValue().toString());
        Assert.assertEquals("test2", row.getEntry(2).getValue().toString());
        Assert.assertEquals("23", row.getEntry(3).getValue().toString());

        Row row2 = dbh.insert(testTable, map2);

        Assert.assertEquals("2", row2.getEntry(0).getValue().toString());
        Assert.assertEquals("row2test1", row2.getEntry(1).getValue().toString());
        Assert.assertEquals("row2test2", row2.getEntry(2).getValue().toString());
        Assert.assertEquals("27", row2.getEntry(3).getValue().toString());
    }

    @Test
    public void getRows() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        createInsert(dbh, testTable);

        List<Row> rows = dbh.getRows(testTable);

        Assert.assertEquals("test1", rows.get(0).getEntry(1).getValue().toString());
        Assert.assertEquals("test3", rows.get(1).getEntry(1).getValue().toString());
        Assert.assertEquals("test5", rows.get(2).getEntry(1).getValue().toString());
    }

    @Test
    public void getRowById() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        createInsert(dbh, testTable);

        Row row = dbh.getRowById(testTable, 2);

        Assert.assertEquals("test3", row.getEntry(1).getValue().toString());
    }

    @Test
    public void updateById() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        createInsert(dbh, testTable);

        Map<String, String> map = new HashMap<>();
        map.put("first_name", "test7");
        map.put("last_name", "test8");
        map.put("age", "30");

        Row row = dbh.updateById(testTable,2, map);

        Assert.assertEquals("2", row.getEntry(0).getValue().toString());
        Assert.assertEquals("test7", row.getEntry(1).getValue().toString());
        Assert.assertEquals("test8", row.getEntry(2).getValue().toString());
        Assert.assertEquals("30", row.getEntry(3).getValue().toString());
    }

    @Test
    public void removeById() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        createInsert(dbh, testTable);

        dbh.removeById(testTable, 1);

        List<Row> rows = dbh.getRows(testTable);

        dbh.close();

        Assert.assertEquals("2", rows.get(0).getEntry(0).getValue().toString());
        Assert.assertEquals("test3", rows.get(0).getEntry(1).getValue().toString());
        Assert.assertEquals("test4", rows.get(0).getEntry(2).getValue().toString());
        Assert.assertEquals("27", rows.get(0).getEntry(3).getValue().toString());

        Assert.assertEquals("3", rows.get(1).getEntry(0).getValue().toString());
        Assert.assertEquals("test5", rows.get(1).getEntry(1).getValue().toString());
        Assert.assertEquals("test6", rows.get(1).getEntry(2).getValue().toString());
        Assert.assertEquals("28", rows.get(1).getEntry(3).getValue().toString());

        Assert.assertEquals(2, rows.size());
    }

    @Test
    public void searchTable() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        createInsert(dbh, testTable);

        Map<String, String> map = new HashMap<>();
        map.put("column", "first_name");
        map.put("value", "test3");
        map.put("operator", "=");
        List<Row> rows = dbh.searchTable(testTable, new ArrayList<>(Arrays.asList(map)));

        dbh.close();

        Assert.assertEquals("2", rows.get(0).getEntry(0).getValue().toString());
        Assert.assertEquals("test3", rows.get(0).getEntry(1).getValue().toString());
        Assert.assertEquals("test4", rows.get(0).getEntry(2).getValue().toString());
        Assert.assertEquals("27", rows.get(0).getEntry(3).getValue().toString());

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

    private void createInsert(SQLiteDatabase dbh, Table testTable) {
        Map<String, String> map1 = new HashMap<>();
        map1.put("first_name", "test1");
        map1.put("last_name", "test2");
        map1.put("age", "26");


        Map<String, String> map2 = new HashMap<>();
        map2.put("first_name", "test3");
        map2.put("last_name", "test4");
        map2.put("age", "27");

        Map<String, String> map3 = new HashMap<>();
        map3.put("first_name", "test5");
        map3.put("last_name", "test6");
        map3.put("age", "28");

        dbh.insert(testTable, map1);
        dbh.insert(testTable, map2);
        dbh.insert(testTable, map3);
    }
}
