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
          src="${pageContext.request.contextPath}/js/chart/jquery.horizBarChart.min.js"></script>
  <link href="${pageContext.request.contextPath}/css/chart/horizBarChart.css"
        type="text/css" rel="stylesheet" media="screen,projection" />

  <script type="text/javascript">
    $(document).ready(function(){
      $('.chart').horizBarChart({
        selector: '.bar',
        speed: 3000
      });
    });
  </script>

  <meta name="viewport"
        content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
  <title>Statistic</title>
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
          <h5 class="center-align blue-text text-darken-2"><e:msg key="teacher.visiting_statistic"></e:msg></h5>
        </div>
      </div>
      <div class="card-panel white">
        <div class="row">
          <div class="row">
            <div class="col s12">
              <c:choose>
                <c:when test="${empty statistic}">
                  <div class="row" style="padding:30px;">
                    <div class="col s12">
                      <h5 class="center-align blue-text text-darken-2"><e:msg key="teacher.there_is_no_students_in_group"></e:msg></h5>
                    </div>
                  </div>
                </c:when>
                <c:otherwise>
                <ul class="chart">
                  <li class="title" title=""></li>
                  <li class="current" title=""><span class="bar" data-number="${amount_of_all_lessons}"></span><span class="number">${amount_of_all_lessons}</span></li>
                  <div class="title_before"><e:msg key="teacher.amount_of_all_lessons"></e:msg></div><br/>
                  <c:forEach var="entry" items="${statistic}">
                      <li class="past" title="">
                      <span class="bar" data-number="${entry.value}"></span><span class="number">${entry.value}</span></li>
                    <div class="title_before">${entry.key.lastName} ${entry.key.firstName}</div><br/>
                  </c:forEach>
                  </ul>
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
