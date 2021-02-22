package fx.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.Security;

import static javafx.application.Application.launch;

public class MainFx extends Application {

    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loaderMenu = new FXMLLoader(
                getClass().getResource("/fxml/Principal.fxml"));
        BorderPane root = loaderMenu.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Agenda");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
//        BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
//        Security.addProvider(bouncyCastleProvider);
    }
}
