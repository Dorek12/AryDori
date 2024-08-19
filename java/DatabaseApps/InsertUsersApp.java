package DatabaseApps;

import java.sql.*;
import java.util.ArrayList;

import Connection.NoConnectionException;
import Connection.PostgreSQLAccess;

//Hier werden alle Benutzer in die Datenbank eingelesen

public class InsertUsersApp {

	ArrayList<String> vorname = new ArrayList<>();
	ArrayList<String> nachname = new ArrayList<>();
	ArrayList<String> strasse = new ArrayList<>();
	ArrayList<Integer> haus_nr = new ArrayList<>();
	ArrayList<String> stadt = new ArrayList<>();
	ArrayList<Integer> plz = new ArrayList<>();
	ArrayList<String> gb_datum = new ArrayList<>();
	ArrayList<String> email = new ArrayList<>();
	ArrayList<String> telephone = new ArrayList<>();
	ArrayList<Boolean> is_admin = new ArrayList<>();
	Connection dbConn;

	public void createConnection() throws NoConnectionException {
		this.dbConn = new PostgreSQLAccess().getConnection();
	}

	public void insertAllUsers() throws SQLException {
		this.createConnection();
		this.setUserArrays();
		for (int i = 0; i < this.vorname.size(); i++) {
			this.insertIntoUsers(this.vorname.get(i), this.nachname.get(i), this.strasse.get(i), this.haus_nr.get(i),
					this.stadt.get(i), this.plz.get(i), this.gb_datum.get(i), this.email.get(i),
					(this.vorname.get(i) + "12!"), this.telephone.get(i), this.is_admin.get(i));
		}
		System.out.println("Alle Benutzer wurden erfolgreich hinzugefügt");
	}

	public void insertIntoUsers(String vorname, String nachname, String strasse, int haus_nr, String stadt, int plz,
			String gb_datum, String email, String password, String telephone, boolean is_admin)
			throws SQLException {
		String sql = "INSERT INTO users (vorname, nachname, strasse, haus_nr, stadt, plz, gb_datum, email, password, telephone, is_admin) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setString(1, vorname);
		prep.setString(2, nachname);
		prep.setString(3, strasse);
		prep.setInt(4, haus_nr);
		prep.setString(5, stadt);
		prep.setInt(6, plz);
		prep.setString(7, gb_datum);
		prep.setString(8, email);
		prep.setString(9, password);
		prep.setString(10, telephone);
		prep.setBoolean(11, is_admin);

		prep.executeUpdate();
		System.out.println(vorname + " " + nachname + " wurde erfolgreich hinzugefügt");
	}

	public void setUserArrays() {
		vorname.add("Florian");
		nachname.add("Straßner");
		strasse.add("Im Ring");
		haus_nr.add(47);
		stadt.add("Germersheim");
		plz.add(76087);
		gb_datum.add("1998-12-03");
		email.add("straßner@test.de");
		telephone.add("062188888");
		is_admin.add(true);

		vorname.add("Endrit");
		nachname.add("Avdulli");
		strasse.add("Im Ring");
		haus_nr.add(45);
		stadt.add("Germersheim");
		plz.add(76087);
		gb_datum.add("1998-12-03");
		email.add("eni_avdulli@hotmail.de");
		telephone.add("062188888");
		is_admin.add(true);
/*
		first_name.add("Florian");
		last_name.add("Neumann");
		street.add("Im Ring");
		houseNumber.add(45);
		city.add("Germersheim");
		postcode.add(76087);
		dateOfBirth.add("1998-12-03");
		email.add("neumann_m@hotmail.de");
		telephone.add("062188888");
		isEmployee.add(true);

		first_name.add("Vanessa");
		last_name.add("Müller");
		street.add("Mutterstraße");
		houseNumber.add(45);
		city.add("Mutterstadt");
		postcode.add(76087);
		dateOfBirth.add("1976-11-03");
		email.add("vanessa.mueller@notexisting.de");
		telephone.add("062188888");
		isEmployee.add(false);

		first_name.add("Max");
		last_name.add("Schmidt");
		street.add("Hauptstraße");
		houseNumber.add(21);
		city.add("Mutterstadt");
		postcode.add(12345);
		dateOfBirth.add("1985-07-15");
		email.add("max.schmidt@notexisting.com");
		telephone.add("0123456789");
		isEmployee.add(false);

		first_name.add("Anna");
		last_name.add("Fischer");
		street.add("Seestraße");
		houseNumber.add(7);
		city.add("Musterstadt");
		postcode.add(76087);
		dateOfBirth.add("1990-04-25");
		email.add("anna.fischer@notexisting.com");
		telephone.add("0987654321");
		isEmployee.add(false);

		first_name.add("Michael");
		last_name.add("Meyers");
		street.add("Waldweg");
		houseNumber.add(12);
		city.add("Waldstadt");
		postcode.add(98765);
		dateOfBirth.add("1982-09-10");
		email.add("michael.meyers@notexisting.com");
		telephone.add("0765432109");
		isEmployee.add(false);

		first_name.add("Sophie");
		last_name.add("Star");
		street.add("Rosenweg");
		houseNumber.add(34);
		city.add("Blumenstadt");
		postcode.add(45678);
		dateOfBirth.add("1994-02-18");
		email.add("sophie.star@notexisting.com");
		telephone.add("0345678901");
		isEmployee.add(false);

		first_name.add("Patrick");
		last_name.add("Star");
		street.add("An der Blies");
		houseNumber.add(9);
		city.add("Flussstadt");
		postcode.add(56789);
		dateOfBirth.add("1988-06-22");
		email.add("patrick.star@notexisting.com");
		telephone.add("0456789012");
		isEmployee.add(false);

		first_name.add("Laura");
		last_name.add("Hoffmann");
		street.add("Ernst-Boehe-Straße");
		houseNumber.add(27);
		city.add("Sonnstadt");
		postcode.add(98765);
		dateOfBirth.add("1997-11-30");
		email.add("laura.hoffmann@notexisting.com");
		telephone.add("0897612345");
		isEmployee.add(false);

		first_name.add("David");
		last_name.add("Meyer");
		street.add("Straßenstraße");
		houseNumber.add(14);
		city.add("Baumstadt");
		postcode.add(34567);
		dateOfBirth.add("1991-03-05");
		email.add("david.meyer@notexisting.com");
		telephone.add("0567890123");
		isEmployee.add(false);

		first_name.add("Lisa");
		last_name.add("Koch");
		street.add("Straußenstraße");
		houseNumber.add(5);
		city.add("Waldstadt");
		postcode.add(23456);
		dateOfBirth.add("1996-08-12");
		email.add("lisa.koch@notexisting.com");
		telephone.add("0432167890");
		isEmployee.add(false);

		first_name.add("Paul");
		last_name.add("Wasser");
		street.add("Teststraße");
		houseNumber.add(19);
		city.add("Feldstadt");
		postcode.add(76543);
		dateOfBirth.add("1989-12-28");
		email.add("paul.wasser@notexisting.com");
		telephone.add("0765432109");
		isEmployee.add(false);
*/
	}
	
}
