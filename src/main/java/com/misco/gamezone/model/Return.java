package com.misco.gamezone.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a product return associated with an existing sale.
 *
 * GRASP:
 * - Information Expert: calculates the refund using information about
 *   the original sale and returned products.
 * - High Cohesion: manages the information and calculations of a return.
 *
 * @author USUARIO
 */
public class Return {

    private String returnId;
    private LocalDate returnDate;
    private Sale originalSale;
    private List<Product> returnedProducts;
    private String reason;
    private double refundAmount;
    private double warrantyRefundAmount;

    /**
     * Creates a new return associated with an existing sale.
     *
     * @param returnId unique identifier of the return
     * @param returnDate date when the return is registered
     * @param originalSale original sale associated with the return
     * @param returnedProducts products included in the return
     * @param reason reason provided for the return
     */
    public Return(String returnId, LocalDate returnDate, Sale originalSale,
            List<Product> returnedProducts, String reason) {
        this.returnId = returnId;
        this.returnDate = returnDate;
        this.originalSale = originalSale;
        this.returnedProducts = returnedProducts == null
                ? new ArrayList<>()
                : new ArrayList<>(returnedProducts);
        this.reason = reason;
        this.warrantyRefundAmount = 0;
        this.refundAmount = calculateRefundAmount();
    }

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Sale getOriginalSale() {
        return originalSale;
    }

    public List<Product> getReturnedProducts() {
        return Collections.unmodifiableList(returnedProducts);
    }

    public void setReturnedProducts(List<Product> returnedProducts) {
        this.returnedProducts = returnedProducts == null
                ? new ArrayList<>()
                : new ArrayList<>(returnedProducts);
        this.refundAmount = calculateRefundAmount();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    /**
     * Calculates the refund using the proportional discount from the sale.
     *
     * @return the calculated refund amount
     */
    public double calculateRefundAmount() {
        double total = warrantyRefundAmount;

        if (originalSale == null || returnedProducts == null) {
            refundAmount = total;
            return refundAmount;
        }

        double subtotal = originalSale.getSubtotal();
        double discount = originalSale.getDiscountAmount();

        double discountRate = subtotal > 0
                ? discount / subtotal
                : 0;

        for (Product product : returnedProducts) {
            if (product != null) {
                double itemPrice = product.getPrice();
                total += itemPrice - (itemPrice * discountRate);
            }
        }

        refundAmount = total;
        return refundAmount;
    }

    /**
     * Generates a receipt describing the return and its refund.
     *
     * @return formatted return receipt
     */
    public String generateReturnReceipt() {
        StringBuilder receipt = new StringBuilder();

        receipt.append("===== RETURN RECEIPT =====")
                .append("\nReturn ID: ").append(returnId)
                .append("\nDate: ").append(returnDate)
                .append("\nOriginal Sale: ")
                .append(originalSale == null ? "N/A" : originalSale.getSaleId())
                .append("\nReturned Items:");

        double subtotal = originalSale == null ? 0 : originalSale.getSubtotal();
        double discount = originalSale == null
                ? 0
                : originalSale.getDiscountAmount();

        double discountRate = subtotal > 0
                ? discount / subtotal
                : 0;

        for (Product product : returnedProducts) {
            if (product == null) {
                continue;
            }

            double originalPrice = product.getPrice();
            double proportionalDiscount = originalPrice * discountRate;
            double refundedAmount = originalPrice - proportionalDiscount;

            receipt.append("\n- ")
                    .append(product.getName())
                    .append("\n  Original Price: $")
                    .append(originalPrice)
                    .append("\n  Proportional Discount: $")
                    .append(proportionalDiscount)
                    .append("\n  Refunded Amount: $")
                    .append(refundedAmount);
        }

        receipt.append("\nExtended Warranty Refund: $")
                .append(warrantyRefundAmount)
                .append("\nReason: ").append(reason)
                .append("\nTotal Refund: $").append(calculateRefundAmount());

        return receipt.toString();
    }

    public double getWarrantyRefundAmount() {
        return warrantyRefundAmount;
    }

    public void setWarrantyRefundAmount(double warrantyRefundAmount) {
        this.warrantyRefundAmount = warrantyRefundAmount;
        this.refundAmount = calculateRefundAmount();
    }
}