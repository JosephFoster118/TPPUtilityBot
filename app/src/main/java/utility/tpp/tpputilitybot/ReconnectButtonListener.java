package utility.tpp.tpputilitybot;

import android.util.Log;
import android.view.View;

/**
 * Created by Joseph on 8/21/2015.
 */
public class ReconnectButtonListener implements View.OnClickListener
{
    NetworkManager manager;
    ReconnectButtonListener(NetworkManager network_manager)
    {
        manager = network_manager;
    }
    public void onClick(View v)
    {
        manager.connectNetwork();
        new Thread() {
            public void run() {
                manager.logIn();
            }
        }.start();
    }
}
