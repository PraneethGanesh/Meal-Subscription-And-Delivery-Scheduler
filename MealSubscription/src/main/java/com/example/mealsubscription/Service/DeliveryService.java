package com.example.mealsubscription.Service;

import com.example.mealsubscription.Entity.Delivery;
import com.example.mealsubscription.Entity.Subscription;
import com.example.mealsubscription.Enum.DeliveryStatus;
import com.example.mealsubscription.Enum.Status;
import com.example.mealsubscription.Repository.DeliveryRepository;
import com.example.mealsubscription.Repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryService {
    private final SubscriptionRepository subscriptionRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryTimeService deliveryTimeService;

    public DeliveryService(SubscriptionRepository subscriptionRepository,
                           DeliveryRepository deliveryRepository,
                           DeliveryTimeService deliveryTimeService) {
        this.subscriptionRepository = subscriptionRepository;
        this.deliveryRepository = deliveryRepository;
        this.deliveryTimeService = deliveryTimeService;
    }

    @Transactional
    public void processDueDeliveries() {
        LocalDateTime now = LocalDateTime.now();

        // Find all active subscriptions with due deliveries
        List<Subscription> dueSubscriptions = subscriptionRepository
                .findByStatusAndNextDeliveryTimeBefore(Status.ACTIVE, now);

        for (Subscription subscription : dueSubscriptions) {
            // Create delivery record
            Delivery delivery = new Delivery();
            delivery.setSubscription(subscription);
            delivery.setMealSlot(subscription.getSlot());
            delivery.setScheduledDeliveryTime(subscription.getNextDeliveryTime());
            delivery.setStatus(DeliveryStatus.IN_PROGRESS);
            deliveryRepository.save(delivery);

            // Update next delivery time
            LocalDateTime nextDelivery = deliveryTimeService.getNextDeliveryTime(subscription);
            subscription.setNextDeliveryTime(nextDelivery);
            subscriptionRepository.save(subscription);
        }
    }

    @Transactional
    public Delivery updateDeliveryStatus(Long deliveryId, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        delivery.setStatus(status);
        if (status == DeliveryStatus.DELIVERED) {
            delivery.setActualDeliveryTime(LocalDateTime.now());
        }

        return deliveryRepository.save(delivery);
    }

    public List<Delivery> getDeliveriesBySubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        return deliveryRepository.findRecentDeliveriesBySubscription(subscription, DeliveryStatus.SCHEDULED);
    }
}