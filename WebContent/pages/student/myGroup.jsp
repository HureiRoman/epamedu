<%@page import="java.net.InetAddress"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../requirements.jsp"></jsp:include>
	<script type="text/javascript"
				src="${pageContext.request.contextPath}/js/jquery.media.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
<script
	src="${pageContext.request.contextPath}/js/jquery.bxslider.min.js"></script>
<link href="${pageContext.request.contextPath}/css/jquery.bxslider.css"
	rel="stylesheet" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/pdfobject.js"></script>
<script type="text/javascript">
<%@page import="java.net.InetAddress" %>
<%InetAddress inetAddress = InetAddress.getLocalHost();
			String ip = inetAddress.getHostAddress();%>   
	var wsocket;
	var serviceLocation = "ws://<%out.print(ip);%>:8080/${pageContext.request.contextPath}/chat/";
	var $message;
	var group = ${logined_user.groupId};
	var userId = ${logined_user.id};
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
		  slideMargin: 40
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
});
    function getUserNameById(id){
       for (var i = 0; i < groupUsers.length; i++) {
        if(groupUsers[i].id==id) return groupUsers[i].firstName+" "+groupUsers[i].lastName;
     	}
    };
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
		wsocket = new WebSocket(serviceLocation + group);
		wsocket.onmessage = onMessageReceived;
	}
	$(document).ready(
			function() {
				connectToChatserver();
				getLastMessages(0);
			});

	function prepareText(text){
		return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
	}

	function prependMessage(message) {
	  if (userId == parseInt(message.sender)){
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
		  if (userId == parseInt(message.sender)){
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
	function setColorForTag(taskId,value,value2){
		if(value!=null&&value2!=null)
			{
			var isPast= isPastDate(value,value2);
			if(isPast==true) {
				$("#fileCard"+taskId).css("background-color","");			
				$("#fileCard"+taskId).css("background-color","#7ED47E !important;");
				}
		else if(isPast==false) {
			$("#fileCard"+taskId).css("background-color","");
			$("#fileCard"+taskId).css("background-color","#FA2B2B !important;");
			
		}
	}
	}
	function isPastDate(value,value2) {
		
		  var valueDate = value.split(" ");
		  var day = valueDate[0].split("-")[0];
		  var month = valueDate[0].split("-")[1];
		  var year = valueDate[0].split("-")[2];
		  
		  var hour = valueDate[1].split(":")[0];
		  var minute = valueDate[1].split(":")[1];
		  		  
		  var valueDate2 = value2.split(" ");
		  var day2 = valueDate2[0].split("-")[0];
		  var month2 = valueDate2[0].split("-")[1];
		  var year2 = valueDate2[0].split("-")[2];
		  
		  var hour2 = valueDate2[1].split(":")[0];
		  var minute2 = valueDate2[1].split(":")[1];
		  
		  
		    if (year2 == year) {
		      if (month2 == month) {
		     if (day2 == day) {
		      if (hour2 < hour || (hour2 == hour && minute2 <= minute) ) {
		      
		       return false;
		     }
		      }else if(day2 < day){
		       return false;
		      }
		     }else if(month2 < month){
		      return false;
		     }
		    }else if(year2 < year){
		     return false;
		    } 
		    return true; 
		 }

	
	function toggleChat(){
		$('#chat_container').toggleClass('hidden');
		if($('#chat_container').hasClass( "hidden" )){
			$('.chat').css('height','45px');
		}else{
			$('.chat').css('height','480px');
		}
	}


	function showDocument(url){
		$('#showMedia').attr('href',url);
		$('a.media').media({width:500, height:400});
		$('#showDocumentModal').openModal();
		$('a.media').media({width:500, height:400});
	}
</script>
<style>
.hidden {
	display: none;
}
</style>

<script type="text/javascript">
	function sendStudentAttachment(taskId,type) {
		var attachmentTitle = $('#attachmentTitle' + taskId).val();
		var formData = new FormData();
		formData.append('taskType', type);
		formData.append('taskId', taskId);
		formData.append('attachmentTitle', attachmentTitle);
		formData.append('file', $('#file' + taskId)[0].files[0]);
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=addAttachmentOfStudent",
					dataType : "xml",
					data : formData,
					async : true,
					cache : false,
					contentType : false,
					processData : false,
					complete : function(data) {
						processFileUploadResult(data);
					}
				});
	}
	function processFileUploadResult(data) {
		var response = data.responseXML.documentElement.firstChild.nodeValue;
		if (response == 1) {
			Materialize.toast($.t('uploaded'), 3000);
			location.reload();
		} else {
			Materialize.toast($.t('uploadError'), 3000);
		}
	}
	function deleteAttachment(attachmentId) {
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=deleteStudentAttachment",
					dataType : "xml",
					data : {
						"attachmentId" : attachmentId
					},
					complete : function(data) {
						processFileDeletingResult(data);
					}
				});
	}
	function processFileDeletingResult(data) {
		var response = data.responseXML.documentElement.firstChild.nodeValue;
		if (response == 1) {
			Materialize.toast($.t('fileDeleted'), 3000);
			location.reload();
		} else {
			Materialize.toast($.t('fileDeleteError'), 3000);
		}
	}
	
	
	
	function filterMessages(string){
		var messagesAlreadyInChat = $('.messagediv').length;
		 $('.messagediv').each(function(){
		     var text = $(this).find(".preview")[0].innerHTML.toLowerCase();
		     text.toLowerCase().includes(string) ? $(this).show() : $(this).hide();         
		   });		
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
	function openingModal(Id) {
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getDataAboutStudentById",
				"studentId" : Id,
			},
			complete : function(data) {
				
				gettingDataAboutStudentRespone(data);
				}
		});
			}
		function gettingDataAboutStudentRespone(data){
			var xml = data.responseXML;
			var fname;
			var lname;
			var pname;
			var email;
			var phone;
			var birth;
			var objective;
			var skills;
			var additional;
			var education;
			var english;
			$(xml)
			.find('student')
			.each(
					function(index) {
			fname=$(this).find('fname').text();
			lname=$(this).find('lname').text();
			pname=$(this).find('pname').text();
			email=$(this).find('email').text();
			phone=$(this).find('phone').text();
			birth=$(this).find('birth').text();
			objective=$(this).find('objective').text();
			skills=$(this).find('skills').text();
			additional=$(this).find('additional').text();
			education=$(this).find('education').text();
			english=$(this).find('english').text();

			 $("#fname").text( fname); 
			 $("#lname").text( lname); 
			 $("#pname").text( pname); 
			 $("#email").text( email); 
			 $("#phone").text( phone); 
			 $("#birth").text( birth); 
			 $("#objective").text( objective); 
			 $("#skills").text( skills); 
			 $("#additional").text( additional); 
			 $("#education").text( education); 
			 $("#english").text( english); 
			 $('#detailedInformationModal').openModal();
			 
			 $('.modal').css('height', '80%');
					});
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
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>My groups</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="../teacher/infoAboutStudent.jsp"></jsp:include>
	<jsp:include page="../teacher/sendMess.jsp"></jsp:include>
	<jsp:include page="header.jsp"></jsp:include>
	<div class="row">
		<jsp:include page="student_panel_sidenav.jsp"></jsp:include>
		<div class="container">
			<div class="row card-panel white">
				<div class="col s3  card-panel white">
					<img
						src="${pageContext.request.contextPath}/images?type=directions&id=${myGroup.directionId}"
						class="circle responsive-img"><br>
					<h3 style="word-break: break-all;">${myGroup.title}</h3>
				</div>
				<div style="margin-left: 10px; !important" class="col s8 ">
					<div class="row ">
						<h3>
							<i class="fa fa-users medium blue-text"></i><e:msg key="myGroup.teachers"></e:msg>
						</h3>
						<ul class="bxslider">
							<c:forEach items="${teachers}" var="teacher">
								<e:teachercard teacher="${teacher}"></e:teachercard>
							</c:forEach>
						</ul>
					</div>
					<div class="row  ">
						<h3>
							<i class="fa fa-graduation-cap medium blue-text"></i><e:msg key="students"></e:msg>
						</h3>
						<ul class="bxslider">
							<c:forEach items="${myGroup.listOfStudents}" var="student">
								<e:studentcard student="${student}"></e:studentcard>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col s12">
					<ul class="tabs">

						<li class="tab col s6"><a href="#homeWork"><i class="fa fa-tasks small blue-text"></i><e:msg key="lessons"></e:msg></a></li>
						<li class="tab col s6"><a class="active" href="#tasks"><i class="fa fa-file-text small blue-text"></i><e:msg key="tasks"></e:msg></a></li>

					</ul>
				</div>
				<div id="homeWork" class="col s12">
				    <c:if test="${empty lessons }">
				    	<div class="row">
						<div class="valign-wrapper  offset-s4  col s6">
							<i class="large mdi-content-drafts blue-text "></i>
							<h5 class="valign center-align"><e:msg key="noLessons"></e:msg></h5>
						</div>
					</div>
				    </c:if>
					<div class="row">
						<ul class="collapsible" data-collapsible="accordion">
							<c:forEach items="${lessons}" var="lesson">
								<e:lessonForStudent userId="${logined_user.id}"
									lesson="${lesson}"></e:lessonForStudent>
							</c:forEach>
						</ul>
					</div>
				</div>
				<div id="tasks" class="col s12">
					<c:if test="${empty tasks}">
					<h5 style="text-align: center"><e:msg key="myGroup.youHaveNoTasks"></e:msg></h5>
					</c:if>
					<div class="row">
						<ul class="collapsible" data-collapsible="accordion">
							<c:forEach var="i" items="${tasks}">
								<e:taskForStudent student="${logined_user}" task="${i}"></e:taskForStudent>
							</c:forEach>
						</ul>
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
						<input onkeyup="filterMessages(this.value)"
							placeholder='Search...' type='text'>
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
					<div class='body' id="chatBody">
						<ul id="chat">
						</ul>
					</div>
					<footer style="background-color: #0066CC">
						<input style="background-color: rgb(250, 255, 189);" id="message"
							type="text">
					</footer>
				</div>
			</div>
		</div>
	</div>
	<script src="${pageContext.request.contextPath}/js/chat_box.js"></script>
	<jsp:include page="../footer.jsp"></jsp:include>

	<div id="showDocumentModal" class="modal">
		<div class="modal-content">
			<h6>Перегляд доків</h6>
					<a id="showMedia" class="media" href="guice.pdf">PDF File</a> 
                     <a class="media {type: 'html'}" href="../">HTML File</a> 
					
		</div>
	</div>
	<div id="previewModal" class="modal modal-fixed-footer">
					    
		<div id="embed" style="width:90%;height:90%;margin-left:5%;margin-top:3%;"></div>	
							
	 </div>
	<jsp:include page="../teacher/sendMess.jsp"></jsp:include>
	<script type="text/javascript">
	 $('ul.tabs').tabs();
	</script>
</body>
</html>