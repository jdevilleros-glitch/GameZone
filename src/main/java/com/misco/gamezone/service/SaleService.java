/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.service;

import com.misco.gamezone.dao.PersonDAO;
import com.misco.gamezone.dao.ProductDAO;
import com.misco.gamezone.dao.SaleDAO;
import com.misco.gamezone.model.Customer;
import com.misco.gamezone.model.Person;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Sale;
import com.misco.gamezone.model.Seller;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Provides the business logic for managing sales in GameZone. It allows sales
 * to be registered and provides operations to retrieve sales and purchase
 * histories.
 *
 * @author USUARIO
 */
public class SaleService {

    private SaleDAO saleDAO;
    private List<Sale> sales;
    private PersonDAO personDAO;
    private ProductDAO productDAO;

    /**
     * Creates a SaleService and loads the existing sales.
     *
     * @param saleDAO DAO used to load and save sales
     * @param personDAO DAO used to retrieve customers and sellers
     * @param productDAO DAO used to retrieve products
     */
    public SaleService(SaleDAO saleDAO, PersonDAO personDAO, ProductDAO productDAO) {
        this.saleDAO = saleDAO;
        this.personDAO = personDAO;
        this.productDAO = productDAO;
        this.sales = saleDAO.loadSales();
    }

    /**
     * Registers a new sale using an existing customer, seller, and one or more
     * existing products. The sale is saved automatically after being
     * registered.
     *
     * @param saleId unique identifier of the sale
     * @param date date when the sale is made
     * @param customerId identifier of the customer
     * @param sellerId identifier of the seller
     * @param productIds identifiers of the products included in the sale
     */
    public void registerSale(String saleId, Date date, String customerId, String sellerId, List<String> productIds) {
        List<Person> persons = personDAO.loadPersons();
        Customer customer = null;
        Seller seller = null;
        List<Product> products = productDAO.loadProducts();
        List<Product> productsSold = new ArrayList<>();

        for (Person person : persons) {

            if (person instanceof Customer && person.getId().equals(customerId)) {
                customer = (Customer) person;
            }

            if (person instanceof Seller && person.getId().equals(sellerId)) {
                seller = (Seller) person;
            }

        }

        for (String productId : productIds) {

            for (Product product : products) {

                if (product.getId().equals(productId)) {
                    productsSold.add(product);
                    break;
                }
            }

        }
        if (customer == null || seller == null || productsSold.isEmpty()) {
            return;
        }
        Sale sale = new Sale(saleId, date, productsSold, customer, seller);
        sales.add(sale);
        saleDAO.saveSales(sales);

    }

    /**
     * Returns all registered sales.
     *
     * @return a list containing all sales
     */
    public List<Sale> listSales() {
        return new ArrayList<>(sales);
    }

    /**
     * Returns the purchase history of a specific customer.
     *
     * @param customerId identifier of the customer
     * @return a list containing the sales associated with the customer
     */
    public List<Sale> getPurchasesByCustomer(String customerId) {
        List<Sale> customerSales = new ArrayList<>();

        for (Sale sale : sales) {
            if (sale.getCustomer().getId().equals(customerId)) {
                customerSales.add(sale);
            }
        }

        return customerSales;
    }

    /**
     * Returns the sales history of a specific seller.
     *
     * @param sellerId identifier of the seller
     * @return a list containing the sales associated with the seller
     */
    public List<Sale> getSalesBySeller(String sellerId) {
        List<Sale> sellerSales = new ArrayList<>();

        for (Sale sale : sales) {
            if (sale.getSeller().getId().equals(sellerId)) {
                sellerSales.add(sale);
            }
        }

        return sellerSales;
    }
}
