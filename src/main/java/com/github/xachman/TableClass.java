/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.xachman;

import java.lang.annotation.Annotation;
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
        com.github.xachman.annotations.Table tableA = (com.github.xachman.annotations.Table) c.getAnnotation(com.github.xachman.annotations.Table.class);
        if(tableA != null) {
            tableName = tableA.name();
            return;
        }
        String name = c.getSimpleName().toLowerCase();
        String lastChar = name.substring(name.length()-1);

        if(lastChar.equals("s")) {
            tableName = name;
            return;
        }
        
        tableName = c.getSimpleName().toLowerCase()+"s";
    }
    
    private Column assembleColumnFromField(Field field) {
        String name = convertCamelCase(field.getName());
        com.github.xachman.annotations.Column columnA = (com.github.xachman.annotations.Column) field.getAnnotation(com.github.xachman.annotations.Column.class);
        int length = 0;
        boolean primarykey = false;
        boolean autoincrement = false;
        if(columnA != null) { 
            String type = columnA.type();
            length = columnA.length();
            primarykey = columnA.primarykey();
            autoincrement = columnA.autoincrement();
            if(!type.equals("")) {
                return makeColumn(type, name, length, primarykey, autoincrement);
            }
        }
        if(field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
            return makeColumn("INTEGER", name, length, primarykey, autoincrement);
        }
        if(field.getType().equals(String.class)) {
            return makeColumn("VARCHAR", name, (length > 0)? length: 255, primarykey, autoincrement);
        }
        if(field.getType().equals(boolean.class)) {
            return makeColumn("TINYINT", name, length, primarykey, autoincrement);
        }
        if(field.getType().equals(Date.class)) {
            return makeColumn("DATETIME", name, length, primarykey, autoincrement);
        }
        if(field.getType().equals(char.class)) {
            return makeColumn("CHARACTER", name, 1, primarykey, autoincrement);
        }
        return makeColumn("VARCHAR", name, (length > 0)? length: 255, primarykey, autoincrement);
    }  
    
    private String convertCamelCase(String input) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        String result = input
                           .replaceAll(regex, replacement)
                           .toLowerCase();
        return result;
    }

    private Column makeColumn(String type, String name, int length, boolean primarykey, boolean autoincrement) {
        if(length > 0) {
            type = type+"("+length+")";
        }
        return new Column(type, name, primarykey, autoincrement);
    }
    
    private Column makeColumn(String type, String name, int length) {
        return makeColumn(type, name, length, false, false);
    }
    
}
