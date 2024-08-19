package DatabaseApps;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Connection.NoConnectionException;
import Connection.PostgreSQLAccess;

public class TablesApp {

	Connection dbConn;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// Create
		TablesApp app = new TablesApp();
		app.createConnection();
		app.dropMethod();
		app.createMethod();
//		app.clearMethod();
		app.insertDataIntoDb();
		// Generic
//		String tableName = "products"; // alle lowercase tableName(users, products, wishlist, reviews, invoice,
										// cart)
//		app.createTable(app, tableName);
//		app.clearTable(tableName);
//		app.dropTable(tableName);

		
		// Create
//		app.createTableUsers();
//		app.createTableProducts();
//		app.createTableWishlist();
//		app.createTableReviews();
//		app.createTableInvoice();
//		app.createTableCart();

		// Clear
//		app.createTableUsers();
//		app.clearTableProducts();
//		app.clearTableWishlist();
//		app.clearTableReviews();
//		app.clearTableInvoice();
//		app.clearTableCart();

		// Drop
//		app.dropTableWishlist();
//		app.dropTableReviews();
//		app.dropTableInvoice();
//		app.dropTableCart();
//		app.dropTableUsers();
//		app.dropTableProducts();

	}
	
	public void insertDataIntoDb() throws SQLException, IOException {
		InsertProdukteApp insertIntoProdukte = new InsertProdukteApp();
		insertIntoProdukte.insertAllProdukte();
		InsertProduktBilderApp insertIntoBilder = new InsertProduktBilderApp();
		insertIntoBilder.insertBilder();
		InsertUsersApp insertIntoUsers = new InsertUsersApp();
		insertIntoUsers.insertAllUsers();

	}

	public void createMethod() throws ClassNotFoundException, SQLException {
		this.createTableUsers();
		this.createTableProdukte();
		this.createTableRechnung();
		this.createTableWarenkorb();
		this.createTableBilder();
	}

	public void clearMethod() throws ClassNotFoundException, SQLException {
		this.createTableUsers();
		this.clearTableProdukte();
		this.clearTableRechnung();
		this.clearTableWarenkorb();
		this.clearTableBilder();
	}

	public void dropMethod() throws ClassNotFoundException, SQLException {
		this.dropTableRechnung();
		this.dropTableWarenkorb();
		this.dropTableUsers();
		this.dropTableBilder();
		this.dropTableProdukte();
	}

	// Create Tables
	
	public void createTable(TablesApp app, String tableName) throws SQLException, ClassNotFoundException {
		switch (tableName) {
		case "users":
			app.createTableUsers();
			break;
		case "products":
			app.createTableProdukte();
			break;
		case "rechnung":
			app.createTableRechnung();
			break;
		case "warenkorb":
			app.createTableWarenkorb();
			break;
		default:
			break;
		}
	}

	public void createTableUsers() throws SQLException, ClassNotFoundException {
		this.createConnection();
		this.dropTable("users");

		String sql = "CREATE TABLE users ("
				+ "    user_id 				SERIAL 			PRIMARY KEY,"
				+ "    vorname 				VARCHAR(50) 	NOT NULL,"
				+ "    nachname 			VARCHAR(50) 	NOT NULL,"
				+ "    strasse 				VARCHAR(100)			," 
				+ "    haus_nr 				VARCHAR(10)				,"
				+ "    stadt 				VARCHAR(100)			," 
				+ "    plz 					INTEGER					,"
				+ "    gb_datum 			VARCHAR(20) 	NOT NULL,"
				+ "    email 				VARCHAR(100) 	UNIQUE NOT NULL,"
				+ "    password 			VARCHAR(100)			,"
				+ "	   telephone 			VARCHAR(20)				,"
				+ "    is_admin 			BOOLEAN 		NOT NULL DEFAULT false,"
				+ "    account_erstellt_am 	TIMESTAMP 		DEFAULT NOW()"
				+ 	 ")";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.executeUpdate();
		System.out.println("Tabelle users wurde angelegt");
	}

	public void createTableProdukte() throws SQLException, ClassNotFoundException {
		this.createConnection();
		this.dropTable("produkte");

		String sql = "CREATE TABLE products ( "
				+ "    product_id 		SERIAL 				PRIMARY KEY,"
				+ "    groesse			VARCHAR(15)			NOT NULL,"	
				+ "    marke			VARCHAR(100) 		NOT NULL,"
				+ "    kategorie	 	VARCHAR(50) 		NOT NULL,"
				+ "    produkt_name 	VARCHAR(100) 		NOT NULL,"
				+ "    preis 			DOUBLE PRECISION 	NOT NULL, "
				+ "    verfuegbarkeit 	INTEGER						, "
				+ "    farbe 			VARCHAR(16) 		NOT NULL,"
				+ "    beschreibung 	Text						, "
				+ "    gender 			VARCHAR(30) 		NOT NULL"
				+ ")";
	
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.executeUpdate();
		System.out.println("Tabelle produkte wurde angelegt");
		// Alter table
		sql = "ALTER SEQUENCE produkte_product_id_seq RESTART WITH 633001";
		System.out.println(sql);
		prep = this.dbConn.prepareStatement(sql);
		prep.executeUpdate();
		System.out.println("Tabelle produkte angepasst auf Startwert 100000");
	}

	public void createTableRechnung() throws SQLException, ClassNotFoundException {
		this.createConnection();
		this.dropTable("rechnung_details");
		this.dropTable("rechnung_items");

		String sql = "CREATE TABLE rechnung_details ("
				+ "    rechn_id 			SERIAL 			PRIMARY KEY,"
				+ "    user_id 				INTEGER 		REFERENCES users(user_id),"
				+ "    rechn_datum 			VARCHAR(20) 	NOT NULL,"
				+ "    total_preis 			NUMERIC(10, 2) 	NOT NULL,"
				+ "    zahlung_methode 		VARCHAR(50) 	NOT NULL,"
				+ "    zahlung_status 		VARCHAR(20) 	NOT NULL," 
				+ "    erstellt_am 			TIMESTAMP 		DEFAULT NOW()"
				+ ")";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.executeUpdate();
		System.out.println("Tabelle invoice_details wurde angelegt");

		sql = "CREATE TABLE rechnung_items ("
				+ "    item_id 				SERIAL 			PRIMARY KEY,"
				+ "    rechnung_id 			INTEGER 		REFERENCES rechnung_details(rechnung_id),"
				+ "    product_id 			INTEGER 		REFERENCES produkte(product_id)," 
				+ "    menge 				INTEGER 		NOT NULL,"
				+ "    preis_pro_stueck 	NUMERIC(10, 2) 	NOT NULL," 
				+ "    erstellt_am 			TIMESTAMP 		DEFAULT NOW()" 
				+ ")";
		System.out.println(sql);
		prep = this.dbConn.prepareStatement(sql);
		prep.executeUpdate();
		System.out.println("Tabelle invoice_items wurde angelegt");
	}

	public void createTableWarenkorb() throws SQLException, ClassNotFoundException {
		this.createConnection();
		this.dropTable("order_history");

		String sql = "CREATE TABLE warenkorb ("
				+ "    wrk_id 			SERIAL 		PRIMARY KEY,"
				+ "    user_id 			INTEGER 	REFERENCES users(user_id),"
				+ "    product_id 		INTEGER 	REFERENCES produkte(product_id),"
				+ "    anzahl	 		INTEGER 	DEFAULT 1," 
				+  ")";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.executeUpdate();
		System.out.println("Tabelle warenkorb wurde angelegt");
	}

	public void createTableBilder() throws SQLException {
		String sql = "CREATE TABLE bilder ("
				+ "	bild_id 	SERIAL 		PRIMARY KEY, "
				+ "	product_id 	INT 		REFERENCES produkte(product_id),"
				+ " bild_byte 	BYTEA 		NOT NULL)";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.executeUpdate();
		System.out.println("Tabelle bilder wurde angelegt");
	}

	// Clear Tables
	
	public void clearTable(String tableName) throws SQLException {
		this.dbConn.createStatement().executeUpdate("DELETE FROM " + tableName);
		System.out.println("Tabelle " + tableName + " wurde gecleared");
	}

	
	public void clearTableAccount_Customer() throws SQLException {
		this.dbConn.createStatement().executeUpdate("DELETE FROM users");
		System.out.println("Tabelle users wurde gecleared");
	}

	public void clearTableProdukte() throws SQLException {
		this.dbConn.createStatement().executeUpdate("DELETE FROM produkte");
		System.out.println("Tabelle produkte wurde gecleared");
	}



	public void clearTableRechnung() throws SQLException {
		this.dbConn.createStatement().executeUpdate("DELETE FROM invoice_details, invoice_items");
		System.out.println("Tabellen invoice_details und invoice_items wurden gecleared");
	}

	public void clearTableWarenkorb() throws SQLException {
		this.dbConn.createStatement().executeUpdate("DELETE FROM warenkorb");
		System.out.println("Tabelle warenkorb wurde gecleared");
	}

	public void clearTableBilder() throws SQLException {
		this.dbConn.createStatement().executeUpdate("DELETE FROM bilder");
		System.out.println("Tabelle bilder wurde gecleared");
	}

	// Drop Tables
	
	public void dropTable(String tableName) throws SQLException {
		this.dbConn.createStatement().executeUpdate("DROP TABLE IF EXISTS " + tableName);
		System.out.println("Tabelle " + tableName + " wurde gelöscht");
	}

	
	public void dropTableUsers() throws SQLException {
		this.dbConn.createStatement().executeUpdate("DROP TABLE IF EXISTS users");
		System.out.println("Tabelle users wurde gelöscht");
	}

	public void dropTableProdukte() throws SQLException {
		this.dbConn.createStatement().executeUpdate("DROP TABLE IF EXISTS produkte");
		System.out.println("Tabelle produkte wurde gelöscht");
	}

	public void dropTableRechnung() throws SQLException {
		this.dbConn.createStatement().executeUpdate("DROP TABLE IF EXISTS invoice_details, invoice_items");
		System.out.println("Tabellen invoice_details und invoice_items wurden gelöscht");
	}

	public void dropTableWarenkorb() throws SQLException {
		this.dbConn.createStatement().executeUpdate("DROP TABLE IF EXISTS warenkorb");
		System.out.println("Tabelle warenkorb wurde gelöscht");
	}

	public void dropTableBilder() throws SQLException {
		this.dbConn.createStatement().executeUpdate("DROP TABLE IF EXISTS bilder");
		System.out.println("Tabelle bilder wurde gelöscht");
	}

	// Connection
	
	public void createConnection() throws NoConnectionException {
		this.dbConn = new PostgreSQLAccess().getConnection();
	}
/*
	// Insert Test
	public void insert() throws SQLException {
		String sql = "insert into products (brand, category, product_name, price, physical_memory, ram, stock, description, operating_system, general_keyword) values "
				+ "('apple', 'smartphone', 'iphone 14', '1382.27', '32' , '16', '8', 'cooles Ding', 'iOS','iphone')";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.executeUpdate();
	}

	public void insert2() throws SQLException {
		String sql = "insert into products (brand, category, product_name, price, physical_memory, ram, stock, description, operating_system, general_keyword) values "
				+ "('samsung', 'smartphone', 'galaxy s3', '1382.27', '128' , '8', '8', 'cooles Ding', 'Android','galaxy')";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.executeUpdate();
	}

	public void insert3() throws SQLException {
		String sql = "insert into products (brand, category, product_name, price, physical_memory, ram, stock, description, operating_system, general_keyword) values "
				+ "('apple', 'laptop', 'macbook air m2', '1382.27', '256' , '12', '8', 'cooles Ding', 'iOS','macbook')";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.executeUpdate();
	}
*/
}
