package edu.epam.im;

import java.io.StringReader;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import edu.epam.model.Message;

public class ImMessageDecoder implements Decoder.Text<Message> {
	@Override
	public void init(final EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public Message decode(final String textMessage) throws DecodeException {
		Message chatMessage = new Message();
		JsonObject obj = Json.createReader(new StringReader(textMessage))
				.readObject();
		chatMessage.setText(obj.getString("message"));
		chatMessage.setSender(obj.getInt("sender"));
		chatMessage.setReceiver(obj.getInt("receiver"));
		chatMessage.setMessageTime(new Date());
		return chatMessage;
	}

	@Override
	public boolean willDecode(final String s) {
		return true;
	}
}
