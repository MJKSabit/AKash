package github.mjksabit.akash.app;

import com.jfoenix.controls.JFXSnackbar;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Main extends Application {

    public static Stage stage;

    private static Main instance;

    public Main() {
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;

        primaryStage.setTitle("AKash");
//        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setOnCloseRequest((event) -> {
            System.out.println("Bye");
            Platform.exit();
            System.exit(0);
        });

        showAuthorizationStage();
    }

    public boolean showAuthorizationStage () {
        try {
            replaceSceneContent("authentication");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        stage.show();
        return true;
    }

    public static Parent replaceSceneContent(String fxml) throws Exception {
        stage.setResizable(true);
        Parent page = loadFXML(fxml);
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page);
//            scene.getStylesheets().add(Main.class.getResource("demo.css").toExternalForm());
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(page);
        }
        stage.sizeToScene();
        //stage.setResizable(false);
        return page;
    }

    public static Parent loadFXML(String fxml) throws IOException {
        return FXMLLoader.load(Main.class.getResource("View/"+fxml+".fxml"));
    }

    public static void showError(String text, long durationInMSec) {
        Pane rootPane = (Pane) stage.getScene().getRoot();
        showError(rootPane, text, durationInMSec);
    }

    public static void showError(Pane rootPane, String text, long durationInMSec) {
        Label toast = new Label(text);
        toast.setPrefWidth(rootPane.getWidth());
        toast.setWrapText(true);
        toast.setStyle("-fx-background-color: #ff0e4d; -fx-text-fill: #f0f8ff; -fx-padding: 20px; -fx-alignment: center; ");
        showNotification(rootPane, toast, durationInMSec);
    }

    public static void showSuccess(String text, long duration) {
        Pane rootPane = (Pane) stage.getScene().getRoot();
        showSuccess(rootPane, text, duration);
    }

    public static void showSuccess(Pane rootPane, String text, long duration) {
        Label toast = new Label(text);
        toast.setPrefWidth(rootPane.getWidth());
        toast.setWrapText(true);
        toast.setStyle("-fx-background-color: #00ba35; -fx-text-fill: #f0f8ff; -fx-padding: 20px; -fx-alignment: center; ");
        showNotification(rootPane, toast, duration);
    }

    private static void showNotification(Pane rootPane, Node node, long duration) {
        Platform.runLater( () -> {
            JFXSnackbar snackbar = new JFXSnackbar(rootPane);
            JFXSnackbar.SnackbarEvent eventToast = new JFXSnackbar.SnackbarEvent(node, new Duration(duration), null);
            snackbar.enqueue(eventToast);
        } );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
