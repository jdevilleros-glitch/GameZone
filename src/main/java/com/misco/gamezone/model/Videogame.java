/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

/**
 *
 * @author USUARIO
 */
public class Videogame extends Product {

    private String genre;
    private String platform;

    public Videogame(String id, String name, double price, int stock, String genre, String platform) {
        super(id, name, price, stock);
        this.genre = genre;
        this.platform = platform;
    }

    public String getGenre() {
        return genre;
    }

    public String getPlatform() {
        return platform;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String getProductType() {
        return "Videogame";
    }

}
