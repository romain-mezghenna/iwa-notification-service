package com.iwa.notificationservice.service;

import com.iwa.notificationservice.model.Notification;
import com.iwa.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    private Notification notification1;
    private Notification notification2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        notification1 = new Notification();
        notification1.setId(1L);
        notification1.setUserId(100L);
        notification1.setTitle("Notification 1");
        notification1.setMessage("This is the first notification.");
        notification1.setRead(false);

        notification2 = new Notification();
        notification2.setId(2L);
        notification2.setUserId(100L);
        notification2.setTitle("Notification 2");
        notification2.setMessage("This is the second notification.");
        notification2.setRead(false);
    }

    @Test
    void testCreateNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification1);

        Notification createdNotification = notificationService.createNotification(notification1);

        assertNotNull(createdNotification);
        assertEquals(notification1.getId(), createdNotification.getId());
        assertEquals(notification1.getTitle(), createdNotification.getTitle());
        verify(notificationRepository, times(1)).save(notification1);
    }

    @Test
    void testGetNotificationsByUserId() {
        when(notificationRepository.findByUserId(100L)).thenReturn(Arrays.asList(notification1, notification2));

        List<Notification> notifications = notificationService.getNotificationsByUserId(100L);

        assertNotNull(notifications);
        assertEquals(2, notifications.size());
        verify(notificationRepository, times(1)).findByUserId(100L);
    }

    @Test
    void testGetUnreadNotificationsByUserId() {
        when(notificationRepository.findByUserIdAndReadFalse(100L)).thenReturn(Arrays.asList(notification1, notification2));

        List<Notification> unreadNotifications = notificationService.getUnreadNotificationsByUserId(100L);

        assertNotNull(unreadNotifications);
        assertEquals(2, unreadNotifications.size());
        verify(notificationRepository, times(1)).findByUserIdAndReadFalse(100L);
    }

    @Test
    void testMarkAsRead() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification1));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification1);

        Notification updatedNotification = notificationService.markAsRead(1L);

        assertNotNull(updatedNotification);
        assertTrue(updatedNotification.isRead());
        verify(notificationRepository, times(1)).findById(1L);
        verify(notificationRepository, times(1)).save(notification1);
    }

    @Test
    void testMarkAsReadThrowsExceptionWhenNotFound() {
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.markAsRead(999L);
        });

        assertEquals("Notification with ID 999 not found", exception.getMessage());
        verify(notificationRepository, times(1)).findById(999L);
    }

    @Test
    void testDeleteNotificationsByUserId() {
        doNothing().when(notificationRepository).deleteByUserId(100L);

        notificationService.deleteNotificationsByUserId(100L);

        verify(notificationRepository, times(1)).deleteByUserId(100L);
    }
}