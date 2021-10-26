package su.usatu.project23.util;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import su.usatu.project23.model.ReportData;

public class PDFUtil {
	
	public static boolean generateNewPDF(ReportData dataForPDF, String fontsPath, String imgPath, String savingPath) 
			throws IOException, IllegalStateException {
		
		boolean pdfGenerationStatus = false;

		try (PDDocument document = new PDDocument()) {
			final PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
				
				// image
				PDImageXObject image = PDImageXObject.createFromFile(imgPath + "favicon.png", document);
				contentStream.drawImage(image, 25, 790);
				
				// text
				contentStream.beginText();

				PDFont timesRegular = PDType0Font.load(document, new File(fontsPath + "times.ttf"));
				PDFont timesBold = PDType0Font.load(document, new File(fontsPath + "timesbd.ttf"));
				PDFont timesItalic = PDType0Font.load(document, new File(fontsPath + "timesi.ttf"));

				contentStream.setFont(timesBold, 20);
				contentStream.setLeading(14 * 1.25f);
				
				contentStream.newLineAtOffset(0, 828);

				contentStream.newLineAtOffset(82, -20);
				String line1 = "Отчёт";
				contentStream.showText(line1);
				contentStream.newLine();

				contentStream.setFont(timesRegular, 14);

				String line2 = "по произведённому расчёту стоимости производства оконных конструкций";
				contentStream.showText(line2);
				contentStream.newLine();
			
				ZonedDateTime currDateObj = ZonedDateTime.now(ZoneId.of("Asia/Yekaterinburg"));
			    DateTimeFormatter dateFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			    String formattedDate = currDateObj.format(dateFormatObj);
			    
				// сбрасываем координаты для newLineAtOffset
			    contentStream.endText();
				contentStream.beginText();
				
				contentStream.newLineAtOffset(265, 25);
				contentStream.setFont(timesItalic, 14);
				String dateLine = "Дата формирования отчёта: " + formattedDate;
				contentStream.showText(dateLine);
				contentStream.newLine();

				contentStream.endText();
				
				// table
				// Build calculationTable
				Table calculationTable = Table.builder().addColumnsOfWidth(150, 150).font(timesRegular).padding(2)
						.addRow(Row.builder()
								.add(TextCell.builder().text("Варианты створок").borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.sashesCount).borderWidth(1).build())
								.build())
						.addRow(Row.builder()
								.add(TextCell.builder().text("Стеклопакет").borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.glazing).borderWidth(1).build())
								.build())
						.addRow(Row.builder()
								.add(TextCell.builder().text("Высота").borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.width).borderWidth(1).build())
								.build())
						.addRow(Row.builder()
								.add(TextCell.builder().text("Ширина").borderWidth(1).build())
								.add(TextCell.builder().text(dataForPDF.height).borderWidth(1).build())
								.build())
						.addRow(Row.builder()
								.add(TextCell.builder().text("Итого").borderWidth(1).font(timesItalic).build())
								.add(TextCell.builder().text(dataForPDF.totalAmount).borderWidth(1).build())
								.build())
						.build();
				
				// Set up the drawer for calculationTable
				TableDrawer calculationTableDrawer = TableDrawer.builder().contentStream(contentStream).startX(25)
						.startY(page.getMediaBox().getUpperRightY() - 80).table(calculationTable).build();

				// Draw the tables
				calculationTableDrawer.draw();
				
			}
			
			document.save(savingPath);
			document.close();
			pdfGenerationStatus = true;
		}
		
		return pdfGenerationStatus;

	}
}
