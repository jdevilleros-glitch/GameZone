/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

/**
 * Represents a person or user in the system. Defines the shared attributes of
 * every person in the system.
 *
 * @author USUARIO
 */
public abstract class Person {

    private String name;
    private String id;
    private String cellphone;

    /**
     * Creates a person with the specified information.
     *
     * @param name
     * @param id
     * @param cellphone
     */
    public Person(String name, String id, String cellphone) {
        this.name = name;
        this.id = id;
        this.cellphone = cellphone;
    }

    /**
     * Returns the person´s name
     *
     * @return the name of the person
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the person´s identification code
     *
     * @return the id of the person
     */

    public String getId() {
        return id;
    }

    /**
     * Returns the person´s phone number
     *
     * @return the phone number of the person
     */
    public String getCellphone() {
        return cellphone;
    }

    /**
     * Sets a new name for the person
     *
     * @param name the new name of the person
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets a new id for the person
     *
     * @param id the new id of the person
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets a new phone number for the person
     *
     * @param cellphone the new phone number of the person
     */
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    /**
     * Returns the name of the role that the person has
     *
     * @return the role of the person
     */
    public abstract String getRole();
}
