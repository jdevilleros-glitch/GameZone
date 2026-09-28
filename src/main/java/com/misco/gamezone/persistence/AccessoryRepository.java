/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.persistence;

import com.misco.gamezone.model.Accessory;
import com.misco.gamezone.model.Controller;
import com.misco.gamezone.model.Cable;
import com.misco.gamezone.model.Memory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Acer
 */

/**
 * Saves and loads accessories from a CSV file.
 */
public class AccessoryRepository {

    private final Path filePath = Path.of("data", "accessories.csv");

    /**
     * Saves all accessories to the CSV file.
     */
    public void saveAll(List<Accessory> accessories) {
        try {
            Files.createDirectories(filePath.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(
                    filePath, StandardCharsets.UTF_8)) {

                for (Accessory accessory : accessories) {
                    String compatible = String.join(
                            ",", accessory.getCompatibleConsoles());

                    String row = accessory.getProductType()
                            + ";" + accessory.getId()
                            + ";" + accessory.getName()
                            + ";" + accessory.getPrice()
                            + ";" + accessory.getStock()
                            + ";" + compatible;

                    if (accessory instanceof Controller) {
                        Controller controller = (Controller) accessory;
                        row += ";" + controller.getConnectionType();

                    } else if (accessory instanceof Cable) {
                        Cable cable = (Cable) accessory;
                        row += ";" + cable.getLengthMeters()
                                + ";" + cable.getConnectorType();

                    } else if (accessory instanceof Memory) {
                        Memory memory = (Memory) accessory;
                        row += ";" + memory.getCapacityGB()
                                + ";" + memory.getMemoryType();
                    }

                    writer.write(row);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error saving accessories: " + e.getMessage());
        }
    }

    /**
     * Loads all accessories from the CSV file.
     * Returns an empty list if the file does not exist.
     */
    public List<Accessory> loadAll() {
        List<Accessory> accessories = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return accessories;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {

            String row;

            while ((row = reader.readLine()) != null) {
                String[] fields = row.split(";", -1);

                try {
                    String type = fields[0];
                    String id = fields[1];
                    String name = fields[2];
                    double price = Double.parseDouble(fields[3]);
                    int stock = Integer.parseInt(fields[4]);

                    List<String> compatibleConsoles = new ArrayList<>();

                    if (!fields[5].isBlank()) {
                        compatibleConsoles = Arrays.asList(
                                fields[5].split(","));
                    }

                    switch (type) {
                        case "CONTROLLER":
                            accessories.add(new Controller(
                                    id, name, price, stock,
                                    compatibleConsoles, fields[6]));
                            break;

                        case "CABLE":
                            double lengthMeters =
                                    Double.parseDouble(fields[6]);
                            accessories.add(new Cable(
                                    id, name, price, stock,
                                    compatibleConsoles, lengthMeters,
                                    fields[7]));
                            break;

                        case "MEMORY":
                            int capacityGB =
                                    Integer.parseInt(fields[6]);
                            accessories.add(new Memory(
                                    id, name, price, stock,
                                    compatibleConsoles, capacityGB,
                                    fields[7]));
                            break;

                        default:
                            System.err.println(
                                    "Unknown accessory type: " + type);
                    }

                } catch (RuntimeException e) {
                    System.err.println(
                            "Invalid accessory row: " + row);
                }
            }

        } catch (IOException e) {
            System.err.println("Error loading accessories: " + e.getMessage());
        }

        return accessories;
    }
}

