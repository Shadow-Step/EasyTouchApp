package shadowstep.easytouch;

import android.view.KeyEvent;
import android.view.MotionEvent;

import java.nio.ByteBuffer;

/**
 * Created by ShadowStep on 17.05.2018.
 */

public class Controller {
    //Fields
    private int     data_size   = 13;

    private int     action      = 2;
    private float   sensitivity = 1;
    private float   memory_x;
    private float   memory_y;

    private float   pos_x;
    private float   pos_y;

    //Constructor
    public void Contoller(){}
    //Methods
    public byte[] TouchAction(MotionEvent event){
        action = event.getAction();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                memory_x = event.getX();
                memory_y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                pos_x = (event.getX() - memory_x) * sensitivity;
                pos_y = (event.getY() - memory_y) * sensitivity;
                memory_x = event.getX();
                memory_y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                memory_x = 0;
                memory_y = 0;
                break;
            default:
                break;
        }
        ByteBuffer data = ByteBuffer.allocate(data_size);
        data.put((byte)0);
        data.putInt(action);
        data.putInt((int)pos_x);
        data.putInt((int)pos_y);
        return data.array();
    }
    public byte[] KeyboardAction(int key_code, KeyEvent event){
        ByteBuffer data = ByteBuffer.allocate(data_size);
        data.put((byte)1);
        data.putInt(event.getAction());
        data.putInt(key_code);
        data.putInt(event.getMetaState());
        return data.array();
    }
    public void SetSensitivity(int value) {
        if (value == 0)
            value = 1;
        sensitivity = (float) value / 10;
    }
}
