(function($) {

	// Number of seconds in every time division
	minutes = 60;

	// Creating the plugin
	$.fn.countdown = function(prop) {

		var options = $.extend({
			callback : function() {
			},
			timestamp : 0
		}, prop);

		var left, m, s, positions;

		// Initialize the plugin
		init(this, options);

		positions = this.find('.position');

		(function tick() {

			// Time left
			left = Math.floor((options.timestamp - (new Date())) / 1000);

			if (left < 0) {
				left = 0;
			}

			// Number of minutes left
			m = Math.floor(left / minutes);
			updateDuo(0, 1, m);
			left -= m * minutes;
			s = left;
			updateDuo(2, 3, s);

			// Calling an optional user supplied callback
			options.callback(m, s);

			// Scheduling another call of this function in 1s
			setTimeout(tick, 1000);
		})();

		// This function updates two digit positions at once
		function updateDuo(minor, major, value) {
			switchDigit(positions.eq(minor), Math.floor(value / 10) % 10);
			switchDigit(positions.eq(major), value % 10);
		}

		return this;
	};

	function init(elem, options) {
		elem.addClass('countdownHolder');

		// Creating the markup inside the container
		$
				.each(
						[ 'Minutes', 'Seconds' ],
						function(i) {
							$('<span class="count' + this + '">')
									.html(
											'<span class="position">\
					<span class="digit static">0</span>\
				</span>\
				<span class="position">\
					<span class="digit static">0</span>\
				</span>')
									.appendTo(elem);
							if (this != "Seconds") {
								elem.append('<span class="countDiv countDiv'
										+ i + '"></span>');
							}
						});

	}

	// Creates an animated transition between the two numbers
	function switchDigit(position, number) {

		var digit = position.find('.digit')

		if (digit.is(':animated')) {
			return false;
		}

		if (position.data('digit') == number) {
			// We are already showing this number
			return false;
		}

		position.data('digit', number);

		var replacement = $('<span>', {
			'class' : 'digit',
			css : {
				top : '-2.1em',
				opacity : 0
			},
			html : number
		});

		digit.before(replacement).removeClass('static').animate({
			top : '2.5em',
			opacity : 0
		}, 'fast', function() {
			digit.remove();
		})

		replacement.delay(100).animate({
			top : 0,
			opacity : 1
		}, 'fast', function() {
			replacement.addClass('static');
		});
	}
})(jQuery);