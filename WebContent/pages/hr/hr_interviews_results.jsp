<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="e" uri="http://epam.edu/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <jsp:include page="../requirements.jsp"></jsp:include>

    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/jeditable/jquery.jeditable.js"></script>

    <!-- data table -->
    <%--<script type="text/javascript" src="${pageContext.request.contextPath}/js/datatable/jquery.dataTables.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/datatable/jquery.dataTables.min.css"
          type="text/css" rel="stylesheet"/>--%>

    <script type="text/javascript" src="${pageContext.request.contextPath}/js/datatable/jquery.dataTables.js"></script>
    <link href="${pageContext.request.contextPath}/css/datatable/jquery.dataTables.css"
          type="text/css" rel="stylesheet"/>

    <!-- regex mask -->
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/inputmask/inputmask.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/inputmask/jquery.inputmask.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/inputmask/inputmask.regex.extensions.js"></script>

    <script type="text/javascript">
        var checkedTrainees = [];
        $(document).ready(function () {
            $('select').material_select();
            $('ul.tabs').tabs();
            $('.tooltipped').tooltip({delay: 50});

            $('#amount').inputmask('Regex', {regex: "^(100|[0-9]{1,2})$"});
            $('#amountdiv').hide();
            $('#amount').keyup(selectAmount);

            $.editable.addInputType('masked', {
                element: function (settings, original) {
                    //var input = $('<input type="text">').mask(settings.mask);
                    var input = $('<input type="text">');
                    input.attr("data-inputmask-regex", settings.mask);
                    if (settings.width != 'none') {
                        input.width(settings.width);
                    }
                    if (settings.height != 'none') {
                        input.height(settings.height);
                    }
                    input.attr('autocomplete', 'off');
                    input.inputmask("Regex");
                    $(this).append(input);
                    return (input);
                }
            });
        });
    </script>


    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
    <title>Results</title>
    <script type="text/javascript">

        function processGroups() {
            var id = $('.interview:visible').find('#interview_id').val();

            $.ajax({
                type: 'POST',
                url: "AjaxController",
                dataType: "xml",
                data: {
                    "command": "getGroupsByInterviewId",
                    "interview_id": id
                },
                complete: function (data) {
                    processGroupsResult(data);
                }
            });
        }

        function processGroupsResult(data) {
            $("#list_of_trainees").empty();
            $('#group_select').empty();
            $('#group_select').append('<option value="" disabled selected>' + $.t("students_choose_group") + '</option>');
            var xml = data.responseXML;
            $(xml)
                    .find('group')
                    .each(
                    function (index) {
                        var id = $(this).find('id').text();
                        var title = $(this).find('title').text();
                        $('#group_select').append('<option value="' + id + '">' + title + '</option>')
                    });
            $('select').material_select();
            $('#addStudentsModal').openModal();
        }

        function processInterview() {

            var id = $('.interview:visible').find('#interview_id').val();

            $.ajax({
                type: 'POST',
                url: "AjaxController",
                dataType: "xml",
                data: {
                    "command": "interviewResults",
                    "interview_id": id
                },
                complete: function (data) {
                    processInterviewResult(data);
                }
            });
        }

        function processInterviewResult(data) {
            var xml = data.responseXML;
            var traineeTable = $('.interview:visible').find('#trainees_table');
            traineeTable.empty();

            traineeTable.append('<table class="datatable display centered"  width="100%" cellspacing="0">'
                    + '<thead>'
                    + '<tr>'
                    + '<th>' + $.t("students_last_name") + '</th>'
                    + '<th>' + $.t("students_first_name") + '</th>'
                    + '<th>' + $.t("students_parent_name") + '</th>'
                    + '<th>' + $.t("students_result") + '</th>'
                    + '<th>' + $.t("students_add_to_students") + '</th>'
                    + '</tr>'
                    + '</thead>'
                    + '<tbody>');

            $(xml)
                    .find('trainee')
                    .each(
                    function (index) {

                        var id = $(this).find('id').text();
                        var lastName = $(this).find('lastname').text();
                        var firstName = $(this).find('firstname').text();
                        var parentName = $(this).find('parentname').text();
                        var result = $(this).find('result').text();

                        //$('#trainees_table tbody')
                        traineeTable.find('tbody').append('<tr style="height: 40px;">'
                                + '<td class="lastname">' + lastName + '</td>'
                                + '<td class="firstname">' + firstName + '</td>'
                                + '<td class="parentname">' + parentName + '</td>'
                                + '<td>' + '<div class="edit" id="' + id + '">' + result + '</div>' + '</td>'
                                + '<td style="text-align: center; vertical-align: baseline;"> <input type="checkbox" class="filled-in" id="trainee' + id + '"/>'
                                + '<label for="trainee' + id + '"> </label></td>' + +'</tr>')

                    })
            traineeTable.append('</tbody> </table>');
            if($(xml).find('trainee').length != 0) {
                traineeTable.append('<a class="waves-effect waves-light btn modal-trigger" onclick="processGroups()">' + $.t("students_add_to_students") + '</a>');
            }

            var oTable = traineeTable.find('.datatable').dataTable({
                "iDisplayLength": 25,
                "bLengthChange": false,
                "order": [[3, "desc"]],
                "aoColumnDefs": [{"bSortable": false, "aTargets": [0, 1, 2, 3, 4]}],
                "oLanguage": {
                    "sZeroRecords": $.t("dt_zero_records"),
                    "sSearch" : $.t("dt_search"),
                    "sInfo": $.t("dt_sInfo"),
                    "sInfoEmpty": $.t("dt_sInfoEmpty"),
                    "sInfoFiltered": $.t("dt_filtering"),
                    "oPaginate": {
                        "sFirst": $.t("dt_first_page"),
                        "sLast": $.t("dt_last_page"),
                        "sNext": $.t("dt_next_page"),
                        "sPrevious": $.t("dt_previous_page")
                    }
                }
            });

            $('.dataTables_filter label').css('position', 'inherit');
            $('.dataTables_length label').css('position', 'inherit');

            $('.edit').editable('AjaxController?command=setInterviewResults', {
                callback: function (sValue, y) {
                    oTable.fnDestroy();

                    traineeTable.find('.datatable').dataTable({
                        "iDisplayLength": 25,
                        "bLengthChange": false,
                        "order": [[3, "desc"]],
                        "aoColumnDefs": [{"bSortable": false, "aTargets": [0, 1, 2, 3, 4]}],
                        "oLanguage": {
                            "sZeroRecords": $.t("dt_zero_records"),
                            "sSearch" : $.t("dt_search"),
                            "sInfo": $.t("dt_sInfo"),
                            "sInfoEmpty": $.t("dt_sInfoEmpty"),
                            "sInfoFiltered": $.t("dt_filtering"),
                            "oPaginate": {
                                "sFirst": $.t("dt_first_page"),
                                "sLast": $.t("dt_last_page"),
                                "sNext": $.t("dt_next_page"),
                                "sPrevious": $.t("dt_previous_page")
                            }
                        }
                    });

                    $('.dataTables_filter label').css('position', 'inherit');
                    $('.dataTables_length label').css('position', 'inherit');


                    /*var aPos = oTable.fnGetPosition(this);
                     oTable.fnUpdate( sValue, aPos[0], aPos[1] );*/
                },
                width: '35',
                submitdata: {
                    interview_id: $('.interview:visible').find('#interview_id').val(),
                },
                type: 'masked',
                //mask: '99'
                mask: '^(100|[0-9]{1,2})$',
            });

            $('select').material_select();
        }

        function addStudents() {
            var groupId = $('#group_select').val();
            var interviewId = $('.interview:visible').find('#interview_id').val();

            if ($('#informOtherTrainees').is(':checked')) {
                informOtherTrainees = 'true';
            } else {
                informOtherTrainees = 'false';
            }
            if (groupId && checkedTrainees.length > 0) {
                $.ajax({
                    type: 'POST',
                    url: "AjaxController",
                    dataType: "xml",
                    data: {
                        "command": "doStudent",
                        "trainee_ids": checkedTrainees,
                        "group_id": groupId,
                        "interview_id": interviewId,
                        "inform": informOtherTrainees
                    },
                    complete: function (data) {
                        processAddStudentsResult(data);
                    }
                });
            } else if (!groupId) {
                Materialize.toast($.t("students_choose_group"), 3000);
            } else if (checkedTrainees.length == 0) {
                Materialize.toast($.t("trainees_no_trainees_chosen"), 3000);
            }
        }

        function processAddStudentsResult(data) {
            var response = data.responseXML.documentElement.firstChild.nodeValue;
            if (response == 2) {
                Materialize.toast($.t("students_adding_error"), 3000);
            } else {
                Materialize.toast($.t("students_students_added"), 3000);
                checkedTrainees = [];
                $('#addStudentsModal').closeModal()
                processInterview();
            }
        }

        function showListOfTrainees() {
            $("#list_of_trainees").empty();
            $('.interview:visible').find('#trainees_table input:checked').each(function () {
                var trainee = $(this).parent().parent();
                $("#list_of_trainees").append(trainee.find(".lastname").text() + " ");
                $("#list_of_trainees").append(trainee.find(".firstname").text() + " ");
                $("#list_of_trainees").append(trainee.find(".parentname").text() + "<br>");
            })
        }

        function choseMode() {

            var numbOfTrainees;
            if ($('input[name=radioName]:checked').val() == 1) {
                numbOfTrainees = 10;
                $('#amount').val(numbOfTrainees)
                $('#amountdiv').show();
                var count = 0;
                $("#list_of_trainees").empty();
                checkedTrainees = [];

                $('.interview:visible').find('#trainees_table input:checkbox').each(function () {
                    var trainee = $(this).parent().parent();
                    if (count == numbOfTrainees) {
                        return false;
                    }
                    $("#list_of_trainees").append(trainee.find(".lastname").text() + " ");
                    $("#list_of_trainees").append(trainee.find(".firstname").text() + " ");
                    $("#list_of_trainees").append(trainee.find(".parentname").text() + "<br>");
                    checkedTrainees.push($(this).attr('id'));
                    count++;
                })
            } else if ($('input[name=radioName]:checked').val() == 0) {

                $("#list_of_trainees").empty();
                checkedTrainees = [];
                $('#amountdiv').hide();

                if ($('.interview:visible').find('#trainees_table input:checked').size() == 0) {
                    $("#list_of_trainees").append($.t("trainees_no_trainees_chosen"));
                }
                $('.interview:visible').find('#trainees_table input:checked').each(function () {
                    var trainee = $(this).parent().parent();
                    $("#list_of_trainees").append(trainee.find(".lastname").text() + " ");
                    $("#list_of_trainees").append(trainee.find(".firstname").text() + " ");
                    $("#list_of_trainees").append(trainee.find(".parentname").text() + "<br>");
                    checkedTrainees.push($(this).attr('id'));
                })
            }
        }

        function selectAmount() {
            if ($('#amount').val()) {
                var numbOfTrainees = $('#amount').val();
                var count = 0;
                $("#list_of_trainees").empty();
                checkedTrainees = [];

                $('.interview:visible').find('#trainees_table input:checkbox').each(function () {
                    var trainee = $(this).parent().parent();
                    if (count == numbOfTrainees) {
                        return false;
                    }
                    $("#list_of_trainees").append(trainee.find(".lastname").text() + " ");
                    $("#list_of_trainees").append(trainee.find(".firstname").text() + " ");
                    $("#list_of_trainees").append(trainee.find(".parentname").text() + "<br>");
                    checkedTrainees.push($(this).attr('id'));
                    count++;
                })
            } else {
                $('#list_of_trainees').empty();
                $("#list_of_trainees").append($.t("trainees_no_trainees_chosen"));
            }
        }

    </script>
</head>
<body bgcolor="#FAFAFA">
<jsp:include page="header.jsp"></jsp:include>
<jsp:include page="hr_panel_sidenav.jsp"></jsp:include>

<div class="parallax-container">
    <div class="parallax">
        <img
                src="${pageContext.request.contextPath}/img/hr/paralax/interview_results.jpg">
    </div>
</div>

<div class="col">
    <div class="row">
        <div class=" col s8 offset-s2 ">
            <div class="row" style="padding:30px;">
                <div class="col s12">
                    <h5 class="center-align blue-text text-darken-2"><e:msg key="hr.interview_results"></e:msg></h5>
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
                                            <h5 class="center-align blue-text text-darken-2"><e:msg key="hr.you_have_no_any_direction"></e:msg></h5>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <ul class="tabs">
                                        <c:forEach var="entry" items="${directions}">
                                            <li class="tab col s3"><a href="#id${entry.key.id}">${entry.key.name}</a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <c:forEach var="entry" items="${directions}">
                            <div id="id${entry.key.id}" class="interview col s12">
                                <div class="input-field col s12">
                                    <select id="interview_id" onchange="processInterview()">
                                        <c:choose>
                                            <c:when test="${empty entry.value}">
                                                <option value="" disabled selected><e:msg key="hr.interview"></e:msg></option>
                                            </c:when>

                                        <c:otherwise>
                                            <option value="" disabled selected><e:msg key="hr.interview"></e:msg></option>

                                            <c:forEach var="interview" items="${entry.value}">
                                                <option value="${interview.id}">${interview.dateOfTesting}</option>
                                            </c:forEach>
                                        </c:otherwise>
                                        </c:choose>
                                    </select>
                                    <label><e:msg key="hr.interview"></e:msg></label>
                                </div>

                                <div class="input-field col s12" id="trainees_table">
                                    <div class="row" style="padding:30px;">'
                                        <div class="col s12">
                                            <h5 class="center-align blue-text text-darken-2"><e:msg key="hr.select_interview"></e:msg> </h5>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="addStudentsModal" class="modal modal-fixed-footer">
    <div class="modal-content">
        <h4><e:msg key="hr.add_students"></e:msg></h4>

        <div class="row">
            <div class="input-field col s12">
                <select id="group_select">

                </select>
                <label><e:msg key="hr.select_group"></e:msg></label>

                <div class="row">
                    <div class="col s4">
                        <input type="radio" name="radioName" id="chosen" value="0" onclick="choseMode()"/>
                        <label for="chosen"><e:msg key="hr.select_chosen"></e:msg> </label>
                    </div>
                    <div class="col s4">
                        <input type="radio" name="radioName" id="rating" value="1" onclick="choseMode()"/>
                        <label for="rating"><e:msg key="hr.select_by_rating"></e:msg></label>
                    </div>
                    <div id="amountdiv" class="col s1">
                        <input id="amount" class="tooltipped" type="text" data-position="bottom" data-delay="50"
                               data-tooltip="<e:msg key="hr.amount_trainees_by_rating"></e:msg>">
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div id="list_of_trainees" class="col s8">
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <input type="checkbox" id="informOtherTrainees"/>
        <label for="informOtherTrainees"><e:msg key="hr.inform_other_trainees"></e:msg></label>
        <a href="#!" class=" modal-close waves-effect waves-green btn-flat"><e:msg key="close"></e:msg></a>
        <a href="#!" onclick="addStudents()" class=" modal-action waves-effect waves-green btn-flat"><e:msg key="hr.add_students"></e:msg></a>
    </div>
</div>
<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>
