package com.misco.gamezone.dao;

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
 * Handles sale persistence.
 *
 * @author USUARIO
 */
public class SaleDAO {

    private final String filePath;
    private final ProductDAO productDAO;
    private final PersonDAO personDAO;
    private final AccessoryRepository accessoryRepository;

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

    public List<Sale> loadSales() {

        List<Sale> sales = new ArrayList<>();

        List<Person> persons = personDAO.loadPersons();
        List<Product> products = productDAO.loadProducts();
        List<Accessory> accessories = accessoryRepository.loadAll();

        File file = new File(filePath);

        if (!file.exists()) {
            return sales;
        }

        SimpleDateFormat dateFormat
                = new SimpleDateFormat("yyyy-MM-dd");

        dateFormat.setLenient(false);

        try (BufferedReader reader
                = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";", -1);

                if (fields.length != 5
                        && fields.length != 7
                        && fields.length != 9) {

                    System.out.println(
                            "Invalid sale record: " + line
                    );
                    continue;
                }

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

                    discountAmount
                            = Double.parseDouble(fields[6].trim());
                }

                if (fields.length == 9) {
                    warrantyCost
                            = Double.parseDouble(fields[7].trim());

                    persistedTotal
                            = Double.parseDouble(fields[8].trim());
                }

                try {

                    Date date = dateFormat.parse(dateText);

                    Customer customer
                            = findCustomer(persons, customerId);

                    Seller seller
                            = findSeller(persons, sellerId);

                    if (customer == null || seller == null) {
                        continue;
                    }

                    List<Product> itemsSold
                            = new ArrayList<>();

                    if (!itemIdsText.isEmpty()) {

                        String[] itemIds
                                = itemIdsText.split(",");

                        for (String rawId : itemIds) {

                            String itemId = rawId.trim();

                            Product item
                                    = findProduct(products, itemId);

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

                    if (itemsSold.isEmpty()) {
                        continue;
                    }

                    Sale sale = new Sale(
                            saleId,
                            date,
                            itemsSold,
                            customer,
                            seller
                    );

                    sale.setAppliedPromotionName(
                            promotionName
                    );

                    sale.setDiscountAmount(
                            discountAmount
                    );

                    sale.setExtendedWarrantyCost(
                            warrantyCost
                    );

                    if (persistedTotal != null) {
                        sale.setTotal(persistedTotal);
                    } else {
                        sale.calculateFinalTotal();
                    }

                    sales.add(sale);

                } catch (ParseException
                        | IllegalArgumentException e) {

                    System.out.println(
                            "Could not load sale record: "
                            + line
                    );
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Error loading sales: "
                    + e.getMessage()
            );
        }

        return sales;
    }

    public void saveSales(List<Sale> sales) {

        File file = new File(filePath);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        SimpleDateFormat dateFormat
                = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedWriter writer
                = new BufferedWriter(new FileWriter(file))) {

            for (Sale sale : sales) {

                StringBuilder itemIds
                        = new StringBuilder();

                for (Product item
                        : sale.getProductsSold()) {

                    if (itemIds.length() > 0) {
                        itemIds.append(",");
                    }

                    itemIds.append(item.getId());
                }

                String promotionName
                        = sale.getAppliedPromotionName() == null
                        ? ""
                        : sale.getAppliedPromotionName();

                String line
                        = sale.getSaleId() + ";"
                        + dateFormat.format(
                                sale.getDate()) + ";"
                        + sale.getCustomer().getId() + ";"
                        + sale.getSeller().getId() + ";"
                        + itemIds + ";"
                        + promotionName + ";"
                        + sale.getDiscountAmount() + ";"
                        + sale.getExtendedWarrantyCost() + ";"
                        + sale.getTotal();

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println(
                    "Error saving sales: "
                    + e.getMessage()
            );
        }
    }

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
