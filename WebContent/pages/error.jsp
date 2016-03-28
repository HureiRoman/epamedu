<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Error</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link
	href="http://fonts.googleapis.com/css?family=Love+Ya+Like+A+Sister"
	rel="stylesheet" type="text/css">
<style type="text/css">
body {
	font-family: 'Love Ya Like A Sister', cursive;
}

body {
	background: #eaeaea;
}

.wrap {
	margin: 0 auto;
	width: 1000px;
}

.logo {
	text-align: center;
	margin-top: 15%;
}

.logo img {
	width: 350px;
}

.logo p {
	color: #272727;
	font-size: 40px;
	margin-top: 1px;
}

.logo p span {
	color: lightgreen;
}

.sub a {
	color: #fff;
	background: #272727;
	text-decoration: none;
	padding: 10px 20px;
	font-size: 13px;
	font-family: arial, serif;
	font-weight: bold;
	-webkit-border-radius: .5em;
	-moz-border-radius: .5em;
	-border-radius: .5em;
}

.footer {
	color: black;
	position: absolute;
	right: 10px;
	bottom: 10px;
}

.footer a {
	color: rgb(114, 173, 38);
}
</style>
</head>


<body>
	<div class="wrap">
		<div class="logo">
			<c:choose>
				<c:when test="${error == '403' }">
					<p>OOPS! - Access denied.</p>
					<img style="height:100%;" src="${pageContext.request.contextPath}/img/errors/403.png">
				</c:when>
				<c:when test="${error == '404' }">
					<p>OOPS! - Don't worry, simply page doesn't exist.</p>
					<img style="height:100%;" src="${pageContext.request.contextPath}/img/errors/404.png">
				</c:when>
				<c:otherwise>
					<p>OOPS! - Sorry... It's not you, it's us.</p>
					<img style="height:100%;" src="${pageContext.request.contextPath}/img/errors/500.png">
				</c:otherwise>
			</c:choose>
			<div class="sub">
				<p><a href="${pageContext.request.contextPath}/">Main page</a></p>
			</div>
		</div>
	</div>
</body>
</html>