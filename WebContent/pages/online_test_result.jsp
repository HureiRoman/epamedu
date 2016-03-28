<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<!DOCTYPE html>
<html>
<jsp:include page="code_highlight.jsp"></jsp:include>
<jsp:include page="requirements.jsp"></jsp:include>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	$(document).ready(function() {
		$('select').material_select();
		if ('${logined_user}' == "undefined" || '${logined_user}' == "") {
			var options = [
				{selector: '#ad', offset: 1126, callback: '$(\'#adModal\').openModal();' },
			];
			Materialize.scrollFire(options);
		}
	});
</script>

<!-- CSS MATERIAL  -->

<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>Test results</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="loginingmodal.jsp"></jsp:include>
	<jsp:include page="header.jsp"></jsp:include>
	
	<e:testresults tests="${tests }" score="${score }"></e:testresults>
	
	<script type="text/javascript">
		dp.SyntaxHighlighter.ClipboardSwf = '/flash/clipboard.swf';
		dp.SyntaxHighlighter.HighlightAll('code');
	</script>
	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>