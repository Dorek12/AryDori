<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
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
			String btnCart = check(request.getParameter("btnCart"));
			String btnWishlist = check(request.getParameter("btnWishlist"));
			String btnWishlistDelete = check(request.getParameter("btnWishlistDelete"));
			String product_name = check(request.getParameter("product"));
			String kaufen = check(request.getParameter("btnKaufen"));
			String btnColor = check(request.getParameter("btnColor"));
			String btnMemory = check(request.getParameter("btnMemory"));
			String btnRam = check(request.getParameter("btnRam"));
			int physical_memory = 0;
			int ram = 0;
			int product_id = 0;
			String product_nameURL = check(request.getParameter("product_name"));
			String colorURL = check(request.getParameter("color"));
			String memoryURL = check(request.getParameter("physical_memory"));
			String ramURL = check(request.getParameter("ram"));

			if (!btnCart.isBlank()) {
		product_id = Integer.parseInt(btnCart);

		if (product.isLoggedIn())
			shopping.addToCart(product_id);
		else {
			shopping.fillGuestCart(product_id);
			framework.setGuestCart(shopping.getGuestCart());
		}

		response.sendRedirect(product.getHrefOfCurrentProduct("Product"));
			} else if (!btnWishlist.isBlank()) {
		product_id = Integer.parseInt(btnWishlist);
		shopping.addToWishlist(product_id);
		response.sendRedirect(product.getHrefOfCurrentProduct("Product"));
			} else if (!btnWishlistDelete.isBlank()) {
		product_id = Integer.parseInt(btnWishlistDelete);
		shopping.deleteProductFromShoppingList("wishlist", product_id);
		response.sendRedirect(product.getHrefOfCurrentProduct("Product"));
			} else if (!btnColor.isBlank()) {
		if (btnColor.equalsIgnoreCase("white"))
			product.setColor(product.translateColor(btnColor));
		else
			product.setColor(btnColor);
		response.sendRedirect("../Views/ProductView.jsp");
			} else if (!btnMemory.isBlank()) {
		physical_memory = Integer.parseInt(btnMemory);
		product.setMemory(physical_memory);
		response.sendRedirect("../Views/ProductView.jsp");
			} else if (!btnRam.isBlank()) {
		ram = Integer.parseInt(btnRam);
		product.setRam(ram);
		response.sendRedirect("../Views/ProductView.jsp");
			} else {
		if (!product_nameURL.isBlank() || !colorURL.isBlank() || !ramURL.isBlank() || !memoryURL.isBlank()) {

			if (!product_nameURL.isBlank())
		product.setProduct_name(product_nameURL);
			else
		product.setProduct_name("");

			if (!colorURL.isBlank())
		product.setColor(colorURL);
			else
		product.setColor("");

			if (!memoryURL.isBlank()) {
		physical_memory = Integer.parseInt(memoryURL);
		product.setMemory(physical_memory);
			} else
		product.setMemory(0);

			if (!ramURL.isBlank()) {
		ram = Integer.parseInt(ramURL);
		product.setRam(ram);
			} else
		product.setRam(0);
			response.sendRedirect("../Views/ProductView.jsp");

		} else
			response.sendRedirect(
			"../Appls/ProductAppl.jsp?product_name=iPhone%2014%20Pro&color=Schwarz&ram=6&physical_memory=256");

	}

	} catch (Exception e) {
	e.printStackTrace();
	response.sendRedirect("../Views/ErrorpageView.jsp");
	}
	%>
</body>
</html>