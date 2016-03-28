<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../requirements.jsp"></jsp:include>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/knockout-3.3.0.js"></script>

	<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/pagination/jquery.quick.pagination.min.js"></script>
	<link href="${pageContext.request.contextPath}/css/pagination/styles.css"
		  type="text/css" rel="stylesheet" media="screen,projection"/>

<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<script type="text/javascript">
      function checkEmail(email) {
	    $.ajax({
	     	type : 'POST',
	    	url : "AjaxController?command=checkEmail",
	    	dataType : "xml",
		    data: {
			'email': email,
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


	$(document).ready(function () {
		$('ul#employees').quickPagination({pageSize: "20"});
		$('#employee_mail').on('input blur', function() {
			checkEmail($(this).val());
		});
	});
    var emailRegEx = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
	var currentNewsItemId;
	function addNewEmployee() {
		var email = $('#employee_mail').val();
		var role = $('#employee_role').val();
		
		if(emailRegEx.test(email)){
			$
			.ajax({
				type : 'POST',
				url : "AjaxController?command=addNewEmployee",
				data : {
					'email':email,
					'role': role
				},
				dataType : "xml",
				async : true,
				complete : function(data) {
					var response = data.responseXML.documentElement.firstChild.nodeValue;
					if (response == 1) {
					   Materialize.toast($.t('invitation_was_sent'), 3000);
					   location.reload();
					} else if (response == 2) {
						   Materialize.toast($.t('userWithEmailExist'), 3000);
						    } 
					       else {
						Materialize.toast($.t('invitation_sending_error'), 3000);
					}
				}
			});
		}else{
			Materialize.toast($.t('incorrect_mail'), 3000);
		}
			
		}

	function setEmployeeActive(id, active) {
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=setUserActive",
					dataType : "xml",
					data : {
						"id" : id,
						"active" : active
					},
					complete : function(data) {
						var response = data.responseXML.documentElement.firstChild.nodeValue;
						if (response == 1) {
							if (Boolean(active)) {
								Materialize.toast($.t('employee_activated'), 3000);
								$('#userStatus'+id).empty().text();
							} else {
								Materialize.toast($.t('employee_deactivated'), 3000);
								$('#userStatus'+id).empty().text(' '+$.t('employee_deactivated'));
							}
						} else {
							Materialize.toast($.t('error_while_emp_status_changing'),
									3000);
						}
					}
				});
	}
	
	function openAddNewEmployeeModal(){
		$('#employee_mail').val('');
		$('#addNewsEmployeeModal').openModal();
	}
</script>
<title>Employee</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="admin_panel_sidenav.jsp"></jsp:include>
	<div class="container">
		<div class="collection with-header">
			<div class="collection-header">
				<div class="row">
					<div class="col s10">
						<h4><i class="fa  fa-users  medium blue-text"></i><e:msg key="employees"></e:msg></h4>
					</div>
					<div class="col s2">
						<div style="margin-top: 10px">
							<a onclick="openAddNewEmployeeModal()"
								class="btn-floating btn-large waves-effect right waves-light green"><i
								class="mdi-content-add"></i></a>
						</div>
					</div>

				</div>

			</div>
			<ul id="employees">
			 <c:forEach items="${employees}" var="employee">
				<e:employee employee="${employee}"></e:employee >
			</c:forEach>
			</ul>
		</div>
	</div>
	<jsp:include page="../footer.jsp"></jsp:include>
	<div id="addNewsEmployeeModal" class="modal"
		style="max-height: 680px !important;">
		<div class="modal-content">
			<h6>
				<i class="small mdi-action-info text-teal"></i><e:msg key="employees_adding"></e:msg>
			</h6>
			<div class="row">
				<div class="input-field">
						<select id="employee_role">
							<option disabled="disabled" value="-1"><e:msg key="please_choose_role"></e:msg></option>
							<option  value="TEACHER"><e:msg key="teacher_role"></e:msg></option>
							<option  value="HR"><e:msg key="hr_role"></e:msg></option>							
						</select>
						<label><e:msg key="role"></e:msg></label>
					</div>
					<div class="input-field ">
						<i class="mdi-communication-email prefix"></i> <input
							id="employee_mail" type="email" class="validate" maxlength="50"> <label
							for="employee_mail">E-Mail</label>
					</div>
				
			</div>
		</div>
		<div class="modal-footer">
			<a id="butLogIn" onclick="addNewEmployee();"
				class=" modal-action  waves-effect waves-green btn-flat left"><e:msg key="teacher.add"></e:msg></a>
		</div>
	</div>
</body>
</html>