<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="e" uri="http://epam.edu/tags" %>
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
            src="${pageContext.request.contextPath}/js/pagination/jquery.quick.pagination.min.js"></script>

    <link href="${pageContext.request.contextPath}/css/pagination/styles.css"
          type="text/css" rel="stylesheet" media="screen,projection"/>

    <script type="text/javascript">
        $(document).ready(function () {
            $('.collapsible').collapsible({
                accordion: false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
            });
            $('ul.tabs').tabs();
            $('.modal-trigger').leanModal();
            showTrainees($('.tab:first').find('a'));
        });


        function prepareText(text){
            return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
        }

        var active_id;

        function showTrainees(chosenTab) {

            var directionId = $(chosenTab).attr('id');

            $.ajax({
                type: 'POST',
                url: "AjaxController",
                dataType: "xml",
                data: {
                    "command": "getTraineesByDirectionId",
                    "direction_id": directionId
                },
                complete: function (data) {
                    processTraineesResult(data);
                }
            });
        }

        function processTraineesResult(data) {
            var trainees = $('.trainees:visible').find('.collapsible');
            trainees.empty();

            var xml = data.responseXML;

            if ($(xml).find('trainee').length == 0) {
                trainees.append('<div class="row" style="padding:30px;">'
                        + '<div class="col s12"><h5 class="center-align blue-text text-darken-2">' + $.t("trainees_no_trainees_in_this_direction") + '</h5></div></div>');
            } else {

                $(xml)
                        .find('trainee')
                        .each(
                        function (index) {

                            var id = $(this).find('id').text();
                            var fname = $(this).find('fname').text();
                            var lname = $(this).find('lname').text();
                            var pname = $(this).find('pname').text();
                            var email = $(this).find('email').text();
                            var skills = $(this).find('skills').text();
                            var englishLevel = $(this).find('english-level').text();
                            var additionalInfo = $(this).find('additional-info').text();
                            var phone = $(this).find('phone').text();
                            var education = $(this).find('education').text();

                            trainees.append('<li id="' + id + '">'
                                    + '<div class="collapsible-header"><i class="mdi-action-account-circle"></i>'
                                    + lname + ' ' + fname + ' ' + pname + '</div>'
                                    + '<div class="collapsible-body">'
                                    + '<div class="row">'
                                    + '<div class="col s12">'
                                    + '<div class="card">'
                                    + '<div class="card-image"'
                                    + 'style="width: 240px; display: block; margin-left: auto; margin-right: auto;">'
                                    + '<img src="${pageContext.request.contextPath}/images?type=users&id=' + id + '">'
                                    + '</div>'
                                    + '<div class="card-content">'
                                    + '<div class="row">'
                                    + '<i class="small left material-icons">email</i>'
                                    + '<div class="col s3">Email</div>'
                                    + '<div class="col s8">' + email + '</div>'
                                    + '</div>'
                                    + '<div class="row">'
                                    + '<i class="small left material-icons">phone</i>'
                                    + '<div class="col s3">' + $.t("trainees_phone") + '</div>'
                                    + '<div class="col s8">' + phone + '</div>'
                                    + '</div>'
                                    + '<div class="row">'
                                    + '<i class="small left material-icons">description</i>'
                                    + '<div class="col s3">' + $.t("trainees_english_level") + '</div>'
                                    + '<div class="col s8">' + englishLevel + '</div>'
                                    + '</div>'
                                    + '</div>'
                                    + '<div class="card-action">'
                                    + '<a class="waves-effect waves-light btn modal-trigger" href="#sendMessageModal" onclick="processUserInfo()">' + $.t("trainees_send_message") + '</a>'
                                    + '</div>'
                                    + '</div>'
                                    + '</div>'
                                    + '</div>'

                                    + '</div>'
                                    + '</li>');

                            if (skills.trim() != '') {
                                trainees.find('li:last').find('.card-content').append('<div class="row">'
                                        + '<i class="small left material-icons">description</i>'
                                        + '<div class="col s3">' + $.t("trainees_skills") + '</div>'
                                        + '<div class="col s8" style="word-break: break-all;">' + skills + '</div>'
                                        + '</div>');
                            }


                            if (additionalInfo.trim() != '') {
                                trainees.find('li:last').find('.card-content').append('<div class="row">'
                                        + '<i class="small left material-icons">description</i>'
                                        + '<div class="col s3">' + $.t("trainees_additional_info") + '</div>'
                                        + '<div class="col s8" style="word-break: break-all;">' + additionalInfo + '</div>'
                                        + '</div>');
                            }

                            if (education.trim() != '') {
                                trainees.find('li:last').find('.card-content').append('<div class="row">'
                                        + '<i class="small left material-icons">description</i>'
                                        + '<div class="col s3">' + $.t("trainees_education") + '</div>'
                                        + '<div class="col s8" style="word-break: break-all;">' + education + '</div>'
                                        + '</div>');
                            }
                        }
                );
                $('.collapsible').collapsible({
                    accordion: false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
                });
                $('.modal-trigger').leanModal();

                trainees.quickPagination({pageSize: "20"});
            }
        }

        function processUserInfo() {
            active_id = $('.collapsible').find(".active").attr("id");
        }

        function sendMessage(element) {
            var text = prepareText($('textarea#message_text').val());
            var subject = $('#subject').val();
            document.getElementById('message_text').value = "";
            if (text) {
                $.ajax({
                    type: 'POST',
                    url: "AjaxController",
                    dataType: "xml",
                    data: {
                        "command": "send_message",
                        "receiver_id": active_id,
                        "text": text,
                        'sendToEmail': $('#sendToEmail').is(':checked'),
                        'subject': subject,
                    },
                    complete: function (data) {
                        processSendMessage(data);
                    }
                });
            } else {
                Materialize.toast($.t("trainees_enter_message_text"), 3000)
            }

            function processSendMessage(data) {
                var xml = data.responseXML;
                var status = $(xml).find('status').text();
                console.log('--------->>>> status ' + status);
                if (status == 1) {
                    var text = $('textarea#message_text').empty();
                    Materialize.toast($.t("trainees_message_sent"), 3000);
                    $('#sendMessageModal').closeModal();
                } else {
                    Materialize.toast($.t("trainees_sending_error"), 3000);
                }
            }
        }
        function addFieldForSubject() {
            if (document.getElementById('sendToEmail').checked) {
                $('#messageSubject').empty();
                $('#messageSubject').append(' '
                        + '<input type="text" id="subject"  maxlength="100">'
                        + '<label for="subject">' + $.t("trainees_message_subject") + '</label>'
                        + ''
                )
            } else  $('#messageSubject').empty();
        }
    </script>
</head>
<body bgcolor="#FAFAFA">
<jsp:include page="header.jsp"></jsp:include>
<jsp:include page="hr_panel_sidenav.jsp"></jsp:include>
<div class="parallax-container">
    <div class="parallax">
        <img
                src="${pageContext.request.contextPath}/img/hr/paralax/trainees.jpg">
    </div>
</div>
<div class="col">
    <div class="row">
        <div class=" col s8 offset-s2 ">
            <div class="row" style="padding:30px;">
                <div class="col s12">
                    <h5 class="center-align blue-text text-darken-2"><e:msg key="trainees"></e:msg> </h5>
                </div>
            </div>
            <div class="card-panel white">
                <div class="row">

                    <div class="row">
                        <div class="col s12">
                            <c:choose>
                                <c:when test="${empty directions}">
                                    <div class="row" style="padding:30px;">
                                        <div class="col s12">
                                            <h5 class="center-align blue-text text-darken-2"><e:msg key="hr.you_have_no_any_direction"></e:msg> </h5>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <ul class="tabs">
                                        <c:forEach items="${directions}" var="direction">
                                            <li class="tab col s3"><a id="dirId${direction.id}"
                                                                      href="#id${direction.id}"
                                                                      onclick="showTrainees(this)">${direction.name}</a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <c:forEach items="${directions}" var="direction">
                            <div id="id${direction.id}" class="col s12 trainees">
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
<div id="sendMessageModal" class="modal">
    <div class="modal-content">
        <h4><e:msg key="send_message"></e:msg></h4>

        <div class="row">
            <form class="col s12">
                <div class="row">
                    <div class="input-field col s6" id="messageSubject">

                    </div>
                    <div class="input-field col s12">
                        <textarea id="message_text" style="max-height:100px" maxlength="250" length="250"
                                  class="materialize-textarea"></textarea>
                        <label for="message_text"><e:msg key="trainees.message_text"></e:msg></label>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="modal-footer">
        <a href="#!" class=" modal-close waves-effect waves-green btn-flat"><e:msg key="close"></e:msg></a>
        <a href="#!" onclick="sendMessage()" class=" modal-action waves-effect waves-green btn-flat"><e:msg key="send_message"></e:msg></a>

        <input type="checkbox" id="sendToEmail" onclick="addFieldForSubject()"/>
        <label for="sendToEmail"><e:msg key="trainees.send_to_email"></e:msg></label>
    </div>
</div>
<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>