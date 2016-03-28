<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags" %>
<link href="${pageContext.request.contextPath}/css/style_saniok.css"
	rel="stylesheet" />

<script>
	$(document).ready(function($) {
		getAllNewMessages();

	})

	function getAllNewMessages() {

		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getUreadMessCount",
				"allUnread" : "true"
			},
			complete : function(data) {
				processGetAllNewMessages(data);
			}
		});
	}
	var cur_amount_of_mess = 0;
	function processGetAllNewMessages(data) {
		var xml = data.responseXML;
		var status = $(xml).find('status').text();

		if (status == 1) {
			var amount_of_messages = $(xml).find('amount_of_messages').text();
			if (amount_of_messages != cur_amount_of_mess
					&& amount_of_messages != 0) {
				var isPlaySound = $(xml).find('isPlaySound').text();

				$('ul').find('#name').find('#counterAll').remove();
				$('ul').find('#name').append(
						'<span class="new badge" id="counterAll">'
						+ amount_of_messages + '</span>');
				$('.row').find('.card_unread_messages').remove();
				$('.row')
						.find('#message_card')
						.append(
								'<div class="card_unread_messages" id="cardCounterAll">'
										+ amount_of_messages
										+ ' нове(их) повідомлення</div>');

				if (amount_of_messages > cur_amount_of_mess
						&& isPlaySound == 'true') {
					var aSound = document.createElement('audio');
					aSound
							.setAttribute('src',
									'${pageContext.request.contextPath}/audio/message_sound.mp3');
					aSound.play();
				}
				cur_amount_of_mess = amount_of_messages;
			} else if (amount_of_messages == 0) {
				cur_amount_of_mess = 0;
				$('ul').find('#name').find('#counterAll').remove();

				$('.row').find('.card_unread_messages').remove();
			}
			window.setTimeout(getAllNewMessages, 3000);

		}
	}
</script>

<ul id="slide-out" class="side-nav">
	<li
		style=" background-image: url('${pageContext.request.contextPath}/img/admin/admin_back.jpg');  background-size: 240px 80px; ">
		<div class=row>

			<div class="col s5">
				<a
					href="${pageContext.request.contextPath}/Controller?command=redirect&direction=hr_cabinet">
					<img height="50" width="50" class="circle "
					src="${pageContext.request.contextPath}/images?type=users&id=${logined_user.id}">
				</a>
			</div>
			<div class="col s5">
				<label class="white-text">${logined_user.firstName}
					${logined_user.lastName}</label>
			</div>
		</div>
	</li>

	<li><a
		href="${pageContext.request.contextPath}/Controller?command=redirect&direction=myProfile"><i
			class="material-icons prefix">perm_identity</i> <e:msg key="myProfile"></e:msg></a></li>
	<li><a id="name"
		href="${pageContext.request.contextPath}/Controller?command=redirect&direction=hr_to_trainee_message">
			<i class="mdi-content-mail  prefix small"></i> <e:msg key="hr.my_messages"></e:msg>
	</a></li>

	<li><a
		href="${pageContext.request.contextPath}/Controller?command=manageInterviews">
			<i class="mdi-av-mic  prefix small"></i> <e:msg key="interviews"></e:msg>
	</a></li>
	<li><a
		href="${pageContext.request.contextPath}/Controller?command=viewTrainees">
			<i class="mdi-social-group  prefix small"></i> <e:msg key="trainees"></e:msg>
	</a></li>

	<li><a
		href="${pageContext.request.contextPath}/Controller?command=interviewResults">
			<i class="mdi-navigation-apps  prefix small"></i> <e:msg key="hr.results"></e:msg>
	</a></li>

	<li><a
		href="${pageContext.request.contextPath}/Controller?command=showStudents">
			<i class="mdi-social-school  prefix small"></i><e:msg key="students"></e:msg>
	</a></li>
		<li><a href="#" onclick="exit()"><i
			class="material-icons prefix">open_in_browser</i><e:msg key="header.Exit"></e:msg></a></li>

</ul>