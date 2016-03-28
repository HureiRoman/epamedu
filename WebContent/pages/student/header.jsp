<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<script>
$( document ).ready(function() {
	 raceChecked("${logined_user.roleType}");
});


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

	function raceChecked(role){
		if(role != "STUDENT"){
			window.location.href = '${pageContext.request.contextPath}/?act=forbidden';
		}
	}
</script>

<script>
function exit(){
	$.ajax({
		type : 'POST',
		url : "AjaxController",
		dataType : "xml",
		data : {
			"command" : "exit"
		},
		complete : function(data) {
			window.location.href = "${pageContext.request.contextPath}/";
		}
	});
}

</script>
<jsp:include page="../testselectmodal.jsp"></jsp:include>
<div class='navbar-fixed'>
<ul id="dropdownLang" class="dropdown-content">
		<li><a onclick="setLanguage('uk');"><img src="${pageContext.request.contextPath}/img/uk.png" width="30" height="30">UKR</a></li>
		<li><a onclick="setLanguage('en');"><img src="${pageContext.request.contextPath}/img/en.ico" width="30" height="30">Eng</a></li>
	</ul>
	<nav class="white" role="navigation">
		<div class="nav-wrapper ">
			<a id="logo-container" href="${pageContext.request.contextPath}/"
				class="brand-logo waves-effect  waves-black  hide-on-med-and-down center header_image">
				<img src="${pageContext.request.contextPath}/img/logo2.png"
				height="65" width=170>
			</a>
			<ul class="left ">
				<li><a href="#" data-activates="slide-out"
					class="button-collapse show-on-large"><i
						class="mdi-navigation-menu medium"></i></a></li>
			</ul>
				<ul class="right hide-on-med-and-down" style="padding-right:30px;">
				<li><a style="cursor: pointer; cursor: hand;" onclick="$('#testsModal').openModal();"><i
					class="mdi-image-dehaze left"></i><e:msg key="header.Tests"></e:msg></a></li>
				
				<li><a class="dropdown-button waves-effect waves-black" href="#!" data-activates="dropdownLang"><e:msg key="header.Lang"></e:msg><i class="mdi-navigation-arrow-drop-down right"></i></a></li>
			</ul>
			
		</div>
	</nav>
</div>