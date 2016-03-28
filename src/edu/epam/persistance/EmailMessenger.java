package edu.epam.persistance;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.epam.role.CommonUser;


public class EmailMessenger {
	public static void sendEmailToGroupOfPeople(String host, String port,String userName, final String password, List<String> toAddress,
                                 String subject, String message) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userName));
        for (int i = 0; i < toAddress.size(); i++) {
            InternetAddress[] toAddresses = {new InternetAddress(toAddress.get(i))};
            msg.addRecipients(Message.RecipientType.CC, toAddresses);
        }
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setContent(message, "text/html;  charset=UTF-8");

        // sends the e-mail
        Thread sendMessageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Transport.send(msg);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });
        sendMessageThread.start();
    }

    public static void sendEmailToGroupOfUsers(String host, String port,String userName, final String password, List<? extends CommonUser> toUsers,
                                                String subject, String message) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userName));
        if (toUsers.size() > 0) {
            for (int i = 0; i < toUsers.size(); i++) {
                InternetAddress[] toAddresses = {new InternetAddress(toUsers.get(i).getEmail())};
                msg.addRecipients(Message.RecipientType.CC, toAddresses);
            }
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setContent(message, "text/html;  charset=UTF-8");

            // sends the e-mail
            Thread sendMessageThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(msg);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendMessageThread.start();
        }
    }


    public static void sendEmail(String host, String port,
                                 final String userName, final String password, String toAddress,
                                 String subject, String message) throws MessagingException {

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setContent(message, "text/html;  charset=UTF-8");

        // sends the e-mail
        Thread sendMessageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Transport.send(msg);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });
        sendMessageThread.start();
    }
}
