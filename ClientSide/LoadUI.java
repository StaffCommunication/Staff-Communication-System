package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    private final Desktop desktop = Desktop.getDesktop();//get desktop

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        AnchorPane group1 = new AnchorPane();
        AnchorPane group2 = new AnchorPane();
        AnchorPane group3 = new AnchorPane();
        final Scene scene1 = new Scene(group1,500,350);//login scene
        final Scene scene2 = new Scene(group2,750,500 );//main scene
        final Scene scene3 = new Scene(group3);//not yet used

        primaryStage.setTitle("Many Scenes");
        Label username =new Label("Username");
        username.setLayoutX(116);
        username.setLayoutY(145);
        TextField textuser=new TextField();
        textuser.setLayoutX(177);
        textuser.setLayoutY(141);
        username.setTextFill(Color.web("#0076a3"));
        textuser.setPromptText("Username");
        Label password=new Label("Password");
        password.setLayoutX(116);
        password.setLayoutY(197);
        PasswordField textpass=new PasswordField();
        textpass.setLayoutX(177);
        textpass.setLayoutY(193);
        textpass.setPromptText("Password");
        Button login=new Button("Sign");
       // login.setStyle("-fx-background-color: cornflowerblue;-fx-padding: 5");
        login.setLayoutY(193);
        login.setLayoutX(342);
        login.setOnAction(new EventHandler<ActionEvent>() {    //open main scene
            @Override
            public void handle(javafx.event.ActionEvent event) {
                final FileChooser fileChooser= new FileChooser();  //the class to getfiles
                primaryStage.setScene(scene2);//second scene layout
               // Button btn=new Button("YES");
                MenuBar menuBar = new MenuBar();
                menuBar.setLayoutY(6);
                menuBar.setLayoutX(7);
                menuBar.setLayoutY(6);
                menuBar.setLayoutX(7);
                Menu menuConf = new Menu("Home");
                Menu menuView = new Menu("_View ");
                Menu menuTools = new Menu("_Tools");
                Menu menuSetup=new Menu("_Setup");
                Menu menuHelp=new Menu("_Help");
                menuBar.getMenus().addAll(menuConf, menuView, menuTools,menuSetup,menuHelp);
                Button btn=new Button("Edit my profile");
                btn.setLayoutX(148);
                btn.setLayoutY(129);
                group2.getChildren().addAll(menuBar,btn);

                btn.setOnAction(                        //set the file from dektop
                        (javafx.event.ActionEvent e) -> {
                            File file = fileChooser.showOpenDialog(primaryStage);
                            if (file != null) {
                                openFile(file);
                            }
                        });

            }
        });
        group1.getChildren().addAll(new Label("Welcome"),username,textpass,password,textuser,login);//added all nodes to root ancho
        group2.setStyle("-fx-border-width: 5.0; -fx-border-color: lightblue;");
       // group2.setStyle("-fx-cursor: hand;-fx-border-color: blue;-fx-border-width: 5px;");
        primaryStage.setScene(scene1);
        primaryStage.show();


    }
    private void openFile(File file) {              //file method
        EventQueue.invokeLater(() -> {
            try {
                desktop.open(file);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
    }
}
