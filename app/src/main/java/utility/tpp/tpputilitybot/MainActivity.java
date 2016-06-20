package utility.tpp.tpputilitybot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    NetworkManager network;
    ChatUpdater updater;
    ScrollView scroll;
    TextView terminal;
    TextView balance_display;
    EditText bet_amount;
    Button bet_red_button;
    Button bet_blue_button;
    Button reconnect_button;
    Button balance_button;

    Button a_button;
    Button b_button;
    Button c_button;
    Button d_button;

    MessageOutButton a_button_listener;
    MessageOutButton b_button_listener;
    MessageOutButton c_button_listener;
    MessageOutButton d_button_listener;

    BetButtonListener bet_red_listener;
    BetButtonListener bet_blue_listener;
    MessageOutButton balance_button_listener;
    ReconnectButtonListener reconnect_listener;
    Vibrator vibrator;
    WebView browser = null;

    static final int BettingMode = 0;
    static final int GBAMode = 1;
    public static Context context;

    public static final Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    void initializeStatics()
    {
        int orientation = getResources().getConfiguration().orientation;
        if(WebviewHolder.getUpdater() == null)
        {
            WebviewHolder.initializeUpdater(this);
        }
        else
        {
            if((ViewGroup)WebviewHolder.getScroll().getParent()!=null)
            {
                ((ViewGroup)WebviewHolder.getScroll().getParent()).removeView(WebviewHolder.getScroll());
            }
            ;
        }
        if(WebviewHolder.getStream() == null)
        {


            WebviewHolder.initializeStream(this);


            browser = WebviewHolder.getStream();
            browser.getSettings().setJavaScriptEnabled(true);
            browser.getSettings().setPluginState(WebSettings.PluginState.ON);
            browser.getSettings().setAllowFileAccess(true);
            browser.getSettings().setAllowContentAccess(true);
            browser.getSettings().setAllowFileAccessFromFileURLs(true);
            browser.getSettings().setAllowUniversalAccessFromFileURLs(true);
            browser.getSettings().setDomStorageEnabled(true);
            browser.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    return (event.getAction() == MotionEvent.ACTION_MOVE);
                }
            });
            //webSettings.setUseWideViewPort(true);
            //webSettings.setLoadWithOverviewMode(true);


            //browser.loadUrl("http://www.twitch.tv/gronkh/embed");
            browser.loadUrl("http://player.twitch.tv/?volume=1.0&channel=twitchplayspokemon");
        }
        else
        {
            browser = WebviewHolder.getStream();
            if((ViewGroup)browser.getParent() !=null)
            {
                ((ViewGroup) browser.getParent()).removeView(browser);
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i("On Destroy", "Destroying Activity");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display mDisplay = mWindowManager.getDefaultDisplay();
        NetworkManagerHolder.setMainActivity(this);
        initializeStatics();
        int orientation = getResources().getConfiguration().orientation;
        Log.i("Startup", "Orientation is " + orientation);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.settings_key),MODE_PRIVATE);
        int button_layout = sharedPref.getInt(getString(R.string.controller_settings),GBAMode);
        if((orientation == 1) || (orientation == 3))
        {
            if(button_layout == BettingMode)
            {
                setContentView(R.layout.activity_main_betting);

                RelativeLayout webview_holder = (RelativeLayout) findViewById(R.id.webviewholder);
                webview_holder.addView(browser);
                Log.i("Startup", "Added Stream");

                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // = (ScrollView) findViewById(R.id.terminal_scroll);
                //terminal = (TextView) findViewById(R.id.terminal);
                balance_display = (TextView) findViewById(R.id.balance);
                //updater = new ChatUpdater(scroll, terminal);

                bet_red_button = (Button) findViewById(R.id.bet_red);
                bet_blue_button = (Button) findViewById(R.id.bet_blue);
                bet_amount = (EditText) findViewById(R.id.bet_amount);
                balance_button = (Button) findViewById(R.id.balance_button);

                reconnect_button = (Button) findViewById(R.id.reconnect_button);

                a_button = (Button) findViewById(R.id.move_a);
                b_button = (Button) findViewById(R.id.move_b);
                c_button = (Button) findViewById(R.id.move_c);
                d_button = (Button) findViewById(R.id.move_d);
                Log.i("Startup", "Added Stream");
                RelativeLayout updater_holder = (RelativeLayout) findViewById(R.id.updater_holder);
                Log.i("Init View Thing", "Value = :" + WebviewHolder.getScroll());
                updater_holder.addView(WebviewHolder.getScroll());

                network = NetworkManagerHolder.initializeNetwork(WebviewHolder.getUpdater(), balance_display, vibrator,getSharedPreferences(getString(R.string.login_key),MODE_PRIVATE));

                bet_red_listener = new BetButtonListener(true, bet_amount, network);
                bet_red_button.setOnClickListener(bet_red_listener);
                bet_blue_listener = new BetButtonListener(false, bet_amount, network);
                bet_blue_button.setOnClickListener(bet_blue_listener);
                reconnect_listener = new ReconnectButtonListener(network);
                reconnect_button.setOnClickListener(reconnect_listener);
                balance_button_listener = new MessageOutButton("!balance\r\n", network);
                balance_button.setOnClickListener(balance_button_listener);

                a_button_listener = new MessageOutButton("!move a\r\n", network);
                b_button_listener = new MessageOutButton("!move b\r\n", network);
                c_button_listener = new MessageOutButton("!move c\r\n", network);
                d_button_listener = new MessageOutButton("!move d\r\n", network);

                a_button.setOnClickListener(a_button_listener);
                b_button.setOnClickListener(b_button_listener);
                c_button.setOnClickListener(c_button_listener);
                d_button.setOnClickListener(d_button_listener);

                ImageButton reload_stream_button = (ImageButton) findViewById(R.id.reload_stream_button);
                reload_stream_button.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        browser.loadUrl("http://player.twitch.tv/?volume=1.0&channel=twitchplayspokemon");
                    }
                });
            }
            else if(button_layout == GBAMode)
            {
                setContentView(R.layout.activity_main_gameboy);

                RelativeLayout webview_holder = (RelativeLayout) findViewById(R.id.webviewholder);
                webview_holder.addView(browser);
                Log.i("Startup", "Added Stream");
                ImageButton reload_stream_button = (ImageButton) findViewById(R.id.reload_stream_button);
                reload_stream_button.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        browser.loadUrl("http://player.twitch.tv/?volume=1.0&channel=twitchplayspokemon");
                    }
                });
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                network = NetworkManagerHolder.initializeNetwork(WebviewHolder.getUpdater(), balance_display, vibrator,getSharedPreferences(getString(R.string.login_key),MODE_PRIVATE));
                reconnect_listener = new ReconnectButtonListener(network);
                reconnect_button = (Button) findViewById(R.id.reconnect_button);
                reconnect_button.setOnClickListener(reconnect_listener);

                Log.i("Startup", "Added Stream");
                RelativeLayout updater_holder = (RelativeLayout) findViewById(R.id.updater_holder);
                Log.i("Init View Thing", "Value = :" + WebviewHolder.getScroll());
                updater_holder.addView(WebviewHolder.getScroll());
                //Setup button stuffs
                ImageButton a_button = (ImageButton) findViewById(R.id.A_button);
                ImageButton b_button = (ImageButton) findViewById(R.id.B_button);
                ImageButton up_button = (ImageButton) findViewById(R.id.up_button);
                ImageButton down_button = (ImageButton) findViewById(R.id.down_button);
                ImageButton left_button = (ImageButton) findViewById(R.id.left_button);
                ImageButton right_button = (ImageButton) findViewById(R.id.right_button);
                //Button select_button = (Button) findViewById(R.id.select_button);
                //Button start_button = (Button) findViewById(R.id.start_button);
                a_button.setOnTouchListener(new ControllerButtonTouchListener(a_button, "a\r\n", network));
                b_button.setOnTouchListener(new ControllerButtonTouchListener(b_button, "b\r\n", network));
                up_button.setOnTouchListener(new ControllerButtonTouchListener(up_button, "up\r\n", network));
                down_button.setOnTouchListener(new ControllerButtonTouchListener(down_button, "down\r\n", network));
                left_button.setOnTouchListener(new ControllerButtonTouchListener(left_button, "left\r\n", network));
                right_button.setOnTouchListener(new ControllerButtonTouchListener(right_button, "right\r\n", network));
                //select_button.setOnClickListener(new MessageOutButton("select\r\n", network));
                //start_button.setOnClickListener(new MessageOutButton("start\r\n", network));
                


            }
        }
        else
        {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            network = NetworkManagerHolder.initializeNetwork(vibrator,getSharedPreferences(getString(R.string.login_key),MODE_PRIVATE));
            setContentView(R.layout.activity_full_screen);
            RelativeLayout webview_holder = (RelativeLayout) findViewById(R.id.webviewholder_full);
            webview_holder.addView(browser);
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.login_settings:
            {
                Log.d("Menu Pressed","Login Pressed");
                Intent intent = new Intent(this, LoginSettings.class);
                startActivity(intent);
                return true;
            }
            case R.id.settings:
            {
                Log.d("Menu Pressed","Settings Pressed");
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;
            }

            default:
                Log.d("Menu Pressed","Unknown Menu Item");
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.settings_key), MODE_PRIVATE);
        boolean reload = sharedPref.getBoolean(getString(R.string.reload_flag), false);
        if(reload)
        {
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
        }
    }


}
