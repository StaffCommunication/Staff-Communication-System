
/*
*display interface for creating a new staff member account
*/
package sysadmin.staff;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import org.json.simple.JSONObject;


public class New extends GridPane {
    
    //declare variables
    private final TextField fName;
    private final TextField workID;
    private final TextField nationalID;
    private String gender;
    private final ComboBox<String> faculty;
    private final ComboBox<String> department;
    private final TextField cellPhone;
    private final TextField eAddress;
    //password, set it to Work ID, can be changed later by the owner of the a/c
    private String pwd;
    
    
    //constructor
    public New()
    {
        //initialize variables
        fName = new TextField();
        cellPhone = new TextField();
        workID = new TextField();
        department = new ComboBox<>();
        faculty = new ComboBox<>();
        
        nationalID = new TextField();
        eAddress = new TextField();
        
        //set a css id for the grid
        this.setId("new-staff");
    }
    
    //set up faculty and department combo boxes
    public void setUp()
    {
        //faculties
        faculty.getItems().addAll(
                "Science","Law","Engineering & Technology",
                "Veterinary Medicine & Surgery","Health Sciences",
                "Environment & Resource Development","Agriculture",
                "Arts & Social Sciences","Education & Community Studies",
                "Commerce","");
        faculty.setPlaceholder(new Label("-- select a faculty"));
        
        //departments
        department.getItems().addAll(
                "Computer Science","Mathematics","Biochemistry","Economics",
                "Agricultural Economics & Agribusiness Management","Clinical Medicine",
                "Agricultural Engineering","Animal Sciences","Chemistry","Nursing",
                "Medical Psychology","Surgery & Reproductive Health","Physics",
                "Peace, Security & Social Studies","Natural Resources","Industrial Energy",
                "Literature Language & Linguistics","Human Nutrition","Human Anatomy",
                "History, Philosophy & Religion","Environmental Science","Geography",
                "Electrical Engineering","Curriculum & Instruction","Civil Engineering",
                "Medicine, Child Health & Community Health","Crops, Horticulture & Soils",
                "Business Administration","Biological Sciences",
                "Agricultural Education & Extension",
                "Applied Development & Community Studies");
        department.setPlaceholder(new Label("-- select a department"));
        
    }
    
    //define the listener
    private void changed(ObservableValue<? extends Toggle> observable,
            Toggle oldB, Toggle newB)
    {
        if(newB != null)
        {//update gender
            gender = ((RadioButton)newB).getText();
        }
    }
    
    //display
    public void showGrid()
    {
        //create a toggle group
        ToggleGroup genderSelector = new ToggleGroup();
        
        //radio buttons
        RadioButton m = new RadioButton("MALE");
        RadioButton f = new RadioButton("FEMALE");
        
        //add radio buttons to toggle group
        genderSelector.getToggles().addAll(m,f);
        //set the default selected radio button
        m.setSelected(true);
        
        //track gender selection changes
        genderSelector.selectedToggleProperty().addListener(this::changed);
        //name
        this.add(new Label("NAME"), 0, 0);
        this.add(fName, 1, 0);
        //gender
        this.add(new Label("GENDER"),0,1);
        this.add(new FlowPane(m,f),1,1);
        //work id
        this.add(new Label("WORK ID"),0,2);
        this.add(workID,1,2);
        //national id
        this.add(new Label("NATIONAL ID"),0,3);
        this.add(nationalID,1,3);
        //e-mail address
        this.add(new Label("E-MAIL ADDRESS"),0,4);
        this.add(eAddress, 1, 4);
        //department
        this.add(new Label("DEPARTMENT"), 0, 5);
        this.add(department, 1, 5);
        //faculty
        this.add(new Label("FACULTY"), 0, 6);
        this.add(faculty, 1, 6);
        
    }

    //on SUBMIT click
    JSONObject onSubmit()
    {
        //get all fields, build a json object, return it
        JSONObject values = new JSONObject();
        //get the name
        values.put("name",fName.getText());
        //get the work id
        values.put("workid", workID.getText());
        //get the 
        
        return values;
    }
    
    //validate inputs
    boolean isValidMailAddress()
    {
        return false;
    }
    
    boolean isValidName()
    {
        return true;
    }
    
    boolean isValidWorkId()
    {
        return true;
    }
    
    boolean isValidCellPhoneNo()
    {
        return true;
    }
    
}
