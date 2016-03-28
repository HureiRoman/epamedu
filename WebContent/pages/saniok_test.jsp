<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="requirements.jsp"></jsp:include>

<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>GetWheels</title>
</head>
<body bgcolor="#FAFAFA">
<script>
	function openModalView(){
		console.log('I am here');
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "get_latest_test_event"
			},
			complete : function(data){
				processResult(data);
			}
			
		});
		
	}
	
	function processResult(data){
		
		var xml = data.responseXML;
		var status = $(xml).find('directions').find('status').text();
		console.log(status);
			console.log('status == ' + status)
		if(status == 2){
			$('#info').empty().append("<p style=\"color:red;\">No available tests</p>");
			$('#tabs_directions').empty().append("<p style=\"text-align:center;\">Sorry at this time you have not available test events</p>");
			processEmptyList(data);
			$('#modal-footer').empty();
			
		}else{
			processOpenModal(data)
		}
		
		$('#applyOnTestModal').openModal();
		$('ul.tabs').tabs();
	}
	
	function processEmptyList(data){
		var xml = data.responseXML;
		var error = $(xml).find('directions').val();
		$('#appliedEvent').empty();
		
		$(xml).find('directions').each(
			function(index){
		
				$(this).find('appliedDirections').each(
						function(index){
							var direction_id;
							var direction_name;
							var recruter_id;
							$('#applied_test_event').css("display", "block");
							$(this).find('direction').each(
								function(index){
									direction_id = $(this).find('id').text();
									direction_name = $(this).find('name').text();
									recruter_id = $(this).find('recruter_id').text();
								}
							);
							$(this).find('group').each(
									function(index){
										var group_id = $(this).find('id').text();
										var group_title = $(this).find('title').text();
										var teacherId = $(this).find('teacherId').text();
										var directionId = $(this).find('directionId').text();
										var date_of_testing = $(this).find('date_of_testing').text();
										var place_of_testing = $(this).find('place_of_testing').text();		
							console.log('make append');
							$('#appliedEvent').append('<p style=\"padding-left:20px;\"> Напрямок : ' 
							+ direction_name +'. ' + date_of_testing + " " + place_of_testing+ '</p>');
									}
								);
							
							
						}	
				)
			}
		)
		
		
		
	}
	
	function processOpenModal(data){
		var xml = data.responseXML;
		var error = $(xml).find('directions').val();
		console.log('error == null ' + error == null);
		
		$('#tabs_directions').empty();
		$("[id^='direction']").remove();
		$('#appliedEvent').empty();
		
		$(xml).find('directions').each(
			function(index){
				$(this).find('directionInfo').each(
				function(index){
				var direction_id;
				$(this).find('direction').each(
					function(index){
						direction_id = $(this).find('id').text();
						var direction_name = $(this).find('name').text();
						var recruter_id = $(this).find('recruter_id').text();
						if(direction_id == 4){
							$('#tabs_directions').append("<li class=\"tab col s3\" ><a href=\"#direction"+direction_id+"\" class=\"active\" id=\""+direction_id+"\">"+ direction_name+"</a></li>");
							$('#'+ direction_id+'').append("<input type=\"hidden\" name=\"direction\" value=\""+direction_id+"\">");
						}else{
							$('#tabs_directions').append("<li class=\"tab col s3\" ><a href=\"#direction"+direction_id+"\" id=\""+direction_id+"\">"+ direction_name+"</a></li>");
							$('#'+ direction_id+'').append("<input type=\"hidden\" name=\"direction\" value=\""+direction_id+"\">");
						}
					
					}
				);
				$(this).find('group').each(
						function(index){
							var group_id = $(this).find('id').text();
							var group_title = $(this).find('title').text();
							var teacherId = $(this).find('teacherId').text();
							var directionId = $(this).find('directionId').text();
							var date_of_testing = $(this).find('date_of_testing').text();
							var place_of_testing = $(this).find('place_of_testing').text();
				$('#'+ direction_id+'').append("<input type=\"hidden\" name=\"groupId\" value=\""+group_id+"\">");			
				$('#row_directions').append("<div id=\"direction"+ direction_id+"\" class=\"col s12\" style=\"padding-top:50px;\"></div>");
				$('#direction'+ direction_id+'').append(date_of_testing).append(" " + place_of_testing);
						}
					);
				}
			)
			
			$(this).find('appliedDirections').each(
					function(index){
						var direction_id;
						var direction_name;
						var recruter_id;
						$('#applied_test_event').css("display", "block");
						$(this).find('direction').each(
							function(index){
								direction_id = $(this).find('id').text();
								direction_name = $(this).find('name').text();
								recruter_id = $(this).find('recruter_id').text();
							}
						);
						$(this).find('group').each(
								function(index){
									var group_id = $(this).find('id').text();
									var group_title = $(this).find('title').text();
									var teacherId = $(this).find('teacherId').text();
									var directionId = $(this).find('directionId').text();
									var date_of_testing = $(this).find('date_of_testing').text();
									var place_of_testing = $(this).find('place_of_testing').text();		
						console.log('make append');
						$('#appliedEvent').append('<p style=\"padding-left:20px;\"> Напрямок : ' 
						+ direction_name +'. ' + date_of_testing + " " + place_of_testing+ '</p>');
								}
							);
						
						
					}	
			)
			}
		);
	}	



</script>	
	<jsp:include page="trainee/applyOnTestModal.jsp"></jsp:include>
	<jsp:include page="loginingmodal.jsp"></jsp:include>
	<jsp:include page="header.jsp"></jsp:include>
	
	<form action="AjaxController?command=send_message" method="post">
		<input type="text" value="TEXT" name="text">
		<input type="text" value="6" name="receiver_id">
		<input type="submit" value="send">
	</form>
	
	<button class="btn waves-effect waves-light" type="submit" name="action" onclick="openModalView()">Apply on test
    	<i class="mdi-content-send right"></i>
  	</button>

	
	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>