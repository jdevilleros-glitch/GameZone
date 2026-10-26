package com.misco.gamezone.dao;

import com.misco.gamezone.model.BasicWarranty;
import com.misco.gamezone.model.ExtendedWarranty;
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
 * GRASP:
 * - Pure Fabrication: separates file persistence from the warranty model.
 * - High Cohesion: concentrates on saving and loading warranty records.
 * - Protected Variations: isolates file access behind a stable DAO interface.
 */
public class WarrantyDAO {

    private final String filePath;

    public WarrantyDAO(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La ruta del archivo de garantías es obligatoria.");
        }

        this.filePath = filePath;
    }

    /**
     * Saves all warranties to the persistence file.
     *
     * @param warranties warranties to save
     */
    public void saveAll(List<Warranty> warranties) {
        if (warranties == null) {
            throw new IllegalArgumentException(
                    "La lista de garantías no puede ser nula.");
        }

        File file = new File(filePath);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists() && !parent.mkdirs()
                && !parent.exists()) {
            throw new IllegalStateException(
                    "No se pudo crear el directorio de garantías.");
        }

        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(file))) {

            for (Warranty warranty : warranties) {
                if (warranty == null) {
                    continue;
                }

                String type = getWarrantyTypeCode(warranty);

                if (type == null) {
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
            throw new IllegalStateException(
                    "Error al guardar las garantías.", e);
        }
    }

    /**
     * Loads warranty records without resolving product or sale references.
     *
     * @return stored warranty records
     */
    public List<WarrantyRecord> loadAll() {
        List<WarrantyRecord> records = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                WarrantyRecord record = parseRecord(line);

                if (record != null) {
                    records.add(record);
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Error al cargar las garantías.", e);
        }

        return records;
    }

    /**
     * Determines the persistence code for a warranty subtype.
     *
     * @param warranty warranty to classify
     * @return persistence code or null if unsupported
     */
    private String getWarrantyTypeCode(Warranty warranty) {
        if (warranty instanceof BasicWarranty) {
            return "BASIC";
        }

        if (warranty instanceof ExtendedWarranty) {
            return "EXTENDED";
        }

        return null;
    }

    /**
     * Converts a persisted line into a warranty record.
     *
     * @param line line read from the file
     * @return parsed record, or null if the line is invalid
     */
    private WarrantyRecord parseRecord(String line) {
        String[] data = line.split(";", -1);

        if (data.length != 5) {
            return null;
        }

        try {
            String type = data[0].trim();
            String warrantyId = data[1].trim();
            String productId = data[2].trim();
            String saleId = data[3].trim();
            LocalDate startDate = LocalDate.parse(data[4].trim());

            if (type.isEmpty() || warrantyId.isEmpty()
                    || productId.isEmpty() || saleId.isEmpty()) {
                return null;
            }

            if (!type.equals("BASIC") && !type.equals("EXTENDED")) {
                return null;
            }

            return new WarrantyRecord(
                    type, warrantyId, productId, saleId, startDate);

        } catch (RuntimeException e) {
            return null;
        }
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