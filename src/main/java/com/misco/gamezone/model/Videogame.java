/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

/**
 * Represents a videogame product in the system. A videogame includes a genre
 * and a platform.
 *
 * @author USUARIO
 */
public class Videogame extends Product {

    private String genre;
    private String platform;

    /**
     * Creates a videogame with the specific information.
     *
     * @param id unique identifier of the videogame
     * @param name name of the videogame
     * @param price price of the videogame
     * @param stock available quantity of the videogame
     * @param genre genre of the videogame
     * @param platform platform of the videogame
     */
    public Videogame(String id, String name, double price, int stock, String genre, String platform) {
        super(id, name, price, stock);
        this.genre = genre;
        this.platform = platform;
    }

    /**
     * Returns the videogame genre.
     *
     * @return the videogame genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Returns the videogame platform.
     *
     * @return the videogame platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Sets the videogame genre.
     *
     * @param genre the new videogame genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Sets the videogame platform.
     *
     * @param platform the new videogame platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * Returns the product type.
     *
     * @return the product type as VIDEOGAME
     */
    @Override
    public String getProductType() {
        return "VIDEOGAME";
    }

}
