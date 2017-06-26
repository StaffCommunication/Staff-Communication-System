package loginui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class EventsAdd extends Application {
    
    public static StackPane EventsTable(){
        StackPane stack=new StackPane();
        TableView events=new TableView();
          
        TableColumn event = new TableColumn("Event");
        event.setMinWidth(200);
        event.setCellValueFactory(new PropertyValueFactory<>("Event"));
 
        TableColumn venue = new TableColumn("Venue");
        venue.setMinWidth(200);
        venue.setCellValueFactory(new PropertyValueFactory<>("Venue"));
        
        TableColumn date = new TableColumn("Date");
        date.setMinWidth(200);
        date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        events.getColumns().addAll(event,venue,date);
        
        VBox vbox=new VBox();
        vbox.getChildren().add(events);
        stack.getChildren().add(vbox);
        return stack;
        }
    
    @Override
    public void start(Stage primaryStage) {
        Button addEvent=new Button("Add Event");
        
        addEvent.setOnMouseClicked(e -> {
            
        });
                
        
        StackPane root = new StackPane();
        root.getChildren().add(addEvent);
        
        Scene scene = new Scene(root, 300, 250);
        
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
}
