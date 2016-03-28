<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="e" uri="http://epam.edu/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <jsp:include page="requirements.jsp"></jsp:include>
   
   <script type="text/javascript"
	src="${pageContext.request.contextPath}/js/knockout-3.3.0.js"></script>
   
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/jquery.maskedinput.js"></script>

    <script type="text/javascript">
        var firstNameRegEx = /^([А-ЯA-ZЇІЄ])(([а-яa-zїіє]{0,16})([-–'])?([A-ZА-Яа-яa-zїіє]{1,16}))+$/;
        var lastNameRegEx = /^([А-ЯA-ZЇІЄ])(([а-яa-zїіє]{0,16})([-–'])?([A-ZА-Яа-яa-zїіє]{1,16}))+$/;
        var parentNameRegEx = /^([А-ЯA-ZЇІЄ])(([а-яa-zїіє]{0,16})([-–'])?([A-ZА-Яа-яa-zїіє]{1,16}))+$/;
       
        $(document).ready(function () {
        	document.getElementById('filePhoto').onchange = function (evt) {
        	    var tgt = evt.target || window.event.srcElement,
        	        files = tgt.files;

        	    // FileReader support
        	    if (FileReader && files && files.length&&files[0].name.match(/\.(jpg|JPG|jpeg|JPEG|png|PNG|gif|GIF|bmp|BMP)$/)) {
        	        var fr = new FileReader();
        	        fr.onload = function () {
        	            document.getElementById('profileImage').src = fr.result;
        	        }
        	        fr.readAsDataURL(files[0]);
        	    }else{
        	    	  Materialize.toast($.t('uncorrectImage'), 3000)
        	    }
        	}
        	
            $('select').material_select();
          
            $('#el_first_name').on('input blur', function () {
                var input = $(this);
                var is_valid = firstNameRegEx.test(input.val());
                if (is_valid) {
                    input.removeClass("validate invalid").addClass("validate");
                }
                else {
                    input.removeClass("validate").addClass("validate invalid");
                }
            });

            $('#el_last_name').on('input blur', function () {
                var input = $(this);
                var is_valid = lastNameRegEx.test(input.val());
                if (is_valid) {
                    input.removeClass("validate invalid").addClass("validate");
                }
                else {
                    input.removeClass("validate").addClass("validate invalid");
                }
            });

            $('#el_parent_name').on('input blur', function () {
                var input = $(this);
                var is_valid = parentNameRegEx.test(input.val());
                if (is_valid) {
                    input.removeClass("validate invalid").addClass("validate");
                }
                else {
                    input.removeClass("validate").addClass("validate invalid");
                }
            });


        });

        function submitRegistration() {
            var firstName = $('#el_first_name').val();
            var lastName = $('#el_last_name').val();
            var parentName = $('#el_parent_name').val();
            var allOk=1;

            if(!firstNameRegEx.test(firstName)) {
                $("#circle").hide();
                Materialize.toast($.t('reg_fname_error'), 3000);
                allOk=2;
            } else if(!lastNameRegEx.test(lastName)) {
                $("#circle").hide();
                Materialize.toast($.t('reg_lname_error'), 3000);
                allOk=2;
            } else if(!parentNameRegEx.test(parentName)) {
                $("#circle").hide();
                Materialize.toast($.t('reg_pname_error'), 3000);
                allOk=2;
            } else	if(typeof  $('#filePhoto')[0].files[0] != 'undefined')
            	if(!$('#filePhoto')[0].files[0].name.match(/\.(jpg|JPG|jpeg|JPEG|png|PNG|gif|GIF|bmp|BMP)$/)) {  
	                $("#circle").hide();
	                Materialize.toast($.t('badPhotoFormat'), 3000);
	                allOk=2;
            	}
            
            if(allOk==1) {
                var formData = new FormData();
                formData.append('fname', firstName);
                formData.append('lname', lastName);
                formData.append('pname', parentName);
                if(typeof  $('#filePhoto')[0].files[0] != 'undefined')
                formData.append('filePhoto', $('#filePhoto')[0].files[0]);

                $.ajax({
                    type: 'POST',
                    url: "AjaxController?command=updateUserInfo",
                    dataType: "xml",
                    data: formData,
                    async: true,
                    cache: false,
                    contentType: false,
                    processData: false,
                    complete: function (data) {
                        processEditResult(data);
                    }
                });
            }
        }

        function processEditResult(data) {
        	var xml = data.responseXML;
            var response = $(xml).find('result').find('status').text();
          if (response == 1) {
                Materialize.toast($.t('changes_saved'), 3000)
                var cabinet_url = $(xml).find('result').find('cabinet_url').text();
    				var url = "/EpamEducationalProject/Controller?command=redirect&direction=" + cabinet_url ;
    			window.location.href = url;
            } else if (response == 2) {
                Materialize.toast($.t('reg_email_already_exist'), 3000)
            } else if (response == 3) {
                Materialize.toast($.t('reg_pass_dont_match'), 3000)
            } else if (response == 4) {
                Materialize.toast($.t('reg_some_value_is_invalid'), 3000)
            } else if (response == 5) {
                Materialize.toast($.t('badPhotoFormat'), 3000)
            } else if (response == 6) {
                Materialize.toast($.t('incorrectSize'), 3000)
            } 
        }
    </script>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
    <title>Edit info</title>

</head>


<body bgcolor="#FAFAFA"  id="editingProfile">

<jsp:include page="header.jsp"></jsp:include>

<c:if test="${logined_user.roleType == 'ADMIN'}">
<jsp:include page="admin/admin_panel_sidenav.jsp"></jsp:include>
</c:if>

<c:if test="${logined_user.roleType == 'TEACHER'}">
<jsp:include page="teacher/teacher_panel_sidenav.jsp"></jsp:include>
</c:if>

<c:if test="${logined_user.roleType == 'HR'}">
<jsp:include page="hr/hr_panel_sidenav.jsp"></jsp:include>
</c:if>

<fmt:setLocale value="en_US" scope="session"/>

<div class="col">
    <div class="row">
        <div class=" col s8 offset-s2 ">
            <div class="card-panel white">
                <div class="row">
                    <h5><e:msg key="user.contactData"></e:msg></h5>

					<div class="input-field col s4" >
					<img id="profileImage"  src="${pageContext.request.contextPath}/images?type=users&id=${logined_user.id}" height="200" width="250">
					<div class="file-field input-field" >
						<input class="file-path validate" type="text" disabled/>
						<div class="btn">
							<input type="file" 
								id="filePhoto"> <span><e:msg key="file"></e:msg></span>
						</div>
					</div>
				</div>

                  
                    <div class="input-field col s4">
                        <i class="mdi-image-timer-auto prefix"></i>
                        <input name="lastName" id="el_last_name" type="text" class="validate"
                               value="<c:out value="${logined_user.lastName}" />" maxlength="30">
                        <label for="el_last_name" class="active"><e:msg key="user.lname"></e:msg>:</label>
                    </div>
                    <div class="input-field col s4">
                        <i class="mdi-image-timer-auto prefix"></i>
                        <input name="firstName" id="el_first_name" type="text" class="validate"
                               value="<c:out value="${logined_user.firstName}" />" maxlength="30">
                        <label for="el_first_name" class="active"><e:msg
                                key="user.fname"></e:msg>:</label>
                    </div>
                    <div class="input-field col s4">
                        <i class="mdi-image-timer-auto prefix"></i>
                        <input name="parentName" id="el_parent_name" type="text" class="validate"
                               value="<c:out value="${logined_user.parentName}" />" maxlength="30">
                        <label for="el_parent_name" class="active"><e:msg key="user.pname"></e:msg>:</label>
                    </div>

                    <div class="input-field col s12 ">
                        <a href="${pageContext.request.contextPath}/Controller?command=redirect&direction=change_password" class="waves-effect waves-light btn"><e:msg key="user.changePassword"></e:msg></a>
                    </div>

                    <a id="butReg" onclick="submitRegistration()"
                       class=" modal-action   waves-effect waves-green btn-flat right"><e:msg key="save"></e:msg></a>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>