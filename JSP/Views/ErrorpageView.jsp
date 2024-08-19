<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ups! Etwas ist schiefgelaufen</title>
<link rel="stylesheet" type="text/css" href="../../CSS/Main.css">
</head>
<body>
	<jsp:useBean id="framework" class="Beans.FrameworkBean" scope="session"></jsp:useBean>
	<div id="error-container">
		<jsp:getProperty property="errorpageAsHtml" name="framework" />
	</div>
</body>
</html>

