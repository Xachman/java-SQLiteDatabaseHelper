package com.github.xachman;

import com.github.xachman.Where.Comparison;
import com.github.xachman.Where.Condition;
import com.github.xachman.Where.Where;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

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

            Assert.assertEquals(testTable.tableName().toString(), row.getEntry(0).getValue().toString());

            List<Row> pRows = dbc.executeQuery("PRAGMA table_info(users)");
            int count = 0;
            for(Row pRow: pRows) {
                Assert.assertEquals(pRow.getEntry(2).getValue().toString(), testTable.columns().get(count).type().toString());
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

        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("first_name", "test1");
        map1.put("last_name", "test2");
        map1.put("age", "23");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("first_name", "row2test1");
        map2.put("last_name", "row2test2");
        map2.put("age", "27");




        Row row = dbh.insert(testTable, map1);

        Assert.assertEquals(row.getEntry(0).getValue().toString(), "1");
        Assert.assertEquals(row.getEntry(1).getValue().toString(), "test1");
        Assert.assertEquals(row.getEntry(2).getValue().toString(), "test2");
        Assert.assertEquals(row.getEntry(3).getValue().toString(), "23");

        Row row2 = dbh.insert(testTable, map2);

        Assert.assertEquals(row2.getEntry(0).getValue().toString(), "2");
        Assert.assertEquals(row2.getEntry(1).getValue().toString(), "row2test1");
        Assert.assertEquals(row2.getEntry(2).getValue().toString(), "row2test2");
        Assert.assertEquals(row2.getEntry(3).getValue().toString(), "27");
    }

    @Test
    public void getRows() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        createInsert(dbh, testTable);

        List<Row> rows = dbh.getRows(testTable);

        Assert.assertEquals(rows.get(0).getEntry(1).getValue().toString(), "test1");
        Assert.assertEquals(rows.get(1).getEntry(1).getValue().toString(), "test3");
        Assert.assertEquals(rows.get(2).getEntry(1).getValue().toString(), "test5");
    }

    @Test
    public void getRowById() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        createInsert(dbh, testTable);

        Row row = dbh.getRowById(testTable, 2);

        Assert.assertEquals(row.getEntry(1).getValue().toString(), "test3");
    }

    @Test
    public void updateById() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        createInsert(dbh, testTable);

        Map<String, String> map = new HashMap<String, String>();
        map.put("first_name", "test7");
        map.put("last_name", "test8");
        map.put("age", "30");

        Row row = dbh.updateById(testTable,2, map);

        Assert.assertEquals(row.getEntry(0).getValue().toString(), "2");
        Assert.assertEquals(row.getEntry(1).getValue().toString(), "test7");
        Assert.assertEquals(row.getEntry(2).getValue().toString(), "test8");
        Assert.assertEquals(row.getEntry(3).getValue().toString(), "30");
    }

    @Test
    public void removeById() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        createInsert(dbh, testTable);

        dbh.removeById(testTable, 1);

        List<Row> rows = dbh.getRows(testTable);

        dbh.close();

        Assert.assertEquals(rows.get(0).getEntry(0).getValue().toString(), "2");
        Assert.assertEquals(rows.get(0).getEntry(1).getValue().toString(), "test3");
        Assert.assertEquals(rows.get(0).getEntry(2).getValue().toString(), "test4");
        Assert.assertEquals(rows.get(0).getEntry(3).getValue().toString(), "27");

        Assert.assertEquals(rows.get(1).getEntry(0).getValue().toString(), "3");
        Assert.assertEquals(rows.get(1).getEntry(1).getValue().toString(), "test5");
        Assert.assertEquals(rows.get(1).getEntry(2).getValue().toString(), "test6");
        Assert.assertEquals(rows.get(1).getEntry(3).getValue().toString(), "28");

        Assert.assertEquals(rows.size(), 2);
    }

    @Test
    public void searchTable() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        createInsert(dbh, testTable);

        Condition condition = new Condition("first_name", "'test3'", Comparison.EQUALS);

        Where where = new Where(new ArrayList<Condition>(Arrays.asList(condition)));

        List<Row> rows = dbh.searchTable(testTable, where);

        dbh.close();

        Assert.assertEquals(rows.get(0).getEntry(0).getValue().toString(), "2");
        Assert.assertEquals(rows.get(0).getEntry(1).getValue().toString(), "test3");
        Assert.assertEquals(rows.get(0).getEntry(2).getValue().toString(), "test4");
        Assert.assertEquals(rows.get(0).getEntry(3).getValue().toString(), "27");

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

    private void createInsert(SQLiteDatabaseHelper dbh, Table testTable) {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("first_name", "test1");
        map1.put("last_name", "test2");
        map1.put("age", "26");


        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("first_name", "test3");
        map2.put("last_name", "test4");
        map2.put("age", "27");

        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("first_name", "test5");
        map3.put("last_name", "test6");
        map3.put("age", "28");

        dbh.insert(testTable, map1);
        dbh.insert(testTable, map2);
        dbh.insert(testTable, map3);
    }
}
