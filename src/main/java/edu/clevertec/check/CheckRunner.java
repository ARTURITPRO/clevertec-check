package edu.clevertec.check;

import edu.clevertec.check.exception.DataException;
import edu.clevertec.check.pdf.CashReceiptPdfFilePrinter;
import edu.clevertec.check.pdf.cashreceiptpdffileprinterimpl.CashReceiptPdfFilePrinterImpl;
import edu.clevertec.check.repository.impl.DiscountCardRepoImpl;
import edu.clevertec.check.repository.impl.ProductRepoImpl;
import edu.clevertec.check.service.OrderProcessingService;
import edu.clevertec.check.service.OutputCheckService;
import edu.clevertec.check.service.impl.DiscountCardServiceImpl;
import edu.clevertec.check.service.impl.OrderProcessingServiceImpl;
import edu.clevertec.check.service.impl.ProductServiceImpl;
import edu.clevertec.check.util.ConnectionManagerImpl;
import edu.clevertec.check.validation.impl.DataValidation;
import edu.clevertec.check.validation.impl.FileValidationImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;

@Slf4j
public class CheckRunner {
    private static final String MESSAGE_DATA_INVALID = "The %s + is not valid. Check the correctness of the entered data.They must be entered " +
            "in regular expression format: %s Valid data example: 1-1 2-2 3-3 4-4 5-5 6-6 7-7 8-8 9-9 mastercard-11111" +
            " or enter the path of the source file, e.g. src\\main\\resources\\data.txt";

    /**
     * The main method for managing the application.
     *
     * @param args parameters of a set of products, their quantities and type of discount card, or path file.
     */
    public static void main(String[] args) {
        log.debug("Input args: " + String.join(" ", args));
        DataValidation.requiredNotEmptyArgs(args);

        if (DataValidation.isReadFromConsole(args)) {
            OrderProcessingService resultProcessedData = generateCheck(args);
            OutputCheckService.printCheckToConsole(resultProcessedData.getOrderFromCheck());
        } else if (DataValidation.isReadFromFile(args)) {
            OrderProcessingService resultProcessedData =
                    generateCheck(FileValidationImpl.FileUpLoad.loadStringsFromFile());
            OutputCheckService.printCheckToFile(new File("src/main/resources/receipt.txt"),
                    resultProcessedData.getOrderFromCheck());
        } else {
            throw new DataException((String.format(MESSAGE_DATA_INVALID,
                    Arrays.toString(args), DataValidation.PATTERN_DATA_VALIDATION_STRING_ARRAY)));
        }
    }

    /**
     * Designed for data processing.
     *
     * @param data parameters of a set of products, their quantities and type of discount card.
     * @return OrderProcessingService which contains processed data.
     */
    @SneakyThrows
    private static OrderProcessingService generateCheck(String[] data)  {

        OrderProcessingService resultProcessedData = new OrderProcessingServiceImpl(new ConnectionManagerImpl(), new ProductServiceImpl
                (new ProductRepoImpl()), data,  new DiscountCardServiceImpl(new DiscountCardRepoImpl()));
        resultProcessedData.orderProcessing();
        resultProcessedData.formationOfCheck();
        CashReceiptPdfFilePrinter printer = new CashReceiptPdfFilePrinterImpl();
        printer.print(resultProcessedData); //Print check to PDF file (in root project)
        return resultProcessedData;
    }
}


