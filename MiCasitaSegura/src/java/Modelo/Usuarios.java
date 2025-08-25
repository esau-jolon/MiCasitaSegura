package Modelo;

public class Usuarios {
    private int idUsuario;
    private String dpi;
    private String nombre;
    private String apellidos;
    private String correo;
    private String contrasena; // mejor no usar "password" para que coincida con la BD
    private int rolId;
    private Integer numeroCasaId; // puede ser null
    private Integer loteId;       // puede ser null
    private boolean estado;

    // Constructor vacío
    public Usuarios() {
    }

    // Constructor sin id (para insertar nuevos registros)
    public Usuarios(String dpi, String nombre, String apellidos, String correo, String contrasena, int rolId, Integer numeroCasaId, Integer loteId, boolean estado) {
        this.dpi = dpi;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rolId = rolId;
        this.numeroCasaId = numeroCasaId;
        this.loteId = loteId;
        this.estado = estado;
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
}

