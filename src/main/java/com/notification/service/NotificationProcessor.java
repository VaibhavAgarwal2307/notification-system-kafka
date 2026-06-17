package com.notification.service;

import com.notification.model.Notification;
import com.notification.model.NotificationStatus;
import com.notification.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(NotificationProcessor.class);
    private final NotificationRepository notificationRepository;
    private final org.springframework.kafka.core.KafkaTemplate<String, String> kafkaTemplate;

    public NotificationProcessor(NotificationRepository notificationRepository,
                                 org.springframework.kafka.core.KafkaTemplate<String, String> kafkaTemplate) {
        this.notificationRepository = notificationRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void process(Notification notification) {

        int maxAttempts = 3;
        int attempt = 0;

        while (attempt < maxAttempts) {
            try {
                attempt++;

                logger.info("Attempt {}: Sending {} to {}",
                        attempt,
                        notification.getType(),
                        notification.getRecipient());

                // Simulate failure randomly (for testing retry)
                if (Math.random() < 0.5) {
                    throw new RuntimeException("Simulated random failure");
                }

              //   Success
                notification.setStatus(NotificationStatus.SENT);
                logger.info("Notification SENT successfully for id: {}", notification.getId());

               notificationRepository.save(notification);
                return; // exit after success

            } catch (Exception e) {
                logger.error("Processing FAILED for id: {} | Reason: {}", notification.getId(), e.getMessage());
                if (attempt == maxAttempts) {
                    notification.setStatus(NotificationStatus.FAILED);
                    notificationRepository.save(notification);

                    //  SEND TO DLQ
                    kafkaTemplate.send("notification-dlq", notification.getMessage());

                    logger.error("Sent to DLQ for id: {}", notification.getId());
                }

                try {
                    Thread.sleep(2000); // wait before retry
                } catch (InterruptedException ignored) {}
            }
        }
    }
}