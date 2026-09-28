/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

import java.util.Date;
import java.util.List;

/**
 * Represents a sale made in GameZone. A sale contains the sale identifier,
 * date, products sold, customer, seller, and the calculated total amount.
 *
 * @author USUARIO
 */
public class Sale {

    private String saleId;
    private Date date;
    private double total;
    private List<Product> productsSold;
    private Customer customer;
    private Seller seller;
    private String appliedPromotionName;
    private double discountAmount;

    /**
     * Creates a new sale and calculates its total amount based on the prices of
     * the products sold.
     *
     * @param saleId unique identifier of the sale
     * @param date date when the sale was made
     * @param productsSold list of products included in the sale
     * @param customer customer who made the purchase
     * @param seller seller who handled the sale
     */
    public Sale(String saleId, Date date, List<Product> productsSold, Customer customer, Seller seller) {
        this.saleId = saleId;
        this.date = date;
        this.productsSold = productsSold;
        this.customer = customer;
        this.seller = seller;
        this.total = calculateTotal();
    }

    /**
     * Returns the sale identifier.
     *
     * @return the sale identifier
     */
    public String getSaleId() {
        return saleId;
    }

    /**
     * Returns the date of the sale.
     *
     * @return the sale date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the total amount of the sale.
     *
     * @return the total sale amount
     */
    public double getTotal() {
        return total;
    }

    /**
     * Returns the products included in the sale.
     *
     * @return the list of products sold
     */
    public List<Product> getProductsSold() {
        return productsSold;
    }

    /**
     * Returns the customer associated with the sale.
     *
     * @return the customer who made the purchase
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns the seller associated with the sale.
     *
     * @return the seller who handled the sale
     */
    public Seller getSeller() {
        return seller;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Calculates the total amount of the sale by adding the prices of all
     * products sold.
     *
     * @return the calculated total amount
     */
    public double calculateTotal() {
        total = 0;
        for (Product product : productsSold) {
            double prodPrice = product.getPrice();
            total += prodPrice;
        }

        return total;
    }

    public String getAppliedPromotionName() {
        return appliedPromotionName;
    }

    public void setAppliedPromotionName(String appliedPromotionName) {
        this.appliedPromotionName = appliedPromotionName;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String generateReceipt() {
        double subtotal = total + discountAmount;

        String promotion = appliedPromotionName != null
                ? appliedPromotionName
                : "No promotion";

        return "Sale ID: " + saleId
                + "\nSubtotal: $" + subtotal
                + "\nPromotion: " + promotion
                + "\nDiscount: $" + discountAmount
                + "\nTotal: $" + total;
    }
}
