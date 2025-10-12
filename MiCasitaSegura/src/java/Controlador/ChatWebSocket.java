package Controlador;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ChatWebSocket
 */
@ServerEndpoint("/chatSocket")
public class ChatWebSocket {

    // 🔹 Mapa para almacenar las sesiones activas: <idUsuario, session>
    private static final Map<String, Session> sesiones = new ConcurrentHashMap<>();

    /**
     * Se ejecuta cuando un usuario se conecta al WebSocket
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        // Esperamos que el cliente se conecte con: ws://.../chatSocket?userId=5
        String query = session.getQueryString();

        if (query != null && query.startsWith("userId=")) {
            String userId = query.split("=")[1];
            sesiones.put(userId, session);

            System.out.println("🔵 Usuario conectado al WebSocket: ID " + userId);
            session.getBasicRemote().sendText("{\"system\":\"Conexión establecida\"}");
        } else {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Falta parámetro userId"));
        }
    }

    /**
     * Se ejecuta cuando llega un mensaje desde el cliente
     */
    @OnMessage
    public void onMessage(Session session, String mensaje) throws IOException {
        System.out.println("📩 Mensaje recibido: " + mensaje);

        // Si se recibe un ping del cliente → responder con pong
        if ("ping".equalsIgnoreCase(mensaje)) {
            session.getBasicRemote().sendText("pong");
            return;
        }

        // Los mensajes deberían venir en formato JSON:
        // { "from":"1", "to":"2", "text":"Hola" }

        // Reenviamos el mensaje solo al destinatario indicado
        if (mensaje.contains("\"to\"")) {
            try {
                // Extraer "to" manualmente (sin usar librería JSON para mantener compatibilidad)
                String idDestino = mensaje.split("\"to\":\"")[1].split("\"")[0];
                Session sesionDestino = sesiones.get(idDestino);

                if (sesionDestino != null && sesionDestino.isOpen()) {
                    sesionDestino.getBasicRemote().sendText(mensaje);
                } else {
                    System.out.println("⚠️ Usuario destino no conectado: ID " + idDestino);
                }
            } catch (Exception e) {
                System.err.println("Error procesando mensaje WebSocket: " + e.getMessage());
            }
        }
    }

    /**
     * Se ejecuta cuando el usuario se desconecta
     */
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        sesiones.values().remove(session);
        System.out.println("🔴 Usuario desconectado del WebSocket: " + reason);
    }

    /**
     * Maneja errores dentro del WebSocket
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("❌ Error en WebSocket: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    /**
     * 🔹 Método auxiliar estático para enviar mensajes desde los Servlets
     * (usado por ControladorMensaje → cuando se guarda un mensaje en BD)
     */
    public static void enviarMensajeEnTiempoReal(String idDestino, String mensaje) {
        try {
            Session sesionDestino = sesiones.get(idDestino);
            if (sesionDestino != null && sesionDestino.isOpen()) {
                sesionDestino.getBasicRemote().sendText(mensaje);
                System.out.println("📤 Mensaje enviado en tiempo real a ID " + idDestino);
            } else {
                System.out.println("⚠️ Usuario no conectado (ID " + idDestino + ")");
            }
        } catch (IOException e) {
            System.err.println("❌ Error al enviar mensaje en tiempo real: " + e.getMessage());
        }
    }
}
