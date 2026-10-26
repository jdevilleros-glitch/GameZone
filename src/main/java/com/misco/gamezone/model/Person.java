package com.misco.gamezone.model;

/**
 * Represents the common information and behavior of every person
 * in the GameZone system.
 *
 * GRASP:
 * - Information Expert: manages its own personal information.
 * - High Cohesion: focuses exclusively on shared person attributes
 *   and behavior.
 * - Protected Variations: defines a common abstraction for different
 *   types of people.
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
     * @param name the name of the person
     * @param id the identification code of the person
     * @param cellphone the phone number of the person
     */
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

    /**
     * Returns the role assigned to this type of person.
     *
     * @return the person's role
     */
    public abstract String getRole();
}