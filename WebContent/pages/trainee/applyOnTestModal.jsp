<%@ taglib prefix="e" uri="http://epam.edu/tags"%>
<div id="applyOnTestModal" class="modal modal-fixed-footer" style="max-height: 100% !important;">
	<div class="modal-content">
		<div class="row" >
			<div class="input-field col s12  grid-example">
				<h5 id="title_info">
					<i class="large mdi-hardware-laptop-mac " style="padding-right: 30px;float:left;" ></i><p id="info"><e:msg key="chooseDir"></e:msg></p>
				</h5>
			</div>
		</div>
				<div class="row" id="row_directions">
					<div class="col s12">
						
						<ul class="tabs" id="tabs_directions">
							  <li class="tab col s3"><a href="#test1"></a></li>
						</ul>
						 <div id="test1" class="col s12"></div>
					</div>
					 
					
				</div>
					<div id="applied_test_event" style="display:none;">
						<div style="padding-left:40px;font-family: Copperplate, 'Copperplate Gothic Light', fantasy;"><e:msg key="youRegisrered"></e:msg></div>
						<div id="appliedEvent">
							
						</div>
					</div>
			</div>
			
	<div class="modal-footer" id="modal-footer">
		<a onclick="send()"
			class="modal-action modal-close waves-effect waves-green btn-flat "><e:msg key="sendAplication"></e:msg></a>
	</div>
</div>