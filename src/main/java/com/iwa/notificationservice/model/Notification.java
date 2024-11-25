package com.iwa.notificationservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // ID de l'utilisateur associé à la notification

    private String title;

    private String message;

    private boolean read;

    private LocalDateTime createdAt;

    // Constructors
    public Notification() {
        this.createdAt = LocalDateTime.now();
        this.read = false; // Par défaut, une notification est non lue
    }

    public Notification(Long userId, String title, String message) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.read = false;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}