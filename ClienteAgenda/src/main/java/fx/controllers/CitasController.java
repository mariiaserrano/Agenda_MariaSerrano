package fx.controllers;

import io.reactivex.Single;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import io.vavr.control.Either;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import model.Cita;
import model.Usuario;
import model.UsuarioLogin;
import model.UsuarioRegistro;
import retrofit.ApiError;
import servicios.ServiciosCitas;
import servicios.ServiciosUsuarios;

import java.net.URL;
import java.security.PublicKey;
import java.util.ResourceBundle;


public class CitasController implements Initializable {
    private PrincipalController principalController;
    private Alert alert;
    //Citas
    @FXML
    private ListView fxListaCitas;
    @FXML
    private TextField fxFechaCita;
    @FXML
    private TextField fxLugarCita;
    @FXML
    private TextArea fxDescCita;

    private ServiciosCitas sc = new ServiciosCitas();


    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }


    public void crearCita() {
        UsuarioLogin usuarioLogueado = principalController.getUsuario();
        String clavePublicaCodificada = principalController.getClaveCliente().getClavePublica();
        if (!fxLugarCita.getText().isEmpty() && !fxDescCita.getText().isEmpty() && !fxFechaCita.getText().isEmpty()) {
            Single<Either<ApiError, Cita>> s = Single.fromCallable(() ->
            {
                Cita cita = new Cita(null, fxLugarCita.getText(), fxDescCita.getText(), null,
                        fxFechaCita.getText(), null, null, 0, null);
                return sc.addCita(cita, usuarioLogueado,clavePublicaCodificada);

            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doFinally(() -> this.principalController
                            .getPantallaPrincipal().setCursor(Cursor.DEFAULT));
            s.subscribe(result -> result.peek(acierto -> {
                        alert.setContentText("Cita añadida correctamente");
                        alert.showAndWait();
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
            alert.setContentText("Debes rellenar todos los campos");
            alert.showAndWait();
        }
    }



    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert = new Alert(Alert.AlertType.INFORMATION);
    }

}

