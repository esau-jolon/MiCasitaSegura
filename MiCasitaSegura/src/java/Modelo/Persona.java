
package Modelo;

public class Persona {
    int id;
    String dpi;
    String nom;

    public Persona() {
    }

    public Persona(String dpi, String nom) {
        this.dpi = dpi;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
}
