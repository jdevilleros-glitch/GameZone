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
 *
 * @author USUARIO
 */
public class ProductService {
    private ProductDAO productDAO;
    private List<Product> products;

    public ProductService(ProductDAO productDAO){
        this.productDAO = productDAO;
        this.products = productDAO.loadProducts();
    }
    
    public void registerVideoGame(String id, String name, double price, int stock, String genre, String platform){
        Videogame videoGame = new Videogame(id, name, price, stock, genre, platform);
        products.add(videoGame);
        productDAO.saveProducts(products);
    }
    
    public void registerConsole(String id, String name, double price, int stock, String brand, String model){
        Console console = new Console(id, name, price, stock, brand, model);
        products.add(console);
        productDAO.saveProducts(products);
    }
    
    public List<Product> listProducts(){
        return new ArrayList<>(products);
    }
}
