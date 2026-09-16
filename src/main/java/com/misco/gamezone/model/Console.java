/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

/**
 *
 * @author USUARIO
 */
public class Console extends Product {

    private String brand;
    private String model;

    public Console(String id, String name, double price, int stock, String brand, String model) {
        super(id, name, price, stock);
        this.brand = brand;
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getProductType() {
        return "Console";
    }

}
