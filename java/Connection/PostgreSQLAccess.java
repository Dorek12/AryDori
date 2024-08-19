package Connection;

public class PostgreSQLAccess extends JDBCAccess {

	public PostgreSQLAccess() throws NoConnectionException {
		super();
	}
	public void setDBParms(){
		dbDrivername = "org.postgresql.Driver";
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ für uni
		//dbURL        = "jdbc:postgresql://143.93.200.243:5435/BWUEBDB";
		//dbUser       = "user1";
		//dbPassword   = "pgusers";
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ für daheim/lokal
		dbURL        = "jdbc:postgresql://localhost:5432/postgres";
		dbUser       = "postgres";
		dbPassword   = "1";
		dbSchema     = "bwi520_636807"; 
		
	}
	
	
	public static void main(String[] args) throws NoConnectionException { 
		new PostgreSQLAccess().getConnection();
		
	}
}