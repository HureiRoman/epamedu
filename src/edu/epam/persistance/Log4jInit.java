package edu.epam.persistance;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;

public class Log4jInit implements ServletContextListener {

	public void contextInitialized(final ServletContextEvent event) {
		final ServletContext servletContext = event.getServletContext();
		final String log4jConfigLocation = servletContext.getInitParameter("log4jConfigLocation");
		final String log4jFilename = servletContext.getRealPath(log4jConfigLocation);
		final DOMConfigurator configurator = new DOMConfigurator();
		configurator.doConfigure(log4jFilename, LogManager.getLoggerRepository());
		System.out.println("LOG4J initialized");
	}

	public void contextDestroyed(final ServletContextEvent event) {
		// nothing
	}

}