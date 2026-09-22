/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

/**
 * Represents a customer in the system. A customer is a person who can make
 * purchases and has an email address.
 *
 * @author USUARIO
 */
public class Customer extends Person {

    private String email;

    /**
     * Creates a customer with the specified information.
     *
     * @param name the name of the customer
     * @param id the identification code of the customer
     * @param cellphone the phone number of the customer
     * @param email the email address of the customer
     */
    public Customer(String name, String id, String cellphone, String email) {
        super(name, id, cellphone);
        this.email = email;
    }

    /**
     * Returns the customer's email address.
     *
     * @return the email address of the customer
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets a new email address for the customer.
     *
     * @param email the new email address of the customer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the role of the customer.
     *
     * @return the customer role
     */
    @Override
    public String getRole() {
        return "CUSTOMER";
    }

}
