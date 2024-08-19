package Beans;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import Connection.NoConnectionException;
import Connection.PostgreSQLAccess;

public abstract class AbstractBean {

	String brand;
	String color;
	double price;
	int ram;
	int physical_memory;
	ArrayList<String> smartphonesToShow;
	ArrayList<String> notebooksToShow;
	ArrayList<String> tabletsToShow;
	ArrayList<String> otherProductsToShow;
	Connection dbConn;

	// Connection-Methode

	public void createConnection() throws NoConnectionException {
		this.dbConn = new PostgreSQLAccess().getConnection();
	}

	public String getAllProductContainer(ArrayList<String> currentProductsToShow, String category) throws SQLException, UnsupportedEncodingException {
		String container = "";
		if (currentProductsToShow != null && currentProductsToShow.size() > 0) {
			if (category != null && !category.isBlank()) {
				container = "<div class='container mt-5 text-center'>\r\n" + "    <h2>"
						+ (!category.equalsIgnoreCase("Other") ? category + "s" : "Weitere spannende Produkte")
						+ "</h2>\r\n" + "    <hr>\r\n" + "</div>\r\n  <div class='container mt-5'>\r\n"
						+ "  <div class='row'>\r\n";
			}

			for (String currentProduct : currentProductsToShow) {
				this.setParametersWithDB(currentProduct);
				String productPathForImg = currentProduct.replace(" ", "_") + "_" + this.getColor(); // dynamischer auf
																										// Bilder //
																										// zugreifen
				container += "			<div id='mainCard' class='col-4 d-flex justify-content-center mb-3'>\r\n"
						+ "				<div class='card'>\r\n"
						+ "	<a href='" + this.getHrefForProducts(currentProduct, this.getColor(), this.getRam(), this.getPhysical_memory()) + "'><div class='image-container'>\r\n" + "						<img\r\n"
						+ "							src='../../IMG/ProductMain/" + productPathForImg
						+ "_Main.jpeg'\r\n" + "							class='card-img-top img-fluid'\r\n"
						+ "							alt='Bild konnte leider nicht geladen werden'>\r\n"
						+ "					</div></a>\r\n" + "					<div class='card-body text-center border-top'>\r\n"
						+ "						<div class='card-title'>\r\n" + "							<b>"
						+ this.getBrand() + " " + currentProduct
						+ (this.getPrice() != 0 && this.getPrice() != Integer.MAX_VALUE ? " ab " + this.removeZeros(this.getPrice()) + " &euro;" : "")
						+ "</b>\r\n";
				container += "						</div>\r\n" + "						<p class='card-text'>"
						+ this.getDescriptionFirstSentence(currentProduct) + "</p>\r\n" + "					</div>\r\n"
						+ "					<div class='card-footer text-center'>\r\n" + "						<a\r\n"
						+ "							href='" + this.getHrefForProducts(currentProduct, this.getColor(), this.getRam(), this.getPhysical_memory()) + "'\r\n"
						+ "							class='btn btn-dark'>Zum Produkt</a>\r\n"
						+ "					</div>\r\n" + "				</div>\r\n" + "			</div>\r\n";

			}

			container += "		</div>\r\n" + "	</div>\r\n";

		}

		return container;
	}	

	/*
	 * Href für jedes Produkt erstellen
	 */

	public String getHrefForProducts(String product, String color, int ram, int memory) throws UnsupportedEncodingException {
		String href = "../Appls/ProductAppl.jsp?product_name=" + URLEncoder.encode(product, "UTF-8");
		if (color.isBlank()) {
			if (color.equalsIgnoreCase("Weiss"))
				color = ("Weiß");
			else
				href += "&color=" +  URLEncoder.encode(color, "UTF-8");
		}
		if (ram != 0)
			href += "&ram=" + this.getRam();
		if (memory != 0)
			href += "&physical_memory=" + memory;
		return href;
	}


	public void setParametersWithDB(String product_name) throws SQLException {
		String sql = "SELECT brand, color, ram, physical_memory, price FROM products WHERE product_name ILIKE ?";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setString(1, product_name);
		ResultSet dbRes = prep.executeQuery();
		this.setPrice(Integer.MAX_VALUE);
		if (dbRes.next()) {
			this.setBrand(dbRes.getString("brand"));
			this.setColor(dbRes.getString("color"));
			this.setPhysical_memory(dbRes.getInt("physical_memory"));
			this.setRam(dbRes.getInt("ram"));
			
			// Ab hier setze ich nur den Preis neu damit der niedrigste Preis angezeigt wird aber damit man nicht zwangsläufig
			// zum billigsten Produkt direkt weitergeleitet wird, verändere ich die anderen Parameter nicht mehr
			if(this.getPrice() > dbRes.getDouble("price")) this.setPrice(dbRes.getDouble("price"));
			while(dbRes.next()) if(this.getPrice() > dbRes.getDouble("price")) this.setPrice(dbRes.getDouble("price"));
		} else {
			this.setBrand("");
			this.setColor("");
			this.setPhysical_memory(0);
			this.setRam(0);
			this.setPrice(0);
		}
	}
	
	// Der erste Satz der Artikelbeschreibung zur Vorschau in Produkt-Containern

	public String getDescriptionFirstSentence(String product_name) throws SQLException {
		String sql = "SELECT description FROM products WHERE product_name ILIKE ?";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setString(1, product_name);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next()) {
			String description = dbRes.getString("description");
			if (description != null && !description.isBlank()) {
				String[] sentences = description.split("\\.\\s+");
				if (sentences != null && !sentences[0].isBlank())
					return sentences[0];
			}
		}
		return "Entdecken Sie die neuesten Elektronik-Highlights in unserem Shop und erleben Sie Innovation pur! Egal ob Smartphones, "
				+ "Tablets oder Notebooks";
	}

	// benötigt um alle gefundenen Produkte umzufüllen (siehe Searchbean und
	// Categorybean)

	public void fillArrayLists(HashMap<String, String> allProducts) {
		this.smartphonesToShow = new ArrayList<>();
		this.notebooksToShow = new ArrayList<>();
		this.tabletsToShow = new ArrayList<>();
		this.otherProductsToShow = new ArrayList<>();

		for (String currentProduct : allProducts.keySet()) {

			if (allProducts.get(currentProduct).equalsIgnoreCase("Smartphone"))
				this.smartphonesToShow.add(currentProduct);
			else if (allProducts.get(currentProduct).equalsIgnoreCase("Notebook"))
				this.notebooksToShow.add(currentProduct);
			else if (allProducts.get(currentProduct).equalsIgnoreCase("Tablet"))
				this.tabletsToShow.add(currentProduct);
			else
				this.otherProductsToShow.add(currentProduct);
		}
	}

	// checken ob Nullen entfernt werden müssen wenn Preis z.B. .00 ist in z.B.
	// ProductBean und ShoppingListBean

	public String removeZeros(double price) {
		DecimalFormat format = new DecimalFormat("0.##");
		return format.format(price);
	}

	// Error

		public String getErrorpageAsHtml() {
			return "<div class='error_container'>\r\n<div class='error_code'>404 Not Found</div>\r\n"
					+ "<div class='error_message'>Etwas ist schiefgelaufen! Bitte\r\n"
					+ "	versuchen Sie es später erneut.</div>\r\n<div class='smiley'>&#128546;</div>\r\n"
					+ "<a class='link'\r\n href='../Views/MainpageView.jsp'>Zurück\r\n" + "	zur Startseite</a>\r\n</div>";
		}

	// Getter und Setter-Methoden

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getRam() {
		return ram;
	}

	public void setRam(int ram) {
		this.ram = ram;
	}

	public int getPhysical_memory() {
		return physical_memory;
	}

	public void setPhysical_memory(int physical_memory) {
		this.physical_memory = physical_memory;
	}

}
