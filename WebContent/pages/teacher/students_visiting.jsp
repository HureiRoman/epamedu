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


  <script type="text/javascript" src="${pageContext.request.contextPath}/js/datatable/jquery.dataTables.js"></script>
  <link href="${pageContext.request.contextPath}/css/datatable/jquery.dataTables.css"
        type="text/css" rel="stylesheet"/>


  <script type="text/javascript">
    var checkedTrainees = [];
    $(document).ready(function () {
      $('select').material_select();
      $('ul.tabs').tabs();
      $(':checkbox').change(function() {
        var isPresent;
        if($(this).is(':checked')) {
          isPresent = 'true';
        } else {
          isPresent = 'false';
        }
        setVisit(isPresent, $(this).attr('id'));
      });
    });

    function setVisit(isPresent, studentId) {
      var lessonId = $('table').attr('id');
      $.ajax({
        type: 'POST',
        url: "AjaxController",
        dataType: "xml",
        data: {
          "command": "setStudentVisit",
          "is_present": isPresent,
          "student_id" : studentId,
          "lesson_id" : lessonId
        },
        complete: function (data) {
          processSetVisitResult(data);
        }
      });
    }

    function processSetVisitResult(data) {
      var response = data.responseXML.documentElement.firstChild.nodeValue;
      if (response != 1) {
        Materialize.toast("error", 3000);
      }
    }
  </script>

  <meta name="viewport"
        content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
  <title>Visiting</title>
</head>
<body bgcolor="#FAFAFA">
<jsp:include page="header.jsp"></jsp:include>
<jsp:include page="teacher_panel_sidenav.jsp"></jsp:include>

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
          <h5 class="center-align blue-text text-darken-2"><e:msg key="teacher.students_visiting"></e:msg></h5>
        </div>
      </div>
      <div class="card-panel white">
        <div class="row">
          <div class="row">
            <div class="col s12">
              <c:choose>
                <c:when test="${empty students}">
                  <div class="row" style="padding:30px;">
                    <div class="col s12">
                      <h5 class="center-align blue-text text-darken-2"><e:msg key="teacher.there_is_no_students_in_group"></e:msg></h5>
                    </div>
                  </div>
                </c:when>
                <c:otherwise>
                  <table class="display centered" id="${lesson.id}" width="100%" cellspacing="0">
                    <thead>
                    <tr>
                      <th>
                        <e:msg key="registration.lname"></e:msg>
                      </th>
                      <th>
                        <e:msg key="registration.fname"></e:msg>
                      </th>
                      <th>
                        <e:msg key="registration.pname"></e:msg>
                      </th>
                      <th>
                        <e:msg key="present"></e:msg>
                      </th>
                    </tr>
                    </thead>
                    <tbody>
                      <c:forEach var="entry" items="${students}">
                        <tr>
                          <td>
                              ${entry.key.lastName}
                          </td>
                          <td>
                              ${entry.key.firstName}
                          </td>
                          <td>
                              ${entry.key.parentName}
                          </td>
                          <td  style="text-align: center; vertical-align: baseline;">
                            <c:choose>
                              <c:when test="${entry.value == 'true'}">
                                <input type="checkbox" id="${entry.key.id}" name="option1" value="a1" checked>
                                <label for="${entry.key.id}"></label>
                              </c:when>
                              <c:otherwise>
                                <input type="checkbox" id="${entry.key.id}" name="option2" value="a2">
                                <label for="${entry.key.id}"></label>
                              </c:otherwise>
                            </c:choose>
                          </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </c:otherwise>
              </c:choose>
          </div>
        </div>
      </div>
        <div class="row">
          <a class="btn col s3" href="/EpamEducationalProject/Controller?command=groupInfo&groupId=${group_id}"><e:msg key="back"></e:msg></a>
        </div>
    </div>
  </div>
</div>

<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>
