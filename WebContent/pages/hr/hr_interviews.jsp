<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="e" uri="http://epam.edu/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="../requirements.jsp"></jsp:include>
    <!--DateTimePicker -->
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/css/datetimepicker/jquery.datetimepicker.css"/>
    <%--<script src="${pageContext.request.contextPath}/js/datetimepicker/jquery.js"></script>--%>
    <script src="${pageContext.request.contextPath}/js/datetimepicker/jquery.datetimepicker.js"></script>

    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
    <title>Interviews</title>
    <script type="text/javascript">
        $(document).ready(function () {
            $('.collapsible').collapsible({
                accordion: false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
            });
            $('ul.tabs').tabs();

            $('.modal-trigger').leanModal();
            $('select').material_select();
            showInterviews($('.tab:first').find('a'));
        });

        function prepareText(text){
            return text.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/&/g, "&amp;")
        }

        function decodePunctuation(str){
            return str.replace(/&#60;/g, "&lt;").replace(/&#62;/g, "&gt;")
                    .replace(/&quot;/g, '"')
                    .replace(/&amp;/g, "&");
        }

        function showInterviews(chosenTab) {

            var directionId = $(chosenTab).attr('id');
            $.ajax({
                type: 'POST',
                url: "AjaxController",
                dataType: "xml",
                data: {
                    "command": "getInterviewsByDirectionId",
                    "direction_id": directionId
                },
                complete: function (data) {
                    processInterviewsResult(data);
                }
            });
        }

        function updateInterviews() {
            var directionId = $('.tab').find('.active').attr('id');
            $.ajax({
                type: 'POST',
                url: "AjaxController",
                dataType: "xml",
                data: {
                    "command": "getInterviewsByDirectionId",
                    "direction_id": directionId
                },
                complete: function (data) {
                    processInterviewsResult(data);
                }
            });
        }

        function processInterviewsResult(data) {
            var interviews = $('.interviews:visible').find('.collapsible');
            interviews.empty();
            var xml = data.responseXML;
            if ($(xml).find('interview').length == 0) {
                interviews.append('<div class="row" style="padding:30px;">'
                        + '<div class="col s12"><h5 class="center-align blue-text text-darken-2">' + $.t("interviews_no_future_interviews") + '</h5></div></div>');
            } else {

                $(xml)
                        .find('interview')
                        .each(
                        function (index) {

                            var id = $(this).find('id').text();
                            var date = $(this).find('date').text();
                            var place = $(this).find('place').text();
                            place = decodePunctuation(place);
                            var description = $(this).find('description').text();
                            description = decodePunctuation(description);
                            var subscribersCount = $(this).find('subscribersCount').text();
                            
                            var showButExcel = '';
                            if(subscribersCount > 0){
                            	showButExcel = '<div class="row">'
                                + '<a onclick="showEditInterview()" class="waves-effect waves-light btn modal-trigger" href="">' + $.t("edit") + '</a>'
                                + '<a class="waves-effect waves-light btn modal-trigger" href="#send_message_modal">' + $.t("interviews_inform_followers") + '</a>'
                                + '<a class="waves-effect waves-light btn modal-trigger" href="#delete_modal">' + $.t("delete") + '</a>'
                                + '</div>'
                            	+ '<div class="row">'
                                + '<a href="${pageContext.request.contextPath}/excel?interviewId='+id+'" class="waves-effect waves-light btn modal-trigger" href="">' + $.t("getExcel") + '</a>'
                                + '</div>'
                            }else{
                            	showButExcel = '<div class="row">'
                                    + '<a onclick="showEditInterview()" class="waves-effect waves-light btn modal-trigger" href="">' + $.t("edit") + '</a>'
                                    + '<a class="waves-effect waves-light btn modal-trigger" href="#delete_modal">' + $.t("delete") + '</a>'
                                    + '</div>'
                            } 
                            
                            interviews.append('<li id="' + id + '">'
                                    + '<div class="collapsible-header">'
                                    + '<i class="material-icons">assignment</i>'
                                    + '<div id="date">' + date
                                    + '</div></div>'
                                    + '<div class="collapsible-body" style="padding: 20px">'
                                    + '<div class="row">'
                                    + '<i class="small left material-icons">location_on</i>'
                                    + '<div class="col s3">' + $.t("interviews_place") + ':</div><div class="place col s8" style="word-break: break-all;">' + place + '</div>'
                                    + '</div></div></li>');
                            if(description.length != 0) {
                                interviews.find('#' + id + ' .collapsible-body').append('<div class="row">'
                                        + '<i class="small left material-icons">description</i>'
                                        + '<div class="col s3">' + $.t("interviews_description") + ':</div><div class="description col s8" style="word-break: break-all;">' + description + '</div>'
                                        + '</div>')
                            }
                             interviews.find('#' + id + ' .collapsible-body').append('<div class="row">'
                                    + '<i class="small left material-icons">label</i>'
                                    + '<div class="col s3">' + $.t("amount_of_subscribers") + ':</div><div class="col s8" style="word-break: break-all;">' + subscribersCount + '</div>'
                                    + '</div>');
                                    /////
                            interviews.find('#' + id + ' .collapsible-body').append(showButExcel);
                                    /////
                        });
                $('.collapsible').collapsible({
                    accordion: false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
                });
                $('.modal-trigger').leanModal();
            }
        }
		
        /////
        function getExcel(){
        	var activeInterview = $('.tabcontent:visible').find('.collapsible').find(".active");
            var activeInterviewID = activeInterview.attr("id");
            
            if(activeInterviewID != null && activeInterviewID != '') {
            	 $.ajax({
                     type: 'POST',
                     url: "GetExcelFileWithAbiturients",
                     dataType: "xml",
                     data: {
                         "interviewId": activeInterviewID
                     },
                     complete: function (data) {
                         processAddInterviewResult(data);
                         $('#add_modal').closeModal();
                         updateInterviews();
                     }
                 });
            }
        }
        
        ////
        
        
        
        
        
        function showEditInterview(element) {
            var activeInterview = $('.tabcontent:visible').find('.collapsible').find(".active");
            var activeInterviewID = activeInterview.attr("id");
            var place = activeInterview.find('.place').text();
            var description = activeInterview.find('.description').text();
            var date = activeInterview.find('#date').text();

            $('#edit_description').val(description);
            $('#edit_place').val(place);
            $('#edit_datetimepicker').val(date);
            $('#edit_modal').find('label').addClass('active');
            $('#edit_modal').openModal();
        }

        function addInterview(element) {

            var place = prepareText($('#place').val());
            var description = prepareText($('#description').val());
            var date = $('#datetimepicker').val();
            var direction = $('#direction').val();
            if (place.length > 0 && date.length > 0 && !isPastDate(date) && direction) {
                $.ajax({
                    type: 'POST',
                    url: "AjaxController",
                    dataType: "xml",
                    data: {
                        "command": "interviewAction",
                        "action": "add",
                        "place": place,
                        "date": date,
                        "description": description,
                        "direction": direction
                    },
                    complete: function (data) {
                        processAddInterviewResult(data);
                        $('#add_modal').closeModal();
                        updateInterviews();
                    }
                });
            }
            if (date.length == 0 || isPastDate(date)) {
                Materialize.toast($.t("interviews_bad_date"), 3000)
            }
            if (place.length == 0) {
                Materialize.toast($.t("interviews_enter_place"), 3000)
            }
            if (!direction) {
                Materialize.toast($.t("interviews_choose_directiion"), 3000)
            }
        }

        function editInterview(element) {
            var place = prepareText($('#edit_place').val());
            var description = prepareText($('#edit_description').val());
            var date = $('#edit_datetimepicker').val();
            var directionId = $('.tabcontent:visible').attr('id');
            var interviewId = $('.tabcontent:visible').find('.collapsible').find(".active").attr("id");
            if (place.length > 0 && !isPastDate(date)) {
                $.ajax({
                    type: 'POST',
                    url: "AjaxController",
                    dataType: "xml",
                    data: {
                        "command": "interviewAction",
                        "action": "edit",
                        "place": place,
                        "date": date,
                        "interviewId": interviewId,
                        "description": description,
                        "direction": directionId
                    },
                    complete: function (data) {
                        processEditInterviewResult(data);
                    }
                });
            }
            if (date.length == 0 || isPastDate(date)) {
                Materialize.toast($.t("interviews_bad_date"), 3000)
            }
            if (place.length == 0) {
                Materialize.toast($.t("interviews_enter_place"), 3000)
            }
        }

        function processAddInterviewResult(data) {
            var response = data.responseXML.documentElement.firstChild.nodeValue;
            if (response == 1) {
                Materialize.toast($.t("interviews_successful_added"), 3000)
                $('#place').val("");
                $('#description').val("");
                $('#date').val("");

                $("#direction").val($("#direction option:first").val());



                updateInterviews();
                $('#add_modal').closeModal();
            } else if (response == 2) {
                Materialize.toast($.t("interviews_bad_value"), 3000);
            }
            $('select').material_select();
        }

        function processEditInterviewResult(data) {
            var response = data.responseXML.documentElement.firstChild.nodeValue;
            if (response == 1) {
                Materialize.toast($.t("interviews_successful_edited"), 3000)
                document.getElementById('place').value = "";
                document.getElementById('description').value = "";
                document.getElementById('date').value = "";

                updateInterviews();
                $('#edit_modal').closeModal();
            } else if (response == 2) {
                Materialize.toast($.t("interviews_bad_value"), 3000);
            }
        }

        function processSendMessageToFollowers() {
            var interviewId = $('.tabcontent:visible').find('.collapsible').find(".active").attr("id");
            var messageText = prepareText($('#message_text').val());
            var subject = $('#subject').val();
            if (messageText.length > 0) {
                $.ajax({
                    type: 'POST',
                    url: "AjaxController",
                    dataType: "xml",
                    data: {
                        "command": "sendMessageToInterviewFollowers",
                        "messageText": messageText,
                        "interviewId": interviewId,
                        'sendToEmail': $('#sendToEmail').is(':checked'),
                        'subject': subject,
                    },
                    complete: function (data) {
                        processSendMessageToInterviewFollowersResult(data)
                    }
                });
            } else {
                Materialize.toast($.t("interviews_enter_message_text"), 3000);
            }
        }

        function processSendMessageToInterviewFollowersResult(data) {
            var response = data.responseXML.documentElement.firstChild.nodeValue;
            if (response == 1) {
                Materialize.toast($.t("interviews_message_sent"), 3000);
                $('#send_message_modal').closeModal();
                updateInterviews()
            } else if (response == 2) {
                Materialize.toast($.t("interviews_sending_error"), 3000);
            }
        }

        function processDeleteInterview() {
            var interviewId = $('.tabcontent:visible').find('.collapsible').find(".active").attr("id");
            $.ajax({
                type: 'POST',
                url: "AjaxController",
                dataType: "xml",
                data: {
                    "command": "interviewAction",
                    "action": "delete",
                    "interviewId": interviewId,
                },
                complete: function (data) {
                    processDeleteInterviewResult(data);
                }
            });
        }

        function processDeleteInterviewResult(data) {
            var response = data.responseXML.documentElement.firstChild.nodeValue;
            if (response == 1) {
                Materialize.toast($.t("interviews_interview_deleted"), 3000);
                $('#delete_modal').closeModal();
                updateInterviews()
            } else if (response == 2) {
                Materialize.toast($.t("interviews_deleting_error"), 3000);
            }
        }

        function addFieldForSubject() {
            if (document.getElementById('sendToEmail').checked) {
                $('#messageSubject').empty();
                $('#messageSubject').append(' '
                        + '<input type="text" id="subject" maxlength="100">'
                        + '<label for="subject">' + $.t("interviews_message_subject") + '</label>'
                        + ''
                )
            } else  $('#messageSubject').empty();
        }

        function isPastDate(value) {
            var now = new Date();
            var valueDate = value.split(" ");
            var day = valueDate[0].split("-")[0];
            var month = valueDate[0].split("-")[1];
            var year = valueDate[0].split("-")[2];

            var hour = valueDate[1].split(":")[0];
            var minute = valueDate[1].split(":")[1];


            if (now.getFullYear() == year) {
                if ((now.getMonth() + 1) == month) {
                    if (now.getDay() == day) {
                        if (now.getHours() < hour || (now.getHours() == hour && now.getMinutes() <= minute)) {

                            return false;
                        }
                    } else if (now.getDay() < day) {
                        return false;
                    }
                } else if ((now.getMonth() + 1) < month) {
                    return false;
                }
            } else if (now.getFullYear() < year) {
                return false;
            }
            return true;
        }


    </script>
</head>
<body bgcolor="#FAFAFA">
<jsp:include page="header.jsp"></jsp:include>
<jsp:include page="hr_panel_sidenav.jsp"></jsp:include>
<fmt:setLocale value="en_US" scope="session"/>
<div class="parallax-container">
    <div class="parallax">
        <img
                src="${pageContext.request.contextPath}/img/hr/paralax/interviews.jpg">
    </div>
</div>

<div class="col">
    <div class="row">
        <div class=" col s8 offset-s2 ">
            <div class="row" style="padding:30px;">
                <div class="col s12">
                    <h5 class="center-align blue-text text-darken-2"><e:msg key="interviews"></e:msg></h5>
                </div>
            </div>
            <div class="card-panel white">
                <div class="row">
                    <div class="row">
                        <a class="waves-effect waves-light btn modal-trigger right" href="#add_modal"><e:msg key="interviews.add_interview"></e:msg></a>
                    </div>
                    <div class="row">
                        <div class="col s12">
                            <c:choose>
                                <c:when test="${empty directions}">
                                    <div class="row" style="padding:30px;">
                                        <div class="col s12">
                                            <h5 class="center-align blue-text text-darken-2"><e:msg key="hr.you_have_no_any_direction"></e:msg></h5>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <ul class="tabs">
                                        <c:forEach items="${directions}" var="direction">
                                            <li class="tab col s3"><a id="dirId${direction.id}"
                                                                      onclick="showInterviews(this)"
                                                                      href="#id${direction.id}">${direction.name}</a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <c:forEach items="${directions}" var="direction">
                            <div id="id${direction.id}" class="interviews tabcontent col s12">
                                <ul class="collapsible" data-collapsible="accordion">

                                </ul>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="add_modal" class="modal">
    <div class="modal-content">
        <h4><e:msg key="interviews.add_interview"></e:msg> </h4>

        <div class="row">
            <form class="col s12">
                <div class="input-field col s12">
                    <select id="direction">
                        <option value="" disabled selected><e:msg key="interviews.choose_direction"></e:msg> </option>
                        <c:forEach items="${directions}" var="direction">
                            <option value="${direction.id}">${direction.name}</option>
                        </c:forEach>
                    </select>
                    <label><e:msg key="interviews.choose_direction"></e:msg></label>
                </div>

                <div class="input-field col s12">
                    <input required id="datetimepicker" type="text" readonly="readonly">
                    <label for="datetimepicker"><e:msg key="interviews.date_time"></e:msg> </label>
                    <script type="text/javascript">
                        $('#datetimepicker').datetimepicker({
                            timepicker: true,
                            minDate: '0',//today
                            step: 15
                        });
                    </script>
                </div>

                <div class="input-field col s12">
                    <textarea id="place" class="materialize-textarea" style="max-height:100px" maxlength="250"
                              length="250"></textarea>
                    <label for="place"><e:msg key="interviews.place"></e:msg></label>
                </div>
                <div class="input-field col s12">
                    <textarea id="description" class="materialize-textarea" style="max-height:100px" maxlength="250"
                              length="250"></textarea>
                    <label for="description"><e:msg key="interviews.description"></e:msg> </label>
                </div>
            </form>
        </div>
    </div>
    <div class="modal-footer">
        <a href="#!" class=" modal-close waves-effect waves-green btn-flat"><e:msg key="close"></e:msg></a>
        <a href="#!" onclick="addInterview()" class=" modal-action waves-effect waves-green btn-flat"><e:msg key="interviews.add_interview"></e:msg></a>
    </div>
</div>

<div id="edit_modal" class="modal">
    <div class="modal-content">
        <h4><e:msg key="interviews.edit_interview"></e:msg></h4>

        <div class="row">
            <form class="col s12">

                <div class="input-field col s12">
                    <input required id="edit_datetimepicker" type="text" readonly="readonly">
                    <label for="edit_datetimepicker" class="active"><e:msg key="interviews.date_time"></e:msg></label>
                    <script type="text/javascript">
                        $('#edit_datetimepicker').datetimepicker({
                            timepicker: true,
                            minDate: '0',//today
                            step: 15
                        });
                    </script>
                </div>

                <div class="input-field col s12">
          <textarea class="materialize-textarea" id="edit_place" style="max-height:100px" maxlength="250" length="250">
          </textarea>
                    <label for="edit_place" class="active"><e:msg key="interviews.place"></e:msg></label>
                </div>
                <div class="input-field col s12">
          <textarea class="materialize-textarea" id="edit_description" style="max-height:100px" maxlength="250"
                    length="250">
          </textarea>
                    <label for="edit_description" class="active"><e:msg key="interviews.description"></e:msg> </label>
                </div>
            </form>
        </div>
    </div>
    <div class="modal-footer">
        <a href="#!" class=" modal-close waves-effect waves-green btn-flat"><e:msg key="close"></e:msg></a>
        <a href="#!" onclick="editInterview()" class=" modal-action waves-effect waves-green btn-flat"><e:msg key="save"></e:msg></a>

    </div>
</div>

<div id="send_message_modal" class="modal">
    <div class="modal-content">
        <h4><e:msg key="interviews.inform_followers"></e:msg> </h4>

        <div class="row">
            <form class="col s12">
                <div class="row">
                    <div class="input-field col s6" id="messageSubject">

                    </div>
                    <div class="input-field col s12">
                        <textarea id="message_text" style="max-height:100px" maxlength="250" length="250"
                                  class="materialize-textarea"></textarea>
                        <label for="message_text"><e:msg key="interviews.message_text"></e:msg></label>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="modal-footer">
        <a href="#!" class=" modal-close waves-effect waves-green btn-flat"><e:msg key="close"></e:msg> </a>
        <a href="#!" onclick="processSendMessageToFollowers()" class=" modal-action waves-effect waves-green btn-flat">
            <e:msg key="interviews.send_message"></e:msg>
        </a>

        <input type="checkbox" id="sendToEmail" onclick="addFieldForSubject()"/>
        <label for="sendToEmail"><e:msg key="interviews.send_to_email"></e:msg></label>

    </div>
</div>


<div id="delete_modal" class="modal">
    <div class="modal-content">
        <h4><e:msg key="interviews.delete_interview"></e:msg> </h4>

        <p><e:msg key="interviews.do_you_want_delete_interview"></e:msg></p>
    </div>
    <div class="modal-footer">
        <a href="#!" class=" modal-close waves-effect waves-green btn-flat"><e:msg key="cancel"></e:msg></a>
        <a href="#!" onclick="processDeleteInterview()"
           class=" modal-action waves-effect waves-green btn-flat"><e:msg key="delete"></e:msg></a>
    </div>
</div>
<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>