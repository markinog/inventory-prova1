package com.ecommerce.inventory.service;

import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Inventory updateInventory(String productId, Integer quantity){

        Optional<Inventory> existingInventory = getInventoryByProductId(productId);

        if(!existingInventory.isPresent()){
            Inventory inventory = new Inventory();
            inventory.setProductId(productId);
            inventory.setQuantity(quantity);
            return inventoryRepository.save(inventory);
        }

        Inventory inventory = existingInventory.get();
        inventory.setQuantity(quantity);
        return inventoryRepository.save(inventory);
    }

    public boolean decreaseInventory(String productId, Integer quantity){
        Optional<Inventory> existingInventory = getInventoryByProductId(productId);

        if(existingInventory.isPresent()){
            Inventory inventory = existingInventory.get();
            if(inventory.getQuantity() >= quantity){
                inventory.setQuantity(inventory.getQuantity() - quantity);
                inventoryRepository.save(inventory);
                return true;
            }
        }
        return false;
    }

    public Optional<Inventory> getInventoryByProductId(String productId){
        return inventoryRepository.findByProductId(productId);
    }

}
