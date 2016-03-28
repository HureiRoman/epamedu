<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<script>
function setLanguage(lang) {
	$.ajax({
		type : 'POST',
		url : "AjaxController",
		dataType : "xml",
		data : {
			"command" : "changeLanguage",
			"language" : lang
		},
		complete : function(data) {
			location.reload();
		}
	});
}
	function exit(){
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "exit"
			},
			complete : function(data) {
				window.location.href="${pageContext.request.contextPath}/"
			}
		});
	}
	
	
	function openLoginModal(){
		$('.modal').closeModal();
		$('#logingModal').openModal();
		$('#logingModal').css("height", "63%");
	}
</script>

<jsp:include page="testselectmodal.jsp"></jsp:include>
<jsp:include page="loginingmodal.jsp"></jsp:include>

<div class='navbar-fixed'>
<ul id="dropdownLang" class="dropdown-content">
		<li><a onclick="setLanguage('uk');"><img src="${pageContext.request.contextPath}/img/uk.png" width="30" height="30">UKR</a></li>
		<li><a onclick="setLanguage('en');"><img src="${pageContext.request.contextPath}/img/en.ico" width="30" height="30">ENG</a></li>
	</ul>
	<nav class="white" role="navigation">
		<div class="nav-wrapper " id="topMenu">
			<a id="logo-container" href="${pageContext.request.contextPath}/"
				class="brand-logo waves-effect  waves-black   center header_image">
				<img src="${pageContext.request.contextPath}/img/logo2.png"
				height="65" width=170>
			</a>
			<ul class="right hide-on-med-and-down">				
				<c:if test="${not empty logined_user}">
				<li><a style="cursor: pointer; cursor: hand;" onclick="exit()"><i
						class="mdi-action-settings-power right"></i><e:msg key="header.Exit"></e:msg></a></li>
				</c:if>
			</ul> 
			<ul class="left ">
				<c:choose>
					<c:when test="${not empty logined_user  && logined_user.roleType == 'TRAINEE' }">
						<li><a href="${pageContext.request.contextPath}/Controller?command=redirect&direction=trainee_cabinet"><i
							class="mdi-action-perm-identity left"></i><e:msg
								key="myCabinet"></e:msg></a></li>
					</c:when>
					<c:when test="${ not empty logined_user && logined_user.roleType == 'ADMIN' }">
						<li><a href="${pageContext.request.contextPath}/Controller?command=redirect&direction=ADMIN_PANEL_PAGE"><i
							class="mdi-action-language left"></i><e:msg	key="myCabinet"></e:msg></a></li>
					</c:when>
					<c:when test="${ not empty logined_user  && logined_user.roleType == 'TEACHER' }">
						<li><a href="${pageContext.request.contextPath}/Controller?command=redirect&direction=teacher_cabinet"><i
							class="mdi-action-work left"></i><e:msg	key="myCabinet"></e:msg></a></li>
					</c:when>
					<c:when test="${not empty logined_user && logined_user.roleType == 'STUDENT' }">
						<li><a href="${pageContext.request.contextPath}/Controller?command=student"><i
							class="mdi-action-home left"></i><e:msg	key="myCabinet"></e:msg></a></li>
					</c:when>
					<c:when test="${not empty logined_user && logined_user.roleType == 'HR' }">
						<li><a href="${pageContext.request.contextPath}/Controller?command=redirect&direction=hr_cabinet"><i
							class="mdi-social-group left"></i><e:msg	key="myCabinet"></e:msg></a></li>
					</c:when>
					<c:when test="${not empty logined_user && logined_user.roleType == 'GRADUATE' }">
						<li><a href="${pageContext.request.contextPath}/Controller?command=redirect&direction=trainee_cabinet"><i
							class="mdi-social-mood left"></i><e:msg	key="myCabinet"></e:msg></a></li>
					</c:when>
					<c:otherwise>
						<li><a style="cursor: pointer; cursor: hand;" onclick="openLoginModal()"><i
							class="mdi-hardware-mouse left"></i><e:msg key="header.Login"></e:msg></a></li>
					</c:otherwise>
					
				</c:choose>
				
				
				<li><a style="cursor: pointer; cursor: hand;" onclick="$('#testsModal').openModal();"><i
						class="mdi-editor-insert-chart left"></i><e:msg key="header.Tests"></e:msg></a></li>
			</ul>
			<ul class="right ">
				<li><a class="dropdown-button waves-effect waves-black" href="#!" data-activates="dropdownLang"><e:msg key="header.Lang"></e:msg><i class="mdi-navigation-arrow-drop-down right"></i></a></li>
			</ul>
		</div>
	</nav>
</div>
