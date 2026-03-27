package com.example.mealsubscription.Controller;

import com.example.mealsubscription.DTO.SubscriptionDTO;
import com.example.mealsubscription.DTO.SubscriptionRequest;
import com.example.mealsubscription.Entity.Subscription;
import com.example.mealsubscription.Service.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
@Tag(name = "Subscription API",description = "create,get,update,pause,resume and expire subscriptions")
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
    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(
            @PathVariable Long id,
            @RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.updateSubscription(id, request));
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

    @GetMapping("/user/{id}")
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptionOfUser(@PathVariable long id){
       return ResponseEntity.ok(subscriptionService.getSubscriptionOfuser(id));
    }
}