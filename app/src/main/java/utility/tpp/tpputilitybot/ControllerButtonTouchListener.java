package utility.tpp.tpputilitybot;

import android.media.Image;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Joseph on 2/19/2016.
 */
public class ControllerButtonTouchListener implements View.OnTouchListener
{
    ImageButton button;
    NetworkManager network;
    String message;
    int x;
    int y;

    public ControllerButtonTouchListener(ImageButton b,String message_out,NetworkManager network_manager)
    {
        button = b;
        network = network_manager;
        message = message_out;
    }
    public boolean onTouch(View v, MotionEvent event)
    {
        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                Log.i("ButtonPressInfo", "Button Down");
                button.setBackground(MainActivity.getDrawable(MainActivity.context, R.drawable.control_button_pressed));
                x = (int) event.getRawX();
                y = (int) event.getRawY();
            }
            break;
            case MotionEvent.ACTION_MOVE:
            {
                //Log.i("ButtonPressInfo", "Position " + event.getX());
                //setMargins(button,(int) event.getRawX() - x,(int) event.getRawY() - y,0,0);
            }
            break;
            case MotionEvent.ACTION_UP:
            {
                Log.i("ButtonPressInfo", "Button Up");
                button.setBackground(MainActivity.getDrawable(MainActivity.context, R.drawable.control_button));
                network.sendMessage("PRIVMSG #twitchplayspokemon :" + message);
            }
            break;
            case MotionEvent.ACTION_CANCEL:
            {

            }
            break;
            default:
            {

            }
            break;
        }
        return true;
    }
}
