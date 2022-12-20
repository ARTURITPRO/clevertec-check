package edu.clevertec.check.pdf.cashreceiptpdffileprinterimpl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import edu.clevertec.check.dto.Market;
import edu.clevertec.check.entity.Product;
import edu.clevertec.check.pdf.CashReceiptPdfFilePrinter;
import edu.clevertec.check.service.OrderProcessingService;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Used to print a receipt in PDF format.
 *
 * @version JDK 1.8
 * @author Artur Malashkov
 */
public class CashReceiptPdfFilePrinterImpl implements CashReceiptPdfFilePrinter {

    /**
     * Print a receipt in PDF format
     * <p>
     * Еhe method collects all the necessary data for printing a check in the format PDF.
     * </p>
     *
     * @param orderProcessingService a class object that contains all the necessary information.
     *                        {@link OrderProcessingService}
     * @see PdfDocument#addNewPage()
     * @see Document#add(IBlockElement)
     * @see PdfCanvas#addXObjectAt(PdfXObject, float, float)
     * @see PdfFormXObject
     */
    @SneakyThrows
    @Override
    public void print(OrderProcessingService orderProcessingService) {

        /** The path to the background file. */
        String READ_FILE_PATH = "src\\main\\resources\\template\\clevertec.pdf";
        @Cleanup
        PdfDocument backgroundPdfDocument = new PdfDocument(new PdfReader(READ_FILE_PATH));
        /** The path where the check will be written in PDF format. */
        String PATH_RECORD_FILE = "checkOfSupermarket.pdf";
        @Cleanup
        PdfDocument checkPdfDocument = new PdfDocument(new PdfWriter(PATH_RECORD_FILE));
        @Cleanup
        Document document = new Document(checkPdfDocument);

        checkPdfDocument.addNewPage();
        document.add(getHeadingTable());
        document.add(getProductTable(orderProcessingService));
        document.add(getResultTable(orderProcessingService));

        PdfCanvas PdfCanvas = new PdfCanvas(checkPdfDocument.getFirstPage().newContentStreamBefore(),
                checkPdfDocument.getFirstPage().getResources(), checkPdfDocument);

        PdfFormXObject pdfFormXObject = backgroundPdfDocument.getFirstPage().copyAsFormXObject(checkPdfDocument);
        PdfCanvas.addXObjectAt(pdfFormXObject, 0, 0);
    }

    /**
     * Forms the Heading Table.
     * <p>
     * Forms the upper part of the check, i.e. header.
     * </p>
     *
     * @return ready-made formatted table containing the upper part of the receipt.
     * @see OrderProcessingService
     * @see Cell
     * @see Table#addCell(Cell)
     * @see Table#setTextAlignment(TextAlignment)
     */
    private static Table getHeadingTable() {
        Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth().setMarginTop(100);
        table.setHorizontalAlignment(HorizontalAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setWidth(UnitValue.createPercentValue(100))
                .setFontSize(16f);
        Cell cashReceipt = new Cell(1, 5).add(new Paragraph("Storage \"Dionis17\" "));
        Cell quentity = new Cell(1, 1).add(new Paragraph("QTY"));
        Cell description = new Cell(1, 1).add(new Paragraph("DESCRIPTION"));
        Cell price = new Cell(1, 1).add(new Paragraph("PRICE"));
        Cell total = new Cell(1, 1).add(new Paragraph("TOTAL"));
        table.addCell(cashReceipt);
        table.addCell(quentity);
        table.addCell(description);
        table.addCell(price);
        table.addCell(total);
        return table.setTextAlignment(TextAlignment.CENTER);
    }

    /**
     * Formation of the main part of the table.
     * <p>
     * Formation of the main part of the check, consisting of information about purchased products
     * {@link  OrderProcessingService#getCustomShoppingList() contains products and quantities }
     * </p>
     *
     * @param orderProcessingService a class object that contains all the necessary information.
     *                        {@link OrderProcessingService}
     * @return of the main part of the table {@link Table#setTextAlignment(TextAlignment)}
     * @see OrderProcessingService
     * @see Cell#add(IBlockElement)
     * @see Table#addCell(Cell)
     */
    private static Table getProductTable(OrderProcessingService orderProcessingService) {
        Map<Product, Integer> mapProduct = orderProcessingService.getCustomShoppingList();
        Table tableProducts = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();

        for (Map.Entry<Product, Integer> product : mapProduct.entrySet()) {
            Cell qty = new Cell(1, 1).add(new Paragraph(String.valueOf(product.getValue())));
            Cell description = new Cell(1, 1).add(new Paragraph(String.valueOf(product.getKey().getName())));
            Cell price = new Cell(1, 1).add(new Paragraph(String.valueOf(product.getKey().getPrice())));
            Cell total = new Cell(1, 1).add(new Paragraph(String.valueOf(CashReceiptPdfFilePrinterImpl.
                    roundedDouble((product.getKey().getPrice()) * (product.getValue())))));
            tableProducts.addCell(qty);
            tableProducts.addCell(description);
            tableProducts.addCell(price);
            tableProducts.addCell(total);
        }

        return tableProducts.setTextAlignment(TextAlignment.CENTER);
    }

    /**
     * Formation of the main part of the table.
     * <p>
     * Forms the bottom of the table.
     * {@link  OrderProcessingService#getCustomShoppingList() contains products and quantities }
     * </p>
     *
     * @param orderProcessingService a class object that contains all the necessary information.
     *                        {@link OrderProcessingService}
     * @return of the main part of the table {@link Table#setTextAlignment(TextAlignment)}
     * @see Cell#add(IBlockElement)
     * @see Table#addCell(Cell)
     */
    private static Table getResultTable(OrderProcessingService orderProcessingService) {
        Table totalPurchase = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        Cell niceDay = new Cell(1, 1).add(new Paragraph("Have a nice day !"));
        Cell phoneNumber = new Cell(1, 1).add(new Paragraph("Phone number: " + Market.TELEPHONE_NUMBER));
        Cell totalPrice = new Cell(2, 2).add(new Paragraph("Total price: " + CashReceiptPdfFilePrinterImpl.
                roundedDouble(orderProcessingService.getTotalCost())));

        totalPurchase.addCell(totalPrice);
        totalPurchase.addCell(phoneNumber);
        totalPurchase.addCell(niceDay);

        return totalPurchase.setTextAlignment(TextAlignment.CENTER);
    }

    /**
     * Rounded double
     * <p>
     * The method takes a floating point number and rounds it to 2 decimal places.
     * </p>
     *
     * @param value floating point number to be rounded to 2 decimal places
     * @return number that is rounded to 2 decimal places
     * @see MathContext
     * @see BigDecimal#setScale(int, RoundingMode)
     */
    private static double roundedDouble(double value) {
        MathContext mathContext = new MathContext(15, RoundingMode.HALF_UP);
        BigDecimal bigDecimal = new BigDecimal(value, mathContext);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.DOWN);
        value = bigDecimal.doubleValue();
        return value;
    }
}
