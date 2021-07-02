package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url) {
        this.baseUrl = url;
    }


    public BigDecimal getBalance(String token) {

        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "account/balance", HttpMethod.GET, makeAuthEntity(token), BigDecimal.class);
            return response.getBody();
        } catch (RestClientResponseException ex) {
            String message = createRegisterExceptionMessage(ex);
            System.out.println(message);
        }

        return BigDecimal.ZERO;
    }

    public String[] getUsers(String token) {
        try {
            ResponseEntity<String[]> response = restTemplate.exchange(baseUrl + "account/users", HttpMethod.GET, makeAuthEntity(token), String[].class);
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


}
