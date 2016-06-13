package utility.tpp.tpputilitybot;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Network;
import android.net.Uri;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LoginSettings extends AppCompatActivity
{

    Button get_ouath_button;
    Button login_button;
    EditText name_edit;
    EditText oauth_edit;
    GetButtonListener get_oauth_listener;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_settings);
        get_ouath_button = (Button) findViewById(R.id.get_oauth);
        get_oauth_listener = new GetButtonListener(this);
        get_ouath_button.setOnClickListener(get_oauth_listener);
        login_button = (Button) findViewById(R.id.login_button);
        name_edit = (EditText) findViewById(R.id.Username);
        oauth_edit = (EditText) findViewById(R.id.OAth);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.login_key),MODE_PRIVATE);
        name_edit.setText(sharedPref.getString(getString(R.string.login_username_key),""));
        oauth_edit.setText(sharedPref.getString(getString(R.string.login_ouath_key),""));


        login_button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                /*
                new Thread()
                {
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(500);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }.start();*/

                Log.i("Login Settings", "name: " + name_edit.getText().toString());
                NetworkManager network = NetworkManagerHolder.getNetwork();
                network.logIn(name_edit.getText().toString(), oauth_edit.getText().toString());
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.login_key),MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.login_username_key),name_edit.getText().toString());
                editor.putString(getString(R.string.login_ouath_key), oauth_edit.getText().toString());
                editor.commit();
                finish();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}

class SaveAndLogin implements View.OnClickListener
{

    @Override
    public void onClick(View v)
    {

    }
}

class GetButtonListener implements View.OnClickListener
{
    AppCompatActivity parent;
    GetButtonListener(AppCompatActivity p)
    {
        parent = p;
    }
    public void onClick(View v)
    {
        String url = "http://frccountdown.hosthorde.net/app/Twitch/Redirect.php";
        try {
            Intent i = new Intent("android.intent.action.MAIN");
            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
            i.addCategory("android.intent.category.LAUNCHER");
            i.setData(Uri.parse(url));
            parent.startActivity(i);
        }
        catch(ActivityNotFoundException e) {
            // Chrome is not installed
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            parent.startActivity(i);
        }
    }
}