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
 * Handles persistence and reconstruction of return records.
 *
 * GRASP:
 * - Pure Fabrication: isolates file persistence from domain entities.
 * - Low Coupling: delegates sales, products, and accessories retrieval
 *   to their respective data access components.
 * - High Cohesion: focuses on loading and saving return records.
 *
 * @author USUARIO
 */
public class ReturnDAO {

    private final Path filePath;
    private final SaleDAO saleDAO;
    private final ProductDAO productDAO;
    private final AccessoryRepository accessoryRepository;

    public ReturnDAO(
            String filePath,
            SaleDAO saleDAO,
            ProductDAO productDAO,
            AccessoryRepository accessoryRepository) {

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be empty.");
        }

        if (saleDAO == null || productDAO == null
                || accessoryRepository == null) {
            throw new IllegalArgumentException(
                    "DAO and repository dependencies cannot be null.");
        }

        this.filePath = Path.of(filePath);
        this.saleDAO = saleDAO;
        this.productDAO = productDAO;
        this.accessoryRepository = accessoryRepository;
    }

    /**
     * Loads all persisted return records.
     *
     * @return loaded returns
     */
    public List<Return> loadAll() {
        List<Return> returns = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return returns;
        }

        List<Sale> sales = saleDAO.loadSales();
        List<Product> products = productDAO.loadProducts();
        List<Accessory> accessories = accessoryRepository.loadAll();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    Return returnRecord = parseReturn(
                            line, sales, products, accessories);

<<<<<<< Updated upstream
                String[] data = line.split(";");

                if (data.length != 6) {
                    continue;
                }

                String returnId = data[0];
                LocalDate returnDate = LocalDate.parse(data[1]);
                String saleId = data[2];
                String[] productIds = data[3].split(",");
                String reason = data[4];

                Sale originalSale = null;

                for (Sale sale : sales) {
                    if (sale.getSaleId().equals(saleId)) {
                        originalSale = sale;
                        break;
=======
                    if (returnRecord != null) {
                        returns.add(returnRecord);
>>>>>>> Stashed changes
                    }
                } catch (RuntimeException ex) {
                    // Ignore malformed records and continue loading.
                }
<<<<<<< Updated upstream

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

                returns.add(returnRecord);
=======
>>>>>>> Stashed changes
            }
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Unable to read returns file: " + filePath, ex);
        }

        return returns;
    }

    /**
     * Reconstructs a return from a persisted record.
     *
     * @param line persisted line
     * @param sales available sales
     * @param products available products
     * @param accessories available accessories
     * @return reconstructed return, or null if its relationships are missing
     */
    private Return parseReturn(
            String line,
            List<Sale> sales,
            List<Product> products,
            List<Accessory> accessories) {

        String[] data = line.split(";", -1);

        if (data.length != 6 && data.length != 7) {
            return null;
        }

        String returnId = data[0];
        LocalDate returnDate = LocalDate.parse(data[1]);
        String saleId = data[2];
        String reason = data[4];

        double warrantyRefundAmount = data.length == 7
                ? Double.parseDouble(data[6])
                : 0.0;

        Sale originalSale = findSale(saleId, sales);

        if (originalSale == null) {
            return null;
        }

        List<Product> returnedProducts = findReturnedProducts(
                data[3], products, accessories);

        if (returnedProducts.isEmpty()) {
            return null;
        }

        Return returnRecord = new Return(
                returnId,
                returnDate,
                originalSale,
                returnedProducts,
                reason);

        returnRecord.setWarrantyRefundAmount(warrantyRefundAmount);

        return returnRecord;
    }

    private Sale findSale(String saleId, List<Sale> sales) {
        for (Sale sale : sales) {
            if (sale != null && sale.getSaleId() != null
                    && sale.getSaleId().equals(saleId)) {
                return sale;
            }
        }

        return null;
    }

    private List<Product> findReturnedProducts(
            String productIds,
            List<Product> products,
            List<Accessory> accessories) {

        List<Product> result = new ArrayList<>();

        if (productIds == null || productIds.isEmpty()) {
            return result;
        }

        String[] ids = productIds.split(",");

        for (String id : ids) {
            Product product = findProduct(id, products);

            if (product == null) {
                product = findAccessory(id, accessories);
            }

            if (product != null) {
                result.add(product);
            }
        }

        return result;
    }

    private Product findProduct(String id, List<Product> products) {
        for (Product product : products) {
            if (product != null && product.getId() != null
                    && product.getId().equalsIgnoreCase(id)) {
                return product;
            }
        }

        return null;
    }

    private Accessory findAccessory(
            String id, List<Accessory> accessories) {

        for (Accessory accessory : accessories) {
            if (accessory != null && accessory.getId() != null
                    && accessory.getId().equalsIgnoreCase(id)) {
                return accessory;
            }
        }

        return null;
    }

    /**
     * Saves all returns to the data file.
     *
     * @param returns returns to persist
     */
    public void saveAll(List<Return> returns) {
        if (returns == null) {
            throw new IllegalArgumentException("Returns list cannot be null.");
        }

        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                for (Return returnRecord : returns) {
                    if (returnRecord == null
                            || returnRecord.getOriginalSale() == null) {
                        continue;
                    }

<<<<<<< Updated upstream
                    String line = returnRecord.getReturnId() + ";"
                            + returnRecord.getReturnDate() + ";"
                            + returnRecord.getOriginalSale().getSaleId() + ";"
                            + productIds + ";"
                            + returnRecord.getReason() + ";"
                            + returnRecord.getRefundAmount();

                    writer.write(line);
=======
                    writer.write(formatReturn(returnRecord));
>>>>>>> Stashed changes
                    writer.newLine();
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Unable to save returns file: " + filePath, ex);
        }
    }

    /**
     * Converts a return into the existing semicolon-separated format.
     *
     * @param returnRecord return to format
     * @return serialized return record
     */
    private String formatReturn(Return returnRecord) {
        StringBuilder productIds = new StringBuilder();

        for (Product product : returnRecord.getReturnedProducts()) {
            if (product == null) {
                continue;
            }

            if (productIds.length() > 0) {
                productIds.append(",");
            }

            productIds.append(product.getId());
        }

        return returnRecord.getReturnId() + ";"
                + returnRecord.getReturnDate() + ";"
                + returnRecord.getOriginalSale().getSaleId() + ";"
                + productIds + ";"
                + returnRecord.getReason() + ";"
                + returnRecord.getRefundAmount() + ";"
                + returnRecord.getWarrantyRefundAmount();
    }
}