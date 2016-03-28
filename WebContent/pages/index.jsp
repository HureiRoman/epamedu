<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="requirements.jsp"></jsp:include>
<style type="text/css">
.bx-wrapper .bx-controls-direction a {
	position: absolute;
	top: 50% !important;
	margin-top: -16px;
	outline: 0;
	width: 32px;
	height: 32px;
	text-indent: -9999px;
	z-index: 0;
}
</style>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/slider-pro.css" />
<script src="${pageContext.request.contextPath}/js/jquery.sliderPro.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.jqtransform.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.jcarousel.js"></script>
<script
	src="${pageContext.request.contextPath}/js/jquery.bxslider.min.js"></script>
<link href="${pageContext.request.contextPath}/css/jquery.bxslider.css"
	rel="stylesheet" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/examples.css"
	media="screen" />
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,600'
	rel='stylesheet' type='text/css'>
<script type="text/javascript">
	$(document).ready(function($) {
		//Check to show registration sended email toast
		var regSended = '${regSended}';
		if(regSended.trim() != ''){
			Materialize.toast(regSended, 3000);
			window.history.pushState("", "", '/EpamEducationalProject/');
		}
		
		$('.bxslider').bxSlider({
			  minSlides: 4,
			  maxSlides: 4,
			  slideWidth: 300,
			  responsive: true
			});
		
		$('#news').sliderPro({
			width : 1360,
			height : 500,
			loop : false,
			arrows : true,
			buttons : false,
			waitForLayers : true,
			thumbnailWidth : 300,
			thumbnailHeight : 100,
			thumbnailPointer : true,
			autoplay : false,
			autoScaleLayers : false,
			breakpoints : {
				500 : {
					thumbnailWidth : 120,
					thumbnailHeight : 50
				}
			}
		});
		$('.slider').slider({
			full_width : true,
			Interval : 5000,
			height : 200
		});
	});
	
	 function prepareText(text){
		  return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
		 }
	
	function leaveReview(directionId) {
		var review = prepareText($('#review_message'+directionId).val());
		if (review != '') {
			$
					.ajax({
						type : 'POST',
						url : "AjaxController",
						dataType : "json",
						data : {
							"command" : "leaveReview",
							"directionId" : directionId,
							"message" : review
						},
						complete : function(data) {
							response = data.responseJSON;
							if (response.result == 1) {
								if ($("#reviews"+directionId+" .collection-item").length == 0) {
									$("#reviews"+directionId)
											.append(
													'<li class="collection-item avatar">'
															+ '<img  src="${pageContext.request.contextPath}/images?type=users&id=${logined_user.id}"  class="circle" >'
															+ '<span class="title" >${logined_user.firstName} ${logined_user.lastName} </span>'
															+ '<p>' + review
															+ '</p>' + '</li>');
									$('#review_message'+directionId).val("");
									$('#noComments'+directionId).remove();
									/*			builder.append("<a href=\"#!\" class=\"secondary-content\"><i class=\"material-icons\">close</i></a>");*/

								} else {
									$("#reviews"+directionId+" .collection-item:last  ")
											.after(
													'<li class="collection-item avatar">'
															+ '<img  src="${pageContext.request.contextPath}/images?type=users&id=${logined_user.id}"  class="circle" >'
															+ '<span class="title" >${logined_user.firstName} ${logined_user.lastName} </span>'
															+ '<p>' + review
															+ '</p>' + '</li>');
									$('#review_message'+directionId).val("");
									/*			builder.append("<a href=\"#!\" class=\"secondary-content\"><i class=\"material-icons\">close</i></a>");*/

								}
								Materialize.toast($.t("review_added"), 3000);
							} else {
								Materialize.toast($.t("unexpected_error"), 3000);
							}
						}
					});
		} else {
			Materialize.toast($.t("you_must_write_your_review"), 3000);
		}
	}
	var currentCommentId;
	function showDeleteCommentModal(id){
		currentCommentId = id;
		$('#deleteReviewCommentModal').openModal();
	}
	function deleteComment(){
		$
		.ajax({
			type : 'POST',
			url : "AjaxController",
			dataType : "json",
			data : {
				"command" : "deleteReview",
				"commentId" : currentCommentId
			},
			complete : function(data) {
				response = data.responseJSON;
				if (response.result == 1) {
					var idOfParent = $('#courseComment'+currentCommentId).parent(".collection").prop("id").replace(/[^\d.-]/g, '');
					var directionId = parseInt(idOfParent);
					$('#courseComment'+currentCommentId).remove();
					if($("#reviews"+directionId+" .collection-item").length == 0) {
						$("#reviews"+directionId)
						.append('<li id="noComments'+directionId+'" class="row">'
								+'<div class="valign-wrapper  offset-s4  col s6">'
								+'<i class="large mdi-content-content-paste blue-text "></i>'
								+'<h5 class="valign center-align">'+$.t('noComment')+'</h5>'
								+'</div>'
								+'</li>'							
						);
					}
					Materialize.toast($.t('review_deleted'), 3000);
				} else {
					Materialize.toast($.t("unexpected_error"), 3000);
				}
			}
		});
	}
</script>
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>Epam.edu</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<div id="index-banner" class="parallax-container" style="min-height:280px;">
		<div class="slider ">
			<ul class="slides">
				<li><img src="img/it1.jpg">
					<div class="caption right-align">
						<h3>Epam edu</h3>
						<h5 class="light grey-text text-lighten-2"><e:msg key="index.letsDoIT"></e:msg></h5>
					</div></li>
				<li><img src="img/it2.jpg">
					<div class="caption left-align">
						<h3>Epam edu</h3>
						<h5 class="light grey-text text-lighten-2"><e:msg key="index.Accuracy"></e:msg></h5>
					</div></li>
				<li><img src="img/it3.jpeg">
					<div class="caption left-align">
						<h3 class="light-green-text text-darken-2">Epam edu</h3>
						<h5 class="light-green-text text-darken-2"><e:msg key="index.comfort"></e:msg></h5>
					</div></li>
				<li><img src="img/it4.jpg">
					<div class="caption center-align">
						<h3 class="light-green-text text-darken-2">Epam edu</h3>
						<h5 class="light-green-text text-darken-2"><e:msg key="index.Creativity"></e:msg></h5>
					</div></li>
			</ul>
		</div>
	</div>
	
	<c:if test="${not empty directions}">
	<div class="row">
	<div class="row"><h5 class="offset-s4 col s4 text1"><e:msg key="index.epamDirections"></e:msg></h5></div>
		<div class="col s12">
			<ul class="tabs">
				<c:forEach items="${directions}" var="direction">
					<li class="tab col s3"><a href="#dir${direction.id}">${direction.name}</a></li>
				</c:forEach>
			</ul>
		</div>
		<c:forEach items="${directions}" var="direction">
			<div id="dir${direction.id}" class="col s12">
				<div class="row">
					<div class="col s4">
						<div class="card hoverable">
							<div class="card-image waves-effect waves-block waves-light">
								<img class="activator"
									src="${pageContext.request.contextPath}/images?type=directions&id=${direction.id}">
							</div>
							<div class="card-content">
								<span class="card-title activator grey-text text-darken-4">${direction.name}
								</span>
							</div>
							<div class="card-reveal">
								<span class="card-title grey-text text-darken-4"><e:msg key="index.description"></e:msg>
								</span>
								<p>${direction.description}</p>
							</div>
						</div>
					</div>
					<div class="col s8">
						<e:courseReviews direction="${direction}"></e:courseReviews>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
	</c:if>
	<e:news news="${news}"></e:news>
		<c:if test="${not empty events}">
			<div>
		<h5 class="row offset-s4 col s4 text1 center"><e:msg key="index.futureInterviews"></e:msg></h5>
				<ul class="bxslider row">
									<c:forEach items="${events}" var="event">
										<e:eventCard event="${event}"></e:eventCard>
									</c:forEach>
				</ul>
			</div>
		</c:if>
		
	<div style="max-height: 200px;" id="deleteReviewCommentModal" class="modal modal-fixed-footer">
		<div
			style="padding-left: 15%; padding-right: 15%; text-align: center;">
			<div class="row">
				<i class="medium mdi-action-info text-teal"></i><e:msg key="index.deleteComment"></e:msg>
			</div>
			<div>
				<button onclick="deleteComment()"
					class="btn waves-effect waves-light modal-action modal-close"
					onmouseout="this.style.backgroundColor='#2196F3';"
					onmouseover="this.style.backgroundColor='red';" type="button">
					<e:msg key="index.deleteButton"></e:msg><i class="mdi-action-delete"></i>
				</button>
				<button onclick="$('#deleteReviewCommentModal').closeModal();"
					class="btn waves-effect waves-light"
					onmouseout="this.style.backgroundColor='#2196F3';"
					onmouseover="this.style.backgroundColor='#FFEB3B';" type="button">
					<e:msg key="index.cancelButton"></e:msg><i class="mdi-navigation-cancel"></i>
				</button>

			</div>
		</div>
	</div>
	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>