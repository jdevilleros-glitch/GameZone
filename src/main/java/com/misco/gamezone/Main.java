/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.misco.gamezone;

import com.misco.gamezone.dao.PersonDAO;
import com.misco.gamezone.dao.ProductDAO;
import com.misco.gamezone.dao.SaleDAO;
import com.misco.gamezone.service.PersonService;
import com.misco.gamezone.service.ProductService;
import com.misco.gamezone.service.SaleService;
import com.misco.gamezone.ui.Menu;

/**
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

        // Services
        ProductService productService = new ProductService(productDAO);
        PersonService personService = new PersonService(personDAO);

        SaleService saleService = new SaleService(
                saleDAO,
                personDAO,
                productDAO
        );

        // User Interface
        Menu menu = new Menu(
                productService,
                personService,
                saleService
        );

        menu.showMainMenu();
    }
}
