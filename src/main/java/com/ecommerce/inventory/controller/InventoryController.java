package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.controller.request.InventoryUpdateRequest;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Consultar estoque de um produto")
    public ResponseEntity<Inventory> getInventory(@PathVariable String productId){
        return inventoryService.getInventoryByProductId(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Atualiza a quantidade em estoque de um produto")
    public ResponseEntity<Inventory> updateProductInventory(@PathVariable String productId, @RequestBody InventoryUpdateRequest request){
        Inventory inventory = inventoryService.updateInventory(productId, request.getQuantity());
        return ResponseEntity.ok().body(inventory);
    }

    @PostMapping("/{productId}/decrease")
    @Operation(summary = "Dar baixa no estoque após confirmaçã de pedido")
    public ResponseEntity<?> decreaseStock(@PathVariable String productId, @RequestBody InventoryUpdateRequest request){
        boolean success = inventoryService.decreaseInventory(productId, request.getQuantity());

        if(!success){
            return ResponseEntity.badRequest().body(new RuntimeException("Não foi possível dar baixa no produto."));
        }

        return ResponseEntity.ok().body("Baixa conclúida!");
    }

}
