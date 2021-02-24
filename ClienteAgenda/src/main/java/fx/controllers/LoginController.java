package fx.controllers;

import io.reactivex.Single;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import io.vavr.control.Either;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.ClienteCitas;
import model.Usuario;
import model.UsuarioLogin;
import model.UsuarioRegistro;
import retrofit.ApiError;
import servicios.ServiciosUsuarios;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    private Alert alert;
    @FXML
    private TextField fxUsuario;
    @FXML
    private PasswordField fxContrasena;
    private ServiciosUsuarios sl = new ServiciosUsuarios();

    private PrincipalController principalController;
    ClienteCitas cliente = new ClienteCitas();
    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }


    public void abrirRegistro() {
        principalController.cargarRegistro();
    }

    public void abrirCitas() {
        principalController.cargarCitas();
    }

    public void hazLogin() {

        UsuarioLogin usuario = new UsuarioLogin(fxUsuario.getText(), "", fxContrasena.getText());

        cliente=sl.login(usuario).get();
        if(cliente.isIniciado()){
            principalController.setUsuario(usuario);
            principalController.setClaveCliente(cliente);
            abrirCitas();
        } else {
            alert.setContentText("Registrado correctamente");
            alert.showAndWait();
        }
    }



//    public void hazLogin() {
//
//        UsuarioLogin usuario = new UsuarioLogin(fxUsuario.getText(), "", fxContrasena.getText());
//        if (sl.login(usuario).get()) {
//            principalController.setUsuario(usuario);
//            abrirCitas();
//        } else {
//            alert.setContentText("Registrado correctamente");
//            alert.showAndWait();
//        }
//    }


//
//    public void hazLogin2() {
//        if (!fxUsuario.getText().isEmpty() && !fxContrasena.getText().isEmpty()) {
//                Single<Either<ApiError, Boolean>> s = Single.fromCallable(() ->
//                {
//                    ServiciosUsuarios serviciosUsuarios = new ServiciosUsuarios();
//                    return serviciosUsuarios.login(new UsuarioLogin(fxUsuario.getText(),"", fxContrasena.getText()));
//
//                })
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(JavaFxScheduler.platform())
//                        .doFinally(() -> this.principalController
//                                .getPantallaPrincipal().setCursor(Cursor.DEFAULT));
//                s.subscribe(result -> result.peek(acierto -> {
//                            principalController.cargarCitas();
//                            alert.setContentText("Bienvenido");
//                            alert.showAndWait();
//                        })
//                                .peekLeft(error -> {
//                                    alert.setContentText(error.getMessage());
//                                    alert.showAndWait();
//                                }),
//                        throwable -> {
//                            alert.setContentText(throwable.getMessage());
//                            alert.showAndWait();
//
//                        });
//
//        } else {
//            alert.setContentText("Debes rellenar todos los campos");
//            alert.showAndWait();
//        }
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert = new Alert(Alert.AlertType.INFORMATION);

    }
}
