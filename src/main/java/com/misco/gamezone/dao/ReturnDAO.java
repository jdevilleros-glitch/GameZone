/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.dao;

import com.misco.gamezone.model.Accessory;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Return;
import com.misco.gamezone.model.Sale;
import com.misco.gamezone.persistence.AccessoryRepository;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the persistence of return data in a CSV file. It reconstructs the
 * relationships between returns, sales, and products.
 *
 * @author USUARIO
 */
public class ReturnDAO {

    private final String filePath;
    private SaleDAO saleDAO;
    private ProductDAO productDAO;
    private final AccessoryRepository accessoryRepository;

    /**
     * Creates a ReturnDAO with the dependencies required to reconstruct
     * returned products and accessories.
     *
     * @param filePath path of the returns data file
     * @param saleDAO DAO used to retrieve sale information
     * @param productDAO DAO used to retrieve product information
     * @param accessoryRepository repository used to retrieve accessories
     */
    public ReturnDAO(
            String filePath,
            SaleDAO saleDAO,
            ProductDAO productDAO,
            AccessoryRepository accessoryRepository) {

        this.filePath = filePath;
        this.saleDAO = saleDAO;
        this.productDAO = productDAO;
        this.accessoryRepository = accessoryRepository;
    }

    /**
     * Loads all returns stored in the data file.
     *
     * @return a list containing the stored returns
     */
    public List<Return> loadAll() {
        List<Return> returns = new ArrayList<>();

        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            return returns;
        }

        List<Sale> sales = saleDAO.loadSales();
        List<Product> products = productDAO.loadProducts();
        List<Accessory> accessories
                = accessoryRepository.loadAll();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(";");

                if (data.length != 6 && data.length != 7) {
                    continue;
                }

                String returnId = data[0];
                LocalDate returnDate = LocalDate.parse(data[1]);
                String saleId = data[2];
                String[] productIds = data[3].split(",");
                String reason = data[4];
                double warrantyRefundAmount = 0;

                if (data.length >= 7) {
                    warrantyRefundAmount = Double.parseDouble(data[6]);
                }

                Sale originalSale = null;

                for (Sale sale : sales) {
                    if (sale.getSaleId().equals(saleId)) {
                        originalSale = sale;
                        break;
                    }
                }

                if (originalSale == null) {
                    continue;
                }

                List<Product> returnedProducts = new ArrayList<>();

                for (String productId : productIds) {

                    Product matchingItem = null;

                    for (Product product : products) {
                        if (product.getId().equalsIgnoreCase(productId)) {
                            matchingItem = product;
                            break;
                        }
                    }

                    if (matchingItem == null) {

                        for (Accessory accessory : accessories) {
                            if (accessory.getId()
                                    .equalsIgnoreCase(productId)) {

                                matchingItem = accessory;
                                break;
                            }
                        }
                    }

                    if (matchingItem != null) {
                        returnedProducts.add(matchingItem);
                    }
                }

                if (returnedProducts.isEmpty()) {
                    continue;
                }

                Return returnRecord = new Return(
                        returnId,
                        returnDate,
                        originalSale,
                        returnedProducts,
                        reason
                );

                returnRecord.setWarrantyRefundAmount(
                        warrantyRefundAmount
                );

                returns.add(returnRecord);
            }

        } catch (IOException e) {
            System.out.println("Error reading returns file");
        }

        return returns;
    }

    /**
     * Saves all returns to the data file.
     *
     * @param returns list of returns to save
     */
    public void saveAll(List<Return> returns) {
        Path path = Path.of(filePath);

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {

                for (Return returnRecord : returns) {

                    String productIds = "";

                    for (Product product : returnRecord.getReturnedProducts()) {

                        if (!productIds.isEmpty()) {
                            productIds += ",";
                        }

                        productIds += product.getId();
                    }

                    String line = returnRecord.getReturnId() + ";"
                            + returnRecord.getReturnDate() + ";"
                            + returnRecord.getOriginalSale().getSaleId() + ";"
                            + productIds + ";"
                            + returnRecord.getReason() + ";"
                            + returnRecord.getRefundAmount() + ";"
                            + returnRecord.getWarrantyRefundAmount();

                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Error saving returns file");
        }
    }
}
