<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../requirements.jsp"></jsp:include>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/knockout-3.3.0.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/pagination/jquery.quick.pagination.min.js"></script>
<link
	href="${pageContext.request.contextPath}/css/pagination/styles.css"
	type="text/css" rel="stylesheet" media="screen,projection" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />

<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						$('#newsList').quickPagination({
							pageSize : "10"
						});

						document.getElementById('new_newsitem_photo').onchange = function(
								evt) {
							alert('change');
							var tgt = evt.target || window.event.srcElement, files = tgt.files;
							// FileReader support
							if (FileReader
									&& files
									&& files.length
									&& files[0].name
											.match(/\.(jpg|jpeg|png|gif)$/)) {
								var fr = new FileReader();
								fr.onload = function() {
									document
											.getElementById('old_newsitem_photo').src = fr.result;
								}
								fr.readAsDataURL(files[0]);
							} else {
								Materialize.toast($.t('choose_correct_image'),
										3000)
							}
						}

						
						
						document.getElementById('photo_field').onchange = function(
								evt) {
							var tgt = evt.target || window.event.srcElement, files = tgt.files;
							// FileReader support
							if (FileReader
									&& files
									&& files.length
									&& files[0].name
											.match(/\.(jpg|jpeg|png|gif)$/)) {
								var fr = new FileReader();
								fr.onload = function() {
									document
											.getElementById('news_item_display_photo').src = fr.result;
								}
								fr.readAsDataURL(files[0]);
							} else {
								Materialize.toast($.t('choose_correct_image'),
										3000)
							}
						}
					});

	function prepareText(text) {
		return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
	}

	var currentNewsItemId;
	function prepareText(text) {
		return text.replace(/</g, "&lt;").replace(/>/g, "&gt;")
	}
	function addNewNewsItem() {
		var formData = new FormData();
		var title = prepareText($('#news_item_title').val());
		var description = prepareText($('#news_item_description').val());
		var content = prepareText($('#news_item_content').val());
		var photo = $('#photo_field')[0].files[0];
		if (title.length >= 5 && description.length >= 10
				&& content.length >= 10 && photo
				&& photo.name.match(/\.(jpg|jpeg|png|gif)$/)) {
			formData.append('title', title);
			formData.append('description', description);
			formData.append('content', content);
			formData.append('photo', photo);

			$.ajax({
				type : 'POST',
				url : "AjaxController?command=addNewsItem",
				data : formData,
				async : true,
				complete : function(data) {
					location.reload();
				},
				cache : false,
				contentType : false,
				processData : false
			});
		} else {
			Materialize.toast($.t("incorrectData"), 3000);
			if (title.length < 5) {
				Materialize.toast($.t("incorrectTitleMinFiveLetters"), 3000);
				return false;
			}
			if (description.length < 10) {
				Materialize.toast($.t("incorrectDescriptionMinTenLetters"),
						3000);
				return false;
			}
			if (content.length < 10) {
				Materialize.toast($.t("incorrectContentMinTenLatters"), 3000);
				return false;
			}
			if (!photo) {
				Materialize.toast($.t("MustToSpecifyPhoto"), 3000);
				return false;
			}

			if (!photo.name.match(/\.(jpg|jpeg|png|gif)$/)) {
				Materialize.toast($.t("MustToSpecifyCorrectPhoto"), 3000);
				return false;
			}
		}

	}
	function moderateNewsItem(id) {
		$.ajax({
			type : 'POST',
			url : "AjaxController?command=getNewsItemData",
			data : {
				'id' : id
			},
			dataType : "xml",
			async : true,
			complete : function(data) {
				bindNewsItemDataToModal(data);
			}
		});

	}

	function updateNewsItemData() {
		var formData = new FormData();
		var id = currentNewsItemId;
		var description = prepareText($('#edit_news_item_description').val());
		var content = prepareText($('#edit_news_item_content').val());
		var title = prepareText($('#edit_news_item_title').val());
		var photo = $('#new_newsitem_photo')[0].files[0];
		if (title.length >= 5 && description.length >= 10
				&& content.length >= 10) {
			if (photo) {
				if (!photo.name.match(/\.(jpg|jpeg|png|gif)$/)) {
					Materialize.toast($.t("incorrectPhotoFormat"), 3000);
					return false;
				}
			}
			formData.append('title', title);
			formData.append('description', description);
			formData.append('content', content);
			formData.append('photo', photo);
			formData.append('id', id);
			$.ajax({
				type : 'POST',
				url : "AjaxController?command=updateNewsItem",
				data : formData,
				async : true,
				dataType : "xml",
				complete : function(data) {
					Materialize.toast($.t("newsItemUpdated"), 3000);
					window.setTiemout(location.reload(), 4600);
				},
				cache : false,
				contentType : false,
				processData : false
			});
		} else {

			Materialize.toast($.t("incorrectData"), 3000);

			if (title.length < 5) {
				Materialize.toast($.t("incorrectTitleMinFiveLetters"), 3000);
				return false;
			}
			if (description.length < 10) {
				Materialize.toast($.t("incorrectDescriptionMinTenLetters"),
						3000);
				return false;
			}
			if (content.length < 10) {
				Materialize.toast($.t("incorrectContentMinTenLatters"), 3000);
				return false;
			}
		}

	}

	function bindNewsItemDataToModal(data) {
		var xml = data.responseXML;
		$(xml).find('newsItem').each(
				function(index) {
					var id = $(this).find('id').text();
					$('#edit_news_item_description').text(
							$(this).find('description').text());
					$('.input-field #edit_news_item_title').text(
							$(this).find('title').text());
					$('#edit_news_item_content').text(
							$(this).find('content').text());
					$('#old_newsitem_photo').attr(
							"src",
							'${pageContext.request.contextPath}/images?type=news&id='
									+ id);
					currentNewsItemId = id;
				});
		Materialize.updateTextFields();
		$('#editNewsItemModal').openModal();
	}
	function setNewsItemAvailable(id, archived) {
		$
				.ajax({
					type : 'POST',
					url : "AjaxController?command=setNewsItemArchived",
					dataType : "xml",
					data : {
						"id" : id,
						"archived" : archived
					},
					complete : function(data) {
						var response = data.responseXML.documentElement.firstChild.nodeValue;
						if (response == 1) {
							if (!Boolean(archived)) {
								Materialize.toast($.t("newsSwitchedOn"), 3000);
							} else {
								Materialize.toast($.t("newsSwitchedOf"), 3000);
							}
						} else {
							Materialize.toast($.t("errorChangingNewsStatus"),
									3000);
						}
					}
				});
	}

	function openDeleteNewsItemModal(id) {
		currentNewsItemId = id
		$('#deleteNewsItemModal').openModal();

	}

	function deleteNewsItem() {

		$.ajax({
			type : 'POST',
			url : "AjaxController?command=deleteNewsItem",
			data : {
				'id' : currentNewsItemId
			},
			dataType : "xml",
			async : true,
			complete : function(data) {
				processNewsItemDeletingResult(data);
			},
		});

	}

	function processNewsItemDeletingResult(data) {

		var response = data.responseXML.documentElement.firstChild.nodeValue;
		if (response == 'true') {
			Materialize.toast($.t('newsItemWasDeleted'), 3000)
			window.setTiemout(location.reload(), 4600);
		} else {
			Materialize.toast($.t('deletingError'), 3000)
		}
		//$('#deleteNewsItemModal').closeModal();

	}
	function openAddNewItemModal() {
		$('#news_item_title').val('');
		$('#news_item_description').val('');
		$('#news_item_content').val('');
		$('#addNewsItemModal').openModal();
	}
</script>
<title>News</title>
</head>
<body bgcolor="#FAFAFA">
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="admin_panel_sidenav.jsp"></jsp:include>
	<div class="container">
		<div class="row card-panel">
			<div class="col s10 ">
				<h4>
					<i class="fa  fa-newspaper-o medium blue-text"></i>
					<e:msg key="news"></e:msg>
				</h4>
			</div>
			<div class="col s2" style="margin-top: 10px">
				<a onclick="openAddNewItemModal()"
					class="btn-floating btn-large waves-effect waves-light green"><i
					class="mdi-content-add"></i></a>
			</div>
		</div>
		<div class="row">
			<ul id="newsList">
				<c:forEach items="${news}" var="news_item">
					<li><e:newsItemCard newsItem="${news_item }"></e:newsItemCard>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>

	<jsp:include page="../footer.jsp"></jsp:include>
	<div id="addNewsItemModal" class="modal"
		style="max-height: 500px !important;">
		<div class="modal-content">
			<h6>
				<i class="small mdi-action-info text-teal"></i>
				<e:msg key="news.addingNewsItem"></e:msg>
			</h6>
			<div class="row">
				<div class="col s6">
					<div class="input-field ">
						<i class="mdi-content-flag prefix"></i> <input
							id="news_item_title" type="text" class="validate" maxlength="50">
						<label for="news_item_title"><e:msg key="teacher.title"></e:msg>
						</label>
					</div>
					<div class="input-field ">
						<i class="mdi-editor-mode-comment prefix"></i>
						<textarea id="news_item_description" class="materialize-textarea"
							maxlength="60" style="max-height: 100px"></textarea>
						<label for="news_item_description"><e:msg
								key="news.shortDescription"></e:msg></label>
					</div>

				</div>
				<div class="col s6">
					<label><e:msg key="news.newsPhoto"></e:msg></label> <img id="news_item_display_photo"
						src="${pageContext.request.contextPath}/img/add_photo.png"
						height="200" width="300">
					<div class="file-field input-field">
						<div class="row">
							<div class="col s3">
								<div class="btn">
									<span><e:msg key="news.photo"></e:msg></span> <input
										type="file" id="photo_field">
								</div>
							</div>
							<div class="col s9">
								<input class="file-path validate" type="text" />
							</div>
						</div>


					</div>
				</div>

				<div class="input-field col s12 ">
					<i class="mdi-editor-insert-comment prefix"></i>
					<textarea id="news_item_content" class="materialize-textarea"
						maxlength="600" style="max-height: 100px"></textarea>
					<label for="news_item_content"><e:msg key="news.сaption"></e:msg></label>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<a id="butLogIn" onclick="addNewNewsItem();"
				class=" modal-action  waves-effect waves-green btn-flat left"><e:msg
					key="add"></e:msg></a>
		</div>
	</div>
	<div id="editNewsItemModal" class="modal"
		style="max-height: 500px !important;">
		<div class="modal-content">
			<h6>
				<i class="small mdi-action-info text-teal"></i>
				<e:msg key="news.editingNews"></e:msg>
			</h6>
			<div class="row">
				<div class="col s6">
					<div class="input-field active ">
						<i class="mdi-communication-email prefix"></i>
						<textarea id="edit_news_item_title" class="materialize-textarea"
							maxlength="40" style="max-height: 100px"></textarea>
						<label for="edit_news_item_title"><e:msg
								key="teacher.title"></e:msg></label>
					</div>
					<div class="input-field active ">
						<i class="mdi-editor-mode-edit prefix"></i>
						<textarea id="edit_news_item_description"
							class="materialize-textarea" maxlength="60"
							style="max-height: 100px"></textarea>
						<label for="edit_news_item_description"><e:msg
								key="news.shortDescription"></e:msg></label>
					</div>
				</div>
				<label><e:msg key="news.newsPhoto"></e:msg> </label>
				<div class="col s6">
					<img id="old_newsitem_photo" height="200" width="300">
					<div class="file-field input-field">
						<div class="row">
							<div class="col s3">
								<div class="btn">
									<span><e:msg key="news.photo"></e:msg></span> <input
										type="file" id="new_newsitem_photo">
								</div>
							</div>
							<div class="col s9">
								<input class="file-path validate" type="text" />
							</div>
						</div>
					</div>
				</div>

				<div class="input-field col s12 ">
					<i class="mdi-editor-mode-edit prefix"></i>
					<textarea id="edit_news_item_content" class="materialize-textarea"
						maxlength="600" style="max-height: 100px"></textarea>
					<label for="edit_news_item_content"><e:msg
							key="news.сaption"></e:msg></label>
				</div>
				<input type="text" id="news_item_to_moderate_id_holder"
					hidden="hidden">
			</div>
		</div>
		<div class="modal-footer">
			<a id="submitNewsModerating" onclick="updateNewsItemData();"
				class=" modal-action  waves-effect waves-green btn-flat left"><e:msg
					key="save"></e:msg></a>
		</div>
	</div>


	<div id="deleteNewsItemModal" class="modal modal-fixed-footer"
		style="width: 540px !important; height: 250px !important">
		<div
			style="padding-left: 15%; padding-right: 15%; text-align: center;">
			<div class="row">
				<i class="medium mdi-action-info text-teal"></i>
				<e:msg key="sureToDeleteNewsItem"></e:msg>
			</div>
			<div>
				<button onclick="deleteNewsItem()"
					class="btn waves-effect waves-light"
					onmouseout="this.style.backgroundColor='#2196F3';"
					onmouseover="this.style.backgroundColor='red';" type="button">
					<e:msg key="delete"></e:msg>
					<i class="mdi-action-delete"></i>
				</button>

				<button onclick="$('#deleteNewsItemModal').closeModal();"
					class="btn waves-effect waves-light"
					onmouseout="this.style.backgroundColor='#2196F3';"
					onmouseover="this.style.backgroundColor='#FFEB3B';" type="button">
					<e:msg key="cancel"></e:msg>
					<i class="mdi-navigation-cancel"></i>
				</button>

			</div>
		</div>
	</div>


</body>
</html>