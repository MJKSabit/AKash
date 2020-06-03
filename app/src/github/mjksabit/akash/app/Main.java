package github.mjksabit.akash.app;

import github.mjksabit.akash.app.Controller.ServerConnect;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.json.JSONObject;

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

    private Parent replaceSceneContent(String fxml) throws Exception {
        stage.setResizable(true);
        Parent page = (Parent) FXMLLoader.load(Main.class.getResource("View/"+fxml+".fxml"), null, new JavaFXBuilderFactory());
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

    public static void main(String[] args) {
        launch(args);
    }
}
