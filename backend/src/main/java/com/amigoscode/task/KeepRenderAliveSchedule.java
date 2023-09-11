package com.amigoscode.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KeepRenderAliveSchedule {

    private final RestTemplate restTemplate;

    @Value("${custom.api.base.uri.backend}")
    private String baseUriBackend;

    @Value("${custom.api.base.uri.frontend}")
    private String baseUriFrontend;
    public KeepRenderAliveSchedule(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 780000L)
    public void pingBackendJob() {
        String userJson = restTemplate.getForObject(baseUriBackend + "/ping", String.class);

    }


    @Scheduled(fixedRate = 780000L)
    public void pingFrontendJob() {
        String userJson = restTemplate.getForObject(baseUriFrontend, String.class);

    }


}

