package com.misco.gamezone.service;

import com.misco.gamezone.dao.PersonDAO;
import com.misco.gamezone.dao.ProductDAO;
import com.misco.gamezone.dao.SaleDAO;
import com.misco.gamezone.model.Accessory;
import com.misco.gamezone.model.Customer;
import com.misco.gamezone.model.Person;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Sale;
import com.misco.gamezone.model.Seller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaleService {

    private final SaleDAO saleDAO;
    private final PersonDAO personDAO;
    private final ProductDAO productDAO;
    private final AccessoryService accessoryService;

    private List<Sale> sales;

    public SaleService(
            SaleDAO saleDAO,
            PersonDAO personDAO,
            ProductDAO productDAO,
            AccessoryService accessoryService) {

        this.saleDAO = saleDAO;
        this.personDAO = personDAO;
        this.productDAO = productDAO;
        this.accessoryService = accessoryService;
        this.sales = saleDAO.loadSales();
    }

    public boolean registerSale(
            String saleId,
            Date date,
            String customerId,
            String sellerId,
            List<String> itemIds) {

        if (saleId == null || saleId.trim().isEmpty()
                || date == null
                || itemIds == null || itemIds.isEmpty()) {
            System.out.println("La información de la venta está incompleta.");
            return false;
        }

        List<Person> persons = personDAO.loadPersons();
        List<Product> products = productDAO.loadProducts();

        Customer customer = findCustomer(persons, customerId);
        Seller seller = findSeller(persons, sellerId);

        if (customer == null) {
            System.out.println("No se encontró el cliente.");
            return false;
        }

        if (seller == null) {
            System.out.println("No se encontró el vendedor.");
            return false;
        }

        List<Product> itemsToSell = new ArrayList<>();
        Map<String, Integer> productCounts = new HashMap<>();
        Map<String, Integer> accessoryCounts = new HashMap<>();

        Map<String, Product> productsById = new HashMap<>();
        Map<String, Accessory> accessoriesById = new HashMap<>();

        // Buscar todos los artículos y contar cuántas unidades se solicitan.
        for (String rawId : itemIds) {
            if (rawId == null || rawId.trim().isEmpty()) {
                System.out.println("Hay un ID de artículo vacío.");
                return false;
            }

            String itemId = rawId.trim();
            Product product = findProduct(products, itemId);

            if (product != null) {
                String key = product.getId().toLowerCase();
                productCounts.put(key, productCounts.getOrDefault(key, 0) + 1);
                productsById.put(key, product);
                itemsToSell.add(product);
                continue;
            }

            Accessory accessory = accessoryService.findById(itemId);

            if (accessory != null) {
                String key = accessory.getId().toLowerCase();
                accessoryCounts.put(key,
                        accessoryCounts.getOrDefault(key, 0) + 1);
                accessoriesById.put(key, accessory);
                itemsToSell.add(accessory);
                continue;
            }

            System.out.println("No se encontró el producto o accesorio: " + itemId);
            return false;
        }

        // Comprobar el stock total requerido de cada producto.
        for (Map.Entry<String, Integer> entry : productCounts.entrySet()) {
            Product product = productsById.get(entry.getKey());
            int requested = entry.getValue();

            if (product.getStock() < requested) {
                System.out.println(
                        "Stock insuficiente para el producto: " + product.getId());
                return false;
            }
        }

        // Comprobar el stock total requerido de cada accesorio.
        for (Map.Entry<String, Integer> entry : accessoryCounts.entrySet()) {
            Accessory accessory = accessoriesById.get(entry.getKey());
            int requested = entry.getValue();

            if (accessory.getStock() < requested) {
                System.out.println(
                        "Stock insuficiente para el accesorio: "
                                + accessory.getId());
                return false;
            }
        }

        // Actualizar inventario de productos.
        for (Map.Entry<String, Integer> entry : productCounts.entrySet()) {
            Product product = productsById.get(entry.getKey());
            product.setStock(product.getStock() - entry.getValue());
        }

        productDAO.saveProducts(products);

        // Actualizar inventario de accesorios.
        for (Map.Entry<String, Integer> entry : accessoryCounts.entrySet()) {
            Accessory accessory = accessoriesById.get(entry.getKey());
            int newStock = accessory.getStock() - entry.getValue();

            boolean updated = accessoryService.updateStock(
                    accessory.getId(),
                    newStock
            );

            if (!updated) {
                System.out.println(
                        "No se pudo actualizar el stock del accesorio: "
                                + accessory.getId());
                return false;
            }
        }

        Sale sale = new Sale(
                saleId.trim(),
                date,
                itemsToSell,
                customer,
                seller
        );

        sales.add(sale);
        saleDAO.saveSales(sales);

        System.out.println("Venta registrada correctamente.");
        return true;
    }

    public List<Sale> listSales() {
        return new ArrayList<>(sales);
    }

    public List<Sale> getPurchasesByCustomer(String customerId) {
        List<Sale> customerSales = new ArrayList<>();

        for (Sale sale : sales) {
            if (sale.getCustomer().getId().equalsIgnoreCase(customerId)) {
                customerSales.add(sale);
            }
        }

        return customerSales;
    }

    public List<Sale> getSalesBySeller(String sellerId) {
        List<Sale> sellerSales = new ArrayList<>();

        for (Sale sale : sales) {
            if (sale.getSeller().getId().equalsIgnoreCase(sellerId)) {
                sellerSales.add(sale);
            }
        }

        return sellerSales;
    }

    private Customer findCustomer(List<Person> persons, String customerId) {
        if (customerId == null) {
            return null;
        }

        for (Person person : persons) {
            if (person instanceof Customer
                    && person.getId().equalsIgnoreCase(customerId.trim())) {
                return (Customer) person;
            }
        }

        return null;
    }

    private Seller findSeller(List<Person> persons, String sellerId) {
        if (sellerId == null) {
            return null;
        }

        for (Person person : persons) {
            if (person instanceof Seller
                    && person.getId().equalsIgnoreCase(sellerId.trim())) {
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
}