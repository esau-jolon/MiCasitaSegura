package Modelo;

import java.sql.Timestamp;

public class Incidente {

    private int idIncidente;
    private int idResidente;
    private int idTipoIncidente;
    private Timestamp fechaHoraIncidente;
    private String descripcion;

    // Auditoría
    private Integer creadoPor;
    private Integer modificadoPor;
    private Timestamp fechaCreacion;
    private Timestamp fechaModificacion;

    // Estado lógico
    private boolean activo;

    // Campos auxiliares (para vistas)
    private String nombreTipo;
    private String nombreResidente;
    private String apellidoResidente;
    private String nombreTipoIncidente;

    public Incidente() {
    }

    public int getIdIncidente() {
        return idIncidente;
    }

    public void setIdIncidente(int idIncidente) {
        this.idIncidente = idIncidente;
    }

    public int getIdResidente() {
        return idResidente;
    }

    public void setIdResidente(int idResidente) {
        this.idResidente = idResidente;
    }

    public int getIdTipoIncidente() {
        return idTipoIncidente;
    }

    public void setIdTipoIncidente(int idTipoIncidente) {
        this.idTipoIncidente = idTipoIncidente;
    }

    public Timestamp getFechaHoraIncidente() {
        return fechaHoraIncidente;
    }

    public void setFechaHoraIncidente(Timestamp fechaHoraIncidente) {
        this.fechaHoraIncidente = fechaHoraIncidente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

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

    public String getNombreTipoIncidente() {
        return nombreTipoIncidente;
    }

    public void setNombreTipoIncidente(String nombreTipoIncidente) {
        this.nombreTipoIncidente = nombreTipoIncidente;
    }
}
