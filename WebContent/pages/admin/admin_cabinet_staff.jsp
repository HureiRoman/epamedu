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

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/jquery-ui.css">
<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
<style>
#techers_of_current_group, #available_teachers {
	border: 1px solid #eee;
	width: 300px;
	min-height: 20px;
	list-style-type: none;
	margin: 0;
	padding: 5px 0 0 0;
	float: left;
	margin-right: 10px;
}

#techers_of_current_group li, #available_teachers li {
	margin: 0 5px 5px 5px;
	padding: 5px;
	font-size: 1.2em;
	width: 300px;
}
</style>
<script>
	$(function() {
		$("#techers_of_current_group, #available_teachers").sortable({
			connectWith : ".connectedSortable"
		}).disableSelection();
	});
</script>

<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<script type="text/javascript">

$(document).ready(function () {
	document.getElementById('edit_direction_photo').onchange = function (evt) {
	    var tgt = evt.target || window.event.srcElement,
	        files = tgt.files;
	    // FileReader support
	    if (FileReader && files && files.length&&files[0].name.match(/\.(jpg|jpeg|png|gif)$/)) {
	        var fr = new FileReader();
	        fr.onload = function () {
	            document.getElementById('old_direction_photo').src = fr.result;
	        }
	        fr.readAsDataURL(files[0]);
	    }else{
	    	  Materialize.toast($.t('choose_correct_image'), 3000)
	    }
	}
	
	document.getElementById('new_direction_photo_field').onchange = function (evt) {
	    var tgt = evt.target || window.event.srcElement,
	        files = tgt.files;
	    // FileReader support
	    if (FileReader && files && files.length&&files[0].name.match(/\.(jpg|jpeg|png|gif)$/)) {
	        var fr = new FileReader();
	        fr.onload = function () {
	            document.getElementById('newDirectionPhoto').src = fr.result;
	        }
	        fr.readAsDataURL(files[0]);
	    }else{
	    	  Materialize.toast($.t('choose_correct_image'), 3000)
	    }
	}
	
	
	
	
});

	var selectedDirection = -1;
	var  selectedGroup = -1;
	function selectDirection(id, row) {
		var directionSpan = row.querySelectorAll('[name=title_span]')[0];
		directionName = directionSpan.innerHTML;
		$('#direction_field').text(directionName);
		$('#currentDirectionImage').attr('src','${pageContext.request.contextPath}/images?type=directions&id='+id);
		$('#current_group_label').empty();
		selectedDirection = parseInt(id);
		getGroupsForDir();
		getHrDataForDirection();
		$("#currentDirectionColHeader").css("display","block");
		$("#currentDirectionCol").css("display","block");
		
	}
	function selectGroup(id, row) {
		var groupSpan = row.querySelectorAll('[name=title_span]')[0];
		groupName = groupSpan.innerHTML;
		$('#current_group_label').text(groupName);
		selectedGroup = parseInt(id);
		getTeachersForGroup(id);
		$("#currentGroupColHeader").css("display","block");
		$("#currentGroupCol").css("display","block");
		
	}

	function getGroupsForDir() {
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getGroupsForDirection",
				"id" : selectedDirection
			},
			complete : function(data) {
				processGetGroups(data);
			}
		});
	}

	function getHrDataForDirection() {
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getHrDataForDirection",
				"id" : selectedDirection
			},
			complete : function(data) {
				processGetHrData(data);
			}
		});
	}

	function processGetHrData(data) {
		var xml = data.responseXML;
		$('#hr_of_dir').empty();
		$(xml)
				.find('hr')
				.each(
						function(index) {
							var id = $(this).find('id').text();
							var firstName = $(this).find('firstName').text();
							var lastName = $(this).find('lastName').text();

							$('#hr_of_dir')
									.append(
											'<div class="card-panel white"><div class="row" >'
													+ '<div class="col s3">'
													+ '<img src="${pageContext.request.contextPath}/images?type=users&id='
													+ id
													+ '" class="circle  responsive-img"></div>'
													+ '<div class="col s6">'
													+ '<div class="row"><h6>'+$.t('role_hr')+'</h6></div>'
													+ '<div class="row"><label>'
													+ firstName
													+ '   </label><label>'
													+ lastName
													+ '</label></div>'
													+ '</div>'
													+ '<div class="col s3">'
													+ '<a onclick="changeHR()" class="btn-floating btn-small waves-effect waves-light brown"><i class="mdi-content-create"></i></a>'
													+ '</div></div></div>');
						})
	}

	function changeHR() {
		bindHrsToChangeHrModal();
		
	}

	function selectHR(id) {
		$('#hr_in_modal_photo')
				.attr(
						"src",
						'${pageContext.request.contextPath}/images?type=users&id='
								+ id);
	}

	function changeHrOfDirection() {

		var hr_id = $('#change_hr_hr_id').val();
		if(hr_id){
			$
			.ajax({
				type : 'POST',
				url : "AjaxController?command=setDirectionHR",
				dataType : "xml",
				data : {
					"dir_id" : selectedDirection,
					"hr_id" : hr_id
				},
				complete : function(data) {
					var response = data.responseXML.documentElement.firstChild.nodeValue;
					if (response == 1) {
						Materialize.toast($.t('changes_saved'), 3000);
						getHrDataForDirection();
						$('#changeDirectionHR').closeModal();
					} else {
						Materialize.toast($.t('changing_error'), 3000);

					}
				}
			});
		}else{
			Materialize.toast($.t('must_choose_hr'), 3000);
		}
		
	}
	function bindHrsToChangeHrModal() {
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=getAllHr",
					async : true,
					complete : function(data) {
						processHrsDataToModal(data);
					},
					cache : false,
					contentType : false,
					processData : false
				});
	}

	function processHrsDataToModal(data) {
		var xml = data.responseXML;
		var hrs_count = 0;
		$('#change_hr_hr_id').empty();
		$(xml).find('hr').each(
				function(index) {
					hrs_count++;
					var id = $(this).find('hr_id').text();
					var fname = $(this).find('hr_name').text();
					var lname = $(this).find('hr_lastname').text();
					$('#change_hr_hr_id').append(
							'<option  value="'+id+'">' + fname + ' ' + lname
									+ '</option>');
				});
		if (hrs_count == 0) {
			Materialize.toast($.t('no_hrs'), 3000);
			$('#change_hr_hr_id').empty();
			$('#change_hr_hr_id').append(
					'<option disabled="disabled"  value="-1">No hrs</option>');
		}else{
			$('#changeDirectionHR').openModal();
		}
		
		
		$('select').material_select();
	}

	function processGetGroups(data) {
		var xml = data.responseXML;
		$('#teacherList').empty();
		$('#groupList').empty();
		$(xml)
				.find('group')
				.each(
						function(index) {
							var id = $(this).find('id').text();
							var title = $(this).find('title').text();
							var date_of_graduation = $(this).find('date__of__graduation').text();
							
							var isActive = $(this).find('is__active').text() === 'true';
							
							var showSwitcher = '';
							if(date_of_graduation == ''){
								showSwitcher =' <div class="switch"><label>Off<input  onchange="setGroupActive('
								+ id
								+ ', $(this).is(\':checked\') )" '
								+ (isActive ? "checked"
										: " ")
								+ ' type="checkbox"> <span class="lever"></span>On</label></div>';
							}else{
								date_of_graduation = date_of_graduation.split(" ")[0];
								showSwitcher = '<div>'+$.t('group_is_graduated')+' : '+ date_of_graduation +'</div>';
							}
							
							$('#groupList')
									.append(
											'<li onclick="selectGroup('
													+ id
													+ ',this)" class="collection-item avatar"><img class="circle  responsive-img"  src="${pageContext.request.contextPath}/images?type=directions&id='
													+ selectedDirection
													+ '"> <span name="title_span" class="title">'
													+ title
													+ '</span>'
													+ '<div class="secondary-content row">'
													+ '<div class="col s10">'
													+ showSwitcher
													+ '</div>'
													+ '<div class=" col s2">'
													+ '<a onclick="openEditGroupModal('
													+ id
													+ ')"'
													+ ' class="btn-floating btn-small waves-effect tooltipped waves-light brown right" data-position="top" data-delay="50"'
													+ ' data-tooltip="Edit"><i class="mdi-content-create "></i></a></div>'
													+ '</div></div> </li>')
						});

	}

	function getTeachersForGroup(id) {
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getTeachersForGroup",
				"id" : id
			},
			complete : function(data) {
				processGetTeachers(data);
			}
		});
	}
	function processGetTeachers(data) {
		var xml = data.responseXML;
		$('#teacherList').empty();
		$(xml)
				.find('teacher')
				.each(
						function(index) {
							var id = $(this).find('id').text();
							var fname = $(this).find('firstName').text();
							var lname = $(this).find('lastName').text();

							$('#teacherList')
									.append(
											'<li class="collection-item avatar"><img class="circle  responsive-img"  src="${pageContext.request.contextPath}/images?type=users&id='
													+ id
													+ '"  >'
													+ '<span class="title">'
													+ fname
													+ ' '
													+ lname
													+ '</span></li>');
						})
	}

	function openAddDirectionModal() {
		$('#new_direction_title').val('');
		$('#new_direction_description').val('');
		bindHrs();
		
	}

	function openAddGroupModal() {
		$('#new_group_title').val('');
		bindTeachers();
	
	}

	function openEditDirectionModal(id) {
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=getDirectionData",
					data : {
						'id' : id
					},
					dataType : "xml",
					async : true,
					complete : function(data) {
						bindDirectionDataToModal(data);
					}
				});
	}

	function openEditGroupModal(id) {
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=getGroupData",
					data : {
						'id' : parseInt(id)
					},
					dataType : "xml",
					async : true,
					complete : function(data) {
						bindGroupDataToModal(data,id);
					}
				});
	}
	function bindGroupDataToModal(data,id) {
		var xml = data.responseXML;
		$(xml).find('group').each(function(index) {
			$('#edit_group_title').val('');
			$('#edit_group_title').val($(this).find('title').text());
			$('#labelEditGroup').addClass('active');
			selectedGroup = parseInt(id);
		});
		
		$('#editGroupModal').openModal();
		
	}

	function bindDirectionDataToModal(data) {
		var xml = data.responseXML;
		$(xml).find('direction').each(
				function(index) {
					var id = $(this).find('id').text();
					var codeLanguage = $(this).find('codeLang').text();
					$('#edit_direction_title')
							.text($(this).find('name').text());
					$('#edit_direction_description').text(
							$(this).find('description').text());
                    
					$('#old_direction_photo').attr(
							"src",
							'${pageContext.request.contextPath}/images?type=directions&id='
									+ id);
					$('#edit_direction_code_lang').val(codeLanguage);
					$('select').material_select();
					selectedDirection = id;
				});
$('#descriptionForEditDirection').addClass('active');
$('#titleForEditDirection').addClass('active');

		$('#editDirectionModal').openModal();
$('span').css('padding','3px');
$('#editDirectionModal').css('max-height','550px');
	}

	function bindTeachers() {
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=getAllTeachers",
					async : true,
					dataType : "xml",
					complete : function(data) {
						processTeacherssData(data);
					},
					cache : false,
					contentType : false,
					processData : false
				});
	}

	function processTeacherssData(data) {
		var xml = data.responseXML;
		var teachers_count = 0;
		$('#new_group_teacher_id').empty();
		$(xml).find('teacher').each(
				function(index) {
					teachers_count++;
					var id = $(this).find('id').text();
					var fname = $(this).find('firstName').text();
					var lname = $(this).find('lastName').text();
					$('#new_group_teacher_id').append(
							'<option  value="'+id+'">' + fname + ' ' + lname
									+ '</option>');
				});
		
		if (teachers_count == 0) {
			Materialize.toast($.t('no_teachers'), 3000);
			$('#new_group_teacher_id').empty();
			$('#new_group_teacher_id')
					.append(
							'<option disabled="disabled"  value="-1">'+$.t('no_teachers')+'</option>');
		}else{
			$('#addGroupModal').openModal();
		}
		$('select').material_select();
			$('span').css('padding','3px');
			$('#addGroupModal').css('width');
	}

	function bindHrs() {
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=getAllHr",
					async : true,
					complete : function(data) {
						processHrsData(data);
					},
					cache : false,
					contentType : false,
					processData : false
				});
	}

	function processHrsData(data) {
		var xml = data.responseXML;
		var hrs_count = 0;
		$('#new_direction_hr_id').empty();
		$(xml).find('hr').each(
				function(index) {
					hrs_count++;
					var id = $(this).find('hr_id').text();
					var fname = $(this).find('hr_name').text();
					var lname = $(this).find('hr_lastname').text();
					$('#new_direction_hr_id').append(
							'<option  value="'+id+'">' + fname + ' ' + lname
									+ '</option>');
				});
		if (hrs_count == 0) {
			Materialize.toast($.t('no_hrs'), 3000);
			$('#change_hr_hr_id').empty();
			$('#change_hr_hr_id').append(
					'<option disabled="disabled"  value="-1">'+$.t('no_hrs')+'</option>');
		}else{
			$('#addDirectionModal').openModal();
		}
		$('select').material_select();
		$('span').css('padding','3px');
	}
	
	 function prepareText(text){
		  return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
		 }
	 
	function addNewDirection() {
		var formData = new FormData();
		var title = prepareText($('#new_direction_title').val());
		var description = prepareText($('#new_direction_description').val());
		var photo = $('#new_direction_photo_field')[0].files[0];
		var hrId = $('#new_direction_hr_id').val();
		var codeLang = $('#new_direction_code_lang').val();
		if ((title.length > 3) && (hrId != -1) &&(description.length>0)&&(codeLang!=null)&&(photo)&&photo.name.match(/\.(jpg|jpeg|png|gif)$/)) {
			formData.append('title', title);
			formData.append('hr_id', hrId);
			formData.append('photo', photo);
			formData.append('description', description);
			formData.append('code_lang', codeLang);
			$
					.ajax({
						type : 'POST',
						url : "AjaxController?command=addNewDirection",
						data : formData,
						dataType : "json",
						async : true,
						complete : function(data) {
							var response = data.responseJSON.status;
							if (response == 1) {
								Materialize.toast($.t('success'), 3000)
								var newDirection  =  data.responseJSON.direction;
								 $('#dirList').append(
										 '<li onclick="selectDirection('+newDirection.id+',this)" class="collection-item avatar">'+
										 '<img class="circle  responsive-img" src="/EpamEducationalProject/images?type=directions&amp;id='+newDirection.id+'">'+
										 '<span name="title_span" class="title">'+newDirection.title+'</span>'+
										 '<div class="secondary-content row"><div class="col s10">'+
										 '<div class="switch">'+
										 '<label>Off<input onchange="setDirectionActive( '+newDirection.id+', $(this).is(\':checked\') )" checked="" type="checkbox">'+
										 '<span class="lever"></span>On'+
										 '</label>'+
										 '</div>'+
										 '</div>'+
										 '<div class=" col s2">'+
										 '<a onclick="openEditDirectionModal('+newDirection.id+')" '+
										 'class="btn-floating btn-small waves-effect tooltipped waves-light brown right"  data-position="top"  data-delay="50"  data-tooltip="Edit">'+
										 '<i class="mdi-content-create  "></i>'+
										 '</a>'+
										 '</div>'+
										 '</div>'+
										 '</li>'
								 );
								$('#addDirectionModal').closeModal();
							} else {
								Materialize.toast($.t('changing_error'), 3000)
							}
						},
						cache : false,
						contentType : false,
						processData : false
					});
		}else{
			if(title.length < 3){
				Materialize.toast($.t('incorrect_title'), 3000);
				return false;
			}
			if (hrId == -1){
				Materialize.toast($.t('must_choose_hr'), 3000);	
				return false;
			}
			if(description.length==0){
				Materialize.toast($.t('must_specify_description'), 3000);	
				return false;
			}
			if(codeLang==null){
				Materialize.toast($.t('must_choose_coding_lang'), 3000);
				return false;
			}
			if(!photo){
				Materialize.toast($.t('must_specify_photo'), 3000);
				return false;
			}
			
			if(!photo.name.match(/\.(jpg|jpeg|png|gif)$/)){
				Materialize.toast($.t('choose_correct_image'), 3000);
				return false;
			}
		}
	}

	function addNewGroup() {
		if (selectedDirection != -1) {
			var title = prepareText($('#new_group_title').val());
			var teacher_id =prepareText( $('#new_group_teacher_id').val());
			var direction_id = selectedDirection;
			if (title.length > 3 && teacher_id!=-1) {
				$
						.ajax({
							type : 'POST',
							url : "AjaxController?command=addNewGroup",
							data : {
								"title" : title,
								"teacher_id" : teacher_id,
								"direction_id" : direction_id
							},
							async : true,
							dataType : "xml",
							complete : function(data) {
								var response = data.responseXML.documentElement.firstChild.nodeValue;
								if (response == 1) {
									Materialize.toast($.t('success'), 3000)
									getGroupsForDir();
									$('#addGroupModal').closeModal();
								} else {
									Materialize.toast($.t('error'), 3000)
								}
							}
						});
			}else{
				if(title.length <= 3){
					Materialize.toast($.t('incorrect_title'), 3000);
					return false;
				}
				if(teacher_id==-1){
					Materialize.toast($.t('choose_teacher'), 3000)
					return false;
				}
			}
		} else {
			Materialize.toast($.t('choose_direction'), 3000)
		}
	}

	function setDirectionActive(id, active) {
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=setDirectonActive",
					dataType : "xml",
					data : {
						"id" : id,
						"active" : active
					},
					complete : function(data) {
						var response = data.responseXML.documentElement.firstChild.nodeValue;
						if (response == 1) {
							if (Boolean(active)) {
								Materialize.toast($.t('direction_activated'), 3000);
							} else {
								Materialize.toast($.t('direction_deactivated'), 3000);
							}
						} else {
							Materialize.toast($.t('error_while_changing'), 3000);
						}
					}
				});
	}
	function setGroupActive(id, active) {
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=setGroupActive",
					dataType : "xml",
					data : {
						"id" : id,
						"active" : active
					},
					complete : function(data) {
						var response = data.responseXML.documentElement.firstChild.nodeValue;
						if (response == 1) {
							if (Boolean(active)) {
								Materialize.toast($.t('group_activated'), 3000);
							} else {
								Materialize.toast($.t('group_deactivated'), 3000);
							}
						} else {
							Materialize.toast($.t('error_while_changing'), 3000);
						}
					}
				});
	}
	function updateDirectionData() {
		var formData = new FormData();
		var title = prepareText($('#edit_direction_title').val());
		var description = prepareText($('#edit_direction_description').val());
		var photo = $('#edit_direction_photo')[0].files[0];
		var codeLang = $('#edit_direction_code_lang').val();
		if ((title.length > 3)&&(description.length>0)&&(codeLang.length>0)) {
			formData.append('title', title);
			formData.append('photo', photo);
			formData.append('description', description);
			formData.append('id', selectedDirection);
			formData.append('code_lang', codeLang);
			$
					.ajax({
						type : 'POST',
						url : "AjaxController?command=updateDirection",
						data : formData,
						dataType : "xml",
						async : true,
						complete : function(data) {
							var response = data.responseXML.documentElement.firstChild.nodeValue;
							if (response == 1) {
								Materialize.toast($.t('data_updated'),
										3000);
								location.reload();
							} else {
								Materialize.toast($.t('error_while_changing'),
										3000);
							}

						},
						cache : false,
						contentType : false,
						processData : false
					});
		}else{
			if(title.length <= 2){
				Materialize.toast($.t('incorrect_title'), 3000);
				return false;
			}
			
			
			if(description.length==0){
				Materialize.toast($.t('must_specify_description'), 3000);	
				return false;
			}
			if(codeLang.length==0){
				Materialize.toast($.t('must_choose_coding_lang'), 3000);
				return false;
			}
		}
	}

	function updateGroupData() {
		var title =prepareText($('#edit_group_title').val());
		if (title.length > 3) { 
			$
					.ajax({
						type : 'POST',
						url : "AjaxController?command=updateGroup",
						data : {
							'title' : title,
							'id' : parseInt(selectedGroup),
						},
						dataType : "xml",
						async : true,
						complete : function(data) {
							var response = data.responseXML.documentElement.firstChild.nodeValue;
							if (response == 1) {
								Materialize.toast($.t('data_updated'),
										3000);
								getGroupsForDir();
								$('#editGroupModal').closeModal();
							} else {
								Materialize.toast(
										$.t('error_while_changing'), 3000);
							}

						}
					});
		}else{
			Materialize.toast($.t('incorrect_title'),
					3000);
		}
	}

	function OpenManageStaffOfGroupModal() {
		BindTeachersForGroupToModalList(selectedGroup);
		BindAvailableTeachersForGroupToModalList(selectedGroup);
		$('#manageStaffOfGroup').openModal();
	}

	function BindTeachersForGroupToModalList(id) {
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getTeachersForGroup",
				"id" : id
			},
			complete : function(data) {
				processTeachersForGroupToModalList(data);
			}
		});
	}

	function BindAvailableTeachersForGroupToModalList(id) {
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getAvailableTeachersForGroup",
				"id" : id
			},
			complete : function(data) {
				processAvailableTeachersForGroupToModalList(data);
			}
		});
	}

	function processAvailableTeachersForGroupToModalList(data) {
		var xml = data.responseXML;
		$('#available_teachers').empty();
		$(xml)
				.find('teacher')
				.each(
						function(index) {
							var id = $(this).find('id').text();
							var fname = $(this).find('firstName').text();
							var lname = $(this).find('lastName').text();
							$('#available_teachers')
									.append(
											'<li class=" ui-state-highlight collection-item avatar">'
													+ '<div class="row">'
													+ '<div class="col s2">'
													+ '<img src="${pageContext.request.contextPath}/images?type=users&id='
													+ id
													+ '"  class="circle responsive-img">'
													+ '</div>'
													+ '<div class="col s10"></div>'
													+ '<span class="title">'
													+ fname
													+ '  '
													+ lname
													+ ' </span>'
													+ '</div><input  value="'+id+'"  name="teacherId" hidden="hidden">'
													+ '</li>');
						})
	}

	function processTeachersForGroupToModalList(data) {
		var xml = data.responseXML;
		$('#techers_of_current_group').empty();
		$(xml)
				.find('teacher')
				.each(
						function(index) {
							var id = $(this).find('id').text();
							var fname = $(this).find('firstName').text();
							var lname = $(this).find('lastName').text();
							$('#techers_of_current_group')
									.append(
											'<li class=" ui-state-default collection-item avatar">'
													+ '<div class="row">'
													+ '<div class="col s2">'
													+ '<img src="${pageContext.request.contextPath}/images?type=users&id='
													+ id
													+ '"  class="circle responsive-img">'
													+ '</div>'
													+ '<div class="col s10"></div>'
													+ '<span class="title">'
													+ fname
													+ '  '
													+ lname
													+ ' </span>'
													+ '</div><input value="'+id+'"  name="teacherId" hidden="hidden">'
													+ '</li>');
						})
	}

	function setStaffForGroup() {
		var teachers = [];
		$('#techers_of_current_group').find('li').each(function(index) {
			var id = $(this).find('input[name="teacherId"]').val();
			teachers.push(id);
		});
		if(teachers.length>0){
			$
			.ajax({
				type : 'POST',
				url : "AjaxController?command=setGroupStaff",
				data : {
					'teachers' : teachers,
					'group_id' : selectedGroup
				},
				dataType : "xml",
				async : true,
				complete : function(data) {
					var response = data.responseXML.documentElement.firstChild.nodeValue;
					if (response == 1) {
						getTeachersForGroup(selectedGroup);
						Materialize.toast($.t('data_updated'), 3000);
						$('#manageStaffOfGroup').closeModal();
					} else {
						Materialize.toast(
								$.t('error_while_changing'),
								3000);
					}

				}
			});
		}else{
			Materialize.toast($.t('choose_at_least_one_teacher'), 3000);
		}
		
	}
</script>
<title>Staff</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="admin_panel_sidenav.jsp"></jsp:include>
	<div class="row">
		<div class="col s4">
			<div class="card-panel white ">
				<div class="row">
					<div class="col s10">
						<h4><i class="fa fa-sitemap medium blue-text"></i><e:msg key="directions"></e:msg></h4>
					</div>
					<div class="col s2" style="margin-top: 10px">
						<a onclick="openAddDirectionModal()"
							class="btn-floating btn-large waves-effect waves-light green"><i
							class="mdi-content-add"></i></a>
					</div>
				</div>
			</div>
		</div>
		<div id="currentDirectionColHeader"  style="display: none" class="col s4">
		<div class="card-panel white ">
			<div class="row">
			    <div class="col s4"><img id="currentDirectionImage" class="responsive-img"></div>
				<div class="col s8">
					<h4 id="direction_field"></h4>
				</div>
			</div>
			</div>
		</div>
		<div class="col s4" id="currentGroupColHeader"  style="display: none">
			<div class="card-panel white ">
				<div class="row">
					<div class="col s10">
						<div class="row">
							<h4><i class="fa  fa-users medium blue-text"></i><e:msg key="staff"></e:msg></h4>
							<h5 id="current_group_label"></h5>
						</div>
					</div>
					<div class="col s2" style="margin-top: 10px">
						<a onclick="OpenManageStaffOfGroupModal()"
							class="btn-floating btn-large waves-effect waves-light yellow"><i
							class="mdi-content-create"></i></a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col s4">
			<div class="card-panel white ">
				<div class="row">
					<ul id="dirList" class="collection">
						<c:forEach items="${directions}" var="direction">
							<e:dirRow direction="${direction}"></e:dirRow>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		<div class="col s4" id="currentDirectionCol"  style="display: none" >
			<div class="card-panel white ">
				<div class="row">
					<div id="hr_of_dir"></div>
				</div>
				<div class="row">
					<div class="col s10">
						<div class="row">
							<h4><e:msg key="groups"></e:msg></h4>
						</div>
					</div>
					<div class="col s2" style="margin-top: 10px">
						<a onclick="openAddGroupModal()"
							class="btn-floating btn-large waves-effect waves-light brown"><i
							class="mdi-content-add"></i></a>
					</div>
				</div>
				<ul id="groupList" class="collection">
				</ul>
			</div>
		</div>
		<div class="col s4" id="currentGroupCol"  style="display: none">
			<div class="card-panel white ">
				<div class="row">
					<ul id="teacherList" class="collection">

					</ul>

				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../footer.jsp"></jsp:include>
	<div id="addDirectionModal" class="modal"
		style="max-height: 500px !important;">
		<div class="modal-content">
			<h6>
				<i class="small mdi-action-info text-teal"></i><e:msg key="addingDirection"></e:msg>
			</h6>
			<div class="row">
				<div class="col s6">
					<div class="input-field">
						<i class="mdi-communication-call-made prefix"></i> <input
							id="new_direction_title" type="text" class="validate" maxlength="50"/> <label
							for="new_direction_title"><e:msg key="title"></e:msg></label>
					</div>
					<div class="input-field">
						<i class="mdi-editor-mode-edit prefix"></i>
						<textarea id="new_direction_description"
							class="materialize-textarea" maxlength="1000" style="max-height:100px"></textarea>
						<label for="new_direction_description"><e:msg key="description"></e:msg></label>
					</div>
					<div class="input-field">
						<select id="new_direction_code_lang">
							<option value="0" selected  disabled><e:msg key="codingLang"></e:msg></option>
							<option value="java">java</option>
							<option value="php">php</option>
							<option value="python">py, python</option>
							<option value="rb">rb, ruby, rails, ror</option>
							<option value="cpp">cpp, c, c++</option>
							<option value="js">js, jscript, javascript</option>
							<option value="sql">sql</option>
							<option value="vb">vb, vb.net</option>
							<option value="html">xml, html, xhtml, xslt</option>
							<option value="css">css</option>
							<option value="csharp">c#, c-sharp, csharp</option>
							<option value="delphi">delphi, pascal</option>
							<option value="">other</option>
						</select><label for="new_direction_code_lang"><e:msg key="codingLang"></e:msg></label>
					</div>
					<div class="input-field">
						<select id="new_direction_hr_id">
							<option disabled="disabled" value="-1"><e:msg key="chooseHR"></e:msg></option>
						</select><label><e:msg key="HR"></e:msg></label>
					</div>
				</div>
				<div class="col s6">
					<img  id= "newDirectionPhoto"
						src="${pageContext.request.contextPath}/img/add_photo.png"
						height="200" width="300">
					<div class="file-field input-field">
						<input class="file-path validate" type="text" />
						<div class="btn">
							<input type="file" 
								id="new_direction_photo_field"> <span><e:msg key="file"></e:msg></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<a id="butAddDirection" onclick="addNewDirection();" style="z-index:0;"
				class=" modal-action    waves-effect waves-green btn-flat left"><e:msg key="add"></e:msg></a>
		</div>
	</div>
	<div id="editDirectionModal" class="modal"
		style="max-height: 500px !important;">
		<div class="modal-content">
			<h6>
				<i class="small mdi-action-info text-teal"></i><e:msg key="directionEditing"></e:msg>
			</h6>
			<div class="row">
				<div class="col s6">
					<div class="input-field">
						<i class="mdi-editor-mode-edit prefix"></i>
						<textarea id="edit_direction_title" class="materialize-textarea" maxlength="50"></textarea>
						<label class="active" for="edit_direction_title" id="titleForEditDirection"><e:msg key="title"></e:msg></label>
					</div>
					<div class="input-field">
						<i class="mdi-editor-mode-edit prefix"></i>
						<textarea id="edit_direction_description"
							class="materialize-textarea" maxlength="1000" style="max-height:100px"></textarea>
						<label class="active" for="edit_direction_description" id="descriptionForEditDirection"><e:msg key="description"></e:msg></label>
					</div>
					<div class="input-field">
						<select id="edit_direction_code_lang">
							<option value="0" selected  disabled><e:msg key="codingLang"></e:msg></option>
							<option value="java">java</option>
							<option value="php">php</option>
							<option value="python">py, python</option>
							<option value="rb">rb, ruby, rails, ror</option>
							<option value="cpp">cpp, c, c++</option>
							<option value="js">js, jscript, javascript</option>
							<option value="sql">sql</option>
							<option value="vb">vb, vb.net</option>
							<option value="html">xml, html, xhtml, xslt</option>
							<option value="css">css</option>
							<option value="csharp">c#, c-sharp, csharp</option>
							<option value="delphi">delphi, pascal</option>
							<option value="">other</option>
						</select> <label for="edit_direction_code_lang"></label>
					</div>
				</div>
				<div class="col s6">
					<img id="old_direction_photo" width="300" height="300">
					<div class="file-field input-field">
						<input class="file-path validate" type="text" />

						<div class="btn">
							<input id="edit_direction_photo" type="file"> <span><e:msg key="file"></e:msg></span>
						</div>
						
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<a id="submitDirectionModerating" onclick="updateDirectionData();"
				class=" modal-action    waves-effect waves-green btn-flat left" style="z-index:0;"><e:msg key="save"></e:msg></a>
		</div>
	</div>

	<div id="addGroupModal" class="modal"
		style="max-height: 500px !important;">
		<div class="modal-content">
			<h6>
				<i class="small mdi-action-info text-teal"></i> <e:msg key="groupAdding"></e:msg>
			</h6>
			<div class="row">
				<div class="col s11">
					<div class="input-field">
						<i class="mdi-communication-email prefix"></i> <input
							id="new_group_title" type="text" class="validate" maxlength="50"> <label class="active"
							for="new_group_title"><e:msg key="title"></e:msg></label>
					</div>
					<div class="input-field">
						<select id="new_group_teacher_id">
							<option disabled="disabled" value="-1"><e:msg key="chooseTeacher"></e:msg></option>
						</select><label><e:msg key="teacher_role"></e:msg></label>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<a id="butLogIn" onclick="addNewGroup();"  style="z-index:0;"
				class=" modal-action  waves-effect waves-green btn-flat left"><e:msg key="add"></e:msg></a>
		</div>
	</div>

	<div id="editGroupModal" class="modal"
		style="max-height: 500px !important;">
		<div class="modal-content">
			<h6>
				<i class="small mdi-action-info text-teal"></i><e:msg key="groupEdit"></e:msg>
			</h6>
			<div class="row">
				<div class="col s6">
					<div class="input-field">
						<i class="mdi-editor-mode-edit prefix"></i>
						<textarea id="edit_group_title" class="materialize-textarea" maxlength="50"></textarea>
						<label for="edit_group_title" id="labelEditGroup"><e:msg key="title"></e:msg></label>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<a id="submitGroupModerating" onclick="updateGroupData();" style="z-index:0;"
				class=" modal-action   waves-effect waves-green btn-flat left"><e:msg key="save"></e:msg></a>
		</div>
	</div>

	<div id="changeDirectionHR" class="modal"
		style="max-height: 500px !important;">
		<div class="modal-content">
			<h6>
				<i class="small mdi-action-info text-teal"></i><e:msg key="hrChanging"></e:msg>
			</h6>
			<div class="row">
				<div class="col s6">
					<div class="input-field">
						<select id="change_hr_hr_id" onchange="selectHR(this.value)">
							<option disabled="disabled" value="-1"><e:msg key="chooseHR"></e:msg></option>
						</select><label><e:msg key="HR"></e:msg></label>
					</div>
				</div>
				<div class="col s6">
					<img id="hr_in_modal_photo" height="200" width="300">
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<a id="submitGroupModerating" onclick="changeHrOfDirection();" style="z-index:0;"
				class=" modal-action    waves-effect waves-green btn-flat left"><e:msg key="save"></e:msg></a>
		</div>
	</div>
	<div id="manageStaffOfGroup" class="modal"
		style="max-height: 500px !important;">
		<div class="modal-content">
			<h6>
				<i class="small mdi-action-info text-teal"></i><e:msg key="staffManagment"></e:msg>
			</h6>
		</div>
		<div class="row ">
			<div class="col s6">
			    <h5><e:msg key="teachersOfGroup"></e:msg></h5>
				<ul id="techers_of_current_group" class="connectedSortable">
				</ul>
			</div>
			<div class="col s6">
			    <h5><e:msg key="availableTeachers"></e:msg></h5>
				<ul id="available_teachers" class="connectedSortable">
				</ul>
			</div>
		</div>
		<div class="modal-footer">
			<a id="submitGroupModerating" onclick="setStaffForGroup();" style="z-index:0;"
				class=" modal-action    waves-effect waves-green btn-flat left"><e:msg key="save"></e:msg></a>
		</div>
	</div>
</body>
</html>