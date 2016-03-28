<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="e" uri="http://epam.edu/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <jsp:include page="requirements.jsp"></jsp:include>
  
  <script type="text/javascript">
    $(document).ready(function () {
      $('#el_new_password').on('input blur', function() {
        var input = $(this);
        var confirmPass = $('#el_confirm_new_password');
        var re = /^\D[^<>]{4,30}$/;
        var is_valid = re.test(input.val());

        if(is_valid && input.val() == confirmPass.val()){
          input.removeClass("validate invalid").addClass("validate");
          confirmPass.removeClass("validate invalid").addClass("validate");
        }
        else{
          input.removeClass("validate").addClass("validate invalid");
          confirmPass.removeClass("validate").addClass("validate invalid");
        }
      });

      $('#el_confirm_new_password').on('input blur', function() {
        var input = $(this);
        var confirmPass = $('#el_new_password');
        var re = /^\D[^<>]{4,30}$/;
        var is_valid = re.test(input.val());

        if(is_valid && input.val() == confirmPass.val()){
          input.removeClass("validate invalid").addClass("validate");
          confirmPass.removeClass("validate invalid").addClass("validate");
        }
        else{
          input.removeClass("validate").addClass("validate invalid");
          confirmPass.removeClass("validate").addClass("validate invalid");
        }
      });
    });

    function submitRetrieving() {
      var newPassword = $('#el_new_password').val();
      var newPasswordRepeat = $('#el_confirm_new_password').val();
      var email=$('#myEmail').val();
      if (newPassword.length != 0 && newPasswordRepeat != 0) {
      
        $.ajax({
          type : 'POST',
          url : "AjaxController",//changed by Mordecai
          dataType : "xml",
          data : {
            'newPassword' : newPassword,
            'command' : 'retrievePassword',
            'confirmNewPassword' : newPasswordRepeat,
            'myEmail' : email,
            },
          complete : function(data) {
            processChangePasswordResult(data);
          }
        });
      }
    }

    function processChangePasswordResult(data) {
    	var xml = data.responseXML;
    	
    	  var response = $(xml).find('result').find('status').text();
          if (response == 1) {
            Materialize.toast($.t("passwordSaved"), 3000)
          } else if (response == 2) {
            Materialize.toast($.t("enterAllData"), 3000)
          } else if (response == 3) {
            Materialize.toast($.t("reg_pass_dont_match"), 3000)
          } else if (response == 4) {
            Materialize.toast($.t("reg_pass_error"), 3000)
          } else if (response == 5) {
            Materialize.toast($.t("shortPassword"), 3000)
          }
    	if(response==1) {
			var cabinet_url = $(xml).find('result').find('cabinet_url').text();
			var url = "/EpamEducationalProject/Controller?command=redirect&direction=";
			window.location.href = url + cabinet_url;
    	}
    }
  </script>
  <meta name="viewport"
        content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
  <title>Change Password</title>

</head>


<body bgcolor="#FAFAFA">

<jsp:include page="header.jsp"></jsp:include>

<c:set var="trainee" value="${sessionScope.logined_user}"/>

<div class="col">
  <div class="row">
    <div class=" col s8 offset-s2 ">
    <br>
    <br>
      <div class="card-panel white">
        <div class="row">

          <div class="input-field col s12">
            <i class="mdi-action-lock prefix"></i> <input id="el_new_password"
                                                          type="password" class="validate"> <label
                  for="el_new_password"><e:msg key="newPass"></e:msg></label>
          </div>
           <div class="input-field col s12">
			<input type="hidden" value="${email }" id="myEmail" name="email">
			</div>
          <div class="input-field col s12">
            <i class="mdi-action-lock prefix"></i> <input
                  id="el_confirm_new_password" type="password" class="validate">
            <label for="el_confirm_new_password"><e:msg key="passAgain"></e:msg></label>
          </div>
            <a id="butReg" onclick="submitRetrieving()"
               class=" modal-action   waves-effect waves-green btn-flat right"><e:msg key="save"></e:msg></a>
        </div>
      </div>
    </div>
  </div>
</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>