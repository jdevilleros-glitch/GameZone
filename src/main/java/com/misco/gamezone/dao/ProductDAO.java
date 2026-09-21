/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.dao;

import com.misco.gamezone.model.Console;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Videogame;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Provides file persistence operations to all products. It loads ad saves
 * product data via file.
 *
 * @author USUARIO
 */
public class ProductDAO {

    private final String filePath;

    /**
     * Creates a product DAO using the specified file path.
     *
     * @param filePath path of the products file
     */
    public ProductDAO(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads the products stored in the products file.
     *
     * @return a list containing the stored products
     */
    public List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();

        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            return products;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length != 7) {
                    continue;
                }
                double price;
                int stock;

                try {
                    price = Double.parseDouble(data[3]);
                    stock = Integer.parseInt(data[4]);
                } catch (NumberFormatException e) {
                    System.out.println("Error with the number format");
                    continue;
                }

                if (data[0].equals("VIDEOGAME")) {
                    Videogame videoGame = new Videogame(
                            data[1],
                            data[2],
                            price,
                            stock,
                            data[5],
                            data[6]
                    );

                    products.add(videoGame);
                } else if (data[0].equals("CONSOLE")) {
                    Console console = new Console(
                            data[1],
                            data[2],
                            price,
                            stock,
                            data[5],
                            data[6]
                    );

                    products.add(console);
                } else {
                    System.out.println("Product type invalid");
                }

            }
        } catch (IOException e) {
            System.out.println("Error reading products file");
        }

        return products;
    }

    /**
     * Saves the specified products to the products file.
     *
     * @param products list of products to save
     */
    public void saveProducts(List<Product> products) {
        Path path = Path.of(filePath);

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {

                for (Product product : products) {

                    if (product instanceof Videogame) {
                        Videogame videoGame = (Videogame) product;
                        String line = "VIDEOGAME" + ";"
                                + videoGame.getId() + ";"
                                + videoGame.getName() + ";"
                                + videoGame.getPrice() + ";"
                                + videoGame.getStock() + ";"
                                + videoGame.getGenre() + ";"
                                + videoGame.getPlatform();

                        writer.write(line);
                        writer.newLine();

                    } else if (product instanceof Console) {
                        Console console = (Console) product;
                        String line = "CONSOLE" + ";"
                                + console.getId() + ";"
                                + console.getName() + ";"
                                + console.getPrice() + ";"
                                + console.getStock() + ";"
                                + console.getBrand() + ";"
                                + console.getModel();

                        writer.write(line);
                        writer.newLine();
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error saving products file");
        }
    }
}
