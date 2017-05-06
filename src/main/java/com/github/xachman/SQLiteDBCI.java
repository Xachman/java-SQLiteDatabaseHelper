package com.github.xachman;

import java.util.List;

/**
 * Created by xach on 5/5/17.
 */
public interface SQLiteDBCI {
    public boolean execute(String sql);
    public List<Row> executeQuery(String sql);

    public void close();

}
