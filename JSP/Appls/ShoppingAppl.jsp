<%@page import="Beans.AbstractBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:useBean id="account" class="Beans.AccountBean" scope="session"></jsp:useBean>
	<jsp:useBean id="product" class="Beans.ProductBean" scope="session"></jsp:useBean>
	<jsp:useBean id="framework" class="Beans.FrameworkBean" scope="session"></jsp:useBean>
	<jsp:useBean id="shopping" class="Beans.ShoppingBean" scope="session"></jsp:useBean>
	<%!public String check(String toCheck) {
		if (toCheck == null)
			toCheck = "";
		return toCheck;
	}%>
	<%
	try {

		String redirect = check(request.getHeader("Referer"));
		String btnDeleteProductFromCart = check(request.getParameter("btnDeleteProductFrom_cart"));
		String btnDeleteProductFromWishlist = check(request.getParameter("btnDeleteProductFrom_wishlist"));
		String btnGoToCheckout = check(request.getParameter("btnGoToCheckout"));
		String btnWishlistIntoCart = check(request.getParameter("btnWishlistIntoCart"));
		String btnWishlistProductIntoCart = check(request.getParameter("btnWishlistProductIntoCart"));
		String btnDecreaseQuantity = check(request.getParameter("btnDecreaseQuantity"));
		String btnIncreaseQuantity = check(request.getParameter("btnIncreaseQuantity"));
		int product_id = 0;

		if (!btnDeleteProductFromCart.isBlank()) {
			product_id = Integer.parseInt(btnDeleteProductFromCart);

			if (account.isLoggedIn())
		shopping.deleteProductFromShoppingList("cart", product_id);

			else if (!account.isLoggedIn()) {
		shopping.removeFromGuestCart(product_id);
		framework.setGuestCart(shopping.getGuestCart());
			}

			shopping.setPriceSum(0);
			response.sendRedirect(redirect);

		} else if (!btnDeleteProductFromWishlist.isBlank()) {
			product_id = Integer.parseInt(btnDeleteProductFromWishlist);
			shopping.deleteProductFromShoppingList("wishlist", product_id);

			response.sendRedirect("../Views/WishlistView.jsp");

		} else if (btnGoToCheckout.equals("btnWasClicked")) {
			if (shopping.isLoggedIn())
		response.sendRedirect("../Views/CheckoutView.jsp");
			else
		response.sendRedirect("../Views/LoginView.jsp");

		} else if (btnWishlistIntoCart.equals("btnWasClicked")) {

			shopping.fillWishlistIntoCart();
			response.sendRedirect("../Views/WishlistView.jsp");

		} else if (!btnWishlistProductIntoCart.isBlank()) {
			product_id = Integer.parseInt(btnWishlistProductIntoCart);
			shopping.addToCart(product_id);
			if (!shopping.isOutOfStock()) {
		shopping.deleteProductFromShoppingList("wishlist", product_id);
			}

			response.sendRedirect("../Views/WishlistView.jsp");

		}

		else if (!btnDecreaseQuantity.isBlank()) {
			product_id = Integer.parseInt(btnDecreaseQuantity);
			shopping.changeQuantityOfProduct("-", product_id);

			response.sendRedirect(redirect);

		}

		else if (!btnIncreaseQuantity.isBlank()) {
			product_id = Integer.parseInt(btnIncreaseQuantity);
			shopping.changeQuantityOfProduct("+", product_id);

			response.sendRedirect(redirect);

		} else
			response.sendRedirect("../Views/MainpageView.jsp");

	} catch (Exception e) {
		e.printStackTrace();
		response.sendRedirect("../Views/ErrorpageView.jsp");
	}
	%>

</body>
</html>