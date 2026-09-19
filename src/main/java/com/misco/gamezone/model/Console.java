/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

/**
 * Represets a console product. A console includes a brand and a model.
 *
 * @author USUARIO
 */
public class Console extends Product {

    private String brand;
    private String model;

    /**
     * Creates a console with the specific information.
     *
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param brand
     * @param model
     */
    public Console(String id, String name, double price, int stock, String brand, String model) {
        super(id, name, price, stock);
        this.brand = brand;
        this.model = model;
    }

    /**
     * Returns the console brand.
     *
     * @return the console brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Returns the console model.
     *
     * @return the console model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the console new brand.
     *
     * @param brand the new brand for the console
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Sets the console model.
     *
     * @param model the new console model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Returns the product type.
     *
     * @return the product type as CONSOLE
     */
    @Override
    public String getProductType() {
        return "CONSOLE";
    }

}
