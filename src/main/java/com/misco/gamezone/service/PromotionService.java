/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.service;

import com.misco.gamezone.dao.PromotionDAO;
import com.misco.gamezone.model.BulkPurchaseDiscount;
import com.misco.gamezone.model.CategoryDiscount;
import com.misco.gamezone.model.PercentageDiscount;
import com.misco.gamezone.model.Promotion;
import com.misco.gamezone.model.Sale;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides business operations for promotions.
 *
 * @author USUARIO
 */
public class PromotionService {

    private final PromotionDAO promotionDAO;
    private List<Promotion> promotions;

    /**
     * Creates a promotion service using the specified promotion DAO.
     *
     * @param promotionDAO DAO used to manage promotion persistence
     */
    public PromotionService(PromotionDAO promotionDAO) {
        this.promotionDAO = promotionDAO;
        this.promotions = promotionDAO.loadAll();
    }

    /**
     * Registers a percentage discount promotion.
     *
     * @param id promotion ID
     * @param name promotion name
     * @param startDate promotion start date
     * @param endDate promotion end date
     * @param percentage discount percentage
     */
    public void registerPercentageDiscount(
            String id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            double percentage) {

        Promotion promotion = new PercentageDiscount(
                id, name, startDate, endDate, percentage
        );

        promotions.add(promotion);
        promotionDAO.saveAll(promotions);
    }

    /**
     * Registers a category discount promotion.
     *
     * @param id promotion ID
     * @param name promotion name
     * @param startDate promotion start date
     * @param endDate promotion end date
     * @param percentage discount percentage
     * @param targetCategory category to which the discount applies
     */
    public void registerCategoryDiscount(
            String id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            double percentage,
            String targetCategory) {

        Promotion promotion = new CategoryDiscount(
                id, name, startDate, endDate,
                percentage, targetCategory
        );

        promotions.add(promotion);
        promotionDAO.saveAll(promotions);
    }

    /**
     * Registers a bulk purchase discount promotion.
     *
     * @param id promotion ID
     * @param name promotion name
     * @param startDate promotion start date
     * @param endDate promotion end date
     * @param minimumQuantity minimum number of products required
     * @param percentage discount percentage
     */
    public void registerBulkPurchaseDiscount(
            String id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            int minimumQuantity,
            double percentage) {

        Promotion promotion = new BulkPurchaseDiscount(
                id, name, startDate, endDate,
                minimumQuantity, percentage
        );

        promotions.add(promotion);
        promotionDAO.saveAll(promotions);
    }

    /**
     * Returns all registered promotions.
     *
     * @return list of all registered promotions
     */
    public List<Promotion> listAllPromotions() {
        promotions = promotionDAO.loadAll();
        return new ArrayList<>(promotions);
    }

    /**
     * Returns promotions that are active on the current date.
     *
     * @return list of active promotions
     */
    public List<Promotion> listActivePromotions() {

        promotions = promotionDAO.loadAll();

        List<Promotion> activePromotions = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Promotion promotion : promotions) {
            if (promotion.isActive(today)) {
                activePromotions.add(promotion);
            }
        }

        return activePromotions;
    }

    /**
     * Finds the active promotion that provides the highest monetary discount
     * for the specified sale.
     *
     * @param sale sale to evaluate
     * @return best promotion, or null if no promotion provides a discount
     */
    public Promotion findBestPromotionFor(Sale sale) {

        List<Promotion> activePromotions = listActivePromotions();

        Promotion bestPromotion = null;
        double highestDiscount = 0;

        for (Promotion promotion : activePromotions) {

            double discount = promotion.calculateDiscount(sale);

            if (discount > highestDiscount) {
                highestDiscount = discount;
                bestPromotion = promotion;
            }
        }

        return bestPromotion;
    }

    /**
     * Finds a promotion by its identifier.
     *
     * @param id promotion ID to search for
     * @return matching promotion, or null if it does not exist
     */
    public Promotion findById(String id) {

        promotions = promotionDAO.loadAll();

        for (Promotion promotion : promotions) {
            if (promotion.getId().equals(id)) {
                return promotion;
            }
        }

        return null;
    }
}
