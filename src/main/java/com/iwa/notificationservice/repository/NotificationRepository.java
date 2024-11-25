package com.iwa.notificationservice.repository;

import com.iwa.notificationservice.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Récupérer toutes les notifications d'un utilisateur
    List<Notification> findByUserId(Long userId);

    // Récupérer toutes les notifications non lues d'un utilisateur
    List<Notification> findByUserIdAndReadFalse(Long userId);

    // Supprimer toutes les notifications d'un utilisateur
    void deleteByUserId(Long userId);
}