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
<title>My profile</title>

<style>
label {
	font-size: 1rem;
}
</style>

</head>


<body bgcolor="#FAFAFA">

	<jsp:include page="header.jsp"></jsp:include>

	<c:if test="${logined_user.roleType == 'TRAINEE'}">
		<jsp:include page="trainee/applyOnTestModal.jsp"></jsp:include>
		<jsp:include page="trainee/trainee_panel_sidenav.jsp"></jsp:include>
	</c:if>

	<c:if test="${logined_user.roleType == 'STUDENT'}">
		<jsp:include page="student/student_panel_sidenav.jsp"></jsp:include>
	</c:if>

	<c:if test="${logined_user.roleType == 'GRADUATE'}">
		<jsp:include page="graduate/graduate_panel_sidenav.jsp"></jsp:include>
	</c:if>

	
	<fmt:setLocale value="en_US" scope="session" />
	<div class="col">
		<div class="row">
			<div class=" col s8 offset-s2 ">
				<div class="card-panel white">
					<div class="row">
						<h5><e:msg key="user.myProfile.myData"></e:msg>:</h5>
						<div class="input-field col s4">
							<img id="profileImage"
								src="${pageContext.request.contextPath}/images?type=users&id=${logined_user.id}"
								height="200" width="250">
								 <a
								href="${pageContext.request.contextPath}/Controller?command=redirect&direction=editProfileWithCV"
								class="waves-effect waves-light btn"><i
								class="material-icons left">create</i><e:msg key="user.myProfile.editProfile"></e:msg></a>					
						</div>

						<i class="mdi-image-timer-auto prefix small"></i> <label>ПІП:</label>

						<c:out
							value="${logined_user.lastName} ${logined_user.firstName} ${logined_user.parentName}" />
						<br> <br> <i class="mdi-action-event prefix small"></i><label>
							<e:msg key="user.dateOfBirth"></e:msg>: </label>
						<fmt:formatDate type="date" pattern="dd MMMM, yyyy"
							value="${logined_user.cv.birth}" />
						<br> <br> <i
							class="mdi-communication-quick-contacts-mail small"></i> <label>
							E-Mail: </label>
						<c:out value="${logined_user.email}" />
						<br> <br> <i
							class="mdi-communication-quick-contacts-dialer small"></i> <label>
							<e:msg key="phone"></e:msg>: </label>
						<c:out value="${logined_user.cv.phone}" />
						<br> <br> <i class="fa fa-language small"></i> <label>
							<e:msg key="user.englishLevel"></e:msg>: </label>
						<c:out value="${logined_user.cv.englishLevel}" />
						<c:if test="${!empty logined_user.cv.education && logined_user.cv.education.trim()!='' }">
						<br> <br> <i class="fa fa-graduation-cap small"></i> <label>
							<e:msg key="user.education"></e:msg>: </label>
						<c:out value="${logined_user.cv.education}" />
						</c:if>
							<c:if test="${!empty logined_user.cv.objective && logined_user.cv.objective.trim()!='' }">
						<br> <br> <i class="fa fa-line-chart small"></i> <label>
							<e:msg key="user.motivation"></e:msg>: </label>
						<c:out value="${logined_user.cv.objective}" />
						</c:if>
							<c:if test="${!empty logined_user.cv.skills &&  logined_user.cv.skills.trim()!=''}">
						<br> <br> <i class="fa fa-user-plus small"></i> <label>
							<e:msg key="user.skills"></e:msg>: </label>
						<c:out value="${logined_user.cv.skills}" />
						</c:if>
						<c:if test="${!empty logined_user.cv.additionalInfo && logined_user.cv.additionalInfo.trim()!=''}">
						<br> <br> <i class="fa fa-info small"></i> <label><e:msg key="user.additionalInfo"></e:msg>: </label>
						<c:out value="${logined_user.cv.additionalInfo}" />
						</c:if>
					</div>

				</div>
			</div>
		</div>
	</div>
	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>