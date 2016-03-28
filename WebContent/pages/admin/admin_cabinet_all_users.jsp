<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../requirements.jsp"></jsp:include>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/knockout-3.3.0.js"></script>
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/pagination/jquery.quick.pagination.min.js"></script>
	<link href="${pageContext.request.contextPath}/css/pagination/styles.css"
		  type="text/css" rel="stylesheet" media="screen,projection"/>

<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>Users</title>
<script type="text/javascript">
	$(document).ready(function () {
		$('ul#users').quickPagination({pageSize: "20"});
	});
function setUserActive(id, active) {
	$.ajax({
				type : 'POST',
				url : "AjaxController?command=setUserActive",
				dataType : "xml",
				data : {
					"id" : id,
					"active" : active
				},
				complete : function(data) {
					var response = data.responseXML.documentElement.firstChild.nodeValue;
					if (response == 1) {
						if (Boolean(active)) {
							Materialize.toast($.t("user_activated"), 3000);
						} else {
							Materialize.toast($.t("user_deactivated"), 3000);
						}
					} else {
						Materialize.toast($.t("user_change_status_error"),
								3000);
					}
				}
			});
}
</script>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="admin_panel_sidenav.jsp"></jsp:include>
	<div class="container">
		<div class="collection-header">
			<div class="row">
				<div class="col s10">
					<h4><i class="fa  fa-users  medium blue-text"></i><e:msg key="users"></e:msg></h4>
				</div>
			</div>

		</div>
		<ul class="collection with-header" id="users">
			 <c:forEach items="${users}" var="user">
				<e:user user="${user}"></e:user>
			</c:forEach> 
		</ul>
	</div>
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>