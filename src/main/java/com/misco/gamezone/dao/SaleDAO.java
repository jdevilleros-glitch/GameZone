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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";", -1);

                if (fields.length != 5) {
                    System.out.println("Registro de venta inválido: " + line);
                    continue;
                }

                String saleId = fields[0].trim();
                String dateText = fields[1].trim();
                String customerId = fields[2].trim();
                String sellerId = fields[3].trim();
                String itemIdsText = fields[4].trim();

                try {
                    Date date = dateFormat.parse(dateText);

                    Customer customer = findCustomer(persons, customerId);
                    Seller seller = findSeller(persons, sellerId);

                    if (customer == null || seller == null) {
                        System.out.println(
                                "No se encontró el cliente o vendedor de la venta: "
                                        + saleId);
                        continue;
                    }

                    List<Product> itemsSold = new ArrayList<>();
                    boolean validItems = true;

                    if (!itemIdsText.isEmpty()) {
                        String[] itemIds = itemIdsText.split(",");

                        for (String rawId : itemIds) {
                            String itemId = rawId.trim();

                            if (itemId.isEmpty()) {
                                continue;
                            }

                            Product item = findProduct(products, itemId);

                            if (item == null) {
                                item = findAccessory(accessories, itemId);
                            }

                            if (item == null) {
                                System.out.println(
                                        "No se encontró el artículo " + itemId
                                                + " de la venta " + saleId);
                                validItems = false;
                                break;
                            }

                            itemsSold.add(item);
                        }
                    }

                    if (!validItems || itemsSold.isEmpty()) {
                        continue;
                    }

                    Sale sale = new Sale(
                            saleId,
                            date,
                            itemsSold,
                            customer,
                            seller
                    );

                    sales.add(sale);

                } catch (ParseException | IllegalArgumentException e) {
                    System.out.println(
                            "No se pudo cargar el registro de venta: " + line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error al cargar las ventas: " + e.getMessage());
        }

        return sales;
    }

    public void saveSales(List<Sale> sales) {
        File file = new File(filePath);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Sale sale : sales) {
                StringBuilder itemIds = new StringBuilder();

                for (Product item : sale.getProductsSold()) {
                    if (itemIds.length() > 0) {
                        itemIds.append(",");
                    }

                    itemIds.append(item.getId());
                }

                writer.write(
                        sale.getSaleid() + ";"
                                + dateFormat.format(sale.getDate()) + ";"
                                + sale.getCustomer().getId() + ";"
                                + sale.getSeller().getId() + ";"
                                + itemIds
                );

                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error al guardar las ventas: " + e.getMessage());
        }
    }

    private Customer findCustomer(List<Person> persons, String customerId) {
        for (Person person : persons) {
            if (person instanceof Customer
                    && person.getId().equalsIgnoreCase(customerId)) {
                return (Customer) person;
            }
        }

        return null;
    }

    private Seller findSeller(List<Person> persons, String sellerId) {
        for (Person person : persons) {
            if (person instanceof Seller
                    && person.getId().equalsIgnoreCase(sellerId)) {
                return (Seller) person;
            }
        }

        return null;
    }

    private Product findProduct(List<Product> products, String itemId) {
        for (Product product : products) {
            if (product.getId().equalsIgnoreCase(itemId)) {
                return product;
            }
        }

        return null;
    }

    private Accessory findAccessory(
            List<Accessory> accessories,
            String itemId) {

        for (Accessory accessory : accessories) {
            if (accessory.getId().equalsIgnoreCase(itemId)) {
                return accessory;
            }
        }

        return null;
    }
}