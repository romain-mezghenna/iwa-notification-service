package com.iwa.notificationservice.repository;

import com.iwa.notificationservice.model.AccountDeletionRequest;
import com.iwa.notificationservice.model.AccountDeletionRequest.RequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test") // Si vous avez un profil spécifique pour les tests
class AccountDeletionRequestRepositoryTest {

    @Autowired
    private AccountDeletionRequestRepository repository;

    private AccountDeletionRequest request1;
    private AccountDeletionRequest request2;
    private AccountDeletionRequest request3;

    @BeforeEach
    void setUp() {
        request1 = new AccountDeletionRequest();
        request1.setUserId(1L);
        request1.setUserEmail("user1@example.com");
        request1.setRequestTime(LocalDateTime.now());
        request1.setStatus(RequestStatus.PENDING);

        request2 = new AccountDeletionRequest();
        request2.setUserId(2L);
        request2.setUserEmail("user2@example.com");
        request2.setRequestTime(LocalDateTime.now());
        request2.setStatus(RequestStatus.APPROVED);

        request3 = new AccountDeletionRequest();
        request3.setUserId(3L);
        request3.setUserEmail("user3@example.com");
        request3.setRequestTime(LocalDateTime.now());
        request3.setStatus(RequestStatus.PENDING);

        repository.save(request1);
        repository.save(request2);
        repository.save(request3);
    }

    @Test
    void testFindByStatus() {
        List<AccountDeletionRequest> pendingRequests = repository.findByStatus(RequestStatus.PENDING);
        assertEquals(2, pendingRequests.size());
        assertTrue(pendingRequests.stream().anyMatch(r -> r.getUserEmail().equals("user1@example.com")));
        assertTrue(pendingRequests.stream().anyMatch(r -> r.getUserEmail().equals("user3@example.com")));

        List<AccountDeletionRequest> approvedRequests = repository.findByStatus(RequestStatus.APPROVED);
        assertEquals(1, approvedRequests.size());
        assertEquals("user2@example.com", approvedRequests.get(0).getUserEmail());
    }
}