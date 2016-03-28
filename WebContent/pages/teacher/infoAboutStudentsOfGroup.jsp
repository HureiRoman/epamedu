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

	function openingModal(Id) {
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getDataAboutStudentById",
				"studentId" : Id,
			},
			complete : function(data) {
				gettingDataAboutStudentRespone(data);
				}
		});
			}
		function gettingDataAboutStudentRespone(data){
			var xml = data.responseXML;
			var fname;
			var lname;
			var pname;
			var email;
			var phone;
			var birth;
			var objective;
			var skills;
			var additional;
			var education;
			var english;
			$(xml)
			.find('student')
			.each(
					function(index) {
			fname=$(this).find('fname').text();
			lname=$(this).find('lname').text();
			pname=$(this).find('pname').text();
			email=$(this).find('email').text();
			phone=$(this).find('phone').text();
			birth=$(this).find('birth').text();
			objective=$(this).find('objective').text();
			skills=$(this).find('skills').text();
			additional=$(this).find('additional').text();
			education=$(this).find('education').text();
			english=$(this).find('english').text();

			 $("#fname").text( fname); 
			 $("#lname").text( lname); 
			 $("#pname").text( pname); 
			 $("#email").text( email); 
			 $("#phone").text( phone); 
			 $("#birth").text( birth); 
			 $("#objective").text( objective); 
			 $("#skills").text( skills); 
			 $("#additional").text( additional); 
			 $("#education").text( education); 
			 $("#english").text( english); 
			 $('#detailedInformationModal').openModal();
					});
		}
</script>
<!-- CSS MATERIAL  -->
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
	
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>Students</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>

	<div class="collection container">
		<div class="row"
			style="font-family: monospace; text-align: center; font-size: 1.9em;">
			<div class="col s6  offset-m3">
				<div class="card-panel white">
					<p>Students of group "${group }"</p>
				</div>
			</div>
		</div>
		<div class="row">
			<c:forEach var="j" items="${students }">
				<div class="col s3">
					<div class="card small">
						<a onclick="openingModal(${j.id })">
							<div class="card-image">
								<img class="img-responsive myStyleForImage"
									src="${pageContext.request.contextPath}/img/teacher/java_direction.jpg">

							</div>
							<div class="card-content" style="text-align: center;">
								<p style="font-size: 1.3em;">${j.firstName }${j.lastName }</p>
							</div>
						</a>
						<div class="card-action">
							<a class="btn" style="width: 11.8em;">Send message</a>
						</div>
					</div>


				</div>
			</c:forEach>

		</div>
	</div>

	<jsp:include page="teacher_panel_sidenav.jsp"></jsp:include>
	<jsp:include page="../footer.jsp"></jsp:include>

	<div id="detailedInformationModal" class="modal"
		style="max-height: 680px !important;">
		<div class="modal-content">
			<div class="card-panel white"
				style="padding: 0px !important; padding-top: 5px">
				<div class="row">
					<div class="col s3 m6 l3 ">
						<img class="img-responsive"
							src="${pageContext.request.contextPath}/img/teacher/java_direction.jpg"
							width="110%" height="140"> <br> <br>
					</div>
					<div class="col s9">
						<span style="font-family: monospace italic; font-size: 130%">
							<table>
								<tr>
									<td>First namesdfksdf</td>
									<td id="fname"></td>
								</tr>
								<tr>
									<td>Last name</td>
									<td id="lname"></td>
								</tr>
								<tr>
									<td>Parent name</td>
									<td id="pname"></td>
								</tr>
								<tr>
									<td>Email</td>
									<td id="email"></td>
								</tr>
								</table>
						</span>
					</div>
				</div>
			</div>
			<span style="font-size: 1.4em">CV</span>


			<div class="card-panel white" style="padding: 5px !important">
				<div class="col s9">
					<span style="font-family: monospace italic; font-size: 130%">
						<table>
							<tr>
								<td style="width: 15em"><i class="small material-icons">call</i>Phone</td>
								<td id="phone"></td>
							</tr>
							<tr>
								<td style="width: 15em"><i class="small material-icons">redeem</i>Birthday</td>
								<td id="birth"></td>
							</tr>
						</table>
					</span>
				</div>
			</div>
			<div class="card-panel white" style="padding: 5px !important">
				<div class="col s9">
					<span style="font-family: monospace italic; font-size: 130%">
						<table>
							<tr>
								<td style="width: 15em"><i class="small material-icons">trending_up</i>Objective</td>
								<td id="objective"></td>
							</tr>
						</table>
					</span>
				</div>
			</div>
			<div class="card-panel white" style="padding: 5px !important">
				<div class="col s9">
					<span style="font-family: monospace italic; font-size: 130%">
						<table>
							<tr>
								<td style="width: 15em"><i class="small material-icons">assignment_ind</i>Skills</td>
								<td id="skills"></td>
							</tr>
							<tr>
								<td style="width: 15em"><i class="small material-icons">description</i>Additional
									info</td>
								<td id="additional"></td>
							</tr>
						</table>
					</span>
				</div>
			</div>
			<div class="card-panel white" style="padding: 5px !important">
				<div class="col s9">
					<span style="font-family: monospace italic; font-size: 130%">
						<table>
							<tr>
								<td style="width: 15em"><i class="small material-icons">work</i>Education</td>
								<td id="education"></td>
							</tr>
							<tr>
								<td style="width: 15em"><i class="small material-icons">language</i>English
									level</td>
								<td id="english"></td>
							</tr>
						</table>
					</span>
				</div>
				<br> <br> <br>

			</div>
		</div>
	</div>
</body>
</html>