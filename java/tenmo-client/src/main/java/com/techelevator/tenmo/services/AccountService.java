package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.TransferStatusDTO;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url) {
        this.baseUrl = url;
    }


    public BigDecimal getBalance(String token) {

        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "account/user/balance", HttpMethod.GET, makeAuthEntity(token), BigDecimal.class);
            return response.getBody();
        } catch (RestClientResponseException ex) {
            String message = createRegisterExceptionMessage(ex);
            System.out.println(message);
        }

        return BigDecimal.ZERO;
    }


    public User[] getUsers(String token) {
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "account/user/all_users", HttpMethod.GET, makeAuthEntity(token), User[].class);
            return response.getBody();
        } catch (RestClientResponseException ex) {
            String message = createRegisterExceptionMessage(ex);
            System.out.println(message);
        }
        return null;
    }

    public TransferDTO[] getTransfers(String token) {
        try {
            ResponseEntity<TransferDTO[]> response = restTemplate.exchange(baseUrl + "transfer/transfer_history", HttpMethod.GET, makeAuthEntity(token), TransferDTO[].class);
            return response.getBody();
        } catch (RestClientResponseException ex) {
            String message = createRegisterExceptionMessage(ex);
            System.out.println(message);
        }
        return null;
    }

    public String createTransfer(String token, int transferType, int transferStatus, int userIdFrom, int userIdTo, BigDecimal transferAmount) {
        // takes all parts of transfer to create one for database
        HttpEntity entity = makeAuthEntityTransfer(token, transferType, transferStatus, userIdFrom, userIdTo, transferAmount);
        try {
            return restTemplate.postForObject(baseUrl + "transfer/create_transfer", entity, String.class);
        } catch (RestClientResponseException ex) {
            String message = createRegisterExceptionMessage(ex);
            System.out.println(message);
        }
        return null;
    }

    public String updatePendingTransfer(String token, int transferId, int transferStatus) {
        // takes transfer ID and updates status
        HttpEntity entity = makeAuthEntityTransferStatusUpdate(token, transferId, transferStatus);
        try {
            restTemplate.put(baseUrl + "transfer/update_transfer", entity);
            return "Transfer updated.";
        } catch (RestClientResponseException ex) {
            String message = createRegisterExceptionMessage(ex);
            System.out.println(message);
        }
        return "";
    }


    private String createRegisterExceptionMessage(RestClientResponseException ex) {
        String message = null;
        if (ex.getRawStatusCode() == 400 && ex.getResponseBodyAsString().length() == 0) {
            message = ex.getRawStatusCode() + " : {\"timestamp\":\"" + LocalDateTime.now() + "+00:00\",\"status\":400,\"error\":\"Invalid credentials\",\"message\":\"Registration failed: Invalid username or password\",\"path\":\"/register\"}";
        } else {
            message = ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString();
        }
        return message;
    }


    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    private HttpEntity makeAuthEntityTransfer(String token, int transferType, int transferStatus, int userIdFrom, int userIdTo, BigDecimal transferAmount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        TransferDTO transfer = new TransferDTO(transferType, transferStatus, userIdFrom, userIdTo, transferAmount);
        HttpEntity entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

    private HttpEntity makeAuthEntityTransferStatusUpdate(String token, int transferId, int transferStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        TransferStatusDTO transferStatusUpdate = new TransferStatusDTO(transferId, transferStatus);
        HttpEntity entity = new HttpEntity<>(transferStatusUpdate, headers);
        return entity;
    }

}
