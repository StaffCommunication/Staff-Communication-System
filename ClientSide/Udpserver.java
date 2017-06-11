/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpserver;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import org.opencv.core.Core;


class Receiver extends Thread
{
    //display frame here
    private ImageView curFrame;
    //socket to connect to the client
    private DatagramSocket sSocket;
    //frame size
    private Socket soundSocket;
   public AudioInputStream audioInputStream;
      public static  AudioFormat audioFormat = new AudioFormat(22050.0F,8,2,true,false); // Get TargetDataLine with that format
       public  int sampleRate = (int) audioFormat.getSampleRate();
      public   int numChannels = audioFormat.getChannels();
      public   int audioBufferSize = sampleRate * numChannels;
          public SourceDataLine sourceDataLine;
    private final int MAX_SIZE = 921600;

    Utils Image = new Utils();
    public Receiver(BorderPane bp)
    {
        curFrame = new ImageView();
        curFrame.setStyle("-fx-border-color : green");
        bp.setCenter(curFrame);
        
        //prepare socket
        try {
            sSocket = new DatagramSocket( new InetSocketAddress("127.0.0.1", 8889)); // create a datagram socket for receiving packets 
            soundSocket = new Socket("127.0.0.1",9999);
        } catch (SocketException socketException) {
           socketException.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void recv() throws IOException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int len = 0;
        
        final byte[] buffer = new byte[65507]; // set up the packet.
         byte[] imagedata = new byte[1048112];
         //ByteBuffer target = ByteBuffer.wrap(imagedata);
     //    ByteArrayOutputStream oStream = new ByteArrayOutputStream();
         byte cnst = 1;
        //DatagramPacket udp = new DatagramPacket(buffer, buffer.length);
         
        InputStream byteArrayInputStream =soundSocket.getInputStream();
          audioInputStream = new AudioInputStream(byteArrayInputStream,audioFormat,audioBufferSize);
         DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
           int cnt = 0;
                    
        try {
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
              sourceDataLine.open(audioFormat);
              sourceDataLine.start();
                 byte tempBuffer[] = new byte[10000];
                  while ((cnt = audioInputStream.read(tempBuffer, 0,tempBuffer.length)) != -1) {
                    if (cnt > 0) {
                        // Write data to the internal buffer of
                        // the data line where it will be
                        // delivered to the speaker.
                        sourceDataLine.write(tempBuffer, 0, cnt);
                    }// end if
                }
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }
           sourceDataLine.drain();
         sourceDataLine.close();            
                        
        while(true)
        {
            //receive all video frame fragments, we fragmented it
            stream.reset();
            while(len < MAX_SIZE)
            {
              
           
               Arrays.fill(buffer, 0, buffer.length, (byte)0);
                DatagramPacket udp = new DatagramPacket(buffer, buffer.length);
               
                 
                //try {
                    sSocket.receive(udp); // wait to receive packet
         //   target.put(buffer, len, buffer.length);
     
          // len += udp.getLength(); // increment the lenght.
         //     if(buffer[0] == cnst){
         //         oStream.write(buffer, 1, buffer.length - 1);
                  ++cnst;
                  len += udp.getLength();
            //  }else{
              //   cnst = 1;
              //    len = 0;
                 /// oStream.reset();
              //}
                System.arraycopy(buffer,1, imagedata, len, buffer.length-1); 
   
                 System.out.println(buffer[0] + " " + len +
                            " " + udp.getLength());
                 }
            
        //     System.arraycopy(buffer,0, imagedata,len, buffer.length); 
         // Arrays.fill(imagedata,0,imagedata.length,(byte)0); // initialise buffer with zeroes//clear
          System.out.println();
            update( Image ,imagedata);  
         //     System.out.println( imagedata.length);
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

   
    public void update(Utils image,byte [] src) 
    {
        Task task = new Task(){
   

            @Override
            protected Object call() throws Exception {
                 curFrame.imageProperty().set(image.mat2Image(src));
                return null;
            }
   
        }; 
     Thread th = new Thread(task);
     th.setDaemon(true);
    th.start();
  //  System.out.println(task.isRunning());
    }
}
//utils ==> conversions take place here
class Utils{
    
    public  Image mat2Image(byte [] src)
    {
        try{
            return SwingFXUtils.toFXImage(matToBufferedImage(src), null);
        }catch(Exception e)
        {
            System.err.println("Cannot convert Mat object : " + e);
            return null;
        }
    }
 /*   
    public static <T> void onFXThread(final ObjectProperty<T> property ,
            final T value)
    {
        Platform.runLater(() -> {
            property.set(value);
        });
    }
    */
    public static BufferedImage matToBufferedImage(byte [] src)
    {
        BufferedImage image = new BufferedImage(
                640,480, BufferedImage.TYPE_3BYTE_BGR);
        
        try {
            final byte [] targetPixels = 
                ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            
            System.arraycopy(src, 0, targetPixels, 0, 921600);
        } catch (Exception e) {
            System.out.println("herererererer");
        }
       
        return image;
    }
}
public class Udpserver  extends Application{

   

    @Override
    public void start(Stage stage) throws Exception {
     BorderPane rootPane = new BorderPane();
        Receiver rcvr = new Receiver(rootPane);
        
        Scene scene = new Scene(rootPane, 600, 600);
        //stage set up
        stage.setScene(scene);
        stage.show();
        
        rcvr.start();
    }
    
    
    
    
     public static void main (String[] args) {
          //load native libs
       System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
       //launch app
        launch(args);
    }
}
