/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.xachman;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author xach
 */
public class TableClass extends Table {
    private String tableName;
    private List<Column> columns;
    private Class c;
    
    public TableClass(Class c) {
        this.c = c;
        setUpName();
        setUpColumns();
        
        
    }

    @Override
    public List<Column> columns() {
        return columns;
    }

    @Override
    public String tableName() {
        return tableName;
    }

    private void setUpColumns() {
        List<Column> columns = new ArrayList<>();
        for(Field field: c.getDeclaredFields()) {
        System.out.println(field);        
            field.setAccessible(true);
            columns.add(assembleColumnFromField(field));
        }

        this.columns = columns;
    }

    private void setUpName() {
        tableName = c.getSimpleName().toLowerCase();
    }
    
    private Column assembleColumnFromField(Field field) {
        if(field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
            return new Column("integer", convertCamelCase(field.getName()));
        }
        if(field.getType().equals(String.class)) {
            return new Column("VARCHAR(255)", convertCamelCase(field.getName()));
        }
        if(field.getType().equals(boolean.class)) {
            return new Column("TINYINT", convertCamelCase(field.getName()));
        }
        if(field.getType().equals(Date.class)) {
            return new Column("DATETIME", convertCamelCase(field.getName()));
        }
        if(field.getType().equals(char.class)) {
            return new Column("CHARACTER(1)",convertCamelCase(field.getName()));
        }
        return new Column("VARCHAR(255)", convertCamelCase(field.getName()));
    }  

    private String convertCamelCase(String input) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        String result = input
                           .replaceAll(regex, replacement)
                           .toLowerCase();
        return result;
    }
}
