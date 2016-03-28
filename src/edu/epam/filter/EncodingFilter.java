package edu.epam.filter;

import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import edu.epam.sms.SMSCSender;

public class EncodingFilter implements Filter {
	private String encoding;
	private FilterConfig filterConfig;
	private static SMSCSender smsSender;
	public EncodingFilter() {
	}

	public void init(FilterConfig filterconfig) throws ServletException {
		smsSender = new SMSCSender("slim25", "a133e6cd12c1a0899f275bc6058a0ce9", "utf-8", true);
		
		filterConfig = filterconfig;
		encoding = filterConfig.getInitParameter("encoding");
	}
	
	public static void sendMessage(String phoneNumber, String message){
		smsSender.sendSms(phoneNumber, message, 1, "", "", 0, "", "");
		smsSender.getSmsCost(phoneNumber, "Ви успішно зареєстровані", 0, 0, "", "");
		smsSender.getBalance();
	}

	public void doFilter(ServletRequest request,
			ServletResponse response, FilterChain filterchain)
			throws IOException, ServletException {
		
		 System.out.println("YOUR INET ADDRESSS +++++====="+InetAddress.getLocalHost());
		
		request.setCharacterEncoding(encoding);
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		filterchain.doFilter(request, response);
	}

	public void destroy() {
	}

}