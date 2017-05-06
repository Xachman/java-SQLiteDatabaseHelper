package com.github.xachman;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by xach on 5/6/17.
 */
public class SQLiteDBCTest {
    @Test(expected = FileNotFoundException.class)
    public void throwErrorIfNoFile() throws FileNotFoundException {
        String path = System.getProperty("user.dir")+"/src/test/mocks/tmp/nodatabase.db";
        SQLiteDBC dbc = new SQLiteDBC(new File(path));
    }

    @Test
    public void insertValues() throws FileNotFoundException {
        SQLiteDBC dbc = new SQLiteDBC(new File(System.getProperty("user.dir")+"/src/test/mocks/database.db"));
        dbc.execute("CREATE TABLE test_table (test1 int, test2 text, test3 int)");
        dbc.execute("INSERT INTO test_table (test1, test2, test3) VALUES (3, 'test text', 4)");

        List<Row> rows = dbc.executeQuery("SELECT * FROM test_table");
        dbc.close();

        Row row = rows.get(0);

        Assert.assertEquals(row.getColumn("test1"), "3");
    }

    @After
    public void deleteDatabaseFileAndCreate() {

    }
}
