package com.iwa.notificationservice.service;

import com.iwa.notificationservice.model.Notification;
import com.iwa.notificationservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Créer une nouvelle notification.
     *
     * @param notification La notification à sauvegarder.
     * @return La notification sauvegardée.
     */
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    /**
     * Récupérer toutes les notifications d'un utilisateur.
     *
     * @param userId L'ID de l'utilisateur.
     * @return La liste des notifications.
     */
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    /**
     * Récupérer toutes les notifications non lues d'un utilisateur.
     *
     * @param userId L'ID de l'utilisateur.
     * @return La liste des notifications non lues.
     */
    public List<Notification> getUnreadNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdAndReadFalse(userId);
    }

    /**
     * Marquer une notification comme lue.
     *
     * @param notificationId L'ID de la notification.
     * @return La notification mise à jour.
     */
    public Notification markAsRead(Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setRead(true);
            return notificationRepository.save(notification);
        }
        throw new RuntimeException("Notification with ID " + notificationId + " not found");
    }

    /**
     * Supprimer toutes les notifications d'un utilisateur.
     *
     * @param userId L'ID de l'utilisateur.
     */
    public void deleteNotificationsByUserId(Long userId) {
        notificationRepository.deleteByUserId(userId);
    }
}