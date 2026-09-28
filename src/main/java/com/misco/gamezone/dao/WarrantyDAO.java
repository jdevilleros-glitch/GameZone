/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.dao;

import com.misco.gamezone.model.BasicWarranty;
import com.misco.gamezone.model.ExtendedWarranty;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Sale;
import com.misco.gamezone.model.Warranty;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles warranty persistence in a CSV file.
 *
 * @author USUARIO
 */
public class WarrantyDAO {

    private final String filePath;
    private final SaleDAO saleDAO;
    private final ProductDAO productDAO;

    /**
     * Creates a warranty DAO.
     *
     * @param filePath warranty file path
     * @param saleDAO sale data access object
     * @param productDAO product data access object
     */
    public WarrantyDAO(String filePath, SaleDAO saleDAO,
                       ProductDAO productDAO) {
        this.filePath = filePath;
        this.saleDAO = saleDAO;
        this.productDAO = productDAO;
    }

    /**
     * Saves all warranties to the persistence file.
     *
     * @param warranties warranties to save
     */
    public void saveAll(List<Warranty> warranties) {
        File file = new File(filePath);

        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file))) {

            for (Warranty warranty : warranties) {

                String type;

                if (warranty instanceof BasicWarranty) {
                    type = "BASIC";
                } else if (warranty instanceof ExtendedWarranty) {
                    type = "EXTENDED";
                } else {
                    continue;
                }

                writer.write(
                        type + ";"
                        + warranty.getWarrantyId() + ";"
                        + warranty.getProduct().getId() + ";"
                        + warranty.getSale().getSaleId() + ";"
                        + warranty.getStartDate()
                );

                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error saving warranties: " + e.getMessage(), e);
        }
    }

    /**
     * Loads all warranties from the persistence file.
     *
     * @return stored warranties
     */
    public List<Warranty> loadAll() {

        List<Warranty> warranties = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return warranties;
        }

        List<Sale> sales = saleDAO.loadSales();
        List<Product> products = productDAO.loadProducts();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] data = line.split(";");

                if (data.length != 5) {
                    continue;
                }

                String type = data[0];
                String warrantyId = data[1];
                String productId = data[2];
                String saleId = data[3];

                Product product = findProduct(products, productId);
                Sale sale = findSale(sales, saleId);

                if (product == null || sale == null) {
                    continue;
                }

                java.time.LocalDate startDate =
                        java.time.LocalDate.parse(data[4]);

                Warranty warranty;

                if ("BASIC".equalsIgnoreCase(type)) {

                    warranty = new BasicWarranty(
                            warrantyId,
                            product,
                            sale,
                            startDate
                    );

                } else if ("EXTENDED".equalsIgnoreCase(type)) {

                    warranty = new ExtendedWarranty(
                            warrantyId,
                            product,
                            sale,
                            startDate
                    );

                } else {
                    continue;
                }

                warranties.add(warranty);
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error loading warranties: " + e.getMessage(), e);
        }

        return warranties;
    }

    private Product findProduct(List<Product> products, String productId) {
        for (Product product : products) {
            if (product.getId().equalsIgnoreCase(productId)) {
                return product;
            }
        }

        return null;
    }

    private Sale findSale(List<Sale> sales, String saleId) {
        for (Sale sale : sales) {
            if (sale.getSaleId().equalsIgnoreCase(saleId)) {
                return sale;
            }
        }

        return null;
    }
}
