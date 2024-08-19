package Beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import Connection.NoConnectionException;
import Connection.PostgreSQLAccess;

public class FrameworkBean {

	int user_id;
	int countWishlist;
	int countCart;
	boolean loggedIn;
	HashMap<Integer, Integer> guestCart;
	Connection dbConn;

	public FrameworkBean() throws SQLException {
		this.createConnection();
		this.setUser_id(Integer.MIN_VALUE);
		this.setCountCart(0);
		this.setCountWishlist(0);
		this.setLoggedIn(false);
		this.guestCart = new HashMap<Integer, Integer>();
	}

	// Connection-Methode

	public void createConnection() throws NoConnectionException {
		this.dbConn = new PostgreSQLAccess().getConnection();
	}

	// Anzahl der Produkte im Warenkorb & Wunschliste

	public void setNumberOfProductsInCart() throws SQLException {
		this.setCountCart(0);
		int quantity = 0;
		if (this.getUser_id() != Integer.MIN_VALUE && this.isLoggedIn()) {
			String sql = "SELECT quantity FROM cart WHERE user_id = ?";
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, this.getUser_id());
			ResultSet dbRes = prep.executeQuery();
			while (dbRes.next()) {
				quantity += dbRes.getInt(1);
			}
			this.setCountCart((quantity));
		} else {
			for (int products : this.getGuestCart().keySet()) {
				quantity += this.getGuestCart().get(products);
			}
			this.setCountCart(quantity);
		}
	}

	public void setNumberOfProductsInWishlist() throws SQLException {
		this.setCountWishlist(0);
		if (this.getUser_id() != Integer.MIN_VALUE && this.isLoggedIn()) {
			String sql = "SELECT COUNT(product_id) FROM wishlist WHERE user_id = ?";
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, this.getUser_id());
			ResultSet dbRes = prep.executeQuery();
			if (dbRes.next())
				this.setCountWishlist(dbRes.getInt(1));
		}
	}

	// Header erstellen - Methoden

	public String getHeaderAsHtml() throws SQLException {
		this.setNumberOfProductsInCart();
		this.setNumberOfProductsInWishlist();
		String html = "<header>\r\n<form action='../Appls/HeaderAppl.jsp' method='get'>\r\n"
				+ "    <div class='fixed-top'>\r\n"
				+ "    <nav class='navbar navbar-expand-lg bg-dark navbar-dark'>\r\n"
				+ "      <div class='container-fluid'>\r\n"
				+ "        <a class='navbar-brand' href='../Views/MainpageView.jsp'><img src='../../IMG/Header/Logo.png' alt='NAS' width='200' height='44'></a>\r\n"
				+ "        \r\n" + "        <div class='container navbar justify-content-center' id='navbar'>\r\n"
				+ "\r\n"
				+ "          <input class='form-control me-2' type='search' placeholder='Wonach suchen Sie?' aria-label='Search' name ='searchedWords' style='width: 720px;'>\r\n"
				+ "          <button class='btn btn-outline-light' type='submit' name='btnSearch' value='suchen'>Suchen</button>\r\n"
				+ "\r\n" + "        </div>\r\n" + "\r\n"
				+ "        <div class='navbar justify-content-end' id='navbar'>\r\n"
				+ "          <div class='navbar-nav'>\r\n"
				+ "            <a class='nav-link position-relative' href='../Views/CartView.jsp' id='cart-icon'><img title='cart' src='../../IMG/Header/Cart.png' width='30'>";
		if (this.getCountCart() > 0)
			html += "<span class='itemCount' id='cart_itemCount'></span>";
		html += "           </a>\r\n<a class='nav-link position-relative' href='../Views/WishlistView.jsp' id='wishlist-icon'><img title='wishlist' src='../../IMG/Header/Wishlist.png' width='30'>";
		if (this.getCountWishlist() > 0)
			html += "<span class='itemCount' id='wishlist_itemCount'></span>";
		html += "            </a>\r\n";
		html += "            <input type='hidden' id='cartCount' name='cartCount' value='" + this.getCountCart() + "'>\r\n"
	            + "            <input type='hidden' id='wishlistCount' name='wishlistCount' value='" + this.getCountWishlist() + "'>\r\n";
		html += this.getPopup();
		html += "<a class='nav-link' " + (this.isLoggedIn() ? "href='../Views/ProfileView.jsp'" : "")
				+ "><img title='account' " + (!this.isLoggedIn() ? "onclick='showPopup(\"account_popup\")'" : "") // \"
				// weil
				// ".."
				// Wert
				// übergeben
				// muss
				// und
				// nicht
				// '..'
				+ " src='../../IMG/Header/Account.png' width='30'></a>\r\n" + "          </div>\r\n"
				+ "        </div>\r\n" + "      </div>\r\n" + "    </nav>\r\n" + "\r\n"
				+ "    <nav class='navbar navbar-expand-lg bg-dark-subtle navbar-light'>\r\n"
				+ "      <div class='container-fluid'>\r\n"
				+ "        <div class='navbar-collapse justify-content-center' id='navbarCategories'>\r\n"
				+ "          <ul class='navbar-nav'>\r\n" + "            <li class='nav-item px-4'>\r\n"
				+ "              <a class='nav-link' href='../Views/AppleView.jsp'>Apple</a>\r\n"
				+ "            </li>\r\n" + "            <li class='nav-item px-4'>\r\n"
				+ "              <a class='nav-link' href='../Views/SamsungView.jsp'>Samsung</a>\r\n"
				+ "            </li>\r\n" + "            <li class='nav-item px-4'>\r\n"
				+ "              <a class='nav-link' href='../Views/HuaweiView.jsp'>Huawei</a>\r\n"
				+ "            </li>\r\n" + "            <li class='nav-item px-4'>\r\n"
				+ "              <a class='nav-link' href='../Views/DiscoverView.jsp'>Entdecken</a>\r\n"
				+ "            </li>\r\n" + "          </ul>\r\n" + "        </div>\r\n" + "      </div>\r\n"
				+ "    </nav>\r\n" + "    </div>\r\n" + "\r\n" + "    </form>\r\n</header>\r\n<div class='container'>";
		return html;

	}

	public String getPopup() throws SQLException {
		String html = new String();
		if (!this.isLoggedIn()) {
			html = "<div id='account_popup' class='bg-dark'\r\n"
					// \" weil ".." Wert übergeben muss und nicht ''
					+ "    style='display: none; z-index: 999; position: absolute; right: 0; padding: 10px; border-radius: 10px;'>\r\n"
					+ "    <span style='color:white; position: absolute; top: 5px; right: 10px; cursor: pointer;' onclick='closePopup(\"account_popup\")'>x</span>\r\n"
					+ "    <table>\r\n" + "        <tr>\r\n"
					+ "            <td colspan='2'><img src='../../IMG/Header/Logo.png' title='logo' width='120'></td>\r\n"
					+ "        </tr>\r\n" + "        <tr>\r\n"
					+ "            <td colspan= '2' style='color:white; font-weight: bold;'>Anmelden</td>\r\n"
					+ "        </tr>\r\n" + "        <tr>\r\n" + "            <td style='color:white;'>E-Mail:</td>\r\n"
					+ "            <td><input type='text' name='email' id='checkEmail' value='' style='border-radius: 10px; background-color: #f5f5f5;'></td>\r\n"
					+ "        </tr>\r\n" + "        <tr>\r\n"
					+ "        <td id='emailMsg' colspan='2' style='color: red; font-size: 12px; font-family: Verdana, sans-serif;'></td>\r\n"
					+ "        </tr>\r\n" + "        <tr>\r\n"
					+ "            <td style='color:white;'>Passwort:</td>\r\n"
					+ "            <td><input type='password' id='checkPassword' name='password' value='' style='border-radius: 10px; background-color: #f5f5f5;'></td>\r\n"
					+ "        </tr>\r\n" + "        <tr>\r\n"
					+ "        <td id='passwordMsg' colspan='2' style='color: red; font-size: 12px; font-family: Verdana, sans-serif;' ></td>\r\n"
					+ "        </tr>\r\n" + "        <tr>\r\n"
					+ "            <td colspan='2'><a href='../Views/RecoverPasswordView.jsp'>Passwort vergessen?</a>\r\n"
					+ "        </tr>\r\n" + "        <tr>\r\n"
					+ "            <td colspan='2'><button style='width: 100%;' name='btnLog' value='einloggen' onclick='return checkInput()'>Anmelden</button></td>\r\n"
					+ "        </tr>\r\n<tr>\r\n"
					+ "            <td colspan='2' style='color: white;'>Kein Account? Melden Sie sich "
					+ "            <a href='../Views/RegisterView.jsp' style='color: white; font-weight: bold; text-decoration: underline;'>hier</a> an!</td>\r\n"
					+ "            \r\n" + "        </tr>\r\n" + "    </table>\r\n" + "</div>\r\n";
		} else {

		}
		return html;
	}

	// Footer erstellen - Methoden

	public String getFooterAsHtml() {
		String html = "</div><footer class='bg-dark text-white py-3 mt-5'>\r\n" + "<div class='container'>\r\n"
				+ "  <div class='row'>\r\n" + "    <div class='col'>\r\n"
				+ "      <ul class='list-inline text-center'>\r\n"
				+ "        <li class='list-inline-item'><a href='../Views/ConditionsView.jsp'>AGB</a></li>\r\n"
				+ "        <li class='list-inline-item'><a href='../Views/DataProtectionView.jsp'>Datenschutz</a></li>\r\n"
				+ "        <li class='list-inline-item'><a href='../Views/ImprintView.jsp'>Impressum</a></li>\r\n"
				+ "      </ul>\r\n" + "    </div>\r\n" + "  </div>\r\n" + "  <div class='row'>\r\n"
				+ "    <div class='col text-center'>\r\n"
				+ "      <p>&copy; 2023 NASELEKTRONIK. All rights reserved.</p>\r\n" + "    </div>\r\n" + "  </div>\r\n"
				+ "</div>\r\n" + "</footer>";
		return html;

	}
	
	// Error 
	
	public String getErrorpageAsHtml() {
		return "<div class='error_container'>\r\n<div class='error_code'>404 Not Found</div>\r\n"
				+ "<div class='error_message'>Etwas ist schiefgelaufen! Bitte\r\n"
				+ "	versuchen Sie es später erneut.</div>\r\n<div class='smiley'>&#128546;</div>\r\n"
				+ "<a class='link'\r\n href='../Views/MainpageView.jsp'>Zurück\r\n" + "	zur Startseite</a>\r\n</div>";
	}

	// Getter und Setter - Methoden

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getCountWishlist() {
		return countWishlist;
	}

	public void setCountWishlist(int countWishlist) {
		this.countWishlist = countWishlist;
	}

	public int getCountCart() {
		return countCart;
	}

	public void setCountCart(int countCart) {
		this.countCart = countCart;
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
