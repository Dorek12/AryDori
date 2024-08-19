package Beans;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.mail.MessagingException;

import Connection.NoConnectionException;
import Connection.PostgreSQLAccess;

public class AccountBean {

	String firstName;
	String lastName;
	String email;
	String password;
	String number;
	String birthdate;
	String street;
	String city;
	String houseNumber;
	String viewMessage;
	String infoMsg;
	String createdAt;
	int user_id;
	int postcode;
	boolean employee;
	boolean loggedIn;
	boolean passwordChanged;
	Connection dbConn;

	ArrayList<Integer> invoiceId;
	ArrayList<String> invoiceDates;
	ArrayList<Double> totalAmounts;
	ArrayList<String> paymentMethods;
	ArrayList<String> paymentStatus;
	ArrayList<Double> totalAmountsTax;
	ArrayList<Integer> relevantFields;

	ArrayList<Integer> productId;
	ArrayList<Integer> quantity;
	ArrayList<Double> pricePerUnit;

	public AccountBean() throws NoConnectionException {
		this.createConnection();
		this.setFirstName("");
		this.setLastName("");
		this.setEmail("");
		this.setPassword("");
		this.setNumber("");
		this.setBirthdate("");
		this.setInfoMsg("");
		this.setMessage("");
		this.setEmployee(false);
		this.setLoggedIn(false);
		this.setUser_id(Integer.MIN_VALUE);

		this.invoiceId = new ArrayList<Integer>();
		this.invoiceDates = new ArrayList<String>();
		this.totalAmounts = new ArrayList<Double>();
		this.paymentMethods = new ArrayList<String>();
		this.paymentStatus = new ArrayList<String>();
		this.totalAmountsTax = new ArrayList<Double>();
		this.relevantFields = new ArrayList<Integer>();
		this.productId = new ArrayList<Integer>();
		this.quantity = new ArrayList<Integer>();
		this.pricePerUnit = new ArrayList<Double>();
	}

	// Connection-Methode

	public void createConnection() throws NoConnectionException {
		this.dbConn = new PostgreSQLAccess().getConnection();
	}

	public String getLoginAsHtml() {
		String html = "<div class='container' style='margin-top: " + (!this.getInfoMsg().isBlank() ? "5" : "50")
				+ "px;'>\r\n";
		if (!this.loggedIn) {
			html += this.getInfoMsg();
			html += "    <div id='login-container'>\r\n"
					+ "        <form id='login' action='../Appls/AccountAppl.jsp' method='get'>\r\n"
					+ "            <div class='form-group'>\r\n"
					+ "                <label for='email'>E-Mail:</label>\r\n"
					+ "                <input type='text' class='form-control' value='' name='email' id='emailLog' placeholder='Geben Sie hier Ihr E-Mail-Adresse ein'>\r\n"
					+ "            </div>\r\n" + "            <div class='form-group'>\r\n"
					+ "                <label for='password'>Passwort:</label>\r\n"
					+ "                <input type='password' class='form-control' name='password' id='passwordLog' placeholder='Geben Sie hier Ihr Passwort ein'>\r\n"
					+ "                 <p id='passwordLogMsg' style='color: red;'></p>" + "            </div>\r\n"
					+ "            <button type='submit' class='btn btn-primary' id='btnLog' name='btnLog' value='einloggen' onclick='checkLogin(event)'>Login</button>\r\n"
					+ "            <div class='form-group d-flex justify-content-between mt-3'>\r\n"
					+ "                <a href='../Views/RecoverPasswordView.jsp' class='btn btn-primary' id='forgot-password-link' style='width: 220px'>Passwort vergessen?</a> <a href='../Views/RegisterView.jsp' class='btn btn-primary' id='register-link'\r\n"
					+ "						style='width: 220px'>Registrieren</a>\r\n" + "				</div>\r\n"
					+ "        </form>\r\n" + "    </div>\r\n" + "</div>";

		} else {
			html = "<div class='alert alert-info' role='alert'>\r\n" + "        <p>Sie sind bereits angemeldet: <b>"
					+ this.getEmail() + "</b></p>\r\n" + "    </div>";
		}
		this.setInfoMsg(""); // setze wieder Default damit es bei erneutem Besuch während der Session wieder
								// leer ist
		this.setPasswordChanged(false);
		return html;
	}

	public String getRegisterAsHtml() {
		String html = "		<div id='registration-container'>\r\n"
				+ "			<form action='../Appls/AccountAppl.jsp'\r\n"
				+ "				method='get' onsubmit='return checkRegister()'>\r\n"
				+ "				<div class='form-group'>\r\n"
				+ "					<label for='first_name'>Vorname<span class='text-danger'>*</span>:\r\n"
				+ "					</label> <input type='text' class='form-control' id='first_name'\r\n"
				+ "						name='first_name' placeholder='Vorname'>\r\n"
				+ "                    <p id='first_nameMsg' style='color: red;';></p>" + "				</div>\r\n"
				+ "				<div class='form-group'>\r\n"
				+ "					<label for='last_name'>Nachname<span class='text-danger'>*</span>:\r\n"
				+ "					</label> <input type='text' class='form-control' id='last_name'\r\n"
				+ "						name='last_name' placeholder='Nachname'>\r\n"
				+ "                    <p id='last_nameMsg' style='color: red;'></p>" + "				</div>\r\n"
				+ "				<div class='form-group'>\r\n" + "					<label>Geburtstag:</label>\r\n"
				+ "					<div class='row'>\r\n" + "\r\n" + "						<div class='col'>\r\n"
				+ "							<select class='form-control' id='birth_month' name='birth_month'></select>\r\n"
				+ "						</div>\r\n" + "						<div class='col'>\r\n"
				+ "							<select class='form-control' id='birth_day' name='birth_day'></select>\r\n"
				+ "						</div>\r\n" + "						<div class='col'>\r\n"
				+ "							<select class='form-control' id='birth_year' name='birth_year'></select>\r\n"
				+ "						</div>\r\n" + "					</div>\r\n" + "				</div>\r\n"
				+ "				<div class='form-group'>\r\n"
				+ "					<label for='email'>E-Mail<span class='text-danger'>*</span>:\r\n"
				+ "					</label> <input type='text' class='form-control' id='emailRegister' value='' name='email'\r\n"
				+ "						placeholder='Geben Sie hier Ihre E-Mail-Adresse ein'>\r\n"
				+ "                    <p id='emailRegisterMsg' style='color: red;'></p>" + "				</div>\r\n"
				+ "				<div class='form-group'>\r\n"
				+ "					<label for='number'>Telefonnummer:</label> <input type='text'\r\n"
				+ "						class='form-control' value='' name='number'\r\n"
				+ "						placeholder='Telefonnummer' id='number'>\r\n"
				+ "                    <p id='numberMsg' style='color: red;'></p>" + "				</div>\r\n"
				+ "				<div class='form-group'>\r\n"
				+ "					<label for='password1'>Passwort<span class='text-danger'>*</span>:\r\n"
				+ "					</label> <input type='password' class='form-control' name='password'\r\n"
				+ "						id='passwordRegister' placeholder='Geben Sie hier ein Passwort ein' onkeyup='checkPasswordRegister(this.value)'>\r\n"
				+ "                    <p id='passwordRegisterMsg' style='color: red;'></p>"
				+ "				</div>\r\n" + "				<div class='form-group'>\r\n"
				+ "					<label for='password2'>Passwort bestätigen<span\r\n"
				+ "						class='text-danger'>*</span>:\r\n"
				+ "					</label> <input type='password' class='form-control' name='password2'\r\n"
				+ "						id='passwordRegisterCheck' placeholder='Passwort bestätigen'>\r\n"
				+ "                    <p id='passwordRegisterCheckMsg' style='color: red;'></p>"
				+ "             </div>"
				+ "				<p class='text-danger'>Felder mit * sind Pflichtangaben.</p>\r\n"
				+ "				<div class='g-recaptcha' data-sitekey='6LeGXBAnAAAAALr-HlhfgVnqUxTjIwXCsqx_RKFW'></div>\r\n"
				+ "				<button type='submit' class='btn btn-primary' id='btnReg'\r\n"
				+ "					name='btnReg' value='registrieren'>Account erstellen</button>\r\n"
				+ "				<p class='mt-3'>\r\n"
				+ "					Haben Sie bereits einen Account? <a href='../Views/LoginView.jsp'>Hier zum Login!</a>\r\n"
				+ "				</p>\r\n" + "			</form>\r\n" + "		</div>\r\n";
		return html;
	}

	public boolean checkLogIn() throws SQLException, ClassNotFoundException {
		String sql = "SELECT * FROM users where email ILIKE ? and password = ?";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setString(1, this.getEmail());
		prep.setString(2, this.getPassword());
		ResultSet dbRes = prep.executeQuery();
		if (dbRes.next()) {
			this.setFirstName(dbRes.getString("first_name"));
			this.setLastName(dbRes.getString("last_name"));
			this.setEmail(dbRes.getString("email"));
			this.setStreet(dbRes.getString("street"));
			this.setCity(dbRes.getString("city"));
			this.setNumber(dbRes.getString("telephone"));
			this.setBirthdate(dbRes.getString("date_of_birth"));
			this.setHouseNumber(dbRes.getString("house_number"));
			this.setPostcode(dbRes.getInt("postcode"));
			this.setEmployee(dbRes.getBoolean("is_employee"));
			this.setCreatedAt(dbRes.getString("account_created_at"));
			this.setLoggedIn(true);
			this.setUser_id(dbRes.getInt("user_id"));
			this.setEmployee(dbRes.getBoolean("is_employee"));
			return true;
		} else {
			this.setFirstName("");
			this.setLastName("");
			this.setEmail("");
			this.setStreet("");
			this.setNumber("");
			this.setHouseNumber("");
			this.setPostcode(0);
			this.setBirthdate("");
			this.setEmployee(false);
			this.setLoggedIn(false);
			this.setUser_id(Integer.MIN_VALUE);
			return false;
		}
	}

	// wird in der AccountAppl aufgerufen und setzt eine passende Info - Message, die in der LogInAsHtml aufgerufen wird
	
	public void createInfoMessage() {
		if (this.isPasswordChanged()) {
			this.setInfoMsg("<div class='alert alert-success' role='alert'>\r\n"

					+ "        <p>Ihr Passwort wurde erfolgreich geändert</p>\r\n" + "    </div>");
		} else {
			this.setInfoMsg("<div class='alert alert-warning' role='alert'>\r\n"

					+ "        <p>Die E-Mail-Adresse und/oder das Passwort waren falsch. Bitte versuchen Sie es erneut.</p>\r\n"
					+ "    </div>");
		}
		this.setPasswordChanged(false);
	}

	public void setParametersForRegistration(String firstName, String lastName, String email, String birthDate,
			String password, String number) {
		this.setFirstName(firstName.trim());
		this.setLastName(lastName.trim());
		this.setEmail(email.trim());
		this.setBirthdate(birthDate);
		this.setPassword(password);
		this.setNumber(number.trim());
	}

	public boolean checkAlreadyRegistered() throws ClassNotFoundException, SQLException {
		String sql = "SELECT email FROM users where email = ?";
		System.out.println(sql);
		PreparedStatement prep = dbConn.prepareStatement(sql);
		prep.setString(1, this.getEmail());
		ResultSet rs = prep.executeQuery();
		return rs.next();
	}

	public void insertNewCustomer() throws ClassNotFoundException, SQLException {
		// gebe diese Nachricht immer aus, damit man nicht checken kann wer bereits bei uns registriert ist
		this.setInfoMsg("<div class='alert alert-success' role='alert'>\r\n"
					+ "        <p>Vielen Dank für Ihre Registrierung. Bitte melden Sie sich hier an.</p>\r\n"
					+ "    </div>");
		if (!this.checkAlreadyRegistered()) {
			String sql = "insert into users (first_name, last_name, date_of_birth, email, password, telephone) values (?,?,?,?,?,?)";
			System.out.println(sql);
			PreparedStatement prep = dbConn.prepareStatement(sql);
			prep.setString(1, this.getFirstName());
			prep.setString(2, this.getLastName());
			prep.setString(3, this.getBirthdate());
			prep.setString(4, this.getEmail());
			prep.setString(5, this.getPassword());
			prep.setString(6, this.getNumber());
			prep.executeUpdate();
		}
	}

	public String getProfilViewAsHtml() throws SQLException {
		String html = "";
		if (this.isLoggedIn()) {
			html += "<div class='container'>\r\n" + "		<form action='../Appls/AccountAppl.jsp' method='get'>\r\n"
					+ "			<div class='row'>\r\n" + "\r\n" + "				<div class='col-md-3'>\r\n"
					+ "					<table class='table'>\r\n" + "						<thead>\r\n"
					+ "							<tr>\r\n" + "								<th>Menü</th>\r\n"
					+ "							</tr>\r\n" + "						</thead>\r\n"
					+ "						<tbody>\r\n" + "							<tr>\r\n"
					+ "								<td class='menu-item'><a'\r\n"
					+ "									onclick='showContent(\"overview\")'>Übersicht</a></td>\r\n"
					+ "							</tr>\r\n" + "							<tr>\r\n"
					+ "								<td class='menu-item'><a\r\n"
					+ "									onclick='showContent(\"recent-purchases\")'>Rechnungen</a></td>\r\n"
					+ "							</tr>\r\n" + "							<tr>\r\n"
					+ "								<td class='menu-item'><a\r\n"
					+ "									onclick='showContent(\"account-edit\")'>Account bearbeiten</a></td>\r\n"
					+ "							</tr>\r\n" + "							<tr>\r\n"
					+ "								<td class='menu-item'><a\r\n"
					+ "									onclick='showContent(\"password-edit\")'>Passwort ändern</a></td>\r\n"
					+ "							</tr>\r\n" + "							<tr>\r\n"
					+ "								<td class='menu-item'><a\r\n"
					+ "									onclick='showContent(\"billing-address\")'>Adresse ändern</a></td>\r\n"
					+ "							</tr>\r\n" + "						</tbody>\r\n"
					+ "					</table>";
			html += this.getPortalBtn() + "				</div>";
			html += "<div class='col-md-8'>\r\n" + "					<div id='overview' class='menu-content'>\r\n"
					+ "						<h2>\r\n" + "							Willkommen, " + this.getFirstName()
					+ " " + this.getLastName() + "</h2>"
					+ "<p>Hier können Sie Ihre Daten einsehen und anpassen.</p>\r\n" + "						<h4>"
					+ this.getMessageHtml() + "</h4>\r\n" + "\r\n" + "\r\n"
					+ "						<h3>Persönliche Informationen:</h3>\r\n"
					+ "						<ul>\r\n" + "							<li><strong>Vorname</strong><br>"
					+ this.getFirstName() + "</li>\r\n" + "							<li><strong>Nachname</strong> <br>"
					+ this.getLastName() + "</li>\r\n"
					+ "							<li><strong>Geburtstag</strong> <br>" + this.getBirthdate()
					+ "</li>\r\n" + "							<li><strong>E-Mail</strong> <br>" + this.getEmail()
					+ "</li>\r\n" + "							<li><strong>Telefon Nummer</strong> <br>"
					+ this.getNumber() + "</li>\r\n"
					+ "							<li><strong>Account erstellt am: </strong> <br>" + this.getCreatedAt()
					+ "</li>\r\n" + "							<li><strong>Ihre Kundennummer: </strong> <br>"
					+ this.getUser_id() + "</li>\r\n" + "						</ul>\r\n" + "\r\n" + "\r\n"
					+ "					</div>					<div id='recent-purchases' class='menu-content'>\r\n"
					+ "						<h2 class='mb-3'>Ihre Einkäufe</h2>" + this.getUserInvoices()
					+ "					</div>\r\n" + "				</div>\r\n"
					+ "				<div id='account-edit' class='menu-content'>\r\n"
					+ "					<h2>Account bearbeiten</h2>\r\n"
					+ "					<h3>Persönliche Informationen</h3>" + this.getAccEdit()
					+ "</div>   <div id='password-edit' class='menu-content'>\r\n"
					+ "		<h2>Passwort bearbeiten</h2>\r\n" + "		<div class='form-group mt-2'>\r\n"
					+ "			<label for='currentPassword'>Aktuelles Passwort:</label> <input\r\n"
					+ "				type='password' name='currentPassword' class='form-control'\r\n"
					+ "				id='currentPassword'>\r\n" + "		</div>\r\n"
					+ "		<div class='form-group mt-2'>\r\n"
					+ "			<label for='newPassword'>Neues Passwort:</label> <input\r\n"
					+ "				type='password' name='newPassword' class='form-control'\r\n"
					+ "				id='newPassword'>\r\n" + "		</div>\r\n"
					+ "		<div class='form-group mt-2'>\r\n"
					+ "			<label for='confirmNewPassword'>Neues Passwort bestätigen:</label> <input\r\n"
					+ "				type='password' name='confPassword' class='form-control'\r\n"
					+ "				id='confirmNewPassword'>\r\n"
					+ "			<div id='errorMessage' class='error-message' style='color: red;'></div>\r\n"
					+ "		</div>\r\n" + "		<button type='submit' value='savePassword' name='btnEditPassword'\r\n"
					+ "			id='validatePasswordButton' class='btn btn-primary mt-3'>Passwort\r\n"
					+ "			ändern</button>\r\n" + "\r\n" + "\r\n" + "		<br> <br> <br> <br>\r\n" + "	</div>"
					+ "	<div id='billing-address' class='menu-content'>\r\n" + "		<h2>Adresse bearbeiten</h2>\r\n"
					+ "		<p>Hier können Sie Ihre hinterlegte Adresse aktualisieren.</p>\r\n" + "\r\n" + "\r\n"
					+ "		<div class='form-group mt-2'>\r\n"
					+ "			<label for='streetAddress'>Straße:</label> <input type='text'\r\n"
					+ "				value='" + this.getStreet() + "'\r\n"
					+ "				name='streetAdd' class='form-control' id='street'>\r\n" + "		</div>\r\n"
					+ "		<div class='form-group mt-2'>\r\n"
					+ "			<label for='streetAddress'>Hausnummer:</label> <input type='text'\r\n"
					+ "				value='" + this.getHouseNumber() + "'\r\n"
					+ "				name='streetNumberAdd' class='form-control' id='streetNumber'>\r\n"
					+ "		</div>\r\n" + "		<div class='form-group mt-2'>\r\n"
					+ "			<label for='city'>Stadt:</label> <input type='text' name='cityAdd'\r\n"
					+ "				value='" + this.getCity() + "'\r\n"
					+ "				class='form-control' id='city'>\r\n" + "		</div>\r\n"
					+ "		<div class='form-group mt-2'>\r\n"
					+ "			<label for='zipCode'>Postleitzahl:</label> <input type='number'\r\n"
					+ "				name='zipCodeAdd'\r\n" + "				value='" + this.getPostcode() + "'\r\n"
					+ "				class='form-control' id='zipCode'>\r\n" + "		</div>\r\n" + "\r\n"
					+ "		<button type='submit' value='changeAddress' name='btnChangeAddress'\r\n"
					+ "			class='btn btn-primary mt-3'>Änderungen speichern</button>\r\n" + "\r\n"
					+ "	</div>\r\n" + "	</div>\r\n" + "\r\n" + "	<div class='col-md-1'>\r\n"
					+ "		<button type='submit' class='btn btn-outline-danger mt-3'\r\n"
					+ "			value='logout' name='btnLogout' style='float: right;'>Logout</button>\r\n"
					+ "	</div>\r\n" + "	</div>\r\n" + "	</form>\r\n" + "	</div>\r\n" + "";
		} else {
			html += "<div class='alert alert-warning' role='alert'>\r\n"
					+ "        <p>Sie müssen angemeldet sein, um auf Ihr Profil zugreifen zu können.</p>\r\n"
					+ "    </div>";
		}
		return html;
	}
	
	public String getAccEdit() {
		String html = "";
		if (this.isLoggedIn()) {
			html += "<form action='../Appls/AccountAppl.jsp' method='get'>\r\n"
					+ "						<div class=\"form-group mt-2\">\r\n"
					+ "							<label for=\"firstName\">Vorname:</label> <input type=\"text\"\r\n"
					+ "								class=\"form-control\" name='changeFirstName' value="
					+ this.firstName + ">\r\n" + "						</div>\r\n"
					+ "						<div class=\"form-group mt-2\">\r\n"
					+ "							<label for=\"lastName\">Nachname:</label> <input type=\"text\"\r\n"
					+ "								class=\"form-control\" name='changeLastName' value=" + this.lastName
					+ ">\r\n" + "						</div>\r\n"
					+ "						<div class=\"form-group mt-2\">\r\n"
					+ "							<label for=\"email\">E-Mail-Adresse:</label> <input type=\"email\"\r\n"
					+ "								class=\"form-control\" name='changeEmail'\r\n"
					+ "								value=" + this.email + ">\r\n" + "						</div>\r\n"
					+ "						<div class=\"form-group mt-2\">\r\n"
					+ "							<label for=\"phoneNumber\">Telefonnummer:</label> <input type=\"tel\"\r\n"
					+ "								class=\"form-control\" name='changePhoneNumber' value="
					+ this.number + ">\r\n" + "						</div>\r\n"
					+ "						<button type=\"submit\" value='saveChanges' name='btnEditData' class=\"btn btn-primary mt-3\">Änderungen\r\n"
					+ "							speichern</button>\r\n" + "					</form>";
		} else {
			html += "Bitte melden Sie sich an.";
		}
		return html;
	}
	
	// allgemine Informationen des Users ändern

	public void editData(String firstName, String lastName, String phoneNumber, String email) throws SQLException {
		try {
			String sql = "UPDATE bwi520_633899.users SET first_name = ?, last_name = ?, email = ?, telephone = ? WHERE user_id = ?;";
			System.out.println(sql);
			PreparedStatement prep = dbConn.prepareStatement(sql);
			prep.setString(1, firstName);
			prep.setString(2, lastName);
			prep.setString(3, email);
			prep.setString(4, phoneNumber);
			prep.setInt(5, this.getUser_id());

			this.setFirstName(firstName);
			this.setLastName(lastName);
			this.setEmail(email);
			this.setNumber(phoneNumber);
			this.setMessage("Änderungen gespeichert.");
			prep.executeUpdate();
		} catch (Exception e) {
			this.setMessage("Daten konnten nicht geändert werden.");
		}
	}

	public boolean changePassword(String oldPassword, String newPassword) throws SQLException {
		try {
			String sql = "SELECT password FROM users WHERE user_id = ?;";
			System.out.println(sql);
			PreparedStatement prep = dbConn.prepareStatement(sql);
			prep.setInt(1, this.getUser_id());
			ResultSet dbRes = prep.executeQuery();
			if (dbRes.next()) {
				String dbPassword = dbRes.getString(1);
				System.out.println(dbPassword + oldPassword);
				if (oldPassword.equals(dbPassword)) {
					sql = "UPDATE bwi520_633899.users SET password = ? WHERE user_id = ?;";
					System.out.println(sql);
					prep = dbConn.prepareStatement(sql);
					prep.setString(1, newPassword);
					prep.setInt(2, this.getUser_id());
					prep.executeUpdate();
					return true;
				}
			}
			this.setMessage("Passwort konnte nicht geändert werden.");

			return false;
		} catch (Exception e) {
			this.setMessage("Passwort konnte nicht geändert werden.");
			return false;
		}

	}

	public void editAddress(String streetAddress, String streetNumberAddress, String cityAddress, String zipCodeAddress)
			throws SQLException {
		try {
			String sql = "UPDATE bwi520_633899.users SET street = ?, house_number = ?, city = ?, postcode = ? WHERE user_id = ?;";
			System.out.println(sql);
			PreparedStatement prep = dbConn.prepareStatement(sql);
			prep.setString(1, streetAddress);
			prep.setString(2, streetNumberAddress);
			prep.setString(3, cityAddress);
			int intZipCodeAddress = Integer.valueOf(zipCodeAddress);
			prep.setInt(4, intZipCodeAddress);
			prep.setInt(5, this.getUser_id());

			this.setStreet(streetAddress);
			this.setHouseNumber(streetNumberAddress);
			this.setCity(cityAddress);
			this.setPostcode(Integer.valueOf(zipCodeAddress));

			this.setMessage("Addresse erfolgreich geändert.");
			prep.executeUpdate();
		} catch (Exception e) {
			this.setMessage("Addresse konnte nicht geändert werden.");
		}
	}

	// Portal btn steht nur Mitarbeitern zur Verfügung

	public String getPortalBtn() {
		String html = "";
		if (this.isLoggedIn() && this.isEmployee()) {
			html += "<button type='submit' name=\"btnPortal\" value=\"btnPortal\" class='btn btn-outline-primary'>\r\n"
					+ "					 Ins Portal <i class=\"fas fa-door-open\"></i>\r\n" + "				</button>";
		}
		return html;
	}

	// lädt beim einloggen die Rechnungen
	
	public void setUserInvoices() throws SQLException {
		try {

			String sql = "SELECT invoice_id, invoice_date, total_amount, payment_method, payment_status FROM bwi520_633899.invoice_details WHERE user_id = ?;";
			System.out.println(sql);
			PreparedStatement prep = dbConn.prepareStatement(sql);
			prep.setInt(1, this.getUser_id());
			ResultSet dbRes = prep.executeQuery();
			while (dbRes.next()) {
				this.invoiceId.add(dbRes.getInt("invoice_id"));
				this.invoiceDates.add(dbRes.getString("invoice_date"));
				this.totalAmounts.add(dbRes.getDouble("total_amount"));
				this.paymentMethods.add(dbRes.getString("payment_method"));
				this.paymentStatus.add(dbRes.getString("payment_status"));
			}

		} catch (Exception e) {
			this.setMessage("Etwas ist schief gelaufen");
			e.printStackTrace();
		}

	}

	public String getUserInvoices() throws SQLException {
		String html = "<div id=\"accordion\">";
		if (this.invoiceId.isEmpty()) {
			html += "Sie haben noch nichts gekauft.";
		} else {
			// Rechnungen werden ausgegeben
			for (int i = 0; i < invoiceId.size(); i++) {
				this.readInvoiceItems(this.invoiceId.get(i));
				html += "<div class=\"card rounded-0\">\r\n"
						+ "							<div class=\"card-header text-center\" id=\"heading" + (i + 1)
						+ "\">\r\n" + "								<h5 class=\"mb-0\">\r\n"
						+ "									<button type=\"button\" class=\"btn btn-block collapsed\"\r\n"
						+ "										data-bs-toggle=\"collapse\" data-bs-target=\"#collapse"
						+ (i + 1) + "\"\r\n"
						+ "										aria-expanded=\"false\" aria-controls=\"collapse"
						+ (i + 1) + "\">\r\n" + "										<b>Rechnung " + (i + 1)
						+ "</b>\r\n" + "									</button>\r\n"
						+ "								</h5>\r\n" + "							</div>\r\n" + "\r\n"
						+ "							<div id=\"collapse" + (i + 1) + "\" class=\"collapse\"\r\n"
						+ "								aria-labelledby=\"heading" + (i + 1)
						+ "\" data-bs-parent=\"#accordion\">\r\n"
						+ "								<div class=\"card-body\">\r\n"
						+ "									<div class=\"container\">\r\n"
						+ "										<div class=\"row\">\r\n"
						+ "											<div class=\"col\">\r\n"
						+ "												<h1 class=\"text-center mb-5\">Rechnung vom "
						+ this.invoiceDates.get(i) + "</h1>\r\n"
						+ "											</div>\r\n"
						+ "										</div>\r\n"
						+ "										\r\n"
						+ "										<div class=\"row\">\r\n"
						+ "										\r\n"
						+ "											<div class=\"col\">\r\n"
						+ "												<strong>Rechnungsnummer:</strong> "
						+ this.invoiceId.get(i) + "\r\n" + "											</div>\r\n"
						+ "											\r\n"
						+ "											<div class=\"col\">\r\n"
						+ "												<strong>Kundennummer:</strong> " + this.user_id
						+ "\r\n" + "											</div>\r\n"
						+ "										\r\n"
						+ "										</div>\r\n"
						+ "										\r\n"
						+ "										<div class=\"row\">\r\n"
						+ "										\r\n"
						+ "											<div class=\"col\">\r\n"
						+ "												<strong>Rechnungsdatum:</strong> "
						+ this.invoiceDates.get(i) + "\r\n" + "											</div>\r\n"
						+ "											\r\n"
						+ "											<div class=\"col\">\r\n"
						+ "												<strong>Rechnungsstatus:</strong> "
						+ this.paymentStatus.get(i) + "\r\n" + "											</div>\r\n"
						+ "											\r\n"
						+ "										</div>\r\n"
						+ "										\r\n"
						+ "										<div class=\"row\">\r\n"
						+ "											<div class=\"col\">\r\n"
						+ "												<strong>Bezahlmethode:</strong> "
						+ this.paymentMethods.get(i) + "\r\n" + "											</div>\r\n"
						+ "										\r\n" + "										\r\n"
						+ "										</div>\r\n" + "\r\n"
					    + "<br><div><button type='submit' class='btn btn-outline-secondary' name='btnInvoiceMail' value='" + this.invoiceId.get(i) +"'>Rechnung per E-Mail erhalten</button></div>"
						+ "										<table class=\"table my-5\">\r\n"
						+ "											<thead>\r\n"
						+ "												<tr>\r\n"
						+ "													<th scope=\"col\">Position</th>\r\n"
						+ "													<th scope=\"col\">Beschreibung</th>\r\n"
						+ "													<th scope=\"col\">Menge</th>\r\n"
						+ "													<th scope=\"col\">Einzelpreis</th>\r\n"
						+ "													<th scope=\"col\">Gesamtpreis</th>\r\n"
						+ "												</tr>\r\n"
						+ "											</thead>\r\n"
						+ "											<tbody>\r\n";
				// füllt die einzelnen Rechnungen
				for (int k = 0; k < this.productId.size(); k++) {
					html += "										<tr>\r\n"
							+ "													<th scope=\"row\">" + (k + 1)
							+ "</th>\r\n" + "													<td>"
							+ this.productId.get(k) + "</td>\r\n"
							+ "													<td>" + this.quantity.get(k)
							+ "</td>\r\n" + "													<td>"
							+ this.removeZeros(this.pricePerUnit.get(k)) + " €</td>\r\n"
							+ "													<td>"
							+ this.removeZeros((this.quantity.get(k) * this.pricePerUnit.get(k))) + " €</td>\r\n"
							+ "												</tr>\r\n";
				}

				html += "											</tbody>\r\n"
						+ "										</table>\r\n"
						+ "										<div class=\"row\">\r\n"
						+ "											<div class=\"col\">\r\n"
						+ "												<p>\r\n"
						+ "													<strong>Zwischensumme:</strong> "
						+ this.removeZeros(this.totalAmounts.get(i) * 0.81) + " €\r\n" + "												</p>\r\n"
						+ "											</div>\r\n"
						+ "										</div>\r\n"
						+ "										<div class=\"row\">\r\n"
						+ "											<div class=\"col\">\r\n"
						+ "												<p>\r\n"
						+ "													<strong>MwSt (19%):</strong> "
						+ String.valueOf(this.removeZeros(this.totalAmounts.get(i) * 0.19)) + " €\r\n"
						+ "												</p>\r\n"
						+ "											</div>\r\n"
						+ "										</div>\r\n"
						+ "										<hr>\r\n"
						+ "										<div class=\"row\">\r\n"
						+ "											<div class=\"col\">\r\n"
						+ "												<h3>\r\n"
						+ "													<strong>Gesamtsumme:</strong> <u>"
						+ String.valueOf(this.removeZeros(this.totalAmounts.get(i))) + " €</u>\r\n"
						+ "												</h3>\r\n"
						+ "											</div>\r\n"
						+ "										</div>\r\n"
						+ "									</div>\r\n" + "								</div>\r\n"
						+ "							</div>\r\n" + "						</div>";
			}
		}
		return html;

	}

	public void readInvoiceItems(Integer invoiceId) throws SQLException {
		this.productId.clear();
		this.quantity.clear();
		this.pricePerUnit.clear();

		String sql = "SELECT product_id, quantity, price_per_unit FROM bwi520_633899.invoice_items WHERE invoice_id=?;";
		System.out.println(sql);
		PreparedStatement prep = dbConn.prepareStatement(sql);
		prep.setInt(1, invoiceId);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next()) {
			this.productId.add(dbRes.getInt("product_id"));
			this.quantity.add(dbRes.getInt("quantity"));
			this.pricePerUnit.add(dbRes.getDouble("price_per_unit"));
		}

	}

	public boolean checkInInvoice(Integer productId, Integer invoiceId) throws SQLException {
		boolean exists = false;
		if (productId != 0) {
			String sql = "SELECT * FROM bwi520_633899.invoice_items WHERE product_id=? AND invoice_id=?;";
			System.out.println(sql);
			PreparedStatement prep = dbConn.prepareStatement(sql);
			prep.setInt(1, productId);
			prep.setInt(2, invoiceId);
			ResultSet dbRes = prep.executeQuery();
			if (dbRes.next()) {
				exists = true;
			}
		}
		return exists;
	}

	public void setInvoicesEmpty() {
		this.invoiceId.clear();
		this.invoiceDates.clear();
		this.totalAmounts.clear();
		this.paymentMethods.clear();
		this.paymentStatus.clear();
		this.totalAmountsTax.clear();
		this.productId.clear();
		this.quantity.clear();
		this.pricePerUnit.clear();
		System.out.println("cleared invoices");
	}

	public void sendInvoiceMail(String invoiceId) throws MessagingException, IOException {
		if(this.getEmail() != null && !this.getEmail().isBlank()) {
			String html = "Hallo " + this.getFirstName() + " " +  this.getLastName() + ","
					    + "<p>Im Anhang finden Sie die von Ihnen angeforderte Rechnung.</p>";
			MailBean mail = new MailBean(); 
			mail.setEmail(this.getEmail());
			mail.sendInvoiceAgain(html, String.valueOf(this.getUser_id()), invoiceId);
		}
	}
	
	public String removeZeros(double price) {
		DecimalFormat format = new DecimalFormat("0.##");
		return format.format(price);
	}

	// Getter und Setter-Methoden

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public boolean isEmployee() {
		return employee;
	}

	public void setEmployee(boolean employee) {
		this.employee = employee;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public int getPostcode() {
		return postcode;
	}

	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}

	public boolean isPasswordChanged() {
		return passwordChanged;
	}

	public void setPasswordChanged(boolean passwordChanged) {
		this.passwordChanged = passwordChanged;
	}

	public String getMessageHtml() {
		String html = this.getMessage();
		this.setMessage("");
		return html;
	}

	public String getMessage() {
		return this.viewMessage;
	}

	public void setMessage(String message) {
		this.viewMessage = message;
	}

	public ArrayList<Integer> getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(ArrayList<Integer> invoiceId) {
		this.invoiceId = invoiceId;
	}

	public ArrayList<String> getInvoiceDates() {
		return invoiceDates;
	}

	public void setInvoiceDates(ArrayList<String> invoiceDates) {
		this.invoiceDates = invoiceDates;
	}

	public ArrayList<Double> getTotalAmounts() {
		return totalAmounts;
	}

	public void setTotalAmounts(ArrayList<Double> totalAmounts) {
		this.totalAmounts = totalAmounts;
	}

	public ArrayList<String> getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(ArrayList<String> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	public ArrayList<String> getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(ArrayList<String> paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public ArrayList<Double> getTotalAmountsTax() {
		return totalAmountsTax;
	}

	public void setTotalAmountsTax(ArrayList<Double> totalAmountsTax) {
		this.totalAmountsTax = totalAmountsTax;
	}

	public ArrayList<Integer> getRelevantFields() {
		return relevantFields;
	}

	public void setRelevantFields(ArrayList<Integer> relevantFields) {
		this.relevantFields = relevantFields;
	}

	public ArrayList<Integer> getProductId() {
		return productId;
	}

	public void setProductId(ArrayList<Integer> productId) {
		this.productId = productId;
	}

	public ArrayList<Integer> getQuantity() {
		return quantity;
	}

	public void setQuantity(ArrayList<Integer> quantity) {
		this.quantity = quantity;
	}

	public ArrayList<Double> getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(ArrayList<Double> pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

}
