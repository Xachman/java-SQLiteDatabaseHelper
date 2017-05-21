package com.github.xachman;

/**
 * Created by xach on 5/10/17.
 */
public class ValueType {

    private int value;
    private transient String name;

    public static final ValueType STRING   = new ValueType( 0, "String"   );
    public static final ValueType INTEGER  = new ValueType( 1, "Integer"  );
    public static final ValueType NUMBER  = new ValueType( 1, "Integer");
    private ValueType( int value, String name )
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
