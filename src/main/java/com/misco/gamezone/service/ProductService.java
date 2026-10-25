/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.service;

import com.misco.gamezone.dao.ProductDAO;
import com.misco.gamezone.exceptions.BusinessRuleException;
import com.misco.gamezone.exceptions.InvalidDataException;
import com.misco.gamezone.model.Console;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Videogame;
import com.misco.gamezone.validation.ProductValidator;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides business operations for managing products in GameZone.
 *
 * Handles videogame and console registration, product retrieval,
 * stock updates, and stock restoration.
 *
 * Uses ProductValidator for product validation and ProductDAO
 * for data persistence.
 *
 * @author USUARIO
 */
public class ProductService {

    private final ProductDAO productDAO;
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
     * Validates product information and prevents duplicate identifiers.
     *
     * @param id unique identifier of the videogame
     * @param name name of the videogame
     * @param price price of the videogame
     * @param stock initial available quantity
     * @param genre genre of the videogame
     * @param platform platform supported by the videogame
     * @throws InvalidDataException if product information is invalid
     * @throws BusinessRuleException if the identifier already exists
     */
    public void registerVideoGame(
            String id,
            String name,
            double price,
            int stock,
            String genre,
            String platform) {

        ProductValidator.validateProductData(id, name, price, stock);
        validateUniqueProductId(id);

        Videogame videoGame = new Videogame(
                id, name, price, stock, genre, platform
        );

        products.add(videoGame);
        productDAO.saveProducts(products);
    }

    /**
     * Registers a new console and saves the updated product list.
     *
     * Validates product information and prevents duplicate identifiers.
     *
     * @param id unique identifier of the console
     * @param name name of the console
     * @param price price of the console
     * @param stock initial available quantity
     * @param brand brand of the console
     * @param model model of the console
     * @throws InvalidDataException if product information is invalid
     * @throws BusinessRuleException if the identifier already exists
     */
    public void registerConsole(
            String id,
            String name,
            double price,
            int stock,
            String brand,
            String model) {

        ProductValidator.validateProductData(id, name, price, stock);
        validateUniqueProductId(id);

        Console console = new Console(
                id, name, price, stock, brand, model
        );

        products.add(console);
        productDAO.saveProducts(products);
    }

    /**
     * Returns the registered products.
     *
     * Reloads the products from persistent storage and returns
     * a copy of the product list.
     *
     * @return a copy of the registered product list
     */
    public List<Product> listProducts() {
        products = productDAO.loadProducts();
        return new ArrayList<>(products);
    }

    /**
     * Updates the stock of an existing product.
     *
     * The new stock quantity must be zero or greater.
     *
     * @param productId unique identifier of the product
     * @param newStock new available quantity
     * @return true if the product was updated, false if not found
     * @throws InvalidDataException if the new stock is negative
     */
    public boolean updateStock(String productId, int newStock) {

        if (newStock < 0) {
            throw new InvalidDataException(
                    "stock", "Stock cannot be negative."
            );
        }

        products = productDAO.loadProducts();

        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setStock(newStock);
                productDAO.saveProducts(products);
                return true;
            }
        }

        return false;
    }

    /**
     * Restores stock for a product by increasing its current quantity.
     *
     * Validates that the restored quantity is positive and prevents
     * integer overflow when calculating the new stock.
     *
     * @param productId unique identifier of the product
     * @param quantity quantity to restore
     * @return true if stock was restored, false if not found
     * @throws InvalidDataException if the quantity is not positive
     * @throws BusinessRuleException if the resulting stock exceeds
     * the maximum allowed integer value
     */
    public boolean restoreStock(String productId, int quantity) {

        if (quantity <= 0) {
            throw new InvalidDataException(
                    "quantity", "Restored quantity must be greater than zero."
            );
        }

        products = productDAO.loadProducts();

        for (Product product : products) {
            if (product.getId().equals(productId)) {

                long restoredStock = (long) product.getStock() + quantity;

                if (restoredStock > Integer.MAX_VALUE) {
                    throw new BusinessRuleException(
                            "Restored stock exceeds the maximum allowed quantity."
                    );
                }

                product.setStock((int) restoredStock);
                productDAO.saveProducts(products);
                return true;
            }
        }

        return false;
    }

    /**
     * Finds a product by its identifier.
     *
     * Performs a case-insensitive search and preserves the original
     * behavior of returning null when the product does not exist.
     *
     * @param productId product identifier
     * @return matching product or null if not found
     */
    public Product findProductById(String productId) {

        products = productDAO.loadProducts();

        for (Product product : products) {
            if (product.getId().equalsIgnoreCase(productId)) {
                return product;
            }
        }

        return null;
    }

    /**
     * Validates that a product identifier is not already registered.
     *
     * Performs a case-insensitive comparison to prevent duplicate IDs.
     *
     * @param id identifier to validate
     * @throws BusinessRuleException if the identifier already exists
     */
    private void validateUniqueProductId(String id) {

        products = productDAO.loadProducts();

        for (Product product : products) {
            if (product.getId().equalsIgnoreCase(id)) {
                throw new BusinessRuleException(
                        "A product with ID " + id + " already exists."
                );
            }
        }
    }
}