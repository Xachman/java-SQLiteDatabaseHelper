/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.xachman.annotations;

/**
 *
 * @author xach
 */
public @interface Column {

    public boolean primarykey() default false;

    public boolean autoincrement() default false;

    public int length() default 0;

    public String type() default "VARCHAR";
    
}
