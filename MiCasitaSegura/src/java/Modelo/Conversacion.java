package Modelo;

import java.sql.Timestamp;

public class Conversacion {

    private int idConversacion;
    private int idResidente;
    private int idAgente;
    private Timestamp fechaCreacion;
    private String estado;

    // 🔹 Auditoría (si deseas usarla en el futuro)
    private Integer creadoPor;
    private Integer modificadoPor;
    private Timestamp fechaModificacion;

    public Conversacion() {
    }

    public Conversacion(int idResidente, int idAgente, String estado) {
        this.idResidente = idResidente;
        this.idAgente = idAgente;
        this.estado = estado;
    }

    // Getters y Setters
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
}
