	/**
	 * Mail
	 *
	 * It is used to send an email with the HTML table and in the body
	 * and attach in email .
	 *
	 * @author Elías Alfonso Carrasco Guerrero
	 * @author eliascarrasco1227@gmail.com
	 */

package com.isban.scf.sid.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
//import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mail {
	private static final Logger logger = LogManager.getLogger(Mail.class);
	
	/*
	public void main(String [] args) throws Exception {
		logger.debug(" - Entering sendMail");
        String subject = "Confirm your sign up in SIDManager";
        String body = "Click on the next URL to activate your SIDManager user: ";
        Mail.sendMail("daleaenter00@gmail.com", "urlConfirmation", subject, body);
	}
	*/
	
	/**
	* This method creates the session, it also specify the server properties
	* and set the message and its content (that includes the receivers,
	* the body of the mail...). 
	* Finally the method send the mail.
	 * @param confirmationCode 
	 * @param receiver 
	*
	* @throws Exception  If a method exception occurred
	*/
    public static void sendMail(String receiver,
    		String urlConfirmation,
    		String subject,
    		String body) throws Exception {   
    	logger.debug("Entering Mail");
    	
    	final String sender = SharedProperties.get( "mailSender" ); 
    	
    	final String password = SharedProperties.get( "mailPassword" ); 
    	// Crear sesión de correo
    	Session session = getSession(sender, password);
    	
    	// Get session logging in with server properties
    	//Session session = Session.getInstance(getServerProperties());
       
        try {
            // Create email message
        	Message message = createMessage(session, sender, receiver, urlConfirmation, subject, body);

            // Send email
            Transport.send(message);

            logger.debug("The mail was sent successfully.");

        } catch (MessagingException e) {
        	logger.error("There was an error sending the email: " + e.getMessage());
        	logger.error("Status of the VARIABLES: "
        			+ " | session: " + session
        			+ " | sender: " + sender
        			+ " | receiver: " + receiver
        			+ " | urlConfirmation: " + urlConfirmation
        			+ " | subject: " + subject
        			+ " | body: " + body
        	);
            throw new Exception();
        }
        
        logger.debug("Exiting Mail");
    }

    /**
	* This method define who is the sender and receiver,
	* it also specify the mail subject, body and attachment.
	*
	* @param  session A session for the logged sender in Session format.
	* @param  sender A sender which is going to send the email in string format.
	* @param  receiver A receiver of the email in String format.
	* @param  confirmationCode A code for confirm a user in String format.
	* 
	* @throws AddressException  If an error with the address occurs
	* @throws MessagingException If an error with the message occurs
	* 
	* @return the email message in Message format
	*/
    private static Message createMessage
    	(Session session, String sender, String receiver, String urlConfirmation, String subject, String body)
    	throws AddressException, MessagingException {
    	
    	logger.debug("Entering setMessageAndItsContent");
    	
        Message message = new MimeMessage(session);

        // Define who the sender is
        message.setFrom(new InternetAddress(sender)); 

        message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));

        // Set email subject
        //message.setSubject("Confirm your sign up in SIDManager");
        message.setSubject(subject);
        
        // Email body 
        //String content = "Click on the next URL to activate your SIDManager user: " + urlConfirmation;
        String content = body + urlConfirmation;
        
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(content, "text/html; charset=utf-8");
        
		// Create multipart email, mix the attach to the email body.
        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);
        
        logger.debug("Exiting setMessageAndItsContent");

        return message;
    }



	/**
	* This method get the session of the sender with authentication.
	*
	* @param  sender A sender which is going to send the email in string format.
	* @param  password The password of the sender email.
	* 
    * @throws Exception If an error with the session occurs
    * 
    * @return the session in Session format
	*/
	 private static Session getSession (final String sender, final String password) throws Exception {
		
        // Set mail server settings
        Properties props = getServerProperties();
		
		// Get session logging in with user and password
		Session session = Session.getInstance(props, new Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(sender, password);
	            }
	        });
		return session;
	}
	
	
	/**
	* This method get the properties needed for the session (port, host...).
	* 
	* @return the properties needed for the session (port and host).
	*
	* @exception Exception If an error with the properties occurs
	*/
	private static Properties getServerProperties() throws Exception {
		// Get the port and host
		String host = SharedProperties.get("mailHost");
		String port = SharedProperties.get("mailPort");
		
		// Server properties are specified here
		Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true"); //Si se requiere autenticacion y password
        props.put("mail.smtp.starttls.enable","true");
        
        //props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //props.put("mail.smtp.socketFactory.fallback", "false");
        
		return props;
	}
}

