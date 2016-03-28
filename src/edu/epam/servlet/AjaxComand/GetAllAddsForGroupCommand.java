package edu.epam.servlet.AjaxComand;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.epam.annotations.UserPermissions;
import edu.epam.constants.RoleType;
import edu.epam.model.Advertisement;
import edu.epam.persistance.TimeToText;
import edu.epam.service.AdvertisementService;

@UserPermissions({ RoleType.STUDENT, RoleType.TEACHER })
public class GetAllAddsForGroupCommand implements AjaxActionCommand {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, Locale locale)
			throws ServletException, IOException, Exception {
		Integer groupId = Integer.parseInt(request.getParameter("groupId"));

		JsonObjectBuilder responseJsonBuilder = Json.createObjectBuilder();
		JsonArrayBuilder adsJsonArrayBuilder = Json.createArrayBuilder();

		List<Advertisement> advertisements = AdvertisementService
				.getListOfAdvertisementForGroup(groupId);

		for (Advertisement advertisement : advertisements) {
			JsonObjectBuilder adsJsonBuilder = Json.createObjectBuilder();
			adsJsonBuilder.add("title", advertisement.getTitle());
			adsJsonBuilder.add("content", advertisement.getContent());
			adsJsonBuilder.add("date", TimeToText.timeToText(advertisement.getAdvertisementDate()));
			JsonObject advJsonObject = adsJsonBuilder.build();
		    adsJsonArrayBuilder.add(advJsonObject);
		}
		
        responseJsonBuilder.add("advertisements", adsJsonArrayBuilder.build());		

		return responseJsonBuilder.build().toString();
	}

}
