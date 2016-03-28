<%@page import="java.net.InetAddress"%>
<link href="${pageContext.request.contextPath}/css/style_saniok.css"
	rel="stylesheet" />
	<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="requirements.jsp"></jsp:include>
<script type="text/javascript">
var userId;
var collocutorId;
var wsocket;
var serviceLocation;
var $userName;
var $colocutorName ;
var $message ;


$(document).ready(function() {
	userId = ${logined_user.id};
	collocutorId = ${collocutor.id};
	
	<%@page import="java.net.InetAddress" %>
	<%InetAddress inetAddress = InetAddress.getLocalHost();
				String ip = inetAddress.getHostAddress();%>   
	serviceLocation = "ws://<%out.print(ip);%>:8080/${pageContext.request.contextPath}/im/"+userId+"/"+collocutorId ;
    $message  =  $('#message');
    $colocutorName = "${collocutor.firstName}" + " "	+ "${collocutor.lastName}" ;
    $userName = "${logined_user.firstName}" + " "	+ "${logined_user.lastName}" ;
	
    $('#colocutor-name').text('Chat with '+$colocutorName);
    connectToChatserver();
	getLastMessages(0);
	
	$('#message').keypress(function(e) {
		var key = e.which;
		if (key == 13) 
		{
			sendMessage();
			e.preventDefault();
		}
	});
	
	
	
      $('#chat').on('scroll', function() {		 
	        var scroll = $('#chat').scrollTop();		        
	        if (scroll < 1 ) {
	           var firstMsg = $('.messagediv:first');
	           var curOffset = firstMsg.offset().top - $('#chat').scrollTop(); 
	           moreMessages();       
	   		 // $('#chat').scrollTop(curOffset);	
	        }
	  });

});
	function onMessageReceived(evt) {
		var message = JSON.parse(evt.data); // native API
		appendMessage(message);
	}
   
	function getLastMessages() {
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "json",
			data : {
				"command" : "getPersonalMessagesWithOffset",
				"userId" : userId,
				"collocutorId" : collocutorId,
				"offset" : 0
			},
			complete : function(data) {
				addMessagesToChatWindow(data,true);
			}
		});
	}
	function getMoreMessages(offset) {
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "json",
			data : {
				"command" : "getPersonalMessagesWithOffset",
				"userId" : userId,
				"collocutorId" : collocutorId,
				"offset" : offset
			},
			complete : function(data) {
				addMessagesToChatWindow(data,false);
			}
		});
	}
	
	function prepareText(text){
		return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
	}
	
	function moreMessages() {
		var messagesAlreadyInChat = $('.messagediv').length;
		 getMoreMessages(messagesAlreadyInChat);	       
	}

	function addMessagesToChatWindow(data,append) {
		response = data.responseJSON;
		messages = response.messages;
	
		var countOfMessages = 0;
		for (i = 0; i < messages.length; i++) {
			countOfMessages++
			var message = messages[i];
			if(append){
				appendMessage(message);
			}else{
				prependMessage(message);
			}
		}
		$('#chat').scrollTop($('#chat')[0].scrollHeight);
	}
	function sendMessage() {
		if($message.val().trim() != ''){
			var msg = '{"message":"' + $message.val() + '", "received":"" , "sender" : ' + userId	+ ' , "receiver" : ' + collocutorId	+ ' }';
			wsocket.send(msg);
			$("#chat").animate({
				scrollTop : $('#chat')[0].scrollHeight
			}, 1000);
			$message.val("");
		}
	}

	function connectToChatserver() {
		wsocket = new WebSocket(serviceLocation);
		wsocket.onmessage = onMessageReceived;
	}
	function prependMessage(message) {
		var senderName = (userId == message.sender ?  $userName :  $colocutorName);
		var chatFloat = (userId == message.sender ? 'right' : 'left');
		$('#chat')
				.prepend(
						'<div class="messagediv chat-box-'+chatFloat+'" ">'
						+ '<p style=\"word-wrap: break-word;margin:0px;\">'
						+ prepareText(message.message)
						+ '</p>'
						+ '</div>'
						+ '<div  class="message_time_'+chatFloat+'">'
						+ message.received
						+ '</div>'
						+ '<div class="chat-box-name-'+chatFloat+'">'
						+
						(userId == message.sender ? senderName
						+ ' - <img src="${pageContext.request.contextPath}/images?type=users&id=' 
						+ message.sender+'"  class="circle"  />'
						: '<img src="${pageContext.request.contextPath}/images?type=users&id=' 
						+ message.sender+'"  class="circle"   /> - '+senderName)				
						+ ' </div>'
						+ '<hr class="hr-clas" />');
	}
	
	function appendMessage(message) {
		var senderName = (userId == message.sender ?  $userName :  $colocutorName)
		var chatFloat = (userId == message.sender ? 'right' : 'left');
		$('#chat')
				.append(
						'<div class="messagediv chat-box-'+chatFloat+'" ">'
						+ '<p style=\"word-wrap: break-word;margin:0px;\">'
						+ prepareText(message.message)
						+ '</p>'
						+ '</div>'
						+ '<div  class="message_time_'+chatFloat+'">'
						+ message.received
						+ '</div>'
						+ '<div class="chat-box-name-'+chatFloat+'">'
						+
						(userId == message.sender ? senderName
						+ ' - <img src="${pageContext.request.contextPath}/images?type=users&id=' 
						+ message.sender+'"  class="circle"  />'
						: '<img src="${pageContext.request.contextPath}/images?type=users&id=' 
						+ message.sender+'"  class="circle"   /> - '+senderName)				
						+ ' </div>'
						+ '<hr class="hr-clas" />');
	}
</script>
		<div class="row pad-top pad-bottom">
					<div class="chat-box-div">
						<div class="chat-box-head" id="colocutor-name"></div>
						<div class="panel-body chat-box-main z-depth-5" style="max-height: 430px;" id="chat">

						</div>
						<div class="chat-box-footer">
							<div class="input-group">
								<textarea style="resize: vertical; color: white;" id="message" maxlength="450"
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