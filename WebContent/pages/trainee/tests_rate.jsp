<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../requirements.jsp"></jsp:include>

<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>Tests rate</title>

</head>


<body bgcolor="#FAFAFA">

	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="applyOnTestModal.jsp"></jsp:include>
	<jsp:include page="trainee_panel_sidenav.jsp"></jsp:include>
	<jsp:include page="../testselectmodal.jsp"></jsp:include>

	<div class="row" style="padding-top: 30px;">
		<e:testrate logined_user="${logined_user }"></e:testrate>
	</div>

	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>