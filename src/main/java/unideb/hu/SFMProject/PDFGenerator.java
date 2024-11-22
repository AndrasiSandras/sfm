package unideb.hu.SFMProject;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.properties.TextAlignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;

public class PDFGenerator {

    public static void generateReport() {
        // Célkönyvtár és fájl elérési útvonal
        String directoryPath = "./src/main/resources/Reports";
        String filePath = directoryPath + "/report.pdf";

        // Könyvtár ellenőrzése és létrehozása, ha nem létezik
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                System.out.println("Nem sikerült létrehozni a célkönyvtárat: " + directoryPath);
                return;
            }
        }

        // Adatbázis kapcsolat beállítása
        String url = "jdbc:h2:file:./src/main/resources/Database/my_database"; // Adatbázis URL
        String user = "sa"; // Felhasználónév
        String password = ""; // Jelszó

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // SQL lekérdezés
            String sql = "SELECT Inout, TransactionID, starterName, Product FROM Report";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // PDF Dokumentum létrehozása
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Háttérszín beállítása (RGB: 58,71,80)
            PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage()); // Új oldal hozzáadása
            canvas.setFillColor(new DeviceRgb(58, 71, 80)); // Háttérszín beállítása
            float width = pdfDoc.getPage(1).getPageSize().getWidth(); // Első oldal szélessége
            float height = pdfDoc.getPage(1).getPageSize().getHeight(); // Első oldal magassága
            canvas.rectangle(0, 0, width, height);
            canvas.fill();

            // Kép beszúrása a dokumentum elejére
            String imagePath = "./src/main/resources/image/palacklogo.png"; // Kép elérési útja
            Image img = new Image(ImageDataFactory.create(imagePath));
            img.setFixedPosition(50, 750); // A kép pozíciója a dokumentumban (x, y)
            img.scaleToFit(100, 100); // Kép méretének beállítása
            document.add(img);

            // Dokumentum margók beállítása a táblázat számára
            document.setMargins(50, 20, 20, 20); // A táblázatnak nagyobb margóval

            // Fejléc stílusa
            Paragraph header = new Paragraph("Styled PDF Report")
                    .setFontSize(18)
                    .setFontColor(new DeviceRgb(0, 102, 204)) // Kék szín
                    .setBold() // Félkövér
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(header);

            // Táblázat létrehozása
            float[] columnWidths = {100, 200, 200, 200};
            Table table = new Table(columnWidths).setWidth(100);

            // Táblázat fejlécek stílusa
            table.addCell(new Cell().add(new Paragraph("INOUT"))
                    .setBackgroundColor(new DeviceRgb(234,146,21))
                    .setFontColor(new DeviceRgb(255, 255, 255)) // Fehér szöveg
                    .setBold());
            table.addCell(new Cell().add(new Paragraph("TransactionID"))
                    .setBackgroundColor(new DeviceRgb(234,146,21))
                    .setFontColor(new DeviceRgb(255, 255, 255)) // Fehér szöveg
                    .setBold());
            table.addCell(new Cell().add(new Paragraph("Starter Name"))
                    .setBackgroundColor(new DeviceRgb(234,146,21))
                    .setFontColor(new DeviceRgb(255, 255, 255)) // Fehér szöveg
                    .setBold());
            table.addCell(new Cell().add(new Paragraph("Product"))
                    .setBackgroundColor(new DeviceRgb(234,146,21))
                    .setFontColor(new DeviceRgb(255, 255, 255)) // Fehér szöveg
                    .setBold());

            // Adatok hozzáadása a táblázathoz
            while (rs.next()) {
                table.addCell(new Cell().add(new Paragraph(rs.getString("Inout"))
                        .setFontColor(new DeviceRgb(255,255, 255)))); // Fekete szöveg
                table.addCell(new Cell().add(new Paragraph(rs.getString("TransactionID"))
                        .setFontColor(new DeviceRgb(255,255, 255)))); // Fekete szöveg
                table.addCell(new Cell().add(new Paragraph(rs.getString("starterName"))
                        .setFontColor(new DeviceRgb(255,255, 255)))); // Fekete szöveg
                table.addCell(new Cell().add(new Paragraph(rs.getString("Product"))
                        .setFontColor(new DeviceRgb(255,255, 255)))); // Fekete szöveg
            }

            // Táblázat hozzáadása a PDF dokumentumhoz
            document.add(new Paragraph("\n\n\n\n"));
            document.add(table);

            // Dokumentum lezárása
            document.close();

            System.out.println("Styled PDF sikeresen létrehozva: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
