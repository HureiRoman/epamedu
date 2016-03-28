package edu.epam.servlet.AjaxComand.teacher;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.Advertisement;
import edu.epam.service.AdvertisementService;
import edu.epam.servlet.AjaxComand.AjaxActionCommand;

@UserPermissions({ RoleType.TEACHER })
public class GetAdvertisementDataCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		String idStr = request.getParameter("id");
		Integer id = null;
		if (idStr != null) {
			id = Integer.parseInt(idStr);
		}

		if (id != null) {
			Advertisement advertisement = AdvertisementService
					.getAdvertisementById(id);

			if (advertisement != null) {
				XStream stream = new XStream(new StaxDriver());
				stream.alias("advertisement", Advertisement.class);
				String xml = stream.toXML(advertisement);
				return xml;
			}
		}

		return "";
	}

}
