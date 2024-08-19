package Beans;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import Connection.NoConnectionException;


public class MainpageBean extends AbstractBean {

	public MainpageBean() throws NoConnectionException {
		this.createConnection();
		this.setAllArrayLists();
		this.setColor("");
		this.setPhysical_memory(0);
		this.setRam(0);
	}

	// Wenn Produkte die gezeigt werden sollen geändert werden - bitte entsprechende
	// Bilder im (jpeg-Format) in IMG/Mainpage

	public void setAllArrayLists() {
		smartphonesToShow = new ArrayList<>();
		notebooksToShow = new ArrayList<>();
		tabletsToShow = new ArrayList<>();
		otherProductsToShow = new ArrayList<>();

		smartphonesToShow.add("iPhone 14 Pro");
		smartphonesToShow.add("Z Fold5");
		smartphonesToShow.add("P60 Pro");

		notebooksToShow.add("MacBook Air M1");
		notebooksToShow.add("Book3");
		notebooksToShow.add("MateBook 16s");

		tabletsToShow.add("iPad Air");
		tabletsToShow.add("Tab A8");
		tabletsToShow.add("MatePad SE WiFi 4");
	}

	public String getBodyForMainpage() throws SQLException, UnsupportedEncodingException {
		String html = this.getBannerCarousel() + this.getAllProductContainer(this.smartphonesToShow, "Smartphone")
				+ this.getAllProductContainer(this.notebooksToShow, "Notebook")
				+ this.getAllProductContainer(this.tabletsToShow, "Tablet")
				+ this.getAllProductContainer(this.otherProductsToShow, "Other");
		return html;
	}

	public String getBannerCarousel() {
		String carousel = "<div id='carouselMain' class='carousel carousel-dark slide container'\r\n"
				+ "data-bs-ride='carousel'>\r\n" + "<div class='carousel-indicators'>\r\n"
				+ "	<button type='button' data-bs-target='#carouselMain'\r\n"
				+ "		data-bs-slide-to='0' class='active' aria-current='true'\r\n"
				+ "		aria-label='Slide 1'></button>\r\n"
				+ "	<button type='button' data-bs-target='#carouselMain'\r\n"
				+ "		data-bs-slide-to='1' aria-label='Slide 2'></button>\r\n"
				+ "	<button type='button' data-bs-target='#carouselMain'\r\n"
				+ "		data-bs-slide-to='2' aria-label='Slide 3'></button>\r\n" + "</div>\r\n"
				+ "<div class='carousel-inner'>\r\n" + "	<div class='carousel-item active text-center'>\r\n"
				+ "		<a href='../Appls/ProductAppl.jsp?product_name=iPhone%2014%20Pro&color=Schwarz&ram=6&physical_memory=256' ><img src='../../IMG/Banner/iPhone_Banner.png'\r\n"
				+ "			class='d-block mx-auto img-fluid' alt='Leider wurde kein Bild gefunden'></a>\r\n"
				+ "	</div>\r\n" + "	<div class='carousel-item text-center'>\r\n"
				// Ein Bild mit Absicht nicht einfügen um zu zeigen wie es dann aussehen würde
				+ "		<img src='#'\r\n"
				+ "			class='d-block mx-auto img-fluid' alt='Leider wurde kein Bild gefunden'>\r\n"
				+ "	</div>\r\n" + "	<div class='carousel-item text-center'>\r\n"
				+ "		<a href='../Views/SamsungView.jsp'><img src='../../IMG/Banner/Samsung_Banner.png'\r\n"
				+ "			class='d-block mx-auto img-fluid' alt='Leider wurde kein Bild gefunden'></a>\r\n"
				+ "	</div>\r\n" + "</div>\r\n" + "<button class='carousel-control-prev' type='button'\r\n"
				+ "	data-bs-target='#carouselMain' data-bs-slide='prev'>\r\n"
				+ "	<span class='carousel-control-prev-icon' aria-hidden='true'></span> <span\r\n"
				+ "		class='visually-hidden'>Previous</span>\r\n" + "</button>\r\n"
				+ "<button class='carousel-control-next' type='button'\r\n"
				+ "	data-bs-target='#carouselMain' data-bs-slide='next'>\r\n"
				+ "	<span class='carousel-control-next-icon' aria-hidden='true'></span> <span\r\n"
				+ "		class='visually-hidden'>Next</span>\r\n" + "</button>\r\n" + "</div>\r\n";
		return carousel;
	}
}