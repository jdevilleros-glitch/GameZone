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
public class Controller extends Accessory {
    
    private String connectionType;
    
    public Controller(String id, String name, double price, int stock, List<String> compatibleConsoles, String connectionType) {
        super(id, name, price, stock, compatibleConsoles);
        this.connectionType = connectionType;
        
    }
    
    public String getConnectionType(){
        return connectionType;  
    }
    
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }
    
    
    @Override
    public String getProductType() {
        return "CONTROLLER";
    }
    
    @Override
    public String getDescription(){
        return super.getDescription() + ", Connection type: " + connectionType;
    }
    
    }
