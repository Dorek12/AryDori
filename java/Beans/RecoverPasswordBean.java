package Beans;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.mail.*;
import Connection.NoConnectionException;
import Connection.PostgreSQLAccess;
import jakarta.servlet.http.HttpServletRequest;

public class RecoverPasswordBean {

	String email;
	String password;
	String localhost;
	int user_id;
	long code;
	long checkCode;
	boolean linkSended;
	Connection dbConn;

	public RecoverPasswordBean() throws NoConnectionException {
		this.createConnection();
		this.setEmail("");
		this.setPassword("");
		this.setUser_id(Integer.MIN_VALUE);
		this.setCode(0); // Code und CheckCode müssen verschiedene Werte haben
		this.setCheckCode(Long.MIN_VALUE);
		this.setLinkSended(false);
	}

	// Connection-Methode

	public void createConnection() throws NoConnectionException {
		this.dbConn = new PostgreSQLAccess().getConnection();
	}

	public String getRecoverPasswordAsHtml() throws SQLException {
		String html = "";
		if (this.isLinkSended()) {
			html = "<div class='alert alert-info' role='alert'>\r\n"
					+ "        Wir haben Ihre Anfrage bearbeitet. Sollte ein Konto mit der von Ihnen angegebenen E-Mail-Adresse existieren, wird eine Mail an diese E-Mail-Adresse gesendet. Bitte überprüfen Sie Ihr Postfach. \r\n"
					+ "    </div>\r\n";
		} else if (!this.checkIfCodeCorrect()) {
			html = " <div class='alert alert-info' role='alert'>\r\n"
					+ "        Bitte geben Sie Ihre E-Mail-Adresse ein um Ihr Passwort zu ändern.\r\n"
					+ "    </div>\r\n" + "\r\n <div class='container' style='margin-top: 50px;'>\r\n";
		}
		html += "		<div id='login-container'>\r\n"
				+ "			<form action='../Appls/AccountAppl.jsp' method='get' onsubmit='return checkEmailForRecovery()'>\r\n"
				+ "				<div class='form-group'>\r\n"
				+ "					<label for='email'>E-Mail:</label> <input type='text'\r\n"
				+ "						class='form-control' value='' name='email' id='recoverEmail'\r\n"
				+ "						placeholder='Geben Sie hier Ihre E-Mail-Adresse ein'>\r\n"
				+ "				</div>\r\n"
				+ "<p id='recoverEmailMsg' style='color: red;'></p>"
				+ "				<button type='submit' class='btn btn-primary' id='btnApplyChange'\r\n"
				+ "					name='btnApplyChange' value='applyChange'>Neues Passwort\r\n"
				+ "					beantragen</button>\r\n" + "			</form>\r\n" + "		</div>\r\n"
				+ "	</div>";
		if (this.checkIfCodeCorrect()) {
			html = "<div class='container' style='margin-top: 50px;'>\r\n" + "		<div id='login-container'>\r\n"
					+ "			<form id='login' action='../Appls/AccountAppl.jsp' method='get'>\r\n"
					+ "				<div class='form-group'>\r\n"
					+ "					<label for='password'>Neues Passwort:</label> <input\r\n"
					+ "						type='password' class='form-control' name='password' id='recoverPassword'\r\n"
					+ "						placeholder='Geben Sie hier Ihr neues Passwort ein'>\r\n"
					+ "				</div>\r\n" + "				<div class='form-group'>\r\n"
					+ "					<label for='password'>Neues Password bestätigen:</label> <input\r\n"
					+ "						type='password' class='form-control' name='password' id='recoverPasswordCheck'\r\n"
					+ "						placeholder='Passwort bestätigen'>\r\n"
					+ "				</div>\r\n"
					+ "<p id='recoverPasswordMsg' style='color: red;'></p>"
					+ "				<button type='submit' class='btn btn-primary' id='btnLog'\r\n"
					+ "					name='btnChangePassword' value='changePassword' onclick='return checkPasswordToRecover()'>Passwort\r\n"
					+ "					jetzt ändern</button>\r\n" + "			</form>\r\n" + "		</div>\r\n"
					+ "	</div>";
		}
		this.setLinkSended(false);
		return html;
	}

	public boolean checkIfUserExist() throws SQLException {
		boolean userExist = false;
		if (this.getEmail() != null && !this.getEmail().isBlank()) {
			String sql = "SELECT user_id FROM users WHERE email ILIKE ?";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setString(1, this.getEmail());
			ResultSet dbRes = prep.executeQuery();
			if (dbRes.next()) {
				this.setUser_id(dbRes.getInt("user_id"));
				userExist = true;
			}
		}
		return userExist;
	}

	public void sendLinkWithCode() throws SQLException, MessagingException {
		if (this.checkIfUserExist() && this.getUser_id() > 0 && this.getEmail() != null && !this.getEmail().isBlank()) {
			this.setRandomCode();
			String link = "http://" + this.localhost + "/NAS_Shop/JSP/Appls/AccountAppl.jsp?Code=" + this.getCode();
			MailBean mail = new MailBean();
			mail.setEmail(this.getEmail());
			mail.sendForgetPasswordMail(link);
		}
		this.setLinkSended(true); // setze auf True auch wenn keine Mail gesendet wurde, damit es immer so
									// aussieht als wäre eine Mail verschickt worden, so kann niemand testen ob eine bestimmte
									// E-Mail-Adresse ein Konto bei uns hat
	}

	// Generiert zufälligen Code der 8 Stellen hat und hängt die Userid ans Ende, durch die Userid ist der Code immer unique

	public void setRandomCode() {
		String code = "";
		int min = 10000000;
		int max = 99999999;
		code += String.valueOf((min + (int) (Math.random() * ((max - min) + 1))));
		code += this.getUser_id();
		this.setCode(Long.parseLong(code));
		code = "";
	}

	// Check ob Code existiert und ob mit checkCode übereinstimmt

	public boolean checkIfCodeCorrect() {
		boolean codeCorrect = false;
		if (this.getCode() == this.getCheckCode())
			codeCorrect = true;
		return codeCorrect;
	}

	// ändere das Passwort des Users

	public void changePassword() throws SQLException {
		if (this.getUser_id() != Integer.MIN_VALUE) {
			// checke nochmal ob E-Mail und user_id übereinstimmen
			if (this.checkEmailAndUserId()) {
				String sql = "UPDATE users SET password = ? WHERE user_id = ?";
				System.out.println(sql);
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setString(1, this.getPassword());
				prep.setInt(2, this.getUser_id());
				prep.execute();
				this.setCode(0); // setze Code zurück
				this.setCheckCode(Long.MIN_VALUE);
				this.setUser_id(Integer.MIN_VALUE);
			}
		}
	}

	public boolean checkEmailAndUserId() throws SQLException {
		String sql = "SELECT * FROM users WHERE email = ? and user_id = ?";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setString(1, this.getEmail());
		prep.setInt(2, this.getUser_id());
		ResultSet dbRes = prep.executeQuery();
		return dbRes.next();
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

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public long getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(long checkCode) {
		this.checkCode = checkCode;
	}

	public void setLocalhost(String localhost) {
		this.localhost = localhost;
	}

	public String getUserHost(HttpServletRequest request) {
		localhost = request.getHeader("Host");
		return localhost;
	}

	public boolean isLinkSended() {
		return linkSended;
	}

	public void setLinkSended(boolean linkSended) {
		this.linkSended = linkSended;
	}

}
