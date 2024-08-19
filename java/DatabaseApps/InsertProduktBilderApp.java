package DatabaseApps;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.LinkedHashMap;

import Connection.NoConnectionException;
import Connection.PostgreSQLAccess;

// Hier werden alle Bilder in die Datenbank eingelesen

public class InsertProduktBilderApp {

	LinkedHashMap<String, Integer> allPaths; // normale HashMap hat die Reihenfolge der Bilder (Main,1,2, etc.)
											 // verändert
	Connection dbConn;

	public void createConnection() throws NoConnectionException {
		this.dbConn = new PostgreSQLAccess().getConnection();
	}

	public void insertBilder() throws SQLException, IOException {
		this.readAllproductsFromDb();
		String sql = "insert into bilder (product_id, image_byte) VALUES (?, ?)";
		System.out.println(sql);
		if (this.allPaths != null && this.allPaths.size() != 0) {
			for (String path : this.allPaths.keySet()) {
				byte[] imageByte = this.getBildBytea(path);
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setInt(1, this.allPaths.get(path));
				prep.setBytes(2, imageByte);
				prep.executeUpdate();
				System.out.println(
						"Bild für " + this.allPaths.get(path) + " wurde erfolgreich in die Datenbank eingefügt.");
			}
		}
	}

	public void readAllproductsFromDb() throws SQLException {
		this.createConnection();
		this.allPaths = new LinkedHashMap<String, Integer>();
		String sql = "SELECT marke, produkt_name, farbe, product_id FROM produkte";
		this.createConnection();
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next())
			this.fillAllPaths(dbRes.getString("marke"), dbRes.getString("produkt_name"), dbRes.getString("farbe"),
					dbRes.getInt("product_id"));
	}

	public void fillAllPaths(String marke, String produkt_name, String farbe, int product_id) {
		int quantity = this.checkBilderQuantity(marke, produkt_name, farbe);
		int index = 1;
		// Keine Bilder zu AirPods um Image Not Found Beispiel zu zeigen
		if (!produkt_name.equalsIgnoreCase("AirPods Pro 2. generation")) {
			if (quantity != 0) {
				String workingDirectoryPath = System.getProperty("user.dir");
				String relativePath = workingDirectoryPath + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator
						+ "IMG" + File.separator + "Products" + File.separator + marke + File.separator + produkt_name
						+ "_" + farbe;
				allPaths.put(relativePath + "_Main.jpeg", product_id);
				while (quantity > 0) {
					String imagePath = relativePath + "_" + index + ".jpeg";
					allPaths.put(imagePath, product_id);
					quantity--;
					index++;
				}
			} else
				this.allPaths = null;
		}
	}
	
	// checkt dynamisch wie viele Bilder es von einem Produkt gibt (abgesehen vom Main Bild)

	public int checkBilderQuantity(String marke, String produkt_name, String farbe) {
		int index = 0;
		// dürfen nie mehr als 10 Bilder sein + Main Bild
		for (int i = 1; i < 10; i++) {
			String currentPath = System.getProperty("user.dir");
			String relativePath = "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "IMG"
					+ File.separator + "Products" + File.separator + marke + File.separator + produkt_name + "_" + farbe
					+ "_" + i + ".jpeg";
			String imageFolder = currentPath + File.separator + relativePath;
			File image = new File(imageFolder);
			if (image.exists())
				index++;
			else
				break;
		}
		return index;
	}

	public byte[] getBildBytea(String path) throws IOException {
		Path imagePath = Path.of(path);
		byte[] imageBytes = Files.readAllBytes(imagePath);
		return imageBytes;
	}

}