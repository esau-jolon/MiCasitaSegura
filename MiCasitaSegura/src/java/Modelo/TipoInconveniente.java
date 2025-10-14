package Modelo;

import java.sql.Timestamp;

public class TipoInconveniente {

    private int idTipoInconveniente;
    private String nombre;
    private Boolean estado;

    // Auditoría
    private Integer creadoPor;
    private Integer modificadoPor;
    private Timestamp fechaCreacion;
    private Timestamp fechaModificacion;

    // 🔹 Constructores
    public TipoInconveniente() {
    }

    public TipoInconveniente(String nombre) {
        this.nombre = nombre;
    }

    public TipoInconveniente(int idTipoInconveniente, String nombre, Boolean estado) {
        this.idTipoInconveniente = idTipoInconveniente;
        this.nombre = nombre;
        this.estado = estado;
    }

    // 🔹 Getters y Setters
    public int getIdTipoInconveniente() {
        return idTipoInconveniente;
    }

    public void setIdTipoInconveniente(int idTipoInconveniente) {
        this.idTipoInconveniente = idTipoInconveniente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
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

    @Override
    public String toString() {
        return nombre;
    }
}
