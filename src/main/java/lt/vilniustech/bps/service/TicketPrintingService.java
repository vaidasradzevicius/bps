package lt.vilniustech.bps.service;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lt.vilniustech.bps.dto.Order;
import lt.vilniustech.bps.dto.Ticket;
import lt.vilniustech.bps.util.TicketPriceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class TicketPrintingService {
    private static final Logger logger = LoggerFactory.getLogger(TicketPrintingService.class);

    private static final String FILENAME_PREFIX = "/BPS bilietai ";
    private static final String FILENAME_EXTENSION = ".pdf";

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter dateFormatForFilename = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private Font headingFont;
    private Font normalFont;

    public TicketPrintingService() {
        String robotoFontPath = Objects.requireNonNull(TicketPrintingService.class
                .getResource("/fonts/roboto/Roboto-Regular.ttf")).toString();
        String robotoBoldFontPath = Objects.requireNonNull(TicketPrintingService.class
                .getResource("/fonts/roboto/Roboto-Bold.ttf")).toString();

        try {
            BaseFont robotoFont = BaseFont.createFont(robotoFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont robotoBoldFont = BaseFont.createFont(robotoBoldFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            headingFont = new Font(robotoBoldFont, 18);
            normalFont = new Font(robotoFont, 12);
        } catch (DocumentException | IOException e) {
            logger.error("Could not initialize a font", e);
        }
    }

    public void generateTicketsPdf(Order order, List<Ticket> tickets, String pathToSaveFileIn) throws DocumentException, IOException {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(createFileName(pathToSaveFileIn)));
        doc.open();

        addHeadingToDocument(doc);
        addOrderInformationToDocument(doc, order);
        addTicketsInformationToDocument(doc, tickets);

        doc.close();
    }

    private void addHeadingToDocument(Document doc) throws DocumentException, IOException {
        URL logoUrl = getClass().getResource("/img/BPS_logo.png");
        if (logoUrl == null) {
            throw new IOException("Logo image was not found.");
        }

        Image logoImage = Image.getInstance(logoUrl);
        logoImage.setAlignment(Element.ALIGN_CENTER);
        logoImage.scaleAbsolute(80, 80);
        doc.add(logoImage);
        Paragraph headerParagraph = createParagraph("BPS bilietai", 20, headingFont);
        headerParagraph.setAlignment(Element.ALIGN_CENTER);
        doc.add(headerParagraph);
    }

    private void addOrderInformationToDocument(Document doc, Order order) throws DocumentException {
        String orderDate = dateFormat.format(order.getDateTimeOrdered());
        String orderPrice = TicketPriceUtil.formatPrice(order.getTotalPrice());
        doc.add(createParagraph("Pirkimo data ir laikas: " + orderDate));
        doc.add(createParagraph("Mokėtojas: " + order.getBuyer().getFullName()));
        doc.add(createParagraph("Suma: " + orderPrice + " €"));
    }

    private void addTicketsInformationToDocument(Document doc, List<Ticket> tickets) throws DocumentException,
            IOException {
        for (Ticket ticket : tickets) {
            String ticketPriceText = TicketPriceUtil.formatPrice(ticket.getPrice()) + getTicketPriceTypeText(ticket);

            PdfPTable table = new PdfPTable(2);
            table.setSpacingBefore(15);
            table.setSpacingAfter(15);

            table.addCell(new Phrase("Maršrutas", normalFont));
            table.addCell(new Phrase(ticket.getRoute().getName(), normalFont));
            table.addCell(new Phrase("Pradinė stotelė", normalFont));
            table.addCell(new Phrase(ticket.getStartStation().getStopName(), normalFont));
            table.addCell(new Phrase("Galutinė stotelė", normalFont));
            table.addCell(new Phrase(ticket.getDestinationStation().getStopName(), normalFont));
            table.addCell(new Phrase("Bilieto kaina", normalFont));
            table.addCell(new Phrase(ticketPriceText, normalFont));
            table.addCell(createCellForQrCode());

            doc.add(table);
        }
    }

    private String getTicketPriceTypeText(Ticket ticket) {
        if (ticket.isLoweredPrice()) {
            return " (su nuolaida)";
        }

        return " (pilna kaina)";
    }

    private Paragraph createParagraph(String text) {
        return createParagraph(text, 5, normalFont);
    }

    private Paragraph createParagraph(String text, int spacing, Font font) {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setSpacingBefore(spacing);
        paragraph.setSpacingAfter(spacing);

        return paragraph;
    }

    private String createFileName(String pathToSaveFileIn) {
        return pathToSaveFileIn + FILENAME_PREFIX + dateFormatForFilename.format(LocalDateTime.now()) + FILENAME_EXTENSION;
    }

    private Image getQrCodeImage() throws BadElementException, IOException {
        URL imgUrl = getClass().getResource("/img/QR-code.png");
        if (imgUrl == null) {
            logger.error("QR code image was not found.");
            return null;
        }

        Image qrImage = Image.getInstance(imgUrl);
        qrImage.scaleAbsolute(50, 50);
        qrImage.setAlignment(Element.ALIGN_CENTER);

        return qrImage;
    }

    private PdfPCell createCellForQrCode() throws BadElementException, IOException {
        PdfPCell qrCodeCell = new PdfPCell(getQrCodeImage(), false);
        qrCodeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        qrCodeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        qrCodeCell.setColspan(2);
        qrCodeCell.setFixedHeight(70);

        return qrCodeCell;
    }

}
