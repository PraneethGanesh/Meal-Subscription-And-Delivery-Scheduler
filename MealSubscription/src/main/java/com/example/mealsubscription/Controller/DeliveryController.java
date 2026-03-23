package com.example.mealsubscription.Controller;

import com.example.mealsubscription.Entity.Delivery;
import com.example.mealsubscription.Enum.DeliveryStatus;
import com.example.mealsubscription.Service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Delivery> updateDeliveryStatus(
            @PathVariable Long id,
            @RequestParam DeliveryStatus status) {
        return ResponseEntity.ok(deliveryService.updateDeliveryStatus(id, status));
    }

    @GetMapping("/subscription/{subscriptionId}")
    public ResponseEntity<List<Delivery>> getDeliveriesBySubscription(
            @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(deliveryService.getDeliveriesBySubscription(subscriptionId));
    }
}