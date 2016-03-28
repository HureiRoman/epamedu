<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="requirements.jsp"></jsp:include>
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
	<script src="../css/multiDrop/dropzone.js"></script>
	<script src="../css/multiDrop/dropzone-amd-module.js"></script>
	<link href="${pageContext.request.contextPath}/css/multiDrop/dropzone.css"
	rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/multiDrop/basic.css"
	rel="stylesheet" />
	<script>
	Dropzone.autoDiscover = false;
	</script>
<title>Gurei</title>

</head>


<body bgcolor="#FAFAFA">
	<div class="container">
<form action="#!"
      class="dropzone"
      id="my-awesome-dropzone"></form>
</div>
</body>
</html>