/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.xachman;

import com.github.xachman.mocks.Users;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author xach
 */
public class TableClassTest {
    @Test
    public void testCreateTableSql() {
        TableClass tableClass = new TableClass(Users.class);
        
        String sql = tableClass.createTableSQL();

        String expectSql = "CREATE TABLE IF NOT EXISTS users ("+ 
                            "id INT,"+
                            "first_name VARCHAR(255),"+
                            "last_name VARCHAR(255),"+
                            "password VARCHAR(255),"+
                            "email VARCHAR(255),"+
                            "created_date DATE,"+ 
                            "type CARACTER(1),"+
                            "active TINYINT"+
                            ")";

        Assert.assertEquals(expectSql, sql);
    }    
}
