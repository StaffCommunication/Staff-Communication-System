package loginui;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;


public class RightPane {

    
         public StackPane rightScroll(){
        
        String imagePathCall = "/images/icons/11_function-64.png";
                    // Scale the iamge to 200 X 100
                    double requestedWidthCall = 50;
                    double requestedHeightCall = 50;
                    boolean preserveRatioCall = true;
                    boolean smoothCall = true;
                    Image call = new Image(imagePathCall,
                    requestedWidthCall,
                    requestedHeightCall,
                    preserveRatioCall,
                    smoothCall);
                ImageView callView=new ImageView(call);
                               
                String imagePathPause = "/images/icons/ic_pause_circle_fill_48px-64.png";
                    // Scale the iamge to 200 X 100
                    double requestedWidthPause = 40;
                    double requestedHeightPause = 40;
                    boolean preserveRatioPause = true;
                    boolean smoothPause = true;
                    Image pause = new Image(imagePathPause,
                    requestedWidthPause,
                    requestedHeightPause,
                    preserveRatioPause,
                    smoothPause);
                ImageView pauseView=new ImageView(pause);
                            
                HBox icons=new HBox();
                icons.setPadding(new javafx.geometry.Insets(5,12,15,50));
                String imagePathRecord = "/images/icons/icon-mic-a-64.png";
                    // Scale the iamge to 200 X 100
                    double requestedWidthRecord = 40;
                    double requestedHeightRecord = 40;
                    boolean preserveRatioRecord = true;
                    boolean smoothRecord = true;
                    Image record = new Image(imagePathRecord,
                    requestedWidthRecord,
                    requestedHeightRecord,
                    preserveRatioRecord,
                    smoothRecord);
                ImageView recordView=new ImageView(record);
                               
                String imagePathVideo = "/images/icons/icons_video-64.png";
                    // Scale the iamge to 200 X 100
                    double requestedWidthVideo = 40;
                    double requestedHeightVideo = 40;
                    boolean preserveRatioVideo = true;
                    boolean smoothVideo = true;
                    Image video = new Image(imagePathVideo,
                    requestedWidthVideo,
                    requestedHeightVideo,
                    preserveRatioVideo,
                    smoothVideo);
                ImageView videoView=new ImageView(video);
                               
                String imagePathShare = "/images/icons/139-64.png";
                    // Scale the iamge to 200 X 100
                    double requestedWidthShare = 40;
                    double requestedHeightShare = 40;
                    boolean preserveRatioShare = true;
                    boolean smoothShare = true;
                    Image share = new Image(imagePathShare,
                    requestedWidthShare,
                    requestedHeightShare,
                    preserveRatioShare,
                    smoothShare);
                ImageView shareView=new ImageView(share);
                               
                String imagePathAddPerson = "/images/icons/add_user-64.png";
                    // Scale the iamge to 200 X 100
                    double requestedWidthAddPerson = 40;
                    double requestedHeightAddPerson = 40;
                    boolean preserveRatioAddPerson = true;
                    boolean smoothAddPerson = true;
                    Image addPerson = new Image(imagePathAddPerson,
                    requestedWidthAddPerson,
                    requestedHeightAddPerson,
                    preserveRatioAddPerson,
                    smoothAddPerson);
                ImageView addPersonView=new ImageView(addPerson);
                                            
                String imagePathText = "/images/icons/messages-64.png";
                    // Scale the iamge to 200 X 100
                    double requestedWidthText = 40;
                    double requestedHeightText = 40;
                    boolean preserveRatioText = true;
                    boolean smoothText = true;
                    Image text = new Image(imagePathText,
                    requestedWidthText,
                    requestedHeightText,
                    preserveRatioText,
                    smoothText);
                ImageView textView=new ImageView(text);
                
                String imagePathFile = "/images/icons/send-64.png";
                    // Scale the iamge to 200 X 100
                    double requestedWidthFile = 40;
                    double requestedHeightFile = 40;
                    boolean preserveRatioFile = true;
                    boolean smoothFile = true;
                    Image file = new Image(imagePathFile,
                    requestedWidthFile,
                    requestedHeightFile,
                    preserveRatioFile,
                    smoothFile);
                ImageView fileView=new ImageView(file);
                
                HBox iconList=new HBox(15,recordView,videoView,shareView,addPersonView,textView,fileView,callView,pauseView);
                iconList.setPadding(new javafx.geometry.Insets(5,12,15,10));
                
                
                final Separator sepHor = new Separator();
                sepHor.setOrientation(Orientation.HORIZONTAL);
                
                
                StackPane stack=new StackPane(); 
                stack.getChildren().add(iconList);
                
                /*recordView.setOnMouseClicked(e -> {
                //if (e.getCode() == KeyCode.ENTER) {
                TextArea messages=new TextArea();
                messages.setEditable(false);
                TextField typing=new TextField();
                VBox msgVbox =new VBox(5,messages,typing);
        //return msgVbox;
                System.out.println("It works!");
    
});*/
        return stack;
    }
         
         
    }
    

