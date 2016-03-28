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
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/datatable/jquery.dataTables.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/datatable/dataTables.bootstrap.js"></script>


    <!-- regex mask -->
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/inputmask/inputmask.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/inputmask/jquery.inputmask.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/inputmask/inputmask.regex.extensions.js"></script>

    <script type="text/javascript">

        $(document).ready(function () {
            $('select').material_select();
            $('ul.tabs').tabs();
        });
    </script>


    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
    <title>Students</title>
    <script type="text/javascript">

        var studentId;

        function processStudents() {

            var id = $('.group:visible').find('#group_id').val();

            $.ajax({
                type: 'POST',
                url: "AjaxController",
                dataType: "xml",
                data: {
                    "command": "getStudentsByGroupId",
                    "group_id": id
                },
                complete: function (data) {
                    processStudentsResult(data);
                }
            });
        }

        function processStudentsResult(data) {
            var xml = data.responseXML;
            var traineeTable = $('.group:visible').find('#students_table');
            traineeTable.empty();
            if ($(xml).find('student').length == 0) {
                traineeTable.append('<row><h5 class="center-align blue-text text-darken-2">' + $.t("students_no_students_in_this_group") + '</h5></row>');
            } else {
                traineeTable.append('<table id="datatable" class="table table-striped table-bordered" width="100%" cellspacing="0">'
                        + '<thead>'
                        + '<tr>'
                        + '<th>' + $.t("students_last_name") + '</th>'
                        + '<th>' + $.t("students_first_name") + '</th>'
                        + '<th>' + $.t("students_parent_name") + '</th>'
                        + '<th>' + $.t("students_action") + '</th>'
                        + '</tr>'
                        + '</thead>'
                        + '<tbody>');

                $(xml)
                        .find('student')
                        .each(
                        function (index) {

                            var id = $(this).find('id').text();
                            var lastName = $(this).find('lastname').text();
                            var firstName = $(this).find('firstname').text();
                            var parentName = $(this).find('parentname').text();

                            //$('#students_table tbody')
                            traineeTable.find('tbody').append('<tr style="height: 40px;">'
                                    + '<td class="lastname">' + lastName + '</td>'
                                    + '<td class="firstname">' + firstName + '</td>'
                                    + '<td class="parentname">' + parentName + '</td>'
                                    + '<td>' + '<a id="' + id + '"'
                                    + ' class="deleteStudentBtn waves-effect waves-light btn" >' + $.t("delete") + '</a>' + '</td>'
                                    + '</tr>')

                        })
                traineeTable.append('</tbody> </table>');
                $('.deleteStudentBtn').click(function () {
                    var fullName = $('#studentFullName');
                    studentId = this.id;
                    $('#studentFullName').empty();
                    var student = $(this).parent().parent();
                    fullName.append(student.find('.lastname').text());
                    fullName.append(' ');
                    fullName.append(student.find('.firstname').text());
                    fullName.append(' ');
                    fullName.append(student.find('.parentname').text());
                    $('#deleteStudentModal').openModal();
                });
            }
        }

        function deleteStudent() {
            $.ajax({
                type: 'POST',
                url: "AjaxController",
                dataType: "xml",
                data: {
                    "command": "deleteStudent",
                    "student_id": studentId
                },
                complete: function (data) {
                    processDeleteStudentResult(data);
                }
            });
        }

        function processDeleteStudentResult(data) {
            var response = data.responseXML.documentElement.firstChild.nodeValue;
            if (response == 2) {
                Materialize.toast($.t("students_deleting_error"), 3000);
            } else {
                Materialize.toast($.t("students_student_deleted"), 3000);
                processStudents();
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
                src="${pageContext.request.contextPath}/img/hr/paralax/students.jpg">
    </div>
</div>

<div class="col">
    <div class="row">
        <div class=" col s8 offset-s2 ">
            <div class="row" style="padding:30px;">
                <div class="col s12">
                    <h5 class="center-align blue-text text-darken-2"><e:msg key="students"></e:msg></h5>
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
                            <div id="id${entry.key.id}" class="group col s12">
                                <div class="input-field col s12">
                                    <select id="group_id" onchange="processStudents()">
                                        <option value="" disabled selected><e:msg key="hr.group"></e:msg> </option>
                                        <c:forEach var="group" items="${entry.value}">
                                            <option value="${group.id}">${group.title}</option>
                                        </c:forEach>
                                    </select>
                                    <label><e:msg key="hr.group"></e:msg> </label>
                                </div>

                                <div class="input-field col s12" id="students_table">
                                    <div class="row" style="padding:30px;">'
                                        <div class="col s12">
                                            <h5 class="center-align blue-text text-darken-2"><e:msg key="hr.select_group"></e:msg></h5>
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
<div id="deleteStudentModal" class="modal">
    <div class="modal-content">
        <h4><e:msg key="hr.delete_student"></e:msg></h4>

        <p><e:msg key="hr.delete_student_from_group"></e:msg></p>

        <div id="studentFullName"></div>
    </div>
    <div class="modal-footer">
        <a href="#!" class=" modal-close waves-effect waves-green btn-flat"><e:msg key="cancel"></e:msg></a>
        <a href="#!" onclick="deleteStudent()" class=" modal-action modal-close waves-effect waves-green btn-flat"><e:msg key="delete"></e:msg> </a>
    </div>
</div>
<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>
