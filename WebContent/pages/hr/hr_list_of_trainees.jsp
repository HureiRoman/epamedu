<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../requirements.jsp"></jsp:include>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
    <title>Trainees</title>

    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/pagination/jquery.quick.pagination.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            $('.collapsible').collapsible({
                accordion: false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
            });
            $('ul.tabs').tabs();
            $('.modal-trigger').leanModal();
        });


        var active_id;

        function processUserInfo() {
            active_id = $('.collapsible').find( ".active" ).attr( "id" );
        }

        function sendMessage(element) {
            var text = $('textarea#message_text').val();
            var subject=$('#subject').val();
            document.getElementById('message_text').value = "";
            $.ajax({
                type : 'POST',
                url : "AjaxController",
                dataType : "xml",
                data : {
                    "command" : "send_message",
                    "receiver_id" : active_id,
                    "text" : text,
                    'sendToEmail' : $('#sentToEmail').is(':checked'),
                    'subject' : subject,
                },
                complete : function(data) {
                    processSendMessage(data);
                }
            });

            function processSendMessage(data) {
                var xml = data.responseXML;
                var status = $(xml).find('status').text();
                console.log('--------->>>> status ' + status);
                var text = $('textarea#message_text').empty();

            }
        }
        function addFieldForSubject() {
       	 if(document.getElementById('sentToEmail').checked)	{
       		 $('#messageSubject').empty();
       	 $('#messageSubject').append(' '
       			 +'<input type="text" id="subject"  maxlength="100">'
       	        +'<label for="subject">Message subject</label>'	
       	        +''
       	 )
       	 } else  $('#messageSubject').empty();
       }
    </script>
</head>
<body bgcolor="#FAFAFA">
<jsp:include page="header.jsp"></jsp:include>
<jsp:include page="hr_panel_sidenav.jsp"></jsp:include>
<div class="col">
    <div class="row">
        <div class=" col s8 offset-s2 ">
            <div class="row" style="padding:30px;">
                <div class="col s12">
                    <h5 class="center-align blue-text text-darken-2">Trainees</h5>
                </div>
            </div>
            <div class="card-panel white">
                <div class="row">

                    <div class="row">
                        <div class="col s12">
                            <ul class="tabs">
                                <c:forEach var="entry" items="${directions}">
                                <li class="tab col s3"><a href="#id${entry.key.id}">${entry.key.name}</a></li>
                                </c:forEach>
                            </ul>
                        </div>

                        <c:forEach var="entry" items="${directions}">
                        <div id="id${entry.key.id}" class="col s12">
                            <ul class="collapsible" data-collapsible="accordion">
                                <c:if test="${empty entry.value}">
                                    <div class="row" style="padding:30px;">
                                        <div class="col s12">
                                            <h5 class="center-align blue-text text-darken-2">No Trainees applicant to this direction</h5>
                                        </div>
                                    </div>
                                </c:if>
                                <c:forEach var="trainee" items="${entry.value}">
                                <li id="${trainee.id}">
                                    <div class="collapsible-header"><i class="mdi-action-account-circle"></i>
                                            ${trainee.lastName} ${trainee.firstName} ${trainee.parentName}</div>
                                    <div class="collapsible-body">

                                        <div class="row">
                                            <div class="col s12">
                                                <div class="card">
                                                    <div class="card-image"
                                                         style="width: 240px; display: block; margin-left: auto; margin-right: auto;">
                                                        <img src="${pageContext.request.contextPath}/images?type=users&id=${trainee.id}">
                                                    </div>
                                                    <div class="card-content">
                                                        <div class="row">
                                                            <div class="col s3">Email</div>
                                                            <div class="col s8">${trainee.email}</div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col s3">Skills</div>
                                                            <div class="col s8" style="word-break: break-all;">${trainee.cv.skills}</div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col s3">English Level</div>
                                                            <div class="col s8">${trainee.cv.englishLevel}</div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col s3">Phone</div>
                                                            <div class="col s8">${trainee.cv.phone}</div>
                                                        </div>


                                                        <div class="row">
                                                            <div class="col s3">Additional Info</div>
                                                            <div class="col s4" style="word-break: break-all;">${trainee.cv.additionalInfo}</div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col s3">Education</div>
                                                            <div class="col s4" style="word-break: break-all;">${trainee.cv.education}</div>
                                                        </div>
                                                    </div>
                                                <div class="card-action">
                                                        <a class="waves-effect waves-light btn modal-trigger" href="#sendMessageModal" onclick="processUserInfo()">Send Message</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </li>
                                </c:forEach>
                            </ul>
                        </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="sendMessageModal" class="modal">
    <div class="modal-content">
        <h4>Send Message</h4>
        <div class="row">
            <form class="col s12">
                <div class="row">
                <div class="input-field col s6" id="messageSubject">
        
         </div>
                    <div class="input-field col s12">
                        <textarea id="message_text" style="max-height:100px" maxlength="250" length="250"  class="materialize-textarea"></textarea>
                         <label for="message_text">Message</label>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="modal-footer">
        <a href="#!" class=" modal-close waves-effect waves-green btn-flat">Close</a>
        <a href="#!" onclick="sendMessage()" class=" modal-action modal-close waves-effect waves-green btn-flat">Send Message</a>
   
    <input type="checkbox" id="sentToEmail" onclick="addFieldForSubject()"/>
      <label for="sentToEmail">Sent to email</label>
    </div>
</div>
<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>