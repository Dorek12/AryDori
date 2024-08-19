package Beans;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import javax.mail.MessagingException;
import Connection.NoConnectionException;
import Connection.PostgreSQLAccess;

public class ShoppingBean {

	String email;
	String firstName;
	String lastName;
	String street;
	String city;
	String houseNumber;
	int user_id;
	int postcode;

	int invoice_id;

	String brand;
	String productName;
	String color;
	int stock;
	int ram;
	int memory;
	double price;
	double priceSum;
	boolean outOfStock; // wenn Produkte von der Wishlist in den Warenkorb gefüllt werden soll (siehe
						// z.B. ShoppingAppl)
	boolean loggedIn;
	HashMap<Integer, Integer> allProducts;
	HashMap<Integer, Integer> guestCart;
	Connection dbConn;

	public ShoppingBean() throws NoConnectionException {
		this.createConnection();
		this.setEmail("mustermail@muster.com");
		this.setFirstName("Max");
		this.setLastName("Mustermann");
		this.setStreet("Musterstrasse");
		this.setCity("Musterstadt");
		this.setHouseNumber("1a");
		this.setPostcode(0);

		this.setInvoice_id(0);

		this.setBrand("");
		this.setProductName("");
		this.setColor("");
		this.setUser_id(Integer.MIN_VALUE);
		this.setStock(0);
		this.setRam(0);
		this.setMemory(0);
		this.setPrice(0);
		this.setPriceSum(0);
		this.setOutOfStock(false);
		this.setLoggedIn(false);
		this.allProducts = new HashMap<Integer, Integer>();
		this.guestCart = new HashMap<Integer, Integer>();
	}

	// Connection-Methode

	public void createConnection() throws NoConnectionException {
		this.dbConn = new PostgreSQLAccess().getConnection();
	}

	public String getCartAsHtml() throws SQLException, UnsupportedEncodingException {
		this.setPriceSum(0);
		return this.getShoppingListAsHtml("cart") + this.getSummaryAsHtml("cart") + "		</div>\r\n"
				+ "	\r\n</form>";
	}

	public String getWishlistAsHtml() throws SQLException, UnsupportedEncodingException {
		String html = "";
		if (this.isLoggedIn()) {
			if (this.isOutOfStock()) {
				this.setOutOfStock(false); // setze wieder auf false damit die Nachricht nicht die ganze Session über
											// angezeigt wird, sobald einmal auf true
				html += "<div class='alert alert-danger' role='alert'>\r\n"
						+ "        <p>Einige Produkte sind derzeit nicht verfügbar.</p>\r\n" + "    </div>";
			}
			html += this.getShoppingListAsHtml("wishlist") + this.getSummaryAsHtml("wishlist") + "		</div>\r\n"
					+ "	\r\n</form>";
		} else {
			html = "<div class='alert alert-warning' role='alert'>\r\n"
					+ "        <p>Sie müssen angemeldet sein, um auf die Wunschliste zuzugreifen.</p>\r\n"
					+ "    </div>";
		}

		return html;
	}

	public String getCheckoutAsHtml() throws SQLException, UnsupportedEncodingException {
		this.setPriceSum(0);
		String html = "";
		if (this.isLoggedIn() && this.user_id != Integer.MIN_VALUE) {
			this.readProductIdsFromDb("cart");
			if (this.allProducts.size() > 0) {
				html = "		<div class='row'>\r\n" + "			<div class='col-md-12 mb-3'>\r\n"
						+ "				<h2 class='fw-bold text-black'>Deine Bestellung</h2>\r\n"
						+ "			</div>\r\n" + "		</div>\r\n" + "		<div class='row'>\r\n"
						+ "			<div class='col-md-8' id='zahlung'>\r\n"
						+ "				<div class='scrollable-content'>";
				html += "<form action='../Appls/ShoppingAppl.jsp' method='get'>" + this.getProductsAsHtml("cart");

				html += " 				</div>\r\n</form>";

				html += "<form action='../Appls/CheckoutAppl.jsp' method='get'>\r\n" + this.getAddressAsHtml()
						+ this.getPaymentMethodAsHtml() + "			</div>\r\n";

				html += this.getSummaryAsHtml("checkout");

				html += "	</div>\r\n" + "	</form>";
			} else {
				html = "\r\n<div class='alert alert-info' role='alert'>\r\n"
						+ "        <p>Es befinden sich derzeit keine Artikel in Ihrem Warenkorb.</p>\r\n"
						+ "    </div>";
			}

		} else {
			html = "<div class='alert alert-warning' role='alert'>\r\n"
					+ "        <p>Sie müssen angemeldet sein, um etwas bestellen zu können.</p>\r\n" + "    </div>";
		}
		return html;
	}

	public String getShoppingListAsHtml(String dataBase) throws SQLException, UnsupportedEncodingException {
		if (dataBase.equalsIgnoreCase("cart") && this.isLoggedIn() || dataBase.equalsIgnoreCase("wishlist"))
			this.readProductIdsFromDb(dataBase);
		else
			this.allProducts = this.getGuestCart();
		String html = "\r\n<form action='../Appls/ShoppingAppl.jsp' method='get'>" + "		<div class='row'>\r\n"
				+ "			<div class='col-md-12 mb-3'>\r\n" + "				<h2 class='fw-bold text-black'>"
				+ (dataBase.equals("cart") ? "Ihr Warenkorb (" + this.allProducts.size() + ")"
						: "Ihre Wunschliste (" + this.allProducts.size() + ")")
				+ "</h2>\r\n" + "			</div>\r\n" + "		</div>\r\n" + "		<div class='row'>\r\n"
				+ "			<div class='col-md-8'>" + this.getProductsAsHtml(dataBase);

		html += "			</div>";
		return html;
	}

	// wird für Wishlist, Cart und Checkout benutzt, da die produktdarstellung immer gleich bleibt 
	// gibt in dataBase mit ob es sich um cart oder wishlist handelt, um die richtigen produkte einzufügen und die Optionen anzupassen
	// Wunschliste hat beispielweise kein "zur Kasse" button sondern "in den warenkorb"
	public String getProductsAsHtml(String dataBase) throws SQLException, UnsupportedEncodingException {
		String html = "";
		if (this.allProducts.size() != 0) {
			for (int product_id : this.allProducts.keySet()) {
				this.readInformationsOfProductFromDb(product_id);
				String productPathForImg = this.getProductName().replace(" ", "_") + "_" + this.getColor();
				html += "<div class='card rounded-3 mb-4'>\r\n" + "					<div class='card-body p-4'>\r\n"
						+ "						<div class='row d-flex justify-content-between align-items-center'>\r\n"
						+ "							<div class='col-md-2'>\r\n"
						+ "								<a href='"
						+ this.getHrefForProducts(this.getProductName(), this.getColor(), this.getRam(),
								this.getMemory())
						+ "'><img\r\n" + "									src='../../IMG/ProductMain/"
						+ productPathForImg + "_Main.jpeg'\r\n"
						+ "									class='img-fluid rounded-3' alt='Es konnte leider kein Bild geladen werden'></a>\r\n"
						+ "							</div>\r\n" + "							<div class='col-md-3'>\r\n"
						+ "								<p class='lead fw-normal mb-2'>" + this.getBrand() + " "
						+ this.getProductName() + "</p>\r\n";
				if (this.getMemory() != 0) {
					html += "								<p class='mb-0'>\r\n"
							+ "									<span class='text-muted'>Speicherplatz: </span>"
							+ this.getMemory() + " GB 								</p>\r\n";
				}
				if (this.getRam() != 0) {
					html += "								<p class='mb-0'>\r\n"
							+ "									<span class='text-muted'>Arbeitsspeicher: </span>"
							+ this.getRam() + " GB 								</p>\r\n";
				}
				html += "								<p class='mb-0'>\r\n"
						+ "									<span class='text-muted'>Farbe: </span>" + this.getColor()
						+ "\r\n" + "								</p>\r\n" + "							</div>\r\n";
				if (dataBase.equalsIgnoreCase("cart")) {
					html += "							<div class='col-md-2 offset-1'>\r\n"
							+ "								<label for='quantitySelect' class='mb-1'>Anzahl:</label>\r\n<br>\r\n<span id='quantityText'>";

					if (this.getStock() != 0) {
						for (int i = 0; i <= this.getStock(); i++) {
							// checke erst ob Kunde mehr in den Warenkorb getan hat als auf Lager, wenn ja
							// wird die höchste Verfügbare Menge angezeigt
							if (this.allProducts.get(product_id) < this.getStock()) {
								if (i == this.allProducts.get(product_id)) {
									html += i + " Stk.</span>\r\n";
									break;
								}
							} else {
								if (i == this.getStock()) {
									html += i + " Stk.</span>\r\n";
									break;
								}
							}
						}

					} else {
						html += "<p style='color: red;'>nicht verfügbar</p></span>\r\n";
					}
					html += "							</div>\r\n";
				}
				html += "							<div class='col-md-2 offset-lg-1'>\r\n"
						+ "								<h5 class='mb-0'>" + this.removeZeros(this.getPrice())
						+ " &euro;</h5>\r\n" + "							</div>\r\n"
						+ "							<div id='delete' class='col-md-1 text-end'>\r\n"
						+ "								<button class='btn btn-link text-danger' name='btnDeleteProductFrom_"
						+ dataBase.toLowerCase() + "' value='" + product_id + "'>\r\n"
						+ "									<i class='fas fa-trash fa-lg'></i>\r\n</button>\r\n";

				if (dataBase.equalsIgnoreCase("wishlist")) {
					html += "								<button class='btn btn-link' name='btnWishlistProductIntoCart"
							+ "' value='" + product_id + "'>\r\n"
							+ "									<i class='fas fa-shopping-cart fa-lg'></i>";
					html += "\r\n" + "								</button>\r\n";
				} else if (dataBase.equalsIgnoreCase("cart")) {
					html += "<div class='btn-group' role='group'>\r\n";
					// Wenn
					html += "    <button class='btn btn-link' name='btnDecreaseQuantity' value='" + product_id
							+ "'>\r\n";
					html += "        <i class='fas fa-minus fa-sm'></i>\r\n";
					html += "    </button>\r\n";
					html += "    <button class='btn btn-link' name='btnIncreaseQuantity' value='" + product_id
							+ "' style='margin-left: -15px;'>\r\n";
					html += "        <i class='fas fa-plus fa-sm'></i>\r\n";
					html += "    </button>\r\n";
					html += "</div>\r\n";
				}

				html += "\r\n" + "							</div>\r\n" + "						</div>\r\n"
						+ "					</div>\r\n" + "				</div>\r\n";

				// addiere Summe nur wenn es um den Warenkorb geht
				if (dataBase.equalsIgnoreCase("cart"))
					this.setPriceSum(this.getPriceSum() + (this.getPrice() * this.allProducts.get(product_id)));

			}

		} else {
			html += "<p>Es befinden sich derzeit keine Artikel in "
					+ (dataBase.equals("cart") ? "Ihrem Warenkorb" : "Ihrer Wunschliste") + "</p>";
		}
		return html;
	}

	public String getAddressAsHtml() throws SQLException {
		this.setAddressInformations();
		String html = "<div class='card mb-4 mt-5'>\r\n" + "    <div class='card-header py-3'>\r\n"
				+ "        <h5 class='mb-0'>Lieferadresse</h5>\r\n" + "    </div>\r\n"
				+ "    <div class='card-body'>\r\n" + "        <div class='mb-3'>\r\n"
				+ "            <label for='first_name' class='form-label'>Vorname</label> <input type='text' class='form-control' name='first_name'\r\n"
				+ "                placeholder='Vorname' value='" + this.getFirstName() + "'>\r\n"
				+ "        </div>\r\n" + "        <div class='mb-3'>\r\n"
				+ "            <label for='last_name' class='form-label'>Nachname</label> <input type='text' class='form-control' name='last_name'\r\n"
				+ "                placeholder='Nachname' value='" + this.getLastName() + "'>\r\n"
				+ "        </div>\r\n" + "        <div class='row mb-3'>\r\n" + "            <div class='col-md-6'>\r\n"
				+ "                <label for='street' class='form-label'>Straße</label> <input\r\n"
				+ "                    type='text' class='form-control' name='street'\r\n"
				+ "                    placeholder='Straße' value='" + this.getStreet() + "'>\r\n"
				+ "            </div>\r\n" + "            <div class='col-md-6'>\r\n"
				+ "                <label for='house_number' class='form-label'>Hausnummer</label> <input\r\n"
				+ "                    type='text' class='form-control' name='house_number'\r\n"
				+ "                    placeholder='Hausnummer' value='" + this.getHouseNumber() + "'>\r\n"
				+ "            </div>\r\n" + "        </div>\r\n" + "        <div class='row mb-3'>\r\n"
				+ "            <div class='col-md-6'>\r\n"
				+ "                <label for='postcode' class='form-label'>PLZ</label> <input\r\n"
				+ "                    type='number' class='form-control' name='postcode' placeholder='PLZ' value='"
				+ this.getPostcode() + "'>\r\n" + "            </div>\r\n" + "            <div class='col-md-6'>\r\n"
				+ "                <label for='city' class='form-label'>Stadt</label> <input\r\n"
				+ "                    type='text' class='form-control' name='city'\r\n"
				+ "                    placeholder='Stadt' value='" + this.getCity() + "'>\r\n"
				+ "            </div>\r\n" + "        </div>\r\n" + "        <div class='mb-3 form-check'>\r\n"
				+ "            <input type='checkbox' class='form-check-input' id='remember_address' name='rememberAddress' value='remember'>\r\n"
				+ "            <label class='form-check-label' for='remember_address'>Diese Adresse merken?</label>\r\n"
				+ "        </div>\r\n" + "    </div>\r\n" + "</div>\r\n";
		return html;
	}

	public String getPaymentMethodAsHtml() {
		String html = "				<div class='card mb-4'>\r\n"
				+ "					<div class='card-header py-3'>\r\n"
				+ "						<h5 class='mb-0'>Zahlungsmethode</h5>\r\n" + "					</div>\r\n"
				+ "					<div class='card-body'>\r\n"
				+ "						<div class='table-responsive'>\r\n"
				+ "							<table class='table table-borderless'>\r\n"
				+ "								<tbody>\r\n"
				+ "									<tr class='payment-method' data-bs-toggle='collapse'\r\n"
				+ "										data-bs-target='#paypal-info'>\r\n"
				+ "										<td><span class='payment-label'>PayPal</span></td>\r\n"
				+ "									</tr>\r\n"
				+ "									<tr class='collapse' id='paypal-info'>\r\n"
				+ "										<td colspan='3'>\r\n"
				+ "											<div class='card card-body'>\r\n"
				+ "												<div class='mb-3'>\r\n"
				+ "													<label for='paypal-email' class='form-label'>PayPal\r\n"
				+ "														E-Mail</label> <input type='email' class='form-control'\r\n"
				+ "														id='payment_method' placeholder='Geben Sie hier Ihre E-Mail-Adresse ein' name='payment_method' value=''>\r\n"
				+ "												</div>\r\n"
				+ "											</div>\r\n"
				+ "										</td>\r\n" + "									</tr>\r\n"
				+ "									<tr class='payment-method' data-bs-toggle='collapse'\r\n"
				+ "										data-bs-target='#sepa-info'>\r\n"
				+ "										<td><span class='payment-label'>Vorkasse</span></td>\r\n"
				+ "									</tr>\r\n"
				+ "									<tr class='collapse' id='sepa-info'>\r\n"
				+ "										<td colspan='3'>\r\n"
				+ "											<div class='card card-body'>\r\n"
				+ "												<div class='mb-3'>\r\n"
				+ "													<label for='iban' class='form-label'><b>IBAN</b></label>\r\n<p><b>Unsere Bankverbindung:</b></p>\r\n"
				+ "                                                          <p><b>Name des Empfängers:</b> NAS Elektronik Shop</p>"
				+ "                                                          <p><b>Name der Bank:</b> Deutsche Bank</p>"
				+ "                                                          <p><b>IBAN:</b> DE5455 63380 0990 000</p>"
				+ "                                                          <p><b>Verwendungszweck:</b> 63300 - "
				+ this.getUser_id() + " (Bitte vollständig angeben)</p>"
				+ "                                                          <p>Bitte überweisen Sie innerhalb der nächsten 14 Werktage</p>"
				+ "												</div>\r\n"
				+ "											</div>\r\n"
				+ "										</td>\r\n" + "									</tr>\r\n"
				+ "								</tbody>\r\n" + "							</table>\r\n"
				+ "						</div>\r\n" + "					</div>\r\n" + "\r\n"
				+ "				</div>\r\n";
		return html;
	}

	public String getSummaryAsHtml(String viewName) {
		String html = "";
		double deliveryCosts = 4.95;
		if (this.getPriceSum() > 2000)
			deliveryCosts = 0;
		if (this.allProducts.size() != 0) {
			html += "<div class='col-md-4'>\r\n" + "				<div id='summary' class='card mb-4'>\r\n";
			if (viewName.equalsIgnoreCase("cart") || viewName.equalsIgnoreCase("checkout")) {
				html += "					<div class='card-header py-3'>\r\n"
						+ "						<h5 class='mb-0'>Summe</h5>\r\n" + "					</div>\r\n";
			}
			html += "					<div class='card-body'>\r\n";
			if (viewName.equalsIgnoreCase("cart") || viewName.equalsIgnoreCase("checkout")) {
				html += "						<ul class='list-group list-group-flush'>\r\n"
						+ "							<li\r\n"
						+ "								class='list-group-item d-flex justify-content-between align-items-center border-0 px-0 pb-0'>\r\n"
						+ "								Artikel <span>" + this.removeZeros(this.getPriceSum())
						+ " &euro;</span>\r\n" + "							</li>\r\n"
						+ "							<li\r\n"
						+ "								class='list-group-item d-flex justify-content-between align-items-center px-0'>\r\n"
						+ "								Lieferkosten <span>"
						+ (deliveryCosts == 0 ? "Gratis" : deliveryCosts + "&euro;") + "</span>\r\n"
						+ "							</li>\r\n" + "							<li\r\n"
						+ "								class='list-group-item d-flex justify-content-between align-items-center border-0 px-0 mb-3'>\r\n"
						+ "								<div>\r\n"
						+ "									<strong>Gesamtbetrag</strong>\r\n"
						+ "								</div> <span><strong>"
						+ this.removeZeros(this.getPriceSum() + deliveryCosts) + " &euro;</strong></span>\r\n"
						+ "							</li>\r\n" + "						</ul>\r\n" + "\r\n";
			}

			html += "						<button class='btn btn-primary btn-lg btn-block' " + (this.getStock() == 0
					&& (viewName.equalsIgnoreCase("cart") || viewName.equalsIgnoreCase("checkout")) ? " disabled" : "")
					+ " name=";
			// angepasst daran auf welcher View man ist hat der Button einen anderen Namen
			if (viewName.equalsIgnoreCase("cart"))
				html += "'btnGoToCheckout'";
			else if (viewName.equalsIgnoreCase("checkout"))
				html += "'btnBuy'";
			else
				html += "'btnWishlistIntoCart'";
			html += " value='btnWasClicked'>\r\n" + "							";
			if (viewName.equalsIgnoreCase("cart"))
				html += "zur Kasse gehen";
			else if (viewName.equalsIgnoreCase("checkout"))
				html += "Jetzt Bestellen";
			else
				html += "Alles in den Warenkorb";
			html += " </button>\r\n" + "					</div>\r\n" + "				</div>\r\n"
					+ "			</div>\r\n";
		}
		return html;
	}

	public void readProductIdsFromDb(String dataBase) throws SQLException {
		this.allProducts = new HashMap<Integer, Integer>();
		if (this.getUser_id() != Integer.MIN_VALUE) {
			String sql = "SELECT product_id" + (dataBase.equalsIgnoreCase("cart") ? ", quantity" : "") + " FROM "
					+ dataBase + " WHERE user_id = ?";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, this.getUser_id());
			ResultSet dbRes = prep.executeQuery();
			while (dbRes.next()) {
				this.allProducts.put(dbRes.getInt("product_id"),
						(dataBase.equalsIgnoreCase("cart") ? dbRes.getInt("quantity") : 1));

			}
		}
	}

	public void readInformationsOfProductFromDb(int product_id) throws SQLException {
		this.setBrand("");
		this.setProductName("");
		this.setColor("");
		this.setStock(0);
		this.setRam(0);
		this.setMemory(0);
		this.setPrice(0);
		if (product_id != 0) {
			String sql = "SELECT brand, product_name, color, stock, price, ram, physical_memory FROM products WHERE product_id = ?";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, product_id);
			ResultSet dbRes = prep.executeQuery();
			if (dbRes.next()) {
				this.setBrand(dbRes.getString("brand"));
				this.setProductName(dbRes.getString("product_name"));
				this.setColor(dbRes.getString("color"));
				this.setStock(dbRes.getInt("stock"));
				this.setRam(dbRes.getInt("ram"));
				this.setMemory(dbRes.getInt("physical_memory"));
				this.setPrice(dbRes.getDouble("price"));
			}
		}
	}

	public void addToCart(int product_id) throws SQLException {
		String sql = "";
		if (this.getUser_id() != Integer.MIN_VALUE && product_id != 0) {
			int inStock = this.checkStockBeforeFillInCart(product_id);
			int quantity = this.getQuantityFromCart(product_id);
			if (quantity != inStock) { // führt prep nur aus wenn ganz sicher ist, dass weniger im Warenkorb sind als
										// im Stock (quantity ungleich inStock und da danach gecheckt wird,
										// dass nur hinzugefügt wird wenn quantitiy kleiner als inStock kann man nie
										// mehr hinzufügen als verfügbar sind)
				if (inStock != 0) {

					if (quantity == 0 && inStock > 0)
						sql = "insert into cart (user_id, product_id) VALUES (?,?)";
					else if (quantity < inStock)
						sql = "UPDATE cart SET quantity = (" + quantity + " + 1) WHERE user_id = ? AND product_id = ?";

					System.out.println(sql);
					PreparedStatement prep = this.dbConn.prepareStatement(sql);
					prep.setInt(1, this.getUser_id());
					prep.setInt(2, product_id);
					prep.executeUpdate();
				}
			}
		}
	}

	public int getQuantityFromCart(int product_id) throws SQLException {
		String sql = "SELECT quantity FROM cart WHERE user_id = ? AND product_id = ?";
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setInt(1, this.getUser_id());
		prep.setInt(2, product_id);
		ResultSet dbRes = prep.executeQuery();
		if (dbRes.next())
			return dbRes.getInt("quantity");
		else
			return 0;
	}

	public void addToWishlist(int product_id) throws SQLException {
		String sql = "";
		if (this.getUser_id() != Integer.MIN_VALUE && product_id != 0) {
			if (!this.checkIfAlreadyOnWishlist(product_id)) {
				sql = "insert into wishlist (user_id, product_id) VALUES (?,?)";
				System.out.println(sql);
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setInt(1, this.getUser_id());
				prep.setInt(2, product_id);
				prep.executeUpdate();
			}
		}
	}

	// checke ob ein Produkt auf Lager ist bevor man es in den Warenkorb tun kann

	public void fillWishlistIntoCart() throws SQLException {
		if (this.allProducts.size() > 0) {
			for (int currentProductId : this.allProducts.keySet()) {
				this.addToCart(currentProductId);
				if (!this.isOutOfStock()) {
					this.deleteProductFromShoppingList("wishlist", currentProductId);
				}

			}

		}
	}

	public int checkStockBeforeFillInCart(int product_id) throws SQLException {
		String sql = "SELECT stock FROM products WHERE product_id = ?";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setInt(1, product_id);
		ResultSet dbRes = prep.executeQuery();
		if (dbRes.next()) {
			if (dbRes.getInt("stock") == 0)
				this.setOutOfStock(true);
			else
				return dbRes.getInt("stock");

		}
		return 0;

	}

	// man kann Produkte nur einmal auf der Wunschliste haben 
	
	public boolean checkIfAlreadyOnWishlist(int product_id) throws SQLException {
		String sql = "SELECT * FROM wishlist WHERE user_id = ? AND product_id = ?";
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setInt(1, this.getUser_id());
		prep.setInt(2, product_id);
		ResultSet dbRes = prep.executeQuery();
		if (dbRes.next()) {
		}
		return dbRes.next();
	}

	public void deleteProductFromShoppingList(String dataBase, int product_id) throws SQLException {
		if (this.getUser_id() != Integer.MIN_VALUE && product_id != 0) {
			String sql = "DELETE FROM " + dataBase.toLowerCase() + " WHERE user_id = ? AND product_id = ?";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, this.getUser_id());
			prep.setInt(2, product_id);
			prep.executeUpdate();
		}
	}

	// ändert die Anzahl der Produkte in der DB Cart und im GuestCart wenn man in der View auf "-" oder "+" drückt
	public void changeQuantityOfProduct(String mathSign, int product_id) throws SQLException {
		if (this.isLoggedIn()) {
			if (this.getUser_id() != Integer.MIN_VALUE && product_id != 0) {
				if (this.allProducts.get(product_id) <= 1 && mathSign.equals("-")) {
					this.deleteProductFromShoppingList("cart", product_id);

				} else if (mathSign.equals("+")) {
					this.addToCart(product_id);

				} else {
					String sql = "UPDATE cart SET quantity = (" + this.getQuantityFromCart(product_id) + " " + mathSign
							+ " 1) WHERE user_id = ? AND product_id = ?";
					System.out.println(sql);
					PreparedStatement prep = this.dbConn.prepareStatement(sql);
					prep.setInt(1, this.getUser_id());
					prep.setInt(2, product_id);
					prep.executeUpdate();
				}
			}
		} else {
			if (mathSign.equals("+"))
				this.fillGuestCart(product_id);
			else if (mathSign.equals("-")) {
				if (this.getGuestCart().get(product_id) <= 1)
					this.removeFromGuestCart(product_id);
				else
					this.getGuestCart().put(product_id, (this.getGuestCart().get(product_id) - 1));

			}
		}
	}

	public void fillGuestCart(int product_id) throws SQLException {
		int inStock = this.checkStockBeforeFillInCart(product_id);
		if (inStock > 0) {
			int quantityOfGuestCart = this.getQuantityInGuestCart(product_id);
			if (quantityOfGuestCart > 0 && this.getGuestCart().get(product_id) < inStock) // nur so viele in den
																							// Warenkorb wie auch
																							// verfügbar
				this.getGuestCart().put(product_id, (this.getGuestCart().get(product_id) + 1));
			else if (quantityOfGuestCart == 0)
				this.getGuestCart().put(product_id, 1);
		}
	}

	public int getQuantityInGuestCart(int product_id) {
		if (this.getGuestCart().size() > 0) {
			for (int currentProductId : this.getGuestCart().keySet()) {
				if (currentProductId == product_id) {
					return this.getGuestCart().get(currentProductId);
				}
			}
		}
		return 0;
	}

	public void removeFromGuestCart(int product_id) {
		if (this.getGuestCart().size() > 0) {
			for (int currentProductId : this.getGuestCart().keySet()) {
				if (currentProductId == product_id) {
					this.getGuestCart().remove(product_id);
					break;
				}
			}
		}
	}

	public void insertGuestCartIntoCartDb() throws SQLException {
		for (int currentProductId : this.getGuestCart().keySet())
			for (int i = 0; i < this.getGuestCart().get(currentProductId); i++) {
				this.addToCart(currentProductId);
			}
	}

	// Methoden für Checkout

	public boolean checkStockBeforeCheckOut() throws SQLException {
		boolean inStock = true;
		for (int product_id : this.allProducts.keySet()) {
			if(!this.checkStock(product_id, this.allProducts.get(product_id))) {
				inStock = false;
				break;
			}
		}
		return inStock;
	}

	public boolean checkStock(int product_id, int quantity) throws SQLException {
		if (this.isLoggedIn() && product_id != 0 && quantity != 0) {
			String sql = "SELECT stock FROM products WHERE product_id = ?";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, product_id);
			ResultSet dbRes = prep.executeQuery();
			if (dbRes.next()) {
				if(dbRes.getInt("stock") == 0) return false;
				else if (dbRes.getInt("stock") >= quantity) {
					return true;
				}
			}
		}
		return false;
	}
	
	// wenn ein User mehr kaufen wollte als noch im Stock sind (beim Checkout)
	// wird sein Bestand auf die verfügbare Menge gesetzt, da der Fehler ja nur auftritt 
	// wenn er mehr kaufen wollte
	
	public void updateCartOfUser() throws SQLException {
		if (this.isLoggedIn() && this.getUser_id() != Integer.MIN_VALUE) {
			for(int productId : this.allProducts.keySet()) {
				String sql = "UPDATE cart SET quantity = '" + this.checkStockBeforeFillInCart(productId) + "' WHERE user_id = ? AND product_id = ?";
				System.out.println(sql);
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setInt(1, this.getUser_id());
				prep.setInt(2, productId);
				prep.executeUpdate();
			}
		}
	}

	public void deleteCartOfUser() throws SQLException {
		if (this.isLoggedIn() && this.getUser_id() != Integer.MIN_VALUE) {
			String sql = "DELETE FROM cart WHERE user_id = ?";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, this.getUser_id());
			prep.executeUpdate();
		}

	}

	public void rememberAddressOfUser(String street, String house_number, String city, int postcode)
			throws SQLException {
		if (this.isLoggedIn() && this.getUser_id() != Integer.MIN_VALUE) {
			String sql = "UPDATE users SET street = '" + street + "', house_number = '" + house_number
					+ "', postcode = '" + postcode + "', city = '" + city + "' WHERE user_id = ?";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, this.getUser_id());
			prep.executeUpdate();
		}
	}

	// Gekaufte Ware aus dem Bestand nehmen

	public void updateStock() throws SQLException {
		if (this.isLoggedIn()) {
			for (int product_id : this.allProducts.keySet()) {
				if (product_id != 0) {
					String sql = "UPDATE products SET stock = (stock - " + this.allProducts.get(product_id)
							+ ") WHERE product_id = ?";
					System.out.println(sql);
					PreparedStatement prep = this.dbConn.prepareStatement(sql);
					prep.setInt(1, product_id);
					prep.executeUpdate();
				}
			}
		}
	}

	// Rechnung und Bestellung erstellen

	public void createInvoiceForOrder(String payment_method) throws SQLException {
		this.createInvoiceForPurchase(payment_method);
		if (this.isLoggedIn() && this.getUser_id() != Integer.MIN_VALUE && this.getInvoice_id() != 0) {
			for (int product_id : this.allProducts.keySet()) {
				String sqlInsert = "insert into invoice_items (invoice_id, product_id, quantity, price_per_unit) VALUES (?, ?, ?, ?)";
				System.out.println(sqlInsert);
				PreparedStatement prepInsert = this.dbConn.prepareStatement(sqlInsert);
				prepInsert.setInt(1, this.getInvoice_id());
				prepInsert.setInt(2, product_id);
				prepInsert.setInt(3, this.allProducts.get(product_id));
				prepInsert.setDouble(4, this.getPriceOfProduct(product_id));
				prepInsert.executeUpdate();
			}
		}
	}
	
	// erstellt eine Rechnung in der DB die PayPal direkt auf bezahlt setzt, da es über eine PayPal API direkt bezahlt wäre

	public void createInvoiceForPurchase(String payment_method) throws SQLException {
		String payment_status = "offen";
		if (!payment_method.isBlank()) {
			payment_status = "bezahlt";
			payment_method = "PayPal";
		} else
			payment_method = "Überweisung";
		if (this.isLoggedIn() && this.getUser_id() != Integer.MIN_VALUE) {
			LocalDate currentDate = LocalDate.now();
			String date = currentDate.toString();
			String sql = "insert into invoice_details (user_id, total_amount, invoice_date, payment_method, payment_status) VALUES (?, ?, ?, ?, ?) returning invoice_id";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, this.getUser_id());
			prep.setDouble(2, this.getPriceSum());
			prep.setString(3, date);
			prep.setString(4, payment_method);
			prep.setString(5, payment_status);
			ResultSet dbRes = prep.executeQuery();
			if (dbRes.next())
				this.setInvoice_id(dbRes.getInt("invoice_id"));
		}
	}

	public double getPriceOfProduct(int product_id) throws SQLException {
		String sql = "SELECT price FROM products WHERE product_id = ?";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setInt(1, product_id);
		ResultSet dbRes = prep.executeQuery();
		if (dbRes.next())
			return dbRes.getDouble("price");
		return 0;
	}

	public void setAddressInformations() throws SQLException {
		this.setEmail("mustermail@muster.com");
		this.setFirstName("Max");
		this.setLastName("MusterMann");
		this.setStreet("Musterstrasse");
		this.setCity("Musterstadt");
		this.setHouseNumber("1a");
		this.setPostcode(0000);

		if (this.isLoggedIn() && this.getUser_id() != Integer.MIN_VALUE) {
			String sql = "SELECT email, first_name, last_name, street, house_number, city, postcode FROM users WHERE user_id = ?";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, this.getUser_id());
			ResultSet dbRes = prep.executeQuery();
			if (dbRes.next()) {
				if (dbRes.getString("email") != null)
					this.setEmail(dbRes.getString("email"));
				if (dbRes.getString("first_name") != null)
					this.setFirstName(dbRes.getString("first_name"));
				if (dbRes.getString("last_name") != null)
					this.setLastName(dbRes.getString("last_name"));
				if (dbRes.getString("street") != null)
					this.setStreet(dbRes.getString("street"));
				if (dbRes.getString("city") != null)
					this.setCity(dbRes.getString("city"));
				if (dbRes.getString("city") != null)
					this.setHouseNumber(dbRes.getString("house_number"));
				if (dbRes.getInt("postcode") != 0)
					this.setPostcode(dbRes.getInt("postcode"));
			}
		}
	}

	/*
	 * Href für jedes Produkt erstellen
	 */

	public String getHrefForProducts(String product, String color, int ram, int memory)
			throws UnsupportedEncodingException {
		String href = "../Appls/ProductAppl.jsp?product_name=" + URLEncoder.encode(product, "UTF-8");
		if (color.isBlank()) {
			if (color.equalsIgnoreCase("Weiss"))
				color = ("Weiß");
			else
				href += "&color=" + URLEncoder.encode(color, "UTF-8");
		}
		if (ram != 0)
			href += "&ram=" + this.getRam();
		if (memory != 0)
			href += "&physical_memory=" + memory;
		return href;
	}

	// checken ob Nullen entfernt werden müssen wenn Preis z.B. .00 ist in z.B.
	// ProductBean und ShoppingBean

	public String removeZeros(double price) {
		DecimalFormat format = new DecimalFormat("0.##");
		return format.format(price);
	}

	// Bestellübersicht für die Bestätigungsmail

	public void createOrderConfirmationMailAsHtml() throws MessagingException, SQLException, IOException {
		String html = "";
		ArrayList<String> products = new ArrayList<String>();
		ArrayList<String> productIds = new ArrayList<String>();
		ArrayList<String> quantity = new ArrayList<String>();
		ArrayList<String> prices = new ArrayList<String>();
		double total = 0;
		double taxes;		
		if (this.getEmail() != null && !this.getEmail().isBlank()) {
			html = "<html><body>" + "<p>Hallo " + this.getFirstName() + " " +  this.getLastName() + ",</p>"
					+ "<p>Vielen Dank für Ihre Bestellung! Bestellübersicht:</p>" + "<table border='1'>"
					+ "<tr>" + "<td>Anzahl:</td>" + "<td>Produktname:</td>" + "<td>Farbe:</td>"
					+ "<td>Preis pro Stück:</td>" + "</tr>";
			for (int product_id : this.allProducts.keySet()) {
				this.readInformationsOfProductFromDb(product_id);
				quantity.add(String.valueOf(this.allProducts.get(product_id)));
				html += "<tr>" + "<td>" + this.allProducts.get(product_id) + "</td>" + "<td>" + this.getBrand() + " "
						+ this.getProductName() + " " + (this.getRam() != 0 ? " mit " + this.getRam() + " GB Ram " : "")
						+ (this.getMemory() != 0 ? " und " + this.getMemory() + " GB Speicher" : "") + "</td>" + "<td>"
						+ this.getColor() + "</td>" + "<td>" + this.removeZeros(this.getPrice()) + " €</td>" + "</tr>";
			products.add(this.getBrand() + " " + this.getProductName());
			productIds.add(String.valueOf(product_id));
			prices.add(this.removeZeros(this.getPrice()));	
			total += this.getPrice();
			}

			html += "</table>"

					+ "<p>Gesamtbetrag: " + this.removeZeros(this.getPriceSum()) + " €</p>"
					+ "<p>Ihre Rechnungsnummer ist die " + this.getInvoice_id() + "</p>"
					+ "<p>Die Rechnung finden Sie im Anhang.</p>" + "</body></html>";

		}
		PDFBean invoice = new PDFBean();
		LocalDate currentDate = LocalDate.now();
		String date = currentDate.toString();	
		taxes = 0.19 * total;
		invoice.setHtml(html);
		invoice.setUser_id(String.valueOf(this.getUser_id()));
		invoice.setEmail(this.getEmail());
		invoice.setFirst_name(this.getFirstName());
		invoice.setLast_name(this.getLastName());
		invoice.setInvoice_id(String.valueOf(this.getInvoice_id()));
		invoice.setDate(date);
		invoice.setTotal(this.removeZeros(this.getPriceSum()));
		invoice.setTaxes(this.removeZeros(taxes));
		invoice.productIds = productIds;
		invoice.quantity = quantity;
		invoice.prices = prices;
		invoice.products = products;
		
		invoice.createInvoicePDF();
	}

	// Getter und Setter - Methoden

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public int getPostcode() {
		return postcode;
	}

	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getInvoice_id() {
		return invoice_id;
	}

	public void setInvoice_id(int invoice_id) {
		this.invoice_id = invoice_id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setPriceSum(double priceSum) {
		this.priceSum = priceSum;
	}

	public double getPriceSum() {
		return priceSum;
	}

	public boolean isOutOfStock() {
		return outOfStock;
	}

	public void setOutOfStock(boolean outOfStock) {
		this.outOfStock = outOfStock;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public HashMap<Integer, Integer> getGuestCart() {
		return guestCart;
	}

	public void setGuestCart(HashMap<Integer, Integer> guestCart) {
		this.guestCart = guestCart;
	}
}
