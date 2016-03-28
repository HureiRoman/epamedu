<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
					<div id="editTaskModal" class="modal modal-fixed-footer">
						
								    <div class="row" style="padding:30px;">
									     <div class="input-field col s8">
									          <i class="mdi-action-note-add prefix"></i>
									          <input  type="text" class="validate" maxlength="40" id="task_title">
									          <label for="task_title" id="task_title_label"><e:msg key="taskTitle"></e:msg></label>
									        </div>
									        
									         <div class="input-field col s4">
										          <input required id="datetimepicker4" type="text" readonly="readonly">
										          <label for="datetimepicker4" id="datetimepicker_label"><e:msg key="deadline"></e:msg></label>
										          <script type="text/javascript">
										            $('#datetimepicker4').datetimepicker({
										              timepicker:true,
										              minDate:'0',
										              step: 15,
										            });
										          </script>
										      </div>
										   
									        <div class="input-field col s12">
									          <textarea class="materialize-textarea" maxlength="600" style="max-height:75px;"id="task_content2"></textarea>
									          <label for="task_content2" id="task_content_label"><e:msg key="TASK_BODY"></e:msg></label>
									        </div>
								    </div>
								    	 <div class="modal-footer">
      <a  onclick="$('#editTaskModal').closeModal()" href="#!" class="modal-action waves-effect waves-red btn-flat "><e:msg key="cancel"></e:msg></a> 
      <a  onclick="saveTaskEdition()" class=" modal-action waves-effect waves-green btn-flat"><e:msg key="save"></e:msg></a>
    </div> 
					      		</div>