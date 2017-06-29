/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sysadmin.ui.menu.account;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


public class PasswordChanger extends ToggleButton
{
    private final PasswordField nPwd;
    private final PasswordField oPwd;
    private final PasswordField cPwd;
    
    private final Button save;
    
    //container
    private VBox cont;
    
    //constructor
    public PasswordChanger(Button sb)
    {
        super("Password");
        nPwd = new PasswordField();
        oPwd = new PasswordField();
        cPwd = new PasswordField();
        save = sb;
        getStyleClass().add("profile-toggle");
        save.getStyleClass().add("submit-button");
    }
    
    //grid
    public void setUpGrid()
    {
        cont = new VBox();
        GridPane gp = new GridPane();
        
        //pack
        gp.add(new Label("Current Password"), 0, 0);
        gp.add(oPwd, 1, 0);
        gp.add(new Label("New Password"), 0, 1);
        gp.add(nPwd, 1, 1);
        gp.add(new Label("Confirm"), 0, 2);
        gp.add(cPwd, 1, 2);
        
        //style
        gp.setVgap(20);
        gp.setHgap(20);
        
        gp.setStyle("-fx-background-color : inherit;"
                + "-fx-text-fill : #888;"
                + "-fx-font-family : \"Lucida\";"
                + "-fx-font-size: 13px;"
                + "-fx-padding : 20px 0px 0px 20px;");
        
        cont.getChildren().addAll(gp, save);
        cont.setSpacing(15);
        //cont.setPadding(new Insets(30));
        cont.setAlignment(Pos.CENTER);
        gp.setAlignment(Pos.CENTER);
    }
    
    //getter
    public VBox getPCPane()
    {
        return cont;
    }
}
