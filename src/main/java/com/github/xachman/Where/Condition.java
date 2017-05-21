package com.github.xachman.Where;

import com.github.xachman.Entry;

import java.sql.Connection;

/**
 * Created by xach on 5/9/17.
 */
public class Condition {
    private String column;
    private String compare;
    private Entry entry;
    private Comparison comparison;


    public Condition(String column, String compare, Comparison comparison) {
        this.comparison = comparison;
        this.compare = compare;
        this.column = column;
    }

    public Condition(Entry entry, Comparison comparison) {
        this.comparison = comparison;
        this.entry = entry;
        convertEntryToCompare(entry);
    }


    @Override
    public String toString() {
        String sql = column;
        sql += getOperatorFromComparison();
        if(entry != null) {
            return sql+entry.getValue().toSql();
        }

        return sql += compare;
    }
    private String getOperatorFromComparison() {
       if (comparison == Comparison.EQUALS) {
            return "=";
       } else if (comparison == Comparison.LIKE) {
            return " LIKE ";
       } else if (comparison == Comparison.GREATERTHAN) {
            return ">";
       } else if (comparison == Comparison.LESSTHAN) {
            return "<";
       }


       return null;
    }

    private void convertEntryToCompare(Entry entry) {
        String compare = "";
        if(entry.getColumn().type().equals("TEXT") || entry.getColumn().type().equals("VARCHAR")) {
            compare = "'"+entry.getValue()+"'";
        }

        column = entry.getColumn().name();
        this.compare = compare;
    }

}
