<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="requirements.jsp"></jsp:include>
<jsp:include page="code_highlight.jsp"></jsp:include>

<script
	src="${pageContext.request.contextPath}/js/countdown/jquery.countdown.js"></script>
<script src="${pageContext.request.contextPath}/js/countdown/timer.js"></script>
<script type="text/javascript">

	$(document).ready(function(){
		$('#testsTimeModal').openModal({dismissible: false});
	});
	
	function selectTab(tabname) {
		$(document).ready(function() {
			$('ul.tabs').tabs('select_tab', tabname);
			$('html, body').animate({
				scrollTop : 0
			}, 'fast');
		});
	}
	

	function changeImage(test_number) {

    	var x = document.getElementById("test" + test_number);
    	var currentClass = x.className;
    	x.className="mdi-action-done";
	}
	
	function sendData(){
		
		var answers;
		var tests;
		for (i = 0; i < 12; i++) {
			var answer = $('input[name=answer'+ (i + 1) + ']:checked').val();
			answers += '<input type="hidden" name="answers" value="' + answer + '" />';
		}
		<c:forEach items="${tests}" var="test">
			tests += '<input type="hidden" name="tests" value="' + '${test.id}' + '" />';
		</c:forEach>
		
		var form = $('<form action="Controller?command=test_result" method="post">' +
		  '<input type="hidden" name="direction" value="' + '${direction.name}' + '" />' +
		  answers + tests + '</form>');
		$('head').append(form);
		form.submit();
	}
	
</script>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/countdown/jquery.countdown.css" />
<link href="${pageContext.request.contextPath}/css/clock.css"
	type="text/css" rel="stylesheet" media="screen,projection" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
	
<title>Online test</title>

</head>

<body bgcolor="#FAFAFA">
	<jsp:include page="loginingmodal.jsp"></jsp:include>
	<jsp:include page="header.jsp"></jsp:include>
	
	<e:tests tests="${tests}"></e:tests>
	
	<script type="text/javascript">
		dp.SyntaxHighlighter.ClipboardSwf = '/flash/clipboard.swf';
		dp.SyntaxHighlighter.HighlightAll('code');
	</script>
	
	
	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>