package edu.clevertec.check.service;

import edu.clevertec.check.dto.Market;
import edu.clevertec.check.service.impl.OrderProcessingServiceImpl;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Used to print a receipt in console or file.
 *
 * @author Artur Malashkov
 * @version JDK 1.8
 */
@Slf4j
public class OutputCheckService {

    /**
     * Used to record the full information of the check
     */
    private static StringBuilder readyCheck;

    /**
     * Print receipt to console.
     *
     * @param checkBody contains basic information about purchased products
     * @see OrderProcessingServiceImpl#getOrderFromCheck()
     */
    public static void printCheckToConsole(StringBuilder checkBody) {
        log.debug("checkBody: \n" + checkBody );
        readyCheck = getHeadLine().append(checkBody);
        log.info("Print check to console...");
        System.out.println(readyCheck);
    }

    /**
     * Print receipt to file.
     *
     * @param file this is the file to which the check will be written
     * @param checkBody contains basic information about purchased products.
     * @see OrderProcessingServiceImpl#getOrderFromCheck()
     */
    @SneakyThrows
    public static void printCheckToFile(File file, StringBuilder checkBody) {
        @Cleanup
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        log.debug("checkBody: \n" + checkBody );
        readyCheck = getHeadLine().append(checkBody);
        log.info("Print check to File...");
        bufferedWriter.write(readyCheck.toString());
    }

    /**
     * It is used to form the check header.
     *
     * @return a completed check that contains all the necessary information.
     */
    private static StringBuilder getHeadLine() {
        readyCheck = new StringBuilder();
        readyCheck.append("****************************************\n");
        readyCheck.append(String.format("%1s %20s %17s", "*", Market.NAME_MARKET, "*")).append("\n");
        readyCheck.append(String.format("%1s %24s %13s", "*", "EKE \"Centrail\"", " *")).append("\n");
        readyCheck.append(String.format("%1s %26s %11s", "*", Market.TELEPHONE_NUMBER, "*")).append("\n");
        readyCheck.append(String.format("%1s %27s", "ZWDN:304219", "CKHO:300030394")).append("\n");
        readyCheck.append(String.format("%1s %23s", "REGN:3100016076", "UNP:390286042")).append("\n");
        readyCheck.append(String.format("%1s %14s", "KASSA:0001 Change:000750", "DKH:000271821")).append("\n");
        readyCheck.append(String.format("%1s %20s", "31 Osipova Tatiana", "CHK:01/000000285")).append("\n");
        readyCheck.append(getCurrentTimeAsString()).append("\n");
        readyCheck.append("----------------------------------------\n");
        readyCheck.append(String.format("%-5s %-17s %5s %10s\n", "QTY", "DESCRIPTION", "PRICE", "TOTAL"));
        return readyCheck;
    }

    /**
     * To get the current date and time of receipt printing in the store.
     *
     * @return a string containing the current time and date.
     */
    private static String getCurrentTimeAsString() {
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        return "TIME  " + ZonedDateTime.now().format(formatterTime)
                + "\t\t   DATE  " + ZonedDateTime.now().format(formatterDate);
    }


}
