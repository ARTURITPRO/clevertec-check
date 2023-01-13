package edu.clevertec.check.pdf;

import edu.clevertec.check.service.OrderProcessingService;

import java.io.IOException;

/**
 * Used to print a receipt in PDF format.
 *
 * @version JDK 1.8
 * @author Artur Malashkov
 */
public interface CashReceiptPdfFilePrinter {

    /**
     * Used to print a receipt in PDF format.
     *
     * @param cashReceipt data for receipt generation and subsequent printing
     * @throws IOException Signals that an I/O exception to some sort has occurred.
     *
     */
    byte[] print(OrderProcessingService cashReceipt) throws IOException;

    String READ_FILE_PATH = "C:\\Users\\maste\\IdeaProjects\\7-clevertec-check-demo-20-12-2022 test\\clevertec-check\\clevertec.pdf";

}
