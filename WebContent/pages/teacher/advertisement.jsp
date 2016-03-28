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
	src="${pageContext.request.contextPath}/js/knockout-3.3.0.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/pagination/jquery.quick.pagination.min.js"></script>

<link
	href="${pageContext.request.contextPath}/css/pagination/styles.css"
	type="text/css" rel="stylesheet" media="screen,projection" />

<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />

<script type="text/javascript">
$(document).ready(function () {
	$('#pagingGroup1').quickPagination({pageSize: "10"});
	
});
	var current_id = -1;
	var inc=0;
	

	
	function removeMultipleWhiteSpaces(value){
		  
		   return value.replace(/\s\s+/g, ' ');
		  
		 }

	function createNewAdvertisement() {
		var formData = new FormData();
		var title = removeMultipleWhiteSpaces( $('#new_advertisement_title').val() );
		var content = removeMultipleWhiteSpaces( $('#new_advertisement_content').val());
		var group_id = $('#new_group_id').val();
		
		if((title.length < 4 )&&(content.length < 4 | content.length > 250 )){
			Materialize.toast($.t('invalidInput'), 3000);
		}else{
			if (title.length < 4 || title.length > 30){
				Materialize.toast($.t('invalidTitle'), 3000)
			}else{
				if (content.length < 4 || content.length > 250){
					Materialize.toast($.t('invalidContent'), 3000)
				}
				else{
					if(	group_id < 1){
						Materialize.toast($.t('Choose group'), 3000)
					}
					else{
						formData.append('title', title);
						formData.append('content', content);
						formData.append('group_id', group_id);

						$
								.ajax({
									type : 'POST',
									url : "AjaxController?command=createAdvertisement",
									data : formData,
									dataType : "xml",
									async : true,
									complete : function(data) {
										processAdvertisementCreatingResult(data,group_id,title,content);
									},
									cache : false,
									contentType : false,
									processData : false
								});
						cleanCreateAdvertisementModal();
					}
					
				}
				

			}

		}	
			
	
	}
	
	function cleanCreateAdvertisementModal(){
		$('#new_advertisement_title').val('');
		$('#new_advertisement_content').val(''); 
		$('#new_group_id').val('0');
	}
	
	 function prepareText(text){
		  return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
		 }
	function processAdvertisementCreatingResult(data, group, title, content) {
		var id = data.responseXML.documentElement.firstChild.nodeValue;
		if (id != -1) {
			
			$("#Message"+group).remove();
			
			$('#group'+group+' .row .row')
			.prepend('<div class="col s6 avd">'
			+ '<div class="card"  id="card'+id+'" style="height: 160px !important" style="max-height:160px !important"  style="padding-left: 6%; padding-right: 6%; padding-top: 4%">'
			
			
			+ '<div class=".card.small .card-content"'
			+ 'style="text-align: center">'
			+ '<div align="right">'
			+'<div align="right"'
			+'style="padding-left: 6%; padding-right: 6%; padding-top: 4%">'
			+ '<a> '
			+ '<i class="small material-icons" '
			+ 'onclick="openDeleteAdvertisementModal('+ id +', '+ group+')">delete</i>'
			+ '</a>'
			+ '</div> '
			+'<div style="text-align: center; padding-left: 6%; padding-right: 6%; padding-top: 0%">'
			+'<i class="small left material-icons">announcement</i> '
			+ '<span '
			+ 'class="card-title activator grey-text text-darken-4" style="word-break:  break-all" >'+prepareText(title)
			+ '</i> </span> </div>'
			+ '<p>'
			+ '</div>'
			
			
			+ '<div class="card-reveal">   <i class="small left material-icons">announcement</i>   '
			+ '<span class="card-title grey-text text-darken-4" style="word-break:  break-all">'+ prepareText(title)
			+ ' </span>   <i class="small left material-icons">assignment</i>   '
			+ '<div class="content" style="word-break:  break-all">'
			+ prepareText(content)
			+ '</div>'
			+ '<div align="right">'
			+ '<a>'
			+ '<i class="small material-icons"'
			+ 'onclick="openUpdateAdvertisementModal(' +id+ ')"> edit</i>'
			//+ '<i class="mdi-editor-mode-edit">Edit</i>'
			+ '</a>'
			+ '</div>'
			+ '</div>'
			
			
			+ '</div>'
			+ '</div>');
			$('#createAdvertisementModal').closeModal();
			Materialize.toast($.t('advertisement')+title + $.t('wasCreated'), 3000);
		} else {
			Materialize.toast($.t('errorCreatingAdvertisement'), 3000)
		}
	}

	function openCreateAdvertisementModal() {
		document.getElementById("new_advertisement_title").value = "";
		$(".input-field #new_advertisement_title").removeClass("active");
		
		document.getElementById("new_advertisement_content").value = "";
		$(".input-field #new_advertisement_content").removeClass("active");	
		$('#createAdvertisementModal').openModal();
		$(document).ready(function() {
		    $('select').material_select();
		  });
		
	}

	
	
	function openDeleteAdvertisementModal(id, idGroup) {   '+id+'
		$('#deleteAdvertisementModal').openModal();
		$('#deleteAdvertisementModal').append(' <div class="hiddenBlock"> <input type="hidden" value="'+ id +'" id="id'+inc+'">' +
				'<input type="hidden" value="'+ idGroup +'" id="idGroup'+inc+'"> </div>');
		
		
	}
	function openUpdateAdvertisementModal(id) {
		
		current_id = id
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=getAdvertisementData",
					data : {
						'id' : id
					},
					dataType : "xml",
					async : true,
					complete : function(data) {
						bindAdvertisementDataToModal(data);
					}
				});
	}

	function bindAdvertisementDataToModal(data) {
		var xml = data.responseXML;
		$(xml).find('advertisement').each(
				function() {
					$('label[for="update_advertisement_title"]').addClass( "active" );
					$(".input-field #update_advertisement_title").val(
							$(this).find('title').text());
					$('label[for="update_advertisement_content"]').addClass( "active" );
					$('#update_advertisement_content').text(
							$(this).find('content').text());

				});

		$('#editAdvertisementModal').openModal();
	}

	function updateAdvertisement() {
		
		var id = current_id;
		var title = $('#update_advertisement_title').val();
		var content = $('#update_advertisement_content').val();

		var formData = new FormData();
		if((title.length < 4 )&&(content.length < 4 | content.length > 250 )){
			Materialize.toast($.t('invalidInput'), 3000);
		}else{
			if (title.length < 4 || title.length > 30){
				Materialize.toast($.t('invalidTitle'), 3000)
			}else{
				if (content.length < 4 || content.length > 250){
					Materialize.toast($.t('invalidContent'), 3000)
				}
				else{ 
					formData.append('id', id);
					formData.append('title', title);
					formData.append('content', content);
					$
							.ajax({
								type : 'POST',
								url : "AjaxController?command=updateAdvertisement",
								data : formData,
								dataType : "xml",
								async : true,
								complete : function(data) {
									processAdvertisementUpdatingResult(data, id, title, content);
								},
								cache : false,
								contentType : false,
								processData : false
							});
				} 

				
			}
	
		}
					}

	function processAdvertisementUpdatingResult(data, id, title, content) {
		var response = data.responseXML.documentElement.firstChild.nodeValue;
		if (response == 'true') {
			$("#card"+id+" .card-title").text(prepareText(title));
			$("#card"+id+" .content").text(prepareText(content));
			$('#editAdvertisementModal').closeModal();
			cleanUpdateAdvertisementModal()
			Materialize.toast($.t('advertisement')+title + $.t('wasUpdated'), 3000);
		} else {
			Materialize.toast($.t('errorUpdatingAdvertisement'), 3000)
		}

	}
	
	function cleanUpdateAdvertisementModal(){
		$('#new_advertisement_title').val('');
		$('#new_advertisement_content').val(''); 
	}

	function deleteAdvertisement() {
		id = document.getElementById("id"+inc).value;
		idGroup = document.getElementById("idGroup"+inc).value;
		inc=inc+1;
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=deleteAdvertisement",
					data : {
						'id' : id
					},
					dataType : "xml",
					async : true,
					complete : function(data) {
						processAdvertisementDeletingResult(data, id, idGroup);
					},
				});

	}

	function processAdvertisementDeletingResult(data, id, idGroup) {
		
		var response = data.responseXML.documentElement.firstChild.nodeValue;
		if (response == 'true') {
			$("#card"+id).remove();
			$(document).xpathEvaluate('//li[@id=\'group'+idGroup+'\']//*[@class=\'row\']/*[@class=\'row\' and not(descendant::div[contains(@class, \'card\')])]').append("<div id=\"Message"+idGroup+"\"><h5 style=\"text-align: center\">"
					+ $.t('noAnyAdvertisement')+ " </h5></div>");
			Materialize.toast($.t('advertisementWasDeleted'), 3000)
		} else {
			Materialize.toast($.t('deletingError'), 3000)
		}
		$("#hiddenBlock").remove();
		$('#deleteAdvertisementModal').closeModal();

	}
	$.fn.xpathEvaluate = function (xpathExpression) {
		   $this = this.first(); 

		   // Evaluate xpath and retrieve matching nodes
		   xpathResult = this[0].evaluate(xpathExpression, this[0], null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);

		   result = [];
		   while (elem = xpathResult.iterateNext()) {
		      result.push(elem);
		   }

		   $result = jQuery([]).pushStack( result );
		   return $result;
		}
</script>
<title>Advertisement</title>
<style type="text/css">
a:hover {
	cursor: pointer;
}
</style>
</head>
<body bgcolor="#FAFAFA">

	<script>
  window.fbAsyncInit = function() {
    FB.init({
      appId      : '317620628430037',
      xfbml      : true,
      version    : 'v2.4'
    });
  };

  (function(d, s, id){
     var js, fjs = d.getElementsByTagName(s)[0];
     if (d.getElementById(id)) {return;}
     js = d.createElement(s); js.id = id;
     js.src = "//connect.facebook.net/en_US/sdk.js";
     fjs.parentNode.insertBefore(js, fjs);
   }(document, 'script', 'facebook-jssdk'));
</script>
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="teacher_panel_sidenav.jsp"></jsp:include>

	<div class="parallax-container">
		<div class="parallax">
			<img
				src="${pageContext.request.contextPath}/img/teacher/advertisement_board.jpg">
		</div>
	</div>

	<div class="container">
		<ul class="collapsible popout" data-collapsible="accordion">
			<li class="collection-header">
				<div class="row">
					<div class="col s10" style="padding-left: 4%; !important">
						<h3>
							<e:msg key="teacher.advertisementsForYourGroups"></e:msg>
						</h3>
					</div>
					<div class="col s2">
						<div style="margin-top: 10px">

							<a onclick="openCreateAdvertisementModal()"
								class="btn-floating btn-large waves-effect right waves-light green"><i
								class="mdi-content-add"></i></a>
						</div>
					</div>

				</div>
			</li>
			<c:if test="${empty groups}">
				<h4 style="text-align: center">
					<e:msg key="teacher.youHaveNoGroupsYet"></e:msg>
				</h4>
			</c:if>

			<c:forEach items="${groups}" var="group">
				<li id="group${group.id}">
					<div class="collapsible-header">${group.title }</div>
					<div class="collapsible-body">
						<div class="row">
							<div class="row">
								<c:set var="notEmpty" />



								<c:forEach items="${advertisements}" var="advertisement">
									<c:if test="${group.id==advertisement.idGroup}">
										<c:set var="notEmpty" value="NotEmpty" />
										<div class="col s6 adv">


											<div class="card" id="card${advertisement.id}"
												style="height: 160px !important"
												style="max-height: 160px !important"
												style="padding-left: 6%; padding-right: 6%; padding-top: 4% !important">
												<div class=".card.small .card-content"
													style="text-align: center">
													<div align="right"
														style="padding-left: 6%; padding-right: 6%; padding-top: 4%">
														<a> <i class="small material-icons"
															onclick="openDeleteAdvertisementModal(${advertisement.id }, ${advertisement.idGroup })">delete</i>
															<!--onclick="deleteAdvertisement(${advertisement.id }, ${advertisement.idGroup })">delete</i> -->

														</a>

													</div>
													<div
														style="text-align: center; padding-left: 6%; padding-right: 6%; padding-top: 0%">
														<i class="small left material-icons">announcement</i> <span
															class="card-title activator grey-text text-darken-4"
															style="word-break: break-all"><c:out
																value="${advertisement.title}"></c:out> </span>
													</div>
													<p>
												</div>
												<div class="card-reveal">
													<i class="small left material-icons">announcement</i> <span
														class="card-title grey-text text-darken-4"
														style="word-break: break-all"><c:out
															value="${advertisement.title}"></c:out> </span> <i
														class="small left material-icons">assignment</i>
													<div class="content" style="word-break: break-all">
														<c:out value="${advertisement.content }"></c:out>
													</div>
													<div align="right">
														<a> <i class="small material-icons"
															onclick="openUpdateAdvertisementModal(${advertisement.id})">
																edit</i>
														</a>

													</div>
												</div>
											</div>




										</div>

									</c:if>
								</c:forEach>




								<c:if test="${empty notEmpty}">
									<div id="Message${group.id}">
										<h5 style="text-align: center">
											<e:msg
												key="teacher.thereAreNoAnyAdvertisementForThisGroupYet"></e:msg>
										</h5>
									</div>
								</c:if>
							</div>
						</div>
					</div>
				</li>

			</c:forEach>

		</ul>
	</div>
	<br>

	<jsp:include page="../footer.jsp"></jsp:include>

	<div id="createAdvertisementModal" class="modal"
		style="max-height: 680px !important;">
		<div class="modal-content">
			<h6>
				<i class="small mdi-action-info text-teal"></i>
				<e:msg key="teacher.advertisementAdding"></e:msg>
			</h6>
			<div class="row">
				<div class="input-field">

					<i class="mdi-editor-mode-edit prefix"></i> <input
						id="new_advertisement_title" type="text" length="30"
						maxlength="30" class="validate"> <label
						for="new_advertisement_title"><e:msg key="teacher.title"></e:msg></label>
				</div>

				<div class="input-field">
					<i class="mdi-editor-mode-edit prefix"></i>
					<textarea id="new_advertisement_content" length="250"
						maxlength="250" style="max-height: 100px"
						class="materialize-textarea"></textarea>
					<label for="new_advertisement_content"><e:msg
							key="teacher.content"></e:msg></label>
				</div>

				<div class="input-field col s6">
					<select id="new_group_id" class="browser-default">
						<option value="0" disabled selected><e:msg
								key="chooseGroup"></e:msg></option>
						<c:forEach items="${groups}" var="group">
							<option value="${group.id }">${group.title }</option>
						</c:forEach>
					</select><label></label>
				</div>

			</div>
		</div>
		<div class="modal-footer">
			<a id="createAdvertisement" onclick="createNewAdvertisement();"
				class=" modal-action  waves-effect waves-green btn-flat left"
				style="position: inherit;"><e:msg key="teacher.add"></e:msg></a>
		</div>
	</div>

	<div id="editAdvertisementModal" class="modal"
		style="max-height: 480px !important;">
		<div class="modal-content">
			<h6>
				<i class="small mdi-action-info text-teal"></i>
				<e:msg key="teacher.advertisementUpdating"></e:msg>
			</h6>
			<div class="row">
				<div class="input-field">
					<i class="mdi-editor-mode-edit prefix"></i><input
						id="update_advertisement_title" name="update_advertisement_title"
						type="text" length="30" maxlength="30" class="validate"> <label
						for="update_advertisement_title"><e:msg
							key="teacher.title"></e:msg></label>
				</div>
				<div class="input-field">
					<i class="mdi-editor-mode-edit prefix"></i>
					<textarea id="update_advertisement_content" length="250"
						maxlength="250" style="max-height: 100px"
						class="materialize-textarea"></textarea>
					<label for="update_advertisement_content"><e:msg
							key="teacher.content"></e:msg></label>

				</div>
			</div>
		</div>

		<div class="modal-footer">
			<a id="updateAdvertisement" onclick="updateAdvertisement()"
				class=" modal-action  waves-effect waves-green btn-flat left"
				style="position: inherit; max-height: 90px"><e:msg
					key="teacher.update"></e:msg></a>
		</div>
	</div>

	<div id="deleteAdvertisementModal" class="modal modal-fixed-footer"
		style="width: 540px !important; height: 250px !important">
		<div
			style="padding-left: 15%; padding-right: 15%; text-align: center;">
			<div class="row">
				<i class="medium mdi-action-info text-teal"></i>
				<e:msg key="teacher.sureToDeleteAdvertisement"></e:msg>
			</div>
			<div>
				<button onclick="deleteAdvertisement()"
					class="btn waves-effect waves-light"
					onmouseout="this.style.backgroundColor='#2196F3';"
					onmouseover="this.style.backgroundColor='red';" type="button">
					<e:msg key="delete"></e:msg>
					<i class="mdi-action-delete"></i>
				</button>

				<button onclick="$('#deleteAdvertisementModal').closeModal();"
					class="btn waves-effect waves-light"
					onmouseout="this.style.backgroundColor='#2196F3';"
					onmouseover="this.style.backgroundColor='#FFEB3B';" type="button">
					<e:msg key="cancel"></e:msg>
					<i class="mdi-navigation-cancel"></i>
				</button>

			</div>
		</div>
	</div>

</body>
</html>