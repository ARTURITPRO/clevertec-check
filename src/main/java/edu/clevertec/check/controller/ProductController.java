package edu.clevertec.check.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import edu.clevertec.check.entity.Product;
import edu.clevertec.check.repository.impl.ProductRepoImpl;
import edu.clevertec.check.service.ProductService;
import edu.clevertec.check.service.impl.ProductServiceImpl;
import edu.clevertec.check.util.ReaderRequestBody;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet({"/products", "/products/*"})
@Slf4j
public class ProductController extends HttpServlet {

    private final ProductService<Integer, Product> productService = new ProductServiceImpl(new ProductRepoImpl());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Gson gsonMapper = new Gson();
    private Integer pageSize = 20;
    private Integer size = 1;
    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        log.info("Starting ProductController for GET request: {}", req.getRequestURI());

        String productsJson;

        if (req.getParameterMap().containsKey("pageSize") && req.getParameterMap().containsKey("size")) {
            pageSize = Integer.parseInt(req.getParameter("pageSize"));
            size = Integer.parseInt(req.getParameter("size"));
            productsJson = new ObjectMapper().writeValueAsString(productService.findAll(pageSize,size)
            );
        } else {
            String[] requestPath = req.getRequestURI().split("/");
            productsJson = new ObjectMapper().writeValueAsString(
                    req.getRequestURI().contains("/products/")
                            ? productService.findById(Integer.valueOf(requestPath[requestPath.length - 1])).get()
                            : productService.findAll(pageSize)
            );
        }


        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(productsJson);
        out.flush();

        log.info("Stopping ProductController for GET request: {}", req.getRequestURI());
    }

    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  {
        log.info("Starting ProductController for POST request: {}", req.getRequestURI());

        String requestBody = ReaderRequestBody.getRequestBody(req);
        log.info("Incoming JSON: {}", requestBody);

        Product product = gsonMapper.fromJson(requestBody, Product.class);
        productService.save(product);

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print("{\"id\": " + product.getId() + "}");
        out.flush();
        log.info("Stopping ProductController for POST request: {}", req.getRequestURI());
    }

    @Override
    @SneakyThrows
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)  {
        log.info("Starting ProductController for PUT request: {}", req.getRequestURI());

        String requestBody = ReaderRequestBody.getRequestBody(req);
        log.info("Incoming JSON: {}", requestBody);

        Product product = objectMapper.readValue(requestBody, Product.class);
        productService.update(product);

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print("{\"id\": " + product.getId() + "}");
        out.flush();

        log.info("Stopping ProductController for PUT request: {}", req.getRequestURI());
    }

    @Override
    @SneakyThrows
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)  {
        log.info("Starting ProductController for DELrequest: {}", req.getRequestURI());
        String[] requestPath = req.getRequestURI().split("/");
        boolean isRemove = productService.delete(Integer.valueOf(requestPath[requestPath.length - 1]));

        try (PrintWriter writer = resp.getWriter()) {
            if (isRemove) {
                String result = new Gson().toJson(true);
                writer.write(result);
                resp.setStatus(200);
            } else {
                resp.sendError(400, "Product not found.");
            }
        }
        log.info("Stopping  ProductController for DELrequest: {}", req.getRequestURI());
    }
}
