package edu.clevertec.check.controller;

import edu.clevertec.check.pdf.CashReceiptPdfFilePrinter;
import edu.clevertec.check.pdf.cashreceiptpdffileprinterimpl.CashReceiptPdfFilePrinterImpl;
import edu.clevertec.check.repository.impl.DiscountCardRepoImpl;
import edu.clevertec.check.repository.impl.ProductRepoImpl;
import edu.clevertec.check.service.OrderProcessingService;
import edu.clevertec.check.service.impl.DiscountCardServiceImpl;
import edu.clevertec.check.service.impl.OrderProcessingServiceImpl;
import edu.clevertec.check.service.impl.ProductServiceImpl;
import edu.clevertec.check.util.ConnectionManagerImpl;
import edu.clevertec.check.validation.impl.DataValidation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet({"/pdf"})
@Slf4j
public class CheckController extends HttpServlet {

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println(1);
        log.info("Input args: " + req.getParameter("arguments"));
        String storageArguments = req.getParameter("arguments");
        String[] split = storageArguments.split(" ");
        DataValidation.requiredNotEmptyArgs(split);
        OrderProcessingService resultProcessedData = new OrderProcessingServiceImpl(new ConnectionManagerImpl(),
                new ProductServiceImpl(new ProductRepoImpl()), split, new DiscountCardServiceImpl(new DiscountCardRepoImpl()));
        resultProcessedData.orderProcessing().formationOfCheck();
        System.out.println(2);

        try {
            System.out.println(3);
            CashReceiptPdfFilePrinter printer = new CashReceiptPdfFilePrinterImpl();
            final byte[] bytes = printer.print(resultProcessedData);
            System.out.println(4);
            resp.setContentType("application/pdf");
            resp.setStatus(201);
            resp.addHeader("Content-Disposition", "attachment; filename=");
            resp.setContentLength(bytes.length);
            resp.getOutputStream().write(bytes);
        } catch (Exception e) {
            resp.sendError(400, "Incorrect request: " + e.getMessage());
        }
    }
}
