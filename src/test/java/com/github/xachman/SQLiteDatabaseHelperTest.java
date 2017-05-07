package com.github.xachman;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by xach on 5/7/17.
 */
public class SQLiteDatabaseHelperTest {

    @Test
    public void createTable() {
        String filePath = System.getProperty("user.dir")+"/src/test/mocks/database.db";
        SQLiteDatabaseHelper dbh = new SQLiteDatabaseHelper(filePath);
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
