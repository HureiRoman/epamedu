<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<script type="text/javascript">
	var currentId;
	function openingModal(Id) {
		currentId = Id;
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
	function gettingDataAboutStudentRespone(data) {
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
		$(xml).find('student').each(function(index) {
			id = currentId;
			fname = $(this).find('fname').text();
			lname = $(this).find('lname').text();
			pname = $(this).find('pname').text();
			email = $(this).find('email').text();
			phone = $(this).find('phone').text();
			birth = $(this).find('birth').text();
			objective = $(this).find('objective').text();
			skills = $(this).find('skills').text();
			additional = $(this).find('additional').text();
			education = $(this).find('education').text();
			english = $(this).find('english').text();
			
			$("#photo").remove();
			$("#forImage").append( '<div id="photo"> <img src="${pageContext.request.contextPath}/images?type=users&id=' + id + '" class= "responsive-img"> </div>');
			$("#fname").text(fname);
			$("#lname").text(lname);
			$("#pname").text(pname);
			$("#email").text(email);
			$("#phone").text(phone);
			$("#birth").text(birth);
			
			if(objective!='')
			$("#objective").text(objective);
			else $('#objective_delete').remove();
			if(skills!='')
			$("#skills").text(skills);
			else $('#skills_delete').remove();
			if(education!='')
			$("#education").text(education);
			else $('#education_delete').remove();
			
			$("#additional").text(additional);
			$("#english").text(english);
			$('#detailedInformationModal').openModal();

			$('.modal').css('height', '80%');
		});
	}
</script>

<div id="detailedInformationModal" class="modal"
	style="max-height: 680px !important;">
	<div class="modal-content">
		<div class="modal-header">
			<h4 align="center"><e:msg key="infoAboutStudent"></e:msg></h4>

		</div>
		<div class="modal-body">
			<div class="row">
				<div class="col s4 ">
				<span id="forImage">
				
				
				
				</span>
					<br>
				</div>

				<div class="col s8" >
					<div class="section">
						<div class="col s6"><e:msg key="user.fname"></e:msg> :</div>
						<div class="col s6"  id="fname"></div>
					</div>
					<div class="section">
						<div class="col s6"><e:msg key="user.lname"></e:msg>:</div>
						<div class="col s6"  id="lname"></div>
					</div>
					<div class="section">
						<div class="col s6"><e:msg key="user.pname"></e:msg>:</div>
						<div class="col s6"  id="pname"></div>
					</div>
					<div class="section">
						<div class="col s6">
							 <e:msg key="user.dateOfBirth"></e:msg>:
						</div>
						<div class="col s6"  id="birth"></div>
					</div>
					<div class="divider"></div>
				</div>
			</div>
			<div class="divider"></div>

			<span style="font-size: 1.4em"><e:msg key="user.contactData"></e:msg>:</span>
			<div class="row" style="vertical-align:center">
				<div class="row">
					<div class="col s4">
						<i class="small material-icons">call</i><e:msg key="phone"></e:msg>
					</div>
					<div class="col s4" style="text-align:center"  id="phone"></div>
				</div>


				<div class="row">
					<div class="col s4">
						<i class="material-icons">email</i> Email
					</div>
					<div class="col s4" style="text-align:center"  id="email"></div>
				</div>
			</div>


			<div class="divider"></div>



			<div class="row">

				<div class="row" style="vertical-align:center" id="skills_delete">
					<div class="col s4">
						<i class="small material-icons">work</i><e:msg key="user.skills"></e:msg>:
					</div>
					<div class="col s4" style="text-align:center" id="skills"></div>
				</div>
				<div class="row" style="vertical-align:center" id="education_delete">
					<div class="col s4">
						<i class="small material-icons">work</i><e:msg key="user.education"></e:msg>:
					</div>
					<div class="col s4" style="text-align:center" id="education"></div>
				</div>
				<div class="row" style="vertical-align:center">
					<div class="col s4">
						<i class="small material-icons">language</i><e:msg key="user.englishLevel"></e:msg>:
					</div>
					<div class="col s4" style="text-align:center" id="english"></div>
				</div>
				
			</div>
			<div class="divider"></div>
			<div class="row" style="vertical-align:center" id="objective_delete">
				<div class="col s4" style="vertical-align:center">
					<i class="small material-icons">trending_up</i><e:msg key="registration.objective"></e:msg>:
				</div>
				<div class="col s4" id="objective" style="text-align:center"></div>
			</div>


		</div>
		<div class="modal-footer">
			<a class="waves-effect waves-light btn" onclick="$('#detailedInformationModal').closeModal()"><e:msg key="close"></e:msg></a>
		</div>

	</div>
</div>