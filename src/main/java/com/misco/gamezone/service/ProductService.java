/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.service;

import com.misco.gamezone.dao.ProductDAO;
import com.misco.gamezone.model.Console;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Videogame;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides business operations for managing products.
 *
 * @author USUARIO
 */
public class ProductService {

    private ProductDAO productDAO;
    private List<Product> products;

    /**
     * Creates the product service and loads the stored products.
     *
     * @param productDAO DAO used for product persistence
     */
    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
        this.products = productDAO.loadProducts();
    }

    /**
     * Registers a new videogame and saves the updated product list.
     *
     * @param id unique identifier of the videogame
     * @param name name of the videogame
     * @param price price of the videogame
     * @param stock available quantity
     * @param genre genre of the videogame
     * @param platform platform of the videogame
     */
    public void registerVideoGame(String id, String name, double price, int stock, String genre, String platform) {
        Videogame videoGame = new Videogame(id, name, price, stock, genre, platform);
        products.add(videoGame);
        productDAO.saveProducts(products);
    }

    /**
     * Registers a new console and saves the updated product list.
     *
     * @param id unique identifier of the videogame
     * @param name name of the console
     * @param price price of the console
     * @param stock available quantity
     * @param brand brand of the console
     * @param model model of the console
     */
    public void registerConsole(String id, String name, double price, int stock, String brand, String model) {
        Console console = new Console(id, name, price, stock, brand, model);
        products.add(console);
        productDAO.saveProducts(products);
    }

    /**
     * Returns the registered products.
     *
     * @return a copy of the product list
     */
    public List<Product> listProducts() {
        return new ArrayList<>(products);
    }
}
