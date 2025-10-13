package Modelo;

import java.sql.Timestamp;

public class TipoIncidente {
    private int idTipoIncidente;
    private String nombre;
    private String descripcion;

    // Auditoría
    private Integer creadoPor;
    private Integer modificadoPor;
    private Timestamp fechaCreacion;
    private Timestamp fechaModificacion;

    public TipoIncidente() {}

    public TipoIncidente(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdTipoIncidente() {
        return idTipoIncidente;
    }

    public void setIdTipoIncidente(int idTipoIncidente) {
        this.idTipoIncidente = idTipoIncidente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return nombre;
    }
}
