package github.mjksabit.akash.app;

import com.jfoenix.controls.JFXSnackbar;
import github.mjksabit.akash.app.Model.Controller;
import github.mjksabit.akash.app.Network.ServerConnect;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.regex.Pattern;

public class Main extends Application {

    // Current Active Stage
    public static Stage stage;

    // Main Class Object - To avoid static Variables
    private static Main instance;

    public Main() {
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }

    /**
     * JavaFX Application Start
     *
     * @param primaryStage Primary Stage where to set scene
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Main.stage sets to primaryStage for Other Classes to use
        stage = primaryStage;

        primaryStage.setTitle("AKash - Banking App");
//      primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setOnCloseRequest((event) -> {
            Platform.exit(); // Stage Exit
            System.exit(0); // All thread Close
        });

        showAuthorizationStage();
    }

    public void showAuthorizationStage() {
        try {
            replaceSceneContent("authentication");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
//        stage.show();
    }

    /**
     * Replace Main.stage with fxml Content
     *
     * @param fxml Name of the FXML file
     * @return Controller to the FXML file, Cast to Use
     */
    public static Object replaceSceneContent(String fxml) {
        // Stage set to Resizable to load the Parent in it's actual size
        stage.setResizable(true);

        FXMLLoader loader = null;
        Parent parent = null;

        // Loader to load FXML
        loader = loadFXML(fxml);

        // FXML must be loaded before using getController
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = stage.getScene();

        // Scene not set, create new Scene
        if (scene == null || parent == null) {
            scene = new Scene(parent);
//          scene.getStylesheets().add(Main.class.getResource("demo.css").toExternalForm());
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(parent);
        }

        // Stage resize to Preferred Size
        stage.sizeToScene();

//      stage.setResizable(false);

        // Stage
        stage.show();

        return loader.getController();
    }

    public static FXMLLoader loadFXML(String fxml) {
        return new FXMLLoader(Main.class.getResource("View/" + fxml + ".fxml"));
    }

    /**
     * Creates new Window in the provided Stage and return it's Controller, Cast to USE
     *
     * @param fxml        FXML Filename
     * @param stage       The Stage to Show new Window
     * @param windowTitle New Window Title
     * @return Controller to that FXML File
     */
    public static Controller newWindowUtility(String fxml, Stage stage, String windowTitle) {
        Controller controller = null;

        stage.initStyle(StageStyle.UTILITY);

        try {
            FXMLLoader loader = Main.loadFXML(fxml);

            // Load FXML before accessing Controller
            Scene scene = new Scene(loader.load());

            // Get FXML Controller
            controller = loader.getController();

            stage.setScene(scene);
            stage.sizeToScene();
            stage.setTitle(windowTitle);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return controller;
    }

    /**
     * Shows Error Message in Main.stage Scene
     *
     * @param text           Error Text
     * @param durationInMSec Duration of Error Message
     */
    public static void showError(String text, long durationInMSec) {
        Pane rootPane = (Pane) stage.getScene().getRoot();
        showError(rootPane, text, durationInMSec);
    }

    /**
     * Shows Error Message in provided Pane
     *
     * @param rootPane       Pane to show Error Message
     * @param text           Error Text
     * @param durationInMSec Duration of Message
     */
    public static void showError(Pane rootPane, String text, long durationInMSec) {
        Label toast = new Label(text);
        toast.setPrefWidth(rootPane.getWidth());
        toast.setWrapText(true);
        toast.setStyle("-fx-background-color: #ff0e4d; -fx-text-fill: #f0f8ff; -fx-padding: 20px; -fx-alignment: center; ");
        showNotification(rootPane, toast, durationInMSec);
    }


    /**
     * Shows Success Message in Main.stage Pane
     *
     * @param text           Success Text
     * @param durationInMSec Duration of Message
     */
    public static void showSuccess(String text, long durationInMSec) {
        Pane rootPane = (Pane) stage.getScene().getRoot();
        showSuccess(rootPane, text, durationInMSec);
    }

    /**
     * Shows Success Message in provided Pane
     *
     * @param rootPane       Pane to show Success Message
     * @param text           Success Text
     * @param durationInMSec Duration of Message
     */
    public static void showSuccess(Pane rootPane, String text, long durationInMSec) {
        Label toast = new Label(text);
        toast.setPrefWidth(rootPane.getWidth());
        toast.setWrapText(true);
        toast.setStyle("-fx-background-color: #00ba35; -fx-text-fill: #f0f8ff; -fx-padding: 20px; -fx-alignment: center; ");
        showNotification(rootPane, toast, durationInMSec);
    }

    /**
     * Shows Custom Notification (node) from rootPane
     *
     * @param rootPane Pane to show Notification
     * @param node     Node to show as Notification
     * @param duration Notification Time
     */
    private static void showNotification(Pane rootPane, Node node, long duration) {
        Platform.runLater(() -> {
            JFXSnackbar snackbar = new JFXSnackbar(rootPane);
            JFXSnackbar.SnackbarEvent eventToast = new JFXSnackbar.SnackbarEvent(node, new Duration(duration), null);
            snackbar.enqueue(eventToast);
        });
    }

    public static void main(String[] args) {

        // If Argument is Provided and its a Number,
        if (args.length > 0 && Pattern.matches("\\d+", args[0])) {
            // Assign this Number as PORT
            ServerConnect.PORT = Integer.parseInt(args[0]);
            System.out.println("Setting PORT: " + ServerConnect.PORT);
        }

        // Launch JavaFX GUI Application
        launch(args);
    }
}
