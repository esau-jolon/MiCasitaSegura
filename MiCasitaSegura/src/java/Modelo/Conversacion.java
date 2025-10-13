package Modelo;

import java.sql.Timestamp;

public class Conversacion {

    private int idConversacion;
    private int idResidente;
    private int idAgente;
    private Timestamp fechaCreacion;
    private String estado;

    // 🔹 Auditoría
    private Integer creadoPor;
    private Integer modificadoPor;
    private Timestamp fechaModificacion;

    // 🔹 Nuevos campos (nombres de residente y agente)
    private String nombreResidente;
    private String apellidoResidente;
    private String nombreAgente;
    private String apellidoAgente;

    // 🔹 Constructores
    public Conversacion() {
    }

    public Conversacion(int idResidente, int idAgente, String estado) {
        this.idResidente = idResidente;
        this.idAgente = idAgente;
        this.estado = estado;
    }

    // 🔹 Getters y Setters principales
    public int getIdConversacion() {
        return idConversacion;
    }

    public void setIdConversacion(int idConversacion) {
        this.idConversacion = idConversacion;
    }

    public int getIdResidente() {
        return idResidente;
    }

    public void setIdResidente(int idResidente) {
        this.idResidente = idResidente;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Integer creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Integer getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(Integer modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    // 🔹 Nuevos getters y setters para nombres
    public String getNombreResidente() {
        return nombreResidente;
    }

    public void setNombreResidente(String nombreResidente) {
        this.nombreResidente = nombreResidente;
    }

    public String getApellidoResidente() {
        return apellidoResidente;
    }

    public void setApellidoResidente(String apellidoResidente) {
        this.apellidoResidente = apellidoResidente;
    }

    public String getNombreAgente() {
        return nombreAgente;
    }

    public void setNombreAgente(String nombreAgente) {
        this.nombreAgente = nombreAgente;
    }

    public String getApellidoAgente() {
        return apellidoAgente;
    }

    public void setApellidoAgente(String apellidoAgente) {
        this.apellidoAgente = apellidoAgente;
    }

    // 🔹 Métodos auxiliares (útiles para mostrar en JSP)
    public String getNombreCompletoResidente() {
        String nombre = (nombreResidente != null ? nombreResidente : "");
        String apellido = (apellidoResidente != null ? apellidoResidente : "");
        return (nombre + " " + apellido).trim();
    }

    public String getNombreCompletoAgente() {
        String nombre = (nombreAgente != null ? nombreAgente : "");
        String apellido = (apellidoAgente != null ? apellidoAgente : "");
        return (nombre + " " + apellido).trim();
    }
}
