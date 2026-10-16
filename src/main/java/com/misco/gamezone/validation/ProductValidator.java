/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.validation;

import com.misco.gamezone.exceptions.BusinessRuleException;
import com.misco.gamezone.exceptions.InvalidDataException;
import com.misco.gamezone.exceptions.ResourceNotFoundException;
import com.misco.gamezone.model.Product;

/**
 *
 * @author USUARIO
 * 
 * Validates product information and business rules.
 */
public final class ProductValidator {

    private ProductValidator() {
        // Utility class.
    }

    /**
     * Validates the data required to register a product.
     */
    public static void validateProductData(
            String id,
            String title,
            double price,
            int stock) {

        if (id == null || id.trim().isEmpty()) {
            throw new InvalidDataException(
                    "id", "Product ID cannot be empty."
            );
        }

        if (title == null || title.trim().isEmpty()) {
            throw new InvalidDataException(
                    "title", "Product title cannot be empty."
            );
        }

        if (!Double.isFinite(price) || price <= 0) {
            throw new InvalidDataException(
                    "price", "Price must be greater than zero."
            );
        }

        if (stock < 0) {
            throw new InvalidDataException(
                    "stock", "Stock cannot be negative."
            );
        }
    }

    /**
     * Validates that the requested quantity is available.
     */
    public static void validateStockAvailability(
            Product product,
            int requestedQuantity) {

        if (product == null) {
            throw new ResourceNotFoundException("Product", "unknown");
        }

        if (requestedQuantity <= 0) {
            throw new InvalidDataException(
                    "quantity", "Quantity must be greater than zero."
            );
        }

        if (product.getStock() < requestedQuantity) {
            throw new BusinessRuleException(
                    "Insufficient stock for product: " + product.getId()
            );
        }
    }

    /**
     * Validates that a product exists.
     */
    public static void validateProductExists(
            Product product,
            String productId) {

        if (product == null) {
            throw new ResourceNotFoundException("Product", productId);
        }
    }
}