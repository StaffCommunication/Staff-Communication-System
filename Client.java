/**
 * Created by Joseph Sang on 5/22/2017.
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application{
    @Override
    public void start(Stage program_stage)throws IOException{
        FXMLLoader loader=new FXMLLoader();
        VBox root=loader.load(getClass().getResourceAsStream("/clientview.fxml"));
        program_stage.setTitle("LSC Client");
        program_stage.setScene(new Scene(root, 800.0, 600.0));
        program_stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
