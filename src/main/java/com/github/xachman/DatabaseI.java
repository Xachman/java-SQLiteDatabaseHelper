/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.xachman;

/**
 *
 * @author xach
 */
public interface DatabaseI {

    public void createTable(Table table);
    public void createTable(Class c);
    
}
