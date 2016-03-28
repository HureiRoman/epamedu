<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="requirements.jsp"></jsp:include>
<!--  Scripts-->

<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.maskedinput.js"></script>

<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>Registration</title>
<script type="text/javascript">
	var firstNameRegEx = /^([А-ЯA-ZЇІЄ])(([а-яa-zїіє]{0,16})([-–'])?([A-ZА-Яа-яa-zїіє]{1,16}))+$/;
	var lastNameRegEx = /^([А-ЯA-ZЇІЄ])(([а-яa-zїіє]{0,16})([-–'])?([A-ZА-Яа-яa-zїіє]{1,16}))+$/;
	var parentNameRegEx = /^([А-ЯA-ZЇІЄ])(([а-яa-zїіє]{0,16})([-–'])?([A-ZА-Яа-яa-zїіє]{1,16}))+$/;
	var passwordRegEx = /^\D[^<>]{4,30}$/;
	var phoneRegEx = /\(?([0-9]{3})\)?([ .-]?)([0-9]{3})\2([0-9]{4})$/;
	var infoRegEx = /^[^<>]{0,250}$/;
	var emailRegEx = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;

	function removeMultipleWhiteSpaces(value) {
		return value.replace(/\s\s+/g, ' ');
	}

	$(document).ready(function() {
		$('.tooltipped').tooltip({
			delay : 50
		});
		$('select').material_select();
		$('#el_phone').mask('(999)-999-9999');
		$('#el_email').on('input blur', function() {
			checkEmail($(this).val());
		});

		$('#el_first_name').on('input blur', function() {
			var input = $(this);
			var is_valid = firstNameRegEx.test(input.val());
			if (is_valid) {
				input.removeClass("invalid").addClass("valid");
			} else {
				input.removeClass("valid").addClass("invalid");
			}
		});

		$('#el_last_name').on('input blur', function() {
			var input = $(this);
			var is_valid = lastNameRegEx.test(input.val());
			if (is_valid) {
				input.removeClass("invalid").addClass("valid");
			} else {
				input.removeClass("valid").addClass("invalid");
			}
		});

		$('#el_parent_name').on('input blur', function() {
			var input = $(this);
			var is_valid = parentNameRegEx.test(input.val());
			if (is_valid) {
				input.removeClass("invalid").addClass("valid");
			} else {
				input.removeClass("valid").addClass("invalid");
			}
		});

		$('#el_email').on('input blur', function() {
			var input = $(this);
			var is_valid = emailRegEx.test(input.val());
			if (is_valid) {
				input.removeClass("invalid").addClass("valid");
			} else {
				input.removeClass("valid").addClass("invalid");
			}
		});

		$('#el_password').on('input blur', function() {
			var input = $(this);
			var confirmPass = $('#el_confirm_password');
			var is_valid = passwordRegEx.test(input.val());

			if (is_valid && input.val() == confirmPass.val()) {
				input.removeClass("invalid").addClass("valid");
				confirmPass.removeClass("invalid").addClass("valid");
			} else {
				input.removeClass("valid").addClass("invalid");
				confirmPass.removeClass("valid").addClass("invalid");
			}
		});

		$('#el_confirm_password').on('input blur', function() {
			var input = $(this);
			var confirmPass = $('#el_password');
			var is_valid = passwordRegEx.test(input.val());

			if (is_valid && input.val() == confirmPass.val()) {
				input.removeClass("invalid").addClass("valid");
				confirmPass.removeClass("invalid").addClass("valid");
			} else {
				input.removeClass("valid").addClass("invalid");
				confirmPass.removeClass("valid").addClass("invalid");
			}
		});

		$('#el_phone').on('input blur', function() {
			var input = $(this);
			var is_valid = phoneRegEx.test(input.val());
			if (is_valid) {
				input.removeClass("invalid").addClass("valid");
			} else {
				input.removeClass("valid").addClass("invalid");
			}
		});

		$('#el_objective').on('input blur', function() {
			var input = $(this);
			var is_valid = infoRegEx.test(input.val());
			if (is_valid) {
				input.removeClass("invalid").addClass("valid");
			} else {
				input.removeClass("valid").addClass("invalid");
			}
		});

		$('#el_additional_info').on('input blur', function() {
			var input = $(this);
			var is_valid = infoRegEx.test(input.val());
			if (is_valid) {
				input.removeClass("invalid").addClass("valid");
			} else {
				input.removeClass("valid").addClass("invalid");
			}
		});

		$('#el_skills').on('input blur', function() {
			var input = $(this);
			var is_valid = infoRegEx.test(input.val());
			if (is_valid) {
				input.removeClass("invalid").addClass("valid");
			} else {
				input.removeClass("valid").addClass("invalid");
			}
		});

		$('#el_education').on('input blur', function() {
			var input = $(this);
			var is_valid = infoRegEx.test(input.val());
			if (is_valid) {
				input.removeClass("invalid").addClass("valid");
			} else {
				input.removeClass("valid").addClass("invalid");
			}
		});
	});

	function checkEmail(email) {
		$.ajax({
			type : 'POST',
			url : "AjaxController?command=checkEmail",
			dataType : "xml",
			data : {
				'email' : email,
			},
			complete : function(data) {
				processCheckEmailResult(data);
			}
		});
	}

	function processCheckEmailResult(data) {
		var response = data.responseXML.documentElement.firstChild.nodeValue;
		if (response == 1) {
			Materialize.toast($.t("reg_email_already_exist"), 3000);
		}
	}

	function selectTab(tabname) {
		$(document).ready(function() {
			$('ul.tabs').tabs('select_tab', tabname);
			$('html, body').animate({
				scrollTop : 0
			}, 'fast');
		});
	}
	function submitRegistration() {
		var firstName = $('#el_first_name').val();
		var lastName = $('#el_last_name').val();
		var parentName = $('#el_parent_name').val();
		var email = $('#el_email').val();
		var password = $('#el_password').val();
		var confirmPass = $('#el_confirm_password').val();
		var phone = $('#el_phone').val();
		var englishLevel = $('#el_english_level').val();
		var education = $('#el_education').val();
		var birth = $('#birth').val();
		var objective = removeMultipleWhiteSpaces($('#el_objective').val());
		var skills = removeMultipleWhiteSpaces($('#el_skills').val());
		var additionalInfo = removeMultipleWhiteSpaces($('#el_additional_info')
				.val());
		if (firstName == '' || lastName == '' || parentName == ''
				|| email == '' || password == '' || confirmPass == ''
				|| englishLevel == -1 || birth == '') {
			$("#circle").hide();
			Materialize.toast($.t("reg_enter_all_required_data"), 3000);
		} else if (!firstNameRegEx.test(firstName)) {
			$("#circle").hide();
			Materialize.toast($.t("reg_fname_error"), 3000);
		} else if (!lastNameRegEx.test(lastName)) {
			$("#circle").hide();
			Materialize.toast($.t("reg_lname_error"), 3000);
		} else if (!parentNameRegEx.test(parentName)) {
			$("#circle").hide();
			Materialize.toast($.t("reg_pname_error"), 3000);
		} else if (!passwordRegEx.test(password)) {
			$("#circle").hide();
			Materialize.toast($.t("reg_pass_error"), 3000);
		} else if (password != confirmPass) {
			$("#circle").hide();
			Materialize.toast($.t("reg_pass_dont_match"), 3000);
		} else if (!phoneRegEx.test(phone)) {
			$("#circle").hide();
			Materialize.toast($.t("reg_phone_error"), 3000);
		} else if (!emailRegEx.test(email)) {
			$("#circle").hide();
			Materialize.toast($.t("reg_email_error"), 3000);
		} else if (birth.length == 0) {
			$("#circle").hide();
			Materialize.toast($.t("reg_birth_error"), 3000);
		} else if (!infoRegEx.test(education)) {
			$("#circle").hide();
			Materialize.toast($.t("reg_education_error"), 3000);
		} else if (!infoRegEx.test(objective)) {
			$("#circle").hide();
			Materialize.toast($.t("reg_objective_error"), 3000);
		} else if (!infoRegEx.test(skills)) {
			$("#circle").hide();
			Materialize.toast($.t("reg_skills_error"), 3000);
		} else if (!infoRegEx.test(additionalInfo)) {
			$("#circle").hide();
			Materialize.toast($.t("reg_additional_info_error"), 3000);
		} else {
			var socialID = $('#socialID').val();
			var dataFrom = $('#dataFrom').val();
			$("#circle").show();
			$.ajax({
				type : 'POST',
				url : "AjaxController?command=registration",
				dataType : "xml",
				data : {
					'fname' : firstName,
					'lname' : lastName,
					'pname' : parentName,
					'email' : email,
					'password' : password,
					'phone' : phone,
					'birth' : birth,
					'objective' : objective,
					'education' : education,
					'skills' : skills,
					'additional_info' : additionalInfo,
					'english_level' : englishLevel,
					'password_repeat' : confirmPass,
					'data_from' : dataFrom,
					'socialID' : socialID
				},
				complete : function(data) {
					processRegistrationResult(data);
				}
			});
		}
	}
	function processRegistrationResult(data) {
		var response = data.responseXML.documentElement.firstChild.nodeValue;
		if (response == 1) {
			// 			Materialize.toast($.t("reg_successful"), 3000)
			window.location.href = "/EpamEducationalProject/?reg_sended=true";
		} else if (response == 2) {
			Materialize.toast($.t("reg_email_already_exist"), 3000);
			$("#circle").hide();
		} else if (response == 3) {
			Materialize.toast($.t("reg_pass_dont_match"), 3000);
			$("#circle").hide();
		} else if (response == 4) {
			Materialize.toast($.t("reg_some_value_is_invalid"), 3000);
			$("#circle").hide();
		}
	}
</script>

</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="loginingmodal.jsp"></jsp:include>
	<jsp:include page="header.jsp"></jsp:include>
	<div class="col">
		<div class="col s12" id="tabsCol">
			<ul class="tabs ">
				<%-- style="pointer-events: none;" --%>
				<li class="tab col s6 "><a href="#contact_tab"
					class="active indigo-text"> <i class="mdi-device-storage small"></i>
						<e:msg key="registration.contact_information"></e:msg>
				</a></li>
				<li class="tab  col s6"><a href="#cv_tab" class="indigo-text">
						<e:msg key="registration.cv"></e:msg> <i
						class="mdi-editor-border-color small"></i>
				</a></li>
			</ul>
		</div>
		<div id="contact_tab" class="col s12">
			<div class="row">
				<div class=" col s8 offset-s2 ">
					<div class="card-panel white">
						<div class="row">
							<h5>
								<e:msg key="registration.enter_contact_info"></e:msg>
							</h5>
							<div class="row">
							    <input id="socialID"  type="hidden" value="${userData.socialId}" >
							    <input id="dataFrom"  type="hidden" value="${userData.dataFrom}">
								<div class="input-field col s4">
									<i class="mdi-image-timer-auto prefix"></i> <input
										value="${userData.lastName}" name="lastName" id="el_last_name"
										type="text" class="validate tooltipped" maxlength="16"
										data-position="bottom" data-delay="50"
										data-tooltip="<e:msg key="registration.tooltip_name"></e:msg>">
									<label for="el_last_name"><e:msg
											key="registration.lname"></e:msg></label>
								</div>
								<div class="input-field col s4">
									<i class="mdi-image-timer-auto prefix"></i> <input
										value="${userData.firstName}" name="firstName"
										id="el_first_name" type="text" class="validate tooltipped"
										maxlength="16" data-position="bottom" data-delay="50"
										data-tooltip="<e:msg key="registration.tooltip_name"></e:msg>">
									<label for="el_first_name"><e:msg
											key="registration.fname"></e:msg></label>
								</div>
								<div class="input-field col s4">
									<i class="mdi-image-timer-auto prefix"></i> <input
										name="parentName" id="el_parent_name" type="text"
										class="validate tooltipped" maxlength="16"
										data-position="bottom" data-delay="50"
										data-tooltip="<e:msg key="registration.tooltip_name"></e:msg>">
									<label for="el_parent_name"><e:msg
											key="registration.pname"></e:msg></label>
								</div>
							</div>
							<div class="row">
								<div class="input-field col s12">
									<i class="mdi-communication-email prefix"></i> <input value="${userData.email}"
										id="el_email" type="email" class="validate" maxlength="25">
									<label for="el_email"><e:msg key="registration.email"></e:msg></label>
								</div>
								<div class="input-field col s12">
									<i class="mdi-action-lock prefix"></i> <input id="el_password"
										type="password" class="validate tooltipped"
										data-position="right" data-delay="50"
										data-tooltip="<e:msg key="registration.tooltip_pass"></e:msg>"
										maxlength="30"> <label for="el_password"><e:msg
											key="registration.password"></e:msg></label>
								</div>
								<div class="input-field col s12">
									<i class="mdi-action-lock prefix"></i> <input
										id="el_confirm_password" type="password"
										class="validate tooltipped" data-position="right"
										data-delay="50"
										data-tooltip="<e:msg key="registration.tooltip_pass"></e:msg>"
										maxlength="30"> <label for="el_password"><e:msg
											key="registration.pass_confirm"></e:msg> </label>
								</div>
								<div class="input-field col s12">
									<i class="mdi-communication-phone prefix"></i> <input
										value="${userData.phone}" id="el_phone" type="tel"
										class="validate"> <label for="el_phone"><e:msg
											key="registration.phone"></e:msg></label>
								</div>
							</div>
							<a id="butNext" onclick="selectTab('cv_tab');"
								class=" modal-action   waves-effect waves-green btn-flat right"><e:msg
									key="next"></e:msg></a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class=" col s6"></div>
			<div class=" col s6"></div>
		</div>

		<div id="cv_tab" class="col s12">
			<div class="row">
				<div class=" col s8 offset-s2 ">
					<div class="card-panel white">
						<div class="row">
							<h5>
								<e:msg key="registration.enter_cv"></e:msg>
							</h5>
							<div class="row">
								<div class="input-field col s12">
									<i class="mdi-action-event prefix"></i> <label for="birth"><e:msg
											key="registration.birth"></e:msg></label> <input id="birth" required
										type="date" class="datepicker"
										value="${userData.userBirth}"
										
										>
									<script type="text/javascript">
										$('.datepicker').pickadate({
											selectMonths : true,
											selectYears : 30,
											closeOnSelect : true,
											max : -3650,
											today : '',
										});
									</script>
								</div>

								<div class="input-field col s12 ">
									<select id="el_english_level" class="validate">
										<option value="-1" selected><e:msg
												key="select_option"></e:msg>
										</option>
										<option value="A1">A1</option>
										<option value="A2">A2</option>
										<option value="B1">B1</option>
										<option value="B2">B2</option>
										<option value="C1">C1</option>
										<option value="C2">C2</option>
									</select> <label for="el_english_level"><e:msg
											key="registration.english_level"></e:msg></label>
								</div>

								<div class="input-field col s12">
									<h7 class="center-align blue-text text-darken-2">
									<e:msg key="optional_fields"></e:msg></h7>
								</div>

								<div class="input-field col s12">
									<i class="mdi-action-assignment prefix"></i> <input
										value="${userData.education}" id="el_education" type="text"
										class="validate" maxlength="250"> <label
										for="el_education"><e:msg key="registration.education"></e:msg></label>
								</div>
								<div class="input-field col s12">
									<i class="mdi-action-assignment prefix"></i>
									<textarea id="el_objective" style="max-height: 100px"
										class="materialize-textarea validate" length="250"
										maxlength="250"></textarea>
									<label for="el_objective"><e:msg
											key="registration.objective"></e:msg> </label>
								</div>
								<div class="input-field col s12">
									<i class="mdi-action-assignment prefix"></i>
									<textarea id="el_skills" style="max-height: 100px"
										class="materialize-textarea validate" length="250"
										maxlength="250"></textarea>
									<label for="el_skills"><e:msg key="registration.skills"></e:msg></label>
								</div>
								<div class="input-field col s12">
									<i class="mdi-action-assignment prefix"></i>
									<textarea id="el_additional_info" style="max-height: 100px"
										maxlength="250" class="materialize-textarea validate"
										length="250"></textarea>
									<label for="el_additional_info"><e:msg
											key="registration.additional_info"></e:msg></label>
								</div>
							</div>
							<a id="butReg" onclick="submitRegistration()"
								class=" modal-action   waves-effect waves-green btn-flat right"><e:msg
									key="registration.register"></e:msg> </a>
							<div class="preloader-wrapper big active" style="display: none;"
								id="circle">
								<div class="spinner-layer spinner-blue-only">
									<div class="circle-clipper right">
										<div class="circle"></div>
									</div>
								</div>
							</div>
						</div>

					</div>

				</div>

			</div>
		</div>
	</div>

	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>