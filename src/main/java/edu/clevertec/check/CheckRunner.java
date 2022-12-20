package edu.clevertec.check;

import edu.clevertec.check.exception.DataException;
import edu.clevertec.check.pdf.CashReceiptPdfFilePrinter;
import edu.clevertec.check.pdf.cashreceiptpdffileprinterimpl.CashReceiptPdfFilePrinterImpl;
import edu.clevertec.check.service.OrderProcessingService;
import edu.clevertec.check.service.OutputCheckService;
import edu.clevertec.check.service.impl.OrderProcessingServiceImpl;
import edu.clevertec.check.validation.DataValidation;
import edu.clevertec.check.validation.impl.DataValidationImpl;
import edu.clevertec.check.validation.impl.FileValidationImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;

import static edu.clevertec.check.validation.DataValidation.MESSAGE_DATA_INVALID;
import static edu.clevertec.check.validation.DataValidation.PATTERN_DATA_VALIDATION_STRING_ARRAY;

@Slf4j
public class CheckRunner {
    /**
     * The main method for managing the application.
     *
     * @param args parameters of a set of products, their quantities and type of discount card, or path file.
     */
    public static void main(String[] args) {
        log.debug("Input args: " + String.join(" ", args));
        DataValidation dataValidation = new DataValidationImpl(args);
        dataValidation.isEmpty();
        // try - if the source data is an array of strings:
        boolean isStringMass = dataValidation.validationStringArray();
        // try - if the source data is a file:
        boolean isFile = dataValidation.validationFile();

        if (isStringMass) {
            OrderProcessingService resultProcessedData = formationCheckBody(args);
            OutputCheckService.printCheckToConsole(resultProcessedData.getOrderFromCheck());
        }
        if (isFile) {
            String[] stringMassFRomFile = FileValidationImpl.FileUpLoad.loadStringsFromFile();
            OrderProcessingService resultProcessedData = formationCheckBody(stringMassFRomFile);
            OutputCheckService.printCheckToFile(new File("src/main/resources/receipt.txt"),
                    resultProcessedData.getOrderFromCheck());
        }
        //If the file and string array is not found:
        if (!isFile ^ isStringMass) {
            throw new DataException((String.format(MESSAGE_DATA_INVALID,
                    Arrays.toString(args), PATTERN_DATA_VALIDATION_STRING_ARRAY)));
        }
    }

    /**
     * Designed for data processing.
     *
     * @param data parameters of a set of products, their quantities and type of discount card.
     * @return OrderProcessingService which contains processed data.
     */
    private static OrderProcessingService formationCheckBody(String[] data) {
        OrderProcessingService resultProcessedData = new OrderProcessingServiceImpl(data);
        resultProcessedData.orderProcessing().formationOfCheck();
        CashReceiptPdfFilePrinter printer = new CashReceiptPdfFilePrinterImpl();
        printer.print(resultProcessedData); //Print check to PDF file (in root project)
        return resultProcessedData;
    }
}


