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
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>Admin cabinet</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>
	<div class="row">
		<jsp:include page="admin_panel_sidenav.jsp"></jsp:include>
		<div class="col s4">
			<a href="${pageContext.request.contextPath}/Controller?command=manageNews"><div class="card medium">
				<div class="card-image">
					<img class="img-responsive"
						src="${pageContext.request.contextPath}/img/admin/news_icon.jpg">
				</div>
				<div class="card-content">
					<span class="card-title black-text"><e:msg key="manageNews"></e:msg></span>
				</div>
			</div></a>
		</div>
		
		<div class="col s4"> 
			<a href="${pageContext.request.contextPath}/Controller?command=manageEmoloyees"><div class="card medium">
				<div class="card-image">
					<img class="img-responsive"
						src="${pageContext.request.contextPath}/img/admin/teachers_icon.jpg" style="height:120%">
				</div>
				<div class="card-content">
					<span class="card-title black-text"><e:msg key="staff"></e:msg></span>
				</div>
			</div>
		</div>
		<div class="col s4">
			<a href="${pageContext.request.contextPath}/Controller?command=manageCourses"><div class="card medium">
				<div class="card-image">
					<img class="img-responsive"
						src="${pageContext.request.contextPath}/img/admin/cources_icon.jpeg">
				</div>
				<div class="card-content">
					<span class="card-title black-text"><e:msg key="managementCourses"></e:msg></span>
				</div>
			</div>
		</div>
			<div class="col s4">
			<a href="${pageContext.request.contextPath}/Controller?command=manageAllUsers"><div class="card medium">
				<div class="card-image">
					<img class="img-responsive"
						src="${pageContext.request.contextPath}/img/peoples.jpg">
				</div>
				<div class="card-content">
					<span class="card-title black-text"><e:msg key="managingUsers"></e:msg></span>
				</div>
			</div>
		</div>
	</div>


	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>