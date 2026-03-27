package com.example.mealsubscription.Repository;

import com.example.mealsubscription.Entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.*;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Query(value = "SELECT * FROM subscription s WHERE s.status = :status AND s.next_delivery_time IS NOT NULL AND s.next_delivery_time <= :currentTime", nativeQuery = true)
    List<Subscription> findDueSubscriptions(
            @Param("status") String status,
            @Param("currentTime") Instant currentTime  // ← was LocalDateTime
    );
    @Query(value = "select count(*) from subscription s where s.user_id=:userId and s.slot=:slot and s.status=:status",nativeQuery = true)
    int checkSubscriptionExists(
            @Param("userId") long userId,
            @Param("slot") String slot,
            @Param("status") String status
    );

    List<Subscription> findByUser_userId(Long userId);
}