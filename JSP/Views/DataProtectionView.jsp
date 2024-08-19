<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Datenschutz</title>
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
	<div class="datenschutz-div"
		style="width: 50%; margin: auto; text-align: center;">
		<h1 style="margin-bottom: 5px; margin-top: 20px;">Datenschutzhinweise</h1>
		<p style="margin-top: 20px;">Unser Bestreben ist es, dass Sie sich
			auf unserer Webseite wohlfühlen. Der Schutz Ihrer Privatsphäre und
			Ihr Persönlichkeitsrecht sind deshalb für uns ein wichtiges Anliegen.
			Deshalb möchten wir Sie bitten, die nachfolgende Zusammenfassung über
			die Funktionsweise unserer Webseite aufmerksam zu lesen. Sie können
			auf eine transparente und faire Datenverarbeitung vertrauen, und wir
			bemühen uns um einen sorgsamen und verantwortungsvollen Umgang mit
			Ihren Daten.</p>

		<h2 style="margin-bottom: 5px; margin-top: 20px;">Datenschutzbeauftragter</h2>
		<p style="margin-top: 20px;">Unser Datenschutzbeauftragter ist Max
			Mustermann. Sie können ihn wie folgt kontaktieren:</p>

		<p>
			Name: Max Mustermann<br> Email: <a
				href="mailto:Max_Mustermann@nas.de">Max_Mustermann@nas.de</a><br>
			Telefon: XXX 000
		</p>
	</div>
	<jsp:getProperty property="footerAsHtml" name="framework" />
</body>
</html>