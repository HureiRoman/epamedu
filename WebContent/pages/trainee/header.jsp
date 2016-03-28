<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>

<script>

function setLanguage(lang) {
	$.ajax({
		type : 'POST',
		url : "AjaxController",
		dataType : "xml",
		data : {
			"command" : "changeLanguage",
			"language" : lang
		},
		complete : function(data) {
			location.reload();
		}
	});
}

$( document ).ready(function() {
	 raceChecked("${logined_user.roleType}");
});
	function raceChecked(role){
		if((role != "TRAINEE") && (role != "GRADUATE")){
			window.location.href = '${pageContext.request.contextPath}/?act=forbidden';
		}
	}

</script>


<script type="text/javascript">
	function send(){
		var interview_id = $('.tab').find('.active').find('input[name=direction]').val();
		
		
		sendApplication(interview_id);
	}
	
	function sendApplication(interview_id){
		
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "apply_to_test",
				"interview_id" : interview_id
			},
			complete : function(data){
				processSendAppResult(data);
			}
				
		});
			
	}	
	
	
	function processSendAppResult(data){
		var response = data.responseXML.documentElement.firstChild.nodeValue;
		
		 if (response == 1) {
			$('#block_direction').empty();
			$('#block_direction').append('<div style="text-align:center;" class="text1">'
			+$.t('allCourses')
			+'</div>');
			Materialize.toast($.t('youRegisteredForTest'), 3000)
		$('#tabs_directions').empty();
		$("[id^='direction']").remove();
			getAllDirections();//may be error
			//window.location.href = "g";
		} else if (response == 2 || response == 3) {
			Materialize.toast($.t('error_try_later'), 3000)
			//	window.location.href = "cabinet?act=created";
		} 
	}
		

	function openModalView(active_id){
		console.log('I am here');
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "get_latest_test_event"
			},
			complete : function(data){
				processResult(data,active_id);
			}
		});
		
	}
	
	function processResult(data,active_id){
		
		var xml = data.responseXML;
		var status = $(xml).find('directions').find('status').text();
		console.log(status);
			console.log('status == ' + status)
		if(status == 2){
			$('#info').css("display", "none");
			$('#info2').remove();
			$('#title_info').append("<p style=\"color:red;\" id=\"info2\">"
			+$.t('noAvailableTests')
			+"</p>");
			$('#tabs_directions').empty().append("<p style=\"text-align:center;\">"
			+$.t('sorryNoTests')
			+"</p>");
			$('.modal').css('height','45%');
			processEmptyList(data);
			$('#modal-footer').css("display", "none");
			
		}else{
			processOpenModal(data,active_id);
			
		}
		
		$('#applyOnTestModal').openModal();
		$('#applyOnTestModal').css('height','53%');
		$('ul.tabs').tabs();
		
	}
	
	function processEmptyList(data){
		var xml = data.responseXML;
		var error = $(xml).find('directions').val();
		$('#appliedEvent').empty();
		
		$(xml).find('directions').each(
			function(index){
		
				$(this).find('appliedDirection').each(
						function(index){
							
							
							$('#applied_test_event').css("display", "block");
							
							
									var direction_id = $(this).find('id').text();
									var direction_name = $(this).find('name').text();
									var recruter_id = $(this).find('recruter_id').text();
									var date_of_testing = $(this).find('date_of_testing').text();
									var place_of_testing = $(this).find('place_of_testing').text();		
							
							$('#appliedEvent').append('<p style=\"padding-left:20px;\">'
							+$.t('direction') 
							+' : <b>' 
							+ direction_name +'</b>. ' + date_of_testing + ' </br>'
							+$.t('interviews_place') 
							+': ' + place_of_testing+ '</p>');
							
// 							$('.modal').css('height','65%');
							$('#applyOnTestModal').css('height','65%');
							
						}
				);
			}
		)
	}
	
	function processOpenModal(data,active_id){
		var xml = data.responseXML;
		var error = $(xml).find('directions').val();
		
		$('#info').css("display", "block");
		$('#info2').css("display", "none");
		$('#modal-footer').css("display", "block");
		
		
		$('#tabs_directions').empty();
		$("[id^='direction']").remove();
		$('#appliedEvent').empty();
		
		$(xml).find('directions').each(
			function(index){
				$(this).find('directionInfo').each(
				
					function(index){
						var direction_id = $(this).find('id').text();
						var direction_name = $(this).find('name').text();
						var recruter_id = $(this).find('recruter_id').text();
						var date_of_testing = $(this).find('date_of_testing').text();
						var place_of_testing = $(this).find('place_of_testing').text();
						
						var only_date = date_of_testing.match(/[0-9]{2}-.*-[0-9]{4}/g)
						if(direction_id == active_id){
							$('#tabs_directions').append("<li class=\"tab col s1\" id=\"directOfTesting\"><a href=\"#direction"+direction_id+"\" class=\"active\" id=\""+direction_id+"\"><div style=\"height: 16px;\">"+ direction_name+"</div><div id=\"dateOfTesting\">(" +only_date+ ")</div></a></li>");
							
						}else{
							$('#tabs_directions').append("<li class=\"tab col s1\" id=\"directOfTesting\"><a href=\"#direction"+direction_id+"\" id=\""+direction_id+"\"><div style=\"height: 16px;\">"+ direction_name+"</div><div id=\"dateOfTesting\">(" +only_date+ ")</div></a></li>");
						}
							$('#'+ direction_id+'').append("<input type=\"hidden\" name=\"direction\" value=\""+direction_id+"\">");
							$('#row_directions').append("<div id=\"direction"+ direction_id+"\" class=\"col s12\" style=\"padding-top:50px;\"></div>");
							$('#direction'+ direction_id+'').append(date_of_testing).append(" </br><b>"
									+$.t('interviews_place')
									+"</b> : " + place_of_testing);
							
							$('#applied_test_event').css("display", "none");
					}
					
				);
			
			
				$(this).find('appliedDirection').each(
						function(index){
							
							
							$('#applied_test_event').css("display", "block");
							
									var direction_id = $(this).find('id').text();
									var direction_name = $(this).find('name').text();
									var recruter_id = $(this).find('recruter_id').text();
									var date_of_testing = $(this).find('date_of_testing').text();
									var place_of_testing = $(this).find('place_of_testing').text();		
							
							$('#appliedEvent').append('<p style=\"padding-left:20px;\">'
							+$.t('direction')
							+': <b>' 
							+ direction_name +'</b>. ' + date_of_testing + "</br>"
							+$.t('interviews_place')
							+": " + place_of_testing+ '</p>');
								}
				);
		});
			
			
		
	}	
	
	function openChooseTestModal(){
		$('#testsModal').css('height','50%');
		$('#testsModal').openModal();
		getDirectionsData();
	}

</script>	

<script>

function exit(){
	$.ajax({
		type : 'POST',
		url : "AjaxController",
		dataType : "xml",
		data : {
			"command" : "exit"
		},
		complete : function(data) {
			window.location.href = "${pageContext.request.contextPath}/";
		}
	});
}

</script>
<jsp:include page="../testselectmodal.jsp"></jsp:include>
<div class='navbar-fixed'>
<ul id="dropdownLang" class="dropdown-content">
		<li><a onclick="setLanguage('uk');"><img src="${pageContext.request.contextPath}/img/uk.png" width="30" height="30">UKR</a></li>
		<li><a onclick="setLanguage('en');"><img src="${pageContext.request.contextPath}/img/en.ico" width="30" height="30">Eng</a></li>
	</ul>
	<nav class="white" role="navigation">
		<div class="nav-wrapper ">
			<a id="logo-container" href="${pageContext.request.contextPath}/"
				class="brand-logo waves-effect  waves-black  hide-on-med-and-down center header_image">
				<img src="${pageContext.request.contextPath}/img/logo2.png"
				height="65" width=170>
			</a>
			<ul class="left hide-on-med-and-down">
				<li><a href="#" data-activates="slide-out" 	class="button-collapse show-on-large"><i class="mdi-navigation-menu medium"></i></a></li>
			</ul>
			<ul class="right hide-on-med-and-down" style="padding-right:30px;">
				<li><a href="#" onclick="openModalView()"><i 
					class="mdi-editor-border-color left"></i><e:msg key="registerForTest"></e:msg></a></li>
				<li><a style="cursor: pointer; cursor: hand;" onclick="openChooseTestModal()"><i
					class="mdi-image-dehaze left"></i><e:msg key="header.Tests"></e:msg></a></li>
				<li><a class="dropdown-button waves-effect waves-black" href="#!" data-activates="dropdownLang"><e:msg key="header.Lang"></e:msg><i class="mdi-navigation-arrow-drop-down right"></i></a></li>
				
			</ul>
		</div>
		
	</nav>
</div>





