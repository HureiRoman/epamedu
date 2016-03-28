<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
					<div id="addNewStudentTaskModal" class="modal modal-fixed-footer row">
						
								    <form class="col s10">
									     <div class="input-field col s6">
									          <i class="mdi-action-note-add prefix"></i>
									          <input  type="text" class="validate" maxlength="40" id="add_task_title">
									          <label for="add_task_title"><e:msg key="hwTitle"></e:msg></label>
									        </div>
									        
									         <div class="input-field col s4">
										          <input required id="datetimepicker_add_task" type="text" readonly="readonly">
										          <label for="datetimepicker_add_task"><e:msg key="deadline"></e:msg></label>
										          <script type="text/javascript">
										            $('#datetimepicker_add_task').datetimepicker({
										              timepicker:true,
										              minDate:'0',
										              step: 15,
										            });
										          </script>
										      </div>
										      <div class="offset-s1 col s1" style="padding-top: 5%;" >
									       		 <button onclick="closeAddNewStudentTask()" class="btn waves-effect waves-light" onmouseout="this.style.backgroundColor='#2196F3';" onmouseover="this.style.backgroundColor='#FFEB3B';" type="button"  >
													   <e:msg key="CANCEL"></e:msg><i class="mdi-content-backspace"></i>
												 </button>
									       			
									       	  </div>
									        
									        <div class="input-field col s10">
									          <textarea class="materialize-textarea" maxlength="600"  style="max-height:75px;" id="add_task_content"></textarea>
									          <label for="add_task_content"><e:msg key="TASK_BODY"></e:msg></label>
									        </div>
									        
									        <div class="offset-s1 col s1"  >
									       		 <button style="margin-top: 20px;" onclick="addNewStudentTask(${myGroup.id})" class="btn waves-effect waves-light" onmouseout="this.style.backgroundColor='#2196F3';" onmouseover="this.style.backgroundColor='#FFEB3B';" type="button"  >
													   <e:msg key="SUBMIT"></e:msg><i class="mdi-action-note-add"></i>
												 </button>
									       			
									       	</div>
								    </form>
					      		</div>