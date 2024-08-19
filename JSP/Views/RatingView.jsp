<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bewertungen</title>
<link rel="icon" href="../../IMG/Title/NAS_Logo.png" type="image/png">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
	crossorigin="anonymous"></script>
<script type="text/javascript" src="../../JS/Main.js"></script>

<!--  für die Sterne zuständig -->
<script type="text/javascript"
	src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">

<link rel="stylesheet" type="text/css" href="../../CSS/Main.css">
<link rel="stylesheet" type="text/css" href="../../CSS/Rating.css">



</head>
<body>

	<jsp:useBean id="framework" class="Beans.FrameworkBean" scope="session"></jsp:useBean>
	<jsp:useBean id="search" class="Beans.SearchBean" scope="session"></jsp:useBean>
	<jsp:useBean id="rating" class="Beans.RatingBean" scope="session"></jsp:useBean>

	<jsp:getProperty property="headerAsHtml" name="framework" />
	<jsp:getProperty property="ratingViewAsHtml" name="rating" />
	<jsp:getProperty property="footerAsHtml" name="framework" />
</body>
<script src="../../JS/Rating.js"></script>
</html>