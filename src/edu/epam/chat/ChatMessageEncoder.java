package edu.epam.chat;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import edu.epam.persistance.TimeToText;

public class ChatMessageEncoder implements Encoder.Text<ChatMessage> {
	@Override
	public void init(final EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public String encode(final ChatMessage chatMessage) throws EncodeException {
		return Json.createObjectBuilder()
				.add("message", chatMessage.getMessage())
				.add("sender", chatMessage.getSender())
				.add("groupId",chatMessage.getGroupId())
				.add("received", TimeToText.timeToText(chatMessage.getReceived())).build()
				.toString();
	}
}
