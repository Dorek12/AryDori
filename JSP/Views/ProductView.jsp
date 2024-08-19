<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:useBean id="product" class="Beans.ProductBean" scope="session"></jsp:useBean>
<title><jsp:getProperty property="titleAsHtml" name="product" /></title>
<jsp:getProperty property="linkImgAsHtml" name="product" />
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
	crossorigin="anonymous"></script>
<script type="text/javascript" src="../../JS/Main.js"></script>
<script type="text/javascript" src="../../JS/CopyURL.js"></script>
<!-- Die Sorgen dafür, dass das Carousel funktioniert -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM"
	crossorigin="anonymous">
<link rel="stylesheet" type="text/css" href="../../CSS/Main.css">
<link rel="stylesheet" type="text/css" href="../../CSS/Rating.css">
<link rel="stylesheet" type="text/css" href="../../CSS/CopyPopup.css">
<link rel='stylesheet'
	href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css'>

</head>
<body>
	<jsp:useBean id="framework" class="Beans.FrameworkBean" scope="session"></jsp:useBean>
	<jsp:useBean id="search" class="Beans.SearchBean" scope="session"></jsp:useBean>

	<jsp:include page="../Appls/LocalhostAppl.jsp" flush="true" />

	<jsp:getProperty property="headerAsHtml" name="framework" />
	<jsp:getProperty property="productViewAsHtml" name="product" />
	<jsp:getProperty property="footerAsHtml" name="framework" />

</body>
</html>