package sample;
import java.io.File;
import java.net.URL;
import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import static javafx.scene.media.MediaPlayer.Status.PLAYING;

public class Controller extends Application{
    public static void main(String[] args) {
        Application.launch(args);
    }

            @Override
            public void start(Stage stage) {
// Locate the media content in the CLASSPATH
                
                String mediaPath = new File("src/mediaa/Hazard.mp4").getAbsolutePath();

// Create a Media
             //   Media me = new Media(mediaStringUrl);
                Media me=new Media(new File(mediaPath).toURI().toString());
// Create a Media Player
                MediaPlayer player = new MediaPlayer(me);
// Automatically begin the playback
                player.setAutoPlay(true);
// Create a 400X300 MediaView
                MediaView mediaView = new MediaView(player);
                mediaView.setFitWidth(750);
                mediaView.setFitHeight(450);

// Create Play and Stop player control buttons and add action
// event handlers to them
                Button playBtn = new Button("Play");
                playBtn.setOnAction(e -> {
                    if (player.getStatus() == PLAYING) {
                        player.stop();
                        player.play();
                    } else {
                        player.play();
                    }
                });
                Button stopBtn = new Button("Stop");
                stopBtn.setOnAction(e -> player.stop());
// Add an error handler
                player.setOnError(() -> System.out.println(player.getError().getMessage()));

                // Add a ChangeListener to the player
                player.statusProperty().addListener((prop, oldStatus, newStatus) -> {
                    System.out.println("Status changed from " + oldStatus + " to " + newStatus);
                });
                // The playback should repeat 4 times
                player.setCycleCount(4);
                player.setOnEndOfMedia(() -> {
                    System.out.println("End of media...");
                });
                // Play the media at 3x
                player.setRate(1.0);
                // Display the metadata data on the console
                player.setOnReady(() -> {
                    ObservableMap<String, Object> metadata = me.getMetadata();
                    for(String key : metadata.keySet()) {
                        System.out.println(key + " = " + metadata.get(key));
                    }
                });
                HBox controlBox = new HBox(5, playBtn, stopBtn);
                BorderPane root = new BorderPane();
// Add the MediaView and player controls to the scene graph
                root.setCenter(mediaView);
                root.setBottom(controlBox);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Playing Media");
                stage.show();
            }

}
