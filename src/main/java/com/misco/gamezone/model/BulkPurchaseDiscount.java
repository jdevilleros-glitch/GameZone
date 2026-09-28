/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a discount based on the minimum number of products in a sale.
 *
 * @author USUARIO
 */
public class BulkPurchaseDiscount extends Promotion {

    private int minimumQuantity;
    private double percentage;

    /**
     * Creates a bulk purchase discount promotion.
     *
     * @param id promotion ID
     * @param name promotion name
     * @param startDate promotion start date
     * @param endDate promotion end date
     * @param minimumQuantity minimum number of products required
     * @param percentage discount percentage
     */
    public BulkPurchaseDiscount(String id, String name, LocalDate startDate,
            LocalDate endDate, int minimumQuantity, double percentage) {
        super(id, name, startDate, endDate);
        this.minimumQuantity = minimumQuantity;
        this.percentage = percentage;
    }

    /**
     * Returns the minimum quantity required for the discount.
     *
     * @return minimum quantity
     */
    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    /**
     * Sets the minimum quantity required for the discount.
     *
     * @param minimumQuantity minimum quantity
     */
    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    /**
     * Returns the discount percentage.
     *
     * @return discount percentage
     */
    public double getPercentage() {
        return percentage;
    }

    /**
     * Sets the discount percentage.
     *
     * @param percentage discount percentage
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * Calculates the discount when the sale reaches the minimum quantity.
     *
     * @param sale sale to evaluate
     * @return discount amount
     */
    @Override
    public double calculateDiscount(Sale sale) {
        if (sale.getProductsSold().size() >= minimumQuantity) {
            return sale.getTotal() * (percentage / 100.0);
        }

        return 0;
    }
}
