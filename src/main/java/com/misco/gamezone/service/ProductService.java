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
     * @param id unique identifier
     * @param name videogame name
     * @param price videogame price
     * @param stock initial stock
     * @param genre videogame genre
     * @param platform supported platform
     */
    public void registerVideoGame(
            String id,
            String name,
            double price,
            int stock,
            String genre,
            String platform) {

        ProductValidator.validateProductData(
                id, name, price, stock
        );

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
     * @param id unique identifier
     * @param name console name
     * @param price console price
     * @param stock initial stock
     * @param brand console brand
     * @param model console model
     */
    public void registerConsole(
            String id,
            String name,
            double price,
            int stock,
            String brand,
            String model) {

        ProductValidator.validateProductData(
                id, name, price, stock
        );

        validateUniqueProductId(id);

        Console console = new Console(
                id, name, price, stock, brand, model
        );

        products.add(console);
        productDAO.saveProducts(products);
    }

    /**
     * Reloads and returns the registered products.
     *
     * @return copy of the registered products
     */
    public List<Product> listProducts() {

        products = productDAO.loadProducts();

        return new ArrayList<>(products);
    }

    /**
     * Updates the stock of an existing product.
     *
     * @param productId product identifier
     * @param newStock new stock quantity
     * @return true if updated, false if not found
     */
    public boolean updateStock(
            String productId,
            int newStock) {

        if (newStock < 0) {
            throw new InvalidDataException(
                    "stock",
                    "Stock cannot be negative."
            );
        }

        products = productDAO.loadProducts();

        Product product = findProductInCurrentList(productId);

        if (product == null) {
            return false;
        }

        product.setStock(newStock);

        saveCurrentProducts();

        return true;
    }

    /**
     * Restores stock for an existing product.
     *
     * @param productId product identifier
     * @param quantity quantity to restore
     * @return true if restored, false if not found
     */
    public boolean restoreStock(
            String productId,
            int quantity) {

        if (quantity <= 0) {
            throw new InvalidDataException(
                    "quantity",
                    "Restored quantity must be greater than zero."
            );
        }

        products = productDAO.loadProducts();

        Product product = findProductInCurrentList(productId);

        if (product == null) {
            return false;
        }

        long restoredStock =
                (long) product.getStock() + quantity;

        if (restoredStock > Integer.MAX_VALUE) {
            throw new BusinessRuleException(
                    "Restored stock exceeds the maximum allowed quantity."
            );
        }

        product.setStock((int) restoredStock);

        saveCurrentProducts();

        return true;
    }

    /**
     * Finds a product by its identifier.
     *
     * Uses a case-insensitive comparison.
     *
     * @param productId product identifier
     * @return matching product or null
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
     * Finds a product in the currently loaded list.
     *
     * Preserves the case-sensitive comparison used by
     * stock update and restoration operations.
     *
     * @param productId product identifier
     * @return matching product or null
     */
    private Product findProductInCurrentList(String productId) {

        for (Product product : products) {

            if (product.getId().equals(productId)) {
                return product;
            }
        }

        return null;
    }

    /**
     * Persists the currently loaded product list.
     */
    private void saveCurrentProducts() {
        productDAO.saveProducts(products);
    }

    /**
     * Validates that a product identifier is unique.
     *
     * @param id identifier to validate
     * @throws BusinessRuleException if the ID already exists
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