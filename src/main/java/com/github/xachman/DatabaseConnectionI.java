package com.github.xachman;

import java.util.List;

/**
 * Created by xach on 5/5/17.
 */
public interface DatabaseConnectionI {
    public boolean execute(String sql);
    public List<Row> executeQuery(String sql);
    public List<Row> prepareStatement(String sql, List<Value> values);

    public void prepareUpdateStatement(String sql, List<Value> values);

    public void close();

}
