
$('.fa-search').click(function(){
 $(this).stop().toggleClass('fa-close');
 $('.search').stop().animate({height: "toggle", opacity: "toggle"}, 300);
});

 $('.chat').draggable({ handle: 'header', containment: 'window'  });
 
 $('.fa-plus-square').click(function(){
	 $(this).toggleClass('fa-plus-square');
	 $(this).toggleClass('fa-minus-square');
 });
 
