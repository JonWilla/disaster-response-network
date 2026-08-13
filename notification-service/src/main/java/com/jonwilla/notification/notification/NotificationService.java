package com.jonwilla.notification.notification;

import com.jonwilla.notification.notification.dto.NotificationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository
    ) {
        this.notificationRepository = notificationRepository;
    }

    public void createCriticalIncidentNotification(
            UUID incidentId,
            String title,
            String location
    ) {
        Notification notification =
                new Notification(
                        incidentId,
                        NotificationType.CRITICAL_INCIDENT,
                        "CRITICAL INCIDENT: "
                                + title
                                + " at "
                                + location
                );

        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> findAll() {
        return notificationRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public NotificationResponse markAsRead(
            UUID id
    ) {
        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Notification not found: " + id
                                )
                        );

        notification.markAsRead();

        return toResponse(notification);
    }

    private NotificationResponse toResponse(
            Notification notification
    ) {
        return new NotificationResponse(
                notification.getId(),
                notification.getIncidentId(),
                notification.getType(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}