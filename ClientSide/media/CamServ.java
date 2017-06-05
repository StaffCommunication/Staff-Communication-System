
package server;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;

class Receiver extends Thread
{
    //display frame here
    private ImageView curFrame;
    //socket
    private DatagramSocket sSocket;
    //frame size
    private final int MAX_SIZE = 921600;
    
    public Receiver(BorderPane bp)
    {
        curFrame = new ImageView();
        curFrame.setStyle("-fx-border-color : green");
        bp.setCenter(curFrame);
        
        //prepare socket
        try {
            sSocket = new DatagramSocket(
                    new InetSocketAddress("127.0.0.1", 8889));
        } catch (Exception e) {
        }
    }
    
    public void recv() throws IOException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int len = 0;
        final byte[] buffer = new byte[65507];
        //DatagramPacket udp = new DatagramPacket(buffer, buffer.length);
        while(true)
        {
            //receive all video frame fragments, we fragmented it
            stream.reset();
            while(len < MAX_SIZE)
            {
                //clear
                Arrays.fill(buffer, 0, buffer.length, (byte)0);
                DatagramPacket udp = new DatagramPacket(buffer, buffer.length);
                //try {
                    sSocket.receive(udp);
                    len += udp.getLength();

                    System.out.println(buffer[0] + " " + len +
                            " " + udp.getLength());
                /*} catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
            System.out.println();
            len = 0;
        }
        
    }
    
    //run
    @Override
    public void run()
    {
        try {
            this.recv();
        } catch (Exception e) {
        }
    }
    
    public void update()
    {
        
    }
}

//utils ==> conversions take place here
class Utils{
    
    public static Image mat2Image(byte [] src)
    {
        try{
            return SwingFXUtils.toFXImage(matToBufferedImage(src), null);
        }catch(Exception e)
        {
            System.err.println("Cannot convert Mat object : " + e);
            return null;
        }
    }
    
    public static <T> void onFXThread(final ObjectProperty<T> property ,
            final T value)
    {
        Platform.runLater(() -> {
            property.set(value);
        });
    }
    
    public static BufferedImage matToBufferedImage(byte [] src)
    {
        BufferedImage image = new BufferedImage(
                640,480, BufferedImage.TYPE_3BYTE_BGR);
        
        try {
            final byte [] targetPixels = 
                ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            
            System.arraycopy(src, 0, targetPixels, 0, src.length);
        } catch (Exception e) {
            System.out.println("herererererer");
        }
       
        return image;
    }
}


public class Server extends Application
{
    @Override
    public void start(Stage stage)
    {
        BorderPane rootPane = new BorderPane();
        Receiver rcvr = new Receiver(rootPane);
        
        Scene scene = new Scene(rootPane, 600, 600);
        //stage set up
        stage.setScene(scene);
        stage.show();
        
        rcvr.start();
    }
    public static void main(String[] args) {
       //load native libs
       System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
       //launch app
        launch(args);
    }
}
