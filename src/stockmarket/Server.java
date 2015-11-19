package stockmarket;

import java.net.*;
import java.io.*;

public class Server
{

    protected ServerSocket STSSocket = null;
    public StockMarket mySM;

    public Server()
    {
    }

    public void initSTS()
    {
        System.out.println("StockMarket thread started.");
        Thread t1 = new Thread(mySM.getStockMarket());
        t1.start();
    }


    public void listenForClients()
    {
        try
        {
            STSSocket = new ServerSocket(5000);
            
            while(true)
            {
                System.out.println("Listening for connections from Client.\n");
                new ClientConnect(STSSocket.accept(), mySM.getStockMarket());
            }
        }
        catch(IOException e)
        {
            System.out.println("Error in setting up socket " + e);
            System.exit(1);
        }
    }

}
