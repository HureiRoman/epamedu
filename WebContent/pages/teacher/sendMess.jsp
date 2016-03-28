<%@ taglib prefix="e" uri="http://epam.edu/tags"%>

<script>
	function prepareText(text){
		return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
	}
	function removeMultipleWhiteSpaces(value){
		   return value.replace(/\s\s+/g, ' ');
		 }

function sendMessageToStudent() {
	//alert("student id = " + studId);
     var text = $('textarea#messageTextToOnePerson').val();
     text = removeMultipleWhiteSpaces(text);
    if (text == ' ' || text == ''){
     	Materialize.toast($.t('incorrectData'), 2000);
    	
    }else{
     	prepareText(text);
 	    $.ajax({
 	        type : 'POST',
 	        url : "AjaxController",
 	        dataType : "xml",
 	        data : {
 	            "command" : "send_message",
 	            "receiver_id" : studId,
 	            "text" : text
 	        },
 	        complete : function(data) {
 	        	processSendMessageToStudent(data);
 	        }
 	    });
     }
    }

    function processSendMessageToStudent(data) {
        var xml = data.responseXML;
        var status = $(xml).find('status').text();
        $('#modalSendMess').closeModal();
       	if(status == 1){
      	    $('textarea#messageTextToOnePerson').val('');
        	Materialize.toast($.t('messageSended'), 2000);
       	}else{
       		Materialize.toast($.t('errorTryAgain'), 2000);
       	}
    }
    
    
    function goToDialog(){
    	window.location.href = "${pageContext.request.contextPath}/Controller?command=dialog&with=" + studId;
    }
</script>


<div id="modalSendMess" class="modal">
    <div class="modal-content">
        <h6><e:msg key="sendMessageTo"></e:msg> <i id="fNameLname"></i></h6>
        <div class="row">
            <form class="col s12">
                <div class="row">
                    <div class="input-field col s12">
                        <textarea id="messageTextToOnePerson" class="materialize-textarea" maxlength="300" style="max-height:75px;"></textarea>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="modal-footer row">
    	<div class="col s3">
    		 <a href="#!" onclick="goToDialog()"class=" waves-effect waves-green" style="line-height: 56px;"><e:msg key="dialog"></e:msg> <i class="fa fa-envelope-o"></i></a>	
    	</div>
    	<div class="col s9">
	        <a href="#!" class=" modal-close waves-effect waves-green btn-flat" ><e:msg key="close"></e:msg></a>
	        <a href="#!" class=" modal-action waves-effect waves-green btn-flat" onclick="sendMessageToStudent()"><e:msg key="interviews.send_message"></e:msg></a>
        </div>
    </div>
</div>

