package DatabaseApps;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import Connection.NoConnectionException;
import Connection.PostgreSQLAccess;

//Hier werden alle Ratings in die Datenbank eingelesen

public class InsertRatingsApp {

	ArrayList<Integer> allUsers = new ArrayList<Integer>();
	ArrayList<Integer> allProducts = new ArrayList<Integer>();
	Connection dbConn;

	public void createConnection() throws NoConnectionException {
		this.dbConn = new PostgreSQLAccess().getConnection();
	}

	public void insertAllRatings() throws SQLException {
		this.createConnection();
		this.setUsersQuantity();
		this.setProductsQuantity();
		this.insertIntoRatings();
		System.out.println("Die Reviews wurden erfolgreich hinzugefügt");

	}

	public void insertIntoRatings() throws SQLException {
		for (int currentUser : this.allUsers) {
			for (int currentProduct : this.allProducts) {
				if(currentProduct != 633001) { // Ein Produkt bekommt keine Bewertungen für Präsentationszwecke
					Random random = new Random();
					int rating = random.nextInt(5) + 1;
					String review_text = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, "
							+ "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, "
							+ "sed diam voluptua.";
					LocalDate currentDate = LocalDate.now();
					String review_date = currentDate.toString();
					String sql = "INSERT INTO reviews (user_id, product_id, rating, review_text, review_date) "
							+ "VALUES (?, ?, ?, ?, ?)";
					System.out.println(sql);
					PreparedStatement prep = this.dbConn.prepareStatement(sql);
					prep.setInt(1, currentUser);
					prep.setInt(2, currentProduct);
					prep.setInt(3, rating);
					prep.setString(4, review_text);
					prep.setString(5, review_date);
					prep.executeUpdate();
				}
				}
		}

	}

	public void setRatings() {

	}

	public void setUsersQuantity() throws SQLException {
		String sql = "SELECT user_id FROM users";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next()) {
			this.allUsers.add(dbRes.getInt(1));
		}
	}

	public void setProductsQuantity() throws SQLException {
		String sql = "SELECT product_id FROM products";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next()) {
			this.allProducts.add(dbRes.getInt(1));
		}
	}

}
