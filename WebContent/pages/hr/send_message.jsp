<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>


<!--  Scripts-->
<script type="text/javascript"
	src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/materialize.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/init.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('select').material_select();
	});
</script>
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

<!-- CSS MATERIAL  -->
<link href="${pageContext.request.contextPath}/css/materialize.css"
	type="text/css" rel="stylesheet" media="screen,projection" />
<link href="${pageContext.request.contextPath}/css/style.css"
	type="text/css" rel="stylesheet" media="screen,projection" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>Send message to HR</title>

<!-- CUSTOM STYLE CSS -->
<link href="${pageContext.request.contextPath}/css/style_saniok.css"
	rel="stylesheet" />
	
</head>

<script>

	$(document).ready(function($) {
		$( "#search" ).on('input',function() {
			chooseTraineesBySearch();
			});
		
		
		
		$('#input_text').keypress(function(e) {
			var key = e.which;
			if (key == 13) // the enter key code
			{
				$('#sendMessageButton').click();
				return false;
			}
			
		});
		getAllTraineeInfo();
		
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
					id_list : trainee_id_list
				},
				complete : function(data) {
					processUnreadMessCount(data);
				}
			});
		}
		var map_trainee_id_count_mess = [];
		function processUnreadMessCount(data) {
			var xml = data.responseXML;
			var status = $(xml).find('status').text();
			if(status == 2){
			}else{
				$(xml).find('unread_mes_count').find('info').each(
						function(index) {
							var trainee_id = $(this).find('id').text();
							var trainee_count_mess = $(this).find('count_mess').text();
							
							if(map_trainee_id_count_mess[trainee_id] === undefined){
								map_trainee_id_count_mess[trainee_id] = 0;
							}
							
							if(map_trainee_id_count_mess[trainee_id]<trainee_count_mess && trainee_id != choosed_trainee_id){
								map_trainee_id_count_mess[trainee_id] = trainee_count_mess;
								
								$('#traineeList').find('#trainee'+trainee_id).find('#counter').remove();
								$('#traineeList').find('#trainee'+trainee_id).append('<div class="new_message_hr_dialog" id="counter" style="display:inline;">' + trainee_count_mess + '</div>');
									
								$('select').material_select('destroy');
								$('select').material_select();
								
							}else if(map_trainee_id_count_mess[trainee_id]>trainee_count_mess){
								map_trainee_id_count_mess[trainee_id] = trainee_count_mess;
								
								$('#traineeList').find('#trainee'+trainee_id).find('#counter').remove();
								
								$('select').material_select('destroy');
								$('select').material_select();
							}
				})
			}
			window.setTimeout(getUnreadMessCount, 6000);
		}
	
	////////////////
	
	function getAllTraineeInfo() {
			var hr_id;
			$.ajax({
				type : 'POST',
				url : "AjaxController",
				dataType : "xml",
				data : {
					"command" : "getAllTrainee"
				},
				complete : function(data) {
					 processGetAllTraineeInfo(data);
				}
			});
		}
	var trainee_id_list = [];	
	var choosed_trainee_id ;	
		function processGetAllTraineeInfo(data) {
			var xml = data.responseXML;
			var status = $(xml).find('status').text();
			
			if(status == 3 ){
				
				$('#no_students').css('display', 'block') //to show
// 				setTimeout("location.reload(true);",3000);
				 $('#list').css('display', 'none');
				 $('.select-wrapper').css('display', 'none');
				 
			}else{
				$(xml).find('traineeList').find('trainee').each(
					function(index) {
						$('#list').css('display', 'block');
						 $('.select-wrapper').css('display', 'block');
					
						var trainee_id = $(this).find('trainee_id').text();
						var trainee_name = $(this).find('trainee_name').text();
						var trainee_lastname = $(this).find('trainee_lastname').text();
						var trainee_email = $(this).find('trainee_email').text();
						var trainee_phone = $(this).find('trainee_phone').text();
						
						trainee_id_list.push(trainee_id);
						
						$('#traineeList').append('<option value='+trainee_id+' id="trainee'+trainee_id+'">'+trainee_name+' '+ trainee_lastname+'( '+trainee_phone+' )'+'</option>');
						
				});
				
				$('select').material_select();
				choosed_trainee_id = $('#traineeList').val();
				getChatMessages();
				
				getUnreadMessCount();
				
			}
		}
		
	function change_trainee(){
		var active_id = $('#traineeList').val();
		
		if(active_id != choosed_trainee_id){
		
			choosed_trainee_id=active_id;
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
	var trainee_id;
	var user_id;

	function getChatMessages() {
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getMessagesHrTrainee",
				"trainee_id" : choosed_trainee_id
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
			choosed_hr_id = $('#traineeList').val();
			$("#chat").animate({
				scrollTop : $('#chat')[0].scrollHeight
			}, 700);
			updateMessages();

		} else if (status == 3) {
			window.setTimeout(updateMessages, 5000);
			getChatMessages();
		} else {
			
			
			processShowChatMessage(data);
			$("#chat").animate({
				scrollTop : $('#chat')[0].scrollHeight
			}, 700);
			updateMessages();
		}

	}

	var functionIsRunning = false;
	
	function processShowChatMessage(data, newMessage) {
	if (!functionIsRunning) {
	    functionIsRunning = true;
		var xml = data.responseXML;
		var messages = $(xml).find('messages').val();
		
		
		
		var trainee = $(xml).find('trainee_id').text();
		var user = $(xml).find('logined_user_id').text();
		trainee_id = trainee;

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
							var trainee_name = $(this).find('trainee_name').text();
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
								$('#chat_name' + message_id + '').append(trainee_name + '  - ');
								$('#chat_name' + message_id + '').append('<img  alt="NO image" class="img-circle" id="photo'+message_id+'"/></div>');
								$('#photo' + message_id + '').attr('src','${pageContext.request.contextPath}/images?type=users&id='+ sender);
								
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
			functionIsRunning = false;	
		}			
	}
	
	var timeout;
	function updateMessages() {
	
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getNewMessagesForHRTrainee",
				"last_message_id" : last_mess_id,
				"trainee_id" : choosed_trainee_id
			},
			complete : function(data) {
			
				timeout = window.setTimeout(updateMessages, 4000);
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
		 document.getElementById('input_text').value = "";
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "send_message",
				"receiver_id" : choosed_trainee_id,
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
	

	
	/////////////
	var empty = false;
	function chooseTraineesBySearch(){
		var chooseBy = $('#search').val().trim();
		//prepare text!!!!!!!!!!!!!
		
		if((chooseBy == null || chooseBy == '') && empty == false ){
			empty = true;
			$('#traineeList').empty();
			$('#no_messages').css('display','none');
			$('#no_students').css('display','none');
			$("#chat").empty();
			getAllTraineeInfo();
			empty=false;
			return;
		}
		
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "searchTraineeCommand",
				"chooseBy" : chooseBy
			},
			complete : function(data) {
				 
				processGetAllTraineeSearch(data);
			}
		});
		
	}
	
	
	
	function processGetAllTraineeSearch(data) {
		var xml = data.responseXML;
		var status = $(xml).find('status').text();
		
		if(status == 3 ){
			
			$('#no_students').css('display', 'block') //to show
// 			setTimeout("location.reload(true);",3000);
			 $('#list').css('display', 'none');
			 $('#no_messages').css('display','none');
				
				$("#chat").empty();
			 $('.select-wrapper').css('display', 'none');
			 
		}else{
			$('#traineeList').empty();
			$('#no_messages').css('display','none');
			$(xml).find('traineeList').find('trainee').each(
				function(index) {
					$('#list').css('display', 'block');
					 $('.select-wrapper').css('display', 'block');
					var trainee_id = $(this).find('trainee_id').text();
				
					var trainee_name = $(this).find('trainee_name').text();
					var trainee_lastname = $(this).find('trainee_lastname').text();
					var trainee_email = $(this).find('trainee_email').text();
					var trainee_phone = $(this).find('trainee_phone').text();
					
				
					
					$('#traineeList').append('<option value='+trainee_id+' id="trainee'+trainee_id+'">'+trainee_name+' '+ trainee_lastname+'( '+trainee_phone+' )'+'</option>');
					
			});
			
			$('select').material_select();
			choosed_trainee_id = $('#traineeList').val();
			$('#no_messages').css('display','none');
			$('#no_students').css('display','none');
			
			$("#chat").empty();
			change_trainee(choosed_trainee_id);
			
			getChatMessages();
			
			getUnreadMessCount();
			
		}
	}
	
	
</script>

<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">



<body bgcolor="#FAFAFA">
	<input id="user_id" type="hidden" name="user_id"
		value="${logined_user.id}">
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="hr_panel_sidenav.jsp"></jsp:include>
	


		<div class="row pad-top pad-bottom">

			<div class="row">
				<div class=" col s7 offset-s1" >
					<div class="chat-box-div">
						<div class="chat-box-head" id="hr-title"><e:msg key="hr.chatWithTrainee"></e:msg></div>
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
								class="z-depth-1" id="no_students">
								<p><e:msg key="hr.noRegisteredStudents"></e:msg></p>
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
									placeholder="<e:msg key="ENTER_YOUR_TEXT_HERE"></e:msg>"></textarea>
								<span class="input-group-btn">
									<button class="btn btn-info" type="button" id="sendMessageButton"
										onclick="sendMessage();"><e:msg key="send"></e:msg></button>
								</span>
							</div>
						</div>

					</div>

				</div>


				<div class="col s4">
						<div class="row">
							 <div class="input-field col s6">
						      <input value="" id="search" type="text" class="validate">
						      <label class="active" for="search"><e:msg key="dt_search"></e:msg></label>
						    </div>
						</div>
						
						  <div class="input-field col s12" id="list">
						    <select id="traineeList" onchange="change_trainee()" >
						      
						    </select>
						    <label><e:msg key="hr.chooseTrainee"></e:msg></label>
						  </div>
				      
				</div>

			</div>

		</div>

	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>