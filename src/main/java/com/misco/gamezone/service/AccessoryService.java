package com.misco.gamezone.service;

import com.misco.gamezone.model.Accessory;
import com.misco.gamezone.model.Controller;
import com.misco.gamezone.model.Cable;
import com.misco.gamezone.model.Memory;
import com.misco.gamezone.persistence.AccessoryRepository;

import java.util.ArrayList;
import java.util.List;

public class AccessoryService {

    private final AccessoryRepository repository;
    private final List<Accessory> accessories;

    public AccessoryService(AccessoryRepository repository) {
        this.repository = repository;
        this.accessories = repository.loadAll();
    }

    public boolean registerController(String id, String name, double price,
            int stock, List<String> compatibleConsoles,
            String connectionType) {

        if (findById(id) != null) {
            return false;
        }

        Controller controller = new Controller(
                id, name, price, stock, compatibleConsoles, connectionType);

        accessories.add(controller);
        repository.saveAll(accessories);
        return true;
    }

    public boolean registerCable(String id, String name, double price,
            int stock, List<String> compatibleConsoles,
            double lengthMeters, String connectorType) {

        if (findById(id) != null) {
            return false;
        }

        Cable cable = new Cable(
                id, name, price, stock, compatibleConsoles,
                lengthMeters, connectorType);

        accessories.add(cable);
        repository.saveAll(accessories);
        return true;
    }

    public boolean registerMemory(String id, String name, double price,
            int stock, List<String> compatibleConsoles,
            int capacityGB, String memoryType) {

        if (findById(id) != null) {
            return false;
        }

        Memory memory = new Memory(
                id, name, price, stock, compatibleConsoles,
                capacityGB, memoryType);

        accessories.add(memory);
        repository.saveAll(accessories);
        return true;
    }

    public List<Accessory> listAllAccessories() {
        return new ArrayList<>(accessories);
    }

    public List<Accessory> listAccessoriesByType(String type) {
        List<Accessory> result = new ArrayList<>();

        for (Accessory accessory : accessories) {
            if (accessory.getProductType().equalsIgnoreCase(type)) {
                result.add(accessory);
            }
        }

        return result;
    }

    public List<Accessory> findAccessoriesCompatibleWith(String consoleId) {
        List<Accessory> result = new ArrayList<>();

        for (Accessory accessory : accessories) {
            if (accessory.getCompatibleConsoles().contains(consoleId)) {
                result.add(accessory);
            }
        }

        return result;
    }

    public Accessory findById(String id) {
        for (Accessory accessory : accessories) {
            if (accessory.getId().equalsIgnoreCase(id)) {
                return accessory;
            }
        }

        return null;
    }

    public boolean updateStock(String id, int newStock) {
        Accessory accessory = findById(id);

        if (accessory == null || newStock < 0) {
            return false;
        }

        accessory.setStock(newStock);
        repository.saveAll(accessories);
        return true;
    }

    /**
     * Restores stock for an accessory.
     *
     * @param accessoryId accessory identifier
     * @param quantity quantity to restore
     * @return true if the stock was restored successfully
     */
    public boolean restoreStock(String accessoryId, int quantity) {

        if (quantity <= 0) {
            return false;
        }

        Accessory accessory = findById(accessoryId);

        if (accessory == null) {
            return false;
        }

        accessory.setStock(
                accessory.getStock() + quantity
        );

        repository.saveAll(accessories);

        return true;
    }
}
