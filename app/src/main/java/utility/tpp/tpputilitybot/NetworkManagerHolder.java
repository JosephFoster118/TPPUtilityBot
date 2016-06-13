package utility.tpp.tpputilitybot;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Joseph on 9/26/2015.
 */
public class NetworkManagerHolder
{
    static Activity main_activity = null;
    static NetworkManager network;
    static SharedPreferences sharedPref;
    static NetworkManager initializeNetwork(ChatUpdater chat_updater,TextView balance_display_text,Vibrator v,SharedPreferences sp)
    {
        sharedPref = sp;
        if(network == null)
        {
            network = new NetworkManager(chat_updater,balance_display_text,v);
            network.start();
            new Thread() {
                public void run() {
                    network.logIn(sharedPref.getString(main_activity.getString(R.string.login_username_key),""),sharedPref.getString(main_activity.getString(R.string.login_ouath_key),""));
                }
            }.start();
        }
        else
        {
            network.reInit(chat_updater,balance_display_text,v);
        }
        return network;

    }

    static NetworkManager getNetwork()
    {
        if(network == null)
        {
            network = new NetworkManager(null,null,null);
            network.start();
            new Thread() {
                public void run() {
                    network.logIn();
                }
            }.start();
        }
        else
        {

        }
        return network;
    }

    static NetworkManager initializeNetwork(SharedPreferences sp)
    {
        sharedPref = sp;
        if(network == null)
        {
            new Thread() {
                public void run() {
                    network.logIn(sharedPref.getString(main_activity.getString(R.string.login_username_key),""),sharedPref.getString(main_activity.getString(R.string.login_ouath_key),""));
                }
            }.start();
        }
        else
        {
            network.reInit(null,null,null);
        }
        return network;

    }

    static NetworkManager initializeNetwork(Vibrator v,SharedPreferences sp)
    {
        sharedPref = sp;
        if(network == null)
        {
            network = new NetworkManager(null,null,v);
            network.start();
            new Thread() {
                public void run() {
                    network.logIn();
                }
            }.start();
        }
        else
        {
            network.reInit(null, null, v);
        }
        return network;

    }

    static void setMainActivity(Activity a)
    {
        main_activity = a;
    }

}
