
package sysadmin.ui.menu.explore;

import java.time.LocalDate;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;

/*
** an interface with dialog boxes that can be used to  input 
** new staff member credentials
*/
public class New extends Tab
{
    private final GridPane form;
    private final VBox main;
    //text fields
    private final TextField name;//full name
    private final TextField nId;//national id
    private final TextField wId;//work id
    private final TextField ea;//email address
    private final TextField cp;//cellphone
    
    //combo boxes
    private final ComboBox<String> fac;
    private final ComboBox<String> dep;
    
    //date picker
    private final DatePicker dtPckr;
    
    private String gender;
    private String pwd;
    
    //submit button
    private final Button submit;
    
    
    //constructor
    public New(Button sub)
    {
        //init
        form = new GridPane();
        main = new VBox();
        name = new TextField();
        nId = new TextField();
        wId = new TextField();
        ea = new TextField();
        cp = new TextField();
        fac = new ComboBox<>();
        dep = new ComboBox<>();
        dtPckr = new DatePicker(LocalDate.now());
        submit = sub;
        //set tab title
        setText("New");
        //set a css IDs
        getStyleClass().add("h-tabs");
        form.setId("new-staff-grid");
        submit.getStyleClass().add("submit-button");
        
        //set prompt texts
        name.setPromptText("-- Enter the full name --");
        nId.setPromptText("-- Enter the national ID No. --");
        wId.setPromptText("-- Enter the work ID --");
        ea.setPromptText("-- Enter a valid e-mail address --");
        cp.setPromptText("-- Enter a cellphone No. --");
    }
    
    //set up a few parameters
    public void setUp(){
        //faculty combobox
        fac.getItems().addAll(
                "Science","Law","Engineering & Technology",
                "Veterinary Medicine & Surgery","Health Sciences",
                "Environment & Resource Development","Agriculture",
                "Arts & Social Sciences","Education & Community Studies",
                "Commerce","");
        //fac.setPlaceholder(new Label("-- select a faculty --"));
        fac.setPromptText("-- select a faculty --");
        //department combobox
        dep.getItems().addAll(
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
        //dep.setPlaceholder(new Label("-- select a department --"));
        dep.setPromptText("-- select a department --");
    }
    
    //set pane as the content of this tab
    public void show(){
        //radio buttons for selecting the gender
        RadioButton m = new RadioButton("Male");
        RadioButton f = new RadioButton("Female");
        
        //add buttons to a toggle group
        ToggleGroup selector = new ToggleGroup();
        selector.getToggles().addAll(m,f);
        m.setSelected(true);
        
        gender = "Male";
        
        //track gender selection changes
        selector.selectedToggleProperty().addListener(this::changed);
        
        //build grid
        form.add(new Label("Full Name"), 0, 0);
        form.add(name, 1, 0);
        form.add(new Label("National ID"), 0, 1);
        form.add(nId, 1, 1);
        form.add(new Label("Date of Birth"),0,2);
        form.add(dtPckr, 1, 2);
        form.add(new Label("Work ID"), 0, 3);
        form.add(wId, 1, 3);
        form.add(new Label("E-mail Address"), 0, 4);
        form.add(ea, 1, 4);
        form.add(new Label("Cellphone"), 0, 5);
        form.add(cp, 1, 5);
        form.add(new Label("Gender"), 0, 6);
        form.add(new HBox(20, m,f), 1, 6);
        form.add(new Label("Department"), 0, 7);
        form.add(dep, 1, 7);
        form.add(new Label("Faculty"), 0, 8);
        form.add(fac, 1, 8);
        
        form.setAlignment(Pos.CENTER);
        setClosable(false);
        //set up main pane
        main.getChildren().addAll(form, submit);
        main.setAlignment(Pos.CENTER);
        main.setSpacing(30);
        main.setPadding(new Insets(40, 70, 70, 70));
        //set as content
        Platform.runLater(()->{
            setContent(main);
        });
    }
    
    //build an SQL query
    public String getQuery()
    {
        JSONObject queryObject = new JSONObject();
        StringBuffer sBuff = new StringBuffer("INSERT IGNORE INTO staff(name,workID,"
                + "nationalID,gender,e_mail,phoneNo,department,faculty,dob) values(\"");
        
        sBuff.append(name.getText() + "\",\"" + wId.getText() + "\","
                + nId.getText() + ",\"" + gender + "\",\"" + ea.getText() + "\",\""
                        + cp.getText() + "\",\"" + dep.getValue() + "\",\""
                                + fac.getValue() + "\",\"" + dtPckr.getValue().toString() + 
                "\")");
        
        //prepare the database query object
        //specify type of the query (INSERT,UPDATE,ALTER,SELECT...)
        queryObject.put("type", "INSERT");
        //actual database query
        queryObject.put("query", sBuff.toString());
        
        return queryObject.toJSONString();
    }
    
    //define a gender change listener
    private void changed(ObservableValue<? extends Toggle> observable,
            Toggle oldB, Toggle newB)
    {
        if(newB != null)
        {//update gender
            gender = ((RadioButton)newB).getText();
        }
    }
    
    //reset the sign up form
    public void reset()
    {
        //clear all text fields
        name.clear();
        ea.clear();
        cp.clear();
        nId.clear();
        wId.clear();
        
        fac.setPromptText("-- select a faculty --");
        dep.setPromptText("-- select a department --");
    }
    
    //get the work id
    public String getWorkId()
    {
        return wId.getText();
    }
    
}
