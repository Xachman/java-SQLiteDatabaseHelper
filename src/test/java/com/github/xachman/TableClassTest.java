/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.xachman;

import com.github.xachman.mocks.User;
import com.github.xachman.mocks.UsersAnnotation;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author xach
 */
public class TableClassTest {
    @Test
    public void testCreateTableSql() {
        TableClass tableClass = new TableClass(User.class);
        
        String sql = tableClass.createTableSQL();

        String expectSql = "CREATE TABLE IF NOT EXISTS users ("+ 
                            "id INTEGER,"+
                            "first_name VARCHAR(255),"+
                            "last_name VARCHAR(255),"+
                            "password VARCHAR(255),"+
                            "username VARCHAR(255),"+
                            "email VARCHAR(255),"+
                            "created_date DATETIME,"+ 
                            "type CHARACTER(1),"+
                            "active TINYINT"+
                            ")";

        Assert.assertEquals(expectSql, sql);
    }    
    @Test
    public void testAnnotations() {
        TableClass tableClass = new TableClass(UsersAnnotation.class);
        
        String sql = tableClass.createTableSQL();

        String expectSql = "CREATE TABLE IF NOT EXISTS users_table ("+ 
                            "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
                            "first_name VARCHAR(255),"+
                            "last_name VARCHAR(255),"+
                            "password VARCHAR(255),"+
                            "username VARCHAR(30),"+
                            "email VARCHAR(255),"+
                            "created_date DATE,"+ 
                            "type CHARACTER(1),"+
                            "active TINYINT"+
                            ")";

        Assert.assertEquals(expectSql, sql);
    }    

    @Test
    public void testInsertSql() {
        TableClass tableClass = new TableClass(UsersAnnotation.class);
        

        UsersAnnotation user = new UsersAnnotation();

        user.setFirstName("Ted");
        user.setLastName("Dykes");
        user.setPassword("badpass");
        user.setUsername("tdykes");
        user.setEmail("ted@gmail.com");
        user.setCreatedDate(new Date());
        user.setType('R');
        user.setActive(true);


        String sql = tableClass.insertSql(user);

        
    }
}
