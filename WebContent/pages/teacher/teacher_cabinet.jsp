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
<title>Teacher's room</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>


	<div class="row">
		<jsp:include page="teacher_panel_sidenav.jsp"></jsp:include>
		<div class="col s4">
			<a
				href="${pageContext.request.contextPath}/Controller?command=redirect&direction=myProfile">
				<div class="card medium">
					<div class="card-image">

						<img class="img-responsive"
							src="${pageContext.request.contextPath}/img/edit_profile.jpg"
							style="height: 100%">

					</div>
					<div class="card-content">
						<span class="card-title black-text"><e:msg
								key="myProfile"></e:msg></span>
					</div>
				</div>
			</a>
		</div>
		<div class="col s4">
			<a
				href="${pageContext.request.contextPath}/Controller?command=showGroups">
				<div class="card medium">
					<div class="card-image">

						<img class="img-responsive"
							src="${pageContext.request.contextPath}/img/teacher/view_groups.jpg"
							style="height: 100%">

					</div>
					<div class="card-content">
						<span class="card-title black-text"><e:msg
								key="teacher.viewGroups"></e:msg></span>
					</div>
				</div>
			</a>
		</div>
		<div class="col s4">
			<a
				href="${pageContext.request.contextPath}/Controller?command=topics">
				<div class="card medium">
					<div class="card-image">
						<img class="img-responsive"
							src="${pageContext.request.contextPath}/img/teacher/topics.jpg">

					</div>
					<div class="card-content">
						<span class="card-title black-text"><e:msg
								key="teacher.topics"></e:msg></span>
					</div>
				</div>
			</a>
		</div>
		<div class="col s4">
			<a
				href="${pageContext.request.contextPath}/Controller?command=showAdvertisement">
				<div class="card medium">
					<div class="card-image">
						<img class="img-responsive"
							src="${pageContext.request.contextPath}/img/teacher/new_adv.jpg">

					</div>
					<div class="card-content">
						<span class="card-title black-text"><e:msg
								key="teacher.advertisement"></e:msg></span>
					</div>
				</div>
			</a>
		</div>
		<div class="col s4">
			<a
				href="${pageContext.request.contextPath}/Controller?command=testsEdit">
				<div class="card medium">
					<div class="card-image">

						<img class="img-responsive"
							src="${pageContext.request.contextPath}/img/teacher/tests.jpg">

					</div>
					<div class="card-content">
						<span class="card-title black-text"><e:msg
								key="teacher.tests"></e:msg></span>
					</div>
				</div>
			</a>
		</div>
	</div>


	<jsp:include page="teacher_panel_sidenav.jsp"></jsp:include>
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>