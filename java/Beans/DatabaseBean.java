package Beans;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Connection.NoConnectionException;
import Connection.PostgreSQLAccess;

public class DatabaseBean {

	Connection dbConn;
	int countRows;
	int countCols;
	String dbName;
	String primaryKeyColumnName;
	String sqlCode;
	String message;
	int rowToEdit;
	boolean loggedIn;
	boolean employee;
	ArrayList<String> columnNames;
	ArrayList<String> dataTypes;

	public DatabaseBean() throws NoConnectionException {
		this.createConnection();
		this.countRows = 0;
		this.countCols = 0;
		this.dbName = "";
		this.rowToEdit = 0;
		this.columnNames = new ArrayList<String>();
		this.dataTypes = new ArrayList<String>();
		this.message = "";
		this.setLoggedIn(false);
		this.setEmployee(false);
	}

	// Connection-Methode
	public void createConnection() throws NoConnectionException {
		this.dbConn = new PostgreSQLAccess().getConnection();
	}

	// Bestimmt die Reihen und Spalten der ausgewählten Datenbank 
	public void getDimensions(String dbName) throws SQLException {
		this.createConnection();
		String sql = "SELECT COUNT(*) FROM " + dbName + ";";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		if (dbRes.next()) {
			countRows = dbRes.getInt(1);
		}
		System.out.println("Row Count: " + countRows);
		sql = "SELECT COUNT(*) FROM information_schema.columns WHERE table_name = '" + dbName
				+ "' AND table_schema = 'bwi520_633899';";
		System.out.println(sql);
		prep = this.dbConn.prepareStatement(sql);
		dbRes = prep.executeQuery();
		if (dbRes.next()) {
			countCols = dbRes.getInt(1);
		}
		this.setDbName(dbName);
		sql = "SELECT data_type FROM information_schema.columns WHERE table_schema = 'bwi520_633899' AND table_name = '"
				+ this.dbName + "';";
		System.out.println(sql);
		prep = this.dbConn.prepareStatement(sql);
		dbRes = prep.executeQuery();
		while (dbRes.next()) {
			String dataType = dbRes.getString("data_type");
			this.dataTypes.add(dataType);
		}
		System.out.println("Column Count: " + countCols);
	}
	
	// gibt die aktuell ausgewählte Tabelle aus

	public String getTableAsHtml() throws SQLException {
		String html = "";
		if (this.isLoggedIn() && this.isEmployee()) {

			if (!(dbName.equals(""))) {
				html += "<h2><u>" + this.dbName + "-Table</u></h2><input type='submit' value='+' name='btnAddToDb'>";
				String sql = "SELECT * FROM bwi520_633899." + this.dbName + ";";
				System.out.println(sql);
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				ResultSet dbRes = prep.executeQuery();
				this.primaryKeyColumnName = findPrimaryKeyColumnName(this.dbConn, this.dbName, "bwi520_633899");
				html += "<table><tr>";
				for (int i = 1; i <= this.countCols; i++) {
					this.columnNames.add(dbRes.getMetaData().getColumnName(i));
					html += "<th><u>" + dbRes.getMetaData().getColumnName(i) + "</u><br>(" + this.dataTypes.get(i - 1)
							+ ")</th>";
				}
				html += "<th><u>Edit</u></th>";
				html += "<th><u>Delete</u></th></tr>";

				while (dbRes.next()) {
					html += "<tr>";
					for (int i = 1; i <= this.countCols; i++) {
						html += "<td>" + dbRes.getString(i) + "</td>";
					}
					html += "<td><a href='../Appls/DatabaseAppl.jsp?rowId=" + dbRes.getInt(primaryKeyColumnName)
							+ "&edit=true'>Edit</a></td>";
					html += "<td><a href='../Appls/DatabaseAppl.jsp?rowId=" + dbRes.getInt(primaryKeyColumnName)
							+ "&delete=true'>Delete</a></td>";
					html += "</tr>";
				}

				html += "</table>\r\n" + "</form>\r\n" + "</div><div>";
			}
		}

		return html;

	}
	
	// gibt den Datensatz der editiert werden soll aus

	public String getEditHtml() throws SQLException {
		String html = "";
		if (this.isLoggedIn() && this.isEmployee()) {
			html = "<form id='db' action='../Appls/DatabaseAppl.jsp' method='get'>\r\n"
					+ "<table>";
			String sql = "SELECT * FROM " + this.dbName + " WHERE " + this.primaryKeyColumnName + " = '"
					+ this.rowToEdit + "';";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			ResultSet dbRes = prep.executeQuery();
			while (dbRes.next()) {
				for (int i = 0; i <= this.countCols - 2; i++) {
					if (i == 0) {

					} else {
						html += "<tr><td><label>" + this.columnNames.get(i + 1) + "<br>(" + this.dataTypes.get(i)
								+ ")</label></td>";
						html += "<td><input type='text' name='" + this.columnNames.get(i + 1) + "' value='"
								+ dbRes.getString(i + 1) + "'></td></tr>";
					}
				}
			}
			html += "</table>\r\n" + "<input type='submit' value='Update' name='btnEdit'>\r\n"
					+ "<input type='submit' value='Back' name='btnBack'>\r\n" + "</form>\r\n";
		} else
			html = this.getErrorpageAsHtml();
		
		return html;
	}

	public String getAddHtml() throws SQLException {
		String html = "";
		if (this.isLoggedIn() && this.isEmployee()) {
			html = "<form id='db' action='../Appls/DatabaseAppl.jsp' method='get'>\r\n"
					+ "<table>";
			for (int i = 0; i <= this.countCols - 2; i++) {
				if (i == 0) {

				} else {
					html += "<tr><td><label>" + this.columnNames.get(i + 1) + "<br>(" + this.dataTypes.get(i)
							+ ")</label></td>";
					html += "<td><input type='text' name='" + this.columnNames.get(i + 1) + "' value=''></td></tr>";
				}
			}
			html += "</table>\r\n<input type='submit' value='Add to Database' name='btnAdd'>\r\n"
					+ "<input type='submit' value='Back' name='btnBack'>" + "<form>\r\n";
		} else
			html = this.getErrorpageAsHtml();
		
		return html;
	}

	public String getSelectOptionsAsHtml() {
		String html = "";
		if (this.isLoggedIn() && this.isEmployee()) {
			html = "</div><div class='dbContainer'>\r\n"
					+ "<form id='db' action='../Appls/DatabaseAppl.jsp' method='get'>\r\n" + "\r\n"
					+ "<select name='dbName' id='dbName'>\r\n" + "<option value='users'>Accounts</option>\r\n"
					+ "<option value='products'>Products</option>\r\n"
					+ "<option value='cart'>Shopping Cart</option>\r\n"
					+ "<option value='wishlist'>Wishlist</option>\r\n"
					+ "<option value='invoice_items'>Invoice Items</option>\r\n"
					+ "<option value='invoice_details'>Invoice Details</option>\r\n"
					+ "<option value='reviews'>Reviews</option>\r\n" + "</select>\r\n" + "\r\n"
					+ "<input type='submit' value='Switch Database' name='btnChange'>\r\n" + "";
		}
		return html;
	}
	
	// erstellt zunächst eine leere Nachricht die dann epassend gesetzt wird

	public String getMessageHtml() throws SQLException {
		String html = "";
		if (this.isLoggedIn() && this.isEmployee()) {
			html +=  this.getSelectOptionsAsHtml();
			html += "<p>" + this.getMessage() + "</p>";
			this.setMessage("");
			html += this.getTableAsHtml();
			html += "</div>";
		} else
			html = this.getErrorpageAsHtml();

		return html;
	}

	// Error

	public String getErrorpageAsHtml() {
		return "<div class='error_container'>\r\n<div class='error_code'>404 Not Found</div>\r\n"
				+ "<div class='error_message'>Etwas ist schiefgelaufen! Bitte\r\n"
				+ "	versuchen Sie es später erneut.</div>\r\n<div class='smiley'>&#128546;</div>\r\n"
				+ "<a class='link'\r\n href='../Views/MainpageView.jsp'>Zurück\r\n" + "	zur Startseite</a>\r\n</div>";
	}

	// editiert einen Datensatz aus der EditView
	public void changeDb(ArrayList<String> contentToChange) throws SQLException {
		try {
			String sql = "UPDATE bwi520_633899." + this.dbName + " SET ";
			for (int i = 2; i <= contentToChange.size() - 1; i++) {
				if (i == contentToChange.size() - 1) {
					sql += this.columnNames.get(i) + " = '" + contentToChange.get(i) + "'";
				} else {
					sql += this.columnNames.get(i) + " = '" + contentToChange.get(i) + "', ";
				}
			}
			sql += " WHERE " + this.primaryKeyColumnName + " = " + this.rowToEdit + ";";

			System.out.println(sql);
			this.sqlCode = sql;
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.executeUpdate();
		} catch (Exception e) {
			setMessage("SQL Befehl: " + this.sqlCode
					+ "<br>Die eingegebenen Daten sind nicht richtig. Prüfe deine Eingabe.");
		}
	}
	
	// fügt einen Datensatz hinzu über die AddView

	public void addToDb(ArrayList<String> contentToAdd) throws SQLException {
		try {
			String sql = "INSERT INTO bwi520_633899." + this.dbName + "(";
			for (int i = 2; i <= contentToAdd.size() - 1; i++) {
				if (i == contentToAdd.size() - 1) {
					sql += columnNames.get(i);
				} else {
					sql += columnNames.get(i) + ", ";
				}
			}
			sql += ") VALUES (";
			for (int i = 2; i <= contentToAdd.size() - 1; i++) {
				if (i == contentToAdd.size() - 1) {
					sql += "'" + contentToAdd.get(i) + "'";
				} else {
					sql += "'" + contentToAdd.get(i) + "', ";
				}
			}
			sql += ");";
			System.out.println(sql);
			this.sqlCode = sql;
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.executeUpdate();
		} catch (Exception e) {
			setMessage("SQL Befehl: " + this.sqlCode + "<br>Eure Eingabe ist nicht korrekt.");
		}
	}

	public void deleteRow() throws SQLException {
		try {
			String sql = "DELETE FROM " + this.dbName + " WHERE " + this.primaryKeyColumnName + " = " + this.rowToEdit
					+ ";";
			System.out.println(sql);
			this.sqlCode = sql;
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.executeUpdate();
		} catch (Exception e) {
			setMessage("SQL Befehl: " + this.sqlCode + "<br>Diese Zeile kann zurzeit nicht gelöscht werden.");
		}
	}
	
	// setzt die Spaltennamen der ausgewählten Datenbank 

	public void setCurrentColumnNameList(String dbName) throws SQLException {
		String sql = "SELECT * FROM bwi520_633899." + this.dbName + ";";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		for (int i = 1; i <= this.countCols; i++) {
			this.columnNames.clear();
			this.columnNames.add(dbRes.getMetaData().getColumnName(i));
		}
	}

	public void clearDataTypes() {
		this.dataTypes.clear();
	}

	// findet mit Metadaten den Spaltennamen des Primary Keys heraus
	public String findPrimaryKeyColumnName(Connection dbConn, String dbName, String schemaName) throws SQLException {
		DatabaseMetaData metaData = dbConn.getMetaData();
		ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(null, schemaName, dbName);

		if (primaryKeyResultSet.next()) {
			String primaryKeyColumnName = primaryKeyResultSet.getString("COLUMN_NAME");
			return primaryKeyColumnName;
		}

		return null;
	}

	// Getters and Setters
	public int getCountRows() {
		return countRows;
	}

	public void setCountRows(int countRows) {
		this.countRows = countRows;
	}

	public int getCountCols() {
		return countCols;
	}

	public void setCountCols(int countCols) {
		this.countCols = countCols;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public int getRowToEdit() {
		return rowToEdit;
	}

	public void setRowToEdit(int rowToEdit) {
		this.rowToEdit = rowToEdit;
	}

	public ArrayList<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(ArrayList<String> columnNames) {
		this.columnNames = columnNames;
	}

	public ArrayList<String> getDataTypes() {
		return dataTypes;
	}

	public void setDataTypes(ArrayList<String> dataTypes) {
		this.dataTypes = dataTypes;
	}

	public String getSqlCode() {
		return sqlCode;
	}

	public void setSqlCode(String sqlCode) {
		this.sqlCode = sqlCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isEmployee() {
		return employee;
	}

	public void setEmployee(boolean employee) {
		this.employee = employee;
	}

}
