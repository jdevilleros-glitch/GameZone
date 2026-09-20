/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

/**
 *
 * @author USUARIO
 */
public class Customer extends Person {
    
    private String email;

    public Customer(String name, String id, String cellphone, String email) {
        super(name, id, cellphone);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String getRole(){
        return "CUSTOMER";
    }
    
}
