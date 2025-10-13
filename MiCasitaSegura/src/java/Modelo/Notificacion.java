package Modelo;

import java.sql.Timestamp;

public class Notificacion {

    private int idNotificacion;
    private int idIncidente;
    private int idGuardia;
    private String asunto;
    private String cuerpo;
    private Timestamp fechaEnvio;

    // Auditoría
    private Integer creadoPor;
    private Integer modificadoPor;
    private Timestamp fechaCreacion;
    private Timestamp fechaModificacion;

    // 🔹 Campos auxiliares
    private String nombreGuardia;
    private String nombreIncidente;

    public Notificacion() {}

    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public int getIdIncidente() {
        return idIncidente;
    }

    public void setIdIncidente(int idIncidente) {
        this.idIncidente = idIncidente;
    }

    public int getIdGuardia() {
        return idGuardia;
    }

    public void setIdGuardia(int idGuardia) {
        this.idGuardia = idGuardia;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Timestamp getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Timestamp fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
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

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getNombreGuardia() {
        return nombreGuardia;
    }

    public void setNombreGuardia(String nombreGuardia) {
        this.nombreGuardia = nombreGuardia;
    }

    public String getNombreIncidente() {
        return nombreIncidente;
    }

    public void setNombreIncidente(String nombreIncidente) {
        this.nombreIncidente = nombreIncidente;
    }
}
