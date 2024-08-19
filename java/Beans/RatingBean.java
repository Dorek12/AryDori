package Beans;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import Connection.NoConnectionException;
import Connection.PostgreSQLAccess;

public class RatingBean {

	String username;
	String brand;
	String product_name;
	String color;
	String date;
	int ram;
	int memory;
	int product_id;
	int user_id;
	int ratingQuantity;
	int ratingSum;
	int ratingAverage;
	boolean loggedIn;
	boolean ratedProduct;
	int[] ratings;
	Connection dbConn;

	public RatingBean() throws NoConnectionException {
		this.createConnection();
		this.setUsername("");
		this.setBrand("Apple");
		this.setProduct_name("iPhone 14 Pro");
		this.setColor("Weiß");
		this.setDate("2023-01-01");
		this.setUser_id(Integer.MIN_VALUE);
		this.setProduct_id(633001);
		this.setRam(6);
		this.setMemory(128);
		this.setLoggedIn(false);
		this.setRatedProduct(false);
		this.ratings = new int[5];
	}

	// Connection-Methode

	public void createConnection() throws NoConnectionException {
		this.dbConn = new PostgreSQLAccess().getConnection();
	}

	public String getRatingViewAsHtml() throws SQLException {
		this.readProductIdFromDb();
		String html = "		<div class='col-md-12' style='margin-top: 10px'>		\r\n";
		if (this.isRatedProduct()) {
			html += "<div class='alert alert-success' role='alert'>\r\n"
					+ "       Danke, dass Sie das Produkt bewertet haben!\r\n" + "    </div>\r\n";
		} 
		if(!this.isLoggedIn()) {
			html += "<div class='alert alert-warning' role='alert'>\r\n"
					+ "       Sie müssen angemeldet sein, um Bewertung schreiben zu können.\r\n" + "    </div>\r\n";
		}
				if (this.product_id != 0) {
					html += "			<div class='reviews'>\r\n" + "				<h3>Bewertungen für "
							+ (this.getBrand() != null ? this.getBrand() : "") + " "
							+ (this.getProduct_name() != null ? this.getProduct_name() : "")
							+ (this.getColor() != null ? " in " + this.getColor() : "")
							+ (this.getMemory() != 0 ? " mit " + this.getMemory() + " GB Speicherplatz " : "")
							+ (this.getRam() != 0 ? " und " + this.getRam() + " GB Arbeitsspeicher " : "") + "</h3>\r\n"
							+ this.getRatingAsHtml();
				}
		if (this.isLoggedIn()) {
			html += "			<div class='card' id='review-card'>\r\n"
					+ "				<div class='card-body bg-dark-subtle bg-gradient'>\r\n"
					+ "					<h3 class='card-title'>Eigene Bewertung schreiben:</h3>\r\n"
					+ "					<form action='../Appls/RatingAppl.jsp' method='get'>\r\n"
					+ "						<div class='form-group'>\r\n"
					+ "							<div class='star-rating'>\r\n"
					+ "								<span class='fa fa-star-o' data-rating='1'></span> <span\r\n"
					+ "									class='fa fa-star-o' data-rating='2'></span> <span\r\n"
					+ "									class='fa fa-star-o' data-rating='3'></span> <span\r\n"
					+ "									class='fa fa-star-o' data-rating='4'></span> <span\r\n"
					+ "									class='fa fa-star-o' data-rating='5'></span> <input\r\n"
					+ "									type='hidden' name='rating_user' class='rating-value' value='0'>\r\n"
					+ "							</div>\r\n"
					+ "							<textarea class='form-control mt-1' id='review' name='review' rows='3'></textarea>\r\n"
					+ "						</div>\r\n"
					+ "						<button type='submit' class='btn btn-primary mt-3'\r\n"
					+ "							style='float: right;' name='btnRateProduct' value='rateProduct'>Bewertung abschicken</button>\r\n"
					+ "					</form>\r\n" + "				</div>\r\n" + "			</div>\r\n";
		}

		html += this.getReviewsAsHtml() + "		</div>\r\n";

		this.setRatedProduct(false);
		return html;
	}

	public String getReviewsAsHtml() throws SQLException {
		String html = "";
			if (this.getRatingQuantity() > 0) {
				// chronologisch geordnet mit den aktuellsten Bewertungen zuerst
				String sql = "SELECT review_text, user_id, rating, review_date FROM reviews WHERE product_id = '"
						+ this.getProduct_id() + "' ORDER BY created_at DESC";
				System.out.println(sql);
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				ResultSet dbRes = prep.executeQuery();
				while (dbRes.next()) {
					this.readNameOfUserFromDb(dbRes.getInt("user_id"));
					html += "				<div class='review card'>\r\n"
							+ "					<div class='card-body'>\r\n"
							+ "						<h5 class='card-title'>Bewertung von " + this.getUsername()
							+ " am " + dbRes.getString("review_date") + "</h5>\r\n";
					for (int i = 0; i < 5; i++) {
						html += "<span class='fa fa-star" + (i < dbRes.getInt("rating") ? " checked" : "")
								+ " fa-lg'></span>\r\n";
					}
					html += "						<p class='card-text mt-3'>" + dbRes.getString("review_text")
							+ "</p>\r\n" + "					</div>\r\n" + "				</div>\r\n";
				}

			} else {
				html += "<p>Es scheint als wären noch keine Bewertungen vorhanden. Sie könnten der Erste sein!</p>";

			}
			html += "			</div>			\r\n";
		
		return html;
	}

	public String getRatingAsHtml() throws SQLException {
		String productPathForURL = this.getProduct_name().replace(" ", "+");
		this.readRatingFromDb();
		String html = this.getRatingPopupAsHtml() + "<a onclick='showPopup(\"review_popup\")'><p>";
		// \" weil ".." übergeben muss und nicht '..'
		if (this.getRatingQuantity() != 0 && this.getRatingSum() != 0)
			this.setRatingAverage(this.getRatingSum() / this.getRatingQuantity());
		for (int i = 0; i < 5; i++) {
			html += "<span class='fa fa-star" + (this.getRatingAverage() > 0 ? " checked" : "") + " fa-lg'></span>\r\n";
			this.setRatingAverage((this.getRatingAverage() - 1));
		}
		html += "</a> <b>" + this.getRatingQuantity() + " Bewertungen</b></p>"
				+ "<a href='../Appls/ProductAppl.jsp?product_name=" + productPathForURL + "&color=" + this.getColor() 
				+ "&ram=" + this.getRam() + "&physical_memory=" + this.getMemory() 
				+ "'><button type='button' class='btn btn-outline-dark mt-3'>Zurück zum Produkt</button></a>";
		return html;
	}
	
	public String getRatingPopupAsHtml() {
		String html = "<div class='popup' id='review_popup'>\r\n" + "	<div class='popup-header mb-3'>\r\n"
				+ "		<h2>Bewertungen</h2>\r\n";

		// \" weil ".." Wert übergeben muss und nicht ''
		html += "		<span class='popup-close' onclick='closePopup(\"review_popup\")'>X</span>\r\n" + "	</div>\r\n";

		for (int i = this.ratings.length; i >= 1; i--) {
			int percentage = 0;
			if (this.getRatingQuantity() != 0)
				percentage = (int) (Math.round(((double) this.ratings[i - 1] * 100 / this.getRatingQuantity())));
			html += "	<div class='star-ratings'>\r\n" + "		<div>" + i + " Sterne</div>\r\n"
					+ "		<div style='flex-grow: 1; margin: 0 10px;'>\r\n"
					+ "			<progress class='progress-bar' value='" + percentage + "' max='100'></progress>\r\n"
					+ "		</div>\r\n" + "		<div class='percentage'>" + percentage + "%</div>\r\n" + "	</div>\r\n"
					+ "\r\n";
		}

		html += "</div>\r\n";
		return html;
	}

	public void readProductIdFromDb() throws SQLException {
		String sql = "SELECT * FROM products WHERE product_name ILIKE '" + this.getProduct_name() + "'";
		if (this.getMemory() != 0)
			sql += " AND physical_memory = '" + this.getMemory() + "'";
		if (this.getRam() != 0)
			sql += " AND ram = '" + this.getRam() + "'";
		if (this.getColor() != null && !this.getColor().isBlank())
			sql += " AND color ILIKE '" + this.getColor() + "'";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		if (dbRes.next()) {
			this.setProduct_id(dbRes.getInt("product_id"));
			this.setBrand(dbRes.getString("brand"));
		}
		else {
			this.setProduct_id(0);
			this.setBrand("");
		}
	}

	public void readNameOfUserFromDb(int userid) throws SQLException {
		String sql = "SELECT first_name FROM users WHERE user_id = '" + userid + "'";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		if (dbRes.next())
			this.setUsername(dbRes.getString("first_name"));
		else
			this.setUsername("Anonym");
	}

	public void insertRatingIntoDb(int rating, String review) throws SQLException {
		String sql = "";
		if (this.getUser_id() != Integer.MIN_VALUE && this.getProduct_id() != 0) {
			LocalDate currentDate = LocalDate.now();
			String review_date = currentDate.toString();
			if (!this.checkIfAlreadyRated()) {
				sql = "INSERT INTO reviews (user_id, product_id, rating, review_text, review_date) "
						+ "VALUES (?, ?, ?, ?, ?)";
				System.out.println(sql);
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setInt(1, this.getUser_id());
				prep.setInt(2, this.getProduct_id());
				prep.setInt(3, rating);
				prep.setString(4, review);
				prep.setString(5, review_date);
				prep.executeUpdate();
			} else {
				sql = "UPDATE reviews SET rating = ?, review_text = ?, review_date = ?, created_at = NOW() "
						+ "WHERE user_id = ? AND product_id = ?";
				System.out.println(sql);
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setInt(1, rating);
				prep.setString(2, review);
				prep.setString(3, review_date);
				prep.setInt(4, this.getUser_id());
				prep.setInt(5, this.getProduct_id());
				prep.executeUpdate();
			}
		}
	}

	public boolean checkIfAlreadyRated() throws SQLException {
		String sql = "SELECT * FROM reviews WHERE product_id = '" + this.getProduct_id() + "' AND user_id = '"
				+ this.getUser_id() + "'";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		return dbRes.next();
	}

	// füllt das ratings Array wie viele Sterne jeweils existieren um später auszurechnen wie viel Prozent zum Beispiel 5 Sterne sind im Popup
	
	public void readRatingFromDb() throws SQLException {
		if (this.getProduct_id() != 0) {
			this.setRatingAverage(0);
			this.setRatingQuantity(0);
			this.setRatingSum(0);
			this.ratings = new int[5];
			String sql = "SELECT rating FROM reviews WHERE product_id = '" + this.getProduct_id() + "'";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			ResultSet dbRes = prep.executeQuery();
			while (dbRes.next()) {
				this.setRatingSum((this.getRatingSum() + dbRes.getInt("rating")));
				this.setRatingQuantity((this.getRatingQuantity() + 1));
				if (dbRes.getInt("rating") == 5)
					this.ratings[4]++;
				else if (dbRes.getInt("rating") == 4)
					this.ratings[3]++;
				else if (dbRes.getInt("rating") == 3)
					this.ratings[2]++;
				else if (dbRes.getInt("rating") == 2)
					this.ratings[1]++;
				else if (dbRes.getInt("rating") == 1)
					this.ratings[0]++;
			}
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getRam() {
		return ram;
	}

	public void setRam(int ram) {
		this.ram = ram;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getRatingQuantity() {
		return ratingQuantity;
	}

	public void setRatingQuantity(int ratingQuantity) {
		this.ratingQuantity = ratingQuantity;
	}

	public int getRatingSum() {
		return ratingSum;
	}

	public void setRatingSum(int ratingSum) {
		this.ratingSum = ratingSum;
	}

	public int getRatingAverage() {
		return ratingAverage;
	}

	public void setRatingAverage(int ratingAverage) {
		this.ratingAverage = ratingAverage;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isRatedProduct() {
		return ratedProduct;
	}

	public void setRatedProduct(boolean ratedProduct) {
		this.ratedProduct = ratedProduct;
	}

}
