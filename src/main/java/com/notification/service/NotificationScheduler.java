package com.notification.service;

import com.notification.model.Notification;
import com.notification.model.NotificationStatus;
import com.notification.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationScheduler {

    private final NotificationRepository notificationRepository;
    private final NotificationProcessor notificationProcessor;

    public NotificationScheduler(NotificationRepository notificationRepository,
                                 NotificationProcessor notificationProcessor) {
        this.notificationRepository = notificationRepository;
        this.notificationProcessor = notificationProcessor;
    }

    @Scheduled(fixedRate = 10000) // every 10 sec
    public void retryFailedNotifications() {

        List<Notification> failedNotifications =
                notificationRepository.findByStatus(NotificationStatus.FAILED);

        for (Notification notification : failedNotifications) {

            System.out.println("Retrying notification id: " + notification.getId());

            notification.setStatus(NotificationStatus.PENDING);
            notificationRepository.save(notification);

            notificationProcessor.process(notification);
        }
    }
}