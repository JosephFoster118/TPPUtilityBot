package utility.tpp.tpputilitybot;

import android.util.Log;
import android.view.View;

/**
 * Created by Joseph on 8/24/2015.
 */
public class MessageOutButton implements View.OnClickListener {
    NetworkManager manager;
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
}
