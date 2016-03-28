<%@ taglib prefix="e" uri="http://epam.edu/tags"%>

<script>
	function prepareText(text){
		return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
	}	
	function removeMultipleWhiteSpaces(value){
	   return value.replace(/\s\s+/g, ' ');
	 }
	
function sendMessageToAll() {
	
    var text = $('textarea#message_text').val();
    text = removeMultipleWhiteSpaces(text);
   	if (text == ' ' || text == ''){
    	Materialize.toast($.t("incorrectValue"), 2000);
    	
    }else{
    	prepareText(text);
	    var group_id = $("input[name=group_id]").val();
	    $.ajax({
	        type : 'POST',
	        url : "AjaxController",
	        dataType : "xml",
	        data : {
	            "command" : "sendMessageToGroupStudents",
	            "group_id" : group_id,
	            "text" : text
	        },
	        complete : function(data) {
	            processSendMessageToAll(data);
	        }
	    });
    }
    }

    function processSendMessageToAll(data) {
        var xml = data.responseXML;
        var status = $(xml).find('status').text();
        var text = $('textarea#message_text').empty();
        $('#modalSendMessToAll').closeModal();
       	if(status == 1){
        	Materialize.toast($.t('messageSended'), 2000);
       	}else{
       		Materialize.toast($.t('errorTryAgain'), 2000);
       	}
    }
</script>


<div id="modalSendMessToAll" class="modal">
    <div class="modal-content">
        <h6><e:msg key="sendToAll"></e:msg> <i>${myGroup.title}</i></h6>
        <div class="row">
            <form class="col s12">
                <div class="row">
                    <div class="input-field col s12">
                        <textarea id="message_text" class="materialize-textarea" maxlength="300" style="max-height:75px;"></textarea>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="modal-footer">
        <a href="#!" class=" modal-close waves-effect waves-green btn-flat"><e:msg key="close"></e:msg></a>
        <a href="#!" class=" modal-action waves-effect waves-green btn-flat" onclick="sendMessageToAll()"><e:msg key="send_message"></e:msg></a>
    </div>
</div>

