package utility.tpp.tpputilitybot;

import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Joseph on 8/14/2015.
 */
public class ChatUpdater {
    private ScrollView scroll;
    private TextView terminal;
    private String[] chat_text;
    Handler handler;

    static String output;

    ChatUpdater(ScrollView scroll_view,TextView terminal_text)
    {
        terminal = terminal_text;
        scroll = scroll_view;
        handler = new Handler();
        chat_text = new String[2];
        for(int i = 0;i < chat_text.length;i++)
        {
            chat_text[i] = "lll";
        }
    }

    public ScrollView getUpdater()
    {
        return scroll;
    }

    public void addText(String text)
    {
        output += text;
        if(output.length() > 3000)
        {
            output = output.substring(750);
        }
        /*
        for(int i = chat_text.length - 1;i > 0;i++)
        {
            //chat_text[i + 1] = chat_text[i];
        }
        chat_text[0] = text;
        for(int i = 0; i < chat_text.length;i++)
        {
            if(chat_text[i] != null) {
                text += chat_text[i];
            }
        }
        */

        handler.post(new Runnable() {
            public void run() {
                terminal.setText(output);
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });

    }

}
