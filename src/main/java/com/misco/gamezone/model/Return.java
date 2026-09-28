/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a product return associated with an existing sale. A return
 * contains the products returned by the customer, the reason for the return,
 * and the calculated refund amount.
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
        this.returnedProducts = returnedProducts;
        this.reason = reason;
        this.warrantyRefundAmount = 0;
        this.refundAmount = calculateRefundAmount();
    }

    /**
     * Returns the return identifier.
     *
     * @return the return identifier
     */
    public String getReturnId() {
        return returnId;
    }

    /**
     * Sets the return identifier.
     *
     * @param returnId the new return identifier
     */
    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    /**
     * Returns the date of the return.
     *
     * @return the return date
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * Sets the return date.
     *
     * @param returnDate the new return date
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Returns the original sale associated with this return.
     *
     * @return the original sale
     */
    public Sale getOriginalSale() {
        return originalSale;
    }

    /**
     * Returns the products included in the return.
     *
     * @return the returned products
     */
    public List<Product> getReturnedProducts() {
        return returnedProducts;
    }

    /**
     * Sets the products included in the return.
     *
     * @param returnedProducts the products to include in the return
     */
    public void setReturnedProducts(List<Product> returnedProducts) {
        this.returnedProducts = returnedProducts;
    }

    /**
     * Returns the reason for the return.
     *
     * @return the return reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for the return.
     *
     * @param reason the new return reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Returns the refund amount.
     *
     * @return the refund amount
     */
    public double getRefundAmount() {
        return refundAmount;
    }

    /**
     * Calculates the refund amount considering the proportional discount
     * applied to the original sale.
     *
     * @return the calculated refund amount
     */
    public double calculateRefundAmount() {

        refundAmount = warrantyRefundAmount;

        double subtotal = originalSale.getSubtotal();
        double discount = originalSale.getDiscountAmount();

        double discountRate = subtotal > 0
                ? discount / subtotal
                : 0;

        for (Product product : returnedProducts) {

            double proportionalDiscount
                    = product.getPrice() * discountRate;

            double itemRefund
                    = product.getPrice() - proportionalDiscount;

            refundAmount += itemRefund;
        }

        return refundAmount;
    }

    /**
     * Generates a formatted receipt containing the return details, including
     * the proportional discount applied to each returned item.
     *
     * @return the formatted return receipt
     */
    public String generateReturnReceipt() {

        StringBuilder receipt = new StringBuilder();

        receipt.append("===== RETURN RECEIPT =====")
                .append("\nReturn ID: ").append(returnId)
                .append("\nDate: ").append(returnDate)
                .append("\nOriginal Sale: ")
                .append(originalSale.getSaleId())
                .append("\nReturned Items:");

        double subtotal = originalSale.getSubtotal();
        double discount = originalSale.getDiscountAmount();

        double discountRate = subtotal > 0
                ? discount / subtotal
                : 0;

        for (Product product : returnedProducts) {

            double originalPrice = product.getPrice();

            double proportionalDiscount
                    = originalPrice * discountRate;

            double refundedAmount
                    = originalPrice - proportionalDiscount;

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
                .append(warrantyRefundAmount);

        receipt.append("\nReason: ")
                .append(reason)
                .append("\nTotal Refund: $")
                .append(refundAmount);

        return receipt.toString();
    }

    /**
     * Returns the refundable amount from cancelled extended warranties.
     *
     * @return warranty refund amount
     */
    public double getWarrantyRefundAmount() {
        return warrantyRefundAmount;
    }

    /**
     * Sets the refundable amount from cancelled extended warranties and
     * recalculates the total refund.
     *
     * @param warrantyRefundAmount refundable warranty amount
     */
    public void setWarrantyRefundAmount(double warrantyRefundAmount) {
        this.warrantyRefundAmount = warrantyRefundAmount;
        this.refundAmount = calculateRefundAmount();
    }
}
