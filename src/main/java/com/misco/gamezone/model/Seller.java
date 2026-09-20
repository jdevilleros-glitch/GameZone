/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

/**
 *
 * @author USUARIO
 */
public class Seller extends Person {
    
    private String employeeCode;

    public Seller(String name, String id, String cellphone, String employeeCode) {
        super(name, id, cellphone);
        this.employeeCode = employeeCode;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }
    
    @Override
    public String getRole(){
        return "SELLER";
    }
    
    
}
