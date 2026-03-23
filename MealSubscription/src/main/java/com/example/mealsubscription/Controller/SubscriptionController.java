package com.example.mealsubscription.Controller;

import com.example.mealsubscription.DTO.SubscriptionRequest;
import com.example.mealsubscription.Entity.Subscription;
import com.example.mealsubscription.Service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<?> addSubscription(@RequestBody SubscriptionRequest request) {
        subscriptionService.addSubscription(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Added new subscription for user: " + request.getUserId());
    }

    @GetMapping
    public List<Subscription> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    @PutMapping("/{id}/pause")
    public ResponseEntity<Subscription> pauseSubscription(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.pauseSubscription(id));
    }

    @PutMapping("/{id}/resume")
    public ResponseEntity<Subscription> resumeSubscription(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.resumeSubscription(id));
    }

    @PutMapping("/{id}/expire")
    public ResponseEntity<Subscription> expireSubscription(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.expireSubscription(id));
    }
}