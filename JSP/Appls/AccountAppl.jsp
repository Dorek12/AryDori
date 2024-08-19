<%@page import="Beans.AbstractBean"%>
<%@page import="Beans.AccountBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:useBean id="account" class="Beans.AccountBean" scope="session"></jsp:useBean>
	<jsp:useBean id="shopping" class="Beans.ShoppingBean" scope="session"></jsp:useBean>
	<jsp:useBean id="product" class="Beans.ProductBean" scope="session"></jsp:useBean>
	<jsp:useBean id="framework" class="Beans.FrameworkBean" scope="session"></jsp:useBean>
	<jsp:useBean id="recover" class="Beans.RecoverPasswordBean"
		scope="session"></jsp:useBean>
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
		String btnReg = check(request.getParameter("btnReg"));
		String btnInvoiceMail = check(request.getParameter("btnInvoiceMail"));
		String btnChangePassword = check(request.getParameter("btnChangePassword"));
		String btnApplyChange = check(request.getParameter("btnApplyChange"));
		String btnEditData = check(request.getParameter("btnEditData"));
		String btnEditPassword = check(request.getParameter("btnEditPassword"));
		String btnChangeAddress = check(request.getParameter("btnChangeAddress"));
		String btnPortal = check(request.getParameter("btnPortal"));
		String btnLogout = check(request.getParameter("btnLogout"));
		String firstName = check(request.getParameter("first_name"));
		String lastName = check(request.getParameter("last_name"));
		String birthDay = check(request.getParameter("birth_day"));
		String birthMonth = check(request.getParameter("birth_month"));
		String birthYear = check(request.getParameter("birth_year"));
		String email = check(request.getParameter("email"));
		String password = check(request.getParameter("password"));
		String user_id = check(request.getParameter("user_id"));
		String number = check(request.getParameter("number"));
		String CodeString = check(request.getParameter("Code"));
		String changeFirstName = check(request.getParameter("changeFirstName"));
		String changeLastName = check(request.getParameter("changeLastName"));
		String changeEmail = check(request.getParameter("changeEmail"));
		String changePhoneNumber = check(request.getParameter("changePhoneNumber"));
		String oldPassword = check(request.getParameter("currentPassword"));
		String newPassword = check(request.getParameter("newPassword"));
		String streetAddress = check(request.getParameter("streetAdd"));
		String streetNumberAddress = check(request.getParameter("streetNumberAdd"));
		String cityAddress = check(request.getParameter("cityAdd"));
		String zipCodeAddress = check(request.getParameter("zipCodeAdd"));
		String redirect = check(request.getHeader("Referer"));

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
			framework.setGuestCart(shopping.getGuestCart());
			shopping.getGuestCart().clear();
			framework.getGuestCart().clear();
		}

		response.sendRedirect("../Views/MainpageView.jsp");
			} else {
		account.setLoggedIn(false);
		framework.setLoggedIn(false);
		product.setLoggedIn(false);
		shopping.setLoggedIn(false);
		rating.setLoggedIn(false);
		account.createInfoMessage();
		response.sendRedirect("../Views/LoginView.jsp");
			}

		} else if (btnReg.equals("registrieren")) {

			String birthDate = birthDay + birthMonth + birthYear;
			account.setParametersForRegistration(firstName, lastName, email.toLowerCase(), birthDate, password, number);
			account.insertNewCustomer();

			response.sendRedirect("../Views/LoginView.jsp");

		} else if (btnApplyChange.equals("applyChange")) {

			recover.setEmail(email);
			recover.sendLinkWithCode();
			response.sendRedirect("../Views/RecoverPasswordView.jsp");

		} else if (btnChangePassword.equals("changePassword")) {

			recover.setPassword(password);
			recover.changePassword();
			account.setPasswordChanged(true);
			account.createInfoMessage();
			response.sendRedirect("../Views/LoginView.jsp");

		} else if (!CodeString.isBlank()) {
			int checkCode = Integer.parseInt(CodeString);
			recover.setCheckCode(checkCode);
			user_id = CodeString.substring(8);
			int id = Integer.parseInt(user_id);
			recover.setUser_id(id);
			response.sendRedirect("../Views/RecoverPasswordView.jsp");
		}

		else if (btnEditData.equals("saveChanges")) {

			account.editData(changeFirstName, changeLastName, changePhoneNumber, changeEmail);

			response.sendRedirect("../Views/ProfileView.jsp");

		} else if (btnEditPassword.equals("savePassword")) {

			if (account.changePassword(oldPassword, newPassword)) {
		account.setMessage("Password erfolgreich ge ndert.");
		response.sendRedirect("../Views/ProfileView.jsp");
			} else {

		response.sendRedirect("../Views/ProfileView.jsp");
			}

		} else if (btnChangeAddress.equals("changeAddress")) {

			account.editAddress(streetAddress, streetNumberAddress, cityAddress, zipCodeAddress);

			response.sendRedirect("../Views/ProfileView.jsp");
		} else if (btnLogout.equals("logout")) {
			account.setLoggedIn(false);
			framework.setLoggedIn(false);
			product.setLoggedIn(false);
			shopping.setLoggedIn(false);

			account.setInvoicesEmpty();

			response.sendRedirect("../Views/MainpageView.jsp");
		} else if (btnPortal.equals("btnPortal")) {

			response.sendRedirect("../Views/DatabaseView.jsp");
		} else if (!btnInvoiceMail.isBlank()) {
			account.sendInvoiceMail(btnInvoiceMail);
			response.sendRedirect(redirect);
		} else {

			response.sendRedirect("../Views/MainpageView.jsp");
		}
	} catch (Exception e) {
		e.printStackTrace();
		response.sendRedirect("../Views/ErrorpageView.jsp");
	}
	%>
</body>
</html>