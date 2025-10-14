package Modelo;

import java.sql.Timestamp;

public class ReporteMantenimiento {

    private int idReporte;
    private int idTipoInconveniente;
    private int idResidente;
    private String descripcion;
    private Timestamp fechaHoraIncidente;

    // Campos para mostrar en vistas (joins)
    private String nombreTipoInconveniente;
    private String nombreResidente;
    private String apellidoResidente;

    // Auditoría
    private Integer creadoPor;
    private Integer modificadoPor;
    private Timestamp fechaCreacion;
    private Timestamp fechaModificacion;

    public ReporteMantenimiento() {
    }

    public ReporteMantenimiento(int idTipoInconveniente, int idResidente, String descripcion, Timestamp fechaHoraIncidente) {
        this.idTipoInconveniente = idTipoInconveniente;
        this.idResidente = idResidente;
        this.descripcion = descripcion;
        this.fechaHoraIncidente = fechaHoraIncidente;
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public int getIdTipoInconveniente() {
        return idTipoInconveniente;
    }

    public void setIdTipoInconveniente(int idTipoInconveniente) {
        this.idTipoInconveniente = idTipoInconveniente;
    }

    public int getIdResidente() {
        return idResidente;
    }

    public void setIdResidente(int idResidente) {
        this.idResidente = idResidente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFechaHoraIncidente() {
        return fechaHoraIncidente;
    }

    public void setFechaHoraIncidente(Timestamp fechaHoraIncidente) {
        this.fechaHoraIncidente = fechaHoraIncidente;
    }

    public String getNombreTipoInconveniente() {
        return nombreTipoInconveniente;
    }

    public void setNombreTipoInconveniente(String nombreTipoInconveniente) {
        this.nombreTipoInconveniente = nombreTipoInconveniente;
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

}
