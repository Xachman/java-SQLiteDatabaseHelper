package com.github.xachman.mocks;

import com.github.xachman.Column;
import com.github.xachman.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xach on 5/7/17.
 */
public class TestTableMock extends Table {

    @Override
    public List<Column> columns() {
        List<Column> columns = new ArrayList<Column>();

        columns.add(new Column("integer", "id", true, true));
        columns.add(new Column("varchar", "first_name"));
        columns.add(new Column("varchar", "last_name"));
        columns.add(new Column("int", "age"));
        columns.add(new Column("varchar", "email"));
        columns.add(new Column("text", "description"));

        return columns;
    }

    @Override
    public String tableName() {
        return "users";
    }

    public boolean autoincrement() {
        return true;
    }
}
