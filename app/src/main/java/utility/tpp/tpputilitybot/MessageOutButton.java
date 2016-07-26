package utility.tpp.tpputilitybot;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Joseph on 8/24/2015.
 */
public class MessageOutButton implements View.OnTouchListener {
    protected NetworkManager manager;
    String message;
    MessageOutButton(String message_out,NetworkManager network_manager)
    {
        message = message_out;
        manager = network_manager;
    }
    public void onClick(View v)
    {
        manager.sendMessage("PRIVMSG #twitchplayspokemon :" + message);
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                Log.i("ButtonPressInfo", "Button Down");
                //button.setBackground(MainActivity.getDrawable(MainActivity.context, R.drawable.advance_button_pressed));
                //x = (int) event.getRawX();
                //y = (int) event.getRawY();
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
                //button.setBackground(MainActivity.getDrawable(MainActivity.context, R.drawable.advance_button));
                manager.sendMessage("PRIVMSG #twitchplayspokemon :" + message);
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
