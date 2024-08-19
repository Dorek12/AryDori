<%@ page language='java' contentType='text/html; charset=UTF-8'
	pageEncoding='UTF-8'%>
<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<title>Ihr Konto</title>
<link rel="icon" href="../../IMG/Title/Account.png" type="image/png">
<link
	href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css'
	rel='stylesheet'
	integrity='sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM'
	crossorigin='anonymous'>
<script
	src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'
	integrity='sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz'
	crossorigin='anonymous'></script>
<script type='text/javascript' src='../../JS/Main.js'></script>
<link rel='stylesheet'
	href='https://use.fontawesome.com/releases/v5.3.1/css/all.css'
	integrity='sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU'
	crossorigin='anonymous'>
<link rel='stylesheet' type='text/css' href='../../CSS/Main.css'>
</head>
<body>
	<jsp:useBean id='framework' class='Beans.FrameworkBean' scope='session'></jsp:useBean>
	<jsp:useBean id='account' class='Beans.AccountBean' scope='session'></jsp:useBean>

	<jsp:getProperty property='headerAsHtml' name='framework' />
	<jsp:getProperty property='profilViewAsHtml' name='account' />
	<jsp:getProperty property='footerAsHtml' name='framework' />
</body>
<script type='text/javascript' src='../../JS/Profile.js'></script>
</html>