/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

/**
 * Represents a seller in the system. A seller is a person identified by an
 * employee code.
 *
 * @author USUARIO
 */
public class Seller extends Person {

    private String employeeCode;

    /**
     * Creates a seller with the specified information.
     *
     * @param name the name of the seller
     * @param id the identification code of the seller
     * @param cellphone the phone number of the seller
     * @param employeeCode the employee code of the seller
     */
    public Seller(String name, String id, String cellphone, String employeeCode) {
        super(name, id, cellphone);
        this.employeeCode = employeeCode;
    }

    /**
     * Returns the employee code of the seller.
     *
     * @return the employee code of the seller
     */
    public String getEmployeeCode() {
        return employeeCode;
    }

    /**
     * Sets a new employee code for the seller.
     *
     * @param employeeCode the new employee code of the seller
     */
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    /**
     * Returns the role of the seller.
     *
     * @return the seller role
     */
    @Override
    public String getRole() {
        return "SELLER";
    }

}
