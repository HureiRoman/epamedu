$(document).ready(function() {
$.i18n.init({
		cookieName : 'language',
		cookieDomain: 'localhost',
		useCookie : true,
		ns: 'translation',
		 dynamicLoad: false,
		fallbackLng : 'en',
		resGetPath : '/EpamEducationalProject/resources/locales/__lng__/__ns__.json',
		debug: true
	});
});
