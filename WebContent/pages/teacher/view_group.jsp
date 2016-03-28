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
		
		 $(".collection").css("position", "inherit");
	});
	
	
	function doGraduates(data) {
			$.ajax({
				type : 'POST',
				url : "AjaxController",
				dataType : "xml",
				data : {
					"command" : "doGraduates",
					"groupID" : data,
				},
				complete : function(data) {
					var response = data.responseXML.documentElement.firstChild.nodeValue;
					if(response=='true') Materialize.toast('Graduating was successed', 3000)
				}
			});
	}
</script>
<link href="${pageContext.request.contextPath}/css/gurei_style.css"
	type="text/css" rel="stylesheet" media="screen,projection" />
	
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>View group</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>
	


<div id="modal1" class="modal" style="font-family: monospace;">
    <div class="modal-content">
      <h3 style="text-align:center;"><e:msg key="groupInfo.AreYouSureGraduatingGroup"></e:msg></h3>
    </div>
    <div class="modal-footer">
      <a href="#!" class="modal-action modal-close waves-effect waves-red btn-flat "><e:msg key="index.cancelButton"></e:msg></a> 
      <a  onclick="doGraduates(${group.id});" class=" modal-action modal-close waves-effect waves-green btn-flat"><e:msg key="confirm"></e:msg></a>
    </div> 
  </div>



	<div class="collection container" >
	
	<div id="wrapper">
    <div id="wrap">
    <div id="fixed">
<div class="row">
   <div class="col s6 m6 l3 "><img class="img-responsive" style="height:85%; width:105%;" 
					src="${pageContext.request.contextPath}/img/teacher/java_direction.jpg" >
					 <ul id="dropdown2" class="dropdown-content">
        <li><a onclick="$('#modal1').openModal();"><e:msg key="graduateGroup"></e:msg></a></li>
           <li><a href="#!">List of students</a></li>
      </ul>
      <br>
						<br>
      <a class="btn dropdown-button" href="#!" data-activates="dropdown2" style="font-family: monospace; background-color:#024985">Action<i class="mdi-navigation-arrow-drop-down right" ></i></a>
       </div>
       <div class="col s12 m6 l6 "><span style="font-family: monospace ; font-size:150%"> Name:${group.title }<br>
       Direction:${direction }<br>
       Count of students:${countOfStudents }
       </span>
       </div>
					</div>    
</div>
    </div>
</div>
 <br>
 <br>
 <br>
 <br>
 <br>
 <br>
 <br>
 <br>
 <br>
    <c:if test="${empty homeWork}">
		<h3 class="myStyle1">There are no homeworks!</h3>
		</c:if>    
 <c:if test="${!empty homeWork}">
 <h3 class="myStyle1">Homeworks</h3>
 </c:if>
 
 <ul class="collection" style="position: inherit;">
 <c:forEach var="j" items="${homeWork }">
    <li class="collection-item" style="font-weight:bold">
    <div class="row">
       <div class="col s12 m6 l6" >
      <span class="title">Title: ${j.title }</span>
      <br>
      <span style="font-family: monospace italic;">DeadLine: ${j.deadline } <br>
        Type: ${j.typeOfTasks }
      </span>
      </div>
       <div>
       <br>
       <a class="waves-light btn" style="font-family: monospace ;">Uploaded Homework of students</a> </div> 
     </div>
     </li>
     </c:forEach>
  </ul>
 
 
  </div>


		<jsp:include page="teacher_panel_sidenav.jsp"></jsp:include>
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>