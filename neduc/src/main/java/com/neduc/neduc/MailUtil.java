package com.neduc.neduc;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MailUtil {

    public static void envoyerEmail(String destinataire, String sujet, String messageTexte) throws MessagingException {
        final String expediteur = "tonemail@gmail.com"; // change
        final String motDePasse = "mot_de_passe_app"; // change : mot de passe d'application Gmail

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(expediteur, motDePasse);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(expediteur));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
        message.setSubject(sujet);
        message.setText(messageTexte);

        Transport.send(message);
    }
}
