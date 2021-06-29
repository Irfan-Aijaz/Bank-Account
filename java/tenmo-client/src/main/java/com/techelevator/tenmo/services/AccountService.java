package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class AccountService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    public static String AUTH_TOKEN = "";

    public AccountService(String url) {
        this.baseUrl = url;
    }


    private HttpEntity<String> createRequestEntity(User credentials) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(credentials.getUsername(), headers);
        return entity;
    }

    public ResponseEntity<BigDecimal> viewCurrentBalance(User credentials) throws AuthenticationServiceException {
        HttpEntity<String> entity = new HttpEntity(credentials);
        try {
            return restTemplate.exchange(baseUrl + "account/balance", HttpMethod.GET, entity, BigDecimal.class);
        } catch(RestClientResponseException ex) {
            String message = createRegisterExceptionMessage(ex);
            throw new AuthenticationServiceException(message);
        }
    }

    private String createRegisterExceptionMessage(RestClientResponseException ex) {
        String message = null;
        if (ex.getRawStatusCode() == 400 && ex.getResponseBodyAsString().length() == 0) {
            message = ex.getRawStatusCode() + " : {\"timestamp\":\"" + LocalDateTime.now() + "+00:00\",\"status\":400,\"error\":\"Invalid credentials\",\"message\":\"Registration failed: Invalid username or password\",\"path\":\"/register\"}";
        }
        else {
            message = ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString();
        }
        return message;
    }

    @SuppressWarnings("rawtypes")
    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }



}