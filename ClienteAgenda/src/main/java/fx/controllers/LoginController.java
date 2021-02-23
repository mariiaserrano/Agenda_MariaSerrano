package fx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Usuario;
import model.UsuarioLogin;
import servicios.ServiciosUsuarios;


public class LoginController {

    @FXML
    private TextField fxUsuario;
    @FXML
    private PasswordField fxContrasena;
    private ServiciosUsuarios sl = new ServiciosUsuarios();

    private PrincipalController principalController;
    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }


    public void abrirRegistro(){
        principalController.cargarRegistro();
    }

    public void abrirCitas(){
        principalController.cargarCitas();
    }

    public void hazLogin(){
        UsuarioLogin usuario = new UsuarioLogin(fxUsuario.getText(),"",fxContrasena.getText());
        sl.login(usuario);
    }
}
