import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Joseph Sang on 5/22/2017.
 */
public class ClientView implements Initializable{
    @FXML
    private TextArea inbx_txt, outbx_txt;
    @FXML
    private Label status_txt;
    @FXML
    private Button send_btn;
    private BufferedReader reader;
    private PrintWriter writer;
    Socket sock;
    public ClientView(){}
    public void initialize(URL url, ResourceBundle resources){
        getConnection();
        Thread readerThread=new Thread(new IncomingReader());
        readerThread.start();
    }
    public void getConnection(){
        try {
            sock=new Socket("127.0.0.1", 5000);
            InputStreamReader streamReader=new InputStreamReader(sock.getInputStream());
            reader=new BufferedReader(streamReader);
            writer=new PrintWriter(sock.getOutputStream());
            status_txt.setText("Connection Established");
        }catch (IOException ioe){
            status_txt.setText("Error connecting to server");
            send_btn.setDisable(true);
            return;
        }
    }
    @FXML
    private void send_msg(){
        if(outbx_txt.getText().equals("")){
            status_txt.setText("Please type a message");
            return;
        }else{
            writer.println(outbx_txt.getText());
            writer.flush();
            outbx_txt.clear();
        }
    }
    public void append_msg(String msg_to_append){
        inbx_txt.setText(inbx_txt.getText()+"\n"+msg_to_append);
    }
    class IncomingReader implements Runnable{
        public void run(){
            String message;
            try {
                while ((message=reader.readLine())!=null){
                    append_msg(message);
                }
            }catch (Exception e) {
                if(!status_txt.getText().equals("Error connecting to server")){
                    status_txt.setText("Error fetching message");
                }
            }
        }
    }
}
