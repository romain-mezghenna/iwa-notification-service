package com.iwa.notificationservice.controller;

import com.iwa.notificationservice.model.AccountDeletionRequest;
import com.iwa.notificationservice.service.AccountDeletionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/deletion-requests")
public class AccountDeletionRequestController {

    @Autowired
    private AccountDeletionRequestService service;

    @GetMapping("/pending")
    public List<AccountDeletionRequest> getPendingRequests() {
        return service.getPendingRequests();
    }

    @PostMapping("/{id}/approve")
    public AccountDeletionRequest approveRequest(@PathVariable Long id) {
        return service.approveRequest(id);
    }

    @PostMapping("/{id}/reject")
    public AccountDeletionRequest rejectRequest(@PathVariable Long id) {
        return service.rejectRequest(id);
    }
}