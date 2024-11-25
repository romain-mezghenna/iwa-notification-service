package com.iwa.notificationservice.controller;

import com.iwa.notificationservice.model.AccountDeletionRequest;
import com.iwa.notificationservice.model.AccountDeletionRequest.RequestStatus;
import com.iwa.notificationservice.security.JwtTokenFilter;
import com.iwa.notificationservice.security.JwtTokenUtil;
import com.iwa.notificationservice.service.AccountDeletionRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountDeletionRequestController.class)
@TestPropertySource(properties = {
        "security.jwt.secret=TestSecretKeyForJWT"
})
@Import({JwtTokenFilter.class})
public class AccountDeletionRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountDeletionRequestService service;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WebApplicationContext context;

    private AccountDeletionRequest pendingRequest;
    private AccountDeletionRequest approvedRequest;
    private AccountDeletionRequest rejectedRequest;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        // Préparez des objets de test
        pendingRequest = new AccountDeletionRequest();
        pendingRequest.setId(1L);
        pendingRequest.setUserId(100L);
        pendingRequest.setStatus(RequestStatus.PENDING);

        approvedRequest = new AccountDeletionRequest();
        approvedRequest.setId(2L);
        approvedRequest.setUserId(101L);
        approvedRequest.setStatus(RequestStatus.APPROVED);

        rejectedRequest = new AccountDeletionRequest();
        rejectedRequest.setId(3L);
        rejectedRequest.setUserId(102L);
        rejectedRequest.setStatus(RequestStatus.REJECTED);
    }

    @Test
    public void testGetPendingRequests() throws Exception {
        when(service.getPendingRequests()).thenReturn(Arrays.asList(pendingRequest));

        mockMvc.perform(get("/admin/deletion-requests/pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pendingRequest.getId()))
                .andExpect(jsonPath("$[0].userId").value(pendingRequest.getUserId()))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    public void testApproveRequest() throws Exception {
        when(service.approveRequest(anyLong())).thenReturn(approvedRequest);

        mockMvc.perform(post("/admin/deletion-requests/1/approve")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(approvedRequest.getId()))
                .andExpect(jsonPath("$.userId").value(approvedRequest.getUserId()))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    public void testRejectRequest() throws Exception {
        when(service.rejectRequest(anyLong())).thenReturn(rejectedRequest);

        mockMvc.perform(post("/admin/deletion-requests/1/reject")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rejectedRequest.getId()))
                .andExpect(jsonPath("$.userId").value(rejectedRequest.getUserId()))
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }
}