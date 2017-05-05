
package sysadmin.ui.scenes;

import java.util.HashMap;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


import sysadmin.net.Connector;
import sysadmin.staff.ReadOnly;


public class Home extends Scene
{
    //layout manager
    private BorderPane rootP;
    //connector
    private Connector connector;
    
    //menu bar
    private HBox nav;
    //new
    private Button nw;
    private Button all;
    private Button edit;
    private Button home;
    
    //list of staff
    private HashMap<String,ReadOnly> list;
    
    
    //constructor
    public Home(BorderPane p, Connector c)
    {
        super(p,1000,600);
        rootP = p;
        connector = c;
        
        //load css file
        rootP.getStylesheets().add("sysadmin/ui/css/home.css");
        //css id
        rootP.setId("main");
        
        setUpBar();
    }
    
    public void initList()
    {
        list = new HashMap<>();
        //create css id
        //initialize using db
        //the initialization below is for testing...real stuff coming soon
        list.put("S13/21389/14",new ReadOnly
        ("Mwendwa Reuben", "31772088","S13/21389/14","+254729764597",
                "vrubben@outlook.com","MALE","Science","Computer Sceince"));
        list.put("S13/20445/14",new ReadOnly
        ("Julius Njagwa","31653221","S13/20445/14", "+254788321219","juliusng@gmail.com",
                "MALE","Education & Community Studies","Education"));
    }
    
    //set up buttons
    public void setUpBar()
    {
         nav = new HBox();
         nav.setId("nav");
         
         //create navigation buttons
         nw = new Button("New");
         edit = new Button("Edit");
         all = new Button("View");
         home = new Button("Home");
         
         //great a css class for all buttons
         home.getStyleClass().add("b-button");
         nw.getStyleClass().add("b-button");
         edit.getStyleClass().add("b-button");
         all.getStyleClass().add("b-button");
         
         nav.getChildren().addAll(home,nw,edit,all);
         //navigation bar css id
         nav.setId("nav");
         
         rootP.setTop(nav);
         initList();
         showHome();
         
         home.setOnAction((e)->
         {
             showHome();
         });
         all.setOnAction((e)->
         {
             showList();
         });
         
    }
    
    //show home page
    private void showHome()
    {
        rootP.setCenter(new ScrollPane());
    }
    
    
    
    //show list
    private void showList()
    {
        GridPane lst = new GridPane();
        //create a css id
        lst.setId("list");
        //set header
        lst.add(new Label("NAME"), 0, 0);
        lst.add(new Label("WORK ID"), 1, 0);
        lst.add(new Label("NATIONAL ID"), 2, 0);
        lst.add(new Label("E-MAIL"), 3, 0);
        lst.add(new Label("CELLPHONE"), 4, 0);
        lst.add(new Label("GENDER"), 5, 0);
        lst.add(new Label("DEPARTMENT"), 6, 0);
        lst.add(new Label("FACULTY"), 7, 0);
        
        int row = 1;
        ReadOnly ro = null;
        for(String id : list.keySet())
        {
            ro = list.get(id);
            lst.add(ro.getName(), 0, row);
            lst.add(ro.getWorkId(), 1, row);
            lst.add(ro.getNationalId(), 2, row);
            lst.add(ro.getAddress(), 3, row);
            lst.add(ro.getCellPhone(), 4, row);
            lst.add(ro.getGender(), 5, row);
            lst.add(ro.getDepartment(), 6, row);
            lst.add(ro.getFacultyLabel(), 7, row);
            ++row;
        }
        
        ScrollPane sp = new ScrollPane();
        lst.setAlignment(Pos.CENTER);
        sp.setContent(lst);
        
        rootP.setCenter(sp);
    } 
}
