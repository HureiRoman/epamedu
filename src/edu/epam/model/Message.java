package edu.epam.model;

import java.util.Date;

import edu.epam.annotations.Column;

public class Message {
	@Column("id")
	private int id;
	@Column("sender")
	private int sender;
	@Column("receiver")
	private int receiver;
//	@RegexCheck(regex) check for script and so on
	@Column("text")
	private String text;
	@Column("isRead")
	private boolean isRead;
	@Column("message_time")
	private Date messageTime; 
	
	public Message() {
		
	}
	
	public Message(int id, int sender, int receiver, String text, boolean isRead, Date messageTime) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		this.isRead = isRead;
		this.messageTime = messageTime;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSender() {
		return sender;
	}
	public void setSender(int sender) {
		this.sender = sender;
	}
	public int getReceiver() {
		return receiver;
	}
	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = Boolean.valueOf(isRead);
	}
	public Date getMessageTime() {
		return messageTime;
	}
	public void setMessageTime(Date messageTime) {
		this.messageTime = messageTime;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", sender=" + sender + ", receiver="
				+ receiver + ", text=" + text + ", isRead=" + isRead
				+ ", messageTime=" + messageTime + "]";
	}

}
