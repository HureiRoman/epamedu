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
        var passwordRegEx = /^\D[^<>]{4,30}$/;
        var phoneRegEx = /\(?([0-9]{3})\)?([ .-]?)([0-9]{3})\2([0-9]{4})$/;
        var infoRegEx = /^[^<>]{0,250}$/;
        var emailRegEx = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;

        $(document).ready(function () {
        	
        	document.getElementById('filePhoto').onchange = function (evt) {
        	    var tgt = evt.target || window.event.srcElement,
        	        files = tgt.files;

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
            $('#el_phone').mask('(999)-999-9999');

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


            $('#el_phone').on('input blur', function () {
                var input = $(this);
                var is_valid = phoneRegEx.test(input.val());
                if (is_valid) {
                    input.removeClass("validate invalid").addClass("validate");
                }
                else {
                    input.removeClass("validate").addClass("validate invalid");
                }
            });

            $('#el_objective').on('input blur', function () {
                var input = $(this);
                var is_valid = infoRegEx.test(input.val());
                if (is_valid) {
                    input.removeClass("validate invalid").addClass("validate");
                }
                else {
                    input.removeClass("validate").addClass("validate invalid");
                }
            });

            $('#el_additional_info').on('input blur', function () {
                var input = $(this);
                var is_valid = infoRegEx.test(input.val());
                if (is_valid) {
                    input.removeClass("validate invalid").addClass("validate");
                }
                else {
                    input.removeClass("validate").addClass("validate invalid");
                }
            });


            $('#el_skills').on('input blur', function () {
                var input = $(this);
                var is_valid = infoRegEx.test(input.val());
                if (is_valid) {
                    input.removeClass("validate invalid").addClass("validate");
                }
                else {
                    input.removeClass("validate").addClass("validate invalid");
                }
            });

            $('#el_education').on('input blur', function () {
                var input = $(this);
                var is_valid = infoRegEx.test(input.val());
                if (is_valid) {
                    input.removeClass("validate invalid").addClass("validate");
                }
                else {
                    input.removeClass("validate").addClass("validate invalid");
                }
            });
        });
        
        function removeMultipleWhiteSpaces(value){
        	   return value.replace(/\s\s+/g, ' ');  	  
        	 }
        
        function submitEdition() {
            var firstName = $('#el_first_name').val();
            var lastName = $('#el_last_name').val();
            var parentName = $('#el_parent_name').val();
            var phone = $('#el_phone').val();
            var englishLevel = $('#el_english_level').val();
            var education = $('#el_education').val();
            var birth = $('#birth').val();
            var objective = removeMultipleWhiteSpaces($('#el_objective').val());
            var skills = removeMultipleWhiteSpaces($('#el_skills').val());
            var role=$('#user_role').val();
            var additionalInfo = removeMultipleWhiteSpaces($('#el_additional_info').val());
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
            } else if(!phoneRegEx.test(phone)) {
                $("#circle").hide();
                Materialize.toast($.t('reg_phone_error'), 3000);
                allOk=2;
            } else if(!infoRegEx.test(education)) {
                $("#circle").hide();
                Materialize.toast($.t('reg_education_error'), 3000);
                allOk=2;
            } else if(!infoRegEx.test(objective)) {
                $("#circle").hide();
                Materialize.toast($.t('reg_objective_error'), 3000);
                allOk=2;
            } else if(!infoRegEx.test(skills)) {
                $("#circle").hide();
                Materialize.toast($.t('reg_skills_error'), 3000);
                allOk=2;
            } else if(!infoRegEx.test(additionalInfo)) {
                $("#circle").hide();
                Materialize.toast($.t('reg_additional_info_error'), 3000);
                allOk=2;
            }  else if(typeof  $('#filePhoto')[0].files[0] != 'undefined') if(!$('#filePhoto')[0].files[0].name.match(/\.(jpg|JPG|jpeg|JPEG|png|PNG|gif|GIF|bmp|BMP)$/)) {  
                $("#circle").hide();
                Materialize.toast($.t('badPhotoFormat'), 3000);
                allOk=2;
            }
            if(allOk==1)  {
                var formData = new FormData();
                formData.append('fname', firstName);
                formData.append('lname', lastName);
                formData.append('pname', parentName);
                formData.append('phone', phone);
                formData.append('birth', birth);
                formData.append('objective', objective);
                formData.append('education', education);
                formData.append('skills', skills);
                formData.append('role', role);
                formData.append('additional_info', additionalInfo);
                formData.append('english_level', englishLevel);
                if(typeof  $('#filePhoto')[0].files[0] != 'undefined')
                formData.append('filePhoto', $('#filePhoto')[0].files[0]);

                $.ajax({
                    type: 'POST',
                    url: "AjaxController?command=updateUserInfoWithCV",
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
        	    Materialize.toast($.t('changes_saved'), 1000);
        	    var cabinet_url = $(xml).find('result').find('cabinet_url').text();
        	    if(cabinet_url == 'student_cabinet'){
    				var url = "/EpamEducationalProject/Controller?command=student";
    			}else{
    				var url = "/EpamEducationalProject/Controller?command=redirect&direction=" + cabinet_url ;
    			}
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



<fmt:setLocale value="en_US" scope="session"/>

<div class="col">
    <div class="row">
        <div class=" col s8 offset-s2 ">
            <div class="card-panel white">
                <div class="row">
                    <h5><e:msg key="user.contactData"></e:msg></h5>
					<div class="input-field col s4" >
					<img id="profileImage" src="${pageContext.request.contextPath}/images?type=users&id=${logined_user.id}" height="200" width="250">
					<div class="file-field input-field" >
						<input class="file-path validate" type="text" disabled/>
						<div class="btn">
							<input   type="file" 
								id="filePhoto"> <span><e:msg key="file"></e:msg></span>
						</div>
					</div>
				</div>
                    <div class="input-field col s4">
                        <i class="mdi-image-timer-auto prefix"></i>
                        <input name="lastName" id="el_last_name" type="text" class="validate"
                               value="<c:out value="${logined_user.lastName}" />"  maxlength="30">
                        <label for="el_last_name" class="active"><e:msg key="user.lname"></e:msg>:</label>
                    </div>
                    <div class="input-field col s4">
                        <i class="mdi-image-timer-auto prefix"></i>
                        <input name="firstName" id="el_first_name" type="text" class="validate"
                               value="<c:out value="${logined_user.firstName}" />"  maxlength="30">
                        <label for="el_first_name" class="active"><e:msg
                                key="user.fname"></e:msg>:</label>
                    </div>
                    
                      <input name="role" id="user_role" type="hidden"   value="<c:out value="${logined_user.roleType}" />" maxlength="30">
                    
                    <div class="input-field col s4">
                        <i class="mdi-image-timer-auto prefix"></i>
                        <input name="parentName" id="el_parent_name" type="text" class="validate"
                               value="<c:out value="${logined_user.parentName}" />">
                        <label for="el_parent_name" class="active"><e:msg key="user.pname"></e:msg>:</label>
                    </div>
                    
                    
                    
                    
                    <div class="input-field col s12">
                        <i class="mdi-communication-phone prefix"></i>
                        <input id="el_phone" type="tel" class="validate" value="<c:out value="${logined_user.cv.phone}" />"  maxlength="20">
                        <label for="el_phone" class="active"><e:msg key="phone"></e:msg>:</label>
                    </div>
                    <div class="input-field col s12">
                        <i class="mdi-action-event prefix"></i>
                        <label for="birth" class="active"><e:msg key="user.dateOfBirth"></e:msg></label> <input id="birth"
                                                                                         required type="date"
                                                                                         class="datepicker"
                                                                                         value="<fmt:formatDate type="date" pattern="dd MMMM, yyyy"
                        value="${logined_user.cv.birth}"/>"/  maxlength="30">
                        <script type="text/javascript">
                            $('.datepicker').pickadate({
                                selectMonths : true,
                                selectYears : 30,
                                closeOnSelect: true,
                                max: -3650,
                                today: '',
                            });
                        </script>
                    </div>

                    <div class="input-field col s12 ">
                        <select id="el_english_level" style="padding-top:0px !important; padding-bottom:0px !important; font-size:12px !important;">
                            <option value="<c:out value="${logined_user.cv.englishLevel}"/>"><c:out
                                    value="${logined_user.cv.englishLevel}"/></option>
                            <option value="A1" style="border:0px !important; padding-bottom:0px !important; font-size:12px !important;">A1</option>
                            <option value="A2">A2</option>
                            <option value="B1">B1</option>
                            <option value="B2">B2</option>
                            <option value="C1">C1</option>
                            <option value="C2">C2</option>
                        </select>
                        <label for="el_english_level"><e:msg key="user.englishLevel"></e:msg></label>
                    </div>

                    <div class="input-field col s12">
                        <i class="mdi-action-assignment prefix"></i>
                        <input id="el_education" type="text" class="validate"
                               value="<c:out value="${logined_user.cv.education}"/>"  maxlength="255">
                        <label for="el_education" class="active"><e:msg key="user.education"></e:msg></label>
                    </div>

                    <div class="input-field col s12">
                        <i class="mdi-action-assignment prefix"></i>
										<textarea id="el_objective" class="materialize-textarea validate"
                                                maxlength="255" style="max-height:100px"> <c:out value="${logined_user.cv.objective}"/></textarea>
                        <label for="el_objective" class="active"><e:msg key="user.motivation"></e:msg></label>
                    </div>

                    <div class="input-field col s12">
                        <i class="mdi-action-assignment prefix"></i>
										<textarea id="el_skills" class="materialize-textarea validate"
                                                 maxlength="255" style="max-height:100px"><c:out value="${logined_user.cv.skills}"/></textarea>
                        <label for="el_skills" class="active"><e:msg key="user.skills"></e:msg></label>
                    </div>

                    <div class="input-field col s12">
                        <i class="mdi-action-assignment prefix"></i>
										<textarea id="el_additional_info" class="materialize-textarea validate"
                                                  maxlength="255" style="max-height:100px"><c:out value="${logined_user.cv.additionalInfo}"/></textarea>
                        <label for="el_additional_info" class="active"><e:msg key="user.additionalInfo"></e:msg></label>
                    </div>


                    <div class="input-field col s12 ">
                        <a href="${pageContext.request.contextPath}/Controller?command=redirect&direction=change_password" class="waves-effect waves-light btn"><e:msg key="user.changePassword"></e:msg></a>
                    </div>

                    <a id="butReg" onclick="submitEdition()"
                       class=" modal-action   waves-effect waves-green btn-flat right"><e:msg key="save"></e:msg></a>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>