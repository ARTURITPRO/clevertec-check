package edu.clevertec.check.service;

import edu.clevertec.check.service.impl.OrderProcessingServiceImplTest;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OutputCheckServiceTest {
    StringBuilder stringBuilder;
    StringBuilder stringBuilderFile;

    @BeforeEach
    void init (){
        stringBuilder = new StringBuilder();
        stringBuilder.append(
                "*     The item chicken is promotional\n" +
                "*     Its amount is more than 5\n" +
                "*     You get a 10% discount \n" +
                "*     The cost chicken will be:$2,7\n" +
                "*     Instead of $3.0        \n" +
                "5     chicken           $2,7       $13,5     \n" +
                "1     milk              $1.0       $1        \n" +
                "2     brot              $1.5       $3        \n" +
                "6     pizza             $3.5       $21       \n" +
                "*     The item pie is promotional\n" +
                "*     Its amount is more than 5\n" +
                "*     You get a 10% discount \n" +
                "*     The cost pie will be:$4,5\n" +
                "*     Instead of $5.0        \n" +
                "9     pie               $4,5       $40,5     \n" +
                "8     popcorn           $4.5       $36       \n" +
                "*     The item pudding is promotional\n" +
                "*     Its amount is more than 5\n" +
                "*     You get a 10% discount \n" +
                "*     The cost pudding will be:$3,6\n" +
                "*     Instead of $4.0        \n" +
                "7     pudding           $3,6       $25,2     \n" +
                "4     chocolate         $2.5       $10       \n" +
                "3     icecream          $2.0       $6        \n" +
                "----------------------------------------\n" +
                "#\t  DiscountCard mastercard number 11111\n" +
                "#\t  has been provided\n" +
                "#\t  Included 10% discount\n" +
                "----------------------------------------\n" +
                "####  Total cost:                 $140,58");
    }
    @Test
    void printCheckToConsole() {
                PrintStream stream = mock(PrintStream.class);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        System.setOut(stream);
        String params = stringBuilder.toString();
        System.out.println(params);
        verify(stream).println(captor.capture());
        assertEquals(captor.getValue(), params);
    }
    @SneakyThrows
    @Test
    void printCheckToFile() {
      OutputCheckService.printCheckToFile(new File("src/test/resources/receipt.txt"),stringBuilder);
        stringBuilderFile = new StringBuilder();
        try(FileReader reader = new FileReader("src/test/resources/receipt.txt"))
        {
            // читаем посимвольно
            int c;
            while((c=reader.read())!=-1){
                stringBuilderFile.append((char) c);
            }
        }
        stringBuilderFile.delete(0,442);
        assertEquals(stringBuilderFile.toString(), stringBuilder.toString());
    }
}