package com.example.mealsubscription.Service;

import com.example.mealsubscription.Config.MealSlotService;
import com.example.mealsubscription.DTO.SubscriptionRequest;
import com.example.mealsubscription.Entity.Subscription;
import com.example.mealsubscription.Entity.User;
import com.example.mealsubscription.Enum.MealSlot;
import com.example.mealsubscription.Enum.ScheduleType;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class DeliveryTimeService {
    private final MealSlotService mealSlotService;

    public DeliveryTimeService(MealSlotService mealSlotService) {
        this.mealSlotService = mealSlotService;
    }

    public Instant calculateNextDelivery(SubscriptionRequest request, MealSlot slot, User user) {
        ZoneId userZone=ZoneId.of(user.getTimezone());
        if (request.getScheduleType() == ScheduleType.DAILY) {
            return calculateDailyNextDelivery(slot,userZone);
        } else {
            return calculateWeeklyNextDelivery(slot, request.getDayOfWeek(),userZone);
        }
    }

    private Instant calculateDailyNextDelivery(MealSlot slot, ZoneId userZone) {
        LocalTime slotTime = mealSlotService.getTime(slot);
        ZonedDateTime now = ZonedDateTime.now(userZone);
        ZonedDateTime deliveryTime;

        if (now.toLocalTime().isBefore(slotTime)) {
            deliveryTime=ZonedDateTime.of(now.toLocalDate(),slotTime,userZone);
        } else {
            deliveryTime=ZonedDateTime.of(now.toLocalDate().plusDays(1), slotTime,userZone);
        }
        return deliveryTime.toInstant();
    }

    private Instant calculateWeeklyNextDelivery(MealSlot slot, DayOfWeek dayOfWeek,ZoneId userZone) {
        LocalTime slotTime = mealSlotService.getTime(slot);
        ZonedDateTime now=ZonedDateTime.now(userZone);
        DayOfWeek today = now.getDayOfWeek();


        int daysToAdd = dayOfWeek.getValue() - today.getValue();

        if (daysToAdd == 0) {
            if (now.toLocalTime().isBefore(slotTime)) {
                return ZonedDateTime.of(now.toLocalDate(), slotTime,userZone).toInstant();
            } else {
                return ZonedDateTime.of(now.toLocalDate().plusDays(7), slotTime,userZone).toInstant();
            }
        }

        if (daysToAdd < 0) {
            daysToAdd += 7;
        }

        ZonedDateTime deliveryTime = ZonedDateTime.of(
                now.toLocalDate().plusDays(daysToAdd),slotTime,userZone);
        return deliveryTime.toInstant();

    }

    public Instant getNextDeliveryTime(Subscription subscription) {
        ZoneId userZone=ZoneId.of(subscription.getUser().getTimezone());
        if (subscription.getScheduleType() == ScheduleType.DAILY) {
            return calculateDailyNextDelivery(subscription.getSlot(),userZone);
        } else {
            return calculateWeeklyNextDelivery(subscription.getSlot(), subscription.getDayOfWeek(),userZone);
        }
    }
}