/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.service;

import com.misco.gamezone.dao.ReturnDAO;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Return;
import com.misco.gamezone.model.Accessory;
import com.misco.gamezone.model.Sale;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides business operations for managing product returns.
 *
 * @author USUARIO
 */
public class ReturnService {

    private ReturnDAO returnDAO;
    private SaleService saleService;
    private ProductService productService;
    private List<Return> returns;
    private AccessoryService accessoryService;

    /**
     * Creates the return service and loads the stored returns.
     *
     * @param returnDAO DAO used for return persistence
     * @param saleService service used to retrieve sales
     * @param productService service used to restore product stock
     * @param accessoryService service used to restore accessory stock
     */
    public ReturnService(
            ReturnDAO returnDAO,
            SaleService saleService,
            ProductService productService,
            AccessoryService accessoryService) {

        this.returnDAO = returnDAO;
        this.saleService = saleService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.returns = returnDAO.loadAll();
    }

    /**
     * Registers a return for an existing sale.
     *
     * @param saleId identifier of the original sale
     * @param productIds identifiers of the products to return
     * @param reason reason for the return
     * @return the registered return
     * @throws IllegalArgumentException if the return cannot be registered
     */
    public Return registerReturn(String saleId,
            List<String> productIds,
            String reason) {

        Sale sale = saleService.findSaleById(saleId);

        if (sale == null) {
            throw new IllegalArgumentException("Sale not found.");
        }

        if (!sale.canBeReturned()) {
            throw new IllegalArgumentException(
                    "The return period of 30 days has expired.");
        }

        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one product must be selected.");
        }

        List<Product> returnedProducts = new ArrayList<>();

        for (String productId : productIds) {

            for (Return existingReturn : returns) {
                if (existingReturn.getOriginalSale()
                        .getSaleId()
                        .equalsIgnoreCase(saleId)) {

                    for (Product returnedProduct
                            : existingReturn.getReturnedProducts()) {

                        if (returnedProduct.getId()
                                .equalsIgnoreCase(productId)) {

                            throw new IllegalArgumentException(
                                    "Product " + productId
                                    + " has already been returned for this sale.");
                        }
                    }
                }
            }

            Product matchingProduct = null;

            for (Product product : sale.getProductsSold()) {
                if (product.getId().equalsIgnoreCase(productId)) {
                    matchingProduct = product;
                    break;
                }
            }

            if (matchingProduct == null) {
                throw new IllegalArgumentException(
                        "Product " + productId
                        + " does not belong to the original sale.");
            }

            returnedProducts.add(matchingProduct);
        }

        String returnId = generateReturnId();

        Return returnRecord = new Return(
                returnId,
                LocalDate.now(),
                sale,
                returnedProducts,
                reason
        );

        for (Product product : returnedProducts) {

            boolean restored;

            if (product instanceof Accessory) {

                restored = accessoryService.restoreStock(
                        product.getId(), 1
                );

            } else {

                restored = productService.restoreStock(
                        product.getId(), 1
                );
            }

            if (!restored) {
                throw new IllegalArgumentException(
                        "Stock could not be restored for item "
                        + product.getId() + "."
                );
            }
        }

        returns.add(returnRecord);
        returnDAO.saveAll(returns);

        return returnRecord;
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
     * @param customerId identifier of the customer
     * @return returns associated with the customer
     */
    public List<Return> viewReturnsByCustomer(String customerId) {
        List<Return> customerReturns = new ArrayList<>();

        for (Return returnRecord : returns) {
            if (returnRecord.getOriginalSale()
                    .getCustomer()
                    .getId()
                    .equalsIgnoreCase(customerId)) {

                customerReturns.add(returnRecord);
            }
        }

        return customerReturns;
    }

    /**
     * Returns all returns associated with a specific sale.
     *
     * @param saleId identifier of the sale
     * @return returns associated with the sale
     */
    public List<Return> viewReturnsBySale(String saleId) {
        List<Return> saleReturns = new ArrayList<>();

        for (Return returnRecord : returns) {
            if (returnRecord.getOriginalSale()
                    .getSaleId()
                    .equalsIgnoreCase(saleId)) {

                saleReturns.add(returnRecord);
            }
        }

        return saleReturns;
    }

    /**
     * Calculates the total value of sales for a specific month and year. The
     * final sale total includes discounts and extended warranty costs.
     *
     * @param month month to calculate, from 1 to 12
     * @param year year to calculate
     * @return total sales for the selected month
     */
    public double calculateMonthlySales(int month, int year) {

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
     * Calculates the total value of returns for a specific month and year.
     *
     * @param month month to calculate, from 1 to 12
     * @param year year to calculate
     * @return total returns for the selected month
     */
    public double calculateMonthlyReturns(int month, int year) {

        double returnsTotal = 0;

        for (Return returnRecord : returns) {

            LocalDate returnDate
                    = returnRecord.getReturnDate();

            if (returnDate.getMonthValue() == month
                    && returnDate.getYear() == year) {

                returnsTotal
                        += returnRecord.getRefundAmount();
            }
        }

        return returnsTotal;
    }

    /**
     * Calculates the monthly balance by subtracting returns from sales.
     *
     * @param month month to calculate, from 1 to 12
     * @param year year to calculate
     * @return monthly sales minus monthly returns
     */
    public double generateMonthlyBalance(int month, int year) {

        double salesTotal
                = calculateMonthlySales(month, year);

        double returnsTotal
                = calculateMonthlyReturns(month, year);

        return salesTotal - returnsTotal;
    }

    /**
     * Generates the next return identifier.
     *
     * @return a unique return identifier
     */
    private String generateReturnId() {

        int highestNumber = 0;

        for (Return returnRecord : returns) {
            String id = returnRecord.getReturnId();

            if (id != null && id.matches("R\\d+")) {
                int number = Integer.parseInt(id.substring(1));

                if (number > highestNumber) {
                    highestNumber = number;
                }
            }
        }

        return String.format("R%03d", highestNumber + 1);
    }
}
