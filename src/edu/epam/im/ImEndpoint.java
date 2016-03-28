package edu.epam.im;

import java.io.IOException;
import java.sql.SQLException;

import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import edu.epam.model.Message;
import edu.epam.service.PersonalMessageService;

@ServerEndpoint(value = "/im/{user}/{collocutor}", encoders = ImMessageEncoder.class, decoders = ImMessageDecoder.class)
public class ImEndpoint {
	@OnOpen
	public void open(final Session session,
			@PathParam("user") final String user,
			@PathParam("collocutor") final String collocutor) {
		try {
			boolean messagesMarked;
			messagesMarked = PersonalMessageService.markMessagesAsReaded(Integer.parseInt(user),Integer.parseInt(collocutor));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("session opened for user " + user);
		session.getUserProperties().put("user", user);
		session.getUserProperties().put("collocutor", collocutor);
	}

	@OnMessage
	public void onMessage(final Session session, final Message message) {
		String user = (String) session.getUserProperties().get("user");
		String collocutor = (String) session.getUserProperties().get(
				"collocutor");
		PersonalMessageService.addChatMessageToDB(message);
		try {
			for (Session s : session.getOpenSessions()) {
				if (s.isOpen()
						&& (user.equals(s.getUserProperties().get("user")) && collocutor
								.equals(s.getUserProperties().get("collocutor")))
						|| (collocutor.equals(s.getUserProperties().get("user")) && user
								.equals(s.getUserProperties().get("collocutor")))) {
					s.getBasicRemote().sendObject(message);
				}
			}
		} catch (IOException | EncodeException e) {
			e.printStackTrace();
		}
	}
}
