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
	<jsp:useBean id="confirmation" class="Beans.ConfirmationBean"
		scope="session"></jsp:useBean>

	<%!public String check(String toCheck) {
		if (toCheck == null)
			toCheck = "";
		return toCheck;
	}%>
	<%
	try {

		String rememberAddress = check(request.getParameter("rememberAddress"));
		String btnBuy = check(request.getParameter("btnBuy"));
		String firstName = check(request.getParameter("first_name"));
		String lastName = check(request.getParameter("last_name"));
		String street = check(request.getParameter("street"));
		String house_number = check(request.getParameter("house_number"));
		String city = check(request.getParameter("city"));
		String postcodeString = check(request.getParameter("postcode"));
		String payment_method = check(request.getParameter("payment_method"));
		int postcode = 0;

		if (btnBuy.equals("btnWasClicked")) {

			if (shopping.checkStockBeforeCheckOut()) {
				// checke doppelt für mehr Konsistenz
		if (shopping.checkStockBeforeCheckOut()) {
			// Aufruf der Methoden nach Relevanz
			shopping.updateStock();
			shopping.createInvoiceForOrder(payment_method);
			shopping.createOrderConfirmationMailAsHtml();
			shopping.deleteCartOfUser();
			account.setInvoicesEmpty();
			account.setUserInvoices();
			if (!firstName.isBlank() && !lastName.isBlank() && !street.isBlank() && !house_number.isBlank()
					&& !city.isBlank() && !postcodeString.isBlank()) {
				confirmation.setHtmlConfirmation(firstName, lastName, street, house_number, city, postcodeString);
			}
		}
			} else {
		shopping.updateCartOfUser();
		confirmation.setOrderFailed();
			}
		} else {
			confirmation.setOrderFailed();
		}
		if (rememberAddress.equals("remember")) {
			if (!street.isBlank() && !house_number.isBlank() && !city.isBlank() && !postcodeString.isBlank()) {
		postcode = Integer.parseInt(postcodeString);
		shopping.rememberAddressOfUser(street, house_number, city, postcode);
		account.setStreet(street);
		account.setHouseNumber(house_number);
		account.setCity(city);
		account.setPostcode(postcode);

			}
		}

		response.sendRedirect("../Views/ConfirmationView.jsp");

	} catch (Exception e) {
		e.printStackTrace();
		response.sendRedirect("../Views/ErrorpageView.jsp");
	}
	%>

</body>
</html>