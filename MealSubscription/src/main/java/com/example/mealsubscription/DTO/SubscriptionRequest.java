package com.example.mealsubscription.DTO;

import com.example.mealsubscription.Enum.MealSlot;
import com.example.mealsubscription.Enum.ScheduleType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.List;

public class SubscriptionRequest {
    private long userId;
    private ScheduleType scheduleType;
    private DayOfWeek dayOfWeek;
    @NotNull
    @NotEmpty
    private List<MealSlot> mealSlots;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public List<MealSlot> getMealSlots() {
        return mealSlots;
    }

    public void setMealSlots(List<MealSlot> mealSlots) {
        this.mealSlots = mealSlots;
    }
}
