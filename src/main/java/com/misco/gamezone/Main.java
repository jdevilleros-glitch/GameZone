/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.misco.gamezone;

import com.misco.gamezone.dao.PersonDAO;
import com.misco.gamezone.dao.ProductDAO;
import com.misco.gamezone.dao.SaleDAO;
import com.misco.gamezone.persistence.AccessoryRepository;
import com.misco.gamezone.service.AccessoryService;
import com.misco.gamezone.service.PersonService;
import com.misco.gamezone.service.ProductService;
import com.misco.gamezone.service.SaleService;
import com.misco.gamezone.ui.Menu;

/**
 * Starts the GameZone application and initializes its services.
 *
 * @author USUARIO
 */
public class Main {

    public static void main(String[] args) {
        // DAOs
        ProductDAO productDAO = new ProductDAO("data/products.txt");
        PersonDAO personDAO = new PersonDAO("data/persons.txt");

        SaleDAO saleDAO = new SaleDAO(
                "data/sales.txt",
                productDAO,
                personDAO
        );

        // Repositories
        AccessoryRepository accessoryRepository = new AccessoryRepository();

        // Services
        ProductService productService = new ProductService(productDAO);
        PersonService personService = new PersonService(personDAO);
        AccessoryService accessoryService
                = new AccessoryService(accessoryRepository);

        SaleService saleService = new SaleService(
                saleDAO,
                personDAO,
                productDAO
        );

        // User Interface
        Menu menu = new Menu(
                productService,
                personService,
                saleService,
                accessoryService
        );

        menu.showMainMenu();
    }
}

