package org.systemsbiology.mail;

/**
 *
 * User: attilacsordas
 * Date: 14/05/2013
 * Time: 14:19
 */
import com.lordjoe.utilities.*;
//
//import javax.mail.*;
//import javax.mail.internet.*;
//import java.util.*;
//
//public class SendMailTLS {
//
//
//    private static String g_username;
//
//    public static String getUsername() {
//        return g_username;
//    }
//
//    public static void setUsername(String username) {
//        g_username = username;
//    }
//
//
//
//    private static String g_encrypted_password;
//    public static String getEncrypted_password() {
//        return g_encrypted_password;
//    }
//
//    public static void setEncrypted_password(String encrypted_password) {
//        g_encrypted_password = encrypted_password;
//    }
//
//	public static void sendMail(String recipient, String subjectline, String messagebody) {
//
//		final String username = getUsername();
//		final String password = Encrypt.decryptString(getEncrypted_password());
//
//		Properties props = new Properties();
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", "smtp.gmail.com");
//		props.put("mail.smtp.port", "587");
//
//		Session session = Session.getInstance(props,
//		  new javax.mail.Authenticator() {
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(username, password);
//			}
//		  });
//
//		try {
//
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress("ebihadoop@gmail.com"));
//			message.setRecipients(Message.RecipientType.TO,
//				InternetAddress.parse(recipient));
//			//message.setSubject("Testing Subject");
//            message.setSubject(subjectline);
//			//message.setText("Dear Mail Crawler,"
//			//	+ "\n\n No spam to my email, please!");
//
//            message.setText(messagebody);
//
//			Transport.send(message);
//
//			System.out.println("Done");
//
//		} catch (MessagingException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//    public static void main(String[] args) {
//
//        setUsername("example@gmail.com");
//        setEncrypted_password("example");
//        String recipient = "example@gmail.com, example2@gmail.com";
//        String subjectline = "Hello Subject";
//        String messagebody = "Hello Message Buddy!";
//
//        sendMail(recipient, subjectline, messagebody);
//
//    }
//}
