package edu.epam.tag;

import java.io.IOException;
import java.util.MissingResourceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.epam.constants.Constants;
import edu.epam.exceptions.NoDBConnectionsLongTime;
import edu.epam.persistance.BundleManager;

public class Internationalizer extends SimpleTagSupport {
	private String key;
	@Override
	public void doTag() throws JspException, IOException {
	try{
		JspContext jspContext = getJspContext();
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		HttpSession session = request.getSession();
		JspWriter writer = jspContext.getOut();
		String language = (String) session.getAttribute(Constants.SESSION_PARAM_NAME_LOCALE);

		String resultString = BundleManager.getBundle(language).getString(key);
		if(resultString==null){
			writer.write("?-NONE-?");
			System.out.println("NO RESOURCE BUNDLE");
		}else{
			writer.write(resultString);
		}
	}catch(NoDBConnectionsLongTime ex){
		ex.printStackTrace();
		System.out.println("No Db connection long time");
	}
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
