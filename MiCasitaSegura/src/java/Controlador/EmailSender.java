package Controlador;

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
        final String clave = "vtpkyyucvkealjri"; // contraseña de aplicación (Gmail)

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            if (qrBytes != null && qrBytes.length > 0) {
                // Parte de texto
                MimeBodyPart texto = new MimeBodyPart();
                texto.setText(mensaje);

                // Parte de adjunto (QR)
                MimeBodyPart adjunto = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(qrBytes, "image/png");
                adjunto.setDataHandler(new DataHandler(source));
                adjunto.setFileName("codigo_qr.png");

                // Juntar partes
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(texto);
                multipart.addBodyPart(adjunto);

                message.setContent(multipart);
            } else {
                // Solo enviar texto plano
                message.setText(mensaje);
            }

            Transport.send(message);
            System.out.println("Correo enviado a " + destinatario);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
