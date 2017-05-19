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

        Row row = dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test1","test2","23")));

        Assert.assertEquals(row.getEntry(0).getValue(), "1");
        Assert.assertEquals(row.getEntry(1).getValue(), "test1");
        Assert.assertEquals(row.getEntry(2).getValue(), "test2");
        Assert.assertEquals(row.getEntry(3).getValue(), "23");

        Row row2 = dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"row2test1","row2test2","27")));

        Assert.assertEquals(row2.getEntry(0).getValue(), "2");
        Assert.assertEquals(row2.getEntry(1).getValue(), "row2test1");
        Assert.assertEquals(row2.getEntry(2).getValue(), "row2test2");
        Assert.assertEquals(row2.getEntry(3).getValue(), "27");
    }

    @Test
    public void getRows() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test1","test2","26")));
        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test3","test4","27")));
        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test5","test6","28")));

        List<Row> rows = dbh.getRows(testTable);

        Assert.assertEquals(rows.get(0).getEntry(1).getValue(), "test1");
        Assert.assertEquals(rows.get(1).getEntry(1).getValue(), "test3");
        Assert.assertEquals(rows.get(2).getEntry(1).getValue(), "test5");
    }

    @Test
    public void getRowById() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test1","test2","26")));
        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test3","test4","27")));
        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test5","test6","28")));

        Row row = dbh.getRowById(testTable, 2);

        Assert.assertEquals(row.getEntry(1).getValue(), "test3");
    }

    @Test
    public void updateById() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test1","test2","26")));
        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test3","test4","27")));
        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test5","test6","28")));

        Row row = dbh.updateById(testTable,2,Arrays.asList(null,"test7","test8","30"));

        Assert.assertEquals(row.getEntry(0).getValue(), "2");
        Assert.assertEquals(row.getEntry(1).getValue(), "test7");
        Assert.assertEquals(row.getEntry(2).getValue(), "test8");
        Assert.assertEquals(row.getEntry(3).getValue(), "30");
    }

    @Test
    public void removeById() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test1","test2","26")));
        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test3","test4","27")));
        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test5","test6","28")));

        dbh.removeById(testTable, 1);

        List<Row> rows = dbh.getRows(testTable);

        dbh.close();

        Assert.assertEquals(rows.get(0).getEntry(0).getValue(), "2");
        Assert.assertEquals(rows.get(0).getEntry(1).getValue(), "test3");
        Assert.assertEquals(rows.get(0).getEntry(2).getValue(), "test4");
        Assert.assertEquals(rows.get(0).getEntry(3).getValue(), "27");

        Assert.assertEquals(rows.get(1).getEntry(0).getValue(), "3");
        Assert.assertEquals(rows.get(1).getEntry(1).getValue(), "test5");
        Assert.assertEquals(rows.get(1).getEntry(2).getValue(), "test6");
        Assert.assertEquals(rows.get(1).getEntry(3).getValue(), "28");

        Assert.assertEquals(rows.size(), 2);
    }

    @Test
    public void searchTable() {
        Table testTable = new TestTableMock();
        dbh.createTable(testTable);

        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test1","test2","26")));
        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test3","test4","27")));
        dbh.insert(testTable, new ArrayList<String>(Arrays.asList(null,"test5","test6","28")));

        Condition condition = new Condition("first_name", "'test3'", Comparison.EQUALS);

        Where where = new Where(new ArrayList<Condition>(Arrays.asList(condition)));

        List<Row> rows = dbh.searchTable(testTable, where);

        System.out.println(where.toString());

        dbh.close();

        Assert.assertEquals(rows.get(0).getEntry(0).getValue(), "2");
        Assert.assertEquals(rows.get(0).getEntry(1).getValue(), "test3");
        Assert.assertEquals(rows.get(0).getEntry(2).getValue(), "test4");
        Assert.assertEquals(rows.get(0).getEntry(3).getValue(), "27");

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
