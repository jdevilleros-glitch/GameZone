package com.misco.gamezone.dao;

import com.misco.gamezone.exceptions.PersistenceException;
import com.misco.gamezone.model.Accessory;
import com.misco.gamezone.model.Customer;
import com.misco.gamezone.model.Person;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Sale;
import com.misco.gamezone.model.Seller;
import com.misco.gamezone.persistence.AccessoryRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Manages the persistence of sales in a text file.
 *
 * Loads and saves sales containing products and accessories.
 * Preserves compatibility with previous sale record formats.
 * Converts file operation failures into PersistenceException.
 *
 * @author USUARIO
 */
public class SaleDAO {

    private final String filePath;
    private final ProductDAO productDAO;
    private final PersonDAO personDAO;
    private final AccessoryRepository accessoryRepository;

    /**
     * Creates a SaleDAO using the specified persistence dependencies.
     *
     * @param filePath path of the sales file
     * @param productDAO DAO used to retrieve products
     * @param personDAO DAO used to retrieve customers and sellers
     * @param accessoryRepository repository used to retrieve accessories
     */
    public SaleDAO(
            String filePath,
            ProductDAO productDAO,
            PersonDAO personDAO,
            AccessoryRepository accessoryRepository) {

        this.filePath = filePath;
        this.productDAO = productDAO;
        this.personDAO = personDAO;
        this.accessoryRepository = accessoryRepository;
    }

    /**
     * Loads sales from the data file.
     *
     * Supports legacy records containing five or seven fields
     * and current records containing nine fields.
     *
     * Returns an empty list when the file does not exist.
     * Invalid individual records are skipped.
     *
     * @return a list containing the loaded sales
     * @throws PersistenceException if a file reading operation fails
     */
    public List<Sale> loadSales() {

        List<Sale> sales = new ArrayList<>();

        File file = new File(filePath);

        if (!file.exists()) {
            return sales;
        }

        List<Person> persons = personDAO.loadPersons();
        List<Product> products = productDAO.loadProducts();
        List<Accessory> accessories = accessoryRepository.loadAll();

        SimpleDateFormat dateFormat = createDateFormat();

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";", -1);

                if (!hasValidFieldCount(fields)) {

                    System.out.println(
                            "Invalid sale record: " + line
                    );

                    continue;
                }

                try {

                    Sale sale = parseSaleRecord(
                            fields,
                            persons,
                            products,
                            accessories,
                            dateFormat
                    );

                    if (sale != null) {
                        sales.add(sale);
                    }

                } catch (ParseException
                        | IllegalArgumentException e) {

                    System.out.println(
                            "Could not load sale record: " + line
                    );
                }
            }

        } catch (IOException e) {

            throw new PersistenceException(
                    "Error reading sales from file: " + filePath,
                    e
            );
        }

        return sales;
    }

    /**
     * Creates the date formatter used to read sale records.
     *
     * @return a non-lenient date formatter
     */
    private SimpleDateFormat createDateFormat() {

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd");

        dateFormat.setLenient(false);

        return dateFormat;
    }

    /**
     * Checks whether a sale record uses a supported format.
     *
     * @param fields fields extracted from the record
     * @return true if the record contains 5, 7, or 9 fields
     */
    private boolean hasValidFieldCount(String[] fields) {

        return fields.length == 5
                || fields.length == 7
                || fields.length == 9;
    }

    /**
     * Converts a valid-length record into a Sale object.
     *
     * Returns null when the referenced customer, seller,
     * or sale items cannot be resolved.
     *
     * @param fields record fields
     * @param persons registered persons
     * @param products registered products
     * @param accessories registered accessories
     * @param dateFormat date formatter
     * @return reconstructed sale or null
     * @throws ParseException if the date cannot be parsed
     * @throws IllegalArgumentException if numeric data is invalid
     */
    private Sale parseSaleRecord(
            String[] fields,
            List<Person> persons,
            List<Product> products,
            List<Accessory> accessories,
            SimpleDateFormat dateFormat)
            throws ParseException {

        String saleId = fields[0].trim();
        String dateText = fields[1].trim();
        String customerId = fields[2].trim();
        String sellerId = fields[3].trim();
        String itemIdsText = fields[4].trim();

        String promotionName = null;
        double discountAmount = 0;
        double warrantyCost = 0;
        Double persistedTotal = null;

        if (fields.length >= 7) {

            promotionName = fields[5].trim().isEmpty()
                    ? null
                    : fields[5].trim();

            discountAmount =
                    Double.parseDouble(fields[6].trim());
        }

        if (fields.length == 9) {

            warrantyCost =
                    Double.parseDouble(fields[7].trim());

            persistedTotal =
                    Double.parseDouble(fields[8].trim());
        }

        Date date = dateFormat.parse(dateText);

        Customer customer = findCustomer(persons, customerId);
        Seller seller = findSeller(persons, sellerId);

        if (customer == null || seller == null) {
            return null;
        }

        List<Product> itemsSold = resolveSaleItems(
                itemIdsText,
                products,
                accessories
        );

        if (itemsSold.isEmpty()) {
            return null;
        }

        Sale sale = new Sale(
                saleId,
                date,
                itemsSold,
                customer,
                seller
        );

        restoreSaleValues(
                sale,
                promotionName,
                discountAmount,
                warrantyCost,
                persistedTotal
        );

        return sale;
    }

    /**
     * Resolves the products and accessories referenced
     * by a persisted sale record.
     *
     * Preserves item order and repeated identifiers.
     * Unknown identifiers are ignored.
     *
     * @param itemIdsText comma-separated item identifiers
     * @param products registered products
     * @param accessories registered accessories
     * @return resolved items
     */
    private List<Product> resolveSaleItems(
            String itemIdsText,
            List<Product> products,
            List<Accessory> accessories) {

        List<Product> itemsSold = new ArrayList<>();

        if (!itemIdsText.isEmpty()) {

            String[] itemIds = itemIdsText.split(",");

            for (String rawId : itemIds) {

                String itemId = rawId.trim();

                Product item = findProduct(products, itemId);

                if (item == null) {

                    item = findAccessory(
                            accessories,
                            itemId
                    );
                }

                if (item != null) {
                    itemsSold.add(item);
                }
            }
        }

        return itemsSold;
    }

    /**
     * Restores the persisted promotion, warranty,
     * and total values of a sale.
     *
     * @param sale sale to update
     * @param promotionName persisted promotion name
     * @param discountAmount persisted discount
     * @param warrantyCost persisted warranty cost
     * @param persistedTotal persisted final total
     */
    private void restoreSaleValues(
            Sale sale,
            String promotionName,
            double discountAmount,
            double warrantyCost,
            Double persistedTotal) {

        sale.setAppliedPromotionName(promotionName);

        sale.setDiscountAmount(discountAmount);

        sale.setExtendedWarrantyCost(warrantyCost);

        if (persistedTotal != null) {
            sale.setTotal(persistedTotal);
        } else {
            sale.calculateFinalTotal();
        }
    }

    /**
     * Saves the specified sales to the data file.
     *
     * Stores each sale using the current nine-field format,
     * including promotion information, discount amount,
     * extended warranty cost, and final total.
     *
     * Creates the parent directory when necessary.
     *
     * @param sales list of sales to save
     * @throws PersistenceException if the file cannot be written
     */
    public void saveSales(List<Sale> sales) {

        File file = new File(filePath);

        ensureParentDirectoryExists(file);

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(file))) {

            for (Sale sale : sales) {

                String line = formatSaleRecord(
                        sale,
                        dateFormat
                );

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {

            throw new PersistenceException(
                    "Error saving sales to file: " + filePath,
                    e
            );
        }
    }

    /**
     * Creates the parent directory when necessary.
     *
     * @param file target sales file
     * @throws PersistenceException if directory creation fails
     */
    private void ensureParentDirectoryExists(File file) {

        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {

            if (!parent.mkdirs() && !parent.exists()) {

                throw new PersistenceException(
                        "Error creating sales directory: "
                        + parent.getPath(),
                        new IOException(
                                "Could not create directory: "
                                + parent.getPath()
                        )
                );
            }
        }
    }

    /**
     * Formats a sale using the existing nine-field
     * persistence format.
     *
     * @param sale sale to serialize
     * @param dateFormat date formatter
     * @return serialized sale record
     */
    private String formatSaleRecord(
            Sale sale,
            SimpleDateFormat dateFormat) {

        String itemIds = formatItemIds(
                sale.getProductsSold()
        );

        String promotionName =
                sale.getAppliedPromotionName() == null
                ? ""
                : sale.getAppliedPromotionName();

        return sale.getSaleId() + ";"
                + dateFormat.format(sale.getDate()) + ";"
                + sale.getCustomer().getId() + ";"
                + sale.getSeller().getId() + ";"
                + itemIds + ";"
                + promotionName + ";"
                + sale.getDiscountAmount() + ";"
                + sale.getExtendedWarrantyCost() + ";"
                + sale.getTotal();
    }

    /**
     * Formats sale item identifiers as a comma-separated string.
     *
     * @param items items included in the sale
     * @return comma-separated item identifiers
     */
    private String formatItemIds(List<Product> items) {

        StringBuilder itemIds = new StringBuilder();

        for (Product item : items) {

            if (itemIds.length() > 0) {
                itemIds.append(",");
            }

            itemIds.append(item.getId());
        }

        return itemIds.toString();
    }

    /**
     * Finds a customer in the provided person list.
     *
     * @param persons list of registered persons
     * @param customerId identifier of the customer
     * @return matching customer or null if not found
     */
    private Customer findCustomer(
            List<Person> persons,
            String customerId) {

        for (Person person : persons) {

            if (person instanceof Customer
                    && person.getId()
                            .equalsIgnoreCase(customerId)) {

                return (Customer) person;
            }
        }

        return null;
    }

    /**
     * Finds a seller in the provided person list.
     *
     * @param persons list of registered persons
     * @param sellerId identifier of the seller
     * @return matching seller or null if not found
     */
    private Seller findSeller(
            List<Person> persons,
            String sellerId) {

        for (Person person : persons) {

            if (person instanceof Seller
                    && person.getId()
                            .equalsIgnoreCase(sellerId)) {

                return (Seller) person;
            }
        }

        return null;
    }

    /**
     * Finds a product in the provided product list.
     *
     * @param products list of registered products
     * @param itemId identifier of the product
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

    /**
     * Finds an accessory in the provided accessory list.
     *
     * @param accessories list of registered accessories
     * @param itemId identifier of the accessory
     * @return matching accessory or null if not found
     */
    private Accessory findAccessory(
            List<Accessory> accessories,
            String itemId) {

        for (Accessory accessory : accessories) {

            if (accessory.getId()
                    .equalsIgnoreCase(itemId)) {

                return accessory;
            }
        }

        return null;
    }
}