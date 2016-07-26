package utility.tpp.tpputilitybot;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by joseph on 7/24/16.
 */
public class AdvanceButtonListener extends MessageOutButton
{
    Button button;
    AdvanceButtonListener(Button b, String message_out, NetworkManager network_manager)
    {
        super(message_out,network_manager);
        button = b;
    }
    public boolean onTouch(View v, MotionEvent event)
    {
        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                Log.i("ButtonPressInfo", "Button Down");
                button.setBackground(MainActivity.getDrawable(MainActivity.context, R.drawable.advance_button_pressed));
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
                button.setBackground(MainActivity.getDrawable(MainActivity.context, R.drawable.advance_button));
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
