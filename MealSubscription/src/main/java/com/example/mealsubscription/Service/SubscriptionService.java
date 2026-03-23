package com.example.mealsubscription.Service;

import com.example.mealsubscription.DTO.SubscriptionRequest;
import com.example.mealsubscription.Entity.Subscription;
import com.example.mealsubscription.Entity.User;
import com.example.mealsubscription.Enum.MealSlot;
import com.example.mealsubscription.Enum.Status;
import com.example.mealsubscription.Exceptions.UserNotFoundException;
import com.example.mealsubscription.Repository.SubscriptionRepository;
import com.example.mealsubscription.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final DeliveryTimeService deliveryTimeService;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               UserRepository userRepository,
                               DeliveryTimeService deliveryTimeService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.deliveryTimeService = deliveryTimeService;
    }

    public void addSubscription(SubscriptionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("user with ID:" + request.getUserId() + " not found"));

        for (MealSlot slot : request.getMealSlots()) {
            Subscription subscription = new Subscription();
            subscription.setSlot(slot);
            subscription.setScheduleType(request.getScheduleType());
            if (request.getDayOfWeek() != null) {
                subscription.setDayOfWeek(request.getDayOfWeek());
            }
            subscription.setStatus(Status.ACTIVE);
            subscription.setNextDeliveryTime(deliveryTimeService.calculateNextDelivery(request, slot));
            subscription.setUser(user);
            subscriptionRepository.save(subscription);
        }
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public Subscription pauseSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        subscription.setStatus(Status.PAUSED);
        subscription.setNextDeliveryTime(null);
        return subscriptionRepository.save(subscription);
    }

    public Subscription resumeSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        subscription.setStatus(Status.ACTIVE);
        subscription.setNextDeliveryTime(deliveryTimeService.getNextDeliveryTime(subscription));
        return subscriptionRepository.save(subscription);
    }

    public Subscription expireSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        subscription.setStatus(Status.EXPIRED);
        subscription.setNextDeliveryTime(null);
        return subscriptionRepository.save(subscription);
    }

    public Subscription updateSubscription(Long subscriptionId, SubscriptionRequest request) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscription.setScheduleType(request.getScheduleType());
        if (request.getDayOfWeek() != null) {
            subscription.setDayOfWeek(request.getDayOfWeek());
        }
        subscription.setSlot(request.getMealSlots().get(0)); // Update slot
        subscription.setNextDeliveryTime(deliveryTimeService.calculateNextDelivery(request, subscription.getSlot()));

        return subscriptionRepository.save(subscription);
    }
}