package com.mazanenko.petproject.bookshop.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazanenko.petproject.bookshop.service.RESTConsumerForManagerEmail;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RESTConsumerForManagerEmailImplForYandexMail implements RESTConsumerForManagerEmail {

    private final String domain = "booksland.shop";

    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders headers = new HttpHeaders();
    private final HttpEntity<String> entity = new HttpEntity<>((headers));
    private final ObjectMapper objectMapper = new ObjectMapper();

    {
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("PDDToken", "DFESO3QKDZGJO7OJKYJNRENR7AFWJMSZXJUNOTYUFJM7AYDRBWGQ");
    }

    @Override
    public void createEmail(String email, String password) throws Exception {
        if (email == null || password == null) {
            throw new Exception("Email or password is null");
        }

        Map<String, String> param = setParam(email, password);
        String encodedURL = encodeURL(param, "/api2/admin/email/add");
        String response = restTemplate.postForObject(encodedURL, entity, String.class);

        try {
            Map<String, String> map = objectMapper.readValue(response, new TypeReference<>() {});

            if (map.get("success").equals("error")) {
                throw new Exception("Error while trying registered email " + email
                        + "\n" + "Error code: " + map.get("error"));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changePassword(String email, String newPassword) throws Exception {
        if (email == null || newPassword == null) {
            throw new Exception("Email or new password is null");
        }

        Map<String, String> param = setParam(email, newPassword);
        String encodedURL = encodeURL(param, "/api2/admin/email/edit");
        String response = restTemplate.postForObject(encodedURL, entity, String.class);

        try {
            Map<String, Object> map = objectMapper.readValue(response, new TypeReference<>() {});

            if (map.get("success").equals("error")) {
                throw new Exception("Error while trying to change password for email " + email
                        + "\n" + "Error code: " + map.get("error"));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEmail(String email) throws Exception {
        if (email == null) {
            throw new Exception("Email is null");
        }

        String login = email.substring(0, email.indexOf('@'));

        Map<String, String> param = new HashMap<>();
        param.put("domain", domain);
        param.put("login", login);

        String encodedURL = encodeURL(param, "/api2/admin/email/del");
        String response = restTemplate.postForObject(encodedURL, entity, String.class);

        try {
            Map<String, String> map = objectMapper.readValue(response, new TypeReference<>() {});

            if (map.get("success").equals("error")) {
                throw new Exception("Error while trying to delete email " + email
                        + "\n" + "Error code: " + map.get("error"));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    private String encodeURL(Map<String, String> param, String URN) {
        String host = "https://pddimp.yandex.ru";

        return param.keySet().stream().map(key -> key + "=" + URLEncoder.encode(param.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&", host + URN + "?", ""));
    }

    private Map<String, String> setParam(String email, String password) {
        String login = email.substring(0, email.indexOf('@'));

        Map<String, String> param = new HashMap<>();
        param.put("domain", domain);
        param.put("login", login);
        param.put("password", password);

        return param;
    }
}
