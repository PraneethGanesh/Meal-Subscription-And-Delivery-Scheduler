package com.example.mealsubscription.Entity;

import com.example.mealsubscription.Enum.MealSlot;
import com.example.mealsubscription.Enum.ScheduleType;
import com.example.mealsubscription.Enum.Status;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.*;
@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private MealSlot slot;
    private ScheduleType scheduleType;
    private DayOfWeek dayOfWeek;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Instant nextDeliveryTime;
    private Instant created_at;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MealSlot getSlot() {
        return slot;
    }

    public void setSlot(MealSlot slot) {
        this.slot = slot;
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

    public Instant getNextDeliveryTime() {
        return nextDeliveryTime;
    }

    public void setNextDeliveryTime(Instant nextDeliveryTime) {
        this.nextDeliveryTime = nextDeliveryTime;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    @PrePersist
    public void setCreated_at() {
        this.created_at = Instant.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
