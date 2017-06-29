
package sysadmin.ui.menu.chat;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import sysadmin.net.msg.Base64Utils;

import sysadmin.net.msg.Message;
import sysadmin.ui.UIManager;
import sysadmin.ui.menu.explore.ExploreWorker;
import sysadmin.ui.menu.explore.Staff;



public class ChatWorker extends SplitPane implements Runnable
{
    //create a toggle group for all contacts
    
    //a vbox
    private VBox contacts;
    private HashMap<String, Contact> cMap;
    //main chat pane
    private final ChatPane bPane;
    //input stream
    private final PipedInputStream pIn;
    //handle
    private Thread handle;
    
    //send button
    private Button send;
    
    public ChatWorker(PipedInputStream in)
    {
        //initialize
        contacts = new VBox();
        cMap = new HashMap<String, Contact>();
        send = new Button("Send");
        bPane = new ChatPane(new TextMsgArea(send));
        pIn = in;
        
        //start thread
        handle = new Thread(this,"chat");
        handle.start();
        //set threads name
        setOrientation(Orientation.HORIZONTAL);
        //css id
        setId("chat-interface");
        contacts.setId("contacts-list");
        
        contacts.setPadding(new Insets(10));
        
        send.setOnAction((e)->{
            Message msg = bPane.getMsg();
            if(msg != null)
                //send
                send(msg);
        });
    }
    
    //create a pipe
    private void popen()
    {
        UIManager.createPipe("chat", pIn);
    }
    //set up ui
    public void setUp()
    {
        contacts.setMaxWidth(200);
        contacts.setMinWidth(200);
        contacts.maxWidthProperty().bind(widthProperty().multiply(0.0));
    }
    
    @Override
    public void run()
    {
        //init staff details
        setUp();
        popen();
        init();
        
        ScrollPane sp = new ScrollPane(contacts);
        sp.setMaxWidth(200);
        sp.setMinWidth(200);
        sp.maxWidthProperty().bind(widthProperty().multiply(0.0));
        
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        Platform.runLater(()->{
            getItems().addAll(sp,bPane);
        });
        
        Thread recvr = new Thread()
        {
            public void run()
            {
                while(true)
                {
                    Message msg = recv();
                    update(msg);
                }
            }
        };
        
        recvr.start();
    }
    
    public void update(Message msg)
    {
        cMap.get(msg.getSrc()).
                addText(new MessageBox(
                        Base64Utils.decode(msg.getPayload()),msg.getDest()));
    }
    
    //initialize contacts
    private void init()
    {
        while(!ExploreWorker.isInit)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        ToggleGroup tg = new ToggleGroup();
        
        ArrayList<Staff> list = new ArrayList<>(ExploreWorker.getList());
        for(Staff st : list)
        {
            Contact c = new Contact(st, "sysadmin/resources/pictures/one.png");
            contacts.getChildren().add(c);
            tg.getToggles().add(c);
            cMap.put(st.getWID(), c);
            
            //set listener
            c.setOnAction((e)->{
                bPane.setChatArea(c.getChatArea());
                bPane.setHeader(c.getFullName());
                ChatPane.setDest(c.getWorkerId());
            });
        }
        
        tg.selectedToggleProperty().addListener(this::changed);
        Contact cc = (Contact)tg.getToggles().get(0);
        cc.setSelected(true);
        
        bPane.setChatArea(new ChatArea(cc.getFullName()));
        bPane.setHeader(cc.getFullName());
    }
    
    //new contact clicked
    private void changed(ObservableValue<? extends Toggle> observable,
            Toggle oB, Toggle nB)
    {
        if(nB != null){
            if(oB != null)
            {//change style
                ((ToggleButton)nB).setStyle("-fx-background-color: #004040; "
                            + "-fx-text-fill : silver");
                ((ToggleButton)oB).setStyle("-fx-background-color: silver; "
                            + "-fx-text-fill : #004040;");
                //System.out.println("here ***1***");
            }
            else{
                ((ToggleButton)nB).setStyle("-fx-background-color: #004040; "
                        + "-fx-text-fill : silver");
                //System.out.println("here 1***");
            }
        }/*else{
            ((ToggleButton)oB).setStyle("-fx-background-color: inherit; "
                        + "-fx-text-fill : silver");*/
            //System.out.println("here 2 ***");
        
    }
    
    
    //send message
    public void send(Message msg)
    {
        UIManager.sendToWorker("sender", msg);
        //System.out.println(msg);
        bPane.clear();
        //add text to pane
        cMap.get(msg.getDest()).
                addText(new MessageBox(Base64Utils.decode(
                        msg.getPayload()),msg.getDest()));
    }
    
    public Message recv()
    {
        byte[] buff = new byte[8*1024];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String EOF;
        while(true)
        {
            Arrays.fill(buff, 0, buff.length, (byte)0);
            try {
                int n = pIn.read(buff, 0, buff.length);
                EOF = new String(buff, n - 3, 3);
                if(EOF.compareTo("END") == 0)
                {
                    stream.write(buff, 0, n - 4);
                    break;
                }
                stream.write(buff, 0, n);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Message(stream.toString());
    }
}
