/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.model;

import java.util.List;

/**
 *
 * @author Acer
 */

/**
 * Represents a cable accessory
 * 
 */
public class Cable extends Accessory {
    
    private double lengthMeters;
    private String connectorType;
    
    public Cable(String id, String name, double price, int stock, List<String> compatibleConsoles, double lengthMeters, String connectorType){
        super(id, name, price, stock, compatibleConsoles);
        this.lengthMeters = lengthMeters;
        this.connectorType = connectorType;
    }
    
    public double getLengthMeters(){
          return lengthMeters;
    }
    
    public void setLengthMeters(double lengthMeters){
        this.lengthMeters = lengthMeters;
    }
    
    public String getConnectorType(){
        return connectorType;
    }
    public void SetConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }
    
    @Override
    public String getProductType() {
        return "CABLE";
    }
    
    @Override
    public String getDescription() {
        return super.getDescription()
                + ", Length (meters): " + lengthMeters
                + ", Connector type: " + connectorType;
    }
}
