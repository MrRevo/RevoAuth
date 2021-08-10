package com.revo.auth.recovery;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.bukkit.Bukkit;

import com.revo.auth.data.DataManager;

/*
 * Email sending service 
 * 
 * Created by Revo
 */

public final class EmailSender {

	/*
	 * Send email
	 */

	public static boolean send(String to, String title, String msg) {
		Properties props = new Properties();
		props.put("mail.smtp.host", DataManager.getConfig().getString("mail.smtp.host"));
		props.put("mail.smtp.auth", DataManager.getConfig().getBoolean("mail.smtp.auth"));
		props.put("mail.smtp.port", DataManager.getConfig().getString("mail.smtp.port"));
		props.put("mail.smtp.starttls.enable", DataManager.getConfig().getString("mail.smtp.starttls.enable"));
		try {
			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(DataManager.getConfig().getString("mail.user"), DataManager.getConfig().getString("mail.password"));
				}
			});
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(DataManager.getConfig().getString("mail.user")));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(title);
			message.setText(msg);
			Transport.send(message);
		} catch (Exception e) {
			Bukkit.getLogger().info(e.toString());
			return false;
		}
		return true;
	}

}
