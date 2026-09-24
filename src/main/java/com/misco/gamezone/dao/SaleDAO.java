/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.dao;

import com.misco.gamezone.model.Customer;
import com.misco.gamezone.model.Person;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Sale;
import com.misco.gamezone.model.Seller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author USUARIO
 */
public class SaleDAO {

    private final String filePath;
    private ProductDAO productDAO;
    private PersonDAO personDAO;

    public SaleDAO(String filePath, ProductDAO productDAO, PersonDAO personDAO) {
        this.filePath = filePath;
        this.productDAO = productDAO;
        this.personDAO = personDAO;
    }

    public List<Sale> loadSales() {
        List<Sale> sales = new ArrayList<>();
        List<Person> persons = personDAO.loadPersons();
        List<Product> products = productDAO.loadProducts();

        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            return sales;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(";");

                if (data.length != 5) {
                    continue;
                }

                String saleId = data[0];
                Date date = dateFormat.parse(data[1]);
                String customerId = data[2];
                String sellerId = data[3];
                String[] productIds = data[4].split(",");

                Customer customer = null;
                Seller seller = null;

                for (Person person : persons) {

                    if (person instanceof Customer && person.getId().equals(customerId)) {
                        customer = (Customer) person;
                    }

                    if (person instanceof Seller && person.getId().equals(sellerId)) {
                        seller = (Seller) person;
                    }
                }

                List<Product> productsSold = new ArrayList<>();

                for (String productId : productIds) {

                    for (Product product : products) {
                        if (product.getId().equals(productId)) {
                            productsSold.add(product);
                        }
                    }
                }

                if (customer != null && seller != null && !productsSold.isEmpty()) {
                    Sale sale = new Sale(
                            saleId,
                            date,
                            productsSold,
                            customer,
                            seller
                    );

                    sales.add(sale);

                }

            }
        } catch (IOException | ParseException ex) {
            System.out.println("Error reading sales file");
        }

        return sales;
    }

    public void saveSales(List<Sale> sales) {
        Path path = Path.of(filePath);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (Sale sale : sales) {

                    String productIds = "";

                    for (Product product : sale.getProductsSold()) {

                        if (!productIds.isEmpty()) {
                            productIds += ",";
                        }

                        productIds += product.getId();
                    }

                    String line = sale.getSaleid() + ";"
                            + dateFormat.format(sale.getDate()) + ";"
                            + sale.getCustomer().getId() + ";"
                            + sale.getSeller().getId() + ";"
                            + productIds;

                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving sales file");
        }
    }
}
