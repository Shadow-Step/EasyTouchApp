package shadowstep.easytouch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    //Fields
    TouchClient touch_client;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(touch_client.client_status == ClientStatus.CONNECTED) {
            touch_client.TouchPadEvent(event);
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        if(touch_client.client_status == ClientStatus.CONNECTED) {
            touch_client.CloseConnection();
        }
        super.onDestroy();
        //getDelegate().onDestroy();
    }

    //Events
    public void ConnectToServer(View view) {
        touch_client.ConnectToServer();
    }
    public void DisconnectFromServer(View view) {
        touch_client.CloseConnection();
    }
    //Just comment added
}
