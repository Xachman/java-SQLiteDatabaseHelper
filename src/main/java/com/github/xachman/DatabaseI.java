/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.xachman;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xach
 */
public interface DatabaseI {

    public void createTable(Table table);
    public void dropTable(Table table);
    public Row insert(Table table, Map<String,String> map);
    public List<Row> getRows(Table table);
    public Row getRowById(Table table, int id);
    public Row updateById(Table table, int id, Map<String, String> values); 
    public boolean removeById(Table table, int id);
    public List<Row> searchTable(Table table, List<Map<String, String>> maps);
    
    public void createTable(Class c);
    public void dropTable(Class c);
    public Row insert(Object o);
    public <T> List<T> getRows(Class c);
    public <T> List<T> getRowById(Class c, int id);
    public <T> T updateById(Class c, int id, Object object); 
    public boolean removeById(Class c, int id);
    public <T> List<T> searchTable(Class c, List<Map<String, String>> maps);
    
    
}
