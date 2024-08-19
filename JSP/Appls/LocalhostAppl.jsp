<%@ page language="java" contentType="text/html; UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<jsp:useBean id="product" class="Beans.ProductBean" scope="session"></jsp:useBean>
<jsp:useBean id="recover" class="Beans.RecoverPasswordBean"	scope="session"></jsp:useBean>
<%
String localhost = product.getUserHost(request);
product.setLocalhost(localhost);
recover.setLocalhost(localhost);
%>
</body>
</html>