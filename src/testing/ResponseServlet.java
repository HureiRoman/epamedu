package testing;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ResponseServlet
 */

public class ResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public ResponseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("HELLOW");
		PrintWriter out = response.getWriter();
String login=request.getParameter("login");
String password=request.getParameter("password");
out.write("<!DOCTYPE html> <html> <head> <meta charset=\"utf-8\"> </head> <body style=\"margin:0px; padding:20px;\"> <p>Отримані дані:E-mail: "+login+" Пароль:"+password+"</p> </body> </html>");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("HELLO");
	}

}
