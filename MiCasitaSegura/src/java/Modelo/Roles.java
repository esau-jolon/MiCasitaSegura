package Modelo;

public class Roles {
    private int idRol;
    private String nombreRol; // 'Administrador', 'Guardia', 'Residente'

    public Roles() {
    }

    public Roles(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
}
