package com.iwa.notificationservice.controller;

import com.iwa.notificationservice.model.Notification;
import com.iwa.notificationservice.security.JwtTokenFilter;
import com.iwa.notificationservice.security.JwtTokenUtil;
import com.iwa.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@TestPropertySource(properties = {
        "security.jwt.secret=TestSecretKeyForJWT"
})
@Import({JwtTokenUtil.class, JwtTokenFilter.class})
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private WebApplicationContext context;

    private Notification notification;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        notification = new Notification();
        notification.setId(1L);
        notification.setUserId(1L);
        notification.setTitle("Test Title");
        notification.setMessage("Test Message");
    }

    @Test
    @WithMockUser
    public void testCreateNotification() throws Exception {
        when(notificationService.createNotification(any(Notification.class))).thenReturn(notification);

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"title\":\"Test Title\",\"message\":\"Test Message\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.message").value("Test Message"));
    }

    @Test
    @WithMockUser
    public void testGetNotificationsByUserId() throws Exception {
        // ID de l'utilisateur pour lequel on veut récupérer les notifications
        Long userId = 1L;

        // Données fictives pour les notifications
        Notification notification1 = new Notification(userId, "Test Title 1", "Test Message 1");
        Notification notification2 = new Notification(userId, "Test Title 2", "Test Message 2");

        List<Notification> notifications = Arrays.asList(notification1, notification2);

        // Mock du service pour retourner ces notifications
        Mockito.when(notificationService.getNotificationsByUserId(userId)).thenReturn(notifications);

        // Effectuer la requête GET
        mockMvc.perform(get("/notifications/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Vérifie que le statut est 200
                .andExpect(jsonPath("$[0].title").value("Test Title 1"))
                .andExpect(jsonPath("$[0].message").value("Test Message 1"))
                .andExpect(jsonPath("$[1].title").value("Test Title 2"))
                .andExpect(jsonPath("$[1].message").value("Test Message 2"));

        // Vérifie que le service a été appelé avec le bon ID utilisateur
        Mockito.verify(notificationService).getNotificationsByUserId(userId);
    }

    @Test
    @WithMockUser
    public void testGetUnreadNotificationsByUserId() throws Exception {
        // ID de l'utilisateur pour lequel on veut récupérer les notifications non lues
        Long userId = 1L;

        // Données fictives pour les notifications non lues
        Notification notification1 = new Notification(userId, "Unread Title 1", "Unread Message 1");
        Notification notification2 = new Notification(userId, "Unread Title 2", "Unread Message 2");

        List<Notification> unreadNotifications = Arrays.asList(notification1, notification2);

        // Mock du service pour retourner ces notifications
        Mockito.when(notificationService.getUnreadNotificationsByUserId(userId)).thenReturn(unreadNotifications);

        // Effectuer la requête GET
        mockMvc.perform(get("/notifications/user/{userId}/unread", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Vérifie que le statut est 200
                .andExpect(jsonPath("$[0].title").value("Unread Title 1"))
                .andExpect(jsonPath("$[0].message").value("Unread Message 1"))
                .andExpect(jsonPath("$[1].title").value("Unread Title 2"))
                .andExpect(jsonPath("$[1].message").value("Unread Message 2"));

        // Vérifie que le service a été appelé avec le bon ID utilisateur
        Mockito.verify(notificationService).getUnreadNotificationsByUserId(userId);
    }

    @Test
    @WithMockUser
    public void testMarkNotificationAsRead() throws Exception {
        Notification readNotification = new Notification();
        readNotification.setId(1L);
        readNotification.setUserId(1L);
        readNotification.setTitle("Test Title");
        readNotification.setMessage("Test Message");
        readNotification.setRead(true);

        when(notificationService.markAsRead(1L)).thenReturn(readNotification);

        mockMvc.perform(put("/notifications/1/read")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.read").value(true));
    }

    @Test
    @WithMockUser
    public void testDeleteNotificationsByUserId() throws Exception {
        // ID de l'utilisateur à supprimer
        Long userId = 1L;

        // Effectuer la requête DELETE
        mockMvc.perform(delete("/notifications/user/{userId}", userId))
                .andExpect(status().isNoContent()); // Vérifie que le statut est 204 (No Content)

        // Vérifie que le service a été appelé avec le bon ID
        Mockito.verify(notificationService).deleteNotificationsByUserId(userId);
    }
}