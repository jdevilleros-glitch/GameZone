/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.ui;

import com.misco.gamezone.model.Accessory;
import com.misco.gamezone.model.Console;
import com.misco.gamezone.model.Customer;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Promotion;
import com.misco.gamezone.model.Return;
import com.misco.gamezone.model.Sale;
import com.misco.gamezone.model.Seller;
import com.misco.gamezone.model.Warranty;
import com.misco.gamezone.service.AccessoryService;
import com.misco.gamezone.service.PersonService;
import com.misco.gamezone.service.ProductService;
import com.misco.gamezone.service.PromotionService;
import com.misco.gamezone.service.ReturnService;
import com.misco.gamezone.service.SaleService;
import com.misco.gamezone.service.WarrantyService;
import java.time.LocalDate;
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
    private PromotionService promotionService;
    private ReturnService returnService;
    private WarrantyService warrantyService;
    private Scanner scanner;

    /**
     * Creates the application menu using the required services.
     *
     * @param productService service used to manage products
     * @param personService service used to manage customers and sellers
     * @param saleService service used to manage sales
     * @param accessoryService service used to manage accessories
     * @param promotionService service used to manage promotions
     * @param returnService service used to manage returns
     */
    public Menu(
            ProductService productService,
            PersonService personService,
            SaleService saleService,
            AccessoryService accessoryService,
            PromotionService promotionService,
            ReturnService returnService,
            WarrantyService warrantyService) {

        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
        this.returnService = returnService;
        this.warrantyService = warrantyService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the main application menu.
     */
    public void showMainMenu() {
        int option;

        do {
            System.out.println("1. Product Management");
            System.out.println("2. Person Management");
            System.out.println("3. Sales Management");
            System.out.println("4. Accessory Management");
            System.out.println("5. Promotion Management");
            System.out.println("6. Returns Management");
            System.out.println("7. Warranty Management");
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
                case 5:
                    showPromotionMenu();
                    break;
                case 6:
                    showReturnMenu();
                    break;
                case 7:
                    showWarrantyMenu();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
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
     * Reads a list of compatible console IDs from the user. Enter 0 when there
     * are no more IDs to add.
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

        List<Accessory> accessories
                = accessoryService.listAccessoriesByType(type);

        displayAccessories(accessories);
    }

    /**
     * Searches for accessories compatible with a console ID.
     */
    private void findAccessoriesCompatibleWith() {
        System.out.println("\n======= FIND COMPATIBLE ACCESSORIES =======");
        System.out.print("Enter console ID: ");
        String consoleId = scanner.nextLine();

        List<Accessory> accessories
                = accessoryService.findAccessoriesCompatibleWith(consoleId);

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
            System.out.println("5. Show Sale Details");
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
                case 5:
                    showSaleDetails();
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
    /**
     * Reads sale information and allows the user to select extended warranties
     * for consoles included in the sale.
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

        System.out.println("Enter product or accessory IDs one at a time.");
        System.out.println("Enter 0 when finished.");

        String productId;

        do {
            System.out.print("Item ID: ");
            productId = scanner.nextLine();

            if (!productId.equals("0")
                    && !productIds.contains(productId)) {

                productIds.add(productId);

            } else if (!productId.equals("0")) {
                System.out.println("Item already selected.");
            }

        } while (!productId.equals("0"));

        List<String> extendedWarrantyProductIds = new ArrayList<>();

        List<Product> products = productService.listProducts();

        for (String itemId : productIds) {

            Product selectedProduct = null;

            for (Product product : products) {
                if (product.getId().equalsIgnoreCase(itemId)) {
                    selectedProduct = product;
                    break;
                }
            }

            if (selectedProduct instanceof Console) {

                System.out.print(
                        "Add extended warranty to "
                        + selectedProduct.getName()
                        + " (10% additional cost)? (Y/N): "
                );

                String answer = scanner.nextLine();

                if (answer.equalsIgnoreCase("Y")) {
                    extendedWarrantyProductIds.add(
                            selectedProduct.getId()
                    );
                }
            }
        }

        boolean registered = saleService.registerSale(
                saleId,
                new Date(),
                customerId,
                sellerId,
                productIds,
                extendedWarrantyProductIds
        );

        if (registered) {
            System.out.println("Sale registered successfully.");
        } else {
            System.out.println(
                    "Sale could not be registered. Check the entered data."
            );
        }
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
                    "Sale ID: " + sale.getSaleId()
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

        List<Sale> sales = saleService.getCustomerPurchaseHistory(customerId);

        if (sales.isEmpty()) {
            System.out.println("No purchases found for this customer.");
            return;
        }

        for (Sale sale : sales) {
            System.out.println(
                    "Sale ID: " + sale.getSaleId()
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

        List<Sale> sales = saleService.getSellerSales(sellerId);

        if (sales.isEmpty()) {
            System.out.println("No sales found for this seller.");
            return;
        }

        for (Sale sale : sales) {
            System.out.println(
                    "Sale ID: " + sale.getSaleId()
                    + " | Date: " + sale.getDate()
                    + " | Customer: " + sale.getCustomer().getName()
                    + " | Total: " + sale.getTotal()
            );
        }
    }

    private void showSaleDetails() {

        System.out.print("Enter sale ID: ");
        String saleId = scanner.nextLine();

        Sale sale = saleService.findSaleById(saleId);

        if (sale == null) {
            System.out.println("Sale not found.");
            return;
        }

        System.out.println("\n======= SALE DETAILS =======");
        System.out.println(sale.generateReceipt());
    }

    /**
     * Displays the promotion management menu.
     */
    private void showPromotionMenu() {

        int option;

        do {
            System.out.println("\n===== PROMOTION MANAGEMENT =====");
            System.out.println("1. Register Percentage Discount");
            System.out.println("2. Register Category Discount");
            System.out.println("3. Register Bulk Purchase Discount");
            System.out.println("4. List All Promotions");
            System.out.println("5. List Active Promotions");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    registerPercentageDiscount();
                    break;
                case 2:
                    registerCategoryDiscount();
                    break;
                case 3:
                    registerBulkPurchaseDiscount();
                    break;
                case 4:
                    listAllPromotions();
                    break;
                case 5:
                    listActivePromotions();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        } while (option != 0);
    }

    /**
     * Reads and registers a percentage discount promotion.
     */
    private void registerPercentageDiscount() {

        System.out.println("\n===== REGISTER PERCENTAGE DISCOUNT =====");

        System.out.print("Enter promotion ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter promotion name: ");
        String name = scanner.nextLine();

        System.out.print("Enter start date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter end date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter discount percentage: ");
        double percentage = scanner.nextDouble();
        scanner.nextLine();

        promotionService.registerPercentageDiscount(
                id, name, startDate, endDate, percentage
        );

        System.out.println("Promotion registered successfully.");
    }

    /**
     * Reads and registers a category discount promotion.
     */
    private void registerCategoryDiscount() {

        System.out.println("\n===== REGISTER CATEGORY DISCOUNT =====");

        System.out.print("Enter promotion ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter promotion name: ");
        String name = scanner.nextLine();

        System.out.print("Enter start date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter end date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter discount percentage: ");
        double percentage = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Select category:");
        System.out.println("1. VIDEOGAME");
        System.out.println("2. CONSOLE");
        System.out.println("3. ACCESSORY");
        System.out.print("Option: ");

        int categoryOption = scanner.nextInt();
        scanner.nextLine();

        String targetCategory;

        switch (categoryOption) {
            case 1:
                targetCategory = "VIDEOGAME";
                break;
            case 2:
                targetCategory = "CONSOLE";
                break;
            case 3:
                targetCategory = "ACCESSORY";
                break;
            default:
                System.out.println("Invalid category.");
                return;
        }

        promotionService.registerCategoryDiscount(
                id,
                name,
                startDate,
                endDate,
                percentage,
                targetCategory
        );

        System.out.println("Promotion registered successfully.");
    }

    /**
     * Reads and registers a bulk purchase discount promotion.
     */
    private void registerBulkPurchaseDiscount() {

        System.out.println("\n===== REGISTER BULK PURCHASE DISCOUNT =====");

        System.out.print("Enter promotion ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter promotion name: ");
        String name = scanner.nextLine();

        System.out.print("Enter start date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter end date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter minimum quantity: ");
        int minimumQuantity = scanner.nextInt();

        System.out.print("Enter discount percentage: ");
        double percentage = scanner.nextDouble();
        scanner.nextLine();

        promotionService.registerBulkPurchaseDiscount(
                id,
                name,
                startDate,
                endDate,
                minimumQuantity,
                percentage
        );

        System.out.println("Promotion registered successfully.");
    }

    /**
     * Displays all registered promotions.
     */
    private void listAllPromotions() {

        System.out.println("\n===== ALL PROMOTIONS =====");

        List<Promotion> promotions = promotionService.listAllPromotions();

        if (promotions.isEmpty()) {
            System.out.println("No promotions registered.");
            return;
        }

        for (Promotion promotion : promotions) {
            System.out.println(
                    "ID: " + promotion.getId()
                    + " | Name: " + promotion.getName()
                    + " | Start Date: " + promotion.getStartDate()
                    + " | End Date: " + promotion.getEndDate()
            );
        }
    }

    /**
     * Displays all currently active promotions.
     */
    private void listActivePromotions() {

        System.out.println("\n===== ACTIVE PROMOTIONS =====");

        List<Promotion> promotions = promotionService.listActivePromotions();

        if (promotions.isEmpty()) {
            System.out.println("No active promotions.");
            return;
        }

        for (Promotion promotion : promotions) {
            System.out.println(
                    "ID: " + promotion.getId()
                    + " | Name: " + promotion.getName()
                    + " | Start Date: " + promotion.getStartDate()
                    + " | End Date: " + promotion.getEndDate()
            );
        }
    }

    /**
     * Displays the return management menu.
     */
    private void showReturnMenu() {

        int option;

        do {
            System.out.println("\n===== RETURNS MANAGEMENT =====");
            System.out.println("1. Register Return");
            System.out.println("2. List All Returns");
            System.out.println("3. Returns by Customer");
            System.out.println("4. Returns by Sale");
            System.out.println("5. Monthly Balance");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    registerReturn();
                    break;
                case 2:
                    listAllReturns();
                    break;
                case 3:
                    showReturnsByCustomer();
                    break;
                case 4:
                    showReturnsBySale();
                    break;
                case 5:
                    showMonthlyBalance();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        } while (option != 0);
    }

    /**
     * Reads return information and registers a new product return.
     */
    private void registerReturn() {

        System.out.println("\n===== REGISTER RETURN =====");

        System.out.print("Enter sale ID: ");
        String saleId = scanner.nextLine();

        List<String> productIds = new ArrayList<>();

        System.out.println("Enter product IDs to return one at a time.");
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

        System.out.print("Enter return reason: ");
        String reason = scanner.nextLine();

        try {
            Return returnRecord = returnService.registerReturn(
                    saleId,
                    productIds,
                    reason
            );

            System.out.println("\nReturn registered successfully.");
            System.out.println(returnRecord.generateReturnReceipt());

        } catch (IllegalArgumentException e) {
            System.out.println("Return could not be registered: "
                    + e.getMessage());
        }
    }

    /**
     * Displays all registered returns.
     */
    private void listAllReturns() {

        System.out.println("\n===== RETURN HISTORY =====");

        List<Return> returns = returnService.viewAllReturns();

        if (returns.isEmpty()) {
            System.out.println("No returns registered.");
            return;
        }

        for (Return returnRecord : returns) {
            System.out.println(returnRecord.generateReturnReceipt());
            System.out.println("------------------------------");
        }
    }

    /**
     * Displays returns associated with a specific customer.
     */
    private void showReturnsByCustomer() {

        System.out.println("\n===== RETURNS BY CUSTOMER =====");

        System.out.print("Enter customer ID: ");
        String customerId = scanner.nextLine();

        List<Return> returns
                = returnService.viewReturnsByCustomer(customerId);

        if (returns.isEmpty()) {
            System.out.println("No returns found for this customer.");
            return;
        }

        for (Return returnRecord : returns) {
            System.out.println(returnRecord.generateReturnReceipt());
            System.out.println("------------------------------");
        }
    }

    /**
     * Displays returns associated with a specific sale.
     */
    private void showReturnsBySale() {

        System.out.println("\n===== RETURNS BY SALE =====");

        System.out.print("Enter sale ID: ");
        String saleId = scanner.nextLine();

        List<Return> returns
                = returnService.viewReturnsBySale(saleId);

        if (returns.isEmpty()) {
            System.out.println("No returns found for this sale.");
            return;
        }

        for (Return returnRecord : returns) {
            System.out.println(returnRecord.generateReturnReceipt());
            System.out.println("------------------------------");
        }
    }

    /**
     * Displays the balance for a selected month and year.
     */
    private void showMonthlyBalance() {

        System.out.println("\n===== MONTHLY BALANCE =====");

        System.out.print("Enter month (1-12): ");
        int month = scanner.nextInt();

        System.out.print("Enter year: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        if (month < 1 || month > 12) {
            System.out.println("Invalid month.");
            return;
        }

        double balance = returnService.generateMonthlyBalance(month, year);

        System.out.println(
                "Balance for " + month + "/" + year + ": $" + balance
        );
    }

    /**
     * Displays the warranty management menu.
     */
    private void showWarrantyMenu() {

        int option;

        do {
            System.out.println("\n===== WARRANTY MANAGEMENT =====");
            System.out.println("1. Find Warranty by Product and Sale");
            System.out.println("2. List All Warranties");
            System.out.println("3. List Active Warranties");
            System.out.println("4. List Warranties Expiring Soon");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    findWarrantyByProduct();
                    break;
                case 2:
                    listAllWarranties();
                    break;
                case 3:
                    listActiveWarranties();
                    break;
                case 4:
                    listWarrantiesExpiringSoon();
                    break;
                case 0:
                    break;
                default:
                    System.out.println(
                            "Invalid option. Please try again."
                    );
            }

        } while (option != 0);
    }

    /**
     * Finds a warranty by product and sale.
     */
    private void findWarrantyByProduct() {

        System.out.println("\n===== FIND WARRANTY =====");

        System.out.print("Enter product ID: ");
        String productId = scanner.nextLine();

        System.out.print("Enter sale ID: ");
        String saleId = scanner.nextLine();

        Warranty warranty
                = warrantyService.findWarrantyByProduct(
                        productId,
                        saleId
                );

        if (warranty == null) {
            System.out.println("Warranty not found.");
            return;
        }

        System.out.println(
                warranty.generateWarrantyCertificate()
        );
    }

    /**
     * Displays all registered warranties.
     */
    private void listAllWarranties() {

        System.out.println("\n===== ALL WARRANTIES =====");

        List<Warranty> warranties
                = warrantyService.listAllWarranties();

        displayWarranties(warranties);
    }

    /**
     * Displays all currently active warranties.
     */
    private void listActiveWarranties() {

        System.out.println("\n===== ACTIVE WARRANTIES =====");

        List<Warranty> warranties
                = warrantyService.listActiveWarranties();

        displayWarranties(warranties);
    }

    /**
     * Displays warranties that expire within a specified period.
     */
    private void listWarrantiesExpiringSoon() {

        System.out.println(
                "\n===== WARRANTIES EXPIRING SOON ====="
        );

        System.out.print("Enter number of days ahead: ");
        int daysAhead = scanner.nextInt();
        scanner.nextLine();

        try {
            List<Warranty> warranties
                    = warrantyService.listWarrantiesExpiringSoon(
                            daysAhead
                    );

            displayWarranties(warranties);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays warranty information.
     *
     * @param warranties warranties to display
     */
    private void displayWarranties(
            List<Warranty> warranties) {

        if (warranties == null || warranties.isEmpty()) {
            System.out.println("No warranties found.");
            return;
        }

        for (Warranty warranty : warranties) {

            System.out.println(
                    warranty.generateWarrantyCertificate()
            );

            System.out.println("------------------------------");
        }
    }
}
