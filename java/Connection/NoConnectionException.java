package Connection;

import java.sql.*;

@SuppressWarnings("serial")
public class NoConnectionException extends SQLException {
	
	public NoConnectionException(){
		super("Erzeugung der JDBC-Connection ist fehlgeschlagen");
	}
	public NoConnectionException(String msg){
		super(msg);
	}
	
}
