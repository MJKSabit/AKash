package github.mjksabit.akash.app;

import github.mjksabit.akash.app.Controller.ServerConnect;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.json.JSONObject;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/authentication.fxml"));
        AnchorPane pane = loader.load();

        Scene scene = new Scene(pane, 300, 275);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
//        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest((event) -> {
            System.out.println("Bye");
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();

        ServerConnect connect = ServerConnect.getInstance();

        JSONObject request = new JSONObject();
        request.put("requestType", "Connected");
        connect.sendRequest(request);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
