package com.iwa.notificationservice.repository;

import com.iwa.notificationservice.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test") // Assurez-vous d'utiliser un profil de test si nécessaire
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    private Notification notification1;
    private Notification notification2;
    private Notification notification3;

    @BeforeEach
    void setUp() {
        notification1 = new Notification();
        notification1.setUserId(1L);
        notification1.setTitle("Title 1");
        notification1.setMessage("Message 1");
        notification1.setRead(false);
        notification1.setCreatedAt(LocalDateTime.now());

        notification2 = new Notification();
        notification2.setUserId(1L);
        notification2.setTitle("Title 2");
        notification2.setMessage("Message 2");
        notification2.setRead(true);
        notification2.setCreatedAt(LocalDateTime.now());

        notification3 = new Notification();
        notification3.setUserId(2L);
        notification3.setTitle("Title 3");
        notification3.setMessage("Message 3");
        notification3.setRead(false);
        notification3.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);
        notificationRepository.save(notification3);
    }

    @Test
    void testFindByUserId() {
        List<Notification> notifications = notificationRepository.findByUserId(1L);
        assertEquals(2, notifications.size());
        assertTrue(notifications.stream().anyMatch(n -> n.getTitle().equals("Title 1")));
        assertTrue(notifications.stream().anyMatch(n -> n.getTitle().equals("Title 2")));
    }

    @Test
    void testFindByUserIdAndReadFalse() {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadFalse(1L);
        assertEquals(1, unreadNotifications.size());
        assertEquals("Title 1", unreadNotifications.get(0).getTitle());
    }

    @Test
    void testDeleteByUserId() {
        notificationRepository.deleteByUserId(1L);
        List<Notification> notifications = notificationRepository.findByUserId(1L);
        assertTrue(notifications.isEmpty());

        // Verify notifications for other users are not affected
        List<Notification> remainingNotifications = notificationRepository.findByUserId(2L);
        assertEquals(1, remainingNotifications.size());
    }
}