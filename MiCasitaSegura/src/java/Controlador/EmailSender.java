package Controlador;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;
import java.util.Properties;

public class EmailSender {

    public static void enviarConAdjunto(String destinatario, String asunto, String mensaje, byte[] qrBytes) {
        final String remitente = "ejolont@miumg.edu.gt";
        final String clave = "vtpkyyucvkealjri"; // contraseña de aplicación Gmail

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.mime.charset", "UTF-8"); // 🔹 Forzar UTF-8 en toda la sesión

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto, "UTF-8"); // 🔹 Asunto también en UTF-8

            if (qrBytes != null && qrBytes.length > 0) {
                // 🔹 Parte HTML del cuerpo
                MimeBodyPart cuerpoHTML = new MimeBodyPart();
                cuerpoHTML.setContent(mensaje, "text/html; charset=UTF-8");

                // 🔹 Parte adjunta (QR)
                MimeBodyPart adjunto = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(qrBytes, "image/png");
                adjunto.setDataHandler(new DataHandler(source));
                adjunto.setFileName("codigo_qr.png");

                // 🔹 Ensamblar mensaje
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(cuerpoHTML);
                multipart.addBodyPart(adjunto);
                message.setContent(multipart);
            } else {
                // 🔹 Enviar cuerpo como HTML (sin adjunto)
                message.setContent(mensaje, "text/html; charset=UTF-8");
            }

            // 🔹 Reforzar encabezados
            message.setHeader("Content-Transfer-Encoding", "8bit");
            message.setHeader("Content-Type", "text/html; charset=UTF-8");

            // 🔹 Enviar
            Transport.send(message);
            System.out.println("✅ Correo enviado correctamente a " + destinatario);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error al enviar correo a " + destinatario + ": " + e.getMessage());
        }
    }
}
