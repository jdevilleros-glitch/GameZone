/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

/**
 * Represents a product available in the system. Defines the common attributes
 * and methods shared by all the product types.
 *
 * @author USUARIO
 */
public abstract class Product {

    private String id;
    private String name;
    private double price;
    private int stock;

    /**
     * Creates a product with specific information.
     *
     * @param id
     * @param name
     * @param price
     * @param stock
     */
    public Product(String id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Returns the product indentifier.
     *
     * @return the product identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the product name.
     *
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the product price.
     *
     * @return the product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the product stock.
     *
     * @return the available product stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the product identifier.
     *
     * @param id the new product id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the product name.
     *
     * @param name the new product name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the product price.
     *
     * @param price the new product price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the product stock.
     *
     * @param stock the new available product stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Returns the type of the product.
     *
     * @return the product specific type
     */
    public abstract String getProductType();
}
