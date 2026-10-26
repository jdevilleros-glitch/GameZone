package com.misco.gamezone.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a warranty associated with a product sold in a sale.
 *
 * GRASP:
 * - Information Expert: manages its own warranty information and validity.
 * - High Cohesion: contains only responsibilities related to the warranty.
 * - Polymorphism: delegates duration, type, and additional cost to subclasses.
 */
public abstract class Warranty {

    private final String warrantyId;
    private final Product product;
    private final Sale sale;
    private final LocalDate startDate;
    private final LocalDate endDate;

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

        if (warrantyId == null || warrantyId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El identificador de la garantía es obligatorio.");
        }

        this.warrantyId = warrantyId;
        this.product = Objects.requireNonNull(
                product, "El producto asociado es obligatorio.");
        this.sale = Objects.requireNonNull(
                sale, "La venta asociada es obligatoria.");
        this.startDate = Objects.requireNonNull(
                startDate, "La fecha de inicio es obligatoria.");

        int duration = getDurationInMonths();

        if (duration <= 0) {
            throw new IllegalArgumentException(
                    "La duración de la garantía debe ser mayor que cero.");
        }

        this.endDate = startDate.plusMonths(duration);
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