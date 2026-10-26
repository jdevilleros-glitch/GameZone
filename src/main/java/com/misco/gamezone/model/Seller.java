package com.misco.gamezone.model;

/**
 * Represents a seller in the GameZone system.
 *
 * GRASP:
 * - Information Expert: manages its own employee information.
 * - High Cohesion: focuses on seller-specific information.
 * - Polymorphism: implements the role defined by Person.
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
     * Updates the employee code of the seller.
     *
     * @param employeeCode the new employee code of the seller
     */
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    /**
     * Returns the role assigned to this person.
     *
     * @return the seller role
     */
    @Override
    public String getRole() {
        return "SELLER";
    }
}