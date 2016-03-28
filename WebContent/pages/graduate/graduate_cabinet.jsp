<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="../requirements.jsp"></jsp:include>

<script type="text/javascript">
	$(document).ready(function() {
		$(".card").hover(function() {
			$(this).stop().animate({
				opacity : "0.5"
			}, 'slow');
		}, function() {
			$(this).stop().animate({
				opacity : "1"
			}, 'slow');
		});
	});
</script>
<!-- CSS MATERIAL  -->
<link href="${pageContext.request.contextPath}/css/materialize.css"
	type="text/css" rel="stylesheet" media="screen,projection" />
<link href="${pageContext.request.contextPath}/css/style.css"
	type="text/css" rel="stylesheet" media="screen,projection" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>Graduate cabinet</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>
	<div class="row">
		<jsp:include page="graduate_panel_sidenav.jsp"></jsp:include>
		<div class="col s4">
			<div class="card medium">
				<div class="card-image">
				<a href="${pageContext.request.contextPath}/Controller?command=redirect&direction=myProfileWithCV">
					<img class="img-responsive"
						src="${pageContext.request.contextPath}/img/edit_profile.jpg" style="height:100%">
						</a>
				</div>
				<div class="card-content">
					<span class="card-title black-text">Мій профіль</span>
				</div>
			</div>
		</div>
		<div class="col s4">
			<a
				href="${pageContext.request.contextPath}/Controller?command=manageNews"><div
					class="card medium">
					<div class="card-image">
						<img class="img-responsive"
							src="${pageContext.request.contextPath}/img/admin/news_icon.jpg">
					</div>
					<div class="card-content">
						<span class="card-title black-text">Моя Група</span>
					</div>
				</div></a>
		</div>
	</div>


	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>