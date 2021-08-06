import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;

public class CricketScorer extends Application {
    private static Stage mainStage;
    @Override
    public void start(Stage stage) throws FileNotFoundException, IOException {
        mainStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("scorer.fxml"));
        mainStage.setTitle("Cricket Scorer");
        Scene scene = new Scene(root, 1920, 1000);
        mainStage.setScene(scene);
        mainStage.show();

    }

    public static Stage getStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }

}
