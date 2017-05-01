
package clientside.media;


import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

class CamFeed
{
    private Socket sock;
    private ImageView iView;
    private Button con;

    public CamFeed(BorderPane pane) {
        iView = new ImageView();
        con = new Button("connect");
        pane.setCenter(iView);
        pane.setBottom(con);
        con.setOnAction((e)->{
            handle();
        });
    }
    
    public void handle()
    {
        ScheduledExecutorService timer;
        try {
            sock = new Socket("192.168.43.159",8888);
            sock.setReceiveBufferSize(64*1024);
            InputStream in = sock.getInputStream();
            
            //grab every frame on a different thread
            Runnable grabber = new Runnable()
            {
                @Override
                public void run()
                {
                    BufferedImage bIm;
                    bIm = grab(in);
                    //iView.imageProperty().set(SwingFXUtils.toFXImage(bIm, null));
                    update(SwingFXUtils.toFXImage(bIm, null));
                }
            };
            
            //timer, intervals of 1 millisecond
            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(grabber, 0, 1, TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            System.out.println("can not connect to server...");
        }
    }
    
    //update
    public void update(final Image im)
    {
        Platform.runLater(()->{
            //use another thread to update view
            iView.imageProperty().set(im);
        });
    }
    
    //grab image buffer
    public BufferedImage grab(InputStream in)
    {
        BufferedImage bi =
                new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        byte[] buff =
                ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        byte[] copy = new byte[16*1024];//64kbs
        int offset = 0, rcvd;
        
        while(true)
        {
            Arrays.fill(copy,(byte)0);
            if(offset == (900*1024)) break;
            
            try {
                    rcvd = in.read(copy, 0, copy.length);
                    System.arraycopy(copy, 0, buff, offset, rcvd);
                    offset += rcvd;
            } catch (Exception e) {
                break;
            }
        }
        return bi;
    }
}


public class ClientCam extends Application 
{
    @Override
    public void start(Stage ps) {
        
        //pane
        BorderPane p = new BorderPane();
        Scene sc = new Scene(p,600,600);
        
        CamFeed cf = new CamFeed(p);
        
        ps.setScene(sc);
        ps.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
