package com.ce.ems.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JMSUtil {
	
	private static String HOST;
	private static String SENDER_EMAIL;
	private static String SENDER_PASSWORD;
	
	private Session session;
	private String email;
	private String subject;
	private String message;

    public JMSUtil(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }
    
    public void send(){
    	
    	//Creating properties
        Properties props = new Properties();

        //Configuring properties for mail
        props.put("mail.smtp.host", getHost());
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(getSenderEmail(), getSenderPassword());
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(getSenderEmail()));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(subject);
            
            
            //Adding message
            mm.setText(message);
            

            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

	public static String getSenderEmail() {
		return SENDER_EMAIL;
	}

	public static void setSenderEmail(String sENDER_EMAIL) {
		SENDER_EMAIL = sENDER_EMAIL;
	}

	public static String getSenderPassword() {
		return SENDER_PASSWORD;
	}

	public static void setSenderPassword(String sENDER_PASSWORD) {
		SENDER_PASSWORD = sENDER_PASSWORD;
	}

	public static String getHost() {
		return HOST;
	}

	public static void setHost(String hOST) {
		HOST = hOST;
	}
  
}
