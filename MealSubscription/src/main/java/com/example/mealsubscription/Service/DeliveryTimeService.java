package com.example.mealsubscription.Service;

import com.example.mealsubscription.Config.MealSlotService;
import com.example.mealsubscription.DTO.SubscriptionRequest;
import com.example.mealsubscription.Entity.Subscription;
import com.example.mealsubscription.Enum.MealSlot;
import com.example.mealsubscription.Enum.ScheduleType;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class DeliveryTimeService {
    private final MealSlotService mealSlotService;

    public DeliveryTimeService(MealSlotService mealSlotService) {
        this.mealSlotService = mealSlotService;
    }

    public LocalDateTime calculateNextDelivery(SubscriptionRequest request, MealSlot slot) {
        if (request.getScheduleType() == ScheduleType.DAILY) {
            return calculateDailyNextDelivery(slot);
        } else {
            return calculateWeeklyNextDelivery(slot, request.getDayOfWeek());
        }
    }

    private LocalDateTime calculateDailyNextDelivery(MealSlot slot) {
        LocalTime slotTime = mealSlotService.getTime(slot);
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalTime currentTime = currentDateTime.toLocalTime();

        if (currentTime.isBefore(slotTime)) {
            return LocalDateTime.of(currentDateTime.toLocalDate(), slotTime);
        } else {
            return LocalDateTime.of(currentDateTime.toLocalDate().plusDays(1), slotTime);
        }
    }

    private LocalDateTime calculateWeeklyNextDelivery(MealSlot slot, DayOfWeek dayOfWeek) {
        LocalTime slotTime = mealSlotService.getTime(slot);
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalTime currentTime = currentDateTime.toLocalTime();
        DayOfWeek today = currentDateTime.getDayOfWeek();

        int daysToAdd = dayOfWeek.getValue() - today.getValue();

        if (daysToAdd == 0) {
            if (currentTime.isBefore(slotTime)) {
                return LocalDateTime.of(currentDateTime.toLocalDate(), slotTime);
            } else {
                return LocalDateTime.of(currentDateTime.toLocalDate().plusDays(7), slotTime);
            }
        }

        if (daysToAdd < 0) {
            daysToAdd += 7;
        }

        LocalDateTime nextDateTime = currentDateTime.plusDays(daysToAdd);
        return LocalDateTime.of(nextDateTime.toLocalDate(), slotTime);
    }

    public LocalDateTime getNextDeliveryTime(Subscription subscription) {
        if (subscription.getScheduleType() == ScheduleType.DAILY) {
            return calculateDailyNextDelivery(subscription.getSlot());
        } else {
            return calculateWeeklyNextDelivery(subscription.getSlot(), subscription.getDayOfWeek());
        }
    }
}