package com.misco.gamezone.service;

import com.misco.gamezone.dao.PersonDAO;
import com.misco.gamezone.dao.ProductDAO;
import com.misco.gamezone.dao.SaleDAO;
import com.misco.gamezone.exceptions.BusinessRuleException;
import com.misco.gamezone.exceptions.InvalidDataException;
import com.misco.gamezone.exceptions.ResourceNotFoundException;
import com.misco.gamezone.model.Accessory;
import com.misco.gamezone.model.Console;
import com.misco.gamezone.model.Customer;
import com.misco.gamezone.model.ExtendedWarranty;
import com.misco.gamezone.model.Person;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Promotion;
import com.misco.gamezone.model.Sale;
import com.misco.gamezone.model.Seller;
import com.misco.gamezone.validation.ProductValidator;
import com.misco.gamezone.validation.SaleValidator;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides business operations for managing sales in GameZone.
 *
 * Supports products, accessories, promotions, warranties,
 * stock updates, and sale queries.
 *
 * Uses validators and custom exceptions to enforce business rules.
 *
 * @author USUARIO
 */
public class SaleService {

    private final SaleDAO saleDAO;
    private final PersonDAO personDAO;
    private final ProductDAO productDAO;
    private final AccessoryService accessoryService;
    private final PromotionService promotionService;
    private final WarrantyService warrantyService;

    private final List<Sale> sales;

    /**
     * Creates a SaleService and loads the existing sales.
     *
     * @param saleDAO DAO used to load and save sales
     * @param personDAO DAO used to retrieve customers and sellers
     * @param productDAO DAO used to retrieve products
     * @param accessoryService service used to manage accessories
     * @param promotionService service used to manage promotions
     * @param warrantyService service used to manage warranties
     */
    public SaleService(
            SaleDAO saleDAO,
            PersonDAO personDAO,
            ProductDAO productDAO,
            AccessoryService accessoryService,
            PromotionService promotionService,
            WarrantyService warrantyService) {

        this.saleDAO = saleDAO;
        this.personDAO = personDAO;
        this.productDAO = productDAO;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
        this.warrantyService = warrantyService;
        this.sales = saleDAO.loadSales();
    }

    /**
     * Registers a sale containing products and/or accessories.
     *
     * Validates the sale information, customer, seller, items,
     * and available stock before registering the sale.
     *
     * Applies the best available promotion and assigns warranties
     * to consoles before calculating the final total.
     *
     * @param saleId unique sale identifier
     * @param date sale date
     * @param customerId customer identifier
     * @param sellerId seller identifier
     * @param itemIds identifiers of products or accessories
     * @param extendedWarrantyProductIds console identifiers requiring
     * extended warranty
     * @return true if the sale was registered successfully
     * @throws InvalidDataException if sale information is invalid
     * @throws ResourceNotFoundException if a customer, seller, or item
     * cannot be found
     * @throws BusinessRuleException if a business rule is violated
     */
    public boolean registerSale(
            String saleId,
            Date date,
            String customerId,
            String sellerId,
            List<String> itemIds,
            List<String> extendedWarrantyProductIds) {

        if (saleId == null || saleId.trim().isEmpty()) {
            throw new InvalidDataException(
                    "saleId", "Sale ID cannot be empty."
            );
        }

        if (date == null) {
            throw new InvalidDataException(
                    "date", "Sale date cannot be null."
            );
        }

        if (itemIds == null || itemIds.isEmpty()) {
            throw new BusinessRuleException(
                    "A sale must contain at least one item."
            );
        }

        if (findSaleById(saleId) != null) {
            throw new BusinessRuleException(
                    "A sale with ID " + saleId + " already exists."
            );
        }

        List<Person> persons = personDAO.loadPersons();
        List<Product> products = productDAO.loadProducts();

        Customer customer = findCustomer(persons, customerId);
        Seller seller = findSeller(persons, sellerId);

        if (customer == null) {
            throw new ResourceNotFoundException(
                    "Customer", String.valueOf(customerId)
            );
        }

        if (seller == null) {
            throw new ResourceNotFoundException(
                    "Seller", String.valueOf(sellerId)
            );
        }

        List<Product> itemsToSell = new ArrayList<>();

        Map<String, Integer> productCounts = new HashMap<>();
        Map<String, Integer> accessoryCounts = new HashMap<>();

        Map<String, Product> productsById = new HashMap<>();
        Map<String, Accessory> accessoriesById = new HashMap<>();

        for (String rawId : itemIds) {

            if (rawId == null || rawId.trim().isEmpty()) {
                throw new InvalidDataException(
                        "itemId", "Item ID cannot be empty."
                );
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

            throw new ResourceNotFoundException(
                    "Product or accessory", itemId
            );
        }

        SaleValidator.validateSaleData(
                customer,
                seller,
                itemsToSell
        );

        for (Map.Entry<String, Integer> entry
                : productCounts.entrySet()) {

            Product product = productsById.get(entry.getKey());
            int requested = entry.getValue();

            ProductValidator.validateStockAvailability(
                    product,
                    requested
            );
        }

        for (Map.Entry<String, Integer> entry
                : accessoryCounts.entrySet()) {

            Accessory accessory
                    = accessoriesById.get(entry.getKey());

            int requested = entry.getValue();

            if (accessory.getStock() < requested) {
                throw new BusinessRuleException(
                        "Insufficient stock for accessory: "
                        + accessory.getId()
                );
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
        }

        LocalDate saleDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        double extendedWarrantyCost = 0;

        for (Product product : itemsToSell) {

            if (!(product instanceof Console)) {
                continue;
            }

            /*
             * Every console receives a basic warranty.
             */
            warrantyService.assignBasicWarranty(
                    product,
                    sale,
                    saleDate
            );

            boolean extendedRequested
                    = extendedWarrantyProductIds != null
                    && extendedWarrantyProductIds.stream()
                            .anyMatch(id
                                    -> id != null
                                    && id.equalsIgnoreCase(
                                            product.getId()));

            if (extendedRequested) {

                ExtendedWarranty extendedWarranty
                        = warrantyService.assignExtendedWarranty(
                                product,
                                sale,
                                saleDate
                        );

                extendedWarrantyCost
                        += extendedWarranty.getAdditionalCost();
            }
        }

        sale.setExtendedWarrantyCost(
                extendedWarrantyCost
        );

        sale.calculateFinalTotal();

        for (Map.Entry<String, Integer> entry
                : productCounts.entrySet()) {

            Product product
                    = productsById.get(entry.getKey());

            product.setStock(
                    product.getStock() - entry.getValue()
            );
        }

        productDAO.saveProducts(products);

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
                throw new BusinessRuleException(
                        "Could not update accessory stock: "
                        + accessory.getId()
                );
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
     * @return a copy of the registered sales
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

        if (customerId == null) {
            return customerSales;
        }

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

        if (sellerId == null) {
            return sellerSales;
        }

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
     * Performs a case-insensitive search and returns null when
     * no matching sale exists.
     *
     * @param saleId sale identifier
     * @return matching sale or null if not found
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

    /**
     * Finds a customer in the provided person list.
     *
     * @param persons registered persons
     * @param customerId customer identifier
     * @return matching customer or null if not found
     */
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

    /**
     * Finds a seller in the provided person list.
     *
     * @param persons registered persons
     * @param sellerId seller identifier
     * @return matching seller or null if not found
     */
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

    /**
     * Finds a product in the provided product list.
     *
     * @param products registered products
     * @param itemId product identifier
     * @return matching product or null if not found
     */
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