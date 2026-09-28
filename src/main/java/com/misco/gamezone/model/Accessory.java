/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Acer
 */

/**
 * Represent the common information and behavior of a video game accessory
 * 
 * 
 * @author Acer
 */

public abstract class  Accessory extends Product {
    
    private List<String> compatibleConsoles;
    /**
     * Creates an accessory with its common information and compatible consoles
     * 
     * @param id the accessory identifier 
     * @param name the accessory name
     * @param price the accessory price
     * @param stock the available stock
     * @param compatibleConsoles the identifiers of compatible consoles
     */
 
    public Accessory(String id, String name, double price, int stock,
            List<String> compatibleConsoles) {
        super(id, name, price, stock);
        
        if(compatibleConsoles == null){
            this.compatibleConsoles = new ArrayList<>();
        }else {
            this.compatibleConsoles = new ArrayList<>(compatibleConsoles);
        }
    }

    /**
     * Returns the identifiers of compatible consoles
     * 
     * @return a list of compatible console identifiers
     */
    
     public List<String> getCompatibleConsoles() {
        return new ArrayList<>(compatibleConsoles);
    }
    
    public void setCompatibleConsolesI(List<String> compatibleConsoles) {
        if (compatibleConsoles == null) {
            this.compatibleConsoles = new ArrayList<>();    
        }else{
            this.compatibleConsoles = new ArrayList<>(compatibleConsoles);
        }
    }
    
/**
 * Returns the common product information and compatible consoles
 * 
 * @return tne accessory description
 */

@Override
    public String getDescription() {
        return super.getDescription()
             + ", Compatible consoles: " + compatibleConsoles;
    }
}


        

