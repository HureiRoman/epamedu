<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="requirements.jsp"></jsp:include>

     <%@page import="java.net.InetAddress" %>
<%InetAddress inetAddress = InetAddress.getLocalHost();
			String ip = inetAddress.getHostAddress();%>   
<script type="text/javascript">
	$(document).ready(function($) {
		     var typeOfRequest = $('#typeOfRequest').val();
		     if(typeOfRequest=='VK'){
		    	 var url = window.location.href;	
	             var parameters = url.substr(url.indexOf("#") + 1);
	        	 var signInUrl = "Controller?command=signInBySocialNetwork&type=VK&"+parameters;
	             window.location.href = signInUrl;
		     }else{
		    	 var fbCode = $('#fbCode').val();
		    	 $.ajax({
						type : 'POST',
						url : "https://graph.facebook.com/v2.3/oauth/access_token",
						dataType : "json",
						data : {
							"client_id" : "931276456936171",
							"client_secret" : "c20afb1d2bab05460250b5ef3f68f1e9",
							"code" : fbCode,
							"redirect_uri" : "http://<%out.print(ip);%>:8080/EpamEducationalProject/Controller?command=startWithSocialNetwork&type=FB"
						},
						complete : function(data) {
							var responseJson =  data.responseJSON;
							console.log(responseJson);
							var access_token = responseJson.access_token ;
							getUserData(access_token);
						}
		    	 });	
					
		     }
		     function getUserData(access_token){
	    		 $.ajax({
						type : 'GET',
						url : "https://graph.facebook.com/me",
						dataType : "json",
						data : {
							"access_token" : access_token
							},
						complete : function(data) {
							var responseJson =  data.responseJSON;
							var signInUrl = "Controller?command=signInBySocialNetwork&type=FB&fbId="+responseJson.id;
				             window.location.href = signInUrl;   
						}
		    	 });
	    	 }
        	
	});
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Wait</title>
</head>
<body>
<input value="${type}" type="hidden" id="typeOfRequest" > 
<input value="${code}" type="hidden" id="fbCode" > 
</body>
</html>