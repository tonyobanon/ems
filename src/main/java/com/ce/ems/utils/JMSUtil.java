package com.ce.ems.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ce.ems.base.classes.MailCredentialSpec;
import com.ce.ems.base.core.Exceptions;

public class JMSUtil {
	
	private static MailCredentialSpec credentials;
	
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
        props.put("mail.smtp.host", credentials.getProviderUrl());
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(credentials.getUsername(), credentials.getPassword());
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(credentials.getUsername()));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(subject);        
            
            //Adding message
            mm.setText(message);

            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            Exceptions.throwRuntime(e);
        }
    }

	public static MailCredentialSpec getCredentials() {
		return credentials;
	}

	public static void setCredentials(MailCredentialSpec credentials) {
		JMSUtil.credentials = credentials;
	}
  
}
