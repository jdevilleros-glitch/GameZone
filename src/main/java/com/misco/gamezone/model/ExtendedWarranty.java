/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a twelve-month extended warranty.
 *
 * @author USUARIO
 */
public class ExtendedWarranty extends Warranty {

    public ExtendedWarranty(String warrantyId, Product product,
            Sale sale, LocalDate startDate) {
        super(warrantyId, product, sale, startDate);
    }

    @Override
    public int getDurationInMonths() {
        return 12;
    }

    @Override
    public String getWarrantyType() {
        return "Extended warranty";
    }

    @Override
    public double getAdditionalCost() {
        return getProduct().getPrice() * 0.10;
    }
}
