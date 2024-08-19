package Beans;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.Vector;
import Connection.NoConnectionException;
import Connection.PostgreSQLAccess;
import jakarta.servlet.http.HttpServletRequest;

public class ProductBean extends AbstractBean{
	String Username;
	String brand;
	String product_name;
	String color;
	String description;
	String operating_system;
	String localhost;
	int user_id;
	int product_id;
	int ram;
	int memory;
	int ratingQuantity;
	int ratingSum;
	int ratingAverage;
	double price;
	boolean loggedIn;
	Vector<String> allColor;
	Vector<Integer> allRam;
	Vector<Integer> allMemory;
	Vector<byte[]> allImageBytes;
	int[] ratings;
	Connection dbConn;

	// Setze Werte falls Seite mal default geladen wird

	public ProductBean() throws NoConnectionException {
		this.createConnection();
		this.setUsername("Anonym");
		this.setBrand("Apple");
		this.setProduct_name("iPhone 14 Pro");
		this.setOperating_system("iOS");
		this.setUser_id(Integer.MIN_VALUE);
		this.setRam(6);
		this.setMemory(256);
		this.setRatingAverage(0);
		this.setRatingSum(0);
		this.setRatingQuantity(0);
		this.setColor("Schwarz");
		this.setLoggedIn(false);
		this.ratings = new int[5];
	}

	// Connection-Methode

	public void createConnection() throws NoConnectionException {
		this.dbConn = new PostgreSQLAccess().getConnection();
	}
	
	// Title dynamisch erstellen 
	
		public String getTitleAsHtml() throws SQLException {
			this.readInformationsFromDB();
			return this.getBrand() + " " + this.getProduct_name();
		}
		
	// Bild im Title generieren
		
	   public String getLinkImgAsHtml() {
		   String html = "<link rel='icon' href='../../IMG/Title/";
		   if(this.getBrand().equalsIgnoreCase("Apple")) html += "Apple";
		   else if(this.getBrand().equalsIgnoreCase("Samsung")) html += "Samsung";
		   else if(this.getBrand().equalsIgnoreCase("Huawei")) html += "Huawei";
		   else html += "NAS";
		   html += "_Logo.png' type='image/png'>";
		   return html;
	   }
		
	// HTML-Code generieren und zurückgeben

	public String getProductViewAsHtml() throws SQLException, UnsupportedEncodingException {
		if (!this.checkIfProductExist()) {
			return this.getProductNotExistErrorAsHtml();
		} else
			return this.getProductAsHtml();
	}

	public boolean checkIfProductExist() throws SQLException {
		String sql = "SELECT * FROM products WHERE product_name ILIKE '" + this.getProduct_name() + "'";
		if (this.getMemory() != 0)
			sql += " AND physical_memory = '" + this.getMemory() + "'";
		if (this.getRam() != 0)
			sql += " AND ram = '" + this.getRam() + "'";
		if (this.getColor() != null && !this.getColor().isBlank())
			sql += " AND color ILIKE '" + this.getColor() + "'";
		sql += " ORDER BY product_id";
		/*
		 * ORDER BY ist wichtig damit nachdem man die DB geupdatet hat das ResultSet
		 * nach Höhe der product_id geordnet ist und somit die Reihenfolge der
		 * Darstellung immer gleich bleibt
		 */
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		return dbRes.next();
	}

	public String getProductAsHtml() throws SQLException, UnsupportedEncodingException {
		String html = "<form action='../Appls/ProductAppl.jsp' method='get'>"
				+ "<div class='container mt-5' id='product-container'>\r\n" + "	<div class='row'>\r\n"
				+ "		<div class='col-md-5'>\r\n" + this.getImgCarouselAsHtml()

				+ "		</div>\r\n" + "		<div class='col-md-7'>\r\n" + "			<div>\r\n"
				+ "				<h2 style='margin-bottom: -10px;' class='product-title'>"
				+ (this.getBrand() != null ? this.getBrand() : "") + " "
				+ (this.getProduct_name() != null ? this.getProduct_name() : "")
				+ (this.getColor() != null ? " in " + this.getColor() : "")
				+ (this.getMemory() != 0 ? " mit " + this.getMemory() + " GB Speicherplatz " : "")
				+ (this.getRam() != 0 ? " und " + this.getRam() + " GB Arbeitsspeicher " : "") + "</h2><br>\r\n"
				+ this.getRatingAsHtml();
		if (this.getPrice() != 0)
			html += " 				<p class='product-price' style='color:black;'>Preis: "
					+ this.removeZeros(this.getPrice()) + " €</p>";

		html += (this.checkIfProductIsInStock() != 0 ? ""
				: " 				<p class='stock-text' "
						+ "style='color: red;'>Derzeit leider nicht verfügbar</p>\r\n")
				+ this.getColorsAsHtml() + "\r\n" + this.getRamAsHtml() + "\r\n" + this.getMemoryAsHtml()
				+ "				<p class='product-description'>" + this.getDescription() + "</p>\r\n"
				+ "				\r\n" + "			</div>\r\n" + "			\r\n"
				+ "				<button class='btn btn-primary' type='submit' name='btnCart' value='"
				+ (this.getProduct_id() != 0 ? this.getProduct_id() : "633001") + "'"
				+ (this.checkIfProductIsInStock() != 0 ? "" : " disabled")
				+ "><img class='img-fluid' style='width: 30px; height: 30px; margin-right:10px;' src='../../IMG/Header/Cart.png'>in den Warenkorb</button>\r\n";

		if (this.isLoggedIn()) {
			if (this.getUser_id() != Integer.MIN_VALUE && this.checkIfExistInWishlist()) {
				html += "				<button class='btn btn-outline-danger' type='submit' name='btnWishlistDelete' value='"
						+ (this.getProduct_id() != 0 ? this.getProduct_id() : "")
						+ "'><img class='img-fluid' style='width: 30px; height: 30px;' src='../../IMG/ProductMain/Wishlist.png' alt='Auf die Wunschliste'></button>\r\n";

			} else {
				html += "				<button class='btn btn-outline-danger' type='submit' name='btnWishlist' value='"
						+ (this.getProduct_id() != 0 ? this.getProduct_id() : "633001")
						+ "'><img class='img-fluid' style='width: 30px; height: 30px;' src='../../IMG/ProductMain/Wishlist_Transparent.png' alt='Auf die Wunschliste'></button>\r\n";

			}

		}

		html += "</form>\r\n				<button class='btn btn-outline-dark' id='shareBtn' value='"
				+ this.getHrefOfCurrentProduct("Product")
				+ "' onclick='copyURL();' type='button'><img class='img-fluid' style='width: 30px; height: 30px;' src='../../IMG/ProductMain/Share.png' alt='Artikel merken'></button>\r\n"
				+ "			\r\n" + "<form action='../Appls/ProductAppl.jsp' method='get'>"
				+ "				<div class='popup_link' id='popup_link'>\r\n"
				+ "					Link kopiert! Teilen Sie ihn gerne mit Freunden und Familie.\r\n"
				+ "				</div>\r\n" + "		</div>\r\n" + "	</div>\r\n" + "    <div class='row mt-5'>\r\n"
				+ "        <div class='col-md-12'>\r\n" + this.getTechDetailsAsHtml() + "        </div>\r\n"
				+ "    </div>\r\n" + this.getReviewsAsHtml() + "</div>\r\n</form>";
		return html;
	}

	public void readInformationsFromDB() throws SQLException {
		String sql = "SELECT * FROM products WHERE product_name ILIKE '" + this.getProduct_name() + "'";
		if (this.getMemory() != 0)
			sql += " AND physical_memory = '" + this.getMemory() + "'";
		if (this.getRam() != 0)
			sql += " AND ram = '" + this.getRam() + "'";
		if (this.getColor() != null && !this.getColor().isBlank())
			sql += " AND color ILIKE '" + this.getColor() + "'";
		sql += " ORDER BY product_id";
		/*
		 * ORDER BY ist wichtig damit nachdem man die DB geupdatet hat das ResultSet
		 * nach Höhe der product_id geordnet ist und somit die Reihenfolge der
		 * Darstellung immer gleich bleibt
		 */
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		if (dbRes.next()) {
			this.setProduct_name(dbRes.getString("product_name"));
			this.setBrand(dbRes.getString("brand"));
			this.setDescription(dbRes.getString("description"));
			this.setOperating_system(dbRes.getString("operating_system"));
			this.setPrice(dbRes.getDouble("price"));
			this.setColor(dbRes.getString("color"));
			this.setProduct_id(dbRes.getInt("product_id"));
		} else {
			this.setProduct_name("");
			this.setBrand("");
			this.setDescription("Produktinformationen nicht verfügbar\r\n" + "\r\n"
					+ "Es tut uns leid, aber wir konnten die Informationen zu diesem Produkt im Moment nicht abrufen. "
					+ "Dies an einer vorübergehenden technischen Störung. Wir arbeiten daran, das Problem so schnell wie möglich zu beheben");
			this.setOperating_system("");
			this.setPrice(0);
			this.setColor("");
			this.setProduct_id(dbRes.getInt(0));
		}
	}

	public String getRatingAsHtml() throws SQLException, UnsupportedEncodingException {
		this.readRatingFromDb();
		String html = this.getRatingPopupAsHtml() + "<a onclick='showPopup(\"review_popup\")'><p>"; 
		// \" weil ".."  Wert  übergeben  muss und  nicht '..'
		if (this.getRatingQuantity() != 0 && this.getRatingSum() != 0)
			this.setRatingAverage(this.getRatingSum() / this.getRatingQuantity());
		for (int i = 0; i < 5; i++) {
			html += "<span class='fa fa-star" + (this.getRatingAverage() > 0 ? " checked" : "") + " fa-lg'></span>\r\n";
			this.setRatingAverage((this.getRatingAverage() - 1));
		}
		html += "</p></a>";
		return html;
	}

	public String getRatingPopupAsHtml() throws UnsupportedEncodingException {

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

		html += "<a href='" + this.getHrefOfCurrentProduct("Rating") + "' style='text-decoration: none; color: blue; display: block; margin-top: 10px;'>Alle Bewertungen ansehen (" + this.getRatingQuantity() + ")</a>";

		html += "</div>\r\n";

		return html;
	}

	// füllt das ratings Array wie viele Sterne jeweils existieren um später auszurechnen wie viel Prozent zum Beispiel 5 Sterne sind im Popup
	public void readRatingFromDb() throws SQLException {
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
	// Bytes aus der Datenbank in Bilder umwandeln, BufferedImage ist gängiger aber
	// lädt viel zu lang
	// Quelle: https://onlinestringtools.com/convert-bytes-to-string
	// Quelle:
	// https://www.w3docs.com/snippets/html/how-to-display-base64-images-in-html.html

	public String getImgCarouselAsHtml() throws SQLException {
		this.readAllImagesOfProductFromDb();
		String html = "";
		int index = 0;
		if (this.allImageBytes != null && this.allImageBytes.size() > 0) {
			html = "<div id='product-carousel' class='carousel slide carousel-dark' data-ride='carousel'>\r\n"
					+ "    <div class='carousel-inner'>\r\n";

			for (byte[] currentByte : this.allImageBytes) {
				String imageEncoded = Base64.getEncoder().encodeToString(currentByte);
				String active = "";
				if (index == 0) {
					active = "active";
					index++;
				}

				html += "        <div class='carousel-item " + active + "'>\n"
						+ "            <img src='data:image/jpeg;base64, " + imageEncoded
						+ "' alt='Leider konnte kein Bild geladen werden" + "'\n"
						+ "                class='img-fluid mx-auto d-block'>\n" + "        </div>\n";
			}

			html += "    </div>\n"
					+ "    <a class='carousel-control-prev' href='#product-carousel' role='button' data-slide='prev'>\n"
					+ "        <span class='carousel-control-prev-icon' aria-hidden='true'></span>\n" + "    </a>\n"
					+ "    <a class='carousel-control-next' href='#product-carousel' role='button' data-slide='next'>\n"
					+ "        <span class='carousel-control-next-icon' aria-hidden='true'></span>\n" + "    </a>\n"
					+ "</div>\n" + "<div class='thumbnail-row'>\n";
			index = 0;
			for (byte[] currentByte : this.allImageBytes) {
				html += "    <div class='thumbnail' data-target='#product-carousel' data-slide-to='" + index + "'>\n"
						+ "        <img src='data:image/jpeg;base64, " + Base64.getEncoder().encodeToString(currentByte)
						+ "' alt='Leider konnte kein Bild geladen werden " + (index + 1) + "'\n"
						+ "            class='img-thumbnail'>\n" + "    </div>\n";
				index++;
			}

			html += "</div>\n";

		} else {
			html = "        <div class='carousel-item active'>\r\n"
					+ "               <img src='../../IMG/Error/No_Image_Found.jpg' alt='KLeider konnte kein Bild geladen werden' class='img-fluid'>\r\n"
					+ "            </div>\r\n";
		}
		return html;
	}

	public int setProductIdOfProductForImage() throws SQLException {
		int imageProduct_id = 0;
		if (this.getProduct_name() != null && this.getColor() != null && !this.getProduct_name().isBlank()
				&& !this.getColor().isBlank()) {
			String sql = "SELECT product_id FROM products WHERE product_name ILIKE '" + this.getProduct_name()
					+ "' AND color ILIKE '" + this.getColor() + "' ORDER BY product_id";
			/*
			 * ORDER BY ist wichtig damit nachdem man die DB geupdatet hat das ResultSet
			 * nach Höhe der product_id geordnet ist und somit die Reihenfolge der
			 * Darstellung immer gleich bleibt
			 */
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			ResultSet dbRes = prep.executeQuery();
			while (dbRes.next())
				imageProduct_id = dbRes.getInt("product_id");

			/*
			 * setproduct_id muss immer überschrieben werden, da in der
			 * insertproductImagesApp die Pfade unique sind (HashMap Key) somit kriegen
			 * Produkte immer nur ein Pfad pro Produktname und Farbe in der Datenbank
			 * existieren Produkte mit selben Namen und Farbe aber mehrmals aufgrund des
			 * Rams und physical_memory deshalb muss immer mit der letzten product_id
			 * gearbeitet werden Man könnte in der image Datenbank mit product_name und
			 * color arbeiten statt product_id und würde dieses Problem umgehen Problem
			 * hierbei: Produkte haben die selben Bilder unabhängig von Ram und
			 * Speicherplatz, es würde zwar jedes Produkt eigene Bilder bekommen allerdings
			 * redundand (iPhone 14 Pro in Schwarz mit 128 GB hätte die selben Bilder in der
			 * DB gespeichert wie das iPhone 14 Pro Schwarz mit 256 GB
			 */
		}
		return imageProduct_id;
	}

	public void readAllImagesOfProductFromDb() throws SQLException {
		int imageProduct_id = this.setProductIdOfProductForImage();
		this.allImageBytes = new Vector<byte[]>();
		if (imageProduct_id != 0) {
			String sql = "SELECT image_byte FROM images WHERE product_id = " + imageProduct_id;
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			ResultSet dbRes = prep.executeQuery();
			while (dbRes.next()) {
				this.allImageBytes.add(dbRes.getBytes("image_byte"));
			}
		} else
			this.allImageBytes = null;
	}

	public String getReviewsAsHtml() throws SQLException, UnsupportedEncodingException {
		String html = "<div class='reviews'>\r\n" + "		<h3>Bewertungen für "
				+ (this.getBrand() != null ? this.getBrand() : "") + " "
				+ (this.getProduct_name() != null ? this.getProduct_name() : "")
				+ (this.getColor() != null ? " in " + this.getColor() : "")
				+ (this.getMemory() != 0 ? " mit " + this.getMemory() + " GB Speicherplatz " : "")
				+ (this.getRam() != 0 ? " und " + this.getRam() + " GB Arbeitsspeicher " : "") + "</h3>\r\n";
		if (this.getProduct_id() != 0) {
			// nehmen zur Vorschau nur die 3 Bewertungen
			String sql = "SELECT review_text, user_id, rating, review_date FROM reviews WHERE product_id = '" + this.getProduct_id()
					+ "' LIMIT 3";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			ResultSet dbRes = prep.executeQuery();
			while (dbRes.next()) {
				this.readNameOfUserFromDb(dbRes.getInt("user_id"));
				html += "		<div class='review card'>\r\n" + "			<div class='card-body'>\r\n"
						+ "				<h5 class='card-title'>Bewertung von " + this.getUsername() + " am " + dbRes.getString("review_date") + "</h5>\r\n";
				for (int i = 0; i < 5; i++) {
					html += "<span class='fa fa-star" + (i < dbRes.getInt("rating") ? " checked" : "")
							+ " fa-lg'></span>\r\n";
				}
				html += "				<p class='card-text mt-3'>" + dbRes.getString("review_text") + "</p>\r\n"
						+ "			</div>\r\n" + "		</div>\r\n";

			}
			html += "<a href='" + this.getHrefOfCurrentProduct("Rating") + "' style='text-decoration: none; color: blue; display: block; margin-top: 10px;'>Alle Bewertungen ansehen (" + this.getRatingQuantity() + ")</a>";
			html += "	</div>";
		}

		return html;
	}

	public void readNameOfUserFromDb(int user_id) throws SQLException {
		String sql = "SELECT first_name FROM users WHERE user_id = '" + user_id + "'";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		if (dbRes.next())
			this.setUsername(dbRes.getString("first_name"));
		else
			this.setUsername("Anonym");
	}

	public String getTechDetailsAsHtml() {
		String html = "<div class='product-details technical-details'>\r\n" + "    <h3>Technische Details</h3><br>\r\n"
				+ "    <p><b>Artikelnummer:</b> " + this.getProduct_id() + "</p>\r\n" + "    <p><b>Marke:</b> "
				+ (this.getBrand() != null ? this.getBrand() : "") + "</p>\r\n" + "    <p><b>Farbe:</b> "
				+ (this.getColor() != null ? this.getColor() : "") + "</p>\r\n" + (this.getMemory() != 0 ? "    <p><b>Speicherplatz:</b> "
						+ this.getMemory() + " GB " : "") + (this.getRam() != 0 ? "   <p><b>Arbeitsspeicher:</b> " + this.getRam()
				+ " GB " : "") + "   <p><b>Betriebssystem:</b> "
				+ (this.getOperating_system() != null ? this.getOperating_system() : "") + "</p>\r\n<p>...</p>\r\n"
				+ "</div>\r\n";
		return html;
	}

	// HTML-Code für Color

	public String getColorsAsHtml() throws SQLException {
		this.fillVectorWithColors();
		boolean colorNotInStock = false;
		String html = "";
		int index = 0;
		if (this.allColor != null && this.allColor.size() > 0) {
			html = "<div>";
			for (String currentColor : this.allColor) {
				String translatedColor = this.translateColor(currentColor);
				if (this.allColor.get(index) != null && !this.allColor.get(index).isBlank()) {
					if (!translatedColor.equals("notInStock")) {
						html += "<button style='width: 40px; height: 40px; background-color: " + translatedColor
								+ "; border-radius: 50%; ";
						if (!this.getColor().equals(currentColor))
							html += "border: 2px solid white;";
						html += " margin-right: 10px;' name='btnColor' value='"
								+ (currentColor.equalsIgnoreCase("Weiß") ? "white" : currentColor) + "'></button>";
					} else {
						colorNotInStock = true;
					}
				}
				index++;
			}

			if (colorNotInStock || this.allColor == null) {
				html += "<div style='background-color: #f2f2f2; padding: 10px; border-radius: 5px; display: inline-block;'>"
						+ "<br>Leider sind keine weiteren Farben verfügbar<b>.</div>";
			}

			html += "</div>\r\n<br>";
		}
		return html;
	}

	// Fülle den Vector mit den Color-Optionen

	public void fillVectorWithColors() throws SQLException {
		this.allColor = new Vector<String>();
		boolean vectorIsEmpty = true;
		String sql = "SELECT color FROM products WHERE product_name ILIKE " + "'" + this.getProduct_name() + "'";
		if (this.getRam() != 0)
			sql += " AND physical_memory = '" + this.getMemory() + "'";
		if (this.getMemory() != 0)
			sql += " AND ram = '" + this.getRam() + "'";
		sql += " ORDER BY product_id";
		/*
		 * ORDER BY ist wichtig damit nachdem man die DB geupdatet hat das ResultSet
		 * nach Höhe der product_id geordnet ist und somit die Reihenfolge der
		 * Darstellung immer gleich bleibt
		 */
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next()) {
			this.allColor.add(dbRes.getString("color"));
			vectorIsEmpty = false;
		}

		if (vectorIsEmpty)
			this.allColor = null;
	}

	// Übersetzt die Farben - die Farben sind in der DB in deutsch also sind auch
	// die Parameter in deutsch um einwandfrei mit der DB zu arbeiten
	// ins englische übersetzen um dynamisch mit CSS Farben zu arbeiten

	public String translateColor(String colorToCheck) {
		String colorTranslated;
		switch (colorToCheck.toLowerCase()) {
		case "blau":
			colorTranslated = "blue";
			break;
		case "rot":
			colorTranslated = "red";
			break;
		case "grün":
			colorTranslated = "green";
			break;
		case "gelb":
			colorTranslated = "yellow";
			break;
		case "orange":
			colorTranslated = "orange";
			break;
		case "lila":
			colorTranslated = "purple";
			break;
		case "rosa":
			colorTranslated = "pink";
			break;
		case "pink":
			colorTranslated = "pink";
			break;
		case "braun":
			colorTranslated = "brown";
			break;
		case "grau":
			colorTranslated = "gray";
			break;
		case "schwarz":
			colorTranslated = "black";
			break;
		case "weiß":
			colorTranslated = "white";
			break;
		case "weiss":
			colorTranslated = "white";
			break;
		case "white":
			colorTranslated = "Weiß";
			break;
		case "silber":
			colorTranslated = "silver";
			break;
		default:
			colorTranslated = "notInStock";
			break;
		}

		return colorTranslated;
	}

	// HTML-Code für Ram

	public String getRamAsHtml() throws SQLException {
		this.fillVectorWithRam();
		String html = "";
		int index = 0;
		if (this.allRam != null && this.allRam.size() > 0) {
			html = "<div>";
			if (this.allRam.get(index) != 0) {
				html += "<div style='margin-bottom: 10px;'><strong>Arbeitsspeicher:</strong></div>";
				for (int currentRam : this.allRam) {
					if (this.allRam.get(index) != 0) {
						html += "<button style='";
						if (this.getRam() != currentRam)
							html += "opacity: 0.5; ";
						html += "width: 60px; height: 40px; background-color: #333; color: white; border-radius: 5px; margin-right: 10px; font-weight: bold;' "
								+ "name='btnRam' value='" + currentRam + "'>" + currentRam
								+ " <span style='font-size: 14px;'>GB</span></button>";
					}
					index++;
				}
			}
			html += "</div>\r\n<br>";
		}
		return html;
	}

	// Fülle den Vector mit den Ram-Optionen

	public void fillVectorWithRam() throws SQLException {
		this.allRam = new Vector<Integer>();
		boolean vectorIsEmpty = true;
		String sql = "SELECT ram FROM products WHERE product_name ILIKE '" + this.getProduct_name() + "'";
		if (this.getMemory() != 0)
			sql += " AND physical_memory = '" + this.getMemory() + "'";
		if (this.getColor() != null && !this.getColor().isBlank())
			sql += " AND color ILIKE '" + this.getColor() + "'";
		sql += " ORDER BY ram";
		/*
		 * ORDER BY ist wichtig damit nachdem man die DB geupdatet hat das ResultSet
		 * nach Höhe der product_id geordnet ist und somit die Reihenfolge der
		 * Darstellung immer gleich bleibt
		 */
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next()) {
			if (dbRes.getInt("ram") != 0) {
				this.allRam.add(dbRes.getInt("ram"));
				vectorIsEmpty = false;
			}
		}

		if (vectorIsEmpty)
			this.allRam = null;
	}

	// HTML-Code für Memory

	public String getMemoryAsHtml() throws SQLException {
		this.fillVectorWithMemory();
		String html = "";
		int index = 0;
		if (this.allMemory != null && this.allMemory.size() > 0) {
			html = "<div>";
			if (this.allMemory.get(index) != 0) {
				html += "<div style='margin-bottom: 10px;'><strong>Speicherplatz:</strong></div>";
				for (int currentMemory : this.allMemory) {
					if (this.allMemory.get(index) != 0) {
						html += "<button style='";
						if (this.getMemory() != currentMemory)
							html += "opacity: 0.5; ";
						html += "width: 80px; height: 40px; background-color: #333; color: white; border-radius: 5px; margin-right: 10px; font-weight: bold;' "
								+ "name='btnMemory' value='" + currentMemory + "'>" + currentMemory
								+ " <span style='font-size: 14px;'>GB</span></button>";

					}
					index++;
				}
			}
			html += "</div>\r\n<br>";
		}
		return html;
	}

	// Fülle den Vector mit den Color-Optionen

	public void fillVectorWithMemory() throws SQLException {
		this.allMemory = new Vector<Integer>();
		boolean vectorIsEmpty = true;
		String sql = "SELECT physical_memory FROM products WHERE product_name ILIKE '" + this.getProduct_name() + "'";
		if (this.getRam() != 0)
			sql += " AND ram = '" + this.getRam() + "'";
		if (this.getColor() != null && !this.getColor().isBlank())
			sql += " AND color ILIKE '" + this.getColor() + "'";
		sql += " ORDER BY physical_memory";
		/*
		 * ORDER BY ist wichtig damit nachdem man die DB geupdatet hat das ResultSet
		 * nach Höhe der product_id geordnet ist und somit die Reihenfolge der
		 * Darstellung immer gleich bleibt
		 */
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next()) {
			if (dbRes.getInt("physical_memory") != 0) {
				this.allMemory.add(dbRes.getInt("physical_memory"));
				vectorIsEmpty = false;
			}
		}
		if (vectorIsEmpty)
			this.allMemory = null;
	}

	public int checkIfProductIsInStock() throws SQLException {
		String sql = "SELECT stock FROM products WHERE product_name ILIKE '" + this.getProduct_name() + "'";
		if (this.getRam() != 0)
			sql += " AND ram = '" + this.getRam() + "'";
		if (this.getColor() != null && !this.getColor().isBlank())
			sql += " AND color ILIKE '" + this.getColor() + "'";
		if (this.getMemory() != 0)
			sql += " AND physical_memory = '" + this.getMemory() + "'";
		sql += " ORDER BY product_id"; /*
										 * ORDER BY ist wichtig damit nachdem man die DB geupdatet hat das ResultSet
										 * nach Höhe der product_id geordnet ist und somit die Reihenfolge der
										 * Darstellung immer gleich bleibt
										 */
		System.out.println(sql);
		int stock = 0;
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		if (dbRes.next())
			stock = dbRes.getInt("stock");
		return stock;
	}

	/*
	 * andere Vorgehensweise als in MainpageBean, da ich nicht mit ein nur ein
	 * productPath habe und kein Vector brauche Methoden wird für
	 * response.SendRedirect benötigt (siehe HeaderAppl) und für Artikel teilen
	 */

	public String getHrefOfCurrentProduct(String applName) throws UnsupportedEncodingException {
		String productPathForURL = this.getProduct_name();
		String href = "http://" + this.localhost + "/NAS_Elektronik/JSP/Appls/" + applName + "Appl.jsp?product_name="
				+ URLEncoder.encode(productPathForURL, "UTF-8");
		if (!this.getColor().isBlank()) {
			if (this.getColor().equalsIgnoreCase("Weiss"))
				this.setColor("Weiß");
			else
				href += "&color=" + URLEncoder.encode(this.getColor(), "UTF-8");
		}
		if (this.getRam() != 0)
			href += "&ram=" + this.getRam();
		if (this.getMemory() != 0)
			href += "&physical_memory=" + this.getMemory();
		return href;
	}

	public boolean checkIfExistInWishlist() throws SQLException {
		if (this.loggedIn) {
			String sql = "SELECT * FROM wishlist WHERE user_id = ? AND product_id = ?";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, this.getUser_id());
			prep.setInt(2, this.getProduct_id());
			ResultSet dbRes = prep.executeQuery();
			return dbRes.next();
		}
		return false;
	}

	// checken ob Nullen entfernt werden müssen wenn Preis z.B. .00 ist in z.B.
	// ProductBean und ShoppingListBean

	public String removeZeros(double price) {
		DecimalFormat format = new DecimalFormat("0.##");
		return format.format(price);
	}

	// Error

	public String getProductNotExistErrorAsHtml() {
		return "<div class='container' style='top: margin: 100px;'>\r\n <div class='error_container'>\r\n"
				+ "<div class='error_code'>404 Not Found</div>\r\n"
				+ "<div class='error_message'>Das Produkt konnte nicht gefunden werden.</div>\r\n"
				+ "<div class='smiley'>&#128546;</div>\r\n"
				+ "<a class='link' href='../Views/MainpageView.jsp'>Zurück zur Startseite</a>\r\n"
				+ "</div>\r\n </div>";
	}

	// Getter und Setter - Methoden

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getColor() {
		return color;
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

	public void setColor(String color) {
		this.color = color;
	}

	public String getOperating_system() {
		return operating_system;
	}

	public void setOperating_system(String operating_system) {
		this.operating_system = operating_system;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public void setLocalhost(String localhost) {
		this.localhost = localhost;
	}

	public String getUserHost(HttpServletRequest request) {
		localhost = request.getHeader("Host");
		return localhost;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
