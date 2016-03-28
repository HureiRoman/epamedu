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
<title>Trainee cabinet</title>

</head>

<body bgcolor="#FAFAFA" >
	
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="applyOnTestModal.jsp"></jsp:include>
	<jsp:include page="trainee_panel_sidenav.jsp"></jsp:include>
	
	<div class="row" style="padding-top:30px;">
		<div class="col s4">
		<a href="${pageContext.request.contextPath}/Controller?command=redirect&direction=myProfileWithCV">
			<div class="card medium">
				<div class="card-image">
					<img class="img-responsive"
						src="${pageContext.request.contextPath}/img/edit_profile.jpg" style="height:100%">
						
				</div>
				<div class="card-content">
					<span class="card-title black-text"><e:msg key="student.MyProfile"></e:msg></span>
				</div>
			</div>
			</a>
		</div>
		<div class="col s4">
			<a href="${pageContext.request.contextPath}/Controller?command=redirect&direction=trainee_hr_message">
			<div class="card medium">
				<div class="card-image">
					<img class="img-responsive"
						src="${pageContext.request.contextPath}/img/messages.jpg">
				</div>
				<div class="card-content" id="message_card">
					<span class="card-title black-text" style="float:left;"><e:msg key="getInTouchWithHR"></e:msg></span>
				</div>
			</div>
			</a>
		</div>
		<div class="col s4">
		<a href="${pageContext.request.contextPath}/Controller?command=redirect&direction=testing_activity">
			<div class="card medium">
				<div class="card-image">
					<img class="img-responsive"
						src="${pageContext.request.contextPath}/img/admin/directions_icon.jpg">
				</div>
				<div class="card-content">
					<span class="card-title black-text"><e:msg key="epam.directions"></e:msg></span>
				</div>
			</div>
			</a>
		</div>
		
	</div>
	
	
	<div class="row">
		<div class="col s4">
		<a href="${pageContext.request.contextPath}/Controller?command=testRate">
			<div class="card medium">
				<div class="card-image">
					<img class="img-responsive"
						src="${pageContext.request.contextPath}/img/admin/teachers_icon.jpg">
				</div>
				<div class="card-content">
					<span class="card-title black-text"><e:msg key="student.ViewRanking"></e:msg></span>
				</div>
			</div>
			</a>
		</div>
	</div>


	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>