/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a generic promotion available in the store.
 *
 * @author USUARIO
 */
public abstract class Promotion {

    private String id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a promotion with the specified information.
     *
     * @param id unique identifier of the promotion
     * @param name name of the promotion
     * @param startDate start date of the promotion
     * @param endDate end date of the promotion
     */
    public Promotion(String id, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the promotion ID.
     *
     * @return promotion ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the promotion ID.
     *
     * @param id promotion ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the promotion name.
     *
     * @return promotion name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the promotion name.
     *
     * @param name promotion name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the promotion start date.
     *
     * @return start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the promotion start date.
     *
     * @param startDate start date
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the promotion end date.
     *
     * @return end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the promotion end date.
     *
     * @param endDate end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Determines whether the promotion is active on the specified date.
     *
     * @param date date to validate
     * @return true if the promotion is active
     */
    public boolean isActive(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Calculates the discount for a sale.
     *
     * @param sale sale to evaluate
     * @return discount amount
     */
    public abstract double calculateDiscount(Sale sale);
}
