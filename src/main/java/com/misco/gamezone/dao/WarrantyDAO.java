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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles warranty persistence in a CSV file.
 *
 * @author USUARIO
 */
public class WarrantyDAO {

    private final String filePath;

    /**
     * Creates a warranty DAO.
     *
     * @param filePath warranty file path
     */
    public WarrantyDAO(String filePath) {
        this.filePath = filePath;
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

        try (BufferedWriter writer
                = new BufferedWriter(new FileWriter(file))) {

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
                    "Error saving warranties: "
                    + e.getMessage(), e
            );
        }
    }

    /**
     * Loads warranty persistence records without resolving product or sale
     * references.
     *
     * @return stored warranty records
     */
    public List<WarrantyRecord> loadAll() {

        List<WarrantyRecord> records = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader
                = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] data = line.split(";");

                if (data.length != 5) {
                    continue;
                }

                WarrantyRecord record = new WarrantyRecord(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        LocalDate.parse(data[4])
                );

                records.add(record);
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error loading warranties: "
                    + e.getMessage(), e
            );
        }

        return records;
    }

    /**
     * Represents warranty data loaded from persistence.
     */
    public static class WarrantyRecord {

        private final String type;
        private final String warrantyId;
        private final String productId;
        private final String saleId;
        private final LocalDate startDate;

        /**
         * Creates a warranty persistence record.
         *
         * @param type warranty type
         * @param warrantyId warranty identifier
         * @param productId product identifier
         * @param saleId sale identifier
         * @param startDate warranty start date
         */
        public WarrantyRecord(
                String type,
                String warrantyId,
                String productId,
                String saleId,
                LocalDate startDate) {

            this.type = type;
            this.warrantyId = warrantyId;
            this.productId = productId;
            this.saleId = saleId;
            this.startDate = startDate;
        }

        public String getType() {
            return type;
        }

        public String getWarrantyId() {
            return warrantyId;
        }

        public String getProductId() {
            return productId;
        }

        public String getSaleId() {
            return saleId;
        }

        public LocalDate getStartDate() {
            return startDate;
        }
    }
}
