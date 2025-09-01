package WebSocket;

import ModeloDAO.UsuarioDAO;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/garita")
public class WebSocketServer {

    private static final Logger LOGGER = Logger.getLogger(WebSocketServer.class.getName());
    private static SerialPort arduinoPort;
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    static {
        // Inicializar conexión Arduino al cargar la clase
        initializeArduino();
    }

    private static void initializeArduino() {
        try {
            // Buscar puerto COM disponible (ajusta según tu sistema)
            SerialPort[] availablePorts = SerialPort.getCommPorts();
            System.out.println("Puertos disponibles:");
            for (SerialPort port : availablePorts) {
                System.out.println("- " + port.getSystemPortName());
            }

            // Intentar conectar (ajusta COM3 según tu configuración)
            arduinoPort = SerialPort.getCommPort("COM5");
            arduinoPort.setComPortParameters(9600, 8, 1, 0);
            arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

            if (arduinoPort.openPort()) {
                System.out.println("✓ Conectado al Arduino en " + arduinoPort.getSystemPortName());
            } else {
                System.out.println("✗ No se pudo abrir el puerto COM5");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al inicializar Arduino: " + e.getMessage(), e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("✓ Nueva conexión WebSocket: " + session.getId());
        System.out.println("Conexiones activas: " + sessions.size());

        try {
            // Enviar mensaje fijo de confirmación (sin tocar Arduino todavía)
            session.getBasicRemote().sendText("Conectado al servidor WebSocket (prueba sin Arduino)");

            // Si quieres, luego informar estado de Arduino en otra parte:
            if (arduinoPort != null && arduinoPort.isOpen()) {
                session.getBasicRemote().sendText("Arduino disponible en " + arduinoPort.getSystemPortName());
            } else {
                session.getBasicRemote().sendText("Arduino NO disponible");
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error enviando mensaje de bienvenida", e);
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("📥 Mensaje recibido del cliente: " + message);
        String commandToArduino = null;
        try {
            int idUsuario = Integer.parseInt(message.trim()); // el QR trae el id_usuario

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            boolean puedeAbrir = usuarioDAO.puedeAbrirTalanquera(idUsuario);

            if (puedeAbrir) {
                commandToArduino = "entrada";  // abrir garita si estaba fuera
            } else {
                commandToArduino = "salida";   // abrir garita si estaba dentro
            }

            sendToArduino(commandToArduino); // 🔹 enviar al Arduino
            session.getBasicRemote().sendText("Comando enviado: " + commandToArduino);

        } catch (NumberFormatException e) {
            System.out.println("⚠️ El mensaje no es un ID válido: " + message);
            session.getBasicRemote().sendText("ID inválido");
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        sessions.remove(session);
        System.out.println("✗ Conexión cerrada: " + session.getId()
                + " - Razón: " + closeReason.getReasonPhrase());
        System.out.println("Conexiones activas: " + sessions.size());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("❌ Error en WebSocket para sesión: " + session.getId());
        LOGGER.log(Level.SEVERE, "Error en WebSocket", throwable);
        sessions.remove(session);
    }

    private void sendToArduino(String command) {
        if (arduinoPort != null && arduinoPort.isOpen()) {
            try {
                String fullCommand = command + "\n";
                arduinoPort.getOutputStream().write(fullCommand.getBytes());
                arduinoPort.getOutputStream().flush();
                System.out.println("🔌 Comando enviado al Arduino: " + command);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error enviando comando al Arduino: " + e.getMessage(), e);
            }
        } else {
            System.out.println("⚠️ Arduino no disponible para comando: " + command);
        }
    }

    // Método para broadcast (enviar a todas las sesiones)
    public static void broadcast(String message) {
        synchronized (sessions) {
            for (Session session : sessions) {
                try {
                    if (session.isOpen()) {
                        session.getBasicRemote().sendText(message);
                    }
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Error enviando broadcast", e);
                }
            }
        }
    }

    // Método para cerrar recursos al apagar
    public static void cleanup() {
        if (arduinoPort != null && arduinoPort.isOpen()) {
            arduinoPort.closePort();
            System.out.println("🔌 Puerto Arduino cerrado");
        }
    }
}
