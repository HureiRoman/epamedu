<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<title>My groups</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>
	
	<div class="collection container" >
		<jsp:include page="teacher_panel_sidenav.jsp"></jsp:include>
		<c:if test="${empty groups}">
		<h1 style="text-align:center"> <e:msg key="groups.youHaveNoGroups"></e:msg> </h1>
		</c:if>
		<c:if test="${!empty groups && empty directions}">
		<h1 style="text-align:center"> <e:msg key="groups.youHaveNoGroups"></e:msg> </h1>
		</c:if>
		<c:if test="${!empty groups && !empty directions}">
		
		<h4 style="text-align:center"><e:msg key="groups.yourGroups"></e:msg></h4>
		
		</c:if>
	 <ul class="collapsible  popout" data-collapsible="accordion">
	   <c:forEach var="j" items="${directions }">
    <li>
      <div class="collapsible-header">${j.name }</div>
      <div class="collapsible-body">

		<div class="row">
    
	 <c:forEach var="i" items="${groups }">
	  <c:if test="${j.id==i.directionId}">
		 	<a href="${pageContext.request.contextPath}/Controller?command=groupInfo&groupId=${i.id }&direction=${j.name }">
    <div class="col s12 m6 l3 collection-item" style=" border:3px solid #FAFAFA;">
		<img  src="${pageContext.request.contextPath}/images?type=directions&id=${j.id }" class=" responsive-img">
					<p style="text-align:center">${i.title}</p>
					</div>
  </a>
  </c:if>
   </c:forEach>

		</div>



</div>
    </li>
    </c:forEach> 
  </ul>
    </div>
	
	
		<jsp:include page="teacher_panel_sidenav.jsp"></jsp:include>
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>