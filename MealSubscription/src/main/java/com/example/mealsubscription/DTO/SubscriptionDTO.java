package com.example.mealsubscription.DTO;

import com.example.mealsubscription.Enum.MealSlot;
import com.example.mealsubscription.Enum.ScheduleType;
import com.example.mealsubscription.Enum.Status;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;

public class SubscriptionDTO {
    private long subscriptionId;
    private long userId;
    private MealSlot mealSlot;
    private ScheduleType scheduleType;
    private DayOfWeek dayOfWeek;
    private Status status;
    private ZonedDateTime nextDeliveryTime;

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public MealSlot getMealSlot() {
        return mealSlot;
    }

    public void setMealSlot(MealSlot mealSlot) {
        this.mealSlot = mealSlot;
    }

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ZonedDateTime getNextDeliveryTime() {
        return nextDeliveryTime;
    }

    public void setNextDeliveryTime(ZonedDateTime nextDeliveryTime) {
        this.nextDeliveryTime = nextDeliveryTime;
    }
}
