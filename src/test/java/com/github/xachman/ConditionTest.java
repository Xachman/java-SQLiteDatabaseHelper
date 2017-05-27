package com.github.xachman;

import com.github.xachman.Where.Comparison;
import com.github.xachman.Where.Condition;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;

/**
 * Created by xach on 5/9/17.
 */
public class ConditionTest {

    @Test
    public void equals() {
        Condition condition = new Condition(new Entry(new Column("text", "name"), "Ted"), Comparison.EQUALS);

        String compare = "name='Ted'";

        Assert.assertEquals(compare, condition.toString());
    }

    @Test
    public void like() {
        Condition condition = new Condition(new Entry(new Column("text", "name"), "Ted"), Comparison.LIKE);

        String compare = "name LIKE 'Ted'";

        Assert.assertEquals(compare, condition.toString());
    }
    @Test
    public void greaterThan() {
        Condition condition = new Condition(new Entry(new Column("text", "name"), "Ted"), Comparison.GREATERTHAN);

        String compare = "name>'Ted'";

        Assert.assertEquals(compare, condition.toString());
    }
    @Test
    public void lessThan() {
        Condition condition = new Condition(new Entry(new Column("text", "name"), "Ted"), Comparison.LESSTHAN);

        String compare = "name<'Ted'";

        Assert.assertEquals(compare, condition.toString());
    }

    @Test
    public void likeWildcard() {
        Condition condition = new Condition(new Entry( new Column("TEXT", "name"),"Ted"),Comparison.LIKE);

        String compare = "name LIKE '%Ted'";

        new Entry(new Column("text", "name"), "Ted");
    }
}
