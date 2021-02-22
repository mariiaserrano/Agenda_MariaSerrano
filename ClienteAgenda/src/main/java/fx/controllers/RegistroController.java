package fx.controllers;

import encriptacion.Claves;
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
import model.Usuario;
import model.UsuarioRegistro;
import retrofit.ApiError;
import servicios.ServiciosUsuarios;

import java.net.URL;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ResourceBundle;

public class RegistroController implements Initializable {
    private Alert alert;
    @FXML
    private TextField fxUsuarioRegistro;
    @FXML
    private PasswordField fxContrasenaRegistro;
    @FXML
    private PasswordField fxContrasenaConfir;

    private PrincipalController principalController;

    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }

    public void registro() {
        if(!fxUsuarioRegistro.getText().isEmpty() && !fxContrasenaRegistro.getText().isEmpty() && !fxContrasenaConfir.getText().isEmpty()) {
            if (fxContrasenaRegistro.getText().equals(fxContrasenaConfir.getText())) {
                Single<Either<ApiError, UsuarioRegistro>> s = Single.fromCallable(() ->
                {
                    ServiciosUsuarios serviciosUsuarios = new ServiciosUsuarios();
                    return serviciosUsuarios.addUsuario(new Usuario(fxUsuarioRegistro.getText(), fxContrasenaConfir.getText(),""));

                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(JavaFxScheduler.platform())
                        .doFinally(() -> this.principalController
                                .getPantallaPrincipal().setCursor(Cursor.DEFAULT));
                s.subscribe(result -> result.peek(acierto -> {
                            alert.setContentText("Registrado correctamente");
                            alert.showAndWait();
                            limpiar();
                        })
                                .peekLeft(error -> {
                                    alert.setContentText(error.getMessage());
                                    alert.showAndWait();
                                }),
                        throwable -> {
                            alert.setContentText(throwable.getMessage());
                            alert.showAndWait();

                        });
            } else {
                alert.setContentText("Las contraseñas deben ser iguales");
                alert.showAndWait();
            }
        }
        else {
            alert.setContentText("Debes rellenar todos los campos");
            alert.showAndWait();
        }

    }

    public void limpiar(){
        fxUsuarioRegistro.clear();
        fxContrasenaRegistro.clear();
        fxContrasenaConfir.clear();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert = new Alert(Alert.AlertType.INFORMATION);
    }
}
