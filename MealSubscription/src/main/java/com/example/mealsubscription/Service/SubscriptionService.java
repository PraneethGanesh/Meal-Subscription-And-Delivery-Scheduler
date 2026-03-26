package com.example.mealsubscription.Service;

import com.example.mealsubscription.DTO.SubscriptionDTO;
import com.example.mealsubscription.DTO.SubscriptionRequest;
import com.example.mealsubscription.Entity.Subscription;
import com.example.mealsubscription.Entity.User;
import com.example.mealsubscription.Enum.MealSlot;
import com.example.mealsubscription.Enum.ScheduleType;
import com.example.mealsubscription.Enum.Status;
import com.example.mealsubscription.Exceptions.SubscriptionAlreadyExists;
import com.example.mealsubscription.Exceptions.UserNotFoundException;
import com.example.mealsubscription.Repository.SubscriptionRepository;
import com.example.mealsubscription.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
                .orElseThrow(() ->
                        new UserNotFoundException("User with ID: " + request.getUserId() + " not found"));

        if (request.getMealSlots() == null || request.getMealSlots().isEmpty()) {
            throw new IllegalArgumentException("Meal slots cannot be null or empty");
        }


        if (request.getScheduleType() == ScheduleType.DAILY) {
            request.setDayOfWeek(null); // must be null
        }
        else if (request.getScheduleType() == ScheduleType.WEEKLY) {

            if (request.getDayOfWeek() == null) {
                throw new IllegalArgumentException("DayOfWeek required for WEEKLY schedule");
            }
        }

        for (MealSlot slot : request.getMealSlots()) {


            if (subscriptionRepository.checkSubscriptionExists(
                    request.getUserId(),
                    slot.name(),
                    Status.ACTIVE.name()) > 0) {

                throw new SubscriptionAlreadyExists(
                        "Subscription already exists for user: "
                                + request.getUserId() + " with slot " + slot.name());
            }

            Subscription subscription = new Subscription();
            subscription.setUser(user);
            subscription.setSlot(slot);
            subscription.setScheduleType(request.getScheduleType());
            subscription.setDayOfWeek(request.getDayOfWeek());
            subscription.setStatus(Status.ACTIVE);


            subscription.setNextDeliveryTime(
                    deliveryTimeService.calculateNextDelivery(request, slot,user)
            );

            subscriptionRepository.save(subscription);
        }
    }

    public List<SubscriptionDTO> getAllSubscriptions() {
        List<Subscription> subscriptions=subscriptionRepository.findAll();
        List<SubscriptionDTO> subscriptionDTOList=new ArrayList<>();
        for (Subscription subscription:subscriptions){
            SubscriptionDTO dto=convertToDTO(subscription);
            subscriptionDTOList.add(dto);
        }
        return subscriptionDTOList;
    }

    private SubscriptionDTO convertToDTO(Subscription subscription){
        SubscriptionDTO subscriptionDTO=new SubscriptionDTO();
        subscriptionDTO.setSubscriptionId(subscription.getId());
        subscriptionDTO.setUserId(subscription.getUser().getUserId());
        subscriptionDTO.setMealSlot(subscription.getSlot());
        subscriptionDTO.setScheduleType(subscription.getScheduleType());
        if(subscription.getScheduleType()==ScheduleType.WEEKLY){
            subscriptionDTO.setDayOfWeek(subscription.getDayOfWeek());
        }
        subscriptionDTO.setStatus(subscription.getStatus());
        if(subscription.getNextDeliveryTime()!=null){
            ZoneId userZone=ZoneId.of(subscription.getUser().getTimezone());
            ZonedDateTime zonedDateTime=subscription.getNextDeliveryTime().atZone(userZone);
            subscriptionDTO.setNextDeliveryTime(zonedDateTime);
        }
        return subscriptionDTO;
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

        subscription.setNextDeliveryTime(
                deliveryTimeService.getNextDeliveryTime(subscription)
        );

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
        User user=subscription.getUser();


        if (request.getScheduleType() == ScheduleType.DAILY) {
            subscription.setDayOfWeek(null);
        }
        else if (request.getScheduleType() == ScheduleType.WEEKLY) {

            if (request.getDayOfWeek() == null) {
                throw new IllegalArgumentException("DayOfWeek required");
            }

            subscription.setDayOfWeek(request.getDayOfWeek());
        }

        subscription.setScheduleType(request.getScheduleType());

        if (request.getMealSlots() != null && !request.getMealSlots().isEmpty()) {
            subscription.setSlot(request.getMealSlots().get(0));
        }

        subscription.setNextDeliveryTime(
                deliveryTimeService.calculateNextDelivery(request, subscription.getSlot(),user)
        );

        return subscriptionRepository.save(subscription);
    }
}