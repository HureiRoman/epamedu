function prepareText(text){
	return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
}


function doGraduates(data) {
	$.ajax({
		type : 'POST',
		url : "${pageContext.request.contextPath}/AjaxController",
		dataType : "xml",
		data : {
			"command" : "doGraduates",
			"groupID" : data,
		},
		complete : function(data) {
			var response = data.responseXML.documentElement.firstChild.nodeValue;
			if(response=='true') Materialize.toast('Graduating was successed', 3000)
		}
	});
}
	
	
	function autoTurnOnLesson(lessonId, active) {
		$.ajax({
			type : 'POST',
			url : "${pageContext.request.contextPath}/AjaxController?command=autoTurnOnLesson",
			dataType : "xml",
			data : {
				"lessonId" : lessonId,
				"active" : active
			},
			complete : function(data) {
				var response = data.responseXML.documentElement.firstChild.nodeValue;
				if (response == 1) {
					if (Boolean(active)) {
						Materialize.toast('Функцію увімкнено увімкнено', 3000);
					} else {
						Materialize.toast('Функцію вимкнено', 3000);
					}
				} else {
					Materialize.toast('Помилка зміни ', 3000);
				}
			}
		});
	}


	function changeLessonStatus(id, active) {
		$.ajax({
			type : 'POST',
			url : "${pageContext.request.contextPath}/AjaxController?command=setLessonActive",
			dataType : "xml",
			data : {
				"id" : id,
				"active" : active
			},
			complete : function(data) {
				var response = data.responseXML.documentElement.firstChild.nodeValue;
				if (response == 1) {
					if (Boolean(active)) {
						Materialize.toast(' lesson увімкнено', 3000);
					} else {
						Materialize.toast(' lesson вимкнено', 3000);
					}
				} else {
					Materialize.toast('Помилка зміни ', 3000);
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
			url : "${pageContext.request.contextPath}/AjaxController",
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
			url : "${pageContext.request.contextPath}/AjaxController",
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
		
		filesImage ='<div><b>Files</b><br>';
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
					filesImageForLesson = filesImageForLesson + '<a class="btn-floating btn-small waves-effect waves-light red"><i class="mdi-action-visibility"></i></a>';
					filesImageForLesson = filesImageForLesson + '<a href="${pageContext.request.contextPath}/files?owner='+ file.teacherId+ '&target=topic&target_id='+ topicId + '&attachment_id='+ file.id +'" class="btn-floating btn-small waves-effect waves-light red"><i class="mdi-file-file-download"></i></a>';
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
					filesImage = filesImage + '<a class="btn-floating btn-small waves-effect waves-light red"><i class="mdi-action-visibility"></i></a>';
					filesImage = filesImage + '<a href="${pageContext.request.contextPath}/files?owner='+ file.teacherId+ '&target=topic&target_id='+ topicId + '&attachment_id='+ file.id +'" class="btn-floating btn-small waves-effect waves-light red"><i class="mdi-file-file-download"></i></a>';
					filesImage = filesImage + '</div>';
					filesImage = filesImage + '</div>';
				
				}
				
			}
		}else{
			filesImage = filesImage +  '<div class="col s2">';
			filesImage = filesImage + '<img width="100" height="100" src="${pageContext.request.contextPath}/images?type=extensions&extension=.no_attachments"><br>';
			filesImage = filesImage + '<label> No attachments</label>';
			filesImage = filesImage + '</div>';
			//for lesson
			filesImageForLesson = filesImageForLesson +  '<div class="col s2">';
			filesImageForLesson = filesImageForLesson + '<img width="100" height="100" src="${pageContext.request.contextPath}/images?type=extensions&extension=.no_attachments"><br>';
			filesImageForLesson = filesImageForLesson + '<label> No attachments</label>';
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
		var homework_title = $('#homework_title').val().trim();
		var deadline = $('#datetimepicker2').val();
		var task_content = $('#task_content').val().trim();
		
		if(date == ''){
			Materialize.toast('please choose date', 3000);
			result = 0;
		}
		if(date != '' && isPastDate(date)){
			Materialize.toast('please choose correct date', 3000);
			result = 0;
		}
		if(topicId == null){
			Materialize.toast('please choose theme', 3000);
			result = 0;
		}
		if(deadline != '' && isPastDate(deadline)){
			Materialize.toast('please choose correct date for deadline', 3000);
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
				url : "${pageContext.request.contextPath}/AjaxController",
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
						Materialize.toast('Error, try again', 2000);
					}
					
				}
			});
			
		}else if(result == 1){
			Materialize.toast('please choose all input for homework', 3000);
		}
		
	}
// 	check if date not in past tense

	function isPastDate(value) {
		var now = new Date();
		var valueDate = value.split(" ");
		var day = valueDate[0].split("-")[0];
		var month = valueDate[0].split("-")[1];
		var year = valueDate[0].split("-")[2];
		
		var hour = valueDate[1].split(":")[0];
		var minute = valueDate[1].split(":")[1];
		
		
		  if (now.getFullYear() == year) {
		  	 if ((now.getMonth()+1) == month) {
		 		if (now.getDay() == day) {
				  if (now.getHours() < hour || (now.getHours() == hour && now.getMinutes() <= minute) ) {
					 
					  return false;
					}
		 		 }else if(now.getDay() < day){
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
	
	function openModalDeleteLesson(lesson_id){
		
		$("input[name='choosedLessonHWdelete']" ).remove();
		$('#deleteLessonModal').openModal();
		$('.modal').css('height', '40%');
		$('.modal').css('width', '35%');
		
		$('#deleteLessonModal').append('<input type="hidden" value="'+lesson_id+'" name="choosedLessonHWdelete">');
		
	}
	
	function deleteLesson(){
		var lesson_id = $("input[name='choosedLessonHWdelete']" ).val();
		   $.ajax({
		        type : 'POST',
		        url : "${pageContext.request.contextPath}/AjaxController",
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
	        	Materialize.toast('Lesson successfully deleted', 1500);
	       	}else{
	       		Materialize.toast('Error, try again', 2000);
	       	}
		
	}

	
	var receiverId = 0; // global variable 
	function openModalSendMess(receiver_id,fname,lname){
		$('#fNameLname').empty();
		$('textarea#message_text').val('');
		$('#modalSendMess').openModal();
		$('.modal').css('height', '55%');
		receiverId = receiver_id;
		
		$('#fNameLname').append(fname + ' ' + lname);
	}
	
	//change topic
	function showButton(lessonId){
		var changeTopicValue = $("#changeTopic" + lessonId).val();
		/*alert('changeTopicValue = ' + changeTopicValue)*/
		if(changeTopicValue > 0 ){
			$("#changeTopicButton"+lessonId ).css("display","block");
		}else{
			$("#changeTopicButton"+lessonId  ).css("display","none");
		}
	}
	
	function changeTopicInLessonAction(lessonId){
		var changeTopicValueId = $("#changeTopic" + lessonId).val();
		/*alert('lesson id = ' + lessonId + 'changeTopicValueId ' + changeTopicValueId)*/
		$.ajax({
			type : 'POST',
			url : "${pageContext.request.contextPath}/AjaxController",
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
					Materialize.toast('Error, try again', 2000);
				}
				
			}
		});
		
	}
	
	function editHomeWork(lessonId,taskId){
		 $('#homeworkArea2').openModal();
		 $('.modal').css('height', '40%');
		 $( "input[name='choosedLessonHWedit']" ).remove();
		 $( "input[name='taskIdEdit']" ).remove();
		 //alert('TASK ID = ' + taskId)
		 if(taskId == 0 ){
			 $('#deleteHWbut').css("display", "none");
		 }else{
			 $('#deleteHWbut').css("display", "block");
		 }
		 
		 	var taskTitle = $('#taskTitle'+lessonId+'').text();
			var taskBody = $('#taskBody'+lessonId+'').text();
			var taskDeadline = $('#taskDeadline'+lessonId+'').text();
			
		 //alert("taskTitle "+taskTitle + " taskBody "+ taskBody + " taskDeadline " + taskDeadline)
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
		//alert('taskIdEdit = ' + taskIdEdit)
		//alert('choosedLesson = ' + choosedLesson)
		
		var homework_title = prepareText($('#edit_homework_title').val());
		var deadline = $('#datetimepicker3').val();
		var task_content = prepareText($('#edit_task_content').val());
		
		if(homework_title.trim() != '' && deadline.trim() != '' && task_content.trim() !='' && !isPastDate(deadline) && taskIdEdit.trim()!='' && choosedLesson.trim() != ''){
				
			$.ajax({
				type : 'POST',
				url : "${pageContext.request.contextPath}/AjaxController",
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
					//alert('result = ' + result)
					if(result == 1){
						Materialize.toast('Home work successfully changed', 2000);
						
						$('#blockquote'+choosedLesson+'').empty().append('<i>Title : <b id="taskTitle'+choosedLesson+'">'+homework_title+'</b></br><h6 id="taskBody'+choosedLesson+'">'+task_content+'</h6></br><i>Deadline : <b id="taskDeadline'+choosedLesson+'">'+deadline+'</b></i>');
						
						$("#editHomeWork"+choosedLesson+"").attr("onclick","editHomeWork("+choosedLesson+","+hwNewId+")");
						
						$('#editHWbut').empty().append('Edit homework');
						closeEditHomeWorkWindow();
						
					}else{
						Materialize.toast('Error, try again', 2000);
					}
					
				}
			});
			
			
		}else if(homework_title == ''){
			Materialize.toast('please enter correct homework title value', 2000);
		}else if(deadline == '' || isPastDate(deadline)){
			Materialize.toast('please enter correct deadline value', 2000);
		}else if(task_content == ''){
			Materialize.toast('please enter correct task_content value', 2000);
		}else {
			Materialize.toast('please choose correct value', 2000);
		}
		
	}
	
	
	function deleteHomeWork(){
		
		var choosedLessonId = $('input[name="choosedLessonHWedit"]').val();
		
		$.ajax({
			type : 'POST',
			url : "${pageContext.request.contextPath}/AjaxController",
			dataType : "json",
			data : {
				"command" : "deleteHomeWork",
				"choosedLessonId" : choosedLessonId
			},
			complete : function(data) {
				
				response = data.responseJSON;
				result = response.result;
				//alert('result = ' + result)
				if(result == 1){
					Materialize.toast('Home work successfully deleted', 2000);
					
					$('#blockquote'+choosedLessonId+'').empty().append('You didnt give homework');
					$("#editHomeWork"+choosedLessonId+"").attr("onclick","editHomeWork("+choosedLessonId+","+0+")");
					$('#editHWbut').empty().append('Add homework');
					closeEditHomeWorkWindow();
					
				}else{
					Materialize.toast('Error, try again', 2000);
				}
				
			}
		});
	}