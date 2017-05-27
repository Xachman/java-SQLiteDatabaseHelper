package com.github.xachman;

import com.github.xachman.Where.Comparison;
import com.github.xachman.Where.Condition;
import com.github.xachman.Where.Where;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xach on 5/10/17.
 */
public class WhereTest {

    @Test
    public void constructOneConditionList() {
        List<Condition> conditions = new ArrayList<Condition>();

        conditions.add(new Condition(new Entry(new Column("TEXT", "first_name"), "Ted"), Comparison.EQUALS));
        conditions.add(new Condition(new Entry(new Column("INT",  "age"), "21"), Comparison.GREATERTHAN));
        conditions.add(new Condition(new Entry(new Column("TEXT", "last_name"), "Thomas"), Comparison.EQUALS));

        String compare = "first_name='Ted' AND age>21 AND last_name='Thomas'";

        Where where = new Where(conditions);

        Assert.assertEquals(compare, where.toString());
    }

    @Test
    public void preparedStatementString() {

        List<Condition> conditions = new ArrayList<Condition>();

        conditions.add(new Condition(new Entry(new Column("TEXT", "first_name"), "Ted"), Comparison.EQUALS));
        conditions.add(new Condition(new Entry(new Column("INT",  "age"), "23"), Comparison.GREATERTHAN));
        conditions.add(new Condition(new Entry(new Column("TEXT", "last_name"), "Thomas"), Comparison.EQUALS));

        String compare = "first_name=? AND age>? AND last_name=?";

        Where where = new Where(conditions);

        Assert.assertEquals(compare, where.toPreparedString());
    }
}
