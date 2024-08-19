package Beans;

import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;


public class SearchBean extends AbstractBean {

	HashMap<String, String> keyWordsFromDB;
	HashMap<String, String> searchedWordsHM;
	String[] allSearchedWordsArray;
	String[] productsToShow;
	String searchedWord;

	public SearchBean() throws SQLException {
		super();
		this.createConnection();
		this.fillKeyWordsHashMap();
		this.fillAllSearchedWordsArray();
		this.setSearchedWord("");
	}

	/*
	 * fuellen mit den Werten aus der DB arbeite in der Klasse mit ILIKE und
	 * toLowerCase da man nicht weiß ob mit großen oder kleinen Buchstaben nach
	 * Produkten gesucht wird repäsentiert ab hier die DB
	 */

	public void fillKeyWordsHashMap() throws SQLException {
		this.keyWordsFromDB = new HashMap<String, String>();
		String sql = "SELECT brand, category, product_name, general_keyword FROM products";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next()) {
			this.keyWordsFromDB.put(dbRes.getString("brand").toLowerCase(), dbRes.getMetaData().getColumnName(1));
			this.keyWordsFromDB.put(dbRes.getString("category").toLowerCase(), dbRes.getMetaData().getColumnName(2));
			this.keyWordsFromDB.put(dbRes.getString("product_name").toLowerCase(),
					dbRes.getMetaData().getColumnName(3));
//			this.keyWordsFromDB.put(Integer.toString(dbRes.getInt(6)), dbRes.getMetaData().getColumnName(6));
//			this.keyWordsFromDB.put(Integer.toString(dbRes.getInt(7)), dbRes.getMetaData().getColumnName(7));
			this.keyWordsFromDB.put(dbRes.getString("general_keyword").toLowerCase(),
					dbRes.getMetaData().getColumnName(4));

		}
	}
	
	// Title dynamisch erstellen 
	
	public String getTitleAsHtml() {
		if(this.getSearchedWord() == null || this.getSearchedWord().isBlank()) {
			return "Sie haben keinen Suchbegriff eingegeben.";
		} else {
			return "Suchergebnisse für: " + this.getSearchedWord();
		}
	}

	// Methoden um die Antwort auszuformulieren

	public String getSearchResultsAnswer() throws SQLException {
		this.fillSearchedWordHashMap();
		String html = "";
		if (this.searchedWordsHM != null) {
			if (!this.checkIfAllSearchedWordsCorrect()) {
				html = "<div class='alert alert-warning' role='alert'>";
				html += "Leider haben wir keine passenden Ergebnisse zu ";
				html += "<strong>" + this.returnAllSearchedWordsHMKeys() + "</strong> gefunden. ";
				html += "Hier sind einige Vorschläge, die Sie interessieren könnten.";
				html += "</div>";
			} else if (this.checkIfAllSearchedWordsCorrect() || !this.getSQLCommand().contains(" OR ")) {
				html = "<div class='alert alert-success' role='alert'>";
				html += "Hier sind passende Ergebnisse zu ";
				html += "<strong>" + this.returnAllSearchedWordsHMKeys() + "</strong>.";
				html += "</div>";
			}
		} else {
			html = "<div class='alert alert-info' role='alert'>";
			html += "Sie haben keinen Suchbegriff eingegeben. ";
			html += "Hier sind einige Vorschläge, die Sie interessieren könnten.";
			html += "</div>";
		}

		return html;
	}

	// fülle Array mit den gesuchten Wort/Wörtern, erst mit Array wegen dem
	// split()-Befehl

	public void fillAllSearchedWordsArray() {
		if (this.getSearchedWord() != null && !this.getSearchedWord().isBlank()) {
			if (!this.checkWordsAreSimilar(this.getSearchedWord().toLowerCase()).isBlank()) {
				this.allSearchedWordsArray = new String[1];
				this.allSearchedWordsArray[0] = this.getSearchedWord();
			} else
				this.allSearchedWordsArray = this.getSearchedWord().toLowerCase().split(" ");
		}

		else
			this.allSearchedWordsArray = null;

	}

	// allSearchedWordsArray in HashMap füllen um doppelte Wörter zu filtern, setze
	// null wenn Wort in DB nicht gefunden wurde

	public void fillSearchedWordHashMap() {
		this.fillAllSearchedWordsArray();
		this.searchedWordsHM = new HashMap<String, String>();
		if (this.allSearchedWordsArray != null) {
			for (String checkWord : this.allSearchedWordsArray) {
				if (!this.checkSingleWords(checkWord)) {
					if (!this.checkSingleWords(this.checkWordsAreSimilar(checkWord))) {
						this.searchedWordsHM.put(checkWord, null);
					}
				}
			}
		} else
			this.searchedWordsHM = null;
	}

	// gibt false zurück wenn ein Wert null ist (nicht in der DB existiert) um die
	// Antwort anpassen zu können

	public boolean checkIfAllSearchedWordsCorrect() {
		boolean allCorrect = true;
		for (String currentWord : this.searchedWordsHM.keySet()) {
			if (this.searchedWordsHM.get(currentWord) == null) {
				allCorrect = false;
				break;
			}
		}
		return allCorrect;
	}

	public String returnAllSearchedWordsHMKeys() {
		String allWords = "";
		for (String currentWord : this.searchedWordsHM.keySet())
			allWords += " " + currentWord;
		return allWords;
	}

	// Methoden die für die Erstellung des SQL-Befehls benutzt werden

	public String getSQLCommand() throws SQLException {
		String sql = "SELECT * FROM products";
		if (!this.getSQLCommandIfWordsMatch().isBlank())
			sql = this.getSQLCommandIfWordsMatch();
		else if (this.searchedWordsHM != null)
			sql += " WHERE" + this.returnAllWHEREConditions();
		System.out.println(sql);
		return sql;
	}

	// Wenn die gesuchten Werte zusammenpassen (z.B. apple iphone) gibt es
	// SQL-Befehl mit AND zurück

	public String getSQLCommandIfWordsMatch() throws SQLException {
		this.removeAllNotExistingWords();
		String sql = "";
		if (this.searchedWordsHM != null) {
			int i = this.searchedWordsHM.size();
			sql = "SELECT * FROM products WHERE";
			for (String currentWord : this.searchedWordsHM.keySet()) {
				sql += " " + this.searchedWordsHM.get(currentWord) + " ILIKE " + "'" + currentWord + "'";
				if (i > 1) {
					sql += " AND";
					i--;
				}
			}
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			ResultSet dbRes = prep.executeQuery();
			if (!dbRes.next())
				sql = "";
			System.out.println(sql);
		}
		return sql;
	}

	public void removeAllNotExistingWords() {
		if (this.searchedWordsHM != null) {
			Vector<String> allNotExistingWords = new Vector<String>();
			for (String currentWord : this.searchedWordsHM.keySet()) {
				if (this.searchedWordsHM.get(currentWord) == null)
					allNotExistingWords.add(currentWord);
			}
			for (String removeWord : allNotExistingWords)
				this.searchedWordsHM.remove(removeWord);
			if (this.searchedWordsHM.isEmpty())
				this.searchedWordsHM = null;
		}

	}

	// SQL-Befehl mit OR weil Werte nicht zusammenpassen

	public String returnAllWHEREConditions() {
		String conditions = "";
		int i = this.searchedWordsHM.size();
		if (this.searchedWordsHM != null) {
			for (String currentWord : this.searchedWordsHM.keySet()) {
				if (this.searchedWordsHM.get(currentWord) != null) {
					conditions += " " + this.searchedWordsHM.get(currentWord) + " ILIKE " + "'" + currentWord + "'";
					if (i > 1) {
						conditions += " OR";
						i--;
					}
				}

			}
		}
		return conditions;
	}

	public void readProductsFromDB() throws SQLException {
		String sqlCommand = this.getSQLCommand();
		PreparedStatement prep = this.dbConn.prepareStatement(sqlCommand);
		ResultSet dbRes = prep.executeQuery();
		int index = 0;
		while (dbRes.next())
			index++;
		this.productsToShow = new String[index];
		index = 0;
		PreparedStatement prep1 = this.dbConn.prepareStatement(sqlCommand);
		ResultSet dbRes1 = prep1.executeQuery();
		while (dbRes1.next()) {
			this.productsToShow[index] = dbRes1.getString("product_name");
			index++;
		}
	}

	/*
	 * gibt eine Übersicht der Produkte zurück greift auf Mainpage zu um Code
	 * Redundanz zu vermeiden
	 */

	public String getSearchResultContainer() throws SQLException, UnsupportedEncodingException {
		HashMap<String, String> allProducts = new HashMap<String, String>();
		String sql = this.getSQLCommand();
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		ResultSet dbRes = prep.executeQuery();
		while (dbRes.next()) {
			allProducts.put(dbRes.getString("product_name"), dbRes.getString("category"));
		}

		this.fillArrayLists(allProducts);
		String html = this.getAllProductContainer(this.smartphonesToShow, "Smartphone")
				+ this.getAllProductContainer(this.notebooksToShow, "Notebook")
				+ this.getAllProductContainer(this.tabletsToShow, "Tablet")
				+ this.getAllProductContainer(this.otherProductsToShow, "Other");

		return html;
	}

	// prüft ob das Wort in der DB existiert

	public boolean checkSingleWords(String checkWord) {
		for (String currentWord : this.keyWordsFromDB.keySet()) {
			if (checkWord.equalsIgnoreCase(currentWord)) {
				this.searchedWordsHM.put(currentWord, this.keyWordsFromDB.get(currentWord));
				return true;
			}
		}
		return false;
	}

	// Gibt das Wort zurück dass am ähnlichsten zum gesuchten Word ist

	public String checkWordsAreSimilar(String checkThisWord) {
		String mostSimilarWord = "";
		int minDistance = Integer.MAX_VALUE;

		for (String currentWord : this.keyWordsFromDB.keySet()) {
			int distance = calculateLevenshteinDistance(currentWord, checkThisWord);
			if (distance <= 3 && distance < minDistance) {
				mostSimilarWord = currentWord;
				minDistance = distance;
			}
		}

		return mostSimilarWord;
	}

	/*
	 * Levenshtein-Distance berechnet wie viele Zeichen ausgetauscht werden müssen
	 * zwischen Werten Grenze bei maximal 3 Zeichen gesetzt Quelle:
	 * https://dirask.com/posts/Java-calculate-Levenshtein-distance-between-strings-
	 * p2G2Aj
	 */

	public static int findMin(int a, int b, int c) {
		int min = Math.min(a, b);
		return Math.min(min, c);
	}

	public int calculateLevenshteinDistance(String current, String checkThisWord) {
		int aLimit = current.length() + 1;
		int bLimit = checkThisWord.length() + 1;
		int[][] distance = new int[aLimit][bLimit];
		for (int i = 0; i < aLimit; ++i) {
			distance[i][0] = i;
		}
		for (int j = 0; j < bLimit; ++j) {
			distance[0][j] = j;
		}
		for (int i = 1; i < aLimit; ++i) {
			for (int j = 1; j < bLimit; ++j) {
				char aChar = current.charAt(i - 1);
				char bChar = checkThisWord.charAt(j - 1);
				distance[i][j] = findMin(distance[i - 1][j] + 1, distance[i][j - 1] + 1,
						distance[i - 1][j - 1] + (Objects.equals(aChar, bChar) ? 0 : 1));
			}
		}
		return distance[current.length()][checkThisWord.length()];
	}

	// Getter und Setter - Methoden

	public String getSearchedWord() {
		return searchedWord;
	}

	public void setSearchedWord(String searchedWord) {
		this.searchedWord = searchedWord;
	}
}
