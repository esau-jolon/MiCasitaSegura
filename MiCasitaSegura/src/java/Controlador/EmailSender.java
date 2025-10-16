package Controlador;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;
import java.util.Properties;

public class EmailSender {

    public static void enviarConAdjunto(String destinatario, String asunto, String mensajeHTML, byte[] qrBytes) {
        final String remitente = "ejolont@miumg.edu.gt";
        final String clave = "vtpkyyucvkealjri"; // contraseña de aplicación Gmail

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.mime.encodefilename", "true");
        props.put("mail.mime.charset", "UTF-8");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente, "Mi Casita Segura", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto, "UTF-8");

            // Parte principal HTML
            MimeBodyPart cuerpo = new MimeBodyPart();
            cuerpo.setContent(mensajeHTML, "text/html; charset=UTF-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(cuerpo);

            // Adjuntar QR si existe
            if (qrBytes != null && qrBytes.length > 0) {
                MimeBodyPart adjunto = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(qrBytes, "image/png");
                adjunto.setDataHandler(new DataHandler(source));
                adjunto.setFileName(MimeUtility.encodeText("codigo_qr.png", "UTF-8", null));
                multipart.addBodyPart(adjunto);
            }

            message.setContent(multipart);
            message.saveChanges(); // 🔹 fuerza encabezados MIME correctos

            // 🚀 Enviar
            Transport.send(message);
            System.out.println("✅ Correo enviado correctamente a " + destinatario);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error al enviar correo: " + e.getMessage());
        }
    }
}
