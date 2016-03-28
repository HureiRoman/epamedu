<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<jsp:include page="requirements.jsp"></jsp:include>


<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />

<title><e:msg key="employeeRegistration"></e:msg></title>

<script type="text/javascript">
	function sleep(milliseconds) {
		var start = new Date().getTime();
		for (var i = 0; i < 1e7; i++) {
			if ((new Date().getTime() - start) > milliseconds) {
				break;
			}
		}
	}
	var firstNameRegEx = /^([А-ЯA-ZЇІЄ])(([а-яa-zїіє]{0,16})([-–'])?([A-ZА-Яа-яa-zїіє]{1,16}))+$/;
	var lastNameRegEx = /^([А-ЯA-ZЇІЄ])(([а-яa-zїіє]{0,16})([-–'])?([A-ZА-Яа-яa-zїіє]{1,16}))+$/;
	var parentNameRegEx = /^([А-ЯA-ZЇІЄ])(([а-яa-zїіє]{0,16})([-–'])?([A-ZА-Яа-яa-zїіє]{1,16}))+$/;
	var passwordRegEx = /^\D[^<>]{4,30}$/;

	$(document).ready(
			function() {
				$('select').material_select();

				$('#el_first_name').on(
						'input blur',
						function() {
							var input = $(this);
							var is_valid = firstNameRegEx.test(input.val());
							if (is_valid) {
								input.removeClass("validate invalid").addClass(
										"validate");
							} else {
								input.removeClass("validate").addClass(
										"validate invalid");
							}
						});

				$('#el_last_name').on(
						'input blur',
						function() {
							var input = $(this);
							var is_valid = lastNameRegEx.test(input.val());
							if (is_valid) {
								input.removeClass("validate invalid").addClass(
										"validate");
							} else {
								input.removeClass("validate").addClass(
										"validate invalid");
							}
						});

				$('#el_parent_name').on(
						'input blur',
						function() {
							var input = $(this);
							var is_valid = parentNameRegEx.test(input.val());
							if (is_valid) {
								input.removeClass("validate invalid").addClass(
										"validate");
							} else {
								input.removeClass("validate").addClass(
										"validate invalid");
							}
						});

				$('#el_password').on(
						'input blur',
						function() {
							var input = $(this);
							var confirmPass = $('#el_confirm_password');
							var is_valid = passwordRegEx.test(input.val());

							if (is_valid && input.val() == confirmPass.val()) {
								input.removeClass("validate invalid").addClass(
										"validate");
								confirmPass.removeClass("validate invalid")
										.addClass("validate");
							} else {
								input.removeClass("validate").addClass(
										"validate invalid");
								confirmPass.removeClass("validate").addClass(
										"validate invalid");
							}
						});

				$('#el_confirm_password').on(
						'input blur',
						function() {
							var input = $(this);
							var confirmPass = $('#el_password');
							var is_valid = passwordRegEx.test(input.val());

							if (is_valid && input.val() == confirmPass.val()) {
								input.removeClass("validate invalid").addClass(
										"validate");
								confirmPass.removeClass("validate invalid")
										.addClass("validate");
							} else {
								input.removeClass("validate").addClass(
										"validate invalid");
								confirmPass.removeClass("validate").addClass(
										"validate invalid");
							}
						});

			});

	function selectTab(tabname) {
		$(document).ready(function() {
			$('ul.tabs').tabs('select_tab', tabname);
			$('html, body').animate({
				scrollTop : 0
			}, 'fast');
		});
	}

	function getGet(name) {
		var s = window.location.search;
		s = s.match(new RegExp(name + '=([^&=]+)'));
		return s ? s[1] : false;
	}

	function updateProfileData() {

		var email = getGet("user");
		var firstName = $('#el_first_name').val();
		var lastName = $('#el_last_name').val();
		var parentName = $('#el_parent_name').val();
		var password = $('#el_password').val();
		var confirmPass = $('#el_confirm_password').val();

		if(firstName.length == 0 || lastName.length == 0 || password.length == 0
				|| confirmPass.length == 0) {
			$("#circle").hide();
			Materialize.toast($.t("reg_enter_all_required_data") , 3000);
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
		} else {
			$("#circle").show();
			var formData = new FormData();
			formData.append('act', 'reg');
			formData.append('email', email);
			formData.append('fname', firstName);
			formData.append('lname', lastName);
			formData.append('pname', parentName);
			formData.append('password', password);
			formData.append('password_repeat', confirmPass);
			$.ajax({
						type : 'POST',
						url : "AjaxController?command=registrationEmployee",
						dataType : "xml",
						data : formData,
						async : true,
						cache : false,
						contentType : false,
 						processData : false,
						complete : function(data) {
							processUpdatingProfileData(data);
						}
					});
		}
	}

	function processUpdatingProfileData(data) {
		var response = data.responseXML;
		console.log("tyt = "+$(response).find("status"));
		console.log(response);
		var status = $(response).find("data").find("status").text();
		var cabinet = $(response).find("data").find("cabinet_url").text();

		if (status == 1) {
			Materialize.toast($.t("reg_data_updated"), 3000);
			if(cabinet == 'student_cabinet'){
				var url = "/EpamEducationalProject/Controller?command=student";
			}else{
				var url = "/EpamEducationalProject/Controller?command=redirect&direction=" + cabinet ;
			}

			window.location.href = url;
		} else  {
			Materialize.toast($.t("reg_pass_dont_match"), 3000);
			$("#circle").hide();
		} 
	}
</script>
</head>
<body>

	<jsp:include page="header.jsp"></jsp:include>

	<div class="col">

		<div id="contact_tab" class="col s12">
			<div class="row">
				<div class=" col s8 offset-s2 ">
					<div class="card-panel white">
						<div class="row">
							<h5><e:msg
											key="completePersonalData"></e:msg></h5>
							<div class="row">
								<div class="input-field col s4">
									<i class="mdi-image-timer-auto prefix"></i> <input
										name="lastName" maxlength="20" id="el_last_name" type="text" class="validate">
									<label for="el_last_name"><e:msg key="user.lname"></e:msg></label>
								</div>
								<div class="input-field col s4">
									<i class="mdi-image-timer-auto prefix"></i> <input
										name="firstName" maxlength="20" id="el_first_name" type="text"
										class="validate"> <label for="el_first_name"><e:msg
											key="user.fname"></e:msg></label>
								</div>
								<div class="input-field col s4">
									<i class="mdi-image-timer-auto prefix"></i> <input
										name="parentName" maxlength="20" id="el_parent_name" type="text"
										class="validate"> <label for="el_parent_name"><e:msg
											key="user.pname"></e:msg></label>
								</div>
							</div>
							<div class="row">
								<div class="input-field col s12">
									<i class="mdi-action-lock prefix"></i> <input id="el_password"
										type="password"  maxlength="20" class="validate"> <label
										for="el_password"><e:msg key="registration.password"></e:msg></label>
								</div>
								<div class="input-field col s12">
									<i class="mdi-action-lock prefix"></i> <input
										id="el_confirm_password" maxlength="20" type="password" class="validate">
									<label for="el_password"><e:msg key="registration.pass_confirm"></e:msg></label>
								</div>
								<a id="butReg" onclick="updateProfileData()"
									class=" modal-action   waves-effect waves-green btn-flat right"><e:msg key="confirm"></e:msg></a>

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