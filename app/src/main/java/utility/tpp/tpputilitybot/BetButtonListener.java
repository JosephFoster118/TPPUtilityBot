package utility.tpp.tpputilitybot;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Joseph on 8/20/2015.
 */
public class BetButtonListener implements View.OnClickListener {
    boolean red;
    EditText bet_amount;
    NetworkManager manager;
    BetButtonListener(boolean is_red, EditText bet,NetworkManager network_manager)
    {
        red = is_red;
        bet_amount = bet;
        manager = network_manager;

    }
    public void onClick(View v)
    {
        Log.i("Network Out", "Message out:" + bet_amount.getText());
        if(red == true)
        {
            manager.sendMessage("PRIVMSG #twitchplayspokemon :!bet " + bet_amount.getText() + " red\r\n");
        }
        else
        {
            manager.sendMessage("PRIVMSG #twitchplayspokemon :!bet " + bet_amount.getText() + " blue\r\n");
        }

    }
}
