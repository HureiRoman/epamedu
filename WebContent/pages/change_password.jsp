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

      $('.tooltipped').tooltip({delay: 50});


      $('#el_new_password').on('input blur', function() {
        var input = $(this);
        var confirmPass = $('#el_confirm_new_password');
        var re = /^\D[^<>]{4,30}$/;
        var is_valid = re.test(input.val());

        if(is_valid && input.val() == confirmPass.val()){
          input.removeClass("invalid").addClass("valid");
          confirmPass.removeClass("invalid").addClass("valid");
        }
        else{
          input.removeClass("valid").addClass("invalid");
          confirmPass.removeClass("valid").addClass("invalid");
        }
      });

      $('#el_confirm_new_password').on('input blur', function() {
        var input = $(this);
        var confirmPass = $('#el_new_password');
        var re = /^\D[^<>]{4,30}$/;
        var is_valid = re.test(input.val());

        if(is_valid && input.val() == confirmPass.val()){
          input.removeClass("invalid").addClass("valid");
          confirmPass.removeClass("invalid").addClass("valid");
        }
        else{
          input.removeClass("valid").addClass("invalid");
          confirmPass.removeClass("valid").addClass("invalid");
        }
      });
    });

    function submitChangePassword() {
      var re = /^\D[^<>]{4,30}$/;
      var oldPassword = $('#el_old_password').val();
      var newPassword = $('#el_new_password').val();
      var newPasswordRepeat = $('#el_confirm_new_password').val();
      if(oldPassword.length == 0 || newPassword.length == 0 || newPasswordRepeat.length == 0) {
        Materialize.toast($.t("pass_enter_all_fields"), 3000)
      }
      else if(newPassword != newPasswordRepeat) {
        Materialize.toast($.t("reg_pass_dont_match"), 3000)
      } else if(!re.test(newPassword)) {
        Materialize.toast($.t("pass_incorrect_pass"), 3000)
      } else {

          $.ajax({
            type : 'POST',
            url : "AjaxController",
            dataType : "xml",
            data : {
              'new_password' : newPassword,
              'command' : 'changePassword',
              'confirm_new_password' : newPasswordRepeat,
              'old_password' : oldPassword
            },
            complete : function(data) {
              processChangePasswordResult(data);
            }
          });
        }
      }

    function processChangePasswordResult(data) {
      var response = data.responseXML.documentElement.firstChild.nodeValue;
      if (response == 1) {
        Materialize.toast($.t("pass_saved"), 3000)
        var url;
        var role='${logined_user.roleType}'.toLowerCase();
        if(role=='hr') url='command=redirect&direction=hr_cabinet';
        else if(role=='student') url='command=student';
        else if(role=='trainee') url='command=redirect&direction=TRAINEE_CABINET_PAGE';
         else if(role=='graduate') url='command=redirect&direction=TRAINEE_CABINET_PAGE';
        else if(role=='teacher') url='command=redirect&direction=teacher_cabinet';
        setTimeout(function() {window.location="/EpamEducationalProject/Controller?"+url; }, 600);
      } else if (response == 2) {
        Materialize.toast($.t("pass_enter_all_fields"), 3000)
      } else if (response == 3) {
        Materialize.toast($.t("reg_pass_dont_match"), 3000)
      } else if (response == 4) {
        Materialize.toast($.t("pass_bad_old_pass"), 3000)
      } else if (response == 5) {
        Materialize.toast($.t("pass_bad_length"), 3000)
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
      <div class="card-panel white">
        <div class="row">
          <%--<form action="AjaxController?command=changePassword">--%>
          <div class="input-field col s12">
            <i class="mdi-action-lock prefix"></i> <input id="el_old_password"
                                                          type="password"> <label
                  for="el_old_password"><e:msg key="old_password"></e:msg></label>
          </div>

          <div class="input-field col s12">
            <i class="mdi-action-lock prefix"></i>
            <input class="validate tooltipped" id="el_new_password" type="password"
                   data-position="right" data-delay="50" data-tooltip="<e:msg key="registration.tooltip_pass"></e:msg>"> <label
                  for="el_new_password"><e:msg key="new_password"></e:msg></label>
          </div>

          <div class="input-field col s12">
            <i class="mdi-action-lock prefix"></i>
            <input
                  class="validate tooltipped" id="el_confirm_new_password" type="password"
                  data-position="right" data-delay="50" data-tooltip="<e:msg key="registration.tooltip_pass"></e:msg>">
            <label for="el_confirm_new_password"><e:msg key="pass_confirm"></e:msg> </label>
          </div>
            <a id="butReg" onclick="submitChangePassword()"
               class=" modal-action   waves-effect waves-green btn-flat right"><e:msg key="save"></e:msg></a>
        </div>
      </div>
    </div>
  </div>
</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>