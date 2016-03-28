<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../requirements.jsp"></jsp:include>
<link href="${pageContext.request.contextPath}/css/dialogs.css"
	rel="stylesheet" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>GetWheels</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="trainee_panel_sidenav.jsp"></jsp:include>
	<div class="container">
		<div class="collection">
			<c:forEach items="${collocutors}" var="collocutorId">
			<e:dialogRow colocutorId="${collocutorId}" userId="${logined_user.id}"></e:dialogRow>
			</c:forEach>
		</div>
	</div>
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>