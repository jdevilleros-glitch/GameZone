package com.misco.gamezone.service;

import com.misco.gamezone.dao.ReturnDAO;
import com.misco.gamezone.model.Accessory;
import com.misco.gamezone.model.Console;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Return;
import com.misco.gamezone.model.Sale;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides business operations for managing product returns.
 *
 * GRASP:
 * - Controller: coordinates the return registration use case.
 * - Information Expert: delegates domain calculations to domain objects.
 * - Low Coupling: delegates persistence and stock operations to collaborators.
 * - High Cohesion: organizes operations related to product returns.
 *
 * @author USUARIO
 */
public class ReturnService {

    private final ReturnDAO returnDAO;
    private final SaleService saleService;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final WarrantyService warrantyService;
    private final List<Return> returns;

    public ReturnService(
            ReturnDAO returnDAO,
            SaleService saleService,
            ProductService productService,
            AccessoryService accessoryService,
            WarrantyService warrantyService) {

        if (returnDAO == null || saleService == null
                || productService == null || accessoryService == null
                || warrantyService == null) {
            throw new IllegalArgumentException(
                    "Return service dependencies cannot be null.");
        }

        this.returnDAO = returnDAO;
        this.saleService = saleService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.warrantyService = warrantyService;
        this.returns = new ArrayList<>(returnDAO.loadAll());
    }

    /**
     * Registers a return for an existing sale.
     *
     * @param saleId identifier of the original sale
     * @param productIds identifiers of the products to return
     * @param reason reason for the return
     * @return the registered return
     */
    public Return registerReturn(
            String saleId, List<String> productIds, String reason) {

        Sale sale = saleService.findSaleById(saleId);

        if (sale == null) {
            throw new IllegalArgumentException("Sale not found.");
        }

        if (!sale.canBeReturned()) {
            throw new IllegalArgumentException(
                    "The return period of 30 days has expired.");
        }

        validateReturnRequest(productIds, reason);

        List<Product> returnedProducts = findProductsForReturn(
                sale, saleId, productIds);

        Return returnRecord = new Return(
                generateReturnId(),
                LocalDate.now(),
                sale,
                returnedProducts,
                reason);

        double warrantyRefundAmount = cancelApplicableWarranties(
                sale, returnedProducts);

        returnRecord.setWarrantyRefundAmount(warrantyRefundAmount);

        restoreReturnedStock(returnedProducts);

        returns.add(returnRecord);

        try {
            returnDAO.saveAll(returns);
        } catch (RuntimeException ex) {
            returns.remove(returnRecord);
            throw ex;
        }

        return returnRecord;
    }

    private void validateReturnRequest(
            List<String> productIds, String reason) {

        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one product must be selected.");
        }

        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "A return reason must be provided.");
        }
    }

    private List<Product> findProductsForReturn(
            Sale sale, String saleId, List<String> productIds) {

        List<Product> returnedProducts = new ArrayList<>();

        for (String productId : productIds) {
            if (productId == null || productId.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Product identifier cannot be empty.");
            }

            validateNotPreviouslyReturned(saleId, productId);

            Product matchingProduct = findProductInSale(sale, productId);

            if (matchingProduct == null) {
                throw new IllegalArgumentException(
                        "Product " + productId
                        + " does not belong to the original sale.");
            }

            if (containsProduct(returnedProducts, productId)) {
                throw new IllegalArgumentException(
                        "Product " + productId
                        + " appears more than once in this return.");
            }

            returnedProducts.add(matchingProduct);
        }

        return returnedProducts;
    }

    private void validateNotPreviouslyReturned(
            String saleId, String productId) {

        for (Return existingReturn : returns) {
            Sale originalSale = existingReturn.getOriginalSale();

            if (originalSale == null
                    || !originalSale.getSaleId().equalsIgnoreCase(saleId)) {
                continue;
            }

            for (Product returnedProduct
                    : existingReturn.getReturnedProducts()) {

                if (returnedProduct != null
                        && returnedProduct.getId() != null
                        && returnedProduct.getId()
                                .equalsIgnoreCase(productId)) {
                    throw new IllegalArgumentException(
                            "Product " + productId
                            + " has already been returned for this sale.");
                }
            }
        }
    }

    private Product findProductInSale(Sale sale, String productId) {
        for (Product product : sale.getProductsSold()) {
            if (product != null && product.getId() != null
                    && product.getId().equalsIgnoreCase(productId)) {
                return product;
            }
        }

        return null;
    }

    private boolean containsProduct(
            List<Product> products, String productId) {

        for (Product product : products) {
            if (product.getId().equalsIgnoreCase(productId)) {
                return true;
            }
        }

        return false;
    }

    private double cancelApplicableWarranties(
            Sale sale, List<Product> returnedProducts) {

        double refund = 0;

        for (Product product : returnedProducts) {
            if (product instanceof Console) {
                refund += warrantyService.cancelWarranties(
                        product.getId(), sale.getSaleId());
            }
        }

        return refund;
    }

    private void restoreReturnedStock(List<Product> returnedProducts) {
        for (Product product : returnedProducts) {
            boolean restored;

            if (product instanceof Accessory) {
                restored = accessoryService.restoreStock(
                        product.getId(), 1);
            } else {
                restored = productService.restoreStock(
                        product.getId(), 1);
            }

            if (!restored) {
                throw new IllegalArgumentException(
                        "Stock could not be restored for item "
                        + product.getId() + ".");
            }
        }
    }

    /**
     * Returns all registered returns.
     *
     * @return a copy of the return list
     */
    public List<Return> viewAllReturns() {
        return new ArrayList<>(returns);
    }

    /**
     * Returns all returns associated with a customer.
     *
     * @param customerId customer identifier
     * @return matching returns
     */
    public List<Return> viewReturnsByCustomer(String customerId) {
        List<Return> customerReturns = new ArrayList<>();

        for (Return returnRecord : returns) {
            Sale sale = returnRecord.getOriginalSale();

            if (sale != null && sale.getCustomer() != null
                    && sale.getCustomer().getId() != null
                    && sale.getCustomer().getId()
                            .equalsIgnoreCase(customerId)) {
                customerReturns.add(returnRecord);
            }
        }

        return customerReturns;
    }

    /**
     * Returns all returns associated with a sale.
     *
     * @param saleId sale identifier
     * @return matching returns
     */
    public List<Return> viewReturnsBySale(String saleId) {
        List<Return> saleReturns = new ArrayList<>();

        for (Return returnRecord : returns) {
            Sale sale = returnRecord.getOriginalSale();

            if (sale != null && sale.getSaleId() != null
                    && sale.getSaleId().equalsIgnoreCase(saleId)) {
                saleReturns.add(returnRecord);
            }
        }

        return saleReturns;
    }

    /**
     * Calculates sales for a specific month and year.
     *
     * @param month month from 1 to 12
     * @param year year to calculate
     * @return monthly sales total
     */
    public double calculateMonthlySales(int month, int year) {
        validateMonth(month);

        double salesTotal = 0;

        for (Sale sale : saleService.listSales()) {
            LocalDate saleDate = sale.getDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (saleDate.getMonthValue() == month
                    && saleDate.getYear() == year) {
                salesTotal += sale.getTotal();
            }
        }

        return salesTotal;
    }

    /**
     * Calculates returns for a specific month and year.
     *
     * @param month month from 1 to 12
     * @param year year to calculate
     * @return monthly returns total
     */
    public double calculateMonthlyReturns(int month, int year) {
        validateMonth(month);

        double returnsTotal = 0;

        for (Return returnRecord : returns) {
            LocalDate returnDate = returnRecord.getReturnDate();

            if (returnDate != null
                    && returnDate.getMonthValue() == month
                    && returnDate.getYear() == year) {
                returnsTotal += returnRecord.getRefundAmount();
            }
        }

        return returnsTotal;
    }

    /**
     * Calculates the monthly balance by subtracting returns from sales.
     *
     * @param month month from 1 to 12
     * @param year year to calculate
     * @return sales minus returns
     */
    public double generateMonthlyBalance(int month, int year) {
        return calculateMonthlySales(month, year)
                - calculateMonthlyReturns(month, year);
    }

    private void validateMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException(
                    "Month must be between 1 and 12.");
        }
    }

    /**
     * Generates the next return identifier.
     *
     * @return unique return identifier
     */
    private String generateReturnId() {
        int highestNumber = 0;

        for (Return returnRecord : returns) {
            String id = returnRecord.getReturnId();

            if (id != null && id.matches("R\\d+")) {
                try {
                    int number = Integer.parseInt(id.substring(1));
                    highestNumber = Math.max(highestNumber, number);
                } catch (NumberFormatException ex) {
                    // Ignore identifiers that exceed the supported integer range.
                }
            }
        }

        return String.format("R%03d", highestNumber + 1);
    }
}