<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="e" uri="http://epam.edu/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <jsp:include page="../requirements.jsp"></jsp:include>

    <script type="text/javascript">
        $(document).ready(function () {
            $(".card").hover(function () {
                $(this).stop().animate({
                    opacity: "0.5"
                }, 'slow');
            }, function () {
                $(this).stop().animate({
                    opacity: "1"
                }, 'slow');
            });

        });

    </script>


    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
    <title>HR cabinet</title>

</head>


<body bgcolor="#FAFAFA">

<jsp:include page="header.jsp"></jsp:include>
<jsp:include page="hr_panel_sidenav.jsp"></jsp:include>

<div class="row" style="padding-top:30px;">
    <div class="col s4">
        <a href="${pageContext.request.contextPath}/Controller?command=redirect&direction=myProfile">
            <div class="card medium">
                <div class="card-image">

                    <img class="img-responsive"
                         src="${pageContext.request.contextPath}/img/edit_profile.jpg" style="height:100%">

                </div>
                <div class="card-content">
                    <span class="card-title black-text"><e:msg key="myProfile"></e:msg> </span>
                </div>
            </div>
        </a>
    </div>

    <div class="col s4">
        <a href="${pageContext.request.contextPath}/Controller?command=interviewResults">
            <div class="card medium">
                <div class="card-image">
                    <img class="img-responsive"
                         src="${pageContext.request.contextPath}/img/hr/interview_results_icon.jpg">
                </div>
                <div class="card-content">
                    <span class="card-title black-text"><e:msg key="hr.interview_results"></e:msg></span>
                </div>
            </div>
        </a>
    </div>
    <div class="col s4">
        <a href="${pageContext.request.contextPath}/Controller?command=redirect&direction=hr_to_trainee_message">
            <div class="card medium">
                <div class="card-image">
                    <img class="img-responsive"
                         src="${pageContext.request.contextPath}/img/hr/mailing_icon.jpg">
                </div>
                <div class="card-content" id="message_card">
                    <span class="card-title black-text" style="float:left;"><e:msg key="hr.contacting_with_trainees"></e:msg> </span>
                </div>
            </div>
        </a>
    </div>


</div>

<div class="row" style="padding-top:30px;">
    <div class="col s4">
        <a href="${pageContext.request.contextPath}/Controller?command=manageInterviews">
            <div class="card medium">
                <div class="card-image">

                    <img class="img-responsive"
                         src="${pageContext.request.contextPath}/img/hr/interview_icon.jpg" style="height:100%">

                </div>
                <div class="card-content">
                    <span class="card-title black-text"><e:msg key="hr.manage_interviews"></e:msg> </span>
                </div>
            </div>
        </a>
    </div>
    <div class="col s4">
        <a href="${pageContext.request.contextPath}/Controller?command=showStudents">
            <div class="card medium">
                <div class="card-image">
                    <img class="img-responsive"
                         src="${pageContext.request.contextPath}/img/hr/students_icon.jpg">
                </div>
                <div class="card-content">
                    <span class="card-title black-text"><e:msg key="students"></e:msg></span>
                </div>
            </div>
        </a>
    </div>
    <div class="col s4">
        <a href="${pageContext.request.contextPath}/Controller?command=viewTrainees">
            <div class="card medium">
                <div class="card-image">
                    <img class="img-responsive"
                         src="${pageContext.request.contextPath}/img/hr/trainees_icon.jpg">
                </div>
                <div class="card-content" >
                    <span class="card-title black-text" style="float:left;"><e:msg key="trainees"></e:msg> </span>
                </div>
            </div>
        </a>
    </div>


</div>


<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>