package com.iwa.notificationservice.service;

import com.iwa.notificationservice.model.AccountDeletionRequest;
import com.iwa.notificationservice.model.AccountDeletionRequest.RequestStatus;
import com.iwa.notificationservice.repository.AccountDeletionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountDeletionRequestService {

    @Autowired
    private AccountDeletionRequestRepository repository;

    @KafkaListener(topics = "user-deletion-requests", groupId = "notification-service-group")
    public void listenUserDeletionRequests(String message) {
        Long userId = Long.parseLong(message);

        // Vous pouvez appeler le user-service pour récupérer l'email si nécessaire

        AccountDeletionRequest request = new AccountDeletionRequest();
        request.setUserId(userId);
        // Supposons que nous ayons récupéré l'email de l'utilisateur
        request.setUserEmail("user@example.com");
        request.setRequestTime(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);

        repository.save(request);
    }

    // Méthodes pour récupérer et gérer les demandes
    public List<AccountDeletionRequest> getPendingRequests() {
        return repository.findByStatus(RequestStatus.PENDING);
    }

    public AccountDeletionRequest approveRequest(Long requestId) {
        AccountDeletionRequest request = repository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(RequestStatus.APPROVED);
        repository.save(request);

        // Ici, vous pouvez publier un événement Kafka pour informer le user-service
        // que la suppression a été approuvée

        return request;
    }

    public AccountDeletionRequest rejectRequest(Long requestId) {
        AccountDeletionRequest request = repository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(RequestStatus.REJECTED);
        repository.save(request);

        // Optionnel : Notifier l'utilisateur que sa demande a été rejetée

        return request;
    }
}