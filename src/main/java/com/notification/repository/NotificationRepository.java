package com.notification.repository;

import com.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import com.notification.model.NotificationStatus;
import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStatus(NotificationStatus status);
}

