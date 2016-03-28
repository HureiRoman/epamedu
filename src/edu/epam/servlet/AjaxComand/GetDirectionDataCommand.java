package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.Direction;
import edu.epam.service.DirectionService;
@UserPermissions({RoleType.ADMIN})
public class GetDirectionDataCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {

        String idStr  = request.getParameter("id");
        Integer id = null;
        if(idStr!=null){
        	id = Integer.parseInt(idStr);
        }
		
		if(id!=null){
			Direction direction = DirectionService.getDirectionById(id);
			
			if(direction!=null){
				XStream stream = new  XStream(new StaxDriver());
				stream.alias("direction", Direction.class);
				String xml = stream.toXML(direction);
				return xml;
			}	
		}
		
		return "";
	}

}
