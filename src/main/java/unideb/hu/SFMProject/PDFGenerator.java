/*
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
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
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
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to create directory: " + directoryPath);
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

            // ** Cím középre igazítása **
            Paragraph header = new Paragraph("PDF REPORT")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(header);

            // ** Táblázat létrehozása és fejlécek beállítása **
            float[] columnWidths = {2, 4, 6, 6}; // Szélesebb oszlopok
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // Fejlécek hozzáadása
            DeviceRgb headerBgColor = new DeviceRgb(234, 146, 21); // Narancssárga háttér
            DeviceRgb headerTextColor = new DeviceRgb(255, 255, 255); // Fehér szöveg
            addTableHeader(table, headerBgColor, headerTextColor);

            // Háttérszín beállítása (RGB: 58,71,80)
            PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage()); // Új oldal hozzáadása
            canvas.setFillColor(new DeviceRgb(58, 71, 80)); // Háttérszín beállítása
            float width = pdfDoc.getPage(1).getPageSize().getWidth(); // Első oldal szélessége
            float height = pdfDoc.getPage(1).getPageSize().getHeight(); // Első oldal magassága
            canvas.rectangle(0, 0, width, height);
            canvas.fill();

            // Kép beszúrása a dokumentum elejére
            String imagePath = "./src/main/resources/image/palacklogo.png"; // Kép elérési útja

            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                // ** Logo középre igazítása **
                Image img = new Image(ImageDataFactory.create(imagePath));
                img.setHorizontalAlignment(HorizontalAlignment.CENTER);
                img.scaleToFit(200, 200); // Nagyobb méret
                document.add(img);
            } else {
                System.out.println("Image file not found: " + imagePath);
            }


            // Dokumentum margók beállítása a táblázat számára
            document.setMargins(50, 20, 20, 20); // A táblázatnak nagyobb margóval

            while (rs.next()) {
                table.addCell(createStyledCell(rs.getString("Inout"), TextAlignment.CENTER));
                table.addCell(createStyledCell(rs.getString("TransactionID"), TextAlignment.LEFT));
                table.addCell(createStyledCell(rs.getString("starterName"), TextAlignment.LEFT));
                table.addCell(createStyledCell(rs.getString("Product"), TextAlignment.LEFT));
            }

            document.add(table);
            document.close();

            showAlert(AlertType.INFORMATION, "Success", "PDF successfully generated: " + filePath);


        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Database error occurred: " + e.getMessage());
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "An error occurred while generating the PDF: " + e.getMessage());
        }
    }

    // Segédfüggvény: Fejlécek hozzáadása
    private static void addTableHeader(Table table, DeviceRgb bgColor, DeviceRgb textColor) {
        String[] headers = {"IN/OUT", "TransactionID", "TransactionStarterName", "Product"};
        for (String header : headers) {
            table.addHeaderCell(
                    new Cell()
                            .add(new Paragraph(header).setBold().setFontColor(textColor))
                            .setBackgroundColor(bgColor)
                            .setTextAlignment(TextAlignment.CENTER)
            );
        }
    }

    // Segédfüggvény: Stílusos cellák hozzáadása
    private static Cell createStyledCell(String content, TextAlignment alignment) {
        return new Cell()
                .add(new Paragraph(content).setFontSize(12))
                .setTextAlignment(alignment)
                .setPadding(5);
    }

    // Alert megjelenítésére szolgáló metódus
    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

*/

package unideb.hu.SFMProject;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import javafx.scene.control.Alert;

import java.io.File;
import java.sql.*;

public class PDFGenerator {

    public static void generateReport() {
        String directoryPath = "./src/main/resources/Reports";
        String filePath = directoryPath + "/report.pdf";

        File directory = new File(directoryPath);
        if (!directory.exists() && !directory.mkdirs()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to create directory: " + directoryPath);
            return;
        }

        String url = "jdbc:h2:file:./src/main/resources/Database/my_database";
        String user = "sa";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT Inout, TransactionID, starterName, Product FROM Report";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Set background for all pages
            applyBackgroundToAllPages(pdfDoc);

            // Header
            Paragraph header = new Paragraph("PDF REPORT")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(header);

            // Add logo
            addLogo(document);

            // Table
            Table table = createStyledTable();
            populateTableWithData(rs, table);
            document.add(table);

            document.close();
            showAlert(Alert.AlertType.INFORMATION, "Success", "PDF successfully generated: " + filePath);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database error: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }

    private static void applyBackgroundToAllPages(PdfDocument pdfDoc) {
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            PdfCanvas canvas = new PdfCanvas(pdfDoc.getPage(i));
            canvas.setFillColor(new DeviceRgb(58, 71, 80));
            canvas.rectangle(0, 0, pdfDoc.getPage(i).getPageSize().getWidth(), pdfDoc.getPage(i).getPageSize().getHeight());
            canvas.fill();
        }
    }

    private static void addLogo(Document document) {
        try {
            String imagePath = "./src/main/resources/image/palacklogo.png";
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image img = new Image(ImageDataFactory.create(imagePath));
                img.setHorizontalAlignment(HorizontalAlignment.CENTER);
                img.scaleToFit(200, 200);
                document.add(img);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Failed to load the logo.");
        }
    }

    private static Table createStyledTable() {
        float[] columnWidths = {1, 2, 3, 3};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        String[] headers = {"IN/OUT", "TransactionID", "TransactionStarterName", "Product"};
        for (String header : headers) {
            Cell headerCell = new Cell()
                    .add(new Paragraph(header).setBold().setFontColor(new DeviceRgb(255, 255, 255)))
                    .setBackgroundColor(new DeviceRgb(234, 146, 21))
                    .setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell(headerCell);
        }

        return table;
    }

    private static void populateTableWithData(ResultSet rs, Table table) throws SQLException {
        while (rs.next()) {
            table.addCell(createTableCell(rs.getString("Inout")));
            table.addCell(createTableCell(rs.getString("TransactionID")));
            table.addCell(createTableCell(rs.getString("starterName")));
            table.addCell(createTableCell(rs.getString("Product")));
        }
    }

    private static Cell createTableCell(String content) {
        return new Cell()
                .add(new Paragraph(content).setFontSize(12).setFontColor(new DeviceRgb(255, 255, 255)))
                .setBackgroundColor(new DeviceRgb(58, 71, 80))
                .setTextAlignment(TextAlignment.CENTER);
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


