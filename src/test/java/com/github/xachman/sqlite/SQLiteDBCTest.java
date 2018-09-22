package com.github.xachman.sqlite;

import com.github.xachman.Column;
import com.github.xachman.Entry;
import com.github.xachman.Row;
import com.github.xachman.Value;
import com.github.xachman.ValueType;
import com.github.xachman.sqlite.SQLiteDatabaseConnection;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xach on 5/6/17.
 */
public class SQLiteDBCTest {
    @Test(expected = FileNotFoundException.class)
    public void throwErrorIfNoFile() throws FileNotFoundException {
        String path = System.getProperty("user.dir")+"/src/test/mocks/tmp/nodatabase.db";
        SQLiteDatabaseConnection dbc = new SQLiteDatabaseConnection(new File(path));
    }

    @Test
    public void insertValues() throws FileNotFoundException {
        SQLiteDatabaseConnection dbc = new SQLiteDatabaseConnection(new File(System.getProperty("user.dir")+"/src/test/mocks/database.db"));
        dbc.execute("CREATE TABLE test_table (test1 int, test2 text, test3 int)");
        dbc.execute("INSERT INTO test_table (test1, test2, test3) VALUES (3, 'test text', 4)");

        List<Row> rows = dbc.executeQuery("SELECT * FROM test_table");
        dbc.close();

        Row row = rows.get(0);

        List<Row> compList = new ArrayList<Row>();
        List<Entry> compEntries = new ArrayList<Entry>();
        compEntries.add(new Entry(new Column("int", "test1"), new Value(ValueType.NUMBER, "3")));
        compEntries.add(new Entry(new Column("text", "test2"), new Value(ValueType.STRING, "test text")));
        compEntries.add(new Entry(new Column("int", "test3"), new Value(ValueType.NUMBER, "4")));
        compList.add(new Row(compEntries));

        Row compRow = compList.get(0);

        Entry entry1 = row.getEntry(0);
        Entry compEntry1 = compRow.getEntry(0);

        Assert.assertEquals(entry1.getValue().toString(), compEntry1.getValue().toString());
        Assert.assertEquals(entry1.getColumn().type(), compEntry1.getColumn().type());
        Assert.assertEquals(entry1.getColumn().name(), compEntry1.getColumn().name());

        Entry entry2 = row.getEntry(1);
        Entry compEntry2 = compRow.getEntry(1);

        Assert.assertEquals(entry2.getValue().toString(), compEntry2.getValue().toString());
        Assert.assertEquals(entry2.getColumn().type(), compEntry2.getColumn().type());
        Assert.assertEquals(entry2.getColumn().name(), compEntry2.getColumn().name());

        Entry entry3 = row.getEntry(2);
        Entry compEntry3 = compRow.getEntry(2);

        Assert.assertEquals(entry3.getValue().toString(), compEntry3.getValue().toString());
        Assert.assertEquals(entry3.getColumn().type(), compEntry3.getColumn().type());
        Assert.assertEquals(entry3.getColumn().name(), compEntry3.getColumn().name());
    }
    @Test
    public void preparedStatement() throws FileNotFoundException {
        SQLiteDatabaseConnection dbc = new SQLiteDatabaseConnection(new File(System.getProperty("user.dir")+"/src/test/mocks/database.db"));
        dbc.execute("CREATE TABLE test_table (test1 int, test2 text, test3 int)");
        dbc.execute("INSERT INTO test_table (test1, test2, test3) VALUES (3, 'test text', 4)");

        List<Value> values = new ArrayList<Value>();

        Value value1 = new Value(ValueType.INTEGER, "3");
        Value value2 = new Value(ValueType.STRING, "test text");
        Value value3 = new Value(ValueType.INTEGER, "4");

        values.add(value1);
        values.add(value2);
        values.add(value3);

        List<Row> rows = dbc.prepareStatement("SELECT * FROM test_table WHERE test1=? AND test2=? AND test3=?", values);

        Row row = rows.get(0);
        Assert.assertEquals(row.getEntry(0).getValue().toString(), new Value(ValueType.NUMBER, "3").toString());
        Assert.assertEquals(row.getEntry(1).getValue().toString(), new Value(ValueType.STRING, "test text").toString());
        Assert.assertEquals(row.getEntry(2).getValue().toString(), new Value(ValueType.NUMBER,"4").toString());
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
