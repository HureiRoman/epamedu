<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<script type="text/javascript">
	$(document).ready(function() {
		$('#modal_el_password').keypress(function(e) {
			var key = e.which;
			if (key == 13) // the enter key code
			{
				$('#butLogIn').click();
				return false;
			}
		});
	});
	function submitUser() {
		var email = $('#modal_el_email').val();
		var password = $('#modal_el_password').val();

		if ((validateEmail(email)) && (password.length >= 6)) {
			$.ajax({
				type : 'POST',
				url : "AjaxController",
				dataType : "xml",
				data : {
					"command" : "login",
					"email" : email,
					"password" : password,
				},
				complete : function(data) {
					processLoginResult(data);
				}
			});
		} else {
				if (!(validateEmail(email))) {
				Materialize.toast($.t('enterValidEmail'), 1000);
			}
				else
			if (password.length < 6) {
				Materialize.toast($.t('shortPass'), 1000);
			}
		}
	}
	function processLoginResult(data) {
		var xml = data.responseXML;
		var response = $(xml).find('result').find('status').text();
		if (response == 1) {
			// 			Materialize.toast('ви залоговані', 3000)
			var cabinet_url = $(xml).find('result').find('cabinet_url').text();
			if (cabinet_url == 'student_cabinet') {
				var url = "/EpamEducationalProject/Controller?command=student";
			} else {
				var url = "/EpamEducationalProject/Controller?command=redirect&direction="
						+ cabinet_url;
			}
			window.location.href = url;

		} else if ((response == 2) || (response == 0)) {

			Materialize.toast($.t('incorrectData'), 3000)
			//	window.location.href = "cabinet?act=created";
		} else if (response == 3) {
			Materialize.toast($.t('youAreNotConfirmed'), 3000)
			//	window.location.href = "cabinet?act=created";
		}
	}
	function validateEmail(email) {
		var re = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
		return re.test(email);
	}
	function openModalWithPasswordRecovery() {
		$('#email').val('');
		$('#passwordRetrievalModal').openModal();
	}

	function retrievePassword() {
		var email = $('#email').val();
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "passwordRetrival",
				"email" : email,
			},
			complete : function(data) {
				processRetrieveResult(data);
			}
		});
	}
	function processRetrieveResult(data) {
		var response = data.responseXML.documentElement.firstChild.nodeValue;
		$('#passwordRetrievalModal').leanModal({
			dismissible : true,
		});
		Materialize.toast(response, 3000);
		if (response == 'successfully sended') {
			setTimeout(function() {
				window.location = "/EpamEducationalProject/";
			}, 600);
		}
	}
</script>
<div id="logingModal" class="modal">

	<div class="modal-content">
		<h5>
			<i class="large mdi-action-info text-teal"></i>
			<e:msg key="pleaseLogin"></e:msg>
		</h5>
		<div class="row">
			<div class="input-field col s12">
				<i class="mdi-communication-email prefix"></i> <input
					id="modal_el_email" name="email" type="email" class="validate">
				<label for="modal_el_email">Email</label>
			</div>
			<div class="input-field col s12">
				<i class="mdi-action-lock prefix"></i> <input id="modal_el_password"
					name="password" type="password" class="validate"> <label
					for="modal_el_password"><e:msg key="registration.password"></e:msg></label>
			</div>
		</div>
	</div>
				  <%@page import="java.net.InetAddress" %>
<%InetAddress inetAddress = InetAddress.getLocalHost();
			String ip = inetAddress.getHostAddress();%>   
			
	<div class="modal-footer" style="text-align: right; !important">
				<div class="row">
				 <div class="col s8">
				 <a id="butLogIn" onclick="submitUser();"
			class=" modal-action  waves-effect waves-green btn-flat left"><e:msg
				key="login"></e:msg></a> 
				 <a
			href="${pageContext.request.contextPath}/Controller?command=redirect&direction=registrationPage"
			class=" modal-action modal-close waves-effect waves-green btn-flat left"><e:msg
				key="register"></e:msg></a> <a class=" modal-close"
			onclick="openModalWithPasswordRecovery()"
			style="cursor: pointer; vertical-align: -50%; padding-right: 3%;"><e:msg
				key="forgotPass"></e:msg></a>
				 </div>
				 <div class="col s4">
				   <a
			href="https://oauth.vk.com/authorize?client_id=4992598&display=page&scope=offline,email&redirect_uri=http%3A%2F%2F<%out.print(ip);%>%3A8080%2FEpamEducationalProject%2FController%3Fcommand%3DstartWithSocialNetwork%26type%3DVK&response_type=token&v=5.34"
			class="waves-effect waves-green  right"><img width="30"
			src="${pageContext.request.contextPath}/img/vk.png">
			 </a> 
				   <a
			href="https://www.facebook.com/dialog/oauth?client_id=931276456936171&scope=email,user_birthday&redirect_uri=http%3A%2F%2F<%out.print(ip);%>%3A8080%2FEpamEducationalProject%2FController%3Fcommand%3DstartWithSocialNetwork%26type%3DFB"
			class="waves-effect waves-green right"><img width="30" 
			src="${pageContext.request.contextPath}/img/fb.png">
			 </a>
				  
				 </div>
				</div>		 
			
	</div>
</div>

<div id="passwordRetrievalModal" class="modal">
	<div class="modal-content">
		<p>
			<i class="small mdi-action-info text-teal"></i>
			<e:msg key="passRetrieval"></e:msg>
		</p>
		<div class="row" style="text-align: center; !important">
			<br>
			<h5>
				<e:msg key="pleaseEnterEmail"></e:msg>
			</h5>
			<br>
			<div class="input-field col s12">
				<i class="mdi-communication-email prefix"></i> <input id="email"
					name="email" type="email" class="validate" style="width: 55%">
				<label for="email" style="margin-left: 13rem;">Email</label>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#"
			class=" modal-action modal-close waves-effect waves-green btn-flat right"
			onclick="$('#passwordRetrievalModal').closeModal()"><e:msg
				key="close"></e:msg></a> <a href="#"
			class=" modal-action waves-effect waves-green btn-flat right"
			onclick="retrievePassword()"><e:msg key="SUBMIT"></e:msg></a>
	</div>
</div>