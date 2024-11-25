package com.iwa.notificationservice.controller;

import com.iwa.notificationservice.model.Notification;
import com.iwa.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * Créer une nouvelle notification.
     *
     * @param notification La notification à créer.
     * @return La notification créée.
     */
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification createdNotification = notificationService.createNotification(notification);
        return ResponseEntity.ok(createdNotification);
    }

    /**
     * Récupérer toutes les notifications d'un utilisateur.
     *
     * @param userId L'ID de l'utilisateur.
     * @return La liste des notifications.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Récupérer toutes les notifications non lues d'un utilisateur.
     *
     * @param userId L'ID de l'utilisateur.
     * @return La liste des notifications non lues.
     */
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotificationsByUserId(@PathVariable Long userId) {
        List<Notification> unreadNotifications = notificationService.getUnreadNotificationsByUserId(userId);
        return ResponseEntity.ok(unreadNotifications);
    }

    /**
     * Marquer une notification comme lue.
     *
     * @param notificationId L'ID de la notification.
     * @return La notification mise à jour.
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long notificationId) {
        Notification updatedNotification = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(updatedNotification);
    }

    /**
     * Supprimer toutes les notifications d'un utilisateur.
     *
     * @param userId L'ID de l'utilisateur.
     * @return Une réponse indiquant le succès.
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteNotificationsByUserId(@PathVariable Long userId) {
        notificationService.deleteNotificationsByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}