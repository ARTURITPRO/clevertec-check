package edu.clevertec.check.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import edu.clevertec.check.entity.DiscountCard;
import edu.clevertec.check.repository.impl.DiscountCardRepoImpl;
import edu.clevertec.check.service.DiscountCardService;
import edu.clevertec.check.service.impl.DiscountCardServiceImpl;
import edu.clevertec.check.util.ConnectionManager;
import edu.clevertec.check.util.ConnectionManagerImpl;
import edu.clevertec.check.util.ReaderRequestBody;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet({"/cards", "/cards/*"})
@Slf4j
public class DiscountCardController extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Gson gsonMapper = new Gson();
    private final DiscountCardService <Integer,DiscountCard>
            cardRepo = new DiscountCardServiceImpl(new DiscountCardRepoImpl());
    private Integer pageSize = 20;
    private Integer size = 1;
    private final ConnectionManager connectionManager = new ConnectionManagerImpl();

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        log.info("Starting DiscountCardController for GET request: {}", req.getRequestURI());

        String productsJson;

        if (req.getParameterMap().containsKey("pageSize") && req.getParameterMap().containsKey("size")) {
            pageSize = Integer.parseInt(req.getParameter("pageSize"));
            size = Integer.parseInt(req.getParameter("size"));
            productsJson = new ObjectMapper().writeValueAsString(cardRepo.findAll(connectionManager, pageSize, size));
        } else {
            String[] requestPath = req.getRequestURI().split("/");
            productsJson = new ObjectMapper().writeValueAsString(
                    req.getRequestURI().contains("/cards/")
                            ? cardRepo.findById(connectionManager, Integer.valueOf(requestPath[requestPath.length - 1])).get()
                            : cardRepo.findAll(connectionManager, pageSize));
        }

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(productsJson);
        out.flush();

        log.info("Stopping DiscountCardController for GET request: {}", req.getRequestURI());
    }

    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        log.info("Starting DiscountCardController for POST request: {}", req.getRequestURI());

        String requestBody = ReaderRequestBody.getRequestBody(req);
        log.info("Incoming JSON: {}", requestBody);

        DiscountCard discountCard = gsonMapper.fromJson(requestBody, DiscountCard.class);
        cardRepo.save(connectionManager, discountCard);

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print("{\"id\": " + discountCard.getId() + "}");
        out.flush();
        log.info("Stopping DiscountCardController for POST request: {}", req.getRequestURI());
    }

    @Override
    @SneakyThrows
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)  {
        log.info("Starting DiscountCardController for PUT request: {}", req.getRequestURI());

        String requestBody = ReaderRequestBody.getRequestBody(req);
        log.info("Incoming JSON: {}", requestBody);

        DiscountCard discountCard = gsonMapper.fromJson(requestBody, DiscountCard.class);
        cardRepo.update(connectionManager, discountCard);

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print("{\"id\": " + discountCard.getId() + "}");
        out.flush();

        log.info("Stopping DiscountCardController for PUT request: {}", req.getRequestURI());
    }

    @Override
    @SneakyThrows
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)  {
        log.info("Starting DiscountCardController for DEL request: {}", req.getRequestURI());
        String[] requestPath = req.getRequestURI().split("/");
        boolean isRemove = cardRepo.delete(connectionManager, Integer.valueOf(requestPath[requestPath.length - 1]));

        try (PrintWriter writer = resp.getWriter()) {
            if (isRemove) {
                String result = new Gson().toJson(true);
                writer.write(result);
                resp.setStatus(200);
            } else {
                resp.sendError(400, "DiscountCard not found.");
            }
        }
        log.info("Stopping  DiscountCardController for DEL request: {}", req.getRequestURI());
    }

}
