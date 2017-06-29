/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sysadmin.ui.menu.chat;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import sysadmin.ui.scenes.Home;

/**
 *
 * @author Vincent
 */
public class MessageBox extends HBox
{
    
    public MessageBox(String msg, String dest)
    {
        Label l = new Label(msg);
        l.setMaxWidth(150);
        l.setMinWidth(20);
        l.setPadding(new Insets(5,10,5,10));
        
        if(dest.compareTo(Home.getWorkId()) == 0)
        {
            getChildren().add(l);
            l.setStyle("-fx-background-color : blue;"
                    + "-fx-text-fill : silver;"
                    + "-fx-font-family : \"Courier\";"
                    + "-fx-border-radius : 5px;"
                    + "-fx-background-radius : 5px;"
                    + "-fx-font-size : 14px;");
            setAlignment(Pos.CENTER_LEFT);
        }else
        {
            getChildren().add(l);
            l.setStyle("-fx-background-color : green;"
                    + "-fx-text-fill : silver;"
                    + "-fx-font-family : \"Courier\";"
                    + "-fx-border-radius : 5px;"
                    + "-fx-background-radius : 5px;"
                    + "-fx-font-size : 14px;");
            setAlignment(Pos.CENTER_RIGHT);
        }
    }
}
