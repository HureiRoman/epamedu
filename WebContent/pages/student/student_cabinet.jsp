<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../requirements.jsp"></jsp:include>

<link href="${pageContext.request.contextPath}/css/jquery.bxslider.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/css/style_saniok.css" rel="stylesheet" />

<script src="${pageContext.request.contextPath}/js/jquery.bxslider.min.js"></script>
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
		
		 $('.slider4').bxSlider({
			    slideWidth: 300,
			    minSlides: 2,
			    maxSlides: 3,
			    moveSlides: 1,
			    slideMargin: 10,
			    speed:400,
			    oneToOneTouch:true,
			    autoHover:true,
		});
		 
		 $('.bx-prev').css('display','none');
		 $('.bx-next').css('display','none');
	});
	
	function clickPrev(){
		$('.bx-prev').click();
	}
	function clickNext(){
		$('.bx-next').click();
	}
</script>

<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>Student cabinet</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="student_panel_sidenav.jsp"></jsp:include>
	<c:if test="${not empty listOfAdvertisement}">
		
		<div  style="min-width:50%;max-width:90%;margin-left:30%;text-align:center;margin:auto;height: 240px;margin-top:2%;">
			
			<div class="col s1" style="display: inline-block">
				<button onclick="clickPrev();" class="btn waves-effect waves-light prevSlide" onmouseout="this.style.backgroundColor='#2196F3';" onmouseover="this.style.backgroundColor='#FFEB3B';" type="button"  >
					<i class="mdi-hardware-keyboard-arrow-left"></i>
				</button>
			</div>
			<div style="display: inline-block">
				<div class="slider4">
				  <c:forEach  var="advertisement" items="${listOfAdvertisement}">
					  		<jsp:useBean id="now" class="java.util.Date" />
				  <c:choose>
				 	 <c:when test="${(advertisement.advertisementDate.time+(2*(60*60*24*1000))) gt now.time}">
					  <div class="slide">
					  	<div class="mySlide" style="-webkit-filter: sepia(30%);  ">
					  		<div class="row" style="text-align:center;" id="newTitle"><b><c:out value="${advertisement.title}"></c:out></b></div>
					  		<div class="row" style="text-align:left;" id="newContent"><c:out value="${advertisement.content}"></c:out></div>
					  		
					  		<fmt:setLocale value="${language}" />
							<fmt:formatDate value="${advertisement.advertisementDate}" pattern="yyyy-MMMM-dd HH:mm" var="date" />
					  		
					  		<div class="row"  id="ownerName">${advertisement.advertisementOwner.firstName} ${advertisement.advertisementOwner.lastName}</div>
					  		<div class="row"  id="newDate">${date}</div>
					  		
					  	</div>
					  </div>
					 </c:when>
					 <c:otherwise>
					 	<div class="slide">
					  	<div class="mySlide">
					  		<div class="row" style="text-align:center;" id="newTitle"><b><c:out value="${advertisement.title}"></c:out></b></div>
					  		<div class="row" style="text-align:left;" id="newContent"><c:out value="${advertisement.content}"></c:out></div>
					  		
					  		<fmt:setLocale value="${language}" />
							<fmt:formatDate value="${advertisement.advertisementDate}" pattern="yyyy-MMMM-dd HH:mm" var="date" />
					  		
					  		<div class="row"  id="ownerName">${advertisement.advertisementOwner.firstName} ${advertisement.advertisementOwner.lastName}</div>
					  		<div class="row"  id="newDate">${date}</div>
					  		
					  	</div>
					  </div>
					 </c:otherwise>
				  </c:choose>
				  </c:forEach> 
				</div>
			</div>
			<div class="col s1 " style="display: inline-block">
				<button onclick="clickNext();" class="btn waves-effect waves-light nextSlide" onmouseout="this.style.backgroundColor='#2196F3';" onmouseover="this.style.backgroundColor='#FFEB3B';" type="button"  >
					<i class="mdi-hardware-keyboard-arrow-right"></i>
				</button>
			</div>
			
		</div>
	</c:if>
	<div class="row">
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
			<a href="${pageContext.request.contextPath}/Controller?command=showMyGroup">
			<div class="card medium">
				<div class="card-image">
					<img class="img-responsive"
						src="${pageContext.request.contextPath}/img/student/group.jpg">
				</div>
				
				<div class="card-content">
					<span class="card-title black-text"><e:msg key="student.MyGroup"></e:msg></span>
				</div>
			</div>
			</a>
		</div>
		<div class="col s4">
		<a href="${pageContext.request.contextPath}/Controller?command=testRate">
			<div class="card medium">
				<div class="card-image">
					<img class="img-responsive"
						src="${pageContext.request.contextPath}/img/admin/teachers_icon.jpg" style="height:120%">
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