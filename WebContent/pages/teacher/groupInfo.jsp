<%@page import="java.net.InetAddress"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath}/css/style_saniok.css"
	rel="stylesheet" />
<link href="${pageContext.request.contextPath}/css/jquery.bxslider.css"
	rel="stylesheet" />
<jsp:include page="../requirements.jsp"></jsp:include>
<!-- CUSTOM STYLE CSS -->
<link href="${pageContext.request.contextPath}/css/style_saniok.css"
	rel="stylesheet" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/pdfobject.js"></script>
	<script type="text/javascript">
var groupUsers = []; 

function openModalSendMessToStudent(student_id){
	$('#modalSendMessToStudent').openModal();
	$('.modal').css('height', '50%');
		studId = student_id;
}
$(document).ready(function() {
	$('.bxslider').bxSlider({
		  minSlides: 3,
		  maxSlides: 3,
		  slideWidth: 670,
		  slideMargin: 35,
		  responsive: true
		});
		
	$('#message').keypress(function(e) {
		var key = e.which;
		if (key == 13) 
		{
			sendMessage();
			e.preventDefault();
		}
	});
	
      $('#chatBody').on('scroll', function() {		 
	        var scroll = $('#chatBody').scrollTop();		        
	        if (scroll < 1 ) {
	           var firstMsg = $('.messagediv:first');
	           var curOffset = firstMsg.offset().top - $('#chatBody').scrollTop(); 
	           moreMessages();       
	   		 $('#chat').scrollTop(curOffset);	
	        }
	  });
   
})
    function getUserNameById(id){
       for (var i = 0; i < groupUsers.length; i++) {
        if(groupUsers[i].id==id) return groupUsers[i].firstName+" "+groupUsers[i].lastName;
	}
  }
<%@page import="java.net.InetAddress" %>
<%InetAddress inetAddress = InetAddress.getLocalHost();
			String ip = inetAddress.getHostAddress();%>   
	var wsocket;
	var serviceLocation = "ws://<%out.print(ip);%>:8080/${pageContext.request.contextPath}/chat/";
	var $message;
	var group = ${myGroup.id};
	var userId = ${logined_user.id};
	function onMessageReceived(evt) {
		var message = JSON.parse(evt.data); // native API
		appendMessage(message);
		$("#chatBody").animate({
			scrollTop : $('#chatBody')[0].scrollHeight
		}, 1000);
	}
	function getLastMessages() {
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "json",
			data : {
				"command" : "getChatMessagesWithOffset",
				"groupId" : group,
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
				"command" : "getChatMessagesWithOffset",
				"groupId" : group,
				"offset" : offset
			},
			complete : function(data) {
				addMessagesToChatWindow(data,false);
			}
		});
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
		
		$('#chatBody').scrollTop($('#chatBody')[0].scrollHeight);

	}

	function sendMessage() {
		var msg = '{"message":"' + $message.val() + '", "sender": ' + userId+ ' , "received":"" , "groupId" : ' + group + ' }';
		wsocket.send(msg);
		$message.val(""); 
		$("#chatBody").animate({
			scrollTop : $('#chatBody')[0].scrollHeight
		}, 1000);
	}
	function connectToChatserver() {
		wsocket = new WebSocket(serviceLocation + group);
		wsocket.onmessage = onMessageReceived;
	}
	$(document).ready(
			function() {
				$message = $('#message');
				$.ajax({
					type : 'POST',
					url : "AjaxController",
					dataType : "json",
					data : {
						"command" : "getChatParticipants",
						"groupId" : group
					},
					complete : function(data) {
						var response = data.responseJSON;
						groupUsers = response.participants;
					}
				});
				
				console.log(groupUsers);
				connectToChatserver();
				getLastMessages(0);
			});

	function getMessage() {
		var message = {
			sender : "sender",
			senderId : 1,
			message : "message",
			received : ""
		};
	}
	function prepareText(text){
		return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
	}
	
	function removeMultipleWhiteSpaces(value){
		   return value.replace(/\s\s+/g, ' ');
	}
	
	function prependMessage(message) {
	  if (userId == message.senderId){
		  $('#chat')
			.prepend(' <li class="messagediv" >'
			        +'<div class=content> '
			        +'<h3>'+getUserNameById(message.sender)+'</h3>'
			        +'<span class="preview">'+prepareText(message.message)+'</span>'
			        +' <span class="meta">'
			        + message.received
			        +' </span>'
			        +'</div>'
			        +' <a class="thumbnail" href="#" style="float:right;"  >'
			        +'  <img  src="${pageContext.request.contextPath}/images?type=users&id='+message.sender+'"  class=" responsive-img"  >'
			        +' </a>'
			        +' </li>');		    
	  }else{
		  $('#chat')
			.prepend(' <li class="messagediv" >'
			        +' <a class="thumbnail" href="#"   style="float:left;" >'
			        +'  <img  src="${pageContext.request.contextPath}/images?type=users&id='+message.sender+'"   class=" responsive-img"  >'
			        +' </a>'
			        +'<div class=content> '
			        +'<h3>'+getUserNameById(message.sender)+'</h3>'
			        +'<span class="preview">'+prepareText(message.message)+'</span>'
			        +' <span class="meta">'
			        + message.received
			        +' </span>'
			        +'</div>'
			        +' </li>');	
	  }
		
	}
	
	function appendMessage(message) {
		  if (userId == message.senderId){
			  $('#chat')
				.append(' <li class="messagediv" >'
				        +'<div class=content> '
				        +'<h3>'+getUserNameById(message.sender)+'</h3>'
				        +'<span class="preview">'+prepareText(message.message)+'</span>'
				        +' <span class="meta">'
				        + message.received
				        +' </span>'
				        +'</div>'
				        +' <a class="thumbnail" href="#"   style="float:right;"  >'
				        +'  <img  src="${pageContext.request.contextPath}/images?type=users&id='+message.sender+'"  class="responsive-img"  >'
				        +' </a>'
				        +' </li>');		    
		  }else{
			  $('#chat')
				.append(' <li class="messagediv" >'
				        +' <a class="thumbnail" href="#"  style="float:left;" >'
				        +'  <img  src="${pageContext.request.contextPath}/images?type=users&id='+message.sender+'"   class=" responsive-img"  >'
				        +' </a>'
				        +'<div class=content> '
				        +'<h3>'+getUserNameById(message.sender)+'</h3>'
				        +'<span class="preview">'+prepareText(message.message)+'</span>'
				        +' <span class="meta">'
				        + message.received
				        +' </span>'
				        +'</div>'
				        +' </li>');	
		  }
	}
	
	function toggleChat(){
		$('#chat_container').toggleClass('hidden');
		if($('#chat_container').hasClass( "hidden" )){
			$('.chat').css('height','45px');
		}else{
			$('.chat').css('height','480px');
		}
	}
	function filterMessages(string){
		var messagesAlreadyInChat = $('.messagediv').length;
		 $('.messagediv').each(function(){
		     var text = $(this).find(".preview")[0].innerHTML.toLowerCase();
		     text.toLowerCase().includes(string) ? $(this).show() : $(this).hide();         
		   });		
	}
	function showDocument(url){
        $('#documentURL').attr("href","<%out.print(ip);%>:8080/"+url);
        console.log("<%out.print(ip);%>:8080/"+url);
		$('#documentURL').gdocsViewer();
		$('#showDocumentModal').openModal();
	}
</script>

<style>
.hidden {
	display: none;
}
}
</style>
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
		
		$('select').material_select();
		$('.modal-trigger').leanModal({
		      dismissible: false, // Modal can be dismissed by clicking outside of the modal
		      opacity: .2, // Opacity of modal background
		      in_duration: 300, // Transition in duration
		      out_duration: 200, // Transition out duration
		    }
		  );
		
	});
</script>

<script type="text/javascript">

function prepareText(text){
	return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
}


function doGraduates(data) {
	$.ajax({
		type : 'POST',
		url : "AjaxController",
		dataType : "xml",
		data : {
			"command" : "doGraduates",
			"groupID" : data,
		},
		complete : function(data) {
			var response = data.responseXML.documentElement.firstChild.nodeValue;
			
			if(response=='true') {
			Materialize.toast($.t('graduateSuccessfull'), 3000);
				setTimeout(function() {window.location="/EpamEducationalProject/Controller?command=showGroups"; }, 600);
			}
		}
	});
}
	
	
	function autoTurnOnLesson(lessonId, active) {
		$.ajax({
			type : 'POST',
			url : "AjaxController?command=autoTurnOnLesson",
			dataType : "xml",
			data : {
				"lessonId" : lessonId,
				"active" : active
			},
			complete : function(data) {
				var response = data.responseXML.documentElement.firstChild.nodeValue;
				if (response == 1) {
					if (Boolean(active)) {
						Materialize.toast($.t('functionTurnOn'), 3000);
					} else {
						Materialize.toast($.t('functionTurnOff'), 3000);
					}
				} else {
					Materialize.toast($.t('errorChange'), 3000);
				}
			}
		});
	}


	function changeLessonStatus(id, active) {
		$.ajax({
			type : 'POST',
			url : "AjaxController?command=setLessonActive",
			dataType : "xml",
			data : {
				"id" : id,
				"active" : active
			},
			complete : function(data) {
				var response = data.responseXML.documentElement.firstChild.nodeValue;
				if (response == 1) {
					if (Boolean(active)) {
						Materialize.toast($.t('lessonTurnOn'), 3000);
						
						$('#autoOnSwitcher'+id).attr('checked', false);
					} else {
						Materialize.toast($.t('lessonTurnOff'), 3000);
						$('#autoOnSwitcher'+id).attr('checked', false);
					}
				} else {
					Materialize.toast($.t('errorChange'), 3000);
				}
			}
		});
	}

	function openAddLessonWindow(){
		$('#addLesson').css('display', 'block');
		$('#clickToOpenWindow').trigger('click');
		$("html, body").animate({ scrollTop: $('#addLesson').offset().top }, 1000);
		
		downloadTopics();
	}
	
	function downloadTopics(){
		var direction_id = $("input[name=direction_id]").val();
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "json",
			data : {
				"command" : "getAllTopics",
				"directionId" : direction_id
			},
			complete : function(data) {
				processGetAllTeacherTopics(data);
			}
		});
		
	}
	
	function processGetAllTeacherTopics(data){
		response = data.responseJSON;
		topics = response.topics;
		
		$("#topicItem" ).remove();
		$('#topicList').empty();
		
		for (i = 0; i < topics.length; i++) {
			var topic = topics[i];
			$('#topicList').append('<option value="'+topic.topicId+'" id="topicItem">'+topic.title+'</option>');
		}
		$('select').material_select();
	}
	
	
	function closeWindow(){
		$('#addLesson').css('display', 'none');
	}
	function openHomeworkArea(){
		$('#homeworkArea').css('display', 'block');
	}
	function closeAddHomeWorkWindow(){
		$('#homeworkArea').css('display', 'none');
	}
	
	function closeEditHomeWorkWindow(){
		$('#homeworkArea2').closeModal();
		$('#edit_homework_title').val('');
		$('#datetimepicker3').val('');
		$('#edit_task_content').val('');
		$('#choosedLessonHWedit').remove();
		$('#taskIdEdit').remove();
		
	}
	
	
	function getTopicFiles(forLessonId,topic_id){
		var topicId;
		if(forLessonId > 0){
			topicId = topic_id;
		}else{
			topicId = $( "#topicList" ).val();
		}
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "json",
			data : {
				"command" : "getTopicFiles",
				"topicId" : topicId
			},
			complete : function(data) {
				processGetTopicFiles(data,topicId,forLessonId);
			}
		});
	}
	
	function processGetTopicFiles(data,topicId,forLessonId){
		response = data.responseJSON;
		files = response.files;
		var filesImage;
		var filesImageForLesson = '';
		
		filesImage ='<div><b>'+$.t('filesSania')+'</b><br>';
		filesImage = filesImage +  '<div class="card-panel white" style="min-width: 150px; !important">';
		filesImage = filesImage +  '<div class="row">';
		
		if(!jQuery.isEmptyObject(files)){
			if(forLessonId > 0){
				for (i = 0; i < files.length; i++) {
					var file = files[i];
					filesImageForLesson = filesImageForLesson + '<div class="col s3">';
					filesImageForLesson = filesImageForLesson + '<img width="100" height="100" src="${pageContext.request.contextPath}/images?type=extensions&extension='+ file.extension + '"><br>';
					filesImageForLesson = filesImageForLesson + '<label>' + file.title + '</label>';
					filesImageForLesson = filesImageForLesson + '<div class="row">';
					
					if(file.extension == '.docx' || file.extension == '.pdf'){
						filesImageForLesson = filesImageForLesson + "<a onclick=openPreviewModal("+file.teacherId+",'topic',"+topicId+","+file.id+") class='btn-floating btn-small waves-effect waves-light green'><i class='mdi-action-visibility'></i></a>";
					}
					
					filesImageForLesson = filesImageForLesson + '<a href="${pageContext.request.contextPath}/attachments?owner='+ file.teacherId+ '&target=topic&target_id='+ topicId + '&attachment_id='+ file.id +'" class="btn-floating btn-small waves-effect waves-light green"><i class="mdi-file-file-download"></i></a>';
					filesImageForLesson = filesImageForLesson + '</div>';
					filesImageForLesson = filesImageForLesson + '</div>';
				
				}
			}else{
				for (i = 0; i < files.length; i++) {
					var file = files[i];
					filesImage = filesImage +  '<div class="col s2">';
					filesImage = filesImage + '<img width="100" height="100" src="${pageContext.request.contextPath}/images?type=extensions&extension='+ file.extension + '"><br>';
					filesImage = filesImage + '<label>' + file.title + '</label>';
					filesImage = filesImage + '<div class="row">';
					
					if(file.extension == '.docx' || file.extension == '.pdf'){
						
						filesImage = filesImage + "<a onclick=openPreviewModal("+file.teacherId+",'topic',"+topicId+","+file.id+") class='btn-floating btn-small waves-effect waves-light green'><i class='mdi-action-visibility'></i></a>";
					}
					filesImage = filesImage + '<a href="${pageContext.request.contextPath}/attachments?owner='+ file.teacherId+ '&target=topic&target_id='+ topicId + '&attachment_id='+ file.id +'" class="btn-floating btn-small waves-effect waves-light green"><i class="mdi-file-file-download"></i></a>';
					filesImage = filesImage + '</div>';
					filesImage = filesImage + '</div>';
				
				}
				
			}
		}else{
			filesImage = filesImage +  '<div class="col s2">';
			filesImage = filesImage + '<img width="100" height="100" src="${pageContext.request.contextPath}/images?type=extensions&extension=.no_attachments"><br>';
			filesImage = filesImage + '<label>'+$.t('noAttachments')+'</label>';
			filesImage = filesImage + '</div>';
			//for lesson
			filesImageForLesson = filesImageForLesson +  '<div class="col s2">';
			filesImageForLesson = filesImageForLesson + '<img width="100" height="100" src="${pageContext.request.contextPath}/images?type=extensions&extension=.no_attachments"><br>';
			filesImageForLesson = filesImageForLesson + '<label>'+$.t('noAttachments')+'</label>';
			filesImageForLesson = filesImageForLesson + '</div>';
		}
		filesImage = filesImage + '</div>';
		filesImage = filesImage + '</div>';
		filesImage = filesImage + '</div>';
		filesImage = filesImage + '</div>';
		
		
		var topicFilesDiv = $(filesImage);
		var topicFilesDivForLesson = $(filesImageForLesson);
		
		if(forLessonId > 0){
			$("#teacherTopicFiles"+forLessonId).empty();
			$('#teacherTopicFiles'+forLessonId).append(topicFilesDivForLesson);
			
			topicContent = response.topicContent;
			topicTitle = response.topicTitle;
			$('#topicTitle'+forLessonId).empty().append(topicTitle);
			$('#topicDescription'+forLessonId).empty().append(topicContent);
			
		}else{
			$('#topicFiles').empty();
			$('#topicFiles').append(topicFilesDiv);
		}
		$('select').material_select();
	}
	
	function addLesson(){
		var result = 1;
		
		var topicId = $( "#topicList" ).val();
		var date = $('#datetimepicker').val();
		var homework_title = $('#homework_title').val();
		var deadline = $('#datetimepicker2').val();
		var task_content = $('#task_content').val();
		
		task_content = removeMultipleWhiteSpaces(task_content).trim();
		homework_title = removeMultipleWhiteSpaces(homework_title).trim();
		
		if(date == ''){
			Materialize.toast($.t('chooseDate'), 3000);
			result = 0;
		}
		if(date != '' && isPastDate(date)){
			Materialize.toast($.t('chooseCorrectDate'), 3000);
			result = 0;
		}
		if(topicId == null){
			Materialize.toast($.t('chooseTheme'), 3000);
			result = 0;
		}
		if(deadline != '' && isPastDate(deadline)){
			Materialize.toast($.t('chooseCorrectDeadline'), 3000);
			result = 0;
		}
		
		if(result == 1 && (homework_title == '' && deadline == '' && task_content == '' || homework_title != '' && deadline != '' && task_content != '')){
			
			var homeworkPresent = true;
			if(deadline == ''){
				homeworkPresent = false;
			}
			
		var group_id = $("input[name=group_id]").val();
		
			$.ajax({
				type : 'POST',
				url : "AjaxController",
				dataType : "json",
				data : {
					"command" : "addLesson",
					"homeworkPresent" : homeworkPresent,
					"groupId" : group_id,
					"topicId" : topicId,
					"lessonTime" : date,
					"taskTitle" : homework_title,
					"taskBody" : task_content,
					"deadline" : deadline,
					"type" : "HW"
				},
				complete : function(data) {
					
					response = data.responseJSON;
					result = response.result;
					
					if(result == 1){
						location.reload();
					}else{
						Materialize.toast($.t('errorTryAgain'), 2000);
					}
					
				}
			});
			
		}else if(result == 1){
			Materialize.toast($.t('enterAllForHomework'), 3000);
		}
		
	}
// 	check if date not in past tense

	function isPastDate(value) {
		var now = new Date();
		var valueDate = value.split(" ");
		var day = valueDate[0].split("-")[0];
		//alert('now ' + now.getDate() + " target = " + day);
		var month = valueDate[0].split("-")[1];
		var year = valueDate[0].split("-")[2];
		
		var hour = valueDate[1].split(":")[0];
		var minute = valueDate[1].split(":")[1];
		
		//alert(now.getDay());
		  if (now.getFullYear() == year) {
		  	 if ((now.getMonth()+1) == month) {
		 		if (now.getDate() == day) {
				  if (now.getHours() < hour || (now.getHours() == hour && now.getMinutes() <= minute) ) {
					 
					  return false;
					}
		 		 }else if(now.getDate() < day){
					  return false;
				  }
		  	}else if((now.getMonth()+1) < month){
				  return false;
			  }
		  }else if(now.getFullYear() < year){
			  return false;
		  }
		  return true;
	}
	
	function openModalDeleteLesson(event,lesson_id){
		
		$("input[name='choosedLessonHWdelete']" ).remove();
		$('#deleteLessonModal').openModal();
		$('.modal').css('height', '30%');
		$('.modal').css('width', '40%');
		
		$('#deleteLessonModal').append('<input type="hidden" value="'+lesson_id+'" name="choosedLessonHWdelete">');
		event.stopPropagation();
	}
	
	function deleteLesson(){
		var lesson_id = $("input[name='choosedLessonHWdelete']" ).val();
		   $.ajax({
		        type : 'POST',
		        url : "AjaxController",
		        dataType : "xml",
		        data : {
		            "command" : "deleteLesson",
		            "lesson_id" : lesson_id
		        },
		        complete : function(data) {
		            processDeleteLesson(data);
		        }
		    });
		   $('#deleteLessonModal').closeModal();
	}
	
	function processDeleteLesson(data){
			 var xml = data.responseXML;
	        var lessonId = $(xml).find('status').text();
	        
	       	if(lessonId > 0){
	       		$( "#lesson"+lessonId+"" ).remove();
	       		
	       	 	$('.collapsible').collapsible({
	       	      accordion : false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
	       	    });
	        	Materialize.toast($.t('lessonSuccDeleted'), 1500);
	        	
	       	}else{
	       		Materialize.toast($.t('errorTryAgain'), 2000);
	       	}
	        var lessonCount =  $("li[id^=lesson]").length;
	        if(lessonCount == 0){
	        	$("#no_lessons").append("<blockquote style='border-left:0px;'>"+$.t('noLessons')+"</blockquote>");
	        }
		
	}

	
	var studId = 0; // global variable 
	function openModalSendMess(student_id,fname,lname){
		$('#fNameLname').empty();
		$('#messageTextToOnePerson').val('');
		$('#modalSendMess').openModal();
		$('#fNameLname').append(fname + ' ' + lname);
		$('.modal').css('height', '50%');
 		studId = student_id;
	}
	
	function openModalSendMessToAll(){
	
		$('#message_text').val('');
		$('#modalSendMessToAll').openModal();
		
	}

	
	function changeTopicInLessonAction(lessonId){
		var changeTopicValueId = $("#changeTopic" + lessonId).val();
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "json",
			data : {
				"command" : "changeTopicInLesson",
				"lessonId" : lessonId,
				"topicId" : changeTopicValueId
			},
			complete : function(data) {
				
				response = data.responseJSON;
				result = response.result;
				
				if(result == 1){

					getTopicFiles(lessonId,changeTopicValueId);
					
				}else{
					Materialize.toast($.t('errorTryAgain'), 2000);
				}
				
			}
		});
		
	}
	
	function editHomeWork(lessonId,taskId){
		 $('#homeworkArea2').openModal();
		 $('#homeworkArea2').css('height', '45%');
		 $('#homeworkArea2').css('width', '60%');
		 $('#homeworkArea2').css('padding', '3%');
		 $( "input[name='choosedLessonHWedit']" ).remove();
		 $( "input[name='taskIdEdit']" ).remove();
		 if(taskId == 0 ){
			 $('#deleteHWbut').css("display", "none");
		 }else{
			 $('#deleteHWbut').css("display", "block");
		 }
		 
		 	var taskTitle = $('#taskTitle'+lessonId+'').text();
			var taskBody = $('#taskBody'+lessonId+'').text();
			var taskDeadline = $('#taskDeadline'+lessonId+'').text();
			
			$('#edit_homework_title').val(taskTitle);
			$('#datetimepicker3').val(taskDeadline);
			$('#edit_task_content').val(taskBody);
			if(taskTitle != ''){
				$('label[for="edit_homework_title"]').addClass( "active" );
				$('label[for="datetimepicker3"]').addClass( "active" );
				$('label[for="edit_task_content"]').addClass( "active" );
			}
			
		 $('#homeworkArea2').append('<input type="hidden" value="'+lessonId+'" name="choosedLessonHWedit">');
		 $('#homeworkArea2').append('<input type="hidden" value="'+taskId+'" name="taskIdEdit">');
	}
	
	function submitEditHomeWorkWindow(){
		var choosedLesson = $('input[name="choosedLessonHWedit"]').val();
		var taskIdEdit = $('input[name="taskIdEdit"]').val();
		
		var homework_title = prepareText($('#edit_homework_title').val());
		var deadline = $('#datetimepicker3').val();
		var task_content = prepareText($('#edit_task_content').val());
		
		task_content = removeMultipleWhiteSpaces(task_content).trim();
		homework_title = removeMultipleWhiteSpaces(homework_title).trim();
		
		if(homework_title.trim() != '' && deadline.trim() != '' && task_content.trim() !='' && !isPastDate(deadline) && taskIdEdit.trim()!='' && choosedLesson.trim() != ''){
				
			$.ajax({
				type : 'POST',
				url : "AjaxController",
				dataType : "json",
				data : {
					"command" : "editHomeWork",
					"taskTitle" : homework_title,
					"taskBody" : task_content,
					"deadline" : deadline,
					"taskIdEdit" : taskIdEdit,
					"choosedLesson" : choosedLesson,
					"type" : "HW"
				},
				complete : function(data) {
					
					response = data.responseJSON;
					result = response.result;
					hwNewId = response.hwNewId;
					if(result == 1){
						Materialize.toast($.t('hwSuccessfullyChanged'), 2000);
						
						$('#blockquote'+choosedLesson+'').empty().append('<i>'+$.t('title')+' : <b id="taskTitle'+choosedLesson+'" style="word-break: break-all !important;">'+homework_title+'</b></br><h6 style="word-break: break-all;" id="taskBody'+choosedLesson+'">'+task_content+'</h6></br><i>'+$.t('deadline')+' : <b id="taskDeadline'+choosedLesson+'">'+deadline+'</b></i>');
						
						$("#editHomeWork"+choosedLesson+"").attr("onclick","editHomeWork("+choosedLesson+","+hwNewId+")");
						
						$('#editHWbut'+choosedLesson+'').empty().append($.t('editHomeWork'));
						closeEditHomeWorkWindow();
						
					}else{
						Materialize.toast($.t('errorTryAgain'), 2000);
					}
					
				}
			});
			
			
		}else if(homework_title == ''){
			Materialize.toast($.t('enterCorrectHWtitle'), 2000);
		}else if(deadline == '' || isPastDate(deadline)){
			Materialize.toast($.t('chooseCorrectDeadline'), 2000);
		}else if(task_content == ''){
			Materialize.toast($.t('chooseCorrectTaskContent'), 2000);
		}else {
			Materialize.toast($.t('chooseCorrectValue'), 2000);
		}
		
	}
	
	
	function deleteHomeWork(){
		
		var choosedLessonId = $('input[name="choosedLessonHWedit"]').val();
		
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "json",
			data : {
				"command" : "deleteHomeWork",
				"choosedLessonId" : choosedLessonId
			},
			complete : function(data) {
				
				response = data.responseJSON;
				result = response.result;
				if(result == 1){
					Materialize.toast($.t('taskSuccssDeleted'), 2000);
					
					$('#isHomeworkPresent'+choosedLessonId+'').empty().append('<blockquote id="blockquote'+choosedLessonId+'">'+$.t('YouDidNotGiveHW')+'</blockquote>');
// 					$('#blockquote'+choosedLessonId+'').empty().append('You didnt give homework');
					$("#editHomeWork"+choosedLessonId+"").attr("onclick","editHomeWork("+choosedLessonId+","+0+")");
					$('#editHWbut'+choosedLessonId+'').empty().append($.t('addHW'));
					closeEditHomeWorkWindow();
					
				}else{
					Materialize.toast($.t('errorTryAgain'), 2000);
				}
				
			}
		});
	}
</script>


<!-- TASKS!! -->
<script>

	function showTask(task_id){
		$("#task_files" ).find( ".activeTask" ).css('display','none');
		
		Materialize.showStaggeredList('#tasksListFly');
		$('#taskContent' + task_id).css('display','block');
		
		$('#taskContent' + task_id).addClass('activeTask');
	}
	
</script>



<script>
$( document ).ready(function() {
	$.editable.addInputType('masked', {
	    element: function (settings, original) {
	        var input = $('<input type="text">');
	        input.attr("data-inputmask-regex", settings.mask);
	        if (settings.width != 'none') {
	            input.width(settings.width);
	        }
	        if (settings.height != 'none') {
	            input.height(settings.height);
	        }
	        input.attr('autocomplete', 'off');
	        input.inputmask("Regex");
	        $(this).append(input);
	        return (input);
	    }
	});
});



	function processInputMark(attachmentId,taskId,studentId,teacherId){
	
	$('.edit'+attachmentId+'').editable('AjaxController?command=setTestMark', {
	  
	    width: '30',
	    submitdata: {
	        student_id: studentId ,
	        task_id: taskId,
	        teacher_id: teacherId
	    },
	    type: 'masked',
	    //mask: '99'
	    mask: '^(100|[0-9]{1,2})$',
	});
	}

	function deleteStudentTask(){
		var taskId = $('input[name="choosedTaskDelete"]').val();
		
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "json",
			data : {
				"command" : "deleteHomeTask",
				"taskId" : taskId
			},
			complete : function(data) {
				
				response = data.responseJSON;
				result = response.result;
				if(result == 1){
					$('#taskContent' + taskId).remove();
					$('#taskContentLi' + taskId).remove();
					
					Materialize.toast($.t('taskSuccsDeleted'), 2000);
					var taskCount = $('div[id^=taskContent]').length;
					//alert('task length = ' + taskCount);
					if(taskCount == 0){
						$('#task_files').empty()
						$('#task_files').removeClass('s9').addClass('s4').append('<div id="taskContent"><blockquote style="float:left;">'+$.t('youDidnotGiveTask')+'</blockquote></div>');
						
						$('#leftTaskList').remove();
					}
					$('#deleteTaskModal').closeModal();
				}else{
					Materialize.toast($.t('errorTryAgain'), 2000);
				}
				
			}
		});
		
	}
	
	function openDeleteStudentTaskModal(taskId){
		 $( "input[name='choosedTaskDelete']" ).remove();
		 $('#deleteTaskModal').append('<input type="hidden" value="'+taskId+'" name="choosedTaskDelete">');
		 
		 $('#deleteTaskModal').css('height', '35%');
		 $('#deleteTaskModal').css('width', '40%');
		 $('#deleteTaskModal').css('padding', '2%');
		 $('#deleteTaskModal').openModal();
	}
	
	function openEditStudentTaskModal(taskId){
		 $( "#choosedTaskEdit" ).remove();
		 $('#editTaskModal').append('<input type="hidden" value="'+taskId+'" id="choosedTaskEdit" name="taskIdField">');
			$.ajax({
				type : 'POST',
				url : "AjaxController",
				dataType : "xml",
				data : {
					"command" : "getTaskById",
					"taskId" : taskId,
				},
				complete : function(data) {
					MyRequestResponse(data);
					}
			});
		}
		
		function MyRequestResponse(data){
			var xml = data.responseXML;
			var title = $(xml).find('task').find('title').text();
			var body = $(xml).find('task').find('body').text();
			var deadline = $(xml).find('task').find('deadline').text();
			$('#task_title').val('');
			$('#datetimepicker4').val('');
			$('#task_content2').val('');
			
			
			$('#task_title').val(title);
			$('#datetimepicker4').val(deadline);
			$('#task_content2').val(body);
		 
		 $('#editTaskModal').css('height', '50%');
		 $('#editTaskModal').css('width', '50%');
		 $('#editTaskModal').openModal();
		 
		 $('#task_title_label').addClass('active');
		 $('#datetimepicker_label').addClass('active');
		 $('#task_content_label').addClass('active');
	}
	
	function saveTaskEdition() {
		var task_id=$('#choosedTaskEdit').val();
		var task_title = prepareText($('#task_title').val());
		var task_deadline = $('#datetimepicker4').val();
		var task_content = prepareText($('#task_content2').val());
		
		task_title = removeMultipleWhiteSpaces(task_title).trim();
		task_content = removeMultipleWhiteSpaces(task_content).trim();
		
		if(task_title.trim() != '' && task_deadline.trim() != '' && task_content.trim() !='' && !isPastDate(task_deadline) ){
				
			$.ajax({
				type : 'POST',
				url : "AjaxController",
				dataType : "xml",
				data : {
					"command" : "saveTaskEdition",
					"task_id" : task_id,
					"task_title" : task_title,
					"task_deadline" : task_deadline,
					"task_content" : task_content
				},
				complete : function(data) {
					var response = data.responseXML.documentElement.firstChild.nodeValue;
					if(response==1) { Materialize.toast($.t('changeSaved'),3000);	
					reappendTask(task_id,task_title,task_content,task_deadline);
					$('#editTaskModal').closeModal(); }
					else Materialize.toast($.t('fillAllFields'),3000);
				}
			});
	} else Materialize.toast($.t('fillAllFields'),3000);
	}
		
		function reappendTask(task_id,task_title,task_content,task_deadline){
			$('#taskBlockquote'+task_id).empty();
			$('#taskContentLi'+task_id).empty();
			$('#taskContentLi'+task_id).append(''
					+'<div class="row" style="margin:0px;">'
					+'<div class="col s10" style="word-break: break-all;">'
					+task_title.trim()
					+'</div>'
					+'<div class="col s2"><a href="#!" onclick="showTask('
					+task_id
					+')"class="secondary-content">'
					+'<i class="material-icons">send'
					+'</i>'
					+'</a>'
					+'</div>'
					+'</div>'	
					+'');
			$('#taskBlockquote'+task_id).append(''
					+'<i>'
					+$.t('title')
					+' : <b style="word-break: break-all;">'
					+task_title.trim()
					+'</b></i></br>'
					+'<h6 style="word-break: break-all;">'
					+task_content.trim()
					+'</h6></br>'
					+'<i>'
					+$.t('deadline')
					+' : <b>'
					+task_deadline
					+'</b></i>'
					+'');
		}
		
	function openAddStudentTaskWindow(){
		
		$('#add_task_title').val('');
		$('#datetimepicker_add_task').val('');
		$('#add_task_content').val('');
		
		 $('#addNewStudentTaskModal').openModal();
		 
		 $('#addNewStudentTaskModal').css('height', '45%');
		 $('#addNewStudentTaskModal').css('width', '60%');
		 $('#addNewStudentTaskModal').css('padding', '3%');
		 
	}
	
	function closeAddNewStudentTask(){
		 $('#addNewStudentTaskModal').closeModal();
	}
	
	function addNewStudentTask(groupId){
		
		
		var task_title = prepareText($('#add_task_title').val());
		var task_deadline = $('#datetimepicker_add_task').val();
		var task_content = prepareText($('#add_task_content').val());
		
		task_title = removeMultipleWhiteSpaces(task_title).trim();
		task_content = removeMultipleWhiteSpaces(task_content).trim();
		
		
		if(task_title.trim() != '' && task_deadline.trim() != '' && task_content.trim() !='' && !isPastDate(task_deadline) ){
				
			$.ajax({
				type : 'POST',
				url : "AjaxController",
				dataType : "json",
				data : {
					"command" : "addNewStudentTask",
					"groupId" : groupId,
					"task_title" : task_title,
					"task_deadline" : task_deadline,
					"task_content" : task_content,
					"type" : "TASK"
				},
				complete : function(data) {
					
					response = data.responseJSON;
					result = response.result;
					generatedKeyTaskId = response.generatedKeyTaskId;
					deadline_date = response.deadline_date;
					
					if(result == 1){
						Materialize.toast($.t('taskSuccsAdded'), 2000);
						var taskCount = $('li[id^=taskContentLi]').length;
						if(taskCount>0){
							//alert('taskCount>0');
							$('#leftTaskList').append('<li class="collection-item dismissable" id="taskContentLi'+generatedKeyTaskId+'"><div class="row" style="margin:0px;"><div class="col s10" style="word-break: break-all;">'+task_title.trim()+'</div><div class="col s2"><a href="#!" onclick="showTask('+generatedKeyTaskId+')"class="secondary-content"><i class="material-icons">send</i></a></div></div></li>');
							$('#task_files').append('<li style="opacity: 0;" id="task_li'+generatedKeyTaskId+'"></li>');
							$('#task_li'+generatedKeyTaskId).append('<div id="taskContent'+generatedKeyTaskId+'" style="display:none;"></div>');
							$('#taskContent'+generatedKeyTaskId).append('<div class="row" id="forRow'+generatedKeyTaskId+'"></div>');
							
							$('#forRow'+generatedKeyTaskId).append('<blockquote class="col s9" id="taskBlockquote'+generatedKeyTaskId+'"></blockquote>');
							$('#taskBlockquote'+generatedKeyTaskId).append('<i>'+$.t('title')+' : <b style="word-break: break-all;">'+task_title.trim()+'</b></i></br>');
							$('#taskBlockquote'+generatedKeyTaskId).append('<h6 style="word-break: break-all;">'+task_content.trim()+'</h6></br>');
							$('#taskBlockquote'+generatedKeyTaskId).append('<i>'+$.t('deadline')+' : <b>'+deadline_date+'</b></i>');
							
							$('#forRow'+generatedKeyTaskId).append('<div class="col s3" id="taskHeader'+generatedKeyTaskId+'"></div>')
							$('#taskHeader'+generatedKeyTaskId).append('<a onclick="openDeleteStudentTaskModal('+generatedKeyTaskId+')" id="deleteStudentTask'+generatedKeyTaskId+'" class="btn-floating btn-large waves-effect waves-light red" style="margin-right:2%; width:35px;height:35px;line-height: 41.5px;"><i class="fa fa-minus-circle" style="margin-left:0.8px" ></i></a>');
							$('#taskHeader'+generatedKeyTaskId).append('<b id="deleteTask'+generatedKeyTaskId+'" style="font-size:13px;">'+$.t("deleteTask")+'</b>');
							
							$('#taskHeader'+generatedKeyTaskId).append('<br><a onclick="openEditStudentTaskModal('+generatedKeyTaskId+')" id="editStudentTask'+generatedKeyTaskId+'" class="btn-floating btn-large waves-effect waves-light yellow" style="margin-right:2%; width:35px;height:35px;line-height: 42px;"><i class="fa fa-pencil-square-o" style="margin-left:2.5px" ></i></a>');
							$('#taskHeader'+generatedKeyTaskId).append('<b id="editTask'+generatedKeyTaskId+'" style="font-size:13px;">'+$.t('editTask')+'</b>');
							
							$('#taskContent'+generatedKeyTaskId).append('<i>'+$.t('noStudentsTasksDown')+'</i>');
							showTask(generatedKeyTaskId);
							
						}else{
							//alert('taskCount<0');
							$('#tasksContent').before('<ul class="collection col s3" id="leftTaskList"></ul>');
// 							$('#leftTaskList_row').append('<ul class="collection col s3" id="leftTaskList"></ul>');
							$('#leftTaskList').append('<li class="collection-item dismissable" id="taskContentLi'+generatedKeyTaskId+'"><div class="row" style="margin:0px;"><div class="col s10" style="word-break: break-all;">'+task_title.trim()+'</div><div class="col s2"><a href="#!" onclick="showTask('+generatedKeyTaskId+')"class="secondary-content"><i class="material-icons">send</i></a></div></div></li>');
// 							$('#leftTaskList_row').append('</ul>');
							
							$('#task_files').remove();
// 							$('#leftTaskList').after('<div class="col s9 card-panel white" id="task_files"></div>');
							$('#tasksListFly').append('<div class="col s9 card-panel white" id="task_files"></div>');
							
							//double code ---------------style="opacity: 0;"
							$('#task_files').append('<li style="opacity: 0;" id="task_li'+generatedKeyTaskId+'"></li>');
							$('#task_li'+generatedKeyTaskId).append('<div id="taskContent'+generatedKeyTaskId+'" style="display:none;"></div>');
							$('#taskContent'+generatedKeyTaskId).append('<div class="row" id="forRow'+generatedKeyTaskId+'"></div>');
							
							$('#forRow'+generatedKeyTaskId).append('<blockquote class="col s9" id="taskBlockquote'+generatedKeyTaskId+'"></blockquote>');
							$('#taskBlockquote'+generatedKeyTaskId).append('<i>'+$.t('title')+' : <b style="word-break: break-all;">'+task_title.trim()+'</b></i></br>');
							$('#taskBlockquote'+generatedKeyTaskId).append('<h6 style="word-break: break-all;">'+task_content.trim()+'</h6></br>');
							$('#taskBlockquote'+generatedKeyTaskId).append('<i>'+$.t('deadline')+' : <b>'+deadline_date+'</b></i>');
							
							$('#forRow'+generatedKeyTaskId).append('<div class="col s3" id="taskHeader'+generatedKeyTaskId+'"></div>');
							$('#taskHeader'+generatedKeyTaskId).append('<a onclick="openDeleteStudentTaskModal('+generatedKeyTaskId+')" id="deleteStudentTask'+generatedKeyTaskId+'" class="btn-floating btn-large waves-effect waves-light red" style="margin-right:2%; width:35px;height:35px;line-height: 41.5px;"><i class="fa fa-minus-circle" style="margin-left:0.8px" ></i></a>');
							$('#taskHeader'+generatedKeyTaskId).append('<b id="deleteTask'+generatedKeyTaskId+'" style="font-size:13px;">'+$.t('deleteTask')+'</b>');
							
							$('#taskHeader'+generatedKeyTaskId).append('<br><a onclick="openEditStudentTaskModal('+generatedKeyTaskId+')" id="editStudentTask'+generatedKeyTaskId+'" class="btn-floating btn-large waves-effect waves-light yellow" style="margin-right:2%; width:35px;height:35px;line-height: 42px;"><i class="fa fa-pencil-square-o" style="margin-left:2.5px" ></i></a>');
							$('#taskHeader'+generatedKeyTaskId).append('<b id="editTask'+generatedKeyTaskId+'" style="font-size:13px;">'+$.t('editTask')+'</b>');
							
							
							$('#taskContent'+generatedKeyTaskId).append('<i>'+$.t('noStudentsTasksDown')+'</i>');
							showTask(generatedKeyTaskId);
							
							
						}
						 $('#addNewStudentTaskModal').closeModal();
						 
// 						 Materialize.showStaggeredList('#tasksListFly');
						
					}else{
						Materialize.toast($.t('errorTryAgain'), 2000);
					}
					
				}
			});
			
			
		}else if(task_title == ''){
			Materialize.toast($.t('enterCorrectTaskTitle'), 2000);
		}else if(task_deadline == '' || isPastDate(task_deadline)){
			Materialize.toast($.t('chooseCorrectDeadline'), 2000);
		}else if(task_content == ''){
			Materialize.toast($.t('chooseCorrectTaskContent'), 2000);
		}else {
			Materialize.toast($.t('chooseCorrectValue'), 2000);
		}
		
	}
	
	
	///Preview
		
	

	
    function openPreviewModal(owner_id, target,target_id,attachment_id){
    	//alert('owner = ' + owner_id);
		  $.ajax({
				type : 'POST',
				url : "AjaxController",
				dataType : "json",
				data : {
					"command" : "documentPreviewPath",
					"owner" : owner_id,
					"target" : target,
					"target_id" : target_id,
					"attachment_id" : attachment_id
				},
				complete : function(data) {
					response = data.responseJSON;
					filePath = response.path;
					//alert('filePath : ' + filePath);
			    	var myParams = { 
			    		 url: filePath,
			        	 pdfOpenParams: { view: "FitV" }
			    	};
			    	
			     	var myPDF = new PDFObject(myParams);
			     	//alert(myPDF);
			     	if(myPDF != null) {
			     		myPDF.embed("embed");
			     	}else{
			     		$("#embed").css('display','none');
			     		$("#previevModal").find("modal-content").append(" <p>It appears you don't have Adobe Reader or PDF support in this web browser. <a href='${pageContext.request.contextPath}/storage/files/teachers/teacher45/topics/topic6/attachment10.pdf'>Click here to download the PDF</a></p>");
			     	}
			     	$('#previewModal').css('max-height','85%');
			     	$('#previewModal').css('height','99%');
			     	 $('#previewModal').openModal();
				}
			});
		
		
  };
		
	
</script>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
	
	
	
	
	<script src="${pageContext.request.contextPath}/js/jquery.bxslider.min.js"></script>
	<!--DateTimePicker -->
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/datetimepicker/jquery.datetimepicker.css"/>
  <script src="${pageContext.request.contextPath}/js/datetimepicker/jquery.datetimepicker.js"></script>
  <script type="text/javascript"  src="${pageContext.request.contextPath}/js/jeditable/jquery.jeditable.js"></script>
  
    <!-- regex mask -->
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/inputmask/inputmask.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/inputmask/jquery.inputmask.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/inputmask/inputmask.regex.extensions.js"></script>
  
  
  
<title>Group Info</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="send_message_to_all.jsp"></jsp:include>
	<jsp:include page="sendMess.jsp"></jsp:include>
	<jsp:include page="infoAboutStudent.jsp"></jsp:include>
	<jsp:include page="checkDeleteLessonModal.jsp"></jsp:include>
	<jsp:include page="checkDeleteTaskModal.jsp"></jsp:include>
	<jsp:include page="checkEditTaskModal.jsp"></jsp:include>
	<jsp:include page="addNewStudentTaskModal.jsp"></jsp:include>
	
	<input type="hidden" name="direction_id" value="${myGroup.directionId}">
	<input type="hidden" name="group_id" value="${myGroup.id}">
	
	
	<div id="modal1" class="modal purple lighten-5 red-text text-accent-2"
		style="max-height: 680px !important;">
		<div class="modal-content">
			<h6 class="black-text">
				<i class="fa fa-graduation-cap fa-4x"></i><e:msg key="groupInfo.GraduatingGroup"></e:msg> 
			</h6>
			<div class="row">
				<div class="col s12">
					<input type="hidden" id="remove_id"/>
					<h5 class="center-align"><e:msg key="groupInfo.AreYouSureGraduatingGroup"></e:msg></h5>
				</div>
			</div>
			 <div class="modal-footer purple lighten-5">
      <a href="#!" onclick="$('#modal1').closeModal()" class="modal-action modal-close waves-effect waves-red btn-flat "><e:msg key="CANCEL"></e:msg></a> 
      <a  onclick="doGraduates(${myGroup.id});" class=" modal-action modal-close waves-effect waves-green btn-flat"><e:msg key="confirm"></e:msg></a>
    </div> 
		</div>
	</div>	
	
	<div class="row">
		<jsp:include page="teacher_panel_sidenav.jsp"></jsp:include>
		<div class="container">
			<div class="row card-panel white">
				<div class="col s3  card-panel white">
					<img src="${pageContext.request.contextPath}/images?type=directions&id=${myGroup.directionId}"
						class="circle responsive-img"><br>
					<h4 style="word-break: break-all;">${myGroup.title}</h4>
					
					
					<c:if test="${not empty myGroup.listOfStudents}" >
					<div class="card-action" style="text-align: center;">
                        <a class="waves-effect waves-light btn modal-trigger" onclick="$('#modal1').openModal();" href="#!" id="graduate_button" style="font-size: 12px;padding-left:5px;" ><i class="fa fa-graduation-cap medium blue-text"></i> <e:msg key="graduateGroup"></e:msg></a>
                    </div>
					</c:if>
			
			<div class="row" style="padding-bottom:0px;margin-bottom:0px;padding-left:30%;">
				<div class=" white col s1.3" style="padding:10px;">
					<a class="btn-floating btn-large waves-effect waves-light blue" onclick="openAddLessonWindow()" style="z-index:0;margin-left:auto;margin-right:auto;display: block;"><i class="material-icons">+</i></a>
					<div style="font-size:13px;"><e:msg key="newLesson"></e:msg></div>
				</div>
			</div>
					
				</div>
				<div style="margin-left: 30px; !important" class="col s8  ">
					<div class="row">
						<h3><i class="fa fa-user medium blue-text text-lighten-2 "></i><e:msg key="myGroup.teachers"></e:msg></h3>
						<ul class="bxslider">
						<c:forEach items="${teachers}" var="teacher">
							<e:teachercard teacher="${teacher}"></e:teachercard>
						</c:forEach>
						</ul>
					</div>
					<div class="row" style="margin:0px;">
						<h3> <i class="fa fa-users medium blue-text"></i><e:msg key="students"></e:msg></h3>
							<c:choose >
								<c:when test="${not empty myGroup.listOfStudents}">
									<ul class="bxslider">
										<c:forEach items="${myGroup.listOfStudents}" var="student">
											<e:studentcard student="${student}"></e:studentcard>
										</c:forEach>
									</ul>
								</c:when>
								<c:otherwise>
									<e:msg key="noStudentsInGroup"></e:msg>
								</c:otherwise>
							</c:choose>
								
					</div>
				</div>
				<c:if test="${fn:length(myGroup.listOfStudents) gt 1}">
					<div class="card-action">
						<form class="col s4" action="/EpamEducationalProject/Controller?command=showVisitingStatistic" method="POST">
							<input type="hidden" name="group_id" value="${group_id}">
							<input type="submit" class="btn col s12" value="<e:msg key="teacher.visiting_statistic"></e:msg>">
						</form>
						<a class="col s4 waves-effect waves-light btn modal-trigger" href="#!" onclick="openModalSendMessToAll()"><e:msg key="sendMessToAll"></e:msg></a>
						<form class="col s4" action="/EpamEducationalProject/Controller?command=showGradesOfTasks" method="POST">
							<input type="hidden" name="group_id" value="${group_id}">
							<input type="submit" class="btn col s12" value="<e:msg key="teacher.show_grades"></e:msg>">
						</form>
					</div>

				</c:if>
			</div>
			
			<div id="addLesson" style="display:none;">
				
				<ul class="collapsible" data-collapsible="accordion">
				    <li>
				      <div class="collapsible-header" id="clickToOpenWindow"><div class="col s11"><e:msg key="addNewLesson"></e:msg></div><i class="mdi-content-backspace" onclick="closeWindow()"></i></div>
				      <div class="collapsible-body" style="padding:20px;">
				      		<div class="row" style="margin:0px;">
				      			 <div class="input-field col s4">
							          <input required id="datetimepicker" type="text" readonly="readonly">
							          <label for="datetimepicker">Date/Time</label>
							          <script type="text/javascript">
							            $('#datetimepicker').datetimepicker({
							              timepicker:true,
							              minDate:'0',
							              step: 15,
							            });
							          </script>
							      </div>
							      
							       <div class="input-field col s4">
							       		<select id="topicList" onchange="getTopicFiles()">
									      <option value="" disabled selected><e:msg key="chooseTopic"></e:msg></option>
									    </select>
									    <label><e:msg key="topics"></e:msg></label>
							       		
							       </div>
							       
							      <div class=" white col s1.3 offset-s1" style="padding:10px;float:left;margin-top:0px;">
											<a class="btn-floating btn-large waves-effect waves-light green" onclick="addLesson()" style="z-index:0;margin-left:auto;margin-right:auto;display: block;"><i class="material-icons">+</i></a>
											<div style="font-size:13px;"><b><e:msg key="addLesson"></e:msg></b></div>
								   </div>
							      
				      		</div>
				      		<div class="row">
							      <div id="topicFiles">
							      
							      
							      </div>
							</div>
							      
				      		<div class="row">
				      		<div class=" white col s1.3" style="padding:10px;float:left;">
								<a class="btn-floating btn-large waves-effect waves-light yellow" onclick="openHomeworkArea()" style="z-index:0;margin-left:auto;margin-right:auto;display: block;"><i class="material-icons">+</i></a>
								<div style="font-size:13px;"><e:msg key="addHW"></e:msg></div>
							</div>
							
							
								<div id="homeworkArea" style="display:none;">
								    <form class="col s10">
									     <div class="input-field col s6">
									          <i class="mdi-action-note-add prefix"></i>
									          <input  type="text" class="validate" maxlength="40" id="homework_title">
									          <label for="homework_title"><e:msg key="hwTitle"></e:msg></label>
									        </div>
									        
									         <div class="input-field col s4">
										          <input required id="datetimepicker2" type="text" readonly="readonly">
										          <label for="datetimepicker"><e:msg key="deadline"></e:msg></label>
										          <script type="text/javascript">
										            $('#datetimepicker2').datetimepicker({
										              timepicker:true,
										              minDate:'0',
										              step: 15,
										            });
										          </script>
										      </div>
										      <div class="offset-s1 col s1" style="padding-top: 5%;" >
									       		 <button onclick="closeAddHomeWorkWindow()" class="btn waves-effect waves-light" onmouseout="this.style.backgroundColor='#2196F3';" onmouseover="this.style.backgroundColor='#FFEB3B';" type="button"  >
													   <i class="mdi-content-backspace"></i>
												 </button>
									       			
									       	  </div>
									        
									        <div class="input-field col s12">
									          <textarea class="materialize-textarea" maxlength="600" style="max-height:75px;"id="task_content"></textarea>
									          <label for="task_content"><e:msg key="hwBody"></e:msg></label>
									        </div>
								    </form>
					      		</div>
				      		
				      		
				      		</div>
				      </div>
				    </li>
				</ul>
			</div>
			
						<div id="homeworkArea2" class="modal modal-fixed-footer">
						
								    <form class="col s10">
									     <div class="input-field col s6">
									          <i class="mdi-action-note-add prefix"></i>
									          <input  type="text" class="validate" maxlength="40" id="edit_homework_title">
									          <label for="edit_homework_title"><e:msg key="hwTitle"></e:msg></label>
									        </div>
									        
									         <div class="input-field col s4">
										          <input required id="datetimepicker3" type="text" readonly="readonly">
										          <label for="datetimepicker3"><e:msg key="deadline"></e:msg></label>
										          <script type="text/javascript">
										            $('#datetimepicker3').datetimepicker({
										              timepicker:true,
										              minDate:'0',
										              step: 15,
										            });
										          </script>
										      </div>
										      <div class="offset-s1 col s1" style="padding-top: 5%;" >
									       		 <button onclick="closeEditHomeWorkWindow()" class="btn waves-effect waves-light" onmouseout="this.style.backgroundColor='#2196F3';" onmouseover="this.style.backgroundColor='#FFEB3B';" type="button"  >
													   <e:msg key="CANCEL"></e:msg><i class="mdi-content-backspace"></i>
												 </button>
									       			
									       	  </div>
									        
									        <div class="input-field col s10">
									          <textarea class="materialize-textarea" maxlength="600"  style="max-height:75px;" id="edit_task_content"></textarea>
									          <label for="edit_task_content"><e:msg key="hwBody"></e:msg></label>
									        </div>
									        
									        <div class="offset-s1 col s1"  >
									       		 <button onclick="submitEditHomeWorkWindow()" class="btn waves-effect waves-light" onmouseout="this.style.backgroundColor='#2196F3';" onmouseover="this.style.backgroundColor='#FFEB3B';" type="button"  >
													   <e:msg key="SUBMIT"></e:msg><i class="mdi-action-note-add"></i>
												 </button>
									       			
									       		 <button style="margin-top:40%;" id="deleteHWbut" onclick="deleteHomeWork()" class="btn waves-effect waves-light" onmouseout="this.style.backgroundColor='#2196F3';" onmouseover="this.style.backgroundColor='red';" type="button"  >
													   <e:msg key="delete"></e:msg><i class="mdi-action-delete"></i>
												 </button>
									       			
									       	</div>
								    </form>
					      		</div>
			
			
			<div class="row">
					<div class="row">
						<div class="col s12">
							<ul class="tabs">
		
								<li class="tab col s6"><a class="active" href="#lesson_tab"><i class="fa fa-file-text small blue-text"></i><e:msg key="lessons"></e:msg></a></li>
								<li class="tab col s6"><a href="#tasks_tab"><i class="fa fa-tasks small blue-text"></i><e:msg key="tasks"></e:msg></a></li>
		
							</ul>
						</div>
					</div>
				
					<div class="row">
						
					
						<div id="lesson_tab">
							<ul class="collapsible" data-collapsible="accordion">
								<c:forEach items="${lessons}" var="lesson">
									<e:lessonForTeacher students="${myGroup.listOfStudents}" lesson="${lesson}" teacherId="${logined_user.id}" directionId="${myGroup.directionId}"></e:lessonForTeacher>
								</c:forEach>
								<div id="no_lessons" class="col s12  card-panel white" style="text-align:center;">
									<c:if test="${empty lessons}">
										<blockquote style="border-left:0px;"><e:msg key="noLessons"></e:msg></blockquote>
									</c:if>
								</div>
							</ul>
						</div>
					
						<div id="tasks_tab" class="card-panel white">
								<div class="row">
									<div class="col s3 offset-s9 right">
										<a onclick="openAddStudentTaskWindow()"  class="btn-floating btn-large waves-effect waves-light green" style="margin-right:2%; width:35px;height:35px;line-height: 46px;"><i class="mdi-image-rotate-right"></i></a>
										<b style="font-size:13px;"><e:msg key="addNewTask"></e:msg></b>
			
									</div>
								</div>
							<div class="row" id="leftTaskList_row">
								<c:if test="${not empty tasksOfGroup }">
								<ul class="collection col s3" id="leftTaskList">
									<c:forEach items="${tasksOfGroup}" var="task">
								        <li class="collection-item dismissable" id="taskContentLi${task.id}"><div class="row" style="margin:0px;"><div class="col s10" style="word-break: break-all;">${task.title }</div><div class="col s2"><a href="#!" onclick="showTask(${task.id})"class="secondary-content"><i class="material-icons">send</i></a></div></div></li>
								    </c:forEach>
						     	</ul>
						     	 </c:if>
						     	 	<div id="tasksContent">
							     	 	<c:choose>
								     	 	<c:when test="${empty tasksOfGroup}">
								     	 	<ul id="tasksListFly">
							     				 <div class="col s3 offset-s1 card-panel white" id="task_files">
									     	 		<div id="taskContent">
									     	 			<blockquote style="float:left;"><e:msg key="YouDidNotGiveTasks"></e:msg></blockquote>
									     	 		</div>
									     	 	 </div>
									     	  </ul>
								     	 	</c:when>
								     	 	
								     	 	<c:when test="${not empty tasksOfGroup}">
								     	 		
								     	 	    <ul id="tasksListFly">
								     	 	   		 <div class="col s9 card-panel white" id="task_files">
										     	 		<c:forEach items="${tasksOfGroup}" var="task">
										     	 		<li style="opacity: 0;">
															<e:taskForTeacher task="${task}" ></e:taskForTeacher >
													    </li>
													    </c:forEach>
												    </div>
										     	 </ul>		
								     	 		
								     	 	</c:when>
							     	 	</c:choose>
						     	 	</div>
						     	
						     	 
						     	 
					     	 </div>
						</div>
					</div>
			</div>
			
	    
		</div>
		<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script> 
		<div class='chat' class="z-depth-3">
				<style scoped>
@import url('${pageContext.request.contextPath}/css/chat_style.css');

@import
	url('http://fonts.googleapis.com/css?family=Roboto:400,100,400italic,700italic,700')
	;

@import
	url('http://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css')
	;
</style>
				<header>
					<div class='search' style="display: none;">
						<input  onkeyup="filterMessages(this.value)"  placeholder='Search...' type='text'>
					</div>
					<h2 class='title'>
						<a href='#'><e:msg key="chat"></e:msg></a>
					</h2>
					<ul class='tools'>
						<li><a class='fa fa-search' href='#!'></a></li>
						<li><a class='fa fa-plus-square' href="#!"
							onclick="toggleChat()"></a></li>
					</ul>
				</header>
				<div id="chat_container" class="hidden">
					<div class='body' id="chatBody" >
						<ul id="chat">
						</ul>
					</div>
					<footer style="background-color: #0066CC">
						<input style="background-color: rgb(250, 255, 189);" id="message"
							type="text">
					</footer>
				</div>
			</div>
				<script src="${pageContext.request.contextPath}/js/chat_box.js"></script>
	</div>
<!-- 	<div id="showDocumentModal" class="modal" -->
<!-- 		style="max-height: 680px !important;"> -->
<!-- 		<div class="modal-content"> -->
<!-- 			<a id="documentURL" class="embed"></a> -->
<!-- 		</div> -->
<!-- 	</div> -->
	
	<div id="previewModal" class="modal modal-fixed-footer">
					    
		<div id="embed" style="width:90%;height:90%;margin-left:5%;margin-top:3%;"></div>	
							
	 </div>
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>