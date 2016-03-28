<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="e" uri="http://epam.edu/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <jsp:include page="../requirements.jsp"></jsp:include>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/jquery.maskedinput.js"></script>

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

	});
</script>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
    <title>Tests activity</title>
</head>
<script>
$(document).ready(function($) {
	getAllDirections();
	
})
	
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
		console.log('STATUS === ' + status);
		if(status == 1){
			processData(xml);
			
		}else{
			Materialize.toast($.t('noRegisteredDirections'), 1000);
		}
	}
	
	function processData(xml){
		
		$(xml).find('direction').each(
					function(index) {
						var direction_id = $(this).find('direction_id').text();	
						var direction_name = $(this).find('direction_name').text();	
						var recruter_id = $(this).find('recruter_id').text();	
						var date_of_testing = $(this).find('date_of_testing').text();	
						var place_of_testing = $(this).find('place_of_testing').text();	
						var is_active = $(this).find('is_active').text();	
						var description = $(this).find('description').text();
						var direction_status = $(this).find('direction_status').text();
						var isSubscribed = $(this).find('isSubscribed').text();
						
						$('#block_direction').append('<div class="card col s5" style="margin-left:3%;height:550px;" id="block' + direction_id + '"></div>');
						$('#block'+ direction_id).append('<div class="card-image waves-effect waves-block waves-light" id="image'+ direction_id +'"></div>');
						$('#image'+ direction_id).append('<img class="activator" style="height:200px;" src="${pageContext.request.contextPath}/images?type=directions&id='+direction_id+'">');
						$('#block'+ direction_id).append('<div class="card-content" id="content'+ direction_id +'"></div>');
						$('#content'+ direction_id).append('<span class="card-title activator grey-text text-darken-4">'+direction_name+'<i class="mdi-navigation-more-vert right"></i></span>');
						if(direction_status == 1){
							$('#content'+ direction_id).append('<p style="color:blue;" id="noInterviewsText'+ direction_id+'">'
							+$.t('noAdopt')
							+'</p>');
						
							
							if(isSubscribed == 'true'){
								$('#content'+ direction_id).append('<p style="color:rgb(94, 197, 250);padding-top:35%;font-size:13px;" id="switcherTitle'+direction_id+'">'
								+$.t('unsubscribeFromUpdate')
								+'</p>');
								$('#content'+ direction_id).append(' <div class="switch" id="switcher'+direction_id+'"></div>');
								$('#switcher'+ direction_id).append("<label> Off<input type='checkbox' onchange='subscribeNew("+direction_id+",$(this).is(\":checked\"))' checked><span class='lever'></span>On</label>");
							}else{
								$('#content'+ direction_id).append('<p style="color:rgb(94, 197, 250);padding-top:35%;font-size:13px;" id="switcherTitle'+direction_id+'">'
								+$.t('subscribeToUpdates')
								+'</p>');
								$('#content'+ direction_id).append(' <div class="switch" id="switcher'+direction_id+'"></div>');
								$('#switcher'+ direction_id).append("<label> Off<input type='checkbox' onchange='subscribeNew("+direction_id+",$(this).is(\":checked\"))'><span class='lever'></span>On</label>");
							}
							
						}else if(direction_status == 2){
							var interview_description = $(this).find('interview_description').text();
							
							var textWithDescription;
							if(interview_description.trim() != ''){
								textWithDescription = '<p style="font-size:15px;"><b>'
								+$.t('youRegisteredForTesting')
								+'</b></br>'+date_of_testing+' '+ place_of_testing +'</br>*<i style="color: rgb(215, 187, 0);"> '+interview_description+' </i>*</p>'
							}else{
								textWithDescription = '<p style="font-size:15px;"><b>'
									+$.t('youRegisteredForTesting')
								+'</b></br>'+date_of_testing+' '+ place_of_testing +'</br></p>'
							}
							
							$('#content'+ direction_id).append(textWithDescription);
							$('#content'+ direction_id).append(' <p><a href="#!" onclick="cancelApplicationOnTests('+direction_id+')" style="margin-bottom:10px;">'
							+$.t('cancelApplication')
							+'</a></p>');
						}else{
							
							var interview_id = $(this).find('interview_id').text();
							$('#content'+ direction_id).append('<p id="noInterviewsText'+ direction_id+'"><a href="#!" onclick="openModalView('+interview_id+')">'
							+$.t('registerForTesting')
							+'</a></p>');
							//ADDED!!!!
							if(isSubscribed == 'true'){
								$('#content'+ direction_id).append('<p style="color:rgb(94, 197, 250);padding-top:35%;font-size:13px;" id="switcherTitle'+direction_id+'">'
								+$.t('unsubscribeFromUpdate')
								+'</p>');
								$('#content'+ direction_id).append(' <div class="switch" id="switcher'+direction_id+'"></div>');
								$('#switcher'+ direction_id).append("<label> Off<input type='checkbox' onchange='subscribeNew("+direction_id+",$(this).is(\":checked\"))' checked><span class='lever'></span>On</label>");
							}else{
								$('#content'+ direction_id).append('<p style="color:rgb(94, 197, 250);padding-top:35%;font-size:13px;" id="switcherTitle'+direction_id+'">'
								+$.t('subscribeToUpdates')
								+'</p>');
								$('#content'+ direction_id).append(' <div class="switch" id="switcher'+direction_id+'"></div>');
								$('#switcher'+ direction_id).append("<label> Off<input type='checkbox' onchange='subscribeNew("+direction_id+",$(this).is(\":checked\"))'><span class='lever'></span>On</label>");
							}
						}
						$('#block'+ direction_id).append('<div class="card-reveal" id="info'+ direction_id +'"></div>');
						
						$('#info'+ direction_id).append('<span class="card-title grey-text text-darken-4">'+direction_name+'<i class="mdi-navigation-close right"></i></span>');
						$('#info'+ direction_id).append('<p>'+description+'</p>');
					
					
			})	
		
		}
	
	function cancelApplicationOnTests(direction_id){
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "cancelApplicationOnTest",
				"directionId" : direction_id
			},
			complete : function(data) {
				processCancelApplicationOnTests(data);
			}
		});
		
	}
	
	function processCancelApplicationOnTests(data){
		var xml = data.responseXML;
		var status = $(xml).find('status').text();
		
		if(status == 1){
			Materialize.toast($.t('applicationCanceled'), 3000)
			$('#block_direction').empty();
			$('#block_direction').append('<div style="text-align:center;" class="text1">'
			+$.t('allCourses') 
			+'</div>');
			
			getAllDirections();
		}else{
			Materialize.toast($.t('error_try_later'), 3000)
		}
	}
	
	function subscribeNew(directionId, active){
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "subscribeTraineeToGetNews",
				"directionId" : directionId,
				"active" : active
			},
			complete : function(data) {
				var response = data.responseXML.documentElement.firstChild.nodeValue;
				if (response == 1) {
						$('#switcherTitle'+ directionId).remove();
					if (Boolean(active)) {
						Materialize.toast($.t('subscriptionActivated'), 3000);
						$('#noInterviewsText'+ directionId).after('<p style="color:rgb(94, 197, 250);padding-top:35%;font-size:13px;" id="switcherTitle'+directionId+'">'
						+$.t('unsubscribeFromUpdate')
						+'</p>');

					} else {
						Materialize.toast($.t('subscriptionDeactivated'), 3000);
						$('#noInterviewsText'+ directionId).after('<p style="color:rgb(94, 197, 250);padding-top:35%;font-size:13px;" id="switcherTitle'+directionId+'">'
						+$.t('subscribeToUpdates')
						+'</p>');
	
					}
				} else {
					Materialize.toast($.t('error_while_changing'), 3000);
				}
			}
		});
	}
	
</script>

<body bgcolor="#FAFAFA">

<jsp:include page="header.jsp"></jsp:include>
<jsp:include page="applyOnTestModal.jsp"></jsp:include>
<jsp:include page="trainee_panel_sidenav.jsp"></jsp:include>

<c:set var="trainee" value="${sessionScope.logined_user}"/>
<fmt:setLocale value="en_US" scope="session"/>
            
            <div class="parallax-container">
   				 <div class="parallax"><img src="${pageContext.request.contextPath}/img/trainee/testsParallax.jpg"></div>
  			</div>
			 <div class="container">
			       
			            <div class="card-panel white">
			                <div class="row" id="block_direction">
			                 	<div style="text-align:center;" class="text1"><e:msg key="allCourses"></e:msg> </div>
  				  				 	  
  								  
		                </div>
		            </div>
		        </div>
		
		
<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>