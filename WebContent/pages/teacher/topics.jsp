<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="../requirements.jsp"></jsp:include>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/pdfobject.js"></script>
<script src="${pageContext.request.contextPath}/css/multiDrop/dropzone.js"></script>
		<link href="${pageContext.request.contextPath}/css/multiDrop/dropzone.css"
	rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/multiDrop/basic.css"
	rel="stylesheet" />

<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/knockout-3.3.0.js"></script>

<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<script>
	
	function openEditModal(event,topicId) {
		event.stopPropagation();
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getAttachmentsAndDataForTopic",
				"topicId" : topicId,
			},
			complete : function(data) {
				editResponse(data);
				}
		});
	}
	
	function editResponse(data){
		var xml = data.responseXML;

$('#direction2').empty();
$('#new_topic_content2').empty();

		var topicTitle;
		var content;
		var direction;
		$(xml)
		.find('topics')
		.each(
				function(index) {
		topicTitle=$(this).find('topicTitle').text();
		content=$(this).find('content').text();
		direction=$(this).find('direction').text();
		$('#direction2').val(direction);
		$('#new_topic_content2').val(content);
				

		 $(".input-field #editTopicTitle").val( topicTitle); 
				});
		var count=1;
		$('#forLinks').empty();
		$('#forLinks')
		.append('<h5>'
				+$.t('currentAttach')
				+':</h5>');
		$(xml)
				.find('attachments')
				.each(
						function(index) {
							var title = $(this).find('title').text();
							var extension = $(this).find('extension').text();
							var attachId = $(this).find('attachId').text();
							var topicId = $(this).find('topicId').text();
							$('#forLinks')
									.append('<div class="col s6  m8 l6" id="topicFile'
											+attachId
											+'" style="text-align:center; height:180px;">'
											+'<img width="100" height="100" src="'
											+'${pageContext.request.contextPath}/images?type=extensions&extension='
											+ extension + '\"> <br>'
											+'<span class="fileBlock">'
											+'<a href="#!" id=attach" style="word-break: break-all; width:95%"'
											+count++
											+'"> '
											+title 
											+extension
											+'</a> '
											+'<a style="cursor: pointer;" onclick="deleteFile('
											+attachId
											+','
											+topicId
											+', \'div'
											+(count-1)
											+'\')"><br><i class="small material-icons">delete</i><br></a>'
											
											+'</span>'
											+'</div>'
												);
						});
		if(count==1) { $('#forLinks')
			.append("<br><br><br><h6>"
			+$.t("youHaveNoAttachYet")
			+"</h6>"); }
		var count2=1;
		
		$('#mFooter').empty();
		$(xml)
		.find('topics')
		.each(
				function(index) {
					var topicId = $(this).find('Id').text();
			$('#mFooter').append('<a id="butAddDirection2" onclick="saveEdition('
			+topicId
			+');"'
			+'class=" modal-action  waves-effect waves-green btn-flat left" style="position: inherit;">'
			+$.t("save")
			+'</a>'
			);
				});	
	
			$('#editTopicModal').openModal();
			$('#labelEditTopicTitle').addClass("active");
			
	}
	
	function deleteFile(attachId,topicId,divId){
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "deleteAttachment",
				"attachmentId" : attachId,
				"topicId" : topicId,
			},
			complete : function(data) {
				deleteRespone(attachId,data,topicId);
				$('#attachFile'+attachId).remove();
				}
		});
	}
	
	function deleteRespone(attachId,data,topicId){
		var response = data.responseXML.documentElement.firstChild.nodeValue;
		if(response==true) 	Materialize.toast($.t("successDelete"), 3000);
		$('#topicFile'+attachId+'').remove();
	if($('.fileBlock').length==0) {
		$('#forLinks').empty();
	$('#forLinks').append('<h5>'
			+$.t("currentAttach")
			+':</h5><br><br><br><h6>'
			+$.t("youHaveNoAttachYet")
			+'</h6>');
	$('#forAttach'+topicId).empty();
	$('#forAttach'+topicId).append('<h5 style="text-align: center">'
	+$.t("youHaveNoAttach")
	+'</h5>');
	}	
	}
	function deleteTopic() {
		var topicId=$('#forDelete').val();
		var nameOfUl=$('#nameOfUl').val();
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "deleteTopic",
				"topicId" : topicId,
			},
			complete : function(data) {
				var response = data.responseXML.documentElement.firstChild.nodeValue;
				if(response=='true') { 					
					$('#li'+topicId+'').remove();
					if($('ul#'+nameOfUl+' li').length==0) $('#div'+nameOfUl).remove();
				if($('.forCountingDirection').length==0)  $('#forNewDirection').append('<h3 style="text-align: center">'
						+$.t("youHaveNoTopics")
						+'</h3>');
				Materialize.toast($.t("successDelete"), 3000);
				}
				else Materialize.toast($.t("somethingWrong"), 3000);
			}
		});
}
	function deleteTopicfunc(event,data,data2){
		event.stopPropagation();
		$('#forDelete').val(data);
		$('#nameOfUl').val(data2);
		$('#deleteTopicModal').openModal();
	}
	
	 function removeMultipleWhiteSpaces(value){
  	   return value.replace(/\s\s+/g, ' ');  	  
  	 }
	
	function saveEdition(Id) {
		var formData = new FormData();
		var title ;
		var content;
		var direction ;
		if($('#editTopicTitle').val()!=null&&$('#new_topic_content2').val()!=null&&$('#direction2').val()!=null) {
		 title = prepareText($('#editTopicTitle').val());
		 content = prepareText(removeMultipleWhiteSpaces($('#new_topic_content2').val()));
		 direction = prepareText($('#direction2').val());
		} else { Materialize.toast($.t("fillAllFields"), 3000);
		return false;
		}
	
		formData.append('title', title);
		formData.append('content', content);
		formData.append('direction', direction);
		var allOk=true;
		formData.append('topicId', Id);
		var dz=Dropzone.forElement('#dropzone2');
		
		
		for (var int = 0; int < dz.files.length; int++) {
			
			 formData.append('attachment' + int, dz.files[int]);
		}
		setTimeout(function() {dz.removeAllFiles();}, 1500);
		dz.processQueue();
		
		if(allOk) {
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=saveEditionOfTopic",
					dataType : "xml",
					data : formData,
					async : true,
					cache : false,
					contentType : false,
					processData : false,
					complete : function(data) {
						processAddingResult2(data);
					}
				});
	}
	}
	 function prepareText(text){
		  return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
		 }
	
	function addNewTopic() {
		var formData = new FormData();
		var title ;
		var content;
		var direction ;
		if($('#new_topic_title').val()!=null&&$('#new_topic_content').val()!=null&&$('#direction').val()!=null) {
		 title = prepareText($('#new_topic_title').val());
		 content = prepareText(removeMultipleWhiteSpaces($('#new_topic_content').val()));
		 direction = prepareText($('#direction').val());
		} else { Materialize.toast($.t("fillAllFields"), 3000);
		return false;
		}
		var allOk=true;
		
		formData.append('title', title);
		formData.append('content', content);
		formData.append('direction', direction);
		var dz=Dropzone.forElement('#dropzone');
		
		
		for (var int = 0; int < dz.files.length; int++) {
			
			 formData.append('attachment' + int, dz.files[int]);
		}
		setTimeout(function() {dz.removeAllFiles();}, 1500);
		dz.processQueue();
		if(allOk) {
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=addNewTopic",
					dataType : "xml",
					data : formData,
					async : true,
					cache : false,
					contentType : false,
					processData : false,
					complete : function(data) {
						var xml = data.responseXML;
						var response = $(xml).find('result').find('status').text();
						var topicId = $(xml).find('result').find('topicId').text();
						var directionName=$(xml).find('result').find('directionName').text();
						if (response == 1) {
							Materialize.toast("Added", 3000);
							processAddingResult(xml,title,content,direction,topicId,directionName);		
						} else if (response == 2) {
							Materialize.toast($.t("fillAllFields"), 3000);
						}else if(response == 3){
							Materialize.toast($.t("incorrectFormat"), 3000);
						}
						if(response != 2 || response != 3){
							cleanAllFields();
						}
					}
				});
	}
	}
	
	function cleanAllFields(){
		$('#new_topic_title').val('');
		$('#new_topic_content').val('');
		$('#direction').val('0');
		 countOfFileUploaders = 1;
		 countOfFileUploaders2 = 1;
	}

	function processAddingResult(xml,title,content,direction,topicId,directionName) {
		if($('.forCountingDirection').length==0) $('#forNewDirection').text('');
		if($('#div' + direction).length == 0) {
			$('#forNewDirection').append(''
			+'<div class="card-panel white forCountingDirection" style="width:85%; margin-left:10%;" id="div'
			+direction+'">'
			  +'<h5>'
			  +'<img style="height:35px;" src="${pageContext.request.contextPath}/images?type=directions&id='
			  + direction
			  +'" class="circle responsive-img">'
			+directionName
			  +'</h5>'
			  +'<ul class="collapsible" data-collapsible="accordion" id="'
			  +direction
			  +'">'
			  );
		}
			 $('#'+direction).append(''					 
					+'<li id="li'
					+topicId
					+'">'
				     +'<div class="collapsible-header" ><div class="col s10">'
				     +title 
				     +'</div> <i class="small material-icons" onclick="openEditModal(event,'
				     +topicId
				     +')">edit</i>'
				     +'<i class="small material-icons" onclick="deleteTopicfunc(event,'
				    +topicId
				     +','
				     +direction
				     +')">delete</i>'
					 +'</div>'
				     +'<div class="collapsible-body">'
				     +'<div class="row">'
				    +'<div class="col s6">'
				    +'<div class="card-panel white">'
				     +'<i class="fa fa-list-alt  small blue-text"></i>'
				     +$.t("content")
				     +':<br>'
				     +'<div style="word-break: break-all; width:95%">'
				     +content
				   +'</div>' 
				   +'</div>'
				   +'</div>'
				   +'<div class="col s6">'
				   +'<div class="card-panel white">'
				   +'<i class="fa fa-paperclip small blue-text"></i>'
				   +$.t("files")
				   +': <br>'
				   +'<div class="row" id="forAttach'
				   +topicId
				   +'">'
				   +'</div>'
				   +'<br>'
				   +'</div>'
				   +'</div>'
				   +'</div> '   
				   +'</div>'
				   +'</li>'
			 );
			 if($(xml).find('fileName').text()!='') {
			 $(xml)
				.find('files')
				.each(
						function(index) {
				fileName=$(this).find('fileName').text();
				fileExt=$(this).find('fileExt').text();
				fileId=$(this).find('fileId').text();
				
				var visibility = '';
					 if(fileExt == '.docx' ||  fileExt == '.pdf'){
						 visibility = visibility +'<a onclick=openPreviewModal(${logined_user.id},"topic",'+topicId+','+fileId+') class="btn-floating btn-small waves-effect waves-light green">'
						 visibility = visibility+'<i class="mdi-action-visibility">'
						 visibility = visibility +'</i>'
						 visibility = visibility +'</a>'
					 }
			 $('#forAttach'+topicId).append(''
					 +'<div class="col s4" style="height:250px; padding-left:0px;" id="attachFile'
					 +fileId
					 +'">'
					 +'<img width="100" height="100" src="${pageContext.request.contextPath}/images?type=extensions&extension='
					 +fileExt
					 +'">'
					 + '<div class="row">'
					 +'<div class="col s6">'
					 
					 +'</div>'
					 +'<div class="col s12" style="padding:0px;">'
					 +'<div class="row">'
					 +'<a href="${pageContext.request.contextPath}/attachments?target=topic&target_id='
					 +topicId
					 +'&attachment_id='
					 +fileId
					+'" class="btn-floating btn-small waves-effect waves-light green">'
					 +'<i class="mdi-file-file-download">'
					 +'</i>'
					 +'</a>'
					 + visibility
					 +'</div>'
					 
					 
					  +'</div> <br> <br>'
					  +'<div style="word-break: break-all; text-align:center;">'
					  +fileName
						 +fileExt	
					  +'</div>'
					  +'</div>'
					 +'</div>'
					 );
						});
			 } else {
					$('#forAttach'+topicId).append('<h5 style="text-align: center">'
							+$.t("youHaveNoAttach")
							+'</h5>');
				}
					
			 $('.collapsible').collapsible({
			      accordion : false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
			    });
			}
		
	
	function processAddingResult2(data) {
		var response = data.responseXML.documentElement.firstChild.nodeValue;
		if (response == 1) {
			 location.reload();
			Materialize.toast($.t("saved"), 3000);
		} else if (response == 2) {
			Materialize.toast($.t("fillAllFields"), 3000);
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
						<script src="${pageContext.request.contextPath}/js/jquery-filestyle.min.js"></script>
						<link href="${pageContext.request.contextPath}/css/jquery-filestyle.min.css">
<title>Topics</title>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/gurei_style.css" rel="stylesheet">
<style>
	span{
	   color:rgba(33, 150, 243, 1);
	    font-size: initial !important;
	}
</style>

</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="teacher_panel_sidenav.jsp"></jsp:include>
<div class="row">
    <div class="col s12">
      <ul class="tabs">
        <li class="tab col s6"><a href="#newTopics" style="color: blue !important;"><e:msg key="topics.newTopic"></e:msg></a></li>
        <li class="tab col s6"><a class="active" href="#allTopics" style="color: blue !important;"><e:msg key="topics.allTopics"></e:msg></a></li>
      </ul>
    </div>
    <div id="newTopics" class="col s12">
    <br> <br>
<div class="collection container" style="border:0px">  
		
		<ul class="collapsible" data-collapsible="accordion">
    <li>
      <div class="collapsible-header active"><e:msg key="topics.addingTopic"></e:msg></div>
      <div class="collapsible-body">

	<div class="row">
				<div class="col s6">
					<div class="input-field ">
						<i class="mdi-image-edit prefix blue-text"></i> <input
							id="new_topic_title" type="text" class="validate"  maxlength="30"> <label
							for="new_topic_title"><e:msg key="title"></e:msg></label>
					</div>
							
							
						<ul class="collapsible" data-collapsible="accordion">
    <li>
      <div class="collapsible-header"><i class="fa fa-plus small blue-text"></i><e:msg key="topics.addingAttachments"></e:msg></div>
      <div class="collapsible-body">
							
				<form class="dropzone" id="dropzone">  </form>
   
      <script>
      Dropzone.autoDiscover = false;
      </script>
      <script>
	$('#dropzone').dropzone({
		url: "#!",
				paramName:"files",
				maxFilesize: 20,
				autoProcessQueue: false,
				enqueueForUpload: false,
				 autoDiscover : false,
				 maxFiles : 10
	});
	
	$('#dropzone').on("addedfile",function(file){
		file.previewTemplate.click(function() {$('div#dropzone').removeFile(file); });
	});
</script>
					</div>
					</li>
					</ul>
					
					</div>
				<div class="col s6">
						<div class="input-field ">
						<i class="prefix mdi-image-dehaze prefix blue-text"></i>
						<textarea id="new_topic_content" class="materialize-textarea" style="max-height:100px" maxlength="1000"></textarea>
						<label for="new_topic_content"> <e:msg key="content"></e:msg></label>
						
						<div class="input-field col s12">
							<select name="direction" id="direction" class="browser-default">
							
								<option value="0" disabled selected><e:msg key="topic.chooseDirection"></e:msg></option>
								<c:forEach items="${allDirections}" var="j">
									<option value="${j.id }"><c:out value="${j.name }"/></option>
								</c:forEach>
							</select> <label></label>
							<br>
				<br>
						</div>
						
				</div>
			</div>
			<div class="col s11" style="margin: 0 auto; text-align: right;">
<a id="butAddDirection" onclick="addNewTopic();"
					class="btn-floating btn-large waves-effect waves-light blue"	style="position: right; font-size:12px;"><e:msg key="add"></e:msg></a>
	
			</div>

</div>
</div>
    </li>
   
  </ul>
		
			
			
</div>
</div>
     <div id="allTopics" class="col s12">
   
   
   	<div class="collection container" style="border: 0px;" id="forNewDirection">
   <c:if test="${empty directions}">
			<h3 style="text-align: center"><e:msg key="topics.youHaveNoTopics"></e:msg></h3>
		</c:if>
   	<c:forEach var="k" items="${directions }">
   <div class="card-panel white forCountingDirection" style="width:85%; margin-left:10%;" id="div${k.id }">
  <h5> <img style="height:35px;" src="${pageContext.request.contextPath}/images?type=directions&id=${k.id }" class="circle responsive-img"><c:out value="${k.name }"/></h5>
   
   <ul class="collapsible" data-collapsible="accordion" id="${k.id }">
   <c:forEach var="j" items="${topics }">
										<c:if test="${k.id==j.directionId }">
    <li id="li${j.id }">
      <div class="collapsible-header" ><div class="col s10"><c:out value="${j.title }"/> </div> <i class="small material-icons" onclick="openEditModal(event,${j.id })">edit</i> <i class="small material-icons" onclick="deleteTopicfunc(event,${j.id },'${k.id }')">delete</i> </div>
      <div class="collapsible-body">
    <div class="row">
    <div class="col s6">
     <div class="card-panel white">
     <i class="fa fa-list-alt  small blue-text"></i><e:msg key="content"></e:msg>:<br>
     <div  style="word-break: break-all; width:95%">																
     <c:out value="${j.content }"/>
     </div>
    </div>
     </div>
      <div class="col s6">
      <div class="card-panel white">
      <i class="fa fa-paperclip small blue-text"></i><e:msg key="topics.attachments"></e:msg>: <br>
      <div class="row" id="forAttach${j.id }">
      <c:forEach var="i" items="${attachments }">
															<c:if test="${(i.topicId==j.id)}">
															   <c:set var="notEmpty"  value="IAmNotEmpty"/>
      <div class="col s4" style="height:250px; padding-left:0px;" id="attachFile${i.id }">
	<img width="100" height="100" src="${pageContext.request.contextPath}/images?type=extensions&extension=${i.extension }">
      <div class="row">
     
			<div class="col s12" style="padding:0px;">
		<div class="row">
			<a href="${pageContext.request.contextPath}/attachments?target=topic&target_id=${j.id }&attachment_id=${i.id }"
			 class="btn-floating btn-small waves-effect waves-light green"><i class="mdi-file-file-download"></i></a>
			
			<c:if test="${i.extension =='.docx' ||  i.extension =='.pdf'}">
				<a onclick=openPreviewModal('${logined_user.id}','topic','${j.id }','${i.id }') class="btn-floating btn-small waves-effect waves-light green"><i class="mdi-action-visibility"></i></a>
			</c:if>
			
		</div>
						
					
			 </div>
		     <br><br>
		<div  style="word-break: break-all; text-align:center;">
				<c:out value="${i.title }"/><c:out value="${i.extension }"/>	
							</div>
      </div>
													 	</div>
															</c:if>
														</c:forEach>
														<c:if test="${empty notEmpty}">
			<h5 style="text-align: center"><e:msg key="topics.youHaveNoAttachments"></e:msg></h5>
		</c:if>
			${notEmpty=null }
														
														</div>
      <br>
    </div>
    </div>
      </div>
      </div>
      </li>
      </c:if>
      </c:forEach>
      </ul>
    </div>
    </c:forEach>
   </div>
   </div>  
   
     </div>
  </div>

	<jsp:include page="../footer.jsp"></jsp:include>

	<div id="editTopicModal" class="modal"
		style="max-height: 680px !important;">
		<div class="modal-content" id="modalBody">
			<h6>
				<i class="small mdi-action-info text-teal"></i>
				<e:msg key="editingTopic"></e:msg>
				</h6>
				<div class="row" id="modalRow">
				<div class="col s6 m8 l6 ">
				<div class="input-field ">
				<input type="hidden" value="" id="direction2"/>
				<i class="mdi-communication-email prefix"></i> <input id="editTopicTitle" type="text" class="validate"  maxlength="30">
				<label for="editTopicTitle" id="labelEditTopicTitle">
				<e:msg key="title"></e:msg>
				</label>
				</div>
				<div class="input-field">
				<textarea id="new_topic_content2" class="materialize-textarea" style="max-height:100px"  maxlength="1000">
				</textarea>
				<label for="new_topic_content2" class="active">
				<e:msg key="content"></e:msg>
				</label> 
				</div> 
				</div> 
				<div class="col s6 m8 l6 " style="text-align: left;" id="forLinks"> 	
					</div>
					</div>
					<div class="row">
		<div class="col s6 offset-s6"> <h5>
		<e:msg key="newAttach"></e:msg>:
		</h5>
		<form class="dropzone" id="dropzone2">
		</form>	
		   <script>
      Dropzone.autoDiscover = false;
      </script>
      <script>
	$('#dropzone2').dropzone({
		url: "#!",
				paramName:"files",
				maxFilesize: 20,
				autoProcessQueue: false,
				enqueueForUpload: false,
				 autoDiscover : false,
				 maxFiles : 10
	});
	
	$('#dropzone2').on("addedfile",function(file){
		file.previewTemplate.click(function() {$('div#dropzone2').removeFile(file); });
	});
</script>
</div>  
		</div>
		</div>
		<div class="modal-footer" id="mFooter">
		</div>
		<br> <br> <br> <br>
		</div>
		
		<div id="deleteTopicModal" class="modal purple lighten-5 red-text text-accent-2"
		style="max-height: 680px !important;">
		<div class="modal-content">
			<h6 class="black-text">
				<i class="small mdi-action-info"></i> <e:msg key="topics.deletingTopic"></e:msg>
			</h6>
			<div class="row">
			<input type="hidden" id="forDelete" value=""/>
			<input type="hidden" id="nameOfUl" value=""/>
				<div class="col s12">
					<input type="hidden" id="remove_id"/>
					<h5 class="center-align"><e:msg key="topics.deletingTopicConfirm"></e:msg></h5>
				</div>
			</div>
			 <div class="modal-footer purple lighten-5">
      <a href="#!" class="modal-action modal-close waves-effect waves-red btn-flat "><e:msg key="cancel"></e:msg></a> 
      <a  onclick="deleteTopic();" class=" modal-action modal-close waves-effect waves-green btn-flat"><e:msg key="confirm"></e:msg></a>
    </div> 
		</div>
	</div>	
	
	<div id="previewModal" class="modal modal-fixed-footer">
					    
		<div id="embed" style="width:90%;height:90%;margin-left:5%;margin-top:3%;"></div>	
							
	 </div>
	
</body>
</html>