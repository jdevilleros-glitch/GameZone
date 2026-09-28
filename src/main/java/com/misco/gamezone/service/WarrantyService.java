/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.service;

import com.misco.gamezone.dao.WarrantyDAO;
import com.misco.gamezone.model.BasicWarranty;
import com.misco.gamezone.model.ExtendedWarranty;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Sale;
import com.misco.gamezone.model.Warranty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides business logic for warranty management.
 *
 * @author USUARIO
 */
public class WarrantyService {

    private final WarrantyDAO warrantyDAO;
    private final List<Warranty> warranties;

    /**
     * Creates a warranty service and loads existing warranties.
     *
     * @param warrantyDAO warranty data access object
     */
    public WarrantyService(WarrantyDAO warrantyDAO) {
        this.warrantyDAO = warrantyDAO;
        this.warranties = warrantyDAO.loadAll();
    }

    /**
     * Assigns a basic warranty to a product.
     *
     * @param product associated product
     * @param sale associated sale
     * @param startDate warranty start date
     * @return created basic warranty
     */
    public BasicWarranty assignBasicWarranty(
            Product product,
            Sale sale,
            LocalDate startDate) {

        BasicWarranty warranty = new BasicWarranty(
                generateWarrantyId(),
                product,
                sale,
                startDate
        );

        warranties.add(warranty);
        warrantyDAO.saveAll(warranties);

        return warranty;
    }

    /**
     * Assigns an extended warranty to a product.
     *
     * @param product associated product
     * @param sale associated sale
     * @param startDate warranty start date
     * @return created extended warranty
     */
    public ExtendedWarranty assignExtendedWarranty(
            Product product,
            Sale sale,
            LocalDate startDate) {

        ExtendedWarranty warranty = new ExtendedWarranty(
                generateWarrantyId(),
                product,
                sale,
                startDate
        );

        warranties.add(warranty);
        warrantyDAO.saveAll(warranties);

        return warranty;
    }

    /**
     * Finds the warranty associated with a product in a specific sale.
     *
     * @param productId product identifier
     * @param saleId sale identifier
     * @return matching warranty or null if not found
     */
    public Warranty findWarrantyByProduct(
            String productId,
            String saleId) {

        for (Warranty warranty : warranties) {

            if (warranty.getProduct().getId()
                    .equalsIgnoreCase(productId)
                    && warranty.getSale().getSaleId()
                    .equalsIgnoreCase(saleId)) {

                return warranty;
            }
        }

        return null;
    }

    /**
     * Returns all registered warranties.
     *
     * @return list of warranties
     */
    public List<Warranty> listAllWarranties() {
        return new ArrayList<>(warranties);
    }

    /**
     * Returns all warranties active on the current date.
     *
     * @return active warranties
     */
    public List<Warranty> listActiveWarranties() {

        LocalDate today = LocalDate.now();
        List<Warranty> activeWarranties = new ArrayList<>();

        for (Warranty warranty : warranties) {

            if (warranty.isActive(today)) {
                activeWarranties.add(warranty);
            }
        }

        return activeWarranties;
    }

    /**
     * Returns warranties that expire within the specified number of days.
     *
     * @param daysAhead number of days to check
     * @return warranties expiring soon
     */
    public List<Warranty> listWarrantiesExpiringSoon(int daysAhead) {

        if (daysAhead < 0) {
            throw new IllegalArgumentException(
                    "Days ahead cannot be negative.");
        }

        LocalDate today = LocalDate.now();
        LocalDate limitDate = today.plusDays(daysAhead);

        List<Warranty> expiringWarranties = new ArrayList<>();

        for (Warranty warranty : warranties) {

            LocalDate endDate = warranty.getEndDate();

            if (!endDate.isBefore(today)
                    && !endDate.isAfter(limitDate)) {

                expiringWarranties.add(warranty);
            }
        }

        return expiringWarranties;
    }

    /**
     * Generates the next warranty identifier.
     *
     * @return generated warranty identifier
     */
    private String generateWarrantyId() {

        int highestNumber = 0;

        for (Warranty warranty : warranties) {

            String id = warranty.getWarrantyId();

            if (id != null && id.matches("W\\d+")) {

                int number = Integer.parseInt(id.substring(1));

                if (number > highestNumber) {
                    highestNumber = number;
                }
            }
        }

        return String.format("W%03d", highestNumber + 1);
    }
}
