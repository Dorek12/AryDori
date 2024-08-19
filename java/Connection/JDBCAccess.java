package Connection;

import java.sql.*;

public abstract class JDBCAccess {

	Connection dbConn;
	String dbDrivername;
	String dbURL;
	String dbUser;
	String dbPassword;
	String dbSchema;
	public JDBCAccess() throws NoConnectionException{
		this.setDBParms();
		this.createConnection();
		this.setSchema();
	}
	public abstract void setDBParms();
	
	public void setSchema() throws NoConnectionException {
		try{
			String sql = "SET SCHEMA '" + dbSchema + "'";
			System.out.println(sql);
			dbConn.createStatement().executeUpdate(sql);
			System.out.println("Schema " + dbSchema + " erfolgreich gesetzt");
		}catch(SQLException se){
			se.printStackTrace();
			throw new NoConnectionException();
		}
	}
	public void createConnection() {
		try{
			Class.forName(dbDrivername);
			System.out.println("JDBC-Treiber erfolgreich geladen");
	
			dbConn = DriverManager.getConnection(
												dbURL,
												dbUser,
												dbPassword
												);
			System.out.println("Datenbankverbindung erfolgreich angelegt");
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}
	public Connection getConnection() throws NoConnectionException {
		try{
			this.setSchema();
			return dbConn;
		}catch(SQLException se){
			se.printStackTrace();
			throw new NoConnectionException();

		}
	}

}
