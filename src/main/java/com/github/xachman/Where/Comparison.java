package com.github.xachman.Where;

/**
 * Created by xach on 5/10/17.
 */
public class Comparison {

    private int value;
    private transient String name;

    public static final Comparison EQUALS   = new Comparison( 0, "Equals"   );
    public static final Comparison GREATERTHAN  = new Comparison( 1, "GreaterThan"  );
    public static final Comparison LESSTHAN = new Comparison( 2, "LessThan" );
    public static final Comparison LIKE = new Comparison( 3, "Like" );
    private Comparison( int value, String name )
    {
        this.value = value;
        this.name = name;
    }

    public int getValue()
    {
        return value;
    }

    public String toString()
    {
        return name;
    }
}
