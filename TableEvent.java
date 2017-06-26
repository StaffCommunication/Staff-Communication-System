
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TableEvent extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Label event=new Label();
        TextField eventDescription=new TextField();
        Label venue=new Label();
        TextField venueField=new TextField();
        Label date=new Label();
        TextField dateField=new TextField();
        
        HBox hbox1 =new HBox();
        hbox1.getChildren().addAll(event,eventDescription);
        HBox hbox2=new HBox();
        hbox2.getChildren().addAll(venue,venueField);
        HBox hbox3=new HBox();
        hbox3.getChildren().addAll(date,dateField);
        VBox vbox=new VBox();
        vbox.getChildren().addAll(hbox1,hbox2,hbox3);
        
        StackPane root = new StackPane();
        root.getChildren().add(vbox);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
