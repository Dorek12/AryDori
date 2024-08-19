package Beans;

import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import Connection.NoConnectionException;

public class CategoryBean extends AbstractBean {

	// setzt die Verschiedenen Views für die Marken und Entdecken Kategorie
	// gibt immer nur die passenden nach Marke aus oder bei Entdecken alle Produkte
	
	public CategoryBean() throws NoConnectionException {
		super();
		this.createConnection();
	}

	public String getAppleViewAsHtml() throws SQLException, UnsupportedEncodingException {
		HashMap<String, String> allProducts = new HashMap<String, String>();
		String sql = "SELECT product_name, category FROM products WHERE brand ILIKE 'Apple'";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next())
			allProducts.put(dbRes.getString("product_name"), dbRes.getString("category"));
		String html = "<div class='div-video'>\r\n"
				+ "		<a href='../Appls/ProductAppl.jsp?product_name=MacBook%20Air%20M1&color=Silber&ram=8&physical_memory=256'>"
				+ "<video autoplay loop muted class='video' width='80%'>\r\n"
				+ "			<source src='../../VID/MacBook_Air_M1_Video.mp4' type='video/mp4'>\r\n"
				+ "		</video></a>\r\n" + "	</div>\r\n";
		this.fillArrayLists(allProducts);
		String htmlCheck = this.getAllProductContainer(this.smartphonesToShow, "Smartphone")
				+ this.getAllProductContainer(this.notebooksToShow, "Notebook")
				+ this.getAllProductContainer(this.tabletsToShow, "Tablet")
				+ this.getAllProductContainer(this.otherProductsToShow, "Other");
		if (htmlCheck != null && !htmlCheck.isBlank())
			html += htmlCheck;
		return html;
	}

	public String getSamsungViewAsHtml() throws SQLException, UnsupportedEncodingException {
		HashMap<String, String> allProducts = new HashMap<String, String>();
		String sql = "SELECT product_name, category FROM products WHERE brand ILIKE 'Samsung'";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next())
			allProducts.put(dbRes.getString("product_name"), dbRes.getString("category"));
		String html = "<div class='div-video'>\r\n"
				+ "		<a href='../Appls/ProductAppl.jsp?product_name=Z%20Fold5&color=Schwarz&ram=12&physical_memory=128'>"
				+ "<video autoplay loop muted class='video' width='80%'>\r\n"
				+ "			<source src='../../VID/Z_Fold5_Video.mp4' type='video/mp4'>\r\n"
				+ "		</video></a>\r\n" + "	</div>\r\n";
		this.fillArrayLists(allProducts);
		String htmlCheck = this.getAllProductContainer(this.smartphonesToShow, "Smartphone")
				+ this.getAllProductContainer(this.notebooksToShow, "Notebook")
				+ this.getAllProductContainer(this.tabletsToShow, "Tablet")
				+ this.getAllProductContainer(this.otherProductsToShow, "Other");
		if (htmlCheck != null && !htmlCheck.isBlank())
			html += htmlCheck;
		return html;
	}

	public String getHuaweiViewAsHtml() throws SQLException, UnsupportedEncodingException {
		HashMap<String, String> allProducts = new HashMap<String, String>();
		String sql = "SELECT product_name, category FROM products WHERE brand ILIKE 'Huawei'";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next())
			allProducts.put(dbRes.getString("product_name"), dbRes.getString("category"));
		String html = "<div class='div-video'>\r\n"
				+ "		<a href='../Appls/ProductAppl.jsp?product_name=MatePad%20SE%20WiFi%204&color=Schwarz&ram=4&physical_memory=128'>"
				+ "<video autoplay loop muted class='video' width='80%'>\r\n"
				+ "			<source src='../../VID/MatePad_SE_WiFi_4.mp4' type='video/mp4'>\r\n"
				+ "		</video></a>\r\n" + "	</div>\r\n";
		this.fillArrayLists(allProducts);
		String htmlCheck = this.getAllProductContainer(this.smartphonesToShow, "Smartphone")
				+ this.getAllProductContainer(this.notebooksToShow, "Notebook")
				+ this.getAllProductContainer(this.tabletsToShow, "Tablet")
				+ this.getAllProductContainer(this.otherProductsToShow, "Other");
		if (htmlCheck != null && !htmlCheck.isBlank())
			html += htmlCheck;
		return html;
	}

	public String getDiscoverViewAsHtml() throws SQLException, UnsupportedEncodingException {
		HashMap<String, String> allProducts = new HashMap<String, String>();
		String sql = "SELECT product_name, category FROM products";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next())
			allProducts.put(dbRes.getString("product_name"), dbRes.getString("category"));
		String html = "";
		this.fillArrayLists(allProducts);
		String htmlCheck = this.getAllProductContainer(this.smartphonesToShow, "Smartphone")
				+ this.getAllProductContainer(this.notebooksToShow, "Notebook")
				+ this.getAllProductContainer(this.tabletsToShow, "Tablet")
				+ this.getAllProductContainer(this.otherProductsToShow, "Other");
		if (htmlCheck != null && !htmlCheck.isBlank())
			html = htmlCheck;
		else {
			html = this.getErrorpageAsHtml();
		}
		return html;
	}

}
