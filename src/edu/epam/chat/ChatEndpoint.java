package edu.epam.chat;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

import edu.epam.service.ChatMessageService;


@ServerEndpoint(value = "/chat/{group}", encoders = ChatMessageEncoder.class, decoders = ChatMessageDecoder.class)
public class ChatEndpoint {
	private final Logger log = Logger.getLogger(getClass().getName());

	@OnOpen
	public void open(final Session session, @PathParam("group") final String group) {
		log.info("session openend and bound to group: " + group);
		session.getUserProperties().put("group", group);
	}

	@OnMessage
	public void onMessage(final Session session, final ChatMessage chatMessage) {
		ChatMessageService.addChatMessageToDB(chatMessage);
		String group = (String) session.getUserProperties().get("group");
		try {
			for (Session s : session.getOpenSessions()) {
				if (s.isOpen()
						&& group.equals(s.getUserProperties().get("group"))) {
					s.getBasicRemote().sendObject(chatMessage);
				}
			}
		} catch (IOException | EncodeException e) {
			e.printStackTrace();
		}
	}
}
