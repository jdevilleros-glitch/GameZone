/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.ui;

import com.misco.gamezone.model.Customer;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Promotion;
import com.misco.gamezone.model.Sale;
import com.misco.gamezone.model.Seller;
import com.misco.gamezone.service.PersonService;
import com.misco.gamezone.service.ProductService;
import com.misco.gamezone.service.PromotionService;
import com.misco.gamezone.service.SaleService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Provides the console-based user interface for GameZone. It allows users to
 * manage products, customers, sellers, and sales through the application
 * services.
 *
 * @author USUARIO
 */
public class Menu {

    private ProductService productService;
    private PersonService personService;
    private SaleService saleService;
    private PromotionService promotionService;
    private Scanner scanner;

    /**
     * Creates the application menu using the required services.
     *
     * @param productService service used to manage products
     * @param personService service used to manage customers and sellers
     * @param saleService service used to manage sales
     * @param promotionService service used to manage promotions
     */
    public Menu(
            ProductService productService,
            PersonService personService,
            SaleService saleService,
            PromotionService promotionService) {

        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.promotionService = promotionService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the main application menu and allows the user to access product,
     * person, and sales management.
     */
    public void showMainMenu() {
        int option;

        do {
            System.out.println("========== GAMEZONE ==========");
            System.out.println(" ");
            System.out.println("1. Product Management");
            System.out.println("2. Person Management");
            System.out.println("3. Sales Management");
            System.out.println("4. Promotion Management");
            System.out.println("0. Exit");
            System.out.println(" ");
            System.out.println("Select an option: ");
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
                    showPromotionMenu();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid opption. Try again.");
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
            System.out.println("5. View Sale Details");
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

        boolean registered = saleService.registerSale(
                saleId,
                new Date(),
                customerId,
                sellerId,
                productIds
        );
        if (registered) {
            System.out.println("Sale registered successfully.");
        } else {
            System.out.println("Sale could not be registered. Check the entered data.");
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

        List<Sale> sales = saleService.getPurchasesByCustomer(customerId);

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

        List<Sale> sales = saleService.getSalesBySeller(sellerId);

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

        System.out.print("Enter category (VIDEOGAME or CONSOLE): ");
        String targetCategory = scanner.nextLine();

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
}
