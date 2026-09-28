/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.ui;

import com.misco.gamezone.model.Accessory;
import com.misco.gamezone.model.Customer;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Sale;
import com.misco.gamezone.model.Seller;
import com.misco.gamezone.service.AccessoryService;
import com.misco.gamezone.service.PersonService;
import com.misco.gamezone.service.ProductService;
import com.misco.gamezone.service.SaleService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Provides the console-based user interface for GameZone. It allows users to
 * manage products, customers, sellers, sales, and accessories through the
 * application services.
 *
 * @author USUARIO
 */
public class Menu {

    private ProductService productService;
    private PersonService personService;
    private SaleService saleService;
    private AccessoryService accessoryService;
    private Scanner scanner;

    /**
     * Creates the application menu using the services required to manage
     * products, persons, sales, and accessories.
     *
     * @param productService service used to manage products
     * @param personService service used to manage customers and sellers
     * @param saleService service used to manage sales
     * @param accessoryService service used to manage accessories
     */
    public Menu(ProductService productService, PersonService personService,
            SaleService saleService, AccessoryService accessoryService) {
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.accessoryService = accessoryService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the main application menu.
     */
    public void showMainMenu() {
        int option;

        do {
            System.out.println("========== GAMEZONE ==========");
            System.out.println(" ");
            System.out.println("1. Product Management");
            System.out.println("2. Person Management");
            System.out.println("3. Sales Management");
            System.out.println("4. Accessory Management");
            System.out.println("0. Exit");
            System.out.println(" ");
            System.out.print("Select an option: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    showProductMenu();
                    break;
                case 2:
                    showPersonMenu();
                    break;
                case 3:
                    showSalesMenu();
                    break;
                case 4:
                    showAccessoryMenu();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (option != 0);
    }

    /**
     * Displays the product management menu.
     */
    private void showProductMenu() {
        int option;

        do {
            System.out.println("\n===== PRODUCT MANAGEMENT =====");
            System.out.println("1. Register Videogame");
            System.out.println("2. Register Console");
            System.out.println("3. List Products");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    registerVideogame();
                    break;
                case 2:
                    registerConsole();
                    break;
                case 3:
                    listProducts();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
    }

    /**
     * Reads videogame data from the user and registers a new videogame.
     */
    private void registerVideogame() {
        System.out.println("======= Register Videogame =======");
        System.out.println("Enter product ID");
        String id = scanner.nextLine();

        System.out.println("Enter product name");
        String name = scanner.nextLine();

        System.out.println("Enter product price");
        double price = scanner.nextDouble();

        System.out.println("Enter the amount of stock of the product");
        int stock = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter the genre of the videogame");
        String genre = scanner.nextLine();

        System.out.println("Enter the platform of the videogame");
        String platform = scanner.nextLine();

        productService.registerVideoGame(id, name, price, stock, genre, platform);

        System.out.println("Videogame registered successfully");
    }

    /**
     * Reads console data from the user and registers a new console.
     */
    private void registerConsole() {
        System.out.println("======= Register Console =======");
        System.out.println("Enter product ID");
        String id = scanner.nextLine();

        System.out.println("Enter product name");
        String name = scanner.nextLine();

        System.out.println("Enter product price");
        double price = scanner.nextDouble();

        System.out.println("Enter the amount of stock of the product");
        int stock = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter the brand of the console");
        String brand = scanner.nextLine();

        System.out.println("Enter the model of the console");
        String model = scanner.nextLine();

        productService.registerConsole(id, name, price, stock, brand, model);

        System.out.println("Console registered successfully");
    }

    /**
     * Displays all products currently registered in the inventory.
     */
    private void listProducts() {
        System.out.println("\n======= PRODUCT LIST =======");

        List<Product> products = productService.listProducts();

        if (products.isEmpty()) {
            System.out.println("No products registered.");
            return;
        }

        for (Product product : products) {
            System.out.println(
                    "ID: " + product.getId()
                    + " | Name: " + product.getName()
                    + " | Price: " + product.getPrice()
                    + " | Stock: " + product.getStock()
                    + " | Type: " + product.getProductType()
            );
        }
    }

    /**
     * Displays the accessory management menu.
     */
    private void showAccessoryMenu() {
        int option;

        do {
            System.out.println("\n===== ACCESSORY MANAGEMENT =====");
            System.out.println("1. Register Controller");
            System.out.println("2. Register Cable");
            System.out.println("3. Register Memory");
            System.out.println("4. List All Accessories");
            System.out.println("5. List Accessories by Type");
            System.out.println("6. Find Accessories Compatible with a Console");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    registerController();
                    break;
                case 2:
                    registerCable();
                    break;
                case 3:
                    registerMemory();
                    break;
                case 4:
                    listAllAccessories();
                    break;
                case 5:
                    listAccessoriesByType();
                    break;
                case 6:
                    findAccessoriesCompatibleWith();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
    }

    /**
     * Reads a list of compatible console IDs from the user.
     * Enter 0 when there are no more IDs to add.
     *
     * @return list of compatible console IDs
     */
    private List<String> readCompatibleConsoles() {
        List<String> compatibleConsoles = new ArrayList<>();

        System.out.println("Enter compatible console IDs one at a time.");
        System.out.println("Enter 0 when finished.");

        String consoleId;

        do {
            System.out.print("Compatible console ID: ");
            consoleId = scanner.nextLine();

            if (!consoleId.equals("0")) {
                if (!compatibleConsoles.contains(consoleId)) {
                    compatibleConsoles.add(consoleId);
                } else {
                    System.out.println("Console ID already entered.");
                }
            }
        } while (!consoleId.equals("0"));

        return compatibleConsoles;
    }

    /**
     * Reads controller information and registers a new controller.
     */
    private void registerController() {
        System.out.println("\n======= REGISTER CONTROLLER =======");

        System.out.print("Enter accessory ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter accessory name: ");
        String name = scanner.nextLine();

        System.out.print("Enter accessory price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter accessory stock: ");
        int stock = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter connection type: ");
        String connectionType = scanner.nextLine();

        List<String> compatibleConsoles = readCompatibleConsoles();

        accessoryService.registerController(
                id, name, price, stock, compatibleConsoles, connectionType
        );

        System.out.println("Controller registration completed.");
    }

    /**
     * Reads cable information and registers a new cable.
     */
    private void registerCable() {
        System.out.println("\n======= REGISTER CABLE =======");

        System.out.print("Enter accessory ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter accessory name: ");
        String name = scanner.nextLine();

        System.out.print("Enter accessory price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter accessory stock: ");
        int stock = scanner.nextInt();

        System.out.print("Enter cable length in meters: ");
        double lengthMeters = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter connector type: ");
        String connectorType = scanner.nextLine();

        List<String> compatibleConsoles = readCompatibleConsoles();

        accessoryService.registerCable(
                id, name, price, stock, compatibleConsoles,
                lengthMeters, connectorType
        );

        System.out.println("Cable registration completed.");
    }

    /**
     * Reads memory information and registers a new memory accessory.
     */
    private void registerMemory() {
        System.out.println("\n======= REGISTER MEMORY =======");

        System.out.print("Enter accessory ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter accessory name: ");
        String name = scanner.nextLine();

        System.out.print("Enter accessory price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter accessory stock: ");
        int stock = scanner.nextInt();

        System.out.print("Enter memory capacity in GB: ");
        int capacityGB = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter memory type: ");
        String memoryType = scanner.nextLine();

        List<String> compatibleConsoles = readCompatibleConsoles();

        accessoryService.registerMemory(
                id, name, price, stock, compatibleConsoles,
                capacityGB, memoryType
        );

        System.out.println("Memory registration completed.");
    }

    /**
     * Displays all registered accessories.
     */
    private void listAllAccessories() {
        System.out.println("\n======= ACCESSORY LIST =======");

        List<Accessory> accessories = accessoryService.listAllAccessories();

        displayAccessories(accessories);
    }

    /**
     * Asks for an accessory type and displays matching accessories.
     */
    private void listAccessoriesByType() {
        System.out.println("\n======= ACCESSORIES BY TYPE =======");
        System.out.println("1. Controllers");
        System.out.println("2. Cables");
        System.out.println("3. Memory");
        System.out.print("Select an accessory type: ");

        int option = scanner.nextInt();
        scanner.nextLine();

        String type;

        switch (option) {
            case 1:
                type = "CONTROLLER";
                break;
            case 2:
                type = "CABLE";
                break;
            case 3:
                type = "MEMORY";
                break;
            default:
                System.out.println("Invalid accessory type.");
                return;
        }

        List<Accessory> accessories =
                accessoryService.listAccessoriesByType(type);

        displayAccessories(accessories);
    }

    /**
     * Searches for accessories compatible with a console ID.
     */
    private void findAccessoriesCompatibleWith() {
        System.out.println("\n======= FIND COMPATIBLE ACCESSORIES =======");
        System.out.print("Enter console ID: ");
        String consoleId = scanner.nextLine();

        List<Accessory> accessories =
                accessoryService.findAccessoriesCompatibleWith(consoleId);

        displayAccessories(accessories);
    }

    /**
     * Displays a list of accessories, or a message if the list is empty.
     *
     * @param accessories accessories to display
     */
    private void displayAccessories(List<Accessory> accessories) {
        if (accessories == null || accessories.isEmpty()) {
            System.out.println("No accessories found.");
            return;
        }

        for (Accessory accessory : accessories) {
            System.out.println(
                    "ID: " + accessory.getId()
                    + " | Name: " + accessory.getName()
                    + " | Price: " + accessory.getPrice()
                    + " | Stock: " + accessory.getStock()
                    + " | Type: " + accessory.getProductType()
                    + " | Description: " + accessory.getDescription()
            );
        }
    }

    /**
     * Displays the person management menu.
     */
    private void showPersonMenu() {
        int option;

        do {
            System.out.println("\n===== PERSON MANAGEMENT =====");
            System.out.println("1. Register Customer");
            System.out.println("2. List Customers");
            System.out.println("3. List Sellers");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    listCustomers();
                    break;
                case 3:
                    listSellers();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
    }

    /**
     * Reads customer data from the user and registers a new customer.
     */
    private void registerCustomer() {
        System.out.println("\n======= REGISTER CUSTOMER =======");

        System.out.println("Enter customer name");
        String name = scanner.nextLine();

        System.out.println("Enter customer ID");
        String id = scanner.nextLine();

        System.out.println("Enter cellphone");
        String cellphone = scanner.nextLine();

        System.out.println("Enter email");
        String email = scanner.nextLine();

        personService.registerCustomer(name, id, cellphone, email);

        System.out.println("Customer registered successfully");
    }

    /**
     * Displays all registered customers.
     */
    private void listCustomers() {
        System.out.println("\n======= CUSTOMER LIST =======");

        List<Customer> customers = personService.listCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
            return;
        }

        for (Customer customer : customers) {
            System.out.println(
                    "ID: " + customer.getId()
                    + " | Name: " + customer.getName()
                    + " | Cellphone: " + customer.getCellphone()
                    + " | Email: " + customer.getEmail()
            );
        }
    }

    /**
     * Displays all registered sellers.
     */
    private void listSellers() {
        System.out.println("\n======= SELLER LIST =======");

        List<Seller> sellers = personService.listSellers();

        if (sellers.isEmpty()) {
            System.out.println("No sellers registered.");
            return;
        }

        for (Seller seller : sellers) {
            System.out.println(
                    "ID: " + seller.getId()
                    + " | Name: " + seller.getName()
                    + " | Cellphone: " + seller.getCellphone()
                    + " | Employee Code: " + seller.getEmployeeCode()
            );
        }
    }

    /**
     * Displays the sales management menu.
     */
    private void showSalesMenu() {
        int option;

        do {
            System.out.println("\n===== SALES MANAGEMENT =====");
            System.out.println("1. Register Sale");
            System.out.println("2. List All Sales");
            System.out.println("3. Purchase History by Customer");
            System.out.println("4. Sales History by Seller");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    registerSale();
                    break;
                case 2:
                    listSales();
                    break;
                case 3:
                    showPurchasesByCustomer();
                    break;
                case 4:
                    showSalesBySeller();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
    }

    /**
     * Reads sale information from the user and registers a new sale.
     */
    private void registerSale() {
        System.out.println("\n======= REGISTER SALE =======");

        System.out.println("Enter sale ID");
        String saleId = scanner.nextLine();

        System.out.println("Enter customer ID");
        String customerId = scanner.nextLine();

        System.out.println("Enter seller ID");
        String sellerId = scanner.nextLine();

        List<String> productIds = new ArrayList<>();

        System.out.println("Enter product IDs one at a time.");
        System.out.println("Enter 0 when finished.");

        String productId;

        do {
            System.out.print("Product ID: ");
            productId = scanner.nextLine();

            if (!productId.equals("0") && !productIds.contains(productId)) {
                productIds.add(productId);
            } else if (!productId.equals("0")) {
                System.out.println("Product already selected.");
            }
        } while (!productId.equals("0"));

        saleService.registerSale(
                saleId,
                new Date(),
                customerId,
                sellerId,
                productIds
        );

        System.out.println("Sale operation completed.");
    }

    /**
     * Displays the complete sales history.
     */
    private void listSales() {
        System.out.println("\n======= SALES HISTORY =======");

        List<Sale> sales = saleService.listSales();

        if (sales.isEmpty()) {
            System.out.println("No sales registered.");
            return;
        }

        for (Sale sale : sales) {
            System.out.println(
                    "Sale ID: " + sale.getSaleid()
                    + " | Customer: " + sale.getCustomer().getName()
                    + " | Seller: " + sale.getSeller().getName()
                    + " | Total: " + sale.getTotal()
            );
        }
    }

    /**
     * Displays the purchase history for a specific customer.
     */
    private void showPurchasesByCustomer() {
        System.out.println("\n======= CUSTOMER PURCHASE HISTORY =======");
        System.out.print("Enter customer ID: ");
        String customerId = scanner.nextLine();

        List<Sale> sales = saleService.getPurchasesByCustomer(customerId);

        if (sales.isEmpty()) {
            System.out.println("No purchases found for this customer.");
            return;
        }

        for (Sale sale : sales) {
            System.out.println(
                    "Sale ID: " + sale.getSaleid()
                    + " | Date: " + sale.getDate()
                    + " | Total: " + sale.getTotal()
            );
        }
    }

    /**
     * Displays the sales history for a specific seller.
     */
    private void showSalesBySeller() {
        System.out.println("\n======= SELLER SALES HISTORY =======");
        System.out.print("Enter seller ID: ");
        String sellerId = scanner.nextLine();

        List<Sale> sales = saleService.getSalesBySeller(sellerId);

        if (sales.isEmpty()) {
            System.out.println("No sales found for this seller.");
            return;
        }

        for (Sale sale : sales) {
            System.out.println(
                    "Sale ID: " + sale.getSaleid()
                    + " | Date: " + sale.getDate()
                    + " | Customer: " + sale.getCustomer().getName()
                    + " | Total: " + sale.getTotal()
            );
        }
    }
}