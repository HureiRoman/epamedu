<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="e" uri="http://epam.edu/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

  <jsp:include page="../requirements.jsp"></jsp:include>

  <script type="text/javascript" src="${pageContext.request.contextPath}/js/datatable/jquery.dataTables.js"></script>
  <link href="${pageContext.request.contextPath}/css/datatable/jquery.dataTables.css"
        type="text/css" rel="stylesheet"/>

  <script type="text/javascript" src="${pageContext.request.contextPath}/js/chart/raphael-min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/chart/morris.js"></script>
  <link href="${pageContext.request.contextPath}/css/chart/morris.css"
        type="text/css" rel="stylesheet"/>

  <script type="text/javascript">
    $(document).ready(function () {
      $('#students_grades').dataTable({
        "iDisplayLength": 25,
        "bLengthChange": false,
        "oLanguage": {
          "sZeroRecords": "<e:msg key="dt_zero_records"></e:msg>",
          "sSearch" : "<e:msg key="dt_search"></e:msg>",
          "sInfo": "<e:msg key="dt_sInfo"></e:msg>",
          "sInfoEmpty": "<e:msg key="dt_sInfoEmpty"></e:msg>",
          "sInfoFiltered": "<e:msg key="dt_filtering"></e:msg>",
          "oPaginate": {
            "sFirst": "<e:msg key="dt_first_page"></e:msg>",
            "sLast": "<e:msg key="dt_last_page"></e:msg>",
            "sNext": "<e:msg key="dt_next_page"></e:msg>",
            "sPrevious": "<e:msg key="dt_previous_page"></e:msg>"
          }
        }
      });
      $('.dataTables_filter label').css('position', 'inherit');
      $('.dataTables_length label').css('position', 'inherit');

      var perfect = $('#perfect').val();
      var good = $('#good').val();
      var medium = $('#medium').val();
      var bad = $('#bad').val();


      /*var badLabel = $.t("bad_level");
      var mediumLabel = $.t("medium_level");
      var goodLabel = $.t("good_level");
      var perfectLabel = $.t("perfect_level");*/
      
      var badLabel = $("#bad").attr("name");
      var mediumLabel = $("#medium").attr("name");
      var goodLabel = $("#good").attr("name");
      var perfectLabel = $("#perfect").attr("name");

      Morris.Donut({
        element: 'chart',
        data: [
        {value: bad, label: badLabel},
        {value: medium, label: mediumLabel},
        {value: good, label: goodLabel},
        {value: perfect, label: perfectLabel}
        ],
        backgroundColor: '#ccc',
        labelColor: '#060',
        colors: [
        '#0BA462',
        '#39B580',
        '#67C69D',
        '#95D7BB'
        ],
        formatter: function (x) { return x + "%"}
      });
    });
  </script>

  <meta name="viewport"
        content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
  <title>Grades Of Tasks</title>
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
          <h5 class="center-align blue-text text-darken-2"><e:msg key="teacher.grades_of_tasks"></e:msg></h5>
        </div>
      </div>
      <div class="card-panel white">
        <div class="row">
          <div class="row">
            <div class="col s12">
              <c:choose>
                <c:when test="${empty students_grades}">
                  <div class="row" style="padding:30px;">
                    <div class="col s12">
                      <h5 class="center-align blue-text text-darken-2"><e:msg key="teacher.there_is_no_students_in_group"></e:msg></h5>
                    </div>
                  </div>
                </c:when>
                <c:otherwise>
                  <c:choose>
                    <c:when test="${empty tasks}">
                      <div class="row" style="padding:30px;">
                        <div class="col s12">
                          <h5 class="center-align blue-text text-darken-2"><e:msg key="teacher.there_are_no_task"></e:msg></h5>
                        </div>
                      </div>
                    </c:when>
                    <c:otherwise>
                  <table class="responsive-table display centered" id="students_grades" width="100%" cellspacing="0">
                    <thead>
                      <th>
                        <e:msg key="registration.lname"></e:msg>
                      </th>
                      <th>
                        <e:msg key="registration.fname"></e:msg>
                      </th>
                      <c:forEach var="task" items="${tasks}">
                        <th>
                          ${task.title}
                        </th>
                      </c:forEach>
                      <th>
                        <e:msg key="total"></e:msg>
                      </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="entry" items="${students_grades}">
                      <tr>
                        <td>
                            ${entry.key.lastName}
                        </td>
                        <td>
                            ${entry.key.firstName}
                        </td>
                        <c:forEach var="grade" items="${entry.value}">
                          <td>
                            ${grade}
                          </td>
                        </c:forEach>
                      </tr>
                    </c:forEach>
                    </tbody>
                  </table>
                      <input id="bad" type="hidden" name="<e:msg key="bad_level"></e:msg>" value="${bad_level}">
                      <input id="medium" type="hidden" name="<e:msg key="medium_level"></e:msg>" value="${medium_level}">
                      <input id="good" type="hidden" name="<e:msg key="good_level"></e:msg>" value="${good_level}">
                      <input id="perfect" type="hidden" name="<e:msg key="perfect_level"></e:msg>" value="${perfect_level}">

                      <div class="col s12">
                        <h5 class="center-align blue-text text-darken-2"><e:msg key="teacher.student_performance"></e:msg></h5>
                      </div>
                      <div id="chart" style="height: 250px;"></div>
                    </c:otherwise>
                  </c:choose>
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
