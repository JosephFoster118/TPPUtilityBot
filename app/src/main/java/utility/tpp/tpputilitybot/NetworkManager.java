package utility.tpp.tpputilitybot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;



/**
 * Created by Joseph on 8/13/2015.
 */
public class NetworkManager extends Thread {
    static InetAddress IPAddress;
    String hostName = "irc.twitch.tv";
    int portNumber = 6667;
    Socket client;
    BufferedWriter writer;
    BufferedReader reader;
    boolean connected = false;
    boolean connecting = false;
    //String ouath = "26candgdsj1bf4ryna1mb55b4764ga";
    //String nick_name = "texanmonkeyalpha";
    String nick_name = "";
    String ouath = "";
    String line = null;
    String channel = "#twitchplayspokemon";
    String out_message = null;

    boolean attempting_login;
    boolean thread_is_running;
    boolean destroy;

    IRCMessage parsed_message;


    String received;
    ChatUpdater updater;
    TextView balance_display;

    Handler handler;
    Vibrator vibrator;

    NetworkManager(ChatUpdater chat_updater,TextView balance_display_text,Vibrator v)
    {
        updater = chat_updater;
        received = new String("--------Twitch Chat--------");
        attempting_login = false;
        thread_is_running = false;
        connectNetwork();
        handler = new Handler();
        balance_display = balance_display_text;
        vibrator = v;

    }
    void reInit(ChatUpdater chat_updater,TextView balance_display_text,Vibrator v)
    {
        Log.println(Log.ASSERT,"REINIT NETWORK",chat_updater + " " + balance_display_text + " " + v);
        updater = chat_updater;
        //received = new String("Twitch Chat");
        //attempting_login = false;
        //thread_is_running = false;
        //handler = new Handler();
        balance_display = balance_display_text;
        vibrator = v;

    }

    public void run()
    {
        while(true)
        {
            thread_is_running = true;
            destroy = false;
            while (connected != true)
            {
                try
                {
                    Thread.sleep(250);
                } catch (InterruptedException e)
                {
                }
            }
            try
            {


                // The server to connect to and our details.

                // Keep reading lines from the server.
                while (connected == true)
                {
                    while ((line = reader.readLine()) != null)
                    {
                        while (connected != true)
                        {
                            try
                            {
                                Thread.sleep(250);
                            } catch (InterruptedException e)
                            {
                            }
                        }
                        parsed_message = new IRCMessage(line);
                        if (line.toUpperCase().startsWith("PING "))
                        {
                            // We must respond to PINGs to avoid being disconnected.
                            writer.write("PONG " + line.substring(5) + "\r\n");
                            Log.i("Message Info", "Ping recieved");
                            //writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
                            writer.flush();
                        } else if(line.contains("Error logging in"))
                        {
                            //updater.addText("\nERROR: Invalid Log In!\n");
                        }
                        else if(line.contains("Invalid NICK"))
                        {
                            Log.i("LOG IN ERROR", "Invalid Nick");
                            //DialogFragment newFragment = new FailedLogInDialogInvalidName();
                           // newFragment.show(NetworkManagerHolder.main_activity.getFragmentManager(), "missiles");
                            Log.i("LOG IN ERROR", "Error logging in");
                            Intent intent = new Intent(NetworkManagerHolder.main_activity, LoginSettings.class);
                            NetworkManagerHolder.main_activity.startActivity(intent);
                        }
                        else
                        {
                            //Log.i("MESSAGE ", line);
                            // Print the raw line received by the bot.
                            if (parsed_message.getType().equals("PRIVMSG") ||  parsed_message.getType().equals("WHISPER"))
                            {
                                String test_message = " ";
                                if (parsed_message.getSender().equals("tppbankbot"))
                                {
                                    if (parsed_message.getDirectedTowards().toLowerCase().equals(nick_name.toLowerCase()))
                                    {
                                        Log.i("Message Info", "Its for you!");
                                        int reference_front = parsed_message.getTrailing().indexOf(" ");
                                        reference_front++;//increment to the next letter
                                        int reference_back = parsed_message.getTrailing().lastIndexOf(" ");
                                        //reference_back--;//decrement back one letter
                                        test_message = parsed_message.getTrailing().substring(reference_front, reference_back);
                                        Log.i("Message Info", "Location is [" + test_message + "]");
                                        if (test_message.equals("your balance is"))
                                        {
                                            long[] pattern = {0, 200, 150, 200, 150, 800};
                                            vibrator.vibrate(pattern, -1);
                                            String amount = parsed_message.getTrailing().substring(reference_front + 16);
                                            Log.i("Message Info", "Your balance is " + amount);
                                            out_message = amount;

                                        }


                                    }
                                    if(parsed_message.getType().equals("WHISPER"))
                                    {
                                        //if()
                                    }

                                }
                                if((updater != null) || (balance_display != null))
                                {

                                    updater.addText(parsed_message.getSender() + ": " + parsed_message.getTrailing() + "\n");
                                    if(balance_display != null)
                                    {
                                        if (test_message.equals("your balance is"))
                                        {
                                            handler.post(new Runnable()
                                            {
                                                public void run()
                                                {
                                                    balance_display.setText("Balance: " + out_message);
                                                }
                                            });
                                        }
                                    }



                                } else if (parsed_message.getSender().equals("tppinfobot"))
                                {
                                    Log.i("Message Info", "Info Bot Said " + parsed_message.getTrailing());
                                    if (parsed_message.getTrailing().equals("A new match is about to begin!"))
                                    {
                                        Log.i("Notify Of Event", "New Match Time!!!!!!!!!!!!1!");
                                        sendMessage("PRIVMSG #twitchplayspokemon :!balance\r\n");
                                    }
                                    //else if()
                                }
                            }
                            else if(parsed_message.getType().equals("NOTICE"))
                            {
                                updater.addText("ERROR: Failed to log in!\n");
                            }
                            else
                            {
                                if(line.equals("tmi.twitch.tv NOTICE * :Error logging in"))
                                {
                                    Log.i("LOG IN ERROR", "Error logging in");
                                    updater.addText("ERROR: Failed to log in!");
                                }
                                Log.i("MESSAGE ", line);
                            }

                        }
                        //updater.addText("asdf");
                    }
                }
                Log.i("Network Thread", "Network Disconnected from lack of activity");
            } catch (UnknownHostException e)
            {
                Log.e("Network Thread", "Exception thrown: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
                Log.e("Network Thrown", "Exception thrown: " + e.getMessage());
            }
            Log.i("Network Thread", "Network Disconnected");
            thread_is_running = false;
        }

    }

    public void logIn(String user, String oa)
    {

        nick_name = user;
        ouath = oa;
        connectNetwork();
        logIn();
    }

    public void logIn()
    {
            if (attempting_login == false)
            {
                attempting_login = true;
                while (connected != true)
                {
                    try
                    {
                        Thread.sleep(250);
                    } catch (InterruptedException e)
                    {
                    }
                }
                if (thread_is_running == false)
                {
                    //this.start();
                }
                Log.i("Network Login", "Logging in...");

                new Thread()
                {
                    public void run()
                    {
                        try
                        {
                            writer.write("PASS oauth:" + ouath + "\r\n ");
                            writer.write("NICK " + nick_name + "\r\n ");
                            writer.flush();


                            // Read lines from the server until it tells us we have connected.


                            // Join the channel.
                            writer.write("JOIN " + channel + "\r\n");
                            writer.flush();
                            writer.write("CAP REQ :twitch.tv/commands\r\n");
                            writer.flush();
                            Log.i("Network Login", "User logged in");

                        } catch (IOException e)
                        {
                            Log.e("Network Login", "Exception thrown: " + e.getMessage());
                            updater.addText("ERROR: Failed to log in!\n");
                        }
                        attempting_login = false;
                    }
                }.start();
            }
    }

    void sendMessage(String message)
    {

        out_message = message;
        new Thread() {
            public void run() {
                try {

                    Log.i("Network Out", "Message out:" + out_message);
                    if (connected == true) {
                        writer.write(out_message);
                        writer.flush();
                            //writer.write(out_message);
                            writer.flush();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    void connectNetwork()
    {
        connected = false;
        if(client != null)
        {
            client = null;
            Log.i("Network Creation","Recreating Client");
        }
        if(writer != null)
        {
            writer = null;
        }
        if(reader != null)
        {
            reader = null;
        }
        System.gc();
        new Thread(){
            public void run(){
                try {
                    IPAddress = InetAddress.getByName(hostName);
                    client = new Socket(IPAddress, portNumber);
                    //client.setSoTimeout(5000000);
                    writer = new BufferedWriter(
                            new OutputStreamWriter(client.getOutputStream()));
                    reader = new BufferedReader(
                            new InputStreamReader(client.getInputStream()));

                    connected = true;
                    Log.i("Network", "Network Created");

                } catch (UnknownHostException e1) {
                    Log.e("Network","Unknown host");
                } catch (IOException e1) {
                    Log.e("Network","Failed to create socket");
                }

            }
        }.start();

    }



}

