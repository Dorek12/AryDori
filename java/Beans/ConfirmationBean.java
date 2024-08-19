package Beans;

public class ConfirmationBean {
	
	// erstellt eine Nachricht nach dem Kauf (Bestätigung etc.)

	String html;

	public ConfirmationBean() {
		this.setDefaultMessage();
	}

	public String getConfirmationAsHtml() {
		String html = this.getHtml();
		this.setDefaultMessage(); // setze Default Message wieder damit Bestellbestätigung oder fail nicht wieder
									// angezeigt wird wenn man die Seite in der Session besucht
		return html;
	}

	public void setDefaultMessage() {
		this.setHtml("<div class='alert alert-primary' role='alert'>\r\n"
				+ "        <p>Suchen Sie etwas spezielles? <a href='../Views/DiscoverView.jsp' style='font-weight: bold;'>Hier</a> können Sie neuste "
				+ "Technik entdecken.</p>\r\n" + "    </div>");
	}

	public void setOrderFailed() {
		this.setHtml("<div class='alert alert-warning' role='alert'>\r\n"
				+ "        <p>Leider ist etwas mit Ihrer Bestellung schiefgelaufen. Bitte überprüfen Sie Ihren Warenkorb "
				+ "und versuchen Sie erneut.</p>\r\n" + "    </div>");
	}

	public void setHtmlConfirmation(String first_name, String last_name, String street, String house_number,
			String city, String postcode) {
		this.setHtml("<div class='container'>\r\n" + "		<div class='d-flex justify-content-center'>\r\n"
				+ "			<div class='alert alert-success p-5' role='alert'>\r\n"
				+ "				<div class='jumbotron text-center'>\r\n"
				+ "					<h1 class='display-3 mb-5'>Vielen dank für Ihre Bestellung!</h1>\r\n"
				+ "					<p class='lead mb-3'><strong>Eine Bestätigung wird an Ihre E-Mail\r\n"
				+ "						geschickt.</strong></p>\r\n"
				+ "					<p class='mb-5'>Lieferung an " + first_name + " " + last_name + ", " + street + " "
				+ house_number + " in " + "						" + postcode + " " + city + "</p>\r\n"
				+ "					<hr>\r\n" + "					<p class='lead mt-4'>\r\n"
				+ "						<a class='btn btn-primary btn-lg' style='width: 300px'\r\n"
				+ "							href='../Views/MainpageView.jsp' role='button'>Startseite</a>\r\n" + "					</p>\r\n"
				+ "				</div>\r\n" + "			</div>\r\n" + "		</div>\r\n" + "	</div>");

	}

	// Getter und Setter Methode

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

}
