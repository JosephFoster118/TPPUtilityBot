package utility.tpp.tpputilitybot;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by joseph on 7/24/16.
 */

class DBMutex
{
    private boolean mutex;

    public DBMutex()
    {
        mutex = false;
    }

    public void lock() throws InterruptedException
    {
        while(mutex == true)
        {
            Thread.sleep(1);
        }
        mutex = true;
    }

    public void unlock()
    {
        mutex = false;
    }

}

public class DatabaseManager implements Runnable
{
    private static Pokemon pokemon = null;
    private static boolean is_initialized = false;
    private static boolean thread_running = false;
    private static MainActivity main_activity;
    private static DBMutex mutex;
    private static Thread thread;
    private static Handler handler;
    private static int selected_team;

    private static DatagramSocket clientSocket;
    private static DatagramPacket recv;

    public static final int port = 25738;
    public static final String ip = "98.195.113.119";
    public static final int BLUE_TEAM = 1;
    public static final int RED_TEAM = 2;

    public static void initialize(MainActivity activity)
    {
        if(is_initialized == false)
        {
            mutex = new DBMutex();
            Log.i("DatabaseManager","Initializing Database Manager");
            pokemon = new Pokemon("A","B","C","D");
            handler = new Handler();
            is_initialized = true;
            thread = new Thread(new DatabaseManager());
            selected_team = RED_TEAM;
            thread.start();
        }
        try
        {
            mutex.lock();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        main_activity = activity;
        mutex.unlock();



    }

    public static Pokemon getPokemon()
    {
        return pokemon;
    }

    public static void setBlue()
    {
        selected_team = BLUE_TEAM;
    }

    public static void setRed()
    {
        selected_team = RED_TEAM;
    }

    @Override
    public void run()
    {
        thread_running = true;
        byte send_buffer[];
        byte recv_buffer[] = new byte[2048];

        try
        {
            clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(500);
        } catch (SocketException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        System.out.println("Socket Initialized");

        recv = new DatagramPacket(recv_buffer, recv_buffer.length);
        while(thread_running)
        {
            try
            {
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            //Log.d("DatabaseManager Thread","TICK");

            //send message
            Arrays.fill(recv_buffer,(byte)0);
            if(selected_team == BLUE_TEAM)
            {
                send_buffer = "m_blue".getBytes();
            }
            else
            {
                send_buffer = "m_red".getBytes();
            }
            try
            {
                DatagramPacket pack = new DatagramPacket(send_buffer,send_buffer.length,InetAddress.getByName(ip),port);
                clientSocket.send(pack);
                clientSocket.receive(recv);
                int x = 0;
                while(recv_buffer[x] != (byte)0)
                {
                    x++;
                }
                Log.d("DatabaseManager",new String(recv_buffer,0,x,Charset.forName("US-ASCII")));
                //parse string
                parseMoves(new String(recv_buffer,0,x,Charset.forName("US-ASCII")));
            } catch (UnknownHostException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }



            //set buttons
            handler.post(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        mutex.lock();
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    main_activity.getButtonA().setText(pokemon.getMoveA());
                    main_activity.getButtonB().setText(pokemon.getMoveB());
                    main_activity.getButtonC().setText(pokemon.getMoveC());
                    main_activity.getButtonD().setText(pokemon.getMoveD());
                    mutex.unlock();
                }
            });

        }
        thread_running = false;
    }

    private void parseMoves(String str)
    {
        String move_a = "A";
        String move_b = "B";
        String move_c = "C";
        String move_d = "D";

        int reference_front = 0;
        int reference_back = -1;

        //start parse
        if(reference_front != -1)
        {
            reference_back = str.indexOf('|', reference_front);
            if(reference_back != -1)
            {
                move_a = str.substring(reference_front, reference_back);

            }
        }
        if(reference_back != -1)
        {
            reference_front = reference_back + 1;
            reference_back = str.indexOf('|', reference_front);
            if(reference_back != -1)
            {
                move_b = str.substring(reference_front, reference_back);

            }
        }
        if(reference_back != -1)
        {
            reference_front = reference_back + 1;
            reference_back = str.indexOf('|', reference_front);
            if(reference_back != -1)
            {
                move_c = str.substring(reference_front, reference_back);

            }
        }
        if(reference_back != -1)
        {
            reference_front = reference_back + 1;
            reference_back = str.indexOf('|', reference_front);
            if(reference_back != -1)
            {
                move_d = str.substring(reference_front, reference_back);

            }
        }

        Log.d("DatabaseManager","Move A is " + move_a);
        Log.d("DatabaseManager","Move B is " + move_b);
        Log.d("DatabaseManager","Move C is " + move_c);
        Log.d("DatabaseManager","Move D is " + move_d);

        try
        {
            mutex.lock();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        pokemon = new Pokemon(move_a,move_b,move_c,move_d);
        mutex.unlock();
    }
}
