
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
import sysadmin.ui.menu.AudioCallButton;
import sysadmin.ui.menu.ChatButton;
import sysadmin.ui.menu.EventsButton;
import sysadmin.ui.menu.ExploreButton;
import sysadmin.ui.menu.VideoCallButton;


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
       
        setUp();
    }
    
    //set up nav basr
    public void setUp()
    {
        ChatButton cb = new ChatButton(new PipedOutputStream());
        ExploreButton xb = new ExploreButton();
        EventsButton eb = new EventsButton();
        VideoCallButton vb = new VideoCallButton();
        AudioCallButton ab = new AudioCallButton();
        
        nav.getChildren().addAll(xb,cb,ab,vb,eb);
        nav.setAlignment(Pos.CENTER);

        nav.setMaxWidth(120);
        nav.setMinWidth(120);
        nav.maxWidthProperty().bind(rootP.widthProperty().multiply(0.0));
        rootP.getItems().addAll(nav, bPane);
        
        //event listeners
        addEventListeners(cb, xb, eb, vb, ab);
    }
    
    //add event listeners
    private void addEventListeners(final ChatButton c,final ExploreButton ex,
           final EventsButton ev, final VideoCallButton v,final AudioCallButton a)
    {
        //add event listeners to the toggle buttons
        //keep the buttons in a toggle group
        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(c,ex,ev,v,a);
        
        //load chat interface
        c.setOnAction((e)->{
            //get the content pane pointed to by this toggle button
            //set it as the content pane
            setContent(c.getChatPane());
        });
        
        //explorer button action
        ex.setOnAction((e)->{
            //set the list explorer
            setContent(new Pane());
        });
        
        //events / news feed interface
        ev.setOnAction((e)->{
            //switch to the events pane
            setContent(new Pane());
        });
        
        //video call
        v.setOnAction((e)->{
            setContent(new Pane());
        });
        
        //audio call
        a.setOnAction((e)->{
            setContent(new Pane());
        });
        
        //handle a toggle change event
        group.selectedToggleProperty().addListener(this::switched);
        ex.setSelected(true);
    }
    
    private void switched(ObservableValue<? extends Toggle> observable,
            Toggle oB, Toggle nB)
    {
        if(nB != null){
            //change style
            ((ToggleButton)nB).setStyle("-fx-background-color:a0c0c0; "
                    + "-fx-text-fill : #222");
        }
        if(oB != null){
            ((ToggleButton)oB).setStyle("-fx-background-color:c0c0c0; "
                    + "-fx-text-fill : green");
        }
    }
    
    //set content pane
    private void setContent(Node node)
    {
        bPane.setCenter(node);
    }

}//Home class
