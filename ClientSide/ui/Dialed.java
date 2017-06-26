/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sysclient.ui.menu.call;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;


public class Dialed extends Tab {
    GridPane pane;
    public Label caller;
    public Label Time;
    public Label duration;
    public Label CallType;
    public Button callBackbtn;
    public Dialed(){
        pane = new GridPane();
        caller = new Label();
        duration = new Label();
        CallType = new Label();
        callBackbtn = new Button("Call Back");
    }
    
    public void setUp(){
                 pane.setAlignment(Pos.CENTER);
                 pane.setVgap(5);
                 pane.setHgap(10);
             pane.add(caller, 0, 0);
             pane.add(Time, 0, 1);
             pane.add(duration, 0, 2);
             pane.add(CallType, 0, 3);
             pane.add(callBackbtn, 0, 4);
    }
    
}
