<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:useBean id="rating" class="Beans.RatingBean" scope="session"></jsp:useBean>
	<%!public String check(String toCheck) {
		if (toCheck == null)
			toCheck = "";
		return toCheck;
	}%>
	<%
	try {

		String btnRateProduct = check(request.getParameter("btnRateProduct"));
		String review = check(request.getParameter("review"));
		String ratingString = check(request.getParameter("rating_user"));
		String product_nameURL = check(request.getParameter("product_name"));
		String colorURL = check(request.getParameter("color"));
		String memoryURL = check(request.getParameter("physical_memory"));
		String ramURL = check(request.getParameter("ram"));
		int ratingInt = 5;
		int physical_memory = 0;
		int ram = 0;

		if (btnRateProduct.equals("rateProduct")) {
			System.out.println(ratingString);
			if (!ratingString.isBlank() && !ratingString.equals("0"))
		ratingInt = Integer.parseInt(ratingString);

			rating.insertRatingIntoDb(ratingInt, review);
			rating.setRatedProduct(true);
			response.sendRedirect("../Views/RatingView.jsp");

		} else {
			if (!product_nameURL.isBlank() || !colorURL.isBlank() || !ramURL.isBlank() || !memoryURL.isBlank()) {

		if (!product_nameURL.isBlank())
			rating.setProduct_name(product_nameURL);
		else
			rating.setProduct_name("");

		if (!colorURL.isBlank())
			rating.setColor(colorURL);
		else
			rating.setColor("");

		if (!memoryURL.isBlank()) {
			physical_memory = Integer.parseInt(memoryURL);
			rating.setMemory(physical_memory);
		} else
			rating.setMemory(0);

		if (!ramURL.isBlank()) {
			ram = Integer.parseInt(ramURL);
			rating.setRam(ram);
		} else
			rating.setRam(0);
		response.sendRedirect("../Views/RatingView.jsp");

			} else
		response.sendRedirect(
				"../Appls/RatingAppl.jsp?product_name=iPhone%2014%20Pro&color=Schwarz&ram=6&physical_memory=256");

		}

	} catch (Exception e) {
		e.printStackTrace();
		response.sendRedirect("../Views/ErrorpageView.jsp");
	}
	%>
</body>
</html>