<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../requirements.jsp"></jsp:include>

<script type="text/javascript"
	src="/EpamEducationalProject/js/jquery.maskedinput.js"></script>

<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
<title>Manage tests</title>
	
<script>
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

	$(document).ready(function() {
		getAllDirections();
	});
	
	$(document).ready(function() {
		
		var answers;
		answers = '<table><tr><td>'
		+$.t('correct')
		+'</td>';
		for (i = 1; i <= 4; i++) {
			
			answers += '<tr><td><input id="answer' + i + '" type="radio"></td>' +
			'<td><span class="input-field "><input id="answer' + i + '" type="text" class="validate">' +
			'<label for="answer' + i + '">'
			+$.t('answer') 
			+ i + '</label></span></td></tr>';
		}
		answers += '</table></tr>';
		$('#answers').append(answers);
	});
	
	function getAllDirections(){
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getAllDirections"
			},
			complete : function(data) {
				processGetAllDirections(data);
			}
		});
	
	}
	
	function processGetAllDirections(data){
		
		var xml = data.responseXML;
		var status = $(xml).find('status').text();
		if(status == 1) {
			$('#block_direction').append('<div style="text-align:center;" class="text1"><h3>'
			+$.t('directions')
			+'</h3></div>');
			processData(xml);
			
		} else {
			$('#block_direction').append('<div style="text-align:center;" class="text1"><h3>'
			+$.t('noAvailableDirections')
			+'</h3></div>');
		}
	}
	
	function processData(xml){
		
		$(xml).find('direction').each(
					function(index) {
						var direction_id = $(this).find('direction_id').text();	
						var direction_name = $(this).find('direction_name').text();	
						$('#block_direction').append('<a href="${pageContext.request.contextPath}/Controller?command=testsEdit&direction=' + direction_id +'">'
							+ '<div class="card col s3 offset-s1" style="height:300px;" id="block' + direction_id + '"></div>');
						$('#block' + direction_id).append('<div class="card-image waves-effect waves-block waves-light" id="image' + direction_id + '"></div>');
						$('#image' + direction_id).append('<img style="height:100%; width:100%;" src="${pageContext.request.contextPath}/images?type=directions&id=' + direction_id + '">');
						$('#block' + direction_id).append('<div class="card-content" id="content' + direction_id + '"></div>');
						$('#content' + direction_id).append('<span class="card-title grey-text text-darken-4">' + direction_name + '</i></span>');
		});	
	}
	
	function addNewTest() {
		var formData = new FormData();
		var question = $('#new_question').val();
		var code = $('#new_test_code').val();
		var direction = $('#direction').val();

		formData.append('question', question);
		formData.append('code', code);
		for (var i = 1; i <= 4 ; i++) {
			var answer = $('#answer' + int).val();
			formData.append('answer', 'answer' + i);
			if (true) {
				
			}
		}
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=addNewTest",
					dataType : "xml",
					data : formData,
					async : true,
					cache : false,
					contentType : false,
					processData : false,
					complete : function(data) {
						processAddingResult(data);
					}
				});
	}
	
	function processAddingResult(data) {
		var response = data.responseXML.documentElement.firstChild.nodeValue;
		if (response == 1) {
			Materialize.toast($.t('added'), 3000);
		} else if (response == 2) {
			Materialize.toast($.t('error'), 3000);
			Materialize.toast($.t('fillAllFields'), 3000);
		}
	}
</script>
</head>
<body bgcolor="#FAFAFA">
	<script>
		function exit() {
			$.ajax({
				type : 'POST',
				url : "/EpamEducationalProject/AjaxController",
				dataType : "xml",
				data : {
					"command" : "exit"
				},
				complete : function(data) {
					processExit(data);
				}
			});
		}

		function processExit(data) {

			var xml = data.responseXML;
			var status = $(xml).find('status').text();
			if (status == 1) {

				window.location.href = "/EpamEducationalProject/?act=main";
			} else {
				Materialize.toast($.t('error'), 3000);
			}

		}
	</script>
	<jsp:include page="header.jsp"></jsp:include>
	
	<div class="parallax-container" style="min-height:210px;">
	<div class="parallax"><img src="${pageContext.request.contextPath}/img/teacher/epam_directions.jpg"></div>
	</div>
		<div class="container">
			<div class="card-panel white">
				<div class="row" id="block_direction">
  				  				 	  
			</div>
		</div>
	</div>
	
	<jsp:include page="teacher_panel_sidenav.jsp"></jsp:include>
	<jsp:include page="../footer.jsp"></jsp:include>
	<div id="addTestModal" class="modal"
		style="max-height: 680px !important;">
		<div class="modal-content">
			<h6>
				<i class="small mdi-action-info text-teal"></i><e:msg key="NEW"></e:msg> ${direction.name } <e:msg key="TEST"></e:msg>
			</h6>
			<div class="row">
				<div class="col s6">
					<div class="input-field ">
						<i class="mdi-communication-live-help prefix"></i> <input
							id="new_question" type="text" class="validate"> <label
							for="new_question"><e:msg key="QUESTION"></e:msg></label>
					</div>
					<div class="input-field">
						<textarea id="new_test_code" class="materialize-textarea"></textarea>
	
					</div>
				</div>
				<div class="col s6" id="answers">
					
				</div>
			</div>
			<div class="modal-footer">
				<a id="butAddTest" onclick="addNewTest();"
					class=" modal-action  waves-effect waves-green btn-flat left"
					style="position: inherit;"><e:msg key="ADD"></e:msg></a>
			</div>
		</div>
	</div>
</body>
</html>