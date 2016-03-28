package edu.epam.chat;

import java.util.Date;

import edu.epam.annotations.Column;

public class ChatMessage {
	@Column("text")
	private String message;
	@Column("sender")
	private Integer sender;
	@Column("received")
	private Date received;
	@Column("groupId")
	private Integer groupId;

	public final String getMessage() {
		return message;
	}

	public final void setMessage(final String message) {
		this.message = message;
	}



	public final Date getReceived() {
		return received;
	}

	public final void setReceived(final Date received) {
		this.received = received;
	}
	
	

	
	public Integer getSender() {
		return sender;
	}

	public void setSender(Integer sender) {
		this.sender = sender;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "ChatMessage [message=" + message + ", sender=" + sender
				+ ", received=" + received + "]";
	}
	
}
