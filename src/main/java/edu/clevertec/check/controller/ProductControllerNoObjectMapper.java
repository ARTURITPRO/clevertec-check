package edu.clevertec.check.controller;

import com.google.gson.Gson;
import edu.clevertec.check.entity.Product;
import edu.clevertec.check.repository.impl.ProductRepoImpl;
import edu.clevertec.check.service.ProductService;
import edu.clevertec.check.service.impl.ProductServiceImpl;
import edu.clevertec.check.util.ConnectionManager;
import edu.clevertec.check.util.ConnectionManagerImpl;
import lombok.SneakyThrows;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/product")
public class ProductControllerNoObjectMapper extends HttpServlet {
    private ConnectionManager connectionManager = new ConnectionManagerImpl();
    private final ProductService<Integer, Product> productService = new ProductServiceImpl(new ProductRepoImpl());

    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Integer idProduct = Integer.parseInt(req.getParameter("idProduct"));
        String nameProduct = req.getParameter("name");
        Double priceProduct = Double.parseDouble(req.getParameter("cost"));
        Boolean isPromotional = Boolean.parseBoolean(req.getParameter("promotional"));
        Product product = Product.builder().id(idProduct).name(nameProduct).price(priceProduct)
                .isPromotional(isPromotional).build();
        productService.save(connectionManager, product);
        PrintWriter writer = resp.getWriter();
        String id = new Gson().toJson(product);
        writer.write(id);
        resp.setStatus(200);
    }

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("123");
        Integer idProduct = Integer.parseInt(req.getParameter("idProduct"));
        Product product = productService.findById(connectionManager, idProduct).get();
        PrintWriter writer = resp.getWriter();
        String id = new Gson().toJson(product);
        writer.write(id);
        resp.setStatus(200);
    }
}
