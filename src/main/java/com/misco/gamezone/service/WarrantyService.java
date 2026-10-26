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
import java.util.Objects;

/**
 * Provides business logic for warranty management.
 *
 * GRASP:
 * - Controller: coordinates warranty-related operations.
 * - Information Expert: uses warranty objects to evaluate their validity.
 * - High Cohesion: concentrates on warranty management rules.
 * - Protected Variations: delegates persistence to WarrantyDAO.
 */
public class WarrantyService {

    private final WarrantyDAO warrantyDAO;
    private final SaleDAO saleDAO;
    private final ProductService productService;
    private final List<Warranty> warranties;

    public WarrantyService(
            WarrantyDAO warrantyDAO,
            SaleDAO saleDAO,
            ProductService productService) {

        this.warrantyDAO = Objects.requireNonNull(
                warrantyDAO, "WarrantyDAO es obligatorio.");
        this.saleDAO = Objects.requireNonNull(
                saleDAO, "SaleDAO es obligatorio.");
        this.productService = Objects.requireNonNull(
                productService, "ProductService es obligatorio.");

        this.warranties = new ArrayList<>();
        loadWarranties();
    }

    /**
     * Loads stored warranties and resolves their product and sale references.
     */
    private void loadWarranties() {
        List<WarrantyRecord> records = warrantyDAO.loadAll();
        List<Sale> sales = saleDAO.loadSales();

        for (WarrantyRecord record : records) {
            if (record == null) {
                continue;
            }

            Product product =
                    productService.findProductById(record.getProductId());
            Sale sale = findSaleById(sales, record.getSaleId());

            if (product == null || sale == null) {
                continue;
            }

            Warranty warranty = createWarranty(record, product, sale);

            if (warranty != null && !containsWarranty(warranty.getWarrantyId())) {
                warranties.add(warranty);
            }
        }
    }

    /**
     * Creates the corresponding warranty subtype from a stored record.
     */
    private Warranty createWarranty(
            WarrantyRecord record,
            Product product,
            Sale sale) {

        String type = record.getType();

        if ("BASIC".equalsIgnoreCase(type)) {
            return new BasicWarranty(
                    record.getWarrantyId(),
                    product,
                    sale,
                    record.getStartDate());
        }

        if ("EXTENDED".equalsIgnoreCase(type)) {
            return new ExtendedWarranty(
                    record.getWarrantyId(),
                    product,
                    sale,
                    record.getStartDate());
        }

        return null;
    }

    /**
     * Finds a sale by its identifier.
     */
    private Sale findSaleById(List<Sale> sales, String saleId) {
        if (sales == null || saleId == null) {
            return null;
        }

        for (Sale sale : sales) {
            if (sale != null
                    && sale.getSaleId() != null
                    && sale.getSaleId().equalsIgnoreCase(saleId)) {
                return sale;
            }
        }

        return null;
    }

    /**
     * Assigns a basic warranty to a product.
     */
    public BasicWarranty assignBasicWarranty(
            Product product,
            Sale sale,
            LocalDate startDate) {

        validateWarrantyAssignment(product, sale, startDate);

        BasicWarranty warranty = new BasicWarranty(
                generateWarrantyId(), product, sale, startDate);

        registerWarranty(warranty);
        return warranty;
    }

    /**
     * Assigns an extended warranty to a product.
     */
    public ExtendedWarranty assignExtendedWarranty(
            Product product,
            Sale sale,
            LocalDate startDate) {

        validateWarrantyAssignment(product, sale, startDate);

        ExtendedWarranty warranty = new ExtendedWarranty(
                generateWarrantyId(), product, sale, startDate);

        registerWarranty(warranty);
        return warranty;
    }

    /**
     * Validates the data required to assign a warranty.
     */
    private void validateWarrantyAssignment(
            Product product,
            Sale sale,
            LocalDate startDate) {

        Objects.requireNonNull(product, "El producto es obligatorio.");
        Objects.requireNonNull(sale, "La venta es obligatoria.");
        Objects.requireNonNull(startDate, "La fecha de inicio es obligatoria.");

        if (findWarrantyByProduct(product.getId(), sale.getSaleId()) != null) {
            throw new IllegalArgumentException(
                    "El producto ya tiene una garantía asociada a esta venta.");
        }
    }

    /**
     * Adds a warranty and persists the updated collection.
     */
    private void registerWarranty(Warranty warranty) {
        warranties.add(warranty);

        try {
            warrantyDAO.saveAll(warranties);
        } catch (RuntimeException e) {
            warranties.remove(warranty);
            throw e;
        }
    }

    /**
     * Finds the warranty associated with a product in a specific sale.
     */
    public Warranty findWarrantyByProduct(String productId, String saleId) {
        if (productId == null || saleId == null) {
            return null;
        }

        for (Warranty warranty : warranties) {
            if (warranty.getProduct().getId().equalsIgnoreCase(productId)
                    && warranty.getSale().getSaleId().equalsIgnoreCase(saleId)) {
                return warranty;
            }
        }

        return null;
    }

    /**
     * Returns all registered warranties.
     */
    public List<Warranty> listAllWarranties() {
        return new ArrayList<>(warranties);
    }

    /**
     * Returns warranties active on the current date.
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
     */
    public List<Warranty> listWarrantiesExpiringSoon(int daysAhead) {
        if (daysAhead < 0) {
            throw new IllegalArgumentException(
                    "Los días de anticipación no pueden ser negativos.");
        }

        LocalDate today = LocalDate.now();
        LocalDate limitDate = today.plusDays(daysAhead);
        List<Warranty> expiringWarranties = new ArrayList<>();

        for (Warranty warranty : warranties) {
            LocalDate endDate = warranty.getEndDate();

            if (!endDate.isBefore(today) && !endDate.isAfter(limitDate)) {
                expiringWarranties.add(warranty);
            }
        }

        return expiringWarranties;
    }

    /**
     * Cancels warranties for a product in a specific sale.
     * Extended warranties contribute their additional cost to the refund.
     *
     * @return refundable cost of cancelled extended warranties
     */
    public double cancelWarranties(String productId, String saleId) {
        if (productId == null || saleId == null) {
            throw new IllegalArgumentException(
                    "El producto y la venta son obligatorios.");
        }

        double refundableWarrantyCost = 0.0;
        List<Warranty> warrantiesToRemove = new ArrayList<>();

        for (Warranty warranty : warranties) {
            if (warranty.getProduct().getId().equalsIgnoreCase(productId)
                    && warranty.getSale().getSaleId().equalsIgnoreCase(saleId)) {

                if (warranty instanceof ExtendedWarranty) {
                    refundableWarrantyCost += warranty.getAdditionalCost();
                }

                warrantiesToRemove.add(warranty);
            }
        }

        if (warrantiesToRemove.isEmpty()) {
            return 0.0;
        }

        warranties.removeAll(warrantiesToRemove);

        try {
            warrantyDAO.saveAll(warranties);
        } catch (RuntimeException e) {
            warranties.addAll(warrantiesToRemove);
            throw e;
        }

        return refundableWarrantyCost;
    }

    /**
     * Checks whether a warranty identifier is already registered.
     */
    private boolean containsWarranty(String warrantyId) {
        if (warrantyId == null) {
            return false;
        }

        for (Warranty warranty : warranties) {
            if (warranty.getWarrantyId().equalsIgnoreCase(warrantyId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Generates the next warranty identifier.
     */
    private String generateWarrantyId() {
        int highestNumber = 0;

        for (Warranty warranty : warranties) {
            String id = warranty.getWarrantyId();

            if (id != null && id.matches("W\\d+")) {
                try {
                    int number = Integer.parseInt(id.substring(1));
                    highestNumber = Math.max(highestNumber, number);
                } catch (NumberFormatException e) {
                    // Ignore identifiers whose numeric portion is too large.
                }
            }
        }

        return String.format("W%03d", highestNumber + 1);
    }
}