package fx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lombok.SneakyThrows;
import model.Usuario;

import java.net.URL;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {
    private Alert alert;
    private AnchorPane pantallaLogin;
    private LoginController controller_login;

    private AnchorPane pantallaRegistro;
    private RegistroController controller_registro;

    private AnchorPane pantallaCitas;
    private CitasController controller_citas;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private Usuario usuario;

    @FXML
    private BorderPane pantallaPrincipal;

    public BorderPane getPantallaPrincipal() {
        return pantallaPrincipal;
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        cargarLogin();

    }

    @SneakyThrows
    public void cargarLogin() {
        if (pantallaLogin == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            pantallaLogin = fxmlLoader.load();
            controller_login = fxmlLoader.getController();
            controller_login.setPrincipalController(this);
        }
        pantallaPrincipal.setCenter(pantallaLogin);
    }

    @SneakyThrows
    public void cargarCitas() {
        if (pantallaCitas == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Citas.fxml"));
            pantallaCitas = fxmlLoader.load();
            controller_citas = fxmlLoader.getController();
            controller_citas.setPrincipalController(this);
        }
        pantallaPrincipal.setCenter(pantallaCitas);
    }


    @SneakyThrows
    public void cargarRegistro() {
        if (pantallaRegistro == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Registro.fxml"));
            pantallaRegistro = fxmlLoader.load();
            controller_registro = fxmlLoader.getController();
            controller_registro.setPrincipalController(this);
        }
        pantallaPrincipal.setCenter(pantallaRegistro);
    }


}