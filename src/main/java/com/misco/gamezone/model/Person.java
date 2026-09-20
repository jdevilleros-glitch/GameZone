/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

/**
 *
 * @author USUARIO
 */
public abstract class Person {
        private String name;
        private String id;
        private String cellphone;

    public Person(String name, String id, String cellphone) {
        this.name = name;
        this.id = id;
        this.cellphone = cellphone;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
        
    public abstract String getRole();
}
