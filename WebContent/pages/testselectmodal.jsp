<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(document).ready(function() {
		getDirectionsData();
	});
	
	function getDirectionsData(){
		$.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "xml",
			data : {
				"command" : "getDirections"
			},
			complete : function(data) {
				processDirectionsInfo(data);
			}
		});
		
	}
	
	
	function processDirectionsInfo (data) {
		var xml = data.responseXML;
		console.log($(xml));
		var status = $(xml).find('empty').find('status').text();
		console.log('FUNCTION');
		if(status === '0') {
			var message = $(xml).find('empty').find('message').text();
			$('#testsModal').empty();
			$('#testsModal').append('<div class="modal-content blue lighten-5 teal-text" id="no_tests_message"' 
					+ 'style="padding-top: 20px; padding-left: 10px; padding-right: 10px; padding-bottom: 10px;"'
					+ '<div class="row valign-wrapper" style=\"margin-top: 20px;">'
					+ '<div class="col s12">'
					+ '<h6 class="center">'
					+ message 
					+ '</h6>'
					+ '</div></div>'
					+ '<div class="row blue lighten-5 teal-text" style="margin-bottom: 0px; padding-bottom: 10px; padding-top: 10px;">'
					+ '<div class="col s2 offset-s5">'
					+ '<a class="modal-close waves-effect waves-green btn-flat center" style="position: inherit;">'
					+ 'OK'
					+ '</a>'
					+ '</div></div></div>');
		}
		else {
			$('#testsModal').empty();
			var modalContent = '<div class="modal-content"><div class="row"><div>';
			$(xml).find('directionsList').find('direction').each(
					function(index) {
						var direction_name = $(this).find('direction_name').text();
						var direction_id = $(this).find('direction_id').text();
						modalContent += '<a href="${pageContext.request.contextPath}/Controller?command=tests&direction=' + direction_name +'">'
						+ '<div class="col s12 m6 l3 collection-item" style="border: 3px solid #FAFAFA;">'
						+ '<img class="img-responsive myStyleForImage" style="height:150px; width:150px;" src="${pageContext.request.contextPath}/images?type=directions&id=' + direction_id + '">'
						+ '<p style="text-align: center">' + direction_name +'</p></div></a>';
				});
			modalContent += '</div></div></div>';
			$('#testsModal').append(modalContent);
		}
		
	}
</script>
<div id="testsModal" class="modal"></div>