<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
						<div id="deleteLessonModal" class="modal modal-fixed-footer">
						
								    <div style="padding-left:15%;padding-right:15%;text-align:center;">
										    <div class="row" >
										    	<i class="medium mdi-action-info text-teal"></i><e:msg key="AreUSureDeleteLesson"></e:msg>
										    </div>
									        <div>
									       		 <button onclick="deleteLesson()" class="btn waves-effect waves-light" onmouseout="this.style.backgroundColor='#2196F3';" onmouseover="this.style.backgroundColor='red';" type="button"  >
													  <e:msg key="index.deleteButton"></e:msg><i class="mdi-action-delete"></i>
												 </button>
									       			
									       		 <button  onclick="$('#deleteLessonModal').closeModal();" class="btn waves-effect waves-light" onmouseout="this.style.backgroundColor='#2196F3';" onmouseover="this.style.backgroundColor='#FFEB3B';" type="button"  >
													   <e:msg key="CANCEL"></e:msg><i class="mdi-navigation-cancel"></i>
												 </button>
									       			
									       	</div>
								    </div>
					      		</div>