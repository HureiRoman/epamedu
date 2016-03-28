<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>

<script>
$( document ).ready(function() {
	 raceChecked("${logined_user.roleType}");
});

	function raceChecked(role){
		if(role != "GRADUATE"){
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

<div class='navbar-fixed'>
	<nav class="white" role="navigation">
		<div class="nav-wrapper ">
			<a id="logo-container" href="${pageContext.request.contextPath}/"
				class="brand-logo waves-effect  waves-black  hide-on-med-and-down center header_image">
				<img src="${pageContext.request.contextPath}/img/logo2.png"
				height="65" width=170>
			</a>
			<ul class="left hide-on-med-and-down">
				<li><a href="#" data-activates="slide-out"
					class="button-collapse show-on-large"><i
						class="mdi-navigation-menu medium"></i></a></li>
				<li><a style="cursor: pointer; cursor: hand;" onclick="exit()"><i
						class="mdi-action-backup right"></i>Exit</a></li>
			</ul>
		</div>
	</nav>
</div>