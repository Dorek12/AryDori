package Beans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.mail.MessagingException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFBean {

	String html;
	String email;
	String first_name;
	String last_name;
	String user_id;
	String invoice_id;
	String total;
	String taxes;
	String date;
	ArrayList<String> products;
	ArrayList<String> quantity;
	ArrayList<String> prices;
	ArrayList<String> productIds;
	// Erstelle einen Ordner INVOICES um die Rechnungen zu speichern im workingDirectory (bei Webanwendungen der Eclipse Pfad)
	String workingDirectoryPath = System.getProperty("user.dir");
	File folder = new File(this.workingDirectoryPath + File.separator + "INVOICES");

	public PDFBean() {
		this.setHtml("");
		this.setUser_id("");
		this.setFirst_name("");
		this.setLast_name("");
		this.setInvoice_id("");
		this.setTotal("");
		this.setTaxes("");
		this.setDate("");	
		
		this.products = new ArrayList<String>();
		this.productIds = new ArrayList<String>();
		this.quantity = new ArrayList<String>();
		this.prices = new ArrayList<String>();
		
		// Erstellt den Ordner nur wenn er noch nicht existiert
		this.folder.mkdirs();
		System.out.println("Hier befindet sich der Ordner mit allen Rechnungen: " + folder.getAbsolutePath());
	}

	public void createInvoicePDF() throws IOException, MessagingException {

		// erstellt ein PDF-Dokument
		
		PDDocument document = new PDDocument();
		PDPage page = new PDPage(PDRectangle.A4);
		document.addPage(page);

		// Füllt das Dokument mit Inhalt 
		
		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// Bestimme die Schriftart und die Schriftgröße sowie die Position des Inhaltes
		
		contentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
		contentStream.beginText();
		contentStream.newLineAtOffset(50, 700);
		contentStream.showText("Rechnung");
		contentStream.endText();

		contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
		contentStream.beginText();
		contentStream.newLineAtOffset(50, 650);
		contentStream.showText("Kunde: " + this.getFirst_name() + " " + this.getLast_name());
		contentStream.newLineAtOffset(0, -20);
		contentStream.showText("Rechnungsdatum: " + date);
		contentStream.endText();

		int yPosition = 600;
		contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
		contentStream.beginText();
		contentStream.newLineAtOffset(50, yPosition);
		contentStream.showText("Position: ");
		contentStream.newLineAtOffset(50, 0);
		contentStream.showText("Anzahl: ");
		contentStream.newLineAtOffset(50, 0);
		contentStream.showText("Produktname:");
		contentStream.newLineAtOffset(230, 0);
		contentStream.showText("Artikelnummer:");
		contentStream.newLineAtOffset(120, 0);
		contentStream.showText("Preis:");
		contentStream.endText();

		contentStream.moveTo(50, 595);
		contentStream.lineTo(550, 595);
		contentStream.stroke();

		int invoicePosition = 1;

		// erstellt eine neue Zeile für jedes Produkt
		
		for (int i = 0; i < this.products.size(); i++) {
			yPosition -= 20;
			String position = String.valueOf(invoicePosition);
			String productid = String.valueOf(this.productIds.get(i));
			String numberOfProducts = String.valueOf(this.quantity.get(i));
			contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
			contentStream.beginText();
			contentStream.newLineAtOffset(50, (yPosition));
			contentStream.showText(position);
			contentStream.newLineAtOffset(50, 0);
			contentStream.showText(numberOfProducts);
			contentStream.newLineAtOffset(50, 0);
			contentStream.showText(this.products.get(i));
			contentStream.newLineAtOffset(230, 0);
			contentStream.showText(productid);
			contentStream.newLineAtOffset(120, 0);
			contentStream.showText(this.prices.get(i) + " €");
			contentStream.endText();
			invoicePosition++;
		}

		yPosition -= 30;

		contentStream.moveTo(50, 595);
		contentStream.lineTo(550, 595);
		contentStream.stroke();

		contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
		contentStream.beginText();
		contentStream.newLineAtOffset(50, yPosition);
		contentStream.showText("Gesamtbetrag: " + this.getTotal() + " €");
		contentStream.endText();

		yPosition -= 20;

		contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
		contentStream.beginText();
		contentStream.newLineAtOffset(50, yPosition);
		contentStream.showText("Davon Mehrwertsteuer (19%): " + this.getTaxes() + " €");
		contentStream.endText();
		contentStream.close();
		// Speicher die PDF im INVOICE Ordner
		document.save(
				this.folder.getAbsolutePath() + File.separator + ("Rechnung_" + this.getUser_id() + "_" + this.getInvoice_id() + ".pdf"));
		// Dokument wird geschlossen sonst Fehlermeldung
		document.close();
		
		this.products = new ArrayList<String>();
		this.productIds = new ArrayList<String>();
		this.quantity = new ArrayList<String>();
		this.prices = new ArrayList<String>();

		// ruft die MailBean auf um die PDF zu verschicken
		
		MailBean mail = new MailBean();
		mail.setEmail(this.getEmail());
		mail.sendOrderConfirmationMail(this.folder, this.getHtml(), this.getUser_id(), this.getInvoice_id());

		
	}
	
	// Getter und Setter - Methoden
	

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getInvoice_id() {
		return invoice_id;
	}

	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTaxes() {
		return taxes;
	}

	public void setTaxes(String taxes) {
		this.taxes = taxes;
	}

}
