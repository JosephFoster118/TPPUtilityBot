package utility.tpp.tpputilitybot;

import android.util.Log;

/**
 * Created by Joseph on 8/20/2015.
 */
public class IRCMessage
{
    String full_message = null;
    String type = null;
    String sender = null;
    String trailing = null;
    String directed_towards = null;

    IRCMessage(String line)
    {
        if(line != null)
        {
            full_message = new String(line);

            int reference_front = full_message.indexOf('!');
            int reference_back = -1;

            if(reference_front != -1)
            {
                reference_back = full_message.indexOf('@', reference_front);
                if(reference_back != -1) {
                    sender = full_message.substring(reference_front + 1, reference_back);

                }
            }
            if(reference_back != -1)
            {
                reference_front = full_message.indexOf(' ', reference_back);
                if(reference_front != -1)
                {
                    reference_back = full_message.indexOf(' ', reference_front + 1);
                    if(reference_back != -1)
                    {
                        type = full_message.substring(reference_front + 1,reference_back);
                    }
                }
            }
            if(reference_back != -1)
            {
                reference_front = full_message.indexOf(':', reference_back);
                if(reference_front != -1)
                {

                        trailing = full_message.substring(reference_front + 1);
                }
            }
            if(trailing != null)
            {
                reference_front = trailing.indexOf("@");
                if(reference_front != -1)
                {
                    reference_back = trailing.indexOf(" ",reference_front);
                    if(reference_back != -1)
                    {
                        directed_towards = trailing.substring(reference_front + 1,reference_back);
                        //Log.i("Message Parser"," directed: " + directed_towards);
                    }
                }
            }
            //Log.i("Message Parser", "Sender: " + sender + " type: " + type + " trailing: " + trailing);

        }
        //parse message
    }

    public String getType()
    {
        if(type == null)
        {
            return "";
        }
        else
        {
            return type;
        }
    }

    public String getSender()
    {
        if(sender == null)
        {
            return "";
        }
        else
        {
            return sender;
        }
    }

    public String getTrailing()
    {
        if(trailing == null)
        {
            return "";
        }
        else
        {
            return trailing;
        }
    }

    public String getDirectedTowards()
    {
        if(directed_towards == null)
        {
            return "";
        }
        else
        {
            return directed_towards;
        }
    }
    @Override
    public String toString() {
        return full_message;
    }
}
