package com.misco.gamezone;

import com.misco.gamezone.dao.PersonDAO;
import com.misco.gamezone.dao.ProductDAO;
import com.misco.gamezone.dao.PromotionDAO;
import com.misco.gamezone.dao.ReturnDAO;
import com.misco.gamezone.dao.SaleDAO;
import com.misco.gamezone.dao.WarrantyDAO;
import com.misco.gamezone.persistence.AccessoryRepository;
import com.misco.gamezone.service.AccessoryService;
import com.misco.gamezone.service.PersonService;
import com.misco.gamezone.service.ProductService;
import com.misco.gamezone.service.PromotionService;
import com.misco.gamezone.service.ReturnService;
import com.misco.gamezone.service.SaleService;
import com.misco.gamezone.service.WarrantyService;
import com.misco.gamezone.ui.Menu;

/**
 * Starts the GameZone application and initializes the required persistence,
 * service, and user interface components.
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

        ProductDAO productDAO
                = new ProductDAO("data/products.txt");

        PersonDAO personDAO
                = new PersonDAO("data/persons.txt");

        PromotionDAO promotionDAO
                = new PromotionDAO("data/promotions.csv");

        AccessoryRepository accessoryRepository
                = new AccessoryRepository();

        ProductService productService
                = new ProductService(productDAO);

        PersonService personService
                = new PersonService(personDAO);

        AccessoryService accessoryService
                = new AccessoryService(accessoryRepository);

        SaleDAO saleDAO = new SaleDAO(
                "data/sales.txt",
                productDAO,
                personDAO,
                accessoryRepository
        );

        WarrantyDAO warrantyDAO
                = new WarrantyDAO("data/warranties.csv");

        WarrantyService warrantyService
                = new WarrantyService(
                        warrantyDAO,
                        saleDAO,
                        productService
                );

        ReturnDAO returnDAO = new ReturnDAO(
                "data/returns.csv",
                saleDAO,
                productDAO,
                accessoryRepository
        );

        PromotionService promotionService
                = new PromotionService(promotionDAO);

        SaleService saleService = new SaleService(
                saleDAO,
                personDAO,
                productDAO,
                accessoryService,
                promotionService,
                warrantyService
        );

        ReturnService returnService = new ReturnService(
                returnDAO,
                saleService,
                productService,
                accessoryService,
                warrantyService
        );

        Menu menu = new Menu(
                productService,
                personService,
                saleService,
                accessoryService,
                promotionService,
                returnService,
                warrantyService
        );

        menu.showMainMenu();
    }
}
