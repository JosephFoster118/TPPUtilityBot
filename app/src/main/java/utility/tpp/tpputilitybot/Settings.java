package utility.tpp.tpputilitybot;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class Settings extends AppCompatActivity
{
    RadioButton  betting_mode_radio;
    RadioButton  gba_mode_radio;
    Button accept_button;
    static final int BettingMode = 0;
    static final int GBAMode = 1;

    public void saveSettings()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.settings_key),MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(gba_mode_radio.isChecked())
        {
            editor.putInt(getString(R.string.controller_settings),GBAMode);
        }
        else if(betting_mode_radio.isChecked())
        {
            editor.putInt(getString(R.string.controller_settings),BettingMode);
        }
        editor.putBoolean(getString(R.string.reload_flag),true);

        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        betting_mode_radio = (RadioButton) findViewById(R.id.BetMode);
        gba_mode_radio = (RadioButton) findViewById(R.id.GBAMode);
        accept_button = (Button) findViewById(R.id.SettingsAcceptButton);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.settings_key),MODE_PRIVATE);
        int button_layout = sharedPref.getInt(getString(R.string.controller_settings),GBAMode);

        if(button_layout == GBAMode)
        {
            gba_mode_radio.setChecked(true);
        }
        else if(button_layout == BettingMode)
        {
            betting_mode_radio.setChecked(true);
        }

        accept_button.setOnClickListener
        (
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveSettings();
                        finish();
                    }
                }
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
