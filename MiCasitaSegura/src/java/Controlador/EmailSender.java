/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

/**
 *
 * @author esauj
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author esauj
 */
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;

import java.util.Properties;

public class EmailSender {

    public static void enviarConAdjunto(String destinatario, String asunto, String mensaje, byte[] qrBytes) {
        final String remitente = "ejolont@miumg.edu.gt";
        final String clave = "vtpkyyucvkealjri"; // Usa contraseña de aplicación si es Gmail

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            // Parte de texto
            MimeBodyPart texto = new MimeBodyPart();
            texto.setText(mensaje);

            // Parte de adjunto (QR)
            MimeBodyPart adjunto = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(qrBytes, "image/png");
            adjunto.setDataHandler(new DataHandler(source));
            adjunto.setFileName("codigo_qr.png");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            multipart.addBodyPart(adjunto);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Correo enviado a " + destinatario);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
