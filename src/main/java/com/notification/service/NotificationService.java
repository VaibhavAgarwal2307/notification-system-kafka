package com.notification.service;

import com.notification.model.Notification;
import com.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import com.notification.model.NotificationStatus;
import org.springframework.scheduling.annotation.Async;


import java.time.LocalDateTime;
import java.util.List;


@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationProcessor notificationProcessor;

    // Constructor Injection (IMPORTANT)
    public NotificationService(NotificationRepository notificationRepository,
                               NotificationProcessor notificationProcessor) {
        this.notificationRepository = notificationRepository;
        this.notificationProcessor = notificationProcessor;
    }

    // Save Notification
    public Notification createNotification(Notification notification) {

        // Set default values
        notification.setStatus(NotificationStatus.PENDING);
        notification.setCreatedAt(LocalDateTime.now());

        // Save first
        Notification savedNotification = notificationRepository.save(notification);

        // Process after saving
        notificationProcessor.process(savedNotification);

        return savedNotification;
    }

    // Get All Notifications
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();

    }

    public List<Notification> getNotificationsByStatus(NotificationStatus status) {
        return notificationRepository.findByStatus(status);
    }

}