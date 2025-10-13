package Modelo;

import java.sql.Timestamp; // Para manejar las fechas de auditoría con precisión

public class Usuarios {

    private int idUsuario;
    private String dpi;
    private String nombre;
    private String apellidos;
    private String correo;
    private String contrasena;
    private int rolId;
    private Integer numeroCasaId; // puede ser null
    private Integer loteId;       // puede ser null
    private boolean estado;
    private String nombreRol;
    private Integer numeroCasa;   // número real de casa
    private String codigoLote;    // código real del lote
    // 🔹 Nuevos campos de auditoría
    private Integer creadoPor;          // id del usuario que creó
    private Integer modificadoPor;      // id del usuario que modificó
    private Timestamp fechaCreacion;    // fecha/hora creación
    private Timestamp fechaModificacion; // fecha/hora última modificación

    // Constructor vacío
    public Usuarios() {
    }

    // Constructor para insertar un nuevo usuario
    public Usuarios(String dpi, String nombre, String apellidos, String correo, String contrasena,
            int rolId, Integer numeroCasaId, Integer loteId, boolean estado, Integer creadoPor) {
        this.dpi = dpi;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rolId = rolId;
        this.numeroCasaId = numeroCasaId;
        this.loteId = loteId;
        this.estado = estado;
        this.creadoPor = creadoPor;
    }

// Getters y setters
    public Integer getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(Integer numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public String getCodigoLote() {
        return codigoLote;
    }

    public void setCodigoLote(String codigoLote) {
        this.codigoLote = codigoLote;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    public Integer getNumeroCasaId() {
        return numeroCasaId;
    }

    public void setNumeroCasaId(Integer numeroCasaId) {
        this.numeroCasaId = numeroCasaId;
    }

    public Integer getLoteId() {
        return loteId;
    }

    public void setLoteId(Integer loteId) {
        this.loteId = loteId;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    // 🔹 Nuevos getters y setters de auditoría
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
