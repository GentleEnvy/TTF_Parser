package task_3.window;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Window extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = mainLoader.load();

        stage.setMaximized(true);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void startWindow() {
        launch();
    }
}
