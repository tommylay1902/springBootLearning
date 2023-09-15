package com.amigoscode.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Service
public class KeepRenderAliveSchedule {

    private final RestTemplate restTemplate;
    private final Logger LOGGER = Logger.getLogger(KeepRenderAliveSchedule.class.getName());

    @Value("${custom.api.base.uri.backend}")
    private String baseUriBackend;

    @Value("${custom.api.base.uri.frontend}")
    private String baseUriFrontend;
    public KeepRenderAliveSchedule(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 780000L)
    public void pingBackendJob() {
        LOGGER.info("pinging backend");
        String userJson = restTemplate.getForObject(baseUriBackend + "/ping", String.class);

    }

    
    @Scheduled(fixedRate = 780000L)
    public void pingFrontendJob() {
        LOGGER.info("pinging frontend");
        String userJson = restTemplate.getForObject(baseUriFrontend, String.class);
    }


}

