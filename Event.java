/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sysclient.ui.menu.events;

import java.time.LocalDate;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.json.simple.JSONObject;

/**
 *
 * @author Coder
 */

public class Event extends  GridPane
{
    private TextField nameField;
    public DatePicker datePicker;
    private TextField venueField;
    private TextArea describeField;
    private TextField receiverEvent;

    public Event() {
        this.nameField = new TextField();
        nameField.setPromptText("Event Title");
        this.datePicker = new DatePicker();
        datePicker.setPromptText("Event Title");
        this.venueField=new TextField();
        venueField.setPromptText("Event Venue");
        this.describeField = new TextArea();
        describeField.setPromptText("Event Details");
        this.receiverEvent=new TextField();
        receiverEvent.setPromptText("+ sendto individuals,faculties,departments");
        
        
        datePicker.setOnAction(e->{ 
           LocalDate date= datePicker.getValue();
           
            
        });
    }
    
    public void addItems(){
        //GridPane grid=new GridPane();
        ColumnConstraints col1=new ColumnConstraints(20);
        ColumnConstraints col2=new ColumnConstraints(250);
        add(nameField,1,0 );
        add(datePicker,1,1 );
        add(venueField,1,2 );
        add(new Label("To"),0,3);
        add(describeField,1,3 );
        
        
        getColumnConstraints().addAll(col1,col2);
        setAlignment(Pos.CENTER);
        setVgap(10);
    }
    
    public JSONObject readValues()
    {
        JSONObject jo = new JSONObject();
        
        return jo;
    }
    
}
