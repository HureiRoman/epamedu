package edu.epam.persistance;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BundleManager implements ServletContextListener {
	private static Map<String,ResourceBundle> bundles;
	
	public static void init(){
		bundles = new HashMap<String, ResourceBundle>();
		bundles.put("en", ResourceBundle.getBundle("/translations/messages", new Locale("en"), new UTF8Control()));
		bundles.put("uk", ResourceBundle.getBundle("/translations/messages", new Locale("uk"), new UTF8Control()));
	}
	public static ResourceBundle getBundle(String locale){
		System.out.println("get bundle for "+locale);
		return bundles.get(locale);
	}
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	 // nothing
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("bundle inititalized");
		init();		
	}

}
