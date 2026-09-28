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
 * Represents a memory accessory
 * 
 */
public class Memory extends Accessory{
    
    private int capacityGB;
    private String memoryType;
    
    public Memory(String id, String name, double price, int stock, List<String> compatibleConsoles, int capacityGB, String memoryType) {
        super(id, name, price, stock, compatibleConsoles);
        this.capacityGB = capacityGB;
        this.memoryType = memoryType;
    }
    
    public int getCapacityGB() {
        return capacityGB;
    }
    
    public void setCapacityGB(int capacityGB) {
        this.capacityGB = capacityGB;
    }
    
    public String getMemoryType() {
        return memoryType;
    }
    
    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }
    
    @Override
    public String getProductType(){
        return "MEMORY";
    }
    
    @Override
    public String getDescription(){
        return super.getDescription()
                + ", Capacity (GB): " +capacityGB + ", Memory type: " + memoryType;
    }
}
