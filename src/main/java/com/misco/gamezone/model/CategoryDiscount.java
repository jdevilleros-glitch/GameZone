/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a discount applied only to products of a specific category.
 *
 * @author USUARIO
 */
public class CategoryDiscount extends Promotion {

    private double percentage;
    private String targetCategory;

    /**
     * Creates a category discount promotion.
     *
     * @param id promotion ID
     * @param name promotion name
     * @param startDate promotion start date
     * @param endDate promotion end date
     * @param percentage discount percentage
     * @param targetCategory category to which the discount applies
     */
    public CategoryDiscount(String id, String name, LocalDate startDate,
            LocalDate endDate, double percentage, String targetCategory) {
        super(id, name, startDate, endDate);
        this.percentage = percentage;
        this.targetCategory = targetCategory;
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
     * Returns the target product category.
     *
     * @return target category
     */
    public String getTargetCategory() {
        return targetCategory;
    }

    /**
     * Sets the target product category.
     *
     * @param targetCategory target category
     */
    public void setTargetCategory(String targetCategory) {
        this.targetCategory = targetCategory;
    }

    /**
     * Calculates the discount for products that match the target category.
     *
     * @param sale sale to evaluate
     * @return discount amount
     */
    @Override
    public double calculateDiscount(Sale sale) {
        double categoryTotal = 0;

        for (Product product : sale.getProductsSold()) {
            if (product.getProductType().equalsIgnoreCase(targetCategory)) {
                categoryTotal += product.getPrice();
            }
        }

        return categoryTotal * (percentage / 100.0);
    }
}
