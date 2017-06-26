package loginui;



import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.geometry.Orientation;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;


public class MainUI extends StackPane {
SplitPane splitMain=new SplitPane();
SplitPane splitHor=new SplitPane();
ScrollPane topScroll=new ScrollPane();
ScrollPane bottScroll=new ScrollPane();
ScrollPane scroll2=new ScrollPane();

    public MainUI() {
        final FileChooser fileChooser= new FileChooser();  //the class to getfiles
                 
                 BorderPane border=new BorderPane();
                 MenuBar menuBar=new MenuBar();
                 menuBar.setId("menu");
                //MENU 1
                Menu home=new Menu("Home");
                //menu items
                MenuItem itmNew=new javafx.scene.control.MenuItem("New");
                //menu 2
                Menu about=new Menu("About Us");
                //menu items
                MenuItem itmCut=new javafx.scene.control.MenuItem("Cut");
                //menu3
                Menu help=new Menu("Help");
                //menu items
                MenuItem itmAboutUs=new javafx.scene.control.MenuItem("About Us");
                //submenu
                Menu subSearch=new Menu("Search");
                Region spacer=new Region();
                spacer.getStyleClass().add("menu-bar");
                //menuItems
                MenuItem itmFind=new javafx.scene.control.MenuItem("Find...");
                home.getItems().addAll(itmNew);
                about.getItems().addAll(itmCut);
                help.getItems().addAll(itmAboutUs);
                subSearch.getItems().addAll(itmFind);
                about.getItems().add(subSearch);
        
                menuBar.getMenus().addAll(home,about,help );
                menuBar.setStyle("-fx-padding:10 0 10 5");
                menuBar.setMinHeight(40);
                border.setTop(menuBar);
        
       
        Platform.runLater(new Runnable() {
               @Override
               public void run() {
               splitMain.setDividerPositions(0.30f, 0.70f);
                }
            });
        menuBar.getStylesheets().addAll(MainUI.class.getResource("Style.css").toExternalForm());
        
        Platform.runLater(new Runnable() {
               @Override
               public void run() {
               splitHor.setDividerPositions(0.6f, 0.4f);
                }
            });
        splitHor.getItems().addAll(topScroll,bottScroll); 
        splitHor.setOrientation(Orientation.VERTICAL);
        splitHor.getStylesheets().addAll(MainUI.class.getResource("Style.css").toExternalForm());        
        
        leftScroll();
        RightPane right=new RightPane();
        setPadding(new Insets(0, 30, 30, 30)); 
    
        topScroll.setContent(leftScroll());
        scroll2.setContent(right.rightScroll());
        
        border.setCenter(splitMain);
        getChildren().addAll(border);
        
       splitMain.getItems().addAll(splitHor,scroll2);  
        
                   
        
    }
   
    public VBox leftScroll(){
                TextField search=new TextField();
                search.setId("textField");
                search.setPrefWidth(200);
                search.setPrefHeight(40);
                search.setPromptText("Search a contact");
                search.setFocusTraversable(false);
                
                //if(search.getText()==)
                HBox hbox1=new HBox(10,search);
                hbox1.setMargin(search, new javafx.geometry.Insets(10, 10, 20, 10));
                //scroll1.setContent(hbox1);
                
                
                    String imagePath = "/images/icons/user-64.png";
                    // Scale the iamge to 200 X 100
                    double requestedWidth = 30;
                    double requestedHeight = 30;
                    boolean preserveRatio = true;
                    boolean smooth = true;
                    Image img = new Image(imagePath,
                    requestedWidth,
                    requestedHeight,
                    preserveRatio,
                    smooth);
                ImageView pIcon=new ImageView(img);
                Label people=new Label("People");
                HBox hbox2=new HBox(10,pIcon,people);
                //hbox2.getChildren().addAll(pIcon,people);
                hbox2.setPadding(new javafx.geometry.Insets(5,12,15,10));
                
                    String imagePath2 = "images/icons/time-64.png";
                    // Scale the iamge to 200 X 100
                    double requestedWidth2 = 30;
                    double requestedHeight2 = 30;
                    boolean preserveRatio2 = true;
                    boolean smooth2 = true;
                    Image img2 = new Image(imagePath2,
                    requestedWidth2,
                    requestedHeight2,
                    preserveRatio2,
                    smooth2);
                ImageView rIcon=new ImageView(img2);
                Label recent=new Label("Recent");
                HBox hbox3=new HBox(10,rIcon,recent);
                hbox3.setPadding(new javafx.geometry.Insets(5,12,15,10));
                
                
                String imagePath3 = "/images/icons/common-calendar-month-outline-stroke-64.png";
                    // Scale the iamge to 200 X 100
                    double requestedWidth3 = 30;
                    double requestedHeight3 = 30;
                    boolean preserveRatio3 = true;
                    boolean smooth3 = true;
                    Image img3 = new Image(imagePath3,
                    requestedWidth3,
                    requestedHeight3,
                    preserveRatio3,
                    smooth3);
                ImageView eIcon=new ImageView(img3);
                Label events=new Label("Events");
                HBox hbox4=new HBox(10,eIcon,events);
                hbox4.setPadding(new javafx.geometry.Insets(5,12,15,10));
                
                
                
                String imagePath4 = ("/images/icons/Group_of_People-64.png");
                    // Scale the iamge to 200 X 100
                    double requestedWidth4 = 40;
                    double requestedHeight4 = 40;
                    boolean preserveRatio4 = true;
                    boolean smooth4 = true;
                    Image img4 = new Image(imagePath4,
                    requestedWidth4,
                    requestedHeight4,
                    preserveRatio4,
                    smooth4);
                ImageView wIcon=new ImageView(img4);
                Label workgroups=new Label("Workgroups");
                HBox hbox5=new HBox(10,wIcon,workgroups);
                hbox5.setPadding(new javafx.geometry.Insets(5,12,15,10));
               
                final Separator sepHor = new Separator();
                sepHor.setOrientation(Orientation.HORIZONTAL);
                
               
                hbox2.setOnMouseClicked(e -> {
                    bottScroll.setContent(CreatedUsers.displayAll());
                    
    
                });
                hbox4.setOnMouseClicked(e -> {
                   scroll2.setContent(Table.table1());
    
                });
                                
                VBox vbox=new VBox(20,hbox1,hbox2,hbox3,hbox4,hbox5);
            return vbox;
            
            
                
                
    }
   
}

/*hbox2.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    System.out.println("Enter Pressed");
                }
            }
        });*/
                

/*hbox2.setOnMouseEntered(new EventHandler<MouseEvent>() {
     @Override
     public void handle(MouseEvent t) {
          Node  node =(Node)t.getSource();
          
        }
    });*/