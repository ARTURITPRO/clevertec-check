package edu.clevertec.check.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.clevertec.check.entity.DiscountCard;
import edu.clevertec.check.util.ConnectionManagerImpl;
import lombok.SneakyThrows;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("IntegrationTest")
class DiscountCardControllerTest {

    private static ScriptRunner scriptRunner;

    private static Connection connection;

    private static Reader reader;

    private static ConnectionManagerImpl connectionManager = new ConnectionManagerImpl();

    private final ObjectMapper mapper = new ObjectMapper();

    private final RestTemplate restTemplate = new RestTemplate();

    private Server server = new Server(6001);;

    @SneakyThrows
    @BeforeEach
    public  void startCustomServer()  {
        connection = connectionManager.get();
        scriptRunner = new ScriptRunner(connection);
        reader = new BufferedReader(new FileReader("src/test/resources/db.migration/test.sql"));

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(DiscountCardController.class, "/cards/2");
        server.start();
    }

    @Nested
    class GetTest{
        @Test
        void doGet_statusOK() {
        String resourceUrl = "http://localhost:6001/cards/2";
        ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        }

        @SneakyThrows
        @Test
        void doGet_status()  {
            String resourceUrl = "http://localhost:6001/cards/2";
            ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl, String.class);

            assertNotNull(mapper.readValue(response.getBody(), DiscountCard.class));
        }
    }

    @Test
    void doPostTest_statusOk() {
        String resourceUrl = "http://localhost:6001/cards/2";
        DiscountCard mastercard2 = DiscountCard.builder().id(2).name("mastercard").discount(20).number(22222).build();

        HttpEntity<DiscountCard> entity = new HttpEntity<>(mastercard2);
        ResponseEntity<DiscountCard> response = restTemplate.exchange(resourceUrl, HttpMethod.POST, entity, DiscountCard.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @SneakyThrows
    @Test
    void doPut() {
        String resourceUrl = "http://localhost:6001/cards/2";
        ResponseEntity<String> responseGet = restTemplate.getForEntity(resourceUrl, String.class);
        String json = responseGet.getBody();
        DiscountCard discountCard = mapper.readValue(json, DiscountCard.class);
        discountCard.setName("goldCard");

        // put changed film
        HttpEntity<DiscountCard> entity = new HttpEntity<>(discountCard);
        ResponseEntity<DiscountCard> response = restTemplate.exchange(resourceUrl, HttpMethod.PUT, entity, DiscountCard.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void doDelete() {
        String resourceUrl = "http://localhost:6001/cards/2";
        ResponseEntity<String> response = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, null, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @SneakyThrows
    @AfterEach
    public void cleanup()  {
        System.out.println(" @AfterEach");
        connection = connectionManager.get();
        scriptRunner = new ScriptRunner(connection);
        reader = new BufferedReader(new FileReader("src/test/resources/db.migration/test.sql"));
        scriptRunner.runScript(reader);
        server.stop();
    }
}
