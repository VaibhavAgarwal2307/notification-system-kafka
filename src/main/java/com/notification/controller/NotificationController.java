package com.notification.controller;

import com.notification.model.Notification;
import com.notification.service.KafkaProducerService;
import com.notification.service.NotificationService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.notification.model.NotificationStatus;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final KafkaProducerService kafkaProducerService;

    public NotificationController(NotificationService notificationService,
                                  KafkaProducerService kafkaProducerService) {
        this.notificationService = notificationService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping("/test")
    public String test() {
        return "Working!";
    }

    // CREATE notification
    @PostMapping
    public Notification createNotification(@Valid @RequestBody Notification notification){

        Notification saved = notificationService.createNotification(notification);

        kafkaProducerService.sendMessage(saved.getId().toString());

        return saved;
    }


    // GET all notifications
    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/status/{status}")
    public List<Notification> getNotificationsByStatus(@PathVariable NotificationStatus status) {
        return notificationService.getNotificationsByStatus(status);
    }


    }
