package com.misco.gamezone.dao;

import com.misco.gamezone.model.BulkPurchaseDiscount;
import com.misco.gamezone.model.CategoryDiscount;
import com.misco.gamezone.model.PercentageDiscount;
import com.misco.gamezone.model.Promotion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
            createPromotionsFile(file);
            return promotions;
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                Promotion promotion = parsePromotionRecord(line);

                if (promotion != null) {
                    promotions.add(promotion);
                }
            }

        } catch (IOException | NumberFormatException e) {

            System.out.println(
                    "Error loading promotions: " + e.getMessage()
            );
        }

        return promotions;
    }

    /**
     * Creates the promotions file when it does not exist.
     *
     * Preserves the original directory creation and
     * error reporting behavior.
     *
     * @param file promotions file
     */
    private void createPromotionsFile(File file) {

        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try {

            file.createNewFile();

        } catch (IOException e) {

            System.out.println(
                    "Error creating promotions file: "
                    + e.getMessage()
            );
        }
    }

    /**
     * Converts a stored record into a promotion.
     *
     * @param line stored promotion record
     * @return reconstructed promotion or null
     */
    private Promotion parsePromotionRecord(String line) {

        String[] data = line.split(";");

        String type = data[0];
        String id = data[1];
        String name = data[2];

        LocalDate startDate = LocalDate.parse(data[3]);
        LocalDate endDate = LocalDate.parse(data[4]);

        return createPromotion(
                type,
                id,
                name,
                startDate,
                endDate,
                data
        );
    }

    /**
     * Creates the corresponding promotion subtype
     * using the stored record information.
     *
     * @param type promotion type
     * @param id promotion identifier
     * @param name promotion name
     * @param startDate start date
     * @param endDate end date
     * @param data stored fields
     * @return promotion or null for unsupported types
     */
    private Promotion createPromotion(
            String type,
            String id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            String[] data) {

        switch (type) {

            case "PERCENTAGE":

                return new PercentageDiscount(
                        id,
                        name,
                        startDate,
                        endDate,
                        Double.parseDouble(data[5])
                );

            case "CATEGORY":

                return new CategoryDiscount(
                        id,
                        name,
                        startDate,
                        endDate,
                        Double.parseDouble(data[5]),
                        data[6]
                );

            case "BULK":

                return new BulkPurchaseDiscount(
                        id,
                        name,
                        startDate,
                        endDate,
                        Integer.parseInt(data[5]),
                        Double.parseDouble(data[6])
                );

            default:

                return null;
        }
    }

    /**
     * Saves all promotions to the data file.
     *
     * @param promotions promotions to save
     */
    public void saveAll(List<Promotion> promotions) {

        File file = new File(filePath);

        createParentDirectory(file);

        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(file))) {

            for (Promotion promotion : promotions) {

                String line = formatPromotionRecord(promotion);

                if (line != null) {
                    writer.write(line);
                }

                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println(
                    "Error saving promotions: " + e.getMessage()
            );
        }
    }

    /**
     * Creates the parent directory when necessary.
     *
     * @param file target file
     */
    private void createParentDirectory(File file) {

        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    /**
     * Converts a promotion into its persistence format.
     *
     * Returns null for unsupported promotion types,
     * preserving the original behavior of writing
     * an empty line for those entries.
     *
     * @param promotion promotion to serialize
     * @return formatted record or null
     */
    private String formatPromotionRecord(Promotion promotion) {

        if (promotion instanceof PercentageDiscount) {

            return formatPercentageDiscount(
                    (PercentageDiscount) promotion
            );

        } else if (promotion instanceof CategoryDiscount) {

            return formatCategoryDiscount(
                    (CategoryDiscount) promotion
            );

        } else if (promotion instanceof BulkPurchaseDiscount) {

            return formatBulkPurchaseDiscount(
                    (BulkPurchaseDiscount) promotion
            );
        }

        return null;
    }

    /**
     * Formats a percentage discount promotion.
     *
     * @param promotion percentage promotion
     * @return formatted record
     */
    private String formatPercentageDiscount(
            PercentageDiscount promotion) {

        return "PERCENTAGE;"
                + promotion.getId() + ";"
                + promotion.getName() + ";"
                + promotion.getStartDate() + ";"
                + promotion.getEndDate() + ";"
                + promotion.getPercentage();
    }

    /**
     * Formats a category discount promotion.
     *
     * @param promotion category promotion
     * @return formatted record
     */
    private String formatCategoryDiscount(
            CategoryDiscount promotion) {

        return "CATEGORY;"
                + promotion.getId() + ";"
                + promotion.getName() + ";"
                + promotion.getStartDate() + ";"
                + promotion.getEndDate() + ";"
                + promotion.getPercentage() + ";"
                + promotion.getTargetCategory();
    }

    /**
     * Formats a bulk purchase discount promotion.
     *
     * @param promotion bulk purchase promotion
     * @return formatted record
     */
    private String formatBulkPurchaseDiscount(
            BulkPurchaseDiscount promotion) {

        return "BULK;"
                + promotion.getId() + ";"
                + promotion.getName() + ";"
                + promotion.getStartDate() + ";"
                + promotion.getEndDate() + ";"
                + promotion.getMinimumQuantity() + ";"
                + promotion.getPercentage();
    }
}