package fx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginController {

    @FXML
    private TextField fxUsuario;
    @FXML
    private PasswordField fxContrasena;

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
}
