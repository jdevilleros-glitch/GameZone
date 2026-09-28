/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.dao;

import com.misco.gamezone.model.BulkPurchaseDiscount;
import com.misco.gamezone.model.CategoryDiscount;
import com.misco.gamezone.model.PercentageDiscount;
import com.misco.gamezone.model.Promotion;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles file persistence for promotions.
 *
 * @author USUARIO
 */
public class PromotionDAO {

    private final String filePath;

    /**
     * Creates a promotion DAO using the specified file path.
     *
     * @param filePath path of the promotions data file
     */
    public PromotionDAO(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all promotions from the data file.
     *
     * @return list of stored promotions
     */
    public List<Promotion> loadAll() {

        List<Promotion> promotions = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {

            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating promotions file: " + e.getMessage());
            }

            return promotions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(";");

                String type = data[0];
                String id = data[1];
                String name = data[2];
                LocalDate startDate = LocalDate.parse(data[3]);
                LocalDate endDate = LocalDate.parse(data[4]);

                switch (type) {

                    case "PERCENTAGE":
                        promotions.add(new PercentageDiscount(
                                id,
                                name,
                                startDate,
                                endDate,
                                Double.parseDouble(data[5])
                        ));
                        break;

                    case "CATEGORY":
                        promotions.add(new CategoryDiscount(
                                id,
                                name,
                                startDate,
                                endDate,
                                Double.parseDouble(data[5]),
                                data[6]
                        ));
                        break;

                    case "BULK":
                        promotions.add(new BulkPurchaseDiscount(
                                id,
                                name,
                                startDate,
                                endDate,
                                Integer.parseInt(data[5]),
                                Double.parseDouble(data[6])
                        ));
                        break;

                    default:
                        break;
                }
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading promotions: " + e.getMessage());
        }

        return promotions;
    }

    /**
     * Saves all promotions to the data file.
     *
     * @param promotions promotions to save
     */
    public void saveAll(List<Promotion> promotions) {

        File file = new File(filePath);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (Promotion promotion : promotions) {

                if (promotion instanceof PercentageDiscount) {

                    PercentageDiscount p = (PercentageDiscount) promotion;

                    writer.write(
                            "PERCENTAGE;"
                            + p.getId() + ";"
                            + p.getName() + ";"
                            + p.getStartDate() + ";"
                            + p.getEndDate() + ";"
                            + p.getPercentage()
                    );

                } else if (promotion instanceof CategoryDiscount) {

                    CategoryDiscount p = (CategoryDiscount) promotion;

                    writer.write(
                            "CATEGORY;"
                            + p.getId() + ";"
                            + p.getName() + ";"
                            + p.getStartDate() + ";"
                            + p.getEndDate() + ";"
                            + p.getPercentage() + ";"
                            + p.getTargetCategory()
                    );

                } else if (promotion instanceof BulkPurchaseDiscount) {

                    BulkPurchaseDiscount p = (BulkPurchaseDiscount) promotion;

                    writer.write(
                            "BULK;"
                            + p.getId() + ";"
                            + p.getName() + ";"
                            + p.getStartDate() + ";"
                            + p.getEndDate() + ";"
                            + p.getMinimumQuantity() + ";"
                            + p.getPercentage()
                    );
                }

                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving promotions: " + e.getMessage());
        }
    }
}
