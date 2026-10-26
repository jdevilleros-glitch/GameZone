package com.misco.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a twelve-month extended warranty.
 *
 * GRASP:
 * - Polymorphism: implements the behavior defined by Warranty.
 * - Information Expert: calculates the additional cost using the product price.
 * - High Cohesion: contains only rules specific to the extended warranty.
 */
public class ExtendedWarranty extends Warranty {

    private static final int DURATION_IN_MONTHS = 12;
    private static final String WARRANTY_TYPE = "Extended warranty";
    private static final double ADDITIONAL_COST_RATE = 0.10;

    public ExtendedWarranty(String warrantyId, Product product,
            Sale sale, LocalDate startDate) {
        super(warrantyId, product, sale, startDate);
    }

    @Override
    public int getDurationInMonths() {
        return DURATION_IN_MONTHS;
    }

    @Override
    public String getWarrantyType() {
        return WARRANTY_TYPE;
    }

    @Override
    public double getAdditionalCost() {
        return getProduct().getPrice() * ADDITIONAL_COST_RATE;
    }
}