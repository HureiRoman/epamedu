<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<script type="text/javascript">
$( document ).ready(function() {
	checkUnreadedMessages();
});
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}
function checkUnreadedMessages(){
	$.ajax({
		type : 'POST',
		url : "AjaxController",
		dataType : "json",
		data : {
			"command" : "getCountOfUnreadedMessages"
		},
		complete : function(data) {
			response = data.responseJSON;
			count = response.count;
			var currentCount =  parseInt(getCookie("count"));
			if(count>0){
				if (count!=currentCount&&count>currentCount){
					var aSound = document.createElement('audio');
					aSound.setAttribute('src',
									'${pageContext.request.contextPath}/audio/message_sound.mp3');
					aSound.play();	
					document.cookie="count="+count;
				}
				$('#badgeDiv').append('<span class="new badge">'+count+'</span>');
				
			}
		//window.setTimeout(checkUnreadedMessages, 3000);
		}
	});
}
</script>
<ul id="slide-out" class="side-nav">
	<li
		style=" background-image: url('${pageContext.request.contextPath}/img/admin/admin_back.jpg');  background-size: 240px 80px; ">
		<div class=row>

			<div class="col s5">
				<a href="${pageContext.request.contextPath}/Controller?command=student">
					<img height="50" width="50" class="circle "
					src="${pageContext.request.contextPath}/images?type=users&id=${logined_user.id}">
				</a>
			</div>
			<div class="col s5">
				<label class="white-text">${logined_user.firstName}
					${logined_user.lastName}</label>
			</div>
		</div>
	</li>
	<li><a
		href="${pageContext.request.contextPath}/Controller?command=redirect&direction=myProfileWithCV"><i
			class="material-icons prefix">perm_identity</i> <e:msg key="student.MyProfile"></e:msg></a></li>
	<li><a
		href="${pageContext.request.contextPath}/Controller?command=showMyGroup"><i
			class="material-icons prefix">supervisor_account</i> <e:msg key="student.MyGroup"></e:msg> </a></li>
	<li><a id="badgeDiv" 
		href="${pageContext.request.contextPath}/Controller?command=dialogs"><i
			class="material-icons prefix">email</i><e:msg key="teacher.myMessages"></e:msg></a></li>
	<li><a
		href="${pageContext.request.contextPath}/Controller?command=testRate"><i
			class="material-icons prefix">assessment</i> <e:msg key="student.ViewRanking"></e:msg></a></li>
	<li><a href="#" onclick="exit()"><i
			class="material-icons prefix">open_in_browser</i> <e:msg key="header.Exit"></e:msg></a></li>

</ul>