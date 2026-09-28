/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a warranty associated with a product sold in a sale.
 *
 * @author USUARIO
 */
public abstract class Warranty {

    private String warrantyId;
    private Product product;
    private Sale sale;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a new warranty.
     *
     * @param warrantyId warranty identifier
     * @param product associated product
     * @param sale associated sale
     * @param startDate warranty start date
     */
    public Warranty(String warrantyId, Product product, Sale sale,
            LocalDate startDate) {
        this.warrantyId = warrantyId;
        this.product = product;
        this.sale = sale;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(getDurationInMonths());
    }

    public String getWarrantyId() {
        return warrantyId;
    }

    public Product getProduct() {
        return product;
    }

    public Sale getSale() {
        return sale;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Returns the warranty duration in months.
     *
     * @return duration in months
     */
    public abstract int getDurationInMonths();

    /**
     * Returns the warranty type.
     *
     * @return warranty type
     */
    public abstract String getWarrantyType();

    /**
     * Returns the additional warranty cost.
     *
     * @return additional cost
     */
    public abstract double getAdditionalCost();

    /**
     * Determines whether the warranty is active on the specified date.
     *
     * @param date date to evaluate
     * @return true if the warranty is active
     */
    public boolean isActive(LocalDate date) {
        return date != null
                && !date.isBefore(startDate)
                && !date.isAfter(endDate);
    }

    /**
     * Generates a certificate containing the warranty information.
     *
     * @return formatted warranty certificate
     */
    public String generateWarrantyCertificate() {
        return "===== WARRANTY CERTIFICATE =====\n"
                + "Warranty ID: " + warrantyId + "\n"
                + "Type: " + getWarrantyType() + "\n"
                + "Product: " + product.getName() + "\n"
                + "Sale: " + sale.getSaleId() + "\n"
                + "Start Date: " + startDate + "\n"
                + "Expiration Date: " + endDate + "\n"
                + "Additional Cost: $" + getAdditionalCost();
    }
}
