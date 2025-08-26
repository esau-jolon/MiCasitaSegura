package Intefaces;

import Modelo.Usuarios;
import java.util.List;

public interface UsuarioCrud {
    public List<Usuarios> listar();
    public Usuarios listarId(int id);
    public boolean add(Usuarios u);
    public boolean edit(Usuarios u);
    public boolean delete(int id);
}
