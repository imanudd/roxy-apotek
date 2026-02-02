package helper;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfGenerator {

    public static boolean generateFormalReport(String title, String[] headers, List<Object[]> data, String fileName) {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // 1. Header Image
            try {
                // Try to load the resource as an InputStream for better reliability
                java.io.InputStream imgStream = PdfGenerator.class.getResourceAsStream("/assets/kop.png");
                if (imgStream == null) {
                    imgStream = PdfGenerator.class.getClassLoader().getResourceAsStream("assets/kop.png");
                }

                if (imgStream != null) {
                    byte[] bytes = imgStream.readAllBytes();
                    Image headerImg = Image.getInstance(bytes);
                    headerImg.scaleToFit(PageSize.A4.getWidth() - 72, 100);
                    headerImg.setAlignment(Image.ALIGN_CENTER);
                    document.add(headerImg);
                    imgStream.close();
                } else {
                    throw new IOException("Kop image resource not found in classpath (/assets/kop.png)");
                }
            } catch (Exception e) {
                System.err.println("Header image not found: " + e.getMessage());
                // If image fails, just add a text header
                Paragraph fallbackHeader = new Paragraph("PT. KREANOVA PHARMARET",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
                fallbackHeader.setAlignment(Element.ALIGN_CENTER);
                document.add(fallbackHeader);
            }

            document.add(new Paragraph("\n"));

            // 2. Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph pTitle = new Paragraph(title.toUpperCase(), titleFont);
            pTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(pTitle);

            // Date and Quote (Formal sentence)
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            java.util.Locale localeId = java.util.Locale.forLanguageTag("id-ID");
            String topDate = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm", localeId));
            Paragraph pDate = new Paragraph("Laporan ini dibuat pada tanggal: " + topDate, normalFont);
            pDate.setAlignment(Element.ALIGN_CENTER);
            document.add(pDate);

            document.add(new Paragraph("\n"));

            Paragraph pFormal = new Paragraph(
                    "Berikut adalah data laporan hasil inventarisasi apotek yang sah dan dapat dipertanggungjawabkan.",
                    normalFont);
            pFormal.setSpacingAfter(10f);
            document.add(pFormal);

            // 3. Table
            PdfPTable table = new PdfPTable(headers.length);
            table.setWidthPercentage(100);

            // Header Cells
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new Color(0, 153, 153)); // Teal match with app
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Data Cells
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            for (Object[] row : data) {
                for (Object cellData : row) {
                    PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(cellData), dataFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(5);
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.add(new Paragraph("\n\n"));

            // 4. Footer / Signatures
            PdfPTable footerTable = new PdfPTable(2);
            footerTable.setWidthPercentage(100);
            footerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            // Left side (empty or info)
            footerTable.addCell("");

            // Right side (Signature)
            PdfPCell sigCell = new PdfPCell();
            sigCell.setBorder(Rectangle.NO_BORDER);
            sigCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            // Using the already declared Indonesian locale for the footer date
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", localeId));

            Paragraph pSigDate = new Paragraph("Jakarta, " + today + "\nMengetahui,\nManager\n\n\n\n",
                    normalFont);
            pSigDate.setAlignment(Element.ALIGN_CENTER);
            sigCell.addElement(pSigDate);

            Paragraph pUserName = new Paragraph("Achmad Rizal",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10));
            pUserName.setAlignment(Element.ALIGN_CENTER);
            sigCell.addElement(pUserName);

            footerTable.addCell(sigCell);
            document.add(footerTable);

            document.close();
            return true;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
