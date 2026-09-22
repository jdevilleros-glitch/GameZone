/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class Sale {
    
    private String saleid;
    private Date date;
    private double total;
    private List<Product> productsSold;
    private Customer customer;
    private Seller seller;

    public Sale(String saleid, Date date, List<Product> productsSold, Customer customer, Seller seller) {
        this.saleid = saleid;
        this.date = date;
        this.productsSold = productsSold;
        this.customer = customer;
        this.seller = seller;
        this.total = calculateTotal();
    }

    public String getSaleid() {
        return saleid;
    }

    public Date getDate() {
        return date;
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
    
    public double calculateTotal(){
        total = 0;
        for (Product product : productsSold){
            double prodPrice = product.getPrice();
            total += prodPrice;
        }
        
        return total;
    }
}
