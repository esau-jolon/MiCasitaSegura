package Modelo;

public class RolVista {
    private int idCasa;
    private int numeroCasa;
    private int loteId; // FK a Lotes

    public RolVista() {
    }

    public RolVista(int numeroCasa, int loteId) {
        this.numeroCasa = numeroCasa;
        this.loteId = loteId;
    }

    public int getIdCasa() {
        return idCasa;
    }

    public void setIdCasa(int idCasa) {
        this.idCasa = idCasa;
    }

    public int getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(int numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public int getLoteId() {
        return loteId;
    }

    public void setLoteId(int loteId) {
        this.loteId = loteId;
    }
}
