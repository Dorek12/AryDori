package Beans;
// Folgende JAR´s sind notwendig: 
// javax.mail.jar - > https://github.com/javaee/javamail/releases
// activation-1.1.1.jar - > https://mvnrepository.com/artifact/javax.activation/activation/1.1.1 (Files: jar)
// ACHTUNG! JAR´s müssen auch in WEB-INF/lib

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class MailBean {
	String email;
	String subject;
	String text;

	// Session wird benötigt um das Versenden von E-Mails zu konfigurieren
	Session newSession;
	// MIME für die Darstellung der Mail (erlaubt auch Anhänge, Bilder etc.)
	// Ist der Grundrahmen der E-Mail (darin ist der MultiPart der wiederum mehrere Bodyparts enthalten kann)
	MimeMessage mimeMessage;
	// MultiPart fügt Body zusammen 
	MimeMultipart multiPart = new MimeMultipart();
	//Für jeden Text, Anhang etc. einen neuen Bodypart
	MimeBodyPart bodyPart = new MimeBodyPart();
	
	public MailBean() {
		this.setEmail("");
		this.setSubject("");
		this.setText("");
		this.newSession = null;
		this.mimeMessage = null;
	}
	
	// schickt Link um das Passwort wieder herzustellen
    public void sendForgetPasswordMail(String link) throws MessagingException {
    	if(this.getEmail() != null && !this.getEmail().isBlank()) {
    		this.setSubject("Passwort zurücksetzen");
        	this.setText("Hallo, <p>Sie haben ihr Passwort vergessen? Klicken Sie auf den"
        			+ " Link um Ihr Passwort zu ändern</p>" + link);
        	this.bodyPart.setContent(this.getText(), "text/html; charset=UTF-8");
        	this.multiPart.addBodyPart(bodyPart);
        	this.sendEmail();
    	}
    }
    
    // schickt eine Bestellbestätigung
    public void sendOrderConfirmationMail(File folder, String html, String user_id, String invoice_id) throws MessagingException, IOException {
    	if(this.getEmail() != null && !this.getEmail().isBlank()) {
    		this.setSubject("Ihre Bestellung");
        	this.setText(html);
        	this.bodyPart.setContent(this.getText(), "text/html; charset=UTF-8");
        	this.multiPart.addBodyPart(bodyPart);
        	this.attachInvoiceFile(folder, user_id, invoice_id);
        	this.sendEmail();
    	}
    }
    
    // schickt erneut eine E-Mail mit der Rechnung im Anhang die in der ProfileView ausgewählt wurde
    public void sendInvoiceAgain(String html, String user_id, String invoice_id) throws MessagingException, IOException {
    	if(this.getEmail() != null && !this.getEmail().isBlank()) {
    		String currentWorkingDirectory = System.getProperty("user.dir");
    		File folder = new File(currentWorkingDirectory + File.separator + "INVOICES");
    		this.setSubject("Ihre Rechnung");
        	this.setText(html);
        	this.bodyPart.setContent(this.getText(), "text/html; charset=UTF-8");
        	this.multiPart.addBodyPart(bodyPart);
        	this.attachInvoiceFile(folder, user_id, invoice_id);
        	this.sendEmail();
    	}
    }
    
    // PDF wird angehängt
    public void attachInvoiceFile(File folder, String user_id, String invoice_id) throws MessagingException, IOException {
    	MimeBodyPart bodyPartFile = new MimeBodyPart();
    	File invoice = new File(folder.getAbsolutePath() + File.separator + "Rechnung_" + user_id + "_" + invoice_id +".pdf");
        bodyPartFile.attachFile(invoice);
        this.multiPart.addBodyPart(bodyPartFile);
    }
        
    public void sendEmail() throws MessagingException {
    	this.draftEmail();
    	String account = "nas.elektronik.kontakt@gmail.com";
    	String appPassword = "kmwykwszxjnaiubg"; // App_passwort von Google generiert
    	String emailHost = "smtp.gmail.com";
    	
    	// Transport sendet die Mail an SMTP-Server
    	Transport transport = newSession.getTransport("smtp");
    	transport.connect(emailHost, account, appPassword);
    	transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
    	transport.close();
    	System.out.println("E-Mail an " + this.getEmail() + " wurde versendet");
    	this.newSession = null;
    	this.mimeMessage = null;
    	this.bodyPart = new MimeBodyPart();
    	this.multiPart = new MimeMultipart();
    }
    
    public MimeMessage draftEmail() throws AddressException, MessagingException {
    	this.setupServerProperties();
    	this.mimeMessage = new MimeMessage(newSession);
    	this.mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(this.getEmail()));
    	this.mimeMessage.setSubject(this.getSubject());
    	this.mimeMessage.setContent(this.multiPart);
    	return mimeMessage;
    	
    }
    
    // Verbindung zum SMTP-Server notwendige Konfiguration
    
    public void setupServerProperties() {
    	Properties properties = System.getProperties();
    	// setze mail.smtp auf den Port 587 (Gratis Port von Google),
    	// aktiviere Authentifizierung und Verschlüsselung (starttls) mit true
    	properties.put("mail.smtp.port", "587");
    	properties.put("mail.smtp.auth", "true");
    	properties.put("mail.smtp.starttls.enable", "true");
    	properties.put("mail.smtp.charset", "UTF-8");
    	newSession = Session.getDefaultInstance(properties);    	
    }

    
    // Getter und Setter - Methoden
    
	public String getSubject() {
		return subject;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}    
    
}