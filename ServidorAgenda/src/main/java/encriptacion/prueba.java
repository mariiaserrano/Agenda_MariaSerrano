package encriptacion;

import dao.DaoUsuarios;
import model.Usuario;

import java.net.UnknownServiceException;

public class prueba {
    public static void main(String[] args) {

        Usuario login = new Usuario("admin","jeluous","asdfa");
        DaoUsuarios dao = new DaoUsuarios();
        System.out.println(dao.login(login));



    }
}
