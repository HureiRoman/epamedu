<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 <script type="text/javascript" src="//vk.com/js/api/openapi.js?116"></script>

<footer class="page-footer " style="height:140px;">
	<div class="container" >
		<div class="row">
			<div class="col l6 s12">
				<h5 class="white-text">EPAM.EDU</h5>
				<p class="grey-text text-lighten-4">Let's do it</p>
				<!-- VK Widget -->
			
			</div>
			
			<div class="col l3 s12">
				<i
					class="mdi-content-link small white-text  left waves-effect waves-black"></i>
				<h5 class="white-text"><e:msg key="links"></e:msg></h5>
				<c:set var="req" value="${pageContext.request}" />
				<c:set var="currentJSP" value="${fn:substring(req.requestURI, fn:length(req.requestURI) - 9, fn:length(req.requestURI))}" />
				<ul>
					<li>
					<c:choose>
						<c:when test="${currentJSP == 'index.jsp' }">
							<a class="waves-effect white-text waves-black"
							href="#"><i
							class="mdi-action-home  left "></i> <e:msg key="main"></e:msg></a>
						</c:when>
						<c:otherwise>
							<a class="waves-effect white-text waves-black"
							href="${pageContext.request.contextPath}/"><i
							class="mdi-action-home  left "></i> <e:msg key="main"></e:msg></a>
						</c:otherwise>
					</c:choose>
					</li>
					<li><a class="waves-effect white-text waves-black"
						onclick="$('#testsModal').openModal();"><i class="fa fa-bars left "></i> <e:msg key="header.Tests"></e:msg></a></li>
					
				</ul>
			</div>
			<div class="col l3 s12">
				<i class="mdi-communication-call small white-text  left waves-effect waves-black"></i>
				<h5 class="white-text">
					<e:msg key="feedBack"></e:msg>
				</h5>
				<ul>
					<li class="valign-wrapper"><a class="white-text"
						href="http://vk.com/epam_edu"><e:msg
								key="footer.groupVK"></e:msg></a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="footer-copyright">
		<div class="container">
			Copyright <a href="#" class="brown-text text-lighten-3">JTEAM</a>
		</div>

				
				


		
	</div>
</footer>