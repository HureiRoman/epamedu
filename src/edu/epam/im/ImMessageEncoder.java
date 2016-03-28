package edu.epam.im;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import edu.epam.model.Message;
import edu.epam.persistance.TimeToText;

public class ImMessageEncoder implements Encoder.Text<Message> {
	@Override
	public void init(final EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	@Override	
	public String encode(final Message chatMessage) throws EncodeException {
		return Json.createObjectBuilder()
				.add("message", chatMessage.getText())
				.add("sender", chatMessage.getSender())
				.add("receiver", chatMessage.getReceiver())
				.add("received", TimeToText.timeToText(chatMessage.getMessageTime())).build()
				.toString();
	}
}
