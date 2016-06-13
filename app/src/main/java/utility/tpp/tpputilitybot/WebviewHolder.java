package utility.tpp.tpputilitybot;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Joseph on 9/25/2015.
 */
public class WebviewHolder
{
    static WebView stream = null;
    static ChatUpdater updater = null;
    static ScrollView scroll_view = null;
    static TextView console = null;
    static void initializeStream(Context cont)
    {
        if(stream == null)
        {
            stream = new WebView(cont);
            stream.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT));
        }
    }

    static void initializeUpdater(Context cont)
    {
        if(scroll_view == null)
        {
            console = new TextView(cont);
            console.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));
            console.setTextColor(Color.WHITE);
            console.setText("Twitch chat\n");
            console.setTextSize(10.0f);
            scroll_view = new ScrollView(cont);
            scroll_view.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT));
            scroll_view.setBackgroundColor(Color.BLACK);
            scroll_view.addView(console);

        }
        if(updater == null)
        {
            updater = new ChatUpdater(scroll_view,console);
            Log.i("Updater Init", "Updater Initialized");
        }
    }

    static WebView getStream()
    {
        return stream;
    }
    static ChatUpdater getUpdater(){return updater;}
    static ScrollView getScroll(){return scroll_view;};
}
