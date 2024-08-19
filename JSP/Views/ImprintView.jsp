<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Impressum</title>
<link rel="icon" href="../../IMG/Title/NAS_Logo.png" type="image/png">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
	crossorigin="anonymous"></script>
<script type="text/javascript" src="../../JS/Main.js"></script>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM"
	crossorigin="anonymous">
<link rel="stylesheet" type="text/css" href="../../CSS/Main.css">
</head>
<body>
	<jsp:useBean id="framework" class="Beans.FrameworkBean" scope="session"></jsp:useBean>
	<jsp:useBean id="search" class="Beans.SearchBean" scope="session"></jsp:useBean>
	<jsp:getProperty property="headerAsHtml" name="framework" />
	<div class="container">
		<div class="impressum-div" style="margin: auto; text-align: center;">
			<h2 style="margin-bottom: 50px;">Impressum</h2>

			<p>
				Verantwortlich für den Betrieb von <a
					href="../Views/MainpageView.jsp">www.nas.de</a> und von NAS
				Elektronik angebotene Produkte und Dienstleistungen:
			</p>
			<p>
				NAS Elektronik GmbH<br>Ernst-Boehe-Str. 4<br>67059
				Ludwigshafen am Rhein
			</p>

			<p>Geschäftsführer: Endrit Avdulli, Florian Neumann, Florian
				Strassner</p>
			<p>Eingetragen im Handelsregister des Amtsgerichts Ludwigshafen
				unter XXX</p>
			<p>USt ID: DE633000</p>
			<p>Retournieren Sie bitte Waren an diese Adresse.</p>

			<p style="margin-top: 20px;">Gerne können Sie sich mit uns in
				Verbindung setzen:</p>

			<p>
				Mo.−Sa.: 08:00−21:00 Uhr<br>So./Feiertag: 09:00−18:00 Uhr
			</p>

			<p>Telefon: 000/6333000 (Kosten zum Festnetztarif)</p>
			<p>
				E-Mail: <a href="mailto:nas.elektronik.kontakt@gmail.com">nas.elektronik.kontakt@gmail.com</a>
			</p>
		</div>
	</div>
	<jsp:getProperty property="footerAsHtml" name="framework" />
</body>
</html>