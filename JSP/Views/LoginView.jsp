<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Anmelden</title>
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
	<jsp:useBean id="account" class="Beans.AccountBean" scope="session"></jsp:useBean>

	<jsp:getProperty property="headerAsHtml" name="framework" />
	<jsp:getProperty property="loginAsHtml" name="account" />
	<jsp:getProperty property="footerAsHtml" name="framework" />
</body>
<script type="text/javascript" src="../../JS/Login.js"></script>
</html>