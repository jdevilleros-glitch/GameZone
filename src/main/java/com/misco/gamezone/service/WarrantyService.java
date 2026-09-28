/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.service;

import com.misco.gamezone.dao.SaleDAO;
import com.misco.gamezone.dao.WarrantyDAO;
import com.misco.gamezone.dao.WarrantyDAO.WarrantyRecord;
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
    private final SaleDAO saleDAO;
    private final ProductService productService;
    private final List<Warranty> warranties;

    /**
     * Creates a warranty service and resolves stored warranty references.
     *
     * @param warrantyDAO warranty data access object
     * @param saleDAO sale data access object
     * @param productService product service
     */
    public WarrantyService(
            WarrantyDAO warrantyDAO,
            SaleDAO saleDAO,
            ProductService productService) {

        this.warrantyDAO = warrantyDAO;
        this.saleDAO = saleDAO;
        this.productService = productService;
        this.warranties = new ArrayList<>();

        loadWarranties();
    }

    /**
     * Loads warranty records and resolves their product and sale references.
     */
    private void loadWarranties() {

        List<WarrantyRecord> records = warrantyDAO.loadAll();
        List<Sale> sales = saleDAO.loadSales();

        for (WarrantyRecord record : records) {

            Product product =
                    productService.findProductById(record.getProductId());

            Sale sale = findSaleById(
                    sales,
                    record.getSaleId()
            );

            if (product == null || sale == null) {
                continue;
            }

            Warranty warranty;

            if ("BASIC".equalsIgnoreCase(record.getType())) {

                warranty = new BasicWarranty(
                        record.getWarrantyId(),
                        product,
                        sale,
                        record.getStartDate()
                );

            } else if ("EXTENDED".equalsIgnoreCase(record.getType())) {

                warranty = new ExtendedWarranty(
                        record.getWarrantyId(),
                        product,
                        sale,
                        record.getStartDate()
                );

            } else {
                continue;
            }

            warranties.add(warranty);
        }
    }

    /**
     * Finds a sale by its identifier.
     *
     * @param sales sales to search
     * @param saleId sale identifier
     * @return matching sale or null if not found
     */
    private Sale findSaleById(
            List<Sale> sales,
            String saleId) {

        for (Sale sale : sales) {

            if (sale.getSaleId().equalsIgnoreCase(saleId)) {
                return sale;
            }
        }

        return null;
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
                    "Days ahead cannot be negative."
            );
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

        return String.format(
                "W%03d",
                highestNumber + 1
        );
    }
}