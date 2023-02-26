package edu.clevertec.check.controller;

import lombok.SneakyThrows;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class CheckControllerTest {

    private final RestTemplate restTemplate = new RestTemplate();

    private  Server server = new Server(6001);;


    @SneakyThrows
    @BeforeEach
    public  void startCustomServer()  {
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(CheckController.class, "/pdf");
        server.start();
    }

    @Test
    void doGetTest_statusCreated() {
        String resourceUrl = "http://localhost:6001/pdf?arguments=1-1 2-2 3-3 4-4 5-5 6-6 7-7 8-8 9-9 mastercard-11111";
        ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);

    }

    @SneakyThrows
    @AfterEach
    public void cleanup()  {
        server.stop();
    }
}