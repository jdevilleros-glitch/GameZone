/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * Represents a sale made in GameZone.
 *
 * @author USUARIO
 */
public class Sale {

    private String saleId;
    private Date date;
    private double subtotal;
    private double total;
    private List<Product> productsSold;
    private Customer customer;
    private Seller seller;
    private String appliedPromotionName;
    private double discountAmount;
    private double extendedWarrantyCost;

    /**
     * Creates a new sale.
     *
     * @param saleId unique identifier of the sale
     * @param date date when the sale was made
     * @param productsSold items included in the sale
     * @param customer customer who made the purchase
     * @param seller seller who handled the sale
     */
    public Sale(
            String saleId,
            Date date,
            List<Product> productsSold,
            Customer customer,
            Seller seller) {

        this.saleId = saleId;
        this.date = date;
        this.productsSold = productsSold;
        this.customer = customer;
        this.seller = seller;
        this.subtotal = calculateSubtotal();
        this.discountAmount = 0;
        this.extendedWarrantyCost = 0;
        this.total = subtotal;
    }

    public String getSaleId() {
        return saleId;
    }

    public Date getDate() {
        return date;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTotal() {
        return total;
    }

    public List<Product> getProductsSold() {
        return productsSold;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Seller getSeller() {
        return seller;
    }

    public String getAppliedPromotionName() {
        return appliedPromotionName;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getExtendedWarrantyCost() {
        return extendedWarrantyCost;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setAppliedPromotionName(String appliedPromotionName) {
        this.appliedPromotionName = appliedPromotionName;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setExtendedWarrantyCost(double extendedWarrantyCost) {
        this.extendedWarrantyCost = extendedWarrantyCost;
    }

    /**
     * Calculates the subtotal of all items in the sale.
     *
     * @return item subtotal
     */
    public double calculateSubtotal() {

        double calculatedSubtotal = 0;

        for (Product product : productsSold) {
            calculatedSubtotal += product.getPrice();
        }

        return calculatedSubtotal;
    }

    /**
     * Maintains compatibility with existing promotion logic.
     *
     * @return item subtotal
     */
    public double calculateTotal() {
        return calculateSubtotal();
    }

    /**
     * Calculates the final sale total.
     */
    public void calculateFinalTotal() {
        total = subtotal - discountAmount + extendedWarrantyCost;
    }

    /**
     * Generates the sale receipt.
     *
     * @return formatted receipt
     */
    public String generateReceipt() {

        String promotion = appliedPromotionName != null
                ? appliedPromotionName
                : "No promotion";

        return "===== SALE RECEIPT ====="
                + "\nSale ID: " + saleId
                + "\nSubtotal: $" + subtotal
                + "\nPromotion: " + promotion
                + "\nDiscount: $" + discountAmount
                + "\nExtended Warranty Cost: $"
                + extendedWarrantyCost
                + "\nFinal Total: $" + total;
    }

    /**
     * Determines whether the sale is within the 30-day return period.
     *
     * @return true when the sale can be returned
     */
    public boolean canBeReturned() {

        LocalDate saleDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate currentDate = LocalDate.now();

        long daysSinceSale
                = ChronoUnit.DAYS.between(saleDate, currentDate);

        return daysSinceSale >= 0 && daysSinceSale <= 30;
    }
}
