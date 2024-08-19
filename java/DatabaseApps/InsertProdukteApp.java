package DatabaseApps;

import java.sql.*;

import Connection.NoConnectionException;
import Connection.PostgreSQLAccess;

// Hier werden alle Produkte in die Datenbank eingelesen

public class InsertProdukteApp {
	Connection dbConn;

	public void createConnection() throws NoConnectionException {
		this.dbConn = new PostgreSQLAccess().getConnection();
	}

	public void insertAllProducts() throws SQLException {
		this.createConnection();
		this.insertApplePoducts();
		this.insertSamsungProducts();
		this.insertHuaweiProducts();
	}

	public void insertApplePoducts() throws SQLException {
		this.createConnection();
		this.insertiPhone14Pro();
		this.insertiPhone14ProMax();
		this.insertAirPodsPro();
		this.insertMacBookAir();
		this.insertMacBookPro();
		this.insertiPad();
		this.insertiPadAir();
		System.out.println("Apple Produkte wurden erfolgreich hinzugefügt");
	}

	public void insertSamsungProducts() throws SQLException {
		this.createConnection();
		this.insertGalaxyZFold5();
		this.insertGalaxyBook3();
		this.insertGalaxyTabA8();
		System.out.println("Samsung Produkte wurden erfolgreich hinzugefügt");
	}
	
	public void insertHuaweiProducts() throws SQLException {
		this.createConnection();
		this.insertHuaweiMate50Pro();
		this.insertHuaweiP60Pro();
		this.insertHuaweiMateBook16s();
		this.insertHuaweiMatePad();
		System.out.println("Huawei Produkte wurden erfolgreich hinzugefügt");
	}

	// Apple-Produkte

	public void insertiPhone14Pro() throws SQLException {
		int[] memory = { 128, 256 };
		String[] colors = { "Weiß", "Schwarz", "Lila" };
		String description = "Das iPhone 14 Pro Max beeindruckt mit einem kraftvollen A16 Bionic Chip, der mühelos anspruchsvolle Aufgaben bewältigt. \r\n"
				+ "Das 6,7-Zoll Super Retina XDR Display bietet atemberaubende visuelle Erfahrungen, während die Batterie mit kabellosem Laden "
				+ "bis zu 15 W und schnellem Aufladen punktet. Mit Face ID, LiDAR Scanner und Barometer ist das iPhone 14 Pro Max ein technologisches "
				+ "Meisterwerk.";
		for (int currentMemory : memory) {
			Double price = 0.00;
			for (String currentColor : colors) {
				if(currentMemory == 128) price = 699.99;
				else price = 899.99;
				if(currentColor.equals("Weiß")) price += 250;
				else if(currentColor.equals("Schwarz")) price += 400;
				else price += 100;
				String sql = "INSERT INTO products (brand, category, product_name, price, physical_memory, ram, color, stock, description, "
						+ "operating_system, general_keyword) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				System.out.println(sql);
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setString(1, "Apple"); // brand
				prep.setString(2, "Smartphone"); // category
				prep.setString(3, "iPhone 14 Pro"); // product_name
				prep.setDouble(4, price); // price				
				prep.setInt(5, currentMemory); // physical_memory
				prep.setInt(6, 6); // ram
				prep.setString(7, currentColor); // color
				prep.setInt(8, 8); // stock
				prep.setString(9, description); // description
				prep.setString(10, "iOS"); // operating_system
				prep.setString(11, "iPhone"); // general_keyword
				prep.executeUpdate();
			}
		}
	}

	public void insertiPhone14ProMax() throws SQLException {
		int[] memory = { 128, 256 };
		String[] colors = { "Weiß", "Schwarz", "Lila" };
		String description = "Das iPhone 14 überzeugt mit dem leistungsstarken A16 Bionic Chip, der für reibungslose Performance sorgt. \r\n"
				+ "Das 6,1-Zoll Super Retina XDR Display bietet beeindruckende Bildqualität. Kabelloses Laden bis zu 15 W und "
				+ "schnelles Aufladen sind praktische Funktionen. Face ID, LiDAR Scanner und Barometer ergänzen die Ausstattung "
				+ "des iPhone 14 für ein rundum beeindruckendes Smartphone-Erlebnis.";
		for (int currentMemory : memory) {
			for (String currentColor : colors) {
				String sql = "INSERT INTO products (brand, category, product_name, price, physical_memory, ram, color, stock, description, "
						+ "operating_system, general_keyword) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				System.out.println(sql);
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setString(1, "Apple"); // brand
				prep.setString(2, "Smartphone"); // category
				prep.setString(3, "iPhone 14 Pro Max"); // product_name
				if (currentMemory == 128)
					prep.setDouble(4, 1099.99); // price
				else
					prep.setDouble(4, 1499.99);
				prep.setInt(5, currentMemory); // physical_memory
				prep.setInt(6, 6); // ram
				prep.setString(7, currentColor); // color
				prep.setInt(8, 12); // stock
				prep.setString(9, description); // description
				prep.setString(10, "iOS"); // operating_system
				prep.setString(11, "iPhone"); // general_keyword
				prep.executeUpdate();
			}
		}
	}

	public void insertAirPodsPro() throws SQLException {
		String sql = "INSERT INTO products (brand, category, product_name, price, color, stock, description, general_keyword) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		String description = "Die geringe Verzerrung und die speziell entwickelten Treiber und Verstärker liefern brillante Höhen und Tiefe, "
				+ "satte Bässe in beeindruckender Klarheit. \r\n"
				+ "Bis zu 30 Std. Wiedergabe insgesamt mit eingeschalteter Aktiver Geräuschunterdrückung und dem MagSafe Ladecase – 6 Std. "
				+ "mehr als bei den AirPods Pro (1. Generation). \r\n"
				+ "Durch die In Ear Erkennung und das automatische Wechseln zwischen Geräten funktioniert alles nahtlos. \r\n"
				+ "Teile mit der Audiofreigabe sofort einen Song oder eine Serie zwischen zwei Paar AirPods auf deinem iPhone, iPad, "
				+ "iPod touch oder Apple TV. Finde mit „Wo ist?“ mit Genauer Suche den präzisen Ort des MagSafe Ladecase. "
				+ "Oder du nutzt „Wo ist?“ mit ungefährer Anzeige, um zu wissen, wo deine AirPods Pro sind. \r\n"
				+ "Die AirPods Pro kommen mit bis zu 2x besserer Aktiver Geräuschunterdrückung, Adaptiver Transparenz und "
				+ "personalisiertem 3D Audio mit dynamischem Head Tracking für immersiven Sound. Jetzt mit mehreren Silikontips "
				+ "(XS, S, M, L) und bis zu 6 Stunden Wiedergabe.";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setString(1, "Apple"); // brand
		prep.setString(2, "Kopfhörer"); // category
		prep.setString(3, "AirPods Pro 2. Generation"); // product_name
		prep.setDouble(4, 249.99); // price
		prep.setString(5, "Weiß"); // color
		prep.setInt(6, 23); // stock
		prep.setString(7, description); // description
		prep.setString(8, "airpods"); // general_keyword
		prep.executeUpdate();
	}

	public void insertMacBookPro() throws SQLException {
		int[] memory = { 256, 512 };
		int[] ram = { 8, 16 };
		for (int currentMemory : memory) {
			Double price = 0.00;
			for (int currentRam : ram) {
				if (currentMemory == 256)
					price = 1899.99;
				else
					price = 2199.99;
				if (currentRam == 8)
					price += 200;
				else
					price +=300;
				String sql = "INSERT INTO products (brand, category, product_name, price, physical_memory, ram, color, stock, description, "
						+ "operating_system, general_keyword) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setString(1, "Apple"); // brand
				prep.setString(2, "Notebook"); // category
				prep.setString(3, "MacBook Pro"); // product_name
				prep.setDouble(4, price); // price
				prep.setInt(5, currentMemory); // physical_memory
				prep.setInt(6, currentRam); // ram
				prep.setString(7, "Silber"); // color
				prep.setInt(8, 238); // stock
				prep.setString(9,
						"Das Apple MacBook Pro der neuesten Generation bietet erstklassige Leistung mit dem Apple M2 Chip und eine großzügige "
						        + currentMemory + " GB SSD für blitzschnelle Datenzugriffe. "
						        + "Das atemberaubende Retina Display mit hohem Kontrast und lebendigen Farben sorgt für ein herausragendes visuelles Erlebnis, "
						        + "während die lange Akkulaufzeit und das schlanke, leichte Design es zum perfekten Begleiter für unterwegs machen."); // description
				prep.setString(10, "macOS"); // operating_system
				prep.setString(11, "MacBook"); // general_keyword
				prep.executeUpdate();
			}
		}
	}

	public void insertMacBookAir() throws SQLException {
		int[] memory = { 256, 512 };
		int[] ram = { 8, 16 };
		for (int currentMemory : memory) {
			Double price = 0.00;
			for (int currentRam : ram) {
				if (currentMemory == 256)
					price = 899.00;
				else
					price = 999.00;
				if (currentRam == 8)
					price += 200;
				else
					price += 300;
				String sql = "INSERT INTO products (brand, category, product_name, price, physical_memory, ram, color, stock, description, "
						+ "operating_system, general_keyword) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setString(1, "Apple"); // brand
				prep.setString(2, "Notebook"); // category
				prep.setString(3, "MacBook Air M1"); // product_name
				prep.setDouble(4, price); // price
				prep.setInt(5, currentMemory); // physical_memory
				prep.setInt(6, currentRam); // ram
				prep.setString(7, "Silber"); // color
				prep.setInt(8, 10); // stock
				prep.setString(9,
				        "Das Apple MacBook Air mit dem revolutionären Apple M1 Chip bietet herausragende Leistung, "
				        + "eine " + currentMemory + " GB SSD und einen " + currentRam
				        + "-core CPU, ideal für unterwegs. "
				        + "Mit seinem schlanken und leichten Design, dem beeindruckenden Retina Display und der langen Akkulaufzeit "
				        + "ist es die perfekte Wahl für produktives Arbeiten und Entertainment unterwegs.");				
				prep.setString(10, "macOS"); // operating_system
				prep.setString(11, "MacBook"); // general_keyword
				prep.executeUpdate();
			}
		}
	}

	public void insertiPadAir() throws SQLException {
		String[] color = { "Blau", "Lila", "Pink" };
		String description = "Das Apple iPad Air hebt deine Erlebnisse auf ein neues Level, egal ob du liest, "
				+ "Videos anschaust oder kreativ arbeitest. \r\nMit seinem beeindruckenden 10,9-Zoll Liquid Retina Display, "
				+ "das fortschrittliche Technologien wie True Tone, einen großzügigen P3 Farbraum und eine Antireflex-Beschichtung "
				+ "bietet, tauchst du in eine Welt gestochen scharfer Farben und Details ein. Das iPad läuft auf iPadOS 15 und wird "
				+ "von einem leistungsstarken Okta-Core Prozessor angetrieben, der selbst anspruchsvollste Aufgaben mühelos "
				+ "bewältigt. \r\nMit beeindruckenden 8 GB Arbeitsspeicher bist du für Multitasking bestens gerüstet. Die Auslieferung "
				+ "erfolgt in Teilmengen ab dem 18.03.2022, je nach Verfügbarkeit. Erlebe das iPad Air und entdecke, wie einfach "
				+ "alles beeindruckender sein kann.";
		for (String currentColor : color) {
			String sql = "INSERT INTO products (brand, category, product_name, price, physical_memory, ram, color, "
					+ "stock, description, operating_system, general_keyword) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setString(1, "Apple"); // brand
			prep.setString(2, "Tablet"); // category
			prep.setString(3, "iPad Air"); // product_name
			prep.setDouble(4, 599.99); // price
			prep.setInt(5, 64); // physical_memory
			prep.setInt(6, 8); // ram
			prep.setString(7, currentColor); // color
			prep.setInt(8, 10); // stock
			prep.setString(9, description); // description
			prep.setString(10, "iPadOS"); // operating_system
			prep.setString(11, "iPad"); // general_keyword
			prep.executeUpdate();
		}
	}

	public void insertiPad() throws SQLException {
		String[] color = { "Grau", "Silber" };
		String description = "Das Apple iPad 10.2 Wi-Fi der 9ten Generation ist ein vielseitiges und leistungsstarkes "
				+ "Tablet. \r\nEs verfügt über ein beeindruckendes 10,2-Zoll Retina-Display, einen schnellen A13 Bionic Chip "
				+ "und läuft auf iPadOS 15. Mit 64 GB internem Speicher und einer Akkulaufzeit von bis zu 10 Stunden ist "
				+ "es ideal für Arbeit und Unterhaltung. \r\nDie Kamerafunktionen ermöglichen hochwertige Fotos und Videos "
				+ "und die Stereo-Lautsprecher bieten beeindruckenden Sound. Mit einem schlanken und leichten Design ist "
				+ "es einfach zu transportieren und verfügt über eine Vielzahl von Sensoren für eine verbesserte "
				+ "Benutzererfahrung. Dieses iPad ist die perfekte Lösung für alle, die Produktivität und Unterhaltung "
				+ "unterwegs wünschen.";
		for (String currentColor : color) {
			String sql = "INSERT INTO products (brand, category, product_name, price, physical_memory, ram, color, "
					+ "stock, description, operating_system, general_keyword) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setString(1, "Apple"); // brand
			prep.setString(2, "Tablet"); // category
			prep.setString(3, "iPad 10.2"); // product_name
			prep.setDouble(4, 679.99); // price
			prep.setInt(5, 64); // physical_memory
			prep.setInt(6, 3); // ram
			prep.setString(7, currentColor); // color
			prep.setInt(8, 10); // stock
			prep.setString(9, description); // description
			prep.setString(10, "iPadOS"); // operating_system
			prep.setString(11, "iPad"); // general_keyword
			prep.executeUpdate();
		}
	}

	// Samsung-Produkte

	public void insertGalaxyZFold5() throws SQLException {
		int[] memory = { 128, 256 };
		String[] colors = { "Schwarz", "Blau" };
		String description = "Das Samsung Galaxy Z Fold5 ist ein leistungsstarkes Smartphone mit einem beeindruckenden 7,6-Zoll Dynamic "
				+ "AMOLED Touchscreen Display. \r\nEs verfügt über eine 50 MP + 12 MP + 10 MP Kamera mit Autofokus und 10-fach Zoom oder Fotolicht. "
				+ "Das Betriebssystem ist Android 13 in Kombination mit One UI 5.1 und KNOX 3.9. "
				+ "Es bietet 256 GB internen Speicher und eine Gesprächszeit von bis zu 40 Stunden (4G) dank des Li-Ion-Akkus mit 4400 mAh. "
				+ "Das Galaxy Z Fold 5 unterstützt 5G, NFC, WLAN, Bluetooth und verfügt über ein robustes Gorilla-Glas-Display.";

		for (int currentMemory : memory) {
			for (String currentColor : colors) {
				String sql = "INSERT INTO products (brand, category, product_name, price, physical_memory, ram, color, stock, description, "
						+ "operating_system, general_keyword) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				System.out.println(sql);
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setString(1, "Samsung"); // brand
				prep.setString(2, "Smartphone"); // category
				prep.setString(3, "Z Fold5"); // product_name
				if(currentMemory == 128) prep.setDouble(4, 999.99); // price
				else prep.setDouble(4, 1299.99);
				prep.setInt(5, currentMemory); // physical_memory
				prep.setInt(6, 12); // ram
				prep.setString(7, currentColor); // color
				prep.setInt(8, 0); // stock, damit der Button ausgeblendet wird zur Präsentation
				prep.setString(9, description); // description
				prep.setString(10, "Android 13"); // operating_system
				prep.setString(11, "Galaxy"); // general_keyword
				prep.executeUpdate();
			}
		}
	}

	public void insertGalaxyTabA8() throws SQLException {
		String[] colors = { "Grau", "Silber" };
		String description = "Das Samsung Galaxy Tab A8 Wi-Fi ist ein beeindruckendes 10,5-Zoll Tablet mit einem schlanken Design. \r\n"
				+ "Es bietet großartige Unterhaltungsmöglichkeiten, darunter Surround-Sound aus vier Lautsprechern, "
				+ "Apps und Spiele für die ganze Familie sowie Zugang zu Samsung TV Plus für vielfältige kostenlose Inhalte. "
				+ "Mit einem Octa-Core-Prozessor, 3 GB RAM und 32 GB internem Speicher bietet es eine leistungsstarke "
				+ "Performance für deine Anforderungen.";

		for (String currentColor : colors) {
			String sql = "INSERT INTO products (brand, category, product_name, price, physical_memory, ram, color, stock, description, "
					+ "operating_system, general_keyword) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setString(1, "Samsung"); // brand
			prep.setString(2, "Tablet"); // category
			prep.setString(3, "Tab A8"); // product_name
			prep.setDouble(4, 159.99); // price
			prep.setInt(5, 32); // physical_memory
			prep.setInt(6, 3); // ram
			prep.setString(7, currentColor); // color
			prep.setInt(8, 112); // stock
			prep.setString(9, description); // description
			prep.setString(10, "Android"); // operating_system
			prep.setString(11, "Galaxy"); // general_keyword
			prep.executeUpdate();
		}
	}

	public void insertGalaxyBook3() throws SQLException {
		String description = "Das Galaxy Book3 von Samsung ist ein 15,6-Zoll Notebook mit Full HD IPS-Display. \r\n"
                + "Es wird von einem Intel® Core™ i5-1335U Prozessor mit bis zu 3,4 GHz, 8 GB LPDDR4x RAM und einer 512 GB SSD angetrieben. "
                + "Vorinstalliert ist Windows® 11 Home.\n"
                + "Mit USB- und HDMI-Schnittstellen für Peripheriegeräte, Frontkamera für Videoanrufe, beleuchteter Tastatur und einem "
                + "leichten Gewicht von 1,58 kg ist es ideal für unterwegs. Das Full HD IPS-Display bietet brillante Farben und "
                + "Detailreichtum. Die 512 GB SSD speichert viele Dateien, und die Intel Iris Xe Graphics bieten gute Grafikleistung. "
                + "Windows® 11 Home ermöglicht sofortiges Arbeiten. Mit 4 USB-Anschlüssen, HDMI, Wi-Fi, Bluetooth 5.1 und einem "
                + "Micro SD Card Slot bietet es vielfältige Konnektivitätsoptionen. Die 720p Frontseitenkamera ermöglicht Fotos "
                + "und Videos unterwegs.";
		String sql = "INSERT INTO products (brand, category, product_name, price, physical_memory, ram, color, stock, description, "
				+ "operating_system, general_keyword) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setString(1, "Samsung"); // brand
		prep.setString(2, "Notebook"); // category
		prep.setString(3, "Book3"); // product_name
		prep.setDouble(4, 999.99); // price
		prep.setInt(5, 256); // physical_memory 
		prep.setInt(6, 16); // ram 
		prep.setString(7, "Grau"); // color 
		prep.setInt(8, 71); // stock
		prep.setString(9, description); // description
		prep.setString(10, "Windows 11"); // operating_system
		prep.setString(11, "Galaxy"); // general_keyword
		prep.executeUpdate();
	}

	// Huawei-Produkte

	public void insertHuaweiMate50Pro() throws SQLException {
		int[] memory = { 256, 512 };
		String[] color = { "Schwarz", "Silber" };
		String description = "Das Huawei Mate 50 Pro ist ein hochwertiges Smartphone mit beeindruckender Leistung und vielen Funktionen. "
		        + "Es verfügt über einen 17,12 cm großen Bildschirm (2616 x 1212 Pixel), einen Qualcomm Snapdragon 8+ Gen 1 Octa-Core-Prozessor, 8 GB RAM "
		        + "und bietet wahlweise 256 GB oder 512 GB internen Speicher, der erweiterbar ist. Das Smartphone ist mit Huawei's Betriebssystem und "
		        + "AppGallery vorinstalliert. Seine Kameras bieten 50 MP (Hauptkamera) und 13 MP (Frontkamera). Es unterstützt 4G, 5G, GPS, Dual Nano-SIM-Karten, "
		        + "Bluetooth, WLAN und verfügt über einen USB Typ-C-Anschluss. Mit einer Akkukapazität von 4700 mAh, verschiedenen Sensoren und kompakten Abmessungen "
		        + "ist es ideal für den täglichen Gebrauch.";
		for (int currentMemory : memory) {
			for (String currentColor : color) {
				String sql = "INSERT INTO products (brand, category, product_name, price, physical_memory, ram, color, stock, description, "
						+ "operating_system, general_keyword) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				System.out.println(sql);
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setString(1, "Huawei"); // brand
				prep.setString(2, "Smartphone"); // category
				prep.setString(3, "Mate 50 Pro"); // product_name
				if (currentMemory == 256)
					prep.setDouble(4, 799.00); // price
				else
					prep.setDouble(4, 849.99);
				prep.setInt(5, currentMemory); // physical_memory
				prep.setInt(6, 8); // ram
				prep.setString(7, currentColor); // color
				prep.setInt(8, 200); // stock
				prep.setString(9, description); // description
				prep.setString(10, "Huawei OS"); // operating_system
				prep.setString(11, "Mate"); // general_keyword
				prep.executeUpdate();
			}
		}
	}

	public void insertHuaweiP60Pro() throws SQLException {
		String[] colors = { "Schwarz", "Weiß" };
		int[] memory = { 128, 256, 512 };
		for (String currentColor : colors) {
			for (int currentMemory : memory) {
				String description = "Das Huawei P60 Pro ist ein leistungsstarkes Smartphone mit einem 16,9 cm Full HD-Bildschirm (2700 x 1220 Pixel). \r\n"
						+ "Es wird von einem Qualcomm Snapdragon 8+ Gen 1 Octa-Core-Prozessor mit 3,2 GHz, 8 GB RAM und " + currentMemory + " GB internem "
								+ "Speicher angetrieben. Es läuft auf EMUI 13.1 und bietet Zugang zur Huawei AppGallery. Die Kameras haben 48 MP (Hauptkamera), "
								+ "13 MP (zweite Hauptkamera) und 13 MP (Frontkamera). Das Huawei P60 Pro unterstützt 4G (LTE), GPS, Glonass, Dual Nano-SIM-Karten, "
								+ "Bluetooth und WLAN. Es verfügt über einen Fingerabdrucksensor im Power-Button, eine 4815 mAh Akkukapazität mit Schnellladefunktion "
								+ "und kompakte Abmessungen von 9 cm x 14 cm x 23 cm bei einem Gewicht von 0,6 g.";
				String sql = "INSERT INTO products (brand, category, product_name, price, physical_memory, ram, color, stock, description, "
						+ "operating_system, general_keyword) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				System.out.println(sql);
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setString(1, "Huawei"); // brand
				prep.setString(2, "Smartphone"); // category
				prep.setString(3, "P60 Pro"); // product_name
				if (currentMemory == 128)
					prep.setDouble(4, 899.00); // price
				else if (currentMemory == 256)
					prep.setDouble(4, 999.00);
				else
					prep.setDouble(4, 1150.00);
				prep.setInt(5, currentMemory); // physical_memory
				prep.setInt(6, 8); // ram
				prep.setString(7, currentColor); // color
				prep.setInt(8, 200); // stock
				prep.setString(9, description); // description
				prep.setString(10, "EMUI 13.1"); // operating_system
				prep.setString(11, "P60"); // general_keyword
				prep.executeUpdate();
			}
		}
	}
	
	public void insertHuaweiMateBook16s() throws SQLException {
	    String description = "Das Huawei MateBook 16s Notebook ist ein leistungsstarkes Laptop mit beeindruckenden technischen Spezifikationen. \r\n"
	            + "Es verfügt über einen 40,64 cm (16 Zoll) großen Bildschirm mit einer Auflösung von 2520 x 1680 Pixeln und unterstützt 2.5K-Auflösung. "
	            + "Der Bildschirm verwendet die IPS-Display-Technologie und ist ausklappbar für flexible Betrachtungswinkel von 178 Grad. "
	            + "Das statische Kontrastverhältnis beträgt 1500:1, und es kann über 1 Milliarde Farben mit einer maximalen Farbtiefe von 10 Bit darstellen. "
	            + "Die Bildschirmhelligkeit beträgt 300 cd/m², und die Pixeldichte liegt bei 189 ppi.";

	    String sql = "INSERT INTO products (brand, category, product_name, price, physical_memory, ram, color, stock, description, "
	            + "operating_system, general_keyword) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    System.out.println(sql);
	    PreparedStatement prep = this.dbConn.prepareStatement(sql);
	    prep.setString(1, "Huawei"); // brand
	    prep.setString(2, "Notebook"); // category
	    prep.setString(3, "MateBook 16s"); // product_name
	    prep.setDouble(4, 1899.00); // price 
	    prep.setInt(5, 1000); // physical_memory 
	    prep.setInt(6, 16); // ram 
	    prep.setString(7,  "Silber"); // color
	    prep.setInt(8, 152); // stock 
	    prep.setString(9, description); // description
	    prep.setString(10, "Windows 11 Home (64 Bit)"); // operating_system
	    prep.setString(11, "MateBook"); // general_keyword
	    prep.executeUpdate();
	}
	
	public void insertHuaweiMatePad() throws SQLException {
	    String description = "Das Huawei MatePad SE WiFi 4 Tablet ist ein leistungsstarkes Tablet mit beeindruckenden technischen Spezifikationen. "
	            + "Es verfügt über einen 26,3 cm (10,4 Zoll) großen Bildschirm mit einer Auflösung von 2000 x 1200 Pixeln und verwendet IPS-Technologie. "
	            + "Das Tablet wird von einem Octa-Core Qualcomm Snapdragon 680 Prozessor mit einer Geschwindigkeit von 2,4 GHz angetrieben. "
	            + "Es verfügt über 4 GB RAM und internen Speicher von 128 GB, der über eine microSD-Karte auf bis zu 1000 GB erweitert werden kann. "
	            + "Das Betriebssystem ist HarmonyOS. Das Tablet ist in der Farbe Schwarz erhältlich.";
	    int[] memory = { 128, 256, 512 };
			for (int currentMemory : memory) {
	    String sql = "INSERT INTO products (brand, category, product_name, price, physical_memory, ram, color, stock, description, "
	            + "operating_system, general_keyword) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    System.out.println(sql);
	    PreparedStatement prep = this.dbConn.prepareStatement(sql);
	    prep.setString(1, "Huawei"); // brand
	    prep.setString(2, "Tablet"); // category
	    prep.setString(3, "MatePad SE WiFi 4"); // product_name
	    if(currentMemory == 128) prep.setDouble(4, 249.00); // price 
	    else if(currentMemory == 256) prep.setDouble(4, 300);
	    	 else prep.setDouble(4, 349.99); 
	    prep.setInt(5, currentMemory); // physical_memory 
	    prep.setInt(6, 4); // ram 
	    prep.setString(7, "Schwarz"); // color
	    prep.setInt(8, 83); // stock 
	    prep.setString(9, description); // description
	    prep.setString(10, "HarmonyOS"); // operating_system
	    prep.setString(11, "MatePad"); // general_keyword
	    prep.executeUpdate();
	    
			}
	}


	
	
	
	

}
