
package sysadmin.ui.scenes;

import java.io.PipedOutputStream;
import java.util.HashMap;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import sysadmin.net.Connector;
import sysadmin.ui.menu.Account;
import sysadmin.ui.menu.Call;
import sysadmin.ui.menu.Chat;
import sysadmin.ui.menu.Events;


public class Home extends Scene
{
    //layout manager
    private final SplitPane rootP;
    private final BorderPane bPane;
    //connector
    private final Connector connector;
    
    //side bar
    private VBox nav;
    
    //piped output streams
    private static HashMap<String, PipedOutputStream> ipcs;
    //user work ID
    private static String wId;

    //constructor
    public Home(SplitPane p)
    {
        super(p,1000,600);
        rootP = p;
        connector = null;
        bPane = new BorderPane();
        nav = new VBox();
        nav.setId("nav");
        bPane.setId("content-pane");
        
        Home.ipcs = new HashMap<>();
        
        //load css file
        rootP.getStylesheets().add("sysadmin/ui/css/home.css");
        //css id
        rootP.setId("main");
       
    }
    
    //set up nav basr
    public void setUp()
    {
        Chat cb = new Chat();
        Events eb = new Events();
        Call avb = new Call();
        Account ab = new Account();
        
        
        nav.getChildren().addAll(ab,cb,avb,eb);
        nav.setAlignment(Pos.CENTER);

        nav.setMaxWidth(120);
        nav.setMinWidth(120);
        nav.maxWidthProperty().bind(rootP.widthProperty().multiply(0.0));
        rootP.getItems().addAll(nav, bPane);
        
        setContent(ab.getAccountPane());
        //event listeners
        addEventListeners(cb,eb,avb,ab);
    }
    
    //add event listeners
    private void addEventListeners(final Chat c,
           final Events ev, final Call a, final Account ab)
    {
        //add event listeners to the toggle buttons
        //keep the buttons in a toggle group
        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(c,ev,a,ab);
        
        //load chat interface
        c.setOnAction((e)->{
            //get the content pane pointed to by this toggle button
            //set it as the content pane
            setContent(c.getChatPane());
        });
 
        //events / news feed interface
        ev.setOnAction((e)->{
            //switch to the events pane
            setContent(new Pane());
        });
        //call
        a.setOnAction((e)->{
            setContent(new Pane());
        });
        
        //account
        ab.setOnAction((e)->{
            setContent(ab.getAccountPane());
        });
        
        //handle a toggle change event
        group.selectedToggleProperty().addListener(this::switched);
        ab.setSelected(true);
    }
    
    private void switched(ObservableValue<? extends Toggle> observable,
            Toggle oB, Toggle nB)
    {
        if(nB != null){
            if(oB != null)
            {//change style
                ((ToggleButton)nB).setStyle("-fx-background-color: silver; "
                            + "-fx-text-fill : #222");
                ((ToggleButton)oB).setStyle("-fx-background-color: inherit; "
                            + "-fx-text-fill : silver;");
                //System.out.println("here ***1***");
            }
            else{
                ((ToggleButton)nB).setStyle("-fx-background-color: silver; "
                        + "-fx-text-fill : #222");
                //System.out.println("here 1***");
            }
        }else{
            ((ToggleButton)oB).setStyle("-fx-background-color: inherit; "
                        + "-fx-text-fill : silver");
            //System.out.println("here 2 ***");
        }
    }
    
    //set content pane
    private void setContent(Node node)
    {
        bPane.setCenter(node);
    }
    
    //set work id
    public static void setWorkId(String id)
    {
        wId = id;
    }
    
    //get the work id
    public static String getWorkId()
    {
        return wId;
    }
}//Home class
