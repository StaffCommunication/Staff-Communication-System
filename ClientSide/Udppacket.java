
package udppacket;

//class controller

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

class Controller 
{
    private Button button;
    private ScheduledExecutorService timer;
    private VideoCapture capture = new VideoCapture();
    //private BorderPane bp;
   public ImageView curFrame = new ImageView();
        public static  AudioFormat audioFormat = new AudioFormat(22050.0F,8,2,true,false); // Get TargetDataLine with that format
       public static  Mixer.Info[] minfoSet = AudioSystem.getMixerInfo();
      public static      Mixer mixer = AudioSystem.getMixer(minfoSet[4]);
      public static  DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
       public static  TargetDataLine line;
       public  int sampleRate = (int) audioFormat.getSampleRate();
      public   int numChannels = audioFormat.getChannels();
      public   int audioBufferSize = sampleRate * numChannels;
      public    byte[] audioBytes;
        public int  bytesRead;
        public  ByteArrayOutputStream out = new ByteArrayOutputStream();
       public AudioInputStream audioInputStream;
    public SourceDataLine sourceDataLine;
    ServerSocket socket;
    Socket sound;
      OutputStream outSound;
    
    public Controller(BorderPane p) throws IOException
    {
        this.audioBytes = new byte[audioBufferSize];
        curFrame.setStyle("-fx-border-color : green");
        p.setCenter(curFrame);
        button = new Button("START");
        timer = Executors.newSingleThreadScheduledExecutor();
      //  socket = new ServerSocket(8888);
//        sound = socket.accept(); // accept a connection from my client
 //     outSound = sound.getOutputStream();
        p.setBottom(button);
        
        button.setOnAction((e)->{
            try {
                //start
                start();
            } catch (InterruptedException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
        public static void SoundListener(){ // This method find a line and opens it 
            if(!AudioSystem.isLineSupported(dataLineInfo)){
            System.out.println("Line is not supported...");
            }
                 try {
                 line = (TargetDataLine)AudioSystem.getLine(dataLineInfo); // obtain the line
                   line.open(audioFormat); //open the lline
                    if(line.isOpen()){
                        System.out.println("Line Open");
                    }  
                    line.start();
               } catch (LineUnavailableException ex) { System.out.println("Line Unavailable"); }
            
}
          public static void closeLine(){
            line.stop();
            line.close();
        }
    //starter
    public void start() throws InterruptedException
    {
        capture.open(0);
        final Sender sender = new Sender();
    
  
        if(capture.isOpened())
        {
            Runnable grabber;
            grabber = new Runnable() {
                @Override
                public void run() {
                    //do stuff
                    Mat m = grab(); // grab camera frames
                        Platform.runLater(() -> {
                        byte[] image= sender.send(m);
                        update(sender,image);
                    });
                 }
            };
                    timer.scheduleAtFixedRate(grabber, 0, 1, TimeUnit.MICROSECONDS);
  //  closeLine();
                    }
            SoundListener(); // open the line
         if( line.isOpen()){
        Thread soundman = new Thread(){
            public void run(){
                
                bytesRead = line.read(audioBytes, 0, audioBytes.length);
                    System.out.println("Audio ByteSize: " +audioBytes.length + "  Number of bytes read: " + bytesRead);
               //     out.write(audioBytes, 0, bytesRead);
               out.write(audioBytes, 0, bytesRead);
                  //  byte audioData[] = out.toByteArray();
            // Get an input stream on the byte array
            // containing the data
            InputStream byteArrayInputStream = new ByteArrayInputStream(audioBytes);
                try {
                    outSound.write(audioBytes);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            
        };
        soundman.start();
     // Thread.sleep(sampleRate);
          timer.scheduleAtFixedRate(soundman, 0, 1, TimeUnit.MILLISECONDS);
    }
            
    }
    
    //grab Mat
    public Mat grab()
    {
        Mat frame = new Mat();
        //read
        capture.read(frame);
        //System.out.println("Frame Grabbed...");
        return frame;
    }
          public void update(Sender image,byte [] src) 
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
class Sender
{
    

      //socket
    DatagramSocket dSocket;
    DatagramSocket dSoundSocket;
    //address
    SocketAddress server;
    SocketAddress client;
   
    
    
    public Sender()
    {
        try {
            client = new InetSocketAddress("127.0.0.1", 8888);
            dSocket = new DatagramSocket(client);
//            dSoundSocket = new DatagramSocket(client);
            server  = new InetSocketAddress("127.0.0.1", 8889);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //send packet
    public byte[] send(Mat frame) //pass in frames grabbed from the camera.
    {
        //get bytes
        int pktSize = 65507; //maximum UDP packet size.
        int sendSize = pktSize;
        int  w = frame.width(), h = frame.height(), c = frame.channels(); 
        int len = w*h*c; //about 900kb(921600)
        //bytes
        byte[] data = new byte[w*h*c];
        frame.get(0, 0, data); //get the frame data a copy it to the byte array data.
        //send
        byte[] buffer = new byte[pktSize]; //create a buffer byte array of 65507bytes.
        int sendBytes = 0;
        boolean over = false;
        
        byte t = 1;
        while(!over)
        {
            Arrays.fill(buffer,0,buffer.length,(byte)0); // initialise buffer with zeroes
           
            //
            buffer[0] = t; // first element used as an index. 
            if(len - sendBytes < pktSize) 
            {
                System.arraycopy(data, sendBytes, buffer, 1, (len - sendBytes));
                sendSize = (len - sendBytes);
                over = true;
                //System.out.println(sendBytes);
            }else{
                System.arraycopy(data, sendBytes, buffer, 1, pktSize - 1);
                sendBytes += pktSize;
            }
            //
            try {
                
                dSocket.send(new DatagramPacket(buffer, sendSize, server)); // send the datagram packet.
      
                System.out.println(t + " " + sendBytes + " " + sendSize);
            } catch (Exception e) {
            }
            ++t;
        }//while
        System.out.println();
        return data;
    }
    
  
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


public class Udppacket  extends Application{

  
    

    @Override
    public void start(Stage stage) throws Exception {
         BorderPane rootP = new BorderPane();
        Scene scene = new Scene(rootP, 500,500);
        stage.setScene(scene);
        stage.show();
        
        Controller con = new Controller(rootP);
    }
    public static void main(String[] args) {
         //native libs
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
      launch(args);
    }
}
