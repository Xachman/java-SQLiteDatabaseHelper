/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.xachman;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
            field.setAccessible(true);
            
        }

        this.columns = columns;
    }

    private void setUpName() {
        tableName = c.getSimpleName().toLowerCase();
    }
    
    private Column assembleColumnFromField(Field field) {
        if(field.getType().equals(Integer.class)) {
            return new Column();
        }
    }    
}
