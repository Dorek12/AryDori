<%@page import="Beans.AbstractBean"%>
<%@page import="Beans.ShoppingBean"%>
<%@page import="Beans.SearchBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:useBean id="search" class="Beans.SearchBean" scope="session"></jsp:useBean>
	<jsp:useBean id="framework" class="Beans.FrameworkBean" scope="session"></jsp:useBean>
	<jsp:useBean id="account" class="Beans.AccountBean" scope="session"></jsp:useBean>
	<jsp:useBean id="product" class="Beans.ProductBean" scope="session"></jsp:useBean>
	<jsp:useBean id="shopping" class="Beans.ShoppingBean" scope="session"></jsp:useBean>
	<jsp:useBean id="rating" class="Beans.RatingBean" scope="session"></jsp:useBean>
	<jsp:useBean id="db" class="Beans.DatabaseBean" scope="session"></jsp:useBean>

	<%!public String check(String toCheck) {
		if (toCheck == null)
			toCheck = "";
		return toCheck;
	}%>
	<%
	try {
		String btnLog = check(request.getParameter("btnLog"));
		String btnSearch = check(request.getParameter("btnSearch"));
		String redirect = check(request.getHeader("Referer"));
		String searchedWords = check(request.getParameter("searchedWords"));
		String email = check(request.getParameter("email"));
		String password = check(request.getParameter("password"));		

		if (btnLog.equals("einloggen")) {
			account.setEmail(email.toLowerCase());
			account.setPassword(password);

			if (account.checkLogIn()) {
		// definiere Parameter für alle Klassen bei erfolgreichen login
		account.setLoggedIn(true);
		framework.setLoggedIn(true);
		product.setLoggedIn(true);
		shopping.setLoggedIn(true);
		rating.setLoggedIn(true);
		db.setLoggedIn(true);
		db.setEmployee(account.isEmployee());

		framework.setUser_id(account.getUser_id());
		product.setUser_id(account.getUser_id());
		shopping.setUser_id(account.getUser_id());
		rating.setUser_id(account.getUser_id());
		account.setUserInvoices();

		if (shopping.getGuestCart().size() > 0) {
			shopping.insertGuestCartIntoCartDb();
			shopping.getGuestCart().clear();
			framework.getGuestCart().clear();
		}

		response.sendRedirect(redirect);
			} else {
		account.setLoggedIn(false);
		framework.setLoggedIn(false);
		product.setLoggedIn(false);
		shopping.setLoggedIn(false);
		rating.setLoggedIn(false);
		account.createInfoMessage();
		response.sendRedirect("../Views/LoginView.jsp");
			}

		} else if (btnSearch.equals("suchen")) {
			search.setSearchedWord(searchedWords);
			response.sendRedirect("../Views/SearchView.jsp");

		}  else {
			response.sendRedirect("../Views/LoginView.jsp");
		}

	} catch (Exception e) {
		e.printStackTrace();
		response.sendRedirect("../Views/ErrorPageView.jsp");
	}
	%>

</body>
</html>