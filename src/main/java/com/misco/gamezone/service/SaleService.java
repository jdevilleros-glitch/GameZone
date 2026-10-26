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

        validateSaleRegistration(saleId, date, itemIds);

        List<Person> persons = personDAO.loadPersons();
        List<Product> products = productDAO.loadProducts();

        Customer customer = requireCustomer(persons, customerId);
        Seller seller = requireSeller(persons, sellerId);

        ResolvedItems resolved = resolveSaleItems(itemIds, products);

        SaleValidator.validateSaleData(
                customer,
                seller,
                resolved.itemsToSell
        );

        validateAvailableStock(resolved);

        /*
         * Create the sale before modifying inventory so that
         * the applicable promotion can be calculated.
         */
        Sale sale = new Sale(
                saleId.trim(),
                date,
                resolved.itemsToSell,
                customer,
                seller
        );

        applyBestPromotion(sale);

        sale.setExtendedWarrantyCost(
                assignSaleWarranties(
                        sale,
                        date,
                        resolved.itemsToSell,
                        extendedWarrantyProductIds
                )
        );

        sale.calculateFinalTotal();

        updateSoldInventory(resolved, products);

        sales.add(sale);
        saleDAO.saveSales(sales);

        System.out.println("Sale registered successfully.");

        return true;
    }

    /**
     * Validates the initial requirements for registering a sale.
     *
     * @param saleId unique sale identifier
     * @param date sale date
     * @param itemIds identifiers of the items to be sold
     * @throws InvalidDataException if the sale ID or date is invalid
     * @throws BusinessRuleException if the sale has no items
     * or its identifier already exists
     */
    private void validateSaleRegistration(
            String saleId,
            Date date,
            List<String> itemIds) {

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
    }

    /**
     * Finds the customer required for a sale.
     *
     * @param persons available persons
     * @param customerId customer identifier
     * @return the matching customer
     * @throws ResourceNotFoundException if the customer does not exist
     */
    private Customer requireCustomer(
            List<Person> persons,
            String customerId) {

        Customer customer = findCustomer(persons, customerId);

        if (customer == null) {
            throw new ResourceNotFoundException(
                    "Customer", String.valueOf(customerId)
            );
        }

        return customer;
    }

    /**
     * Finds the seller required for a sale.
     *
     * @param persons available persons
     * @param sellerId seller identifier
     * @return the matching seller
     * @throws ResourceNotFoundException if the seller does not exist
     */
    private Seller requireSeller(
            List<Person> persons,
            String sellerId) {

        Seller seller = findSeller(persons, sellerId);

        if (seller == null) {
            throw new ResourceNotFoundException(
                    "Seller", String.valueOf(sellerId)
            );
        }

        return seller;
    }

    /**
     * Validates and normalizes an item identifier.
     *
     * @param rawId item identifier to validate
     * @return trimmed item identifier
     * @throws InvalidDataException if the identifier is null or blank
     */
    private String requireValidItemId(String rawId) {

        if (rawId == null || rawId.trim().isEmpty()) {
            throw new InvalidDataException(
                    "itemId", "Item ID cannot be empty."
            );
        }

        return rawId.trim();
    }

    /**
     * Holds the items and count maps resolved during registration.
     */
    private static final class ResolvedItems {

        private final List<Product> itemsToSell = new ArrayList<>();

        private final Map<String, Integer> productCounts =
                new HashMap<>();

        private final Map<String, Integer> accessoryCounts =
                new HashMap<>();

        private final Map<String, Product> productsById =
                new HashMap<>();

        private final Map<String, Accessory> accessoriesById =
                new HashMap<>();
    }

    /**
     * Resolves each requested item, preserving order
     * and duplicate counts.
     *
     * @param itemIds requested item identifiers
     * @param products available products
     * @return resolved sale items and quantities
     */
    private ResolvedItems resolveSaleItems(
            List<String> itemIds,
            List<Product> products) {

        ResolvedItems resolved = new ResolvedItems();

        for (String rawId : itemIds) {

            String itemId = requireValidItemId(rawId);

            Product product = findProduct(products, itemId);

            if (product != null) {

                String key = product.getId().toLowerCase();

                resolved.productCounts.put(
                        key,
                        resolved.productCounts.getOrDefault(key, 0) + 1
                );

                resolved.productsById.put(key, product);
                resolved.itemsToSell.add(product);

                continue;
            }

            Accessory accessory = accessoryService.findById(itemId);

            if (accessory != null) {

                String key = accessory.getId().toLowerCase();

                resolved.accessoryCounts.put(
                        key,
                        resolved.accessoryCounts.getOrDefault(key, 0) + 1
                );

                resolved.accessoriesById.put(key, accessory);
                resolved.itemsToSell.add(accessory);

                continue;
            }

            throw new ResourceNotFoundException(
                    "Product or accessory", itemId
            );
        }

        return resolved;
    }

    /**
     * Validates product and accessory stock
     * before any inventory changes.
     *
     * @param resolved resolved sale items and quantities
     */
    private void validateAvailableStock(ResolvedItems resolved) {

        for (Map.Entry<String, Integer> entry
                : resolved.productCounts.entrySet()) {

            Product product =
                    resolved.productsById.get(entry.getKey());

            ProductValidator.validateStockAvailability(
                    product,
                    entry.getValue()
            );
        }

        for (Map.Entry<String, Integer> entry
                : resolved.accessoryCounts.entrySet()) {

            Accessory accessory =
                    resolved.accessoriesById.get(entry.getKey());

            int requested = entry.getValue();

            if (accessory.getStock() < requested) {
                throw new BusinessRuleException(
                        "Insufficient stock for accessory: "
                        + accessory.getId()
                );
            }
        }
    }

    /**
     * Applies the best available promotion
     * using the existing business rules.
     *
     * @param sale sale to evaluate
     */
    private void applyBestPromotion(Sale sale) {

        Promotion bestPromotion =
                promotionService.findBestPromotionFor(sale);

        if (bestPromotion != null) {

            double discount =
                    bestPromotion.calculateDiscount(sale);

            sale.setAppliedPromotionName(
                    bestPromotion.getName()
            );

            sale.setDiscountAmount(discount);
        }
    }

    /**
     * Assigns warranties and returns the total
     * extended-warranty cost.
     *
     * @param sale registered sale
     * @param date sale date
     * @param itemsToSell items included in the sale
     * @param extendedWarrantyProductIds identifiers requiring
     * extended warranties
     * @return total cost of extended warranties
     */
    private double assignSaleWarranties(
            Sale sale,
            Date date,
            List<Product> itemsToSell,
            List<String> extendedWarrantyProductIds) {

        LocalDate saleDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        double extendedWarrantyCost = 0;

        for (Product product : itemsToSell) {

            if (!(product instanceof Console)) {
                continue;
            }

            warrantyService.assignBasicWarranty(
                    product,
                    sale,
                    saleDate
            );

            boolean extendedRequested =
                    extendedWarrantyProductIds != null
                    && extendedWarrantyProductIds.stream()
                            .anyMatch(id
                                    -> id != null
                                    && id.equalsIgnoreCase(
                                            product.getId()));

            if (extendedRequested) {

                ExtendedWarranty extendedWarranty =
                        warrantyService.assignExtendedWarranty(
                                product,
                                sale,
                                saleDate
                        );

                extendedWarrantyCost +=
                        extendedWarranty.getAdditionalCost();
            }
        }

        return extendedWarrantyCost;
    }

    /**
     * Updates sold inventory in the original
     * product-then-accessory order.
     *
     * @param resolved resolved sale items and quantities
     * @param products available products
     */
    private void updateSoldInventory(
            ResolvedItems resolved,
            List<Product> products) {

        for (Map.Entry<String, Integer> entry
                : resolved.productCounts.entrySet()) {

            Product product =
                    resolved.productsById.get(entry.getKey());

            product.setStock(
                    product.getStock() - entry.getValue()
            );
        }

        productDAO.saveProducts(products);

        for (Map.Entry<String, Integer> entry
                : resolved.accessoryCounts.entrySet()) {

            Accessory accessory =
                    resolved.accessoriesById.get(entry.getKey());

            int newStock =
                    accessory.getStock() - entry.getValue();

            boolean updated =
                    accessoryService.updateStock(
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