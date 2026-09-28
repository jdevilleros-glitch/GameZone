/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.misco.gamezone;

import com.misco.gamezone.dao.PersonDAO;
import com.misco.gamezone.dao.ProductDAO;
import com.misco.gamezone.dao.PromotionDAO;
import com.misco.gamezone.dao.SaleDAO;
import com.misco.gamezone.service.PersonService;
import com.misco.gamezone.service.ProductService;
import com.misco.gamezone.service.PromotionService;
import com.misco.gamezone.service.SaleService;
import com.misco.gamezone.ui.Menu;

/**
 * Starts the GameZone application and initializes the required
 * persistence, service, and user interface components.
 *
 * @author USUARIO
 */
public class Main {
/**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        
        ProductDAO productDAO = new ProductDAO("data/products.txt");
        PersonDAO personDAO = new PersonDAO("data/persons.txt");
        PromotionDAO promotionDAO = new PromotionDAO("data/promotions.csv");

        SaleDAO saleDAO = new SaleDAO(
                "data/sales.txt",
                productDAO,
                personDAO
        );
        
        
        ProductService productService = new ProductService(productDAO);
        PersonService personService = new PersonService(personDAO);
        PromotionService promotionService = new PromotionService(promotionDAO);

        SaleService saleService = new SaleService(
                saleDAO,
                personDAO,
                productDAO,
                promotionService
        );


        Menu menu = new Menu(
                productService,
                personService,
                saleService,
                promotionService
        );

        menu.showMainMenu();
    }
}
