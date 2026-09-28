package com.misco.gamezone.service;

import com.misco.gamezone.dao.PersonDAO;
import com.misco.gamezone.dao.ProductDAO;
import com.misco.gamezone.dao.SaleDAO;
import com.misco.gamezone.model.Accessory;
import com.misco.gamezone.model.Customer;
import com.misco.gamezone.model.Person;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Promotion;
import com.misco.gamezone.model.Sale;
import com.misco.gamezone.model.Seller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the business logic for managing sales in GameZone. Supports
 * products, accessories, promotions, stock updates, and sale queries.
 */
public class SaleService {

    private final SaleDAO saleDAO;
    private final PersonDAO personDAO;
    private final ProductDAO productDAO;
    private final AccessoryService accessoryService;
    private final PromotionService promotionService;

    private final List<Sale> sales;

    /**
     * Creates a SaleService and loads the existing sales.
     *
     * @param saleDAO DAO used to load and save sales
     * @param personDAO DAO used to retrieve customers and sellers
     * @param productDAO DAO used to retrieve products
     * @param accessoryService service used to manage accessories
     * @param promotionService service used to manage promotions
     */
    public SaleService(
            SaleDAO saleDAO,
            PersonDAO personDAO,
            ProductDAO productDAO,
            AccessoryService accessoryService,
            PromotionService promotionService) {

        this.saleDAO = saleDAO;
        this.personDAO = personDAO;
        this.productDAO = productDAO;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
        this.sales = saleDAO.loadSales();
    }

    /**
     * Registers a sale containing products and/or accessories.
     *
     * @param saleId unique sale identifier
     * @param date sale date
     * @param customerId customer identifier
     * @param sellerId seller identifier
     * @param itemIds identifiers of products or accessories
     * @return true if the sale was registered successfully
     */
    public boolean registerSale(
            String saleId,
            Date date,
            String customerId,
            String sellerId,
            List<String> itemIds) {

        if (saleId == null || saleId.trim().isEmpty()
                || date == null
                || itemIds == null || itemIds.isEmpty()) {
            System.out.println("Sale information is incomplete.");
            return false;
        }

        List<Person> persons = personDAO.loadPersons();
        List<Product> products = productDAO.loadProducts();

        Customer customer = findCustomer(persons, customerId);
        Seller seller = findSeller(persons, sellerId);

        if (customer == null) {
            System.out.println("Customer not found.");
            return false;
        }

        if (seller == null) {
            System.out.println("Seller not found.");
            return false;
        }

        List<Product> itemsToSell = new ArrayList<>();

        Map<String, Integer> productCounts = new HashMap<>();
        Map<String, Integer> accessoryCounts = new HashMap<>();

        Map<String, Product> productsById = new HashMap<>();
        Map<String, Accessory> accessoriesById = new HashMap<>();

        // Find every requested item and count the required units.
        for (String rawId : itemIds) {

            if (rawId == null || rawId.trim().isEmpty()) {
                System.out.println("An item ID is empty.");
                return false;
            }

            String itemId = rawId.trim();

            Product product = findProduct(products, itemId);

            if (product != null) {
                String key = product.getId().toLowerCase();

                productCounts.put(
                        key,
                        productCounts.getOrDefault(key, 0) + 1
                );

                productsById.put(key, product);
                itemsToSell.add(product);
                continue;
            }

            Accessory accessory = accessoryService.findById(itemId);

            if (accessory != null) {
                String key = accessory.getId().toLowerCase();

                accessoryCounts.put(
                        key,
                        accessoryCounts.getOrDefault(key, 0) + 1
                );

                accessoriesById.put(key, accessory);
                itemsToSell.add(accessory);
                continue;
            }

            System.out.println(
                    "Product or accessory not found: " + itemId);
            return false;
        }

        // Validate product stock.
        for (Map.Entry<String, Integer> entry
                : productCounts.entrySet()) {

            Product product = productsById.get(entry.getKey());
            int requested = entry.getValue();

            if (product.getStock() < requested) {
                System.out.println(
                        "Insufficient stock for product: "
                        + product.getId());
                return false;
            }
        }

        // Validate accessory stock.
        for (Map.Entry<String, Integer> entry
                : accessoryCounts.entrySet()) {

            Accessory accessory
                    = accessoriesById.get(entry.getKey());

            int requested = entry.getValue();

            if (accessory.getStock() < requested) {
                System.out.println(
                        "Insufficient stock for accessory: "
                        + accessory.getId());
                return false;
            }
        }

        /*
         * Create the sale before modifying inventory so that
         * the applicable promotion can be calculated.
         */
        Sale sale = new Sale(
                saleId.trim(),
                date,
                itemsToSell,
                customer,
                seller
        );

        Promotion bestPromotion
                = promotionService.findBestPromotionFor(sale);

        if (bestPromotion != null) {
            double discount
                    = bestPromotion.calculateDiscount(sale);

            sale.setAppliedPromotionName(
                    bestPromotion.getName()
            );

            sale.setDiscountAmount(discount);
            sale.setTotal(sale.getTotal() - discount);
        }

        // Update product inventory.
        for (Map.Entry<String, Integer> entry
                : productCounts.entrySet()) {

            Product product
                    = productsById.get(entry.getKey());

            product.setStock(
                    product.getStock() - entry.getValue()
            );
        }

        productDAO.saveProducts(products);

        // Update accessory inventory.
        for (Map.Entry<String, Integer> entry
                : accessoryCounts.entrySet()) {

            Accessory accessory
                    = accessoriesById.get(entry.getKey());

            int newStock
                    = accessory.getStock() - entry.getValue();

            boolean updated
                    = accessoryService.updateStock(
                            accessory.getId(),
                            newStock
                    );

            if (!updated) {
                System.out.println(
                        "Could not update accessory stock: "
                        + accessory.getId());
                return false;
            }
        }

        sales.add(sale);
        saleDAO.saveSales(sales);

        System.out.println("Sale registered successfully.");
        return true;
    }

    /**
     * Returns all registered sales.
     *
     * @return copy of the registered sales
     */
    public List<Sale> listSales() {
        return new ArrayList<>(sales);
    }

    /**
     * Returns the purchase history of a customer.
     *
     * @param customerId customer identifier
     * @return sales associated with the customer
     */
    public List<Sale> getCustomerPurchaseHistory(
            String customerId) {

        List<Sale> customerSales = new ArrayList<>();

        for (Sale sale : sales) {
            if (sale.getCustomer().getId()
                    .equalsIgnoreCase(customerId)) {
                customerSales.add(sale);
            }
        }

        return customerSales;
    }

    /**
     * Returns the sales associated with a seller.
     *
     * @param sellerId seller identifier
     * @return sales associated with the seller
     */
    public List<Sale> getSellerSales(String sellerId) {

        List<Sale> sellerSales = new ArrayList<>();

        for (Sale sale : sales) {
            if (sale.getSeller().getId()
                    .equalsIgnoreCase(sellerId)) {
                sellerSales.add(sale);
            }
        }

        return sellerSales;
    }

    /**
     * Finds a sale by its identifier.
     *
     * @param saleId sale identifier
     * @return matching sale, or null if it does not exist
     */
    public Sale findSaleById(String saleId) {

        if (saleId == null) {
            return null;
        }

        for (Sale sale : sales) {
            if (sale.getSaleId()
                    .equalsIgnoreCase(saleId.trim())) {
                return sale;
            }
        }

        return null;
    }

    private Customer findCustomer(
            List<Person> persons,
            String customerId) {

        if (customerId == null) {
            return null;
        }

        for (Person person : persons) {
            if (person instanceof Customer
                    && person.getId()
                            .equalsIgnoreCase(
                                    customerId.trim())) {
                return (Customer) person;
            }
        }

        return null;
    }

    private Seller findSeller(
            List<Person> persons,
            String sellerId) {

        if (sellerId == null) {
            return null;
        }

        for (Person person : persons) {
            if (person instanceof Seller
                    && person.getId()
                            .equalsIgnoreCase(
                                    sellerId.trim())) {
                return (Seller) person;
            }
        }

        return null;
    }

    private Product findProduct(
            List<Product> products,
            String itemId) {

        for (Product product : products) {
            if (product.getId()
                    .equalsIgnoreCase(itemId)) {
                return product;
            }
        }

        return null;
    }
}
