package shadowstep.easytouch;

import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

/**
 * Created by ShadowStep on 16.05.2018.
 */
enum ClientStatus
{
    OFFLINE,
    CONNECTED,
    CONNECTING
}
public class TouchClient {
    //Fields
    private Socket          client_socket; //main socket
    private OutputStream    output_stream; //stream for sending data

    public  Controller      controller;
    public  ClientStatus    client_status = ClientStatus.OFFLINE;

    private String ip   = "192.168.1.33"; // temp default
    private String port = "7434"; // temp default
    
    //Constructor
    public TouchClient(){
        controller = new Controller();
    }
    //Methods
    public void ConnectToServer() {
        if(client_status == ClientStatus.OFFLINE)
        new Connection().execute(ip, port);
    }
    public void CloseConnection() {
        if(client_status == ClientStatus.CONNECTED) {
            try {
                output_stream.close();
                client_socket.close();
            }catch(Exception ex){}
            client_status = ClientStatus.OFFLINE;
        }
    }
    public void TouchPadEvent(MotionEvent event) {
        SendMessageToServer(controller.TouchAction(event));
    }
    public void KeyboardEvent(int key_code,KeyEvent event){
        SendMessageToServer(controller.KeyboardAction(key_code,event));
    }
    private void SendMessageToServer(byte[] data){
        try{
            output_stream.write(data,0,data.length);
        }
        catch (Exception ex){}

    }
    //Async
    private class Connection extends AsyncTask<String,Void,Socket> {
        @Override
        protected void onPreExecute() {
            client_status = ClientStatus.CONNECTING;
        }
        @Override
        protected Socket doInBackground(String... params) {
            Socket socket = null; //temp socket
            try {
                //@param[0] = ip , @param[1] = port
                socket = new Socket(InetAddress.getByName(params[0]),parseInt(params[1]));
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }
            return socket;

        }
        @Override
        protected void onPostExecute(Socket socket) {
            if(socket != null)
            {
                client_socket = socket;
                try{
                    output_stream = client_socket.getOutputStream(); //stream to sending data
                }
                catch (Exception ex){}
                client_status = ClientStatus.CONNECTED;
            }
            else {
                client_status = ClientStatus.OFFLINE;
                //ERROR CHECK MUST BE HERE!!!
            }
        }
    }

}
