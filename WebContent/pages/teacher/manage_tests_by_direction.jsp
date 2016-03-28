<%@page import="edu.epam.model.Direction"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--  Scripts-->
<jsp:include page="../requirements.jsp"></jsp:include>
<jsp:include page="../code_highlight.jsp"></jsp:include>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/knockout-3.3.0.js"></script>

<script type="text/javascript">
	
	 $(document).ready(function(){
	    $('.collapsible').collapsible({
	      accordion : false
	    });
	    loadModalAdd();
	    loadModalEdit();
	    goToPage(1);
	  });

	 
	function loadModalAdd() {
		$('#answers').empty();
		var answers = "";
		for (i = 1; i <= 4; i++) {
			answers += '<tr><td><div class="input-field " style="margin-top: 0px; height: 50px;"><input name="correct" id="a' 
			+ i + '" type="radio" value="' 
			+ i + '" class="validate"><label for="a' 
			+ i + '"></label></div></td>' + 
			'<td><div class="input-field "><input id="answer' 
			+ i + '" type="text" class="validate" maxlength="500"><label for="answer' 
			+ i + '">'
			+$.t('answer') 
			+ i + '</label></div></td></tr>';
		}
		$('#answers').append(answers);
	}
	
	function loadModalEdit() {
		$('#edit_answers').empty();
		var answers = "";
		for (i = 1; i <= 4; i++) {
			answers += '<tr><td><div class="input-field " style="margin-top: 0px; height: 50px;"><input name="edit_correct" id="ea' 
			+ i + '" type="radio" value="' 
			+ i + '" class="validate"><label for="ea' 
			+ i + '"></label></div></td>' + 
			'<td><div class="input-field "><input id="edit_answer' 
			+ i + '" type="text" class="validate" placeholder="Answer' 
			+ i + '" length="500"></div></td></tr>';
		}
		$('#edit_answers').append(answers);
	}
	
	function addTestCall() {
		document.getElementById("new_question").value = "";
		$("#new_question").removeClass("active");
		document.getElementById("new_test_code").value = "";
		$("#new_test_code").removeClass("active");
		$("#answers").empty();
		loadModalAdd();
		$('#addTestModal').openModal();
	}
	
	function addNewTest() {
		
		var correct = $('input[name="correct"]:checked').val();
		var question = $('#new_question').val();
		var code = $('#new_test_code').val();
		var answer1 = $('#answer1').val();
		var answer2 = $('#answer2').val();
		var answer3 = $('#answer3').val();
		var answer4 = $('#answer4').val();
		
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "addNewTest",
				"direction" : '${direction.id}',
				"question" : question,
				"code" : code,
				"answer1" : answer1,
				"answer2" : answer2,
				"answer3" : answer3,
				"answer4" : answer4,
				"correct" : correct
			},
			complete : function(data) {
				processResultGeneral(data);
			}
		});
	}
	
	function editTest() {
		
		var id = $('#edit_id').val();
		var correct = $('input[name="edit_correct"]:checked').val();
		var question = $('#edit_question').val();
		var code = $('#edit_test_code').val();
		var answer1 = $('#edit_answer1').val();
		var answer2 = $('#edit_answer2').val();
		var answer3 = $('#edit_answer3').val();
		var answer4 = $('#edit_answer4').val();
		
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"aim" : "edit",
				"test_id" : id,
				"command" : "editTest",
				"question" : question,
				"code" : code,
				"answer1" : answer1,
				"answer2" : answer2,
				"answer3" : answer3,
				"answer4" : answer4,
				"correct" : correct
			},
			complete : function(data) {
				processResultGeneral(data);
			}
		});
	}
	
	function editChosenTest(id) {
		loadModalEdit();
		
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "editTest",
				"aim" : "getTest",
				"test_id" : id
			},
			complete : function(data) {
				setEditValues(data);
			}
		});
		
	}
	
	function setEditValues(data) {
		var xml = data.responseXML;
		var status = $(xml).find('test').find('status').text();
		if(status == 0) {
			var message = $(xml).find('test').find('message').text();
			$('#directions_block').append('<h3 class="center-align\">' + message + '</h3>');
		}
		else {
			var id = $(xml).find('test').find('id').text();
			var question = decodePunctuation($(xml).find('test').find('question').text());
			var code = decodePunctuation($(xml).find('test').find('code').text());
			var answer1 = decodePunctuation($(xml).find('test').find('answer1').text());
			var answer2 = decodePunctuation($(xml).find('test').find('answer2').text());
			var answer3 = decodePunctuation($(xml).find('test').find('answer3').text());
			var answer4 = decodePunctuation($(xml).find('test').find('answer4').text());
			var correct = $(xml).find('test').find('correct').text();
			
			document.getElementById("edit_id").value = id;
			document.getElementById("edit_question").value = question;
			document.getElementById("edit_test_code").value = code;
			document.getElementById("edit_answer1").value = answer1;
			document.getElementById("edit_answer2").value = answer2;
			document.getElementById("edit_answer3").value = answer3;
			document.getElementById("edit_answer4").value = answer4;
			$("input[name=edit_correct][value=" + correct + "]").prop('checked', true);
			$('#editTestModal').openModal();
		}
	}
	
	function decodePunctuation(str){
		 return str.replace(/&lt;/g, "<")
						 .replace(/&#60;/g, "<")
				         .replace(/&gt;/g, ">")
				         .replace(/&#62;/g, ">")
				         .replace(/&quot;/g, '"')
				         .replace(/&amp;/g, "&");
	}
	
	function removeChosenTest(id){
		document.getElementById("remove_id").value = id;
		$('#removeTestModal').openModal();
	}
	
	function removeTest() {
		var id = $('#remove_id').val();
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "editTest",
				"aim" : "removal",
				"test_id" : id
			},
			complete : function(data) {
				processResultGeneral(data);
			}
		});
		
	}
	
	function processResultGeneral(data) {
		var xml = data.responseXML;
		var status = $(xml).find('test').find('status').text();
		var info = $(xml).find('test').find('info').text();
		if (status == 1) {
			Materialize.toast(info, 3000);
			location.reload();
		} else if (status == 2) {
			var message = $(xml).find('test').find('message').text();
			Materialize.toast(info, 3000);
			Materialize.toast(message, 3000);
		}
		else {
			Materialize.toast($.t('unknownError'), 3000);
		}
	}

	function displayPage(data) {
		var xml = data.responseXML;
		var status = $(xml).find('tests').find('status').text();
		if(status == 2) {
			Materialize.toast($.t('error'), 3000);
		}
		else {
			var testPortion = $(xml).find('tests').find('testPortion').text();
			$('#paginated_area').empty();
			$('#paginated_area').append(testPortion);
			$('.collapsible').collapsible({
				accordion : false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
			});
			dp.SyntaxHighlighter.ClipboardSwf = '/flash/clipboard.swf';
			dp.SyntaxHighlighter.HighlightAll('code');
		}
	}
	
	function goToPage(page) {
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getPaginatedPortionForTestEdit",
				"table_number" : page,
				"direction" : '${direction.id}',
				"tests_amount" : '${fn:length(tests)}'
			},
			complete : function(data) {
				displayPage(data);
			}
		});
	}
	
	function addTestModal() {
		document.getElementById("new_question").value = "";
		$("#new_question").removeClass("active");
		document.getElementById("new_test_code").value = "";
		$("#new_test_code").removeClass("active");
		$("#answers").empty();
		loadModalAdd();
	}
	
	function editTestModal() {
		document.getElementById("edit_question").value = "";
		document.getElementById("edit_test_code").value = "";
		$("#edit_answers").empty();
		loadModalEdit();
	}
	
</script>
<!-- CSS MATERIAL  -->
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>Manage tests</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="teacher_panel_sidenav.jsp"></jsp:include>
	<div id="tests">
		<e:addTestCard tests="${tests }"></e:addTestCard>
		<div id="paginated_area"></div>
	</div>
	
	<jsp:include page="../footer.jsp"></jsp:include>
	
	<div id="addTestModal" class="modal indigo-text"
		style="max-height: 650px !important;">
		<div class="modal-content" style="padding-bottom: 12px;">
			<h6 class="black-text">
				<i class="small mdi-action-info"></i> <e:msg key="NEW"></e:msg> ${direction.name } <e:msg key="TEST"></e:msg>
			</h6>
			<div class="row">
				<div class="col s6">
					<div class="input-field ">
						<i class="mdi-communication-live-help prefix"></i> <input
							id="new_question" type="text" class="validate" length="350"> <label
							for="new_question"><e:msg key="teacher.question"></e:msg></label>
					</div>
					<div class="input-field">
						<textarea id="new_test_code" class="materialize-textarea" length="1000" style="max-height:150px"></textarea>
						<label for="new_test_code"><e:msg key="teacher.codeFragment"></e:msg></label>
	
					</div>
				</div>
				<div class="col s6">
					<div class="row">
						<table>
							<thead style="border-bottom-width: 0px;">
								<tr class="text" style="font-style: italic; font-size: 14px;">
									<td><e:msg key="CORRECT"></e:msg></td>
									<td><e:msg key="OPTIONS"></e:msg></td>
								</tr>
							</thead>
							<tbody id="answers">
							
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a id="butAddTest" onclick="addNewTest();"
					class="modal-action waves-effect waves-green btn-flat right"
					style="position: inherit; margin-right: 6px;"><e:msg key="teacher.add"></e:msg></a><a
					class="modal-close waves-effect waves-green btn-flat left"
					style="position: inherit;" onClick="resetNew();"><e:msg key="cancel"></e:msg></a>
			</div>
		</div>
	</div>
	
	<div id="editTestModal" class="modal indigo-text"
		style="max-height: 650px !important;">
		<div class="modal-content" style="padding-bottom: 12px;">
			<h6 class="black-text">
				<i class="small mdi-action-info"></i><e:msg key="EDIT"></e:msg> ${direction.name } <e:msg key="TEST"></e:msg>
			</h6>
			<div class="row">
				<div class="col s6">
					<input type="hidden" id="edit_id"/>
					<div class="input-field ">
						<i class="mdi-communication-live-help prefix"></i>
						<input id="edit_question" type="text" class="validate" placeholder="<e:msg key="QUESTION"></e:msg>" length="350">
					</div>
					<div class="input-field">
						<textarea id="edit_test_code" style="max-height:150px" class="materialize-textarea" placeholder="<e:msg key="CODE_FRAGMENT"></e:msg>" length="1000"></textarea>
					</div>
				</div>
				<div class="col s6">
					<div class="row">
						<table>
							<thead style="border-bottom-width: 0px;">
								<tr class="text" style="font-style: italic; font-size: 14px;">
									<td><e:msg key="CORRECT"></e:msg></td>
									<td><e:msg key="OPTIONS"></e:msg></td>
								</tr>
							</thead>
							<tbody id="edit_answers">
							
							</tbody>
						</table>
					</div>
					
				</div>
			</div>
			<div class="modal-footer">
				<a id="butAddTest" onclick="editTest();"
					class=" modal-action  waves-effect waves-green btn-flat right" 
					style="position: inherit; margin-right: 6px;"><e:msg key="submitEditing"></e:msg></a><a
							class="modal-close waves-effect waves-green btn-flat left"
							style="position: inherit;" onClick="resetEdit(); "><e:msg key="CANCEL"></e:msg></a>
			</div>
		</div>
	</div>
	<div id="removeTestModal" class="modal purple lighten-5 red-text text-accent-2"
		style="max-height: 650px !important;">
		<div class="modal-content">
			<h6 class="black-text">
				<i class="small mdi-action-info"></i><e:msg key="testRemoval"></e:msg>
			</h6>
			<div class="row">
				<div class="col s12">
					<input type="hidden" id="remove_id"/>
					<h5 class="center-align"><e:msg key="removingTest"></e:msg></h5>
				</div>
			</div>
			<div class="modal-footer purple lighten-5">
				<div class="row">
						<a id="butAddTest" onclick="removeTest();"
							class="modal-action waves-effect waves-green btn-flat right"
							style="position: inherit;"><e:msg key="REMOVE"></e:msg></a>
						<a class="modal-close waves-effect waves-green btn-flat left"
							style="position: inherit;"><e:msg key="cancel"></e:msg></a>					
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		dp.SyntaxHighlighter.ClipboardSwf = '/flash/clipboard.swf';
		dp.SyntaxHighlighter.HighlightAll('code');
	</script>
</body>
</html>