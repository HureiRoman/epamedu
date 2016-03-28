<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<ul id="slide-out" class="side-nav">
	<li
		style=" background-image: url('${pageContext.request.contextPath}/img/admin/admin_back.jpg');  background-size: 240px 80px; ">
		<div class=row>

			<div class="col s5">
				<a
					href="${pageContext.request.contextPath}/Controller?command=redirect&direction=ADMIN_PANEL_PAGE">
					<img height="50" width="50" class="circle "
					src="${pageContext.request.contextPath}/img/admin/admin.png">
				</a>
			</div>
			<div class="col s5">
				<label class="white-text">${logined_user.firstName}
					${logined_user.lastName}</label>
			</div>
		</div>
	</li>
	<li><a
		href="${pageContext.request.contextPath}/Controller?command=manageNews">
			<i class=" mdi-action-picture-in-picture  prefix small"> </i> <e:msg key="manageNews"></e:msg>
	</a></li>
	<li><a
		href="${pageContext.request.contextPath}/Controller?command=manageCourses">
			<i class=" mdi-action-view-list  prefix small"></i> <e:msg key="managementCourses"></e:msg>
	</a></li>
	<li><a
		href="${pageContext.request.contextPath}/Controller?command=manageEmoloyees">
			<i class=" mdi-social-group  prefix small"></i> <e:msg key="staff"></e:msg>
	</a></li>
	<li><a
		href="${pageContext.request.contextPath}/Controller?command=manageAllUsers">
			<i class=" mdi-social-group-add  prefix small"></i> <e:msg key="admin.allUsers"></e:msg>
	</a></li>
		<li><a href="#" onclick="exit()"><i
			class="material-icons prefix">open_in_browser</i> <e:msg key="header.Exit"></e:msg></a></li>
	
</ul>