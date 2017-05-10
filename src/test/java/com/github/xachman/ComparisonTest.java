package com.github.xachman;

import com.github.xachman.Where.Comparison;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by xach on 5/10/17.
 */
public class ComparisonTest {

    @Test
    public void constantsAreEqual() {

        Assert.assertEquals(Comparison.LIKE, Comparison.LIKE);
        Assert.assertNotEquals(Comparison.LIKE, Comparison.EQUALS);
        Assert.assertEquals(Comparison.GREATERTHAN, Comparison.GREATERTHAN);
        Assert.assertEquals(Comparison.EQUALS, Comparison.EQUALS);
    }

}
