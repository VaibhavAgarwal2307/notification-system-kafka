package com.notification.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.notification.service.NotificationProcessor;
import com.notification.model.Notification;
import com.notification.repository.NotificationRepository;

@Service
public class KafkaConsumerService {

    private final NotificationProcessor notificationProcessor;
    private final NotificationRepository notificationRepository;

    // ✅ Constructor Injection (NO errors)
    public KafkaConsumerService(NotificationProcessor notificationProcessor,
                                NotificationRepository notificationRepository) {
        this.notificationProcessor = notificationProcessor;
        this.notificationRepository = notificationRepository;
    }

    // ✅ Kafka Listener
    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consume(String message) {

        System.out.println("📩 Message received from Kafka: " + message);

        // ✅ Convert String → Notification (minimal safe fix)
        Long notificationId = Long.parseLong(message);

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        // You can optionally set dummy values to avoid null issues

        // notification.setType(NotificationType.EMAIL); // only if required
        // notification.setStatus(NotificationStatus.PENDING); // only if required

        // 👉 CALL REAL PROCESSOR
        notificationProcessor.process(notification);
    }
}