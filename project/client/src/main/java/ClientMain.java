import client.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

    Controller controller = new Controller();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("utils/startPage.fxml"));
        primaryStage.setTitle("net-storage");
        primaryStage.setScene(new Scene(root, 1280, 600));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
//            controller.disconnectToServer();
            Platform.exit();
            System.exit(0);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
