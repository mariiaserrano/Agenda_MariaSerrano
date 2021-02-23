package fx.main;

import dao.DaoUsuarios;
import encriptacion.Claves;
import model.Usuario;

public class hola {
    public static void main(String[] args) {

        Usuario login = new Usuario("root","root","asdfa");
        Claves cl = new Claves();
       // System.out.println(cl.getClavePrivada(login));
      //  System.out.println(cl.getClavePublica(login));



    }
}
