function countdownStart() {

	var ts = (new Date()).getTime() + 10 * 60 * 1000;

	$('#countdown')
			.countdown(
					{
						timestamp : ts,
						callback : function(minutes, seconds) {
							if(minutes == 0 && seconds == 0) {
								sendData();
							}
						}
					});

};
