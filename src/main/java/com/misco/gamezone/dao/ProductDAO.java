package com.misco.gamezone.dao;

import com.misco.gamezone.exceptions.PersistenceException;
import com.misco.gamezone.model.Console;
import com.misco.gamezone.model.Product;
import com.misco.gamezone.model.Videogame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides file persistence operations for products.
 *
 * Loads and saves videogame and console information using
 * a text file with semicolon-separated values.
 *
 * Converts file operation failures into PersistenceException.
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
     * Returns an empty list when the file does not exist.
     * Throws a persistence exception if reading fails.
     *
     * @return a list containing the stored products
     * @throws PersistenceException if the file cannot be read
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

                String[] data = line.split(";", -1);

                if (data.length != 7) {
                    continue;
                }

                double price;
                int stock;

                try {
                    price = Double.parseDouble(data[3]);
                    stock = Integer.parseInt(data[4]);

                } catch (NumberFormatException e) {
                    System.out.println(
                            "Invalid number format in products file."
                    );
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
                    System.out.println(
                            "Invalid product type in products file."
                    );
                }
            }

        } catch (IOException e) {
            throw new PersistenceException(
                    "Error reading products from file: " + filePath,
                    e
            );
        }

        return products;
    }

    /**
     * Saves the specified products to the products file.
     *
     * Creates the parent directory when necessary.
     * Throws a persistence exception if writing fails.
     *
     * @param products list of products to save
     * @throws PersistenceException if the file cannot be written
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
            throw new PersistenceException(
                    "Error saving products to file: " + filePath,
                    e
            );
        }
    }
}