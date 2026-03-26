package com.example.mealsubscription.Entity;

import jakarta.persistence.*;

import java.time.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private String name;
    private String email;
    private Instant createdAt;
    private String timezone;



    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getTimezone() {
        return timezone;
    }
    @PrePersist
    public void setCreatedAt() {
        this.createdAt = Instant.now();
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
