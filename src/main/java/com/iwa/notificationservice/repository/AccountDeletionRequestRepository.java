package com.iwa.notificationservice.repository;

import com.iwa.notificationservice.model.AccountDeletionRequest;
import com.iwa.notificationservice.model.AccountDeletionRequest.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDeletionRequestRepository extends JpaRepository<AccountDeletionRequest, Long> {
    List<AccountDeletionRequest> findByStatus(RequestStatus status);
}