<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="../requirements.jsp"></jsp:include>

<script type="text/javascript">
	$(document).ready(function() {
		$(".card").hover(function() {
			$(this).stop().animate({
				opacity : "0.5"
			}, 'slow');
		}, function() {
			$(this).stop().animate({
				opacity : "1"
			}, 'slow');
		});

	});
</script>
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>Send message to HR</title>

<!-- CUSTOM STYLE CSS -->
<link href="${pageContext.request.contextPath}/css/style_saniok.css"
	rel="stylesheet" />

</head>

<script>

	$(document).ready(function($) {
		
		$('#input_text').keypress(function(e) {
			var key = e.which;
			if (key == 13) // the enter key code
			{
				$('#sendMessageButton').click();
				return false;
			}
		});
		
		getHrInfo();
		getChatMessages();
		$("#chat").animate({
			scrollTop : $('#chat')[0].scrollHeight
		}, 1000);
		
	})
	
	function prepareText(text){
		  return text.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/&/g, "&amp;")
	}
	
	function decodePunctuation(str){
		 return str.replace(/&#60;/g, "&lt;").replace(/&#62;/g, "&gt;")
				         .replace(/&quot;/g, '"')
				         .replace(/&amp;/g, "&");
	}
	
	function getUnreadMessCount() {
			$.ajax({
				type : 'POST',
				url : "AjaxController",
				dataType : "xml",
				data : {
					"command" : "getUreadMessCount",
					id_list : hr_id_list
				},
				complete : function(data) {
					processUnreadMessCount(data);
				}
			});
		}
		var map_hr_id_count_mess = [];
		function processUnreadMessCount(data) {
			var xml = data.responseXML;
			var status = $(xml).find('status').text();
			
			if(status == 2){
			}else{
				$(xml).find('unread_mes_count').find('info').each(
						function(index) {
							var hr_id = $(this).find('id').text();
							var hr_count_mess = $(this).find('count_mess').text();
							
							if(map_hr_id_count_mess[hr_id] === undefined){
								map_hr_id_count_mess[hr_id] = 0;
							}
							
							if(map_hr_id_count_mess[hr_id]<hr_count_mess){
								map_hr_id_count_mess[hr_id] = hr_count_mess;
								$('li').find('#'+hr_id).find('#counter').remove();
								$('li').find('#'+hr_id).append('<div class="new_message" id="counter">' + hr_count_mess + '</div>');
								
							}else if(map_hr_id_count_mess[hr_id]>hr_count_mess){
								map_hr_id_count_mess[hr_id] = hr_count_mess;
								$('li').find('#'+hr_id).find('#counter').remove();
							}
				})
			
			}
			
			window.setTimeout(getUnreadMessCount, 1000);
		}
	
	////////////////
	
	function getHrInfo() {
			var hr_id;
			$.ajax({
				type : 'POST',
				url : "AjaxController",
				dataType : "xml",
				data : {
					"command" : "getAllHr"
				},
				complete : function(data) {
					 processHrInfo(data);
				}
			});
		}
		
	var hr_id_list = [];
		function processHrInfo(data) {
			var xml = data.responseXML;
			var status = $(xml).find('status').text();
			
			 if(status == 3 ){
				$('#no_hrs').css('display', 'block') //to show
				setTimeout("location.reload(true);",5000);
				
			}else{
				$(xml).find('hrList').find('hr').each(
					function(index) {
						var hr_id = $(this).find('hr_id').text();
						var hr_name = $(this).find('hr_name').text();
						var hr_lastname = $(this).find('hr_lastname').text();
						var hr_email = $(this).find('hr_email').text();
						
						hr_id_list.push(hr_id);
						
						choosed_hr_id = hr_id;
						
						$('#hrList').append('<li onclick="chooseHR('+hr_id+')" id="'+hr_id+'" class="active">' + '</li>');
						$('#'+hr_id).append('<div class="collapsible-header active" id="'+hr_id+'"><i class="mdi-communication-quick-contacts-mail"></i><p style="float:left;margin:0px; margin-top:2%;">'+hr_name+'</p></div>');
						$('#'+hr_id).append('<div class="collapsible-body" id="HRdirectionInfo'+hr_id+'"><img alt="NO image" class="hr_image_info" src="${pageContext.request.contextPath}/images?type=users&id='+hr_id+'"/><p class="hr_text_info" style="padding:20px;">'+hr_name + ' ' + hr_lastname+'</p><p class="hr_text_info">'+hr_email+'</p></div>');
						
						var c = $(this).find('direction_name').text();
						if(c.length>0){
							$('#HRdirectionInfo'+hr_id).append('<div class="HRdirectionI" id="TypeDirectionI'+hr_id+'">'
									+$.t('direction') 
									+': ');
							
							$(this).find('hr_directions').find('direction_name').each(
									function(index) {
										var direction_name = $(this).text();
										$('#TypeDirectionI'+hr_id).append(" <b>" + direction_name+"</b>; ");
										
									}
							);
							$('#HRdirectionInfo'+hr_id).append("</div>");
						}
						$('.collapsible').collapsible();
						choosed_hr_id == hr_id;
				});
				
				getUnreadMessCount();
			}

			
		}
	
	var choosed_hr_id;	
	function chooseHR(hr_id,hr_name){
		var active_id = $('.popout').find( ".active" ).attr( "id" );
		
		
		if(active_id != hr_id){
			var my_li = $('#'+hr_id).addClass('active');
			my_li.find('#'+hr_id).addClass('active');
			$('.collapsible').collapsible();
			
			return;
		}else{
			
			choosed_hr_id=hr_id;
			$( ".chat-box-right" ).remove();
			$( ".message_time_right" ).remove();
			$( ".chat-box-name-right" ).remove();
			$( ".hr-clas" ).remove();
			$( ".chat-box-left" ).remove();
			$( ".message_time_left" ).remove();
			$( ".chat-box-name-left" ).remove();
			
			window.clearTimeout(timeout);
			getChatMessages();
		}
	}
	
	

	var last_mess_id;
	var hr_id;
	var user_id;

	function getChatMessages() {
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getMessagesTraineeHr",
				"hr_id" : choosed_hr_id
			},
			complete : function(data) {
				processGetMessages(data);
			}
		});
	}

	function processGetMessages(data) {
		var xml = data.responseXML;
		var status = $(xml).find('messages').find('status').text();
		
		if (status == 2) {
				
			$('#no_messages').css('display', 'block') //to show
			processShowChatMessage(data);
			choosed_hr_id = $('.popout').find( ".active" ).attr( "id" );
			$("#chat").animate({
				scrollTop : $('#chat')[0].scrollHeight
			}, 700);
			updateMessages();

		} else if (status == 3) {
			window.setTimeout(updateMessages, 3000);
			getChatMessages();
		} else {
			processShowChatMessage(data);
			$("#chat").animate({
				scrollTop : $('#chat')[0].scrollHeight
			}, 700);
			updateMessages();
		}

	}

	function processShowChatMessage(data, newMessage) {
		var xml = data.responseXML;
		var messages = $(xml).find('messages').val();
		
		var hr = $(xml).find('hr_id').text();
		var user = $(xml).find('logined_user_id').text();
		hr_id = hr;

		var last_message_id = $(xml).find('last_mess_id').text();
		last_mess_id = last_message_id;
	
		$(xml).find('message').each(
						function(index) {
							$('#no_messages').css('display', 'none') //do not  show
							var message_id = $(this).find('message_id').text();
							var text = $(this).find('text').text();
							text = decodePunctuation(text);
							var isRead = $(this).find('isRead').text();
							var time = $(this).find('time').text();
							var hr_name = $(this).find('hr_name').text();
							var user_name = $(this).find('user_name').text();
							var photo_url = $(this).find('photo_url').text();
							var sender = $(this).find('sender').text();

							if (sender != user) {
								$('#chat').append('<div class="chat-box-right" id="'+message_id+'"></div>');
								$('#chat').append('<div id="time'+message_id+'" class="message_time_right"></div>');
								$('#chat').append('<div class="chat-box-name-right"  id="chat_name'+message_id+'"></div>');
								$('#chat').append('<hr class="hr-clas" />');

								$('#' + message_id + '').append("<p style=\"word-wrap: break-word;margin:0px;\">"+ text + "</p>");
								$('#time' + message_id + '').append(time);
								$('#chat_name' + message_id + '').append(hr_name + '  - ');
								$('#chat_name' + message_id + '').append('<img  alt="NO image" class="img-circle" id="photo'+message_id+'"/></div>');
								$('#photo' + message_id + '').attr('src','${pageContext.request.contextPath}/images?type=users&id='+ hr);
								if (newMessage == 'true') {
									
								}
							} else {
								$('#chat').append('<div class="chat-box-left" id="'+message_id+'"></div>');
								$('#chat').append(' <div class="chat-box-name-left"  id="chat_name'+message_id+'"></div>');
								$('#chat').append('<div id="time'+message_id+'" class="message_time_left""></div>');
								$('#chat_name' + message_id + '').append('<img  alt="NO image" class="img-circle" id="photo'+message_id+'"/></div>');
								$('#chat').append('<hr class="hr-clas" />');

								$('#' + message_id + '').append("<p style=\"word-wrap: break-word;margin:0px;\">"+ text + "</p>");
								$('#time' + message_id + '').append(time);
								$('#chat_name' + message_id + '').append('  - ' + user_name);
								$('#photo' + message_id + '').attr('src','${pageContext.request.contextPath}/images?type=users&id='+ user);
							}

						})

	}
	
	var timeout;
	function updateMessages() {
		if(last_mess_id == ''){
			last_mess_id = 0;
		}
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getNewMessagesForTraineeHR",
				"last_message_id" : last_mess_id,
				"hr_id" : hr_id
			},
			complete : function(data) {
				timeout = window.setTimeout(updateMessages, 1000);
				processShowChatMessage(data, 'true');
				var xml = data.responseXML;
				var status = $(xml).find('messages').find('status').text();
				if (status != 2) {
					$("#chat").animate({
						scrollTop : $('#chat')[0].scrollHeight
					}, 700);

				}
			}
		});
	}

	function sendMessage(element) {
		
		var text = $('textarea#input_text').val();
		text = prepareText(text);
		 document.getElementById('input_text').value = "";
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "send_message",
				"receiver_id" : hr_id,
				"text" : text
			},
			complete : function(data) {
				 processSendMessage(data);
			}
		});
		
		function processSendMessage(data) {
			var xml = data.responseXML;
			var status = $(xml).find('status').text();
			var text = $('textarea#input_text').empty();
		}
	}
	
</script>

<body bgcolor="#FAFAFA">
	<input id="user_id" type="hidden" name="user_id"
		value="${logined_user.id}">
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="applyOnTestModal.jsp"></jsp:include>
	<jsp:include page="trainee_panel_sidenav.jsp"></jsp:include>
	


		<div class="row pad-top pad-bottom">

			<div class="row">
				<div class=" col s7 offset-s1" >
					<div class="chat-box-div">
						<div class="chat-box-head" id="hr-title"><e:msg key="trainee.chatWithHR"></e:msg></div>
							<div>
							<div style="display: none; background: rgb(221, 227, 237); text-align: center;"
								class="z-depth-1" id="no_messages">
								<p><e:msg key="hr.noMessages"></e:msg></p>
								<i class="small mdi-hardware-keyboard-hide"></i>
								<div class="progress">
									<div class="indeterminate"></div>
								</div>
							</div>
							
							<div style="display: none; background: rgb(221, 227, 237); text-align: center;"
								class="z-depth-1" id="no_hrs">
								<p><e:msg key="trainee.noRegisteredHR"></e:msg></p>
								<i class="small mdi-hardware-keyboard-hide"></i>
								<div class="progress">
									<div class="indeterminate"></div>
								</div>
							</div>
							</div>
						<div class="panel-body chat-box-main z-depth-5" style="max-height: 430px;" id="chat">

						</div>
						<div class="chat-box-footer">
							<div class="input-group">
								<textarea style="resize: vertical; color: white;" id="input_text" maxlength="450"
									class="materialize-textarea" cols="10" rows="5"
									placeholder="Enter Text Here..."></textarea>
								<span class="input-group-btn">
									<button class="btn btn-info" type="button" id="sendMessageButton"
										onclick="sendMessage();"><e:msg key="send"></e:msg></button>
								</span>
							</div>
						</div>

					</div>

				</div>


				<div class="col s4">

					<ul class="collapsible popout" data-collapsible="accordion" id="hrList"> 
						
						
					</ul>

				</div>

			</div>

		</div>

	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>