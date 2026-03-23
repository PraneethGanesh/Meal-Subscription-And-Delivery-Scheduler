package com.example.mealsubscription.Repository;

import com.example.mealsubscription.Entity.Subscription;
import com.example.mealsubscription.Enum.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByStatusAndNextDeliveryTimeBefore(Status status, LocalDateTime time);
}