package com.iwa.notificationservice.service;

import com.iwa.notificationservice.model.AccountDeletionRequest;
import com.iwa.notificationservice.model.AccountDeletionRequest.RequestStatus;
import com.iwa.notificationservice.repository.AccountDeletionRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AccountDeletionRequestServiceTest {

    @InjectMocks
    private AccountDeletionRequestService service;

    @Mock
    private AccountDeletionRequestRepository repository;

    private AccountDeletionRequest pendingRequest;
    private AccountDeletionRequest approvedRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pendingRequest = new AccountDeletionRequest();
        pendingRequest.setId(1L);
        pendingRequest.setUserId(100L);
        pendingRequest.setUserEmail("user@example.com");
        pendingRequest.setRequestTime(LocalDateTime.now());
        pendingRequest.setStatus(RequestStatus.PENDING);

        approvedRequest = new AccountDeletionRequest();
        approvedRequest.setId(2L);
        approvedRequest.setUserId(200L);
        approvedRequest.setUserEmail("anotheruser@example.com");
        approvedRequest.setRequestTime(LocalDateTime.now());
        approvedRequest.setStatus(RequestStatus.APPROVED);
    }

    @Test
    void testListenUserDeletionRequests() {
        doAnswer(invocation -> {
            AccountDeletionRequest savedRequest = invocation.getArgument(0);
            assertEquals(100L, savedRequest.getUserId());
            assertEquals("user@example.com", savedRequest.getUserEmail());
            assertEquals(RequestStatus.PENDING, savedRequest.getStatus());
            return null;
        }).when(repository).save(any(AccountDeletionRequest.class));

        service.listenUserDeletionRequests("100");

        verify(repository, times(1)).save(any(AccountDeletionRequest.class));
    }

    @Test
    void testGetPendingRequests() {
        when(repository.findByStatus(RequestStatus.PENDING)).thenReturn(Arrays.asList(pendingRequest));

        List<AccountDeletionRequest> requests = service.getPendingRequests();

        assertNotNull(requests);
        assertEquals(1, requests.size());
        assertEquals(RequestStatus.PENDING, requests.get(0).getStatus());
        verify(repository, times(1)).findByStatus(RequestStatus.PENDING);
    }

    @Test
    void testApproveRequest() {
        when(repository.findById(1L)).thenReturn(Optional.of(pendingRequest));
        when(repository.save(any(AccountDeletionRequest.class))).thenReturn(pendingRequest);

        AccountDeletionRequest approved = service.approveRequest(1L);

        assertNotNull(approved);
        assertEquals(RequestStatus.APPROVED, approved.getStatus());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(pendingRequest);
    }

    @Test
    void testApproveRequestThrowsExceptionWhenNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.approveRequest(999L);
        });

        assertEquals("Request not found", exception.getMessage());
        verify(repository, times(1)).findById(999L);
    }

    @Test
    void testRejectRequest() {
        when(repository.findById(1L)).thenReturn(Optional.of(pendingRequest));
        when(repository.save(any(AccountDeletionRequest.class))).thenReturn(pendingRequest);

        AccountDeletionRequest rejected = service.rejectRequest(1L);

        assertNotNull(rejected);
        assertEquals(RequestStatus.REJECTED, rejected.getStatus());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(pendingRequest);
    }

    @Test
    void testRejectRequestThrowsExceptionWhenNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.rejectRequest(999L);
        });

        assertEquals("Request not found", exception.getMessage());
        verify(repository, times(1)).findById(999L);
    }
}