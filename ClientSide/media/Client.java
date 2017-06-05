
package client;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

//class controller
class Controller 
{
    private Button button;
    private ScheduledExecutorService timer;
    private VideoCapture capture = new VideoCapture();
    //private BorderPane bp;
    
    public Controller(BorderPane p)
    {
        button = new Button("START");
        timer = Executors.newSingleThreadScheduledExecutor();
        
        p.setBottom(button);
        
        button.setOnAction((e)->{
            //start
            start();
        });
    }
    //starter
    public void start()
    {
        capture.open(0);
        final Sender sender = new Sender();
        if(capture.isOpened())
        {
            Runnable grabber = new Runnable() {
                @Override
                public void run() {
                    //do stuff
                    Mat m = grab();
                    
                    Platform.runLater(()->{
                        sender.send(m);
                    });
                }
            };
            timer.scheduleAtFixedRate(grabber, 0, 1, TimeUnit.MILLISECONDS);
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
}


class Sender
{
    //socket
    DatagramSocket dSocket;
    //address
    SocketAddress server;
    SocketAddress client;
    
    public Sender()
    {
        try {
            client = new InetSocketAddress("127.0.0.1", 8888);
            dSocket = new DatagramSocket(client);
            server  = new InetSocketAddress("127.0.0.1", 8889);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //send packet
    public void send(Mat frame)
    {
        //get bytes
        int pktSize = 65507;
        int sendSize = pktSize;
        int  w = frame.width(), h = frame.height(), c = frame.channels();
        int len = w*h*c;
        //bytes
        byte[] data = new byte[w*h*c];
        frame.get(0, 0, data);
        //send
        byte[] buffer = new byte[pktSize];
        int sendBytes = 0;
        boolean over = false;
        
        byte t = 1;
        while(!over)
        {
            Arrays.fill(buffer,0,buffer.length,(byte)0);
            //
            buffer[0] = t;
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
                dSocket.send(new DatagramPacket(buffer, sendSize, server));
                System.out.println(t + " " + sendBytes + " " + sendSize);
            } catch (Exception e) {
            }
            ++t;
        }//while
        System.out.println();
    }
}

public class Client extends Application
{
    @Override
    public void start(Stage stage)
    {
        BorderPane rootP = new BorderPane();
        Scene scene = new Scene(rootP, 500,500);
        stage.setScene(scene);
        stage.show();
        
        Controller con = new Controller(rootP);
    }

    public static void main(String[] args) {
        //native libs
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //launch
        launch(args);
    }
}
