/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.validation;

import com.misco.gamezone.exceptions.BusinessRuleException;
import com.misco.gamezone.exceptions.ResourceNotFoundException;
import com.misco.gamezone.model.Customer;
import com.misco.gamezone.model.Seller;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Sale;
import java.util.List;


/**
 *
 * @author USUARIO
 * 
 * Validates sale information and business rules.
 */
public final class SaleValidator {

    private SaleValidator() {
        // Utility class.
    }

    /**
     * Validates the information required to register a sale.
     */
    public static void validateSaleData(
            Customer customer,
            Seller seller,
            List<Product> products) {

        if (customer == null) {
            throw new BusinessRuleException(
                    "A valid customer is required to register a sale."
            );
        }

        if (seller == null) {
            throw new BusinessRuleException(
                    "A valid seller is required to register a sale."
            );
        }

        if (products == null || products.isEmpty()) {
            throw new BusinessRuleException(
                    "A sale must contain at least one product."
            );
        }

        for (Product product : products) {
            if (product == null) {
                throw new BusinessRuleException(
                        "A sale cannot contain null products."
                );
            }
        }
    }

    /**
     * Validates that a sale exists.
     */
    public static void validateSaleExists(
            Sale sale,
            String saleId) {

        if (sale == null) {
            throw new ResourceNotFoundException("Sale", saleId);
        }
    }
}
