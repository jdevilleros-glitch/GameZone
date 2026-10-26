package com.misco.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a basic six-month warranty with no additional cost.
 *
 * GRASP:
 * - Polymorphism: implements the abstract behavior defined by Warranty.
 * - High Cohesion: contains only the rules specific to the basic warranty.
 */
public class BasicWarranty extends Warranty {

    private static final int DURATION_IN_MONTHS = 6;
    private static final String WARRANTY_TYPE = "Basic Warranty";
    private static final double ADDITIONAL_COST = 0.0;

    public BasicWarranty(String warrantyId, Product product,
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
        return ADDITIONAL_COST;
    }
}