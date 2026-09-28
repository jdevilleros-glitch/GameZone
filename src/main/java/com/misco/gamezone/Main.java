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

public class Main {

    public static void main(String[] args) {

        ProductDAO productDAO = new ProductDAO("data/products.txt");
        PersonDAO personDAO = new PersonDAO("data/persons.txt");

        AccessoryRepository accessoryRepository =
                new AccessoryRepository();

        ProductService productService =
                new ProductService(productDAO);

        PersonService personService =
                new PersonService(personDAO);

        AccessoryService accessoryService =
                new AccessoryService(accessoryRepository);

        SaleDAO saleDAO = new SaleDAO(
                "data/sales.txt",
                productDAO,
                personDAO,
                accessoryRepository
        );

        SaleService saleService = new SaleService(
                saleDAO,
                personDAO,
                productDAO,
                accessoryService
        );

        Menu menu = new Menu(
                productService,
                personService,
                saleService,
                accessoryService
        );

        menu.showMainMenu();
    }
}