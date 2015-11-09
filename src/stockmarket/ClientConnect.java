package stockmarket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

class ClientConnect extends Thread
{
    protected Socket clientSocket;
    protected BufferedReader in = null;
    protected PrintWriter out = null;
    protected ObjectOutputStream objectOut;

    protected StockMarket mySMRef;

    protected boolean isRegistered = false;

    public ClientConnect(Socket aSocket, StockMarket aSM)
    {
        clientSocket = aSocket;
        mySMRef = aSM;
        start();
    }

    public void run()
    {
        System.out.println("New client has connected, new thread started.");
        System.out.println("Client IP is: " + clientSocket.getRemoteSocketAddress() + "\n\n");
            
        try
        {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            objectOut = new ObjectOutputStream(clientSocket.getOutputStream());

                
            String inputText;
                
            while((inputText = in.readLine()) != null)
            {
                System.out.println("Client: " + clientSocket.getLocalSocketAddress() + " : " + inputText);
                    
                if(inputText.equals("HELO"))
                {
                    System.out.println("ACK:" + clientSocket.getLocalSocketAddress() + ":" + clientSocket.getRemoteSocketAddress());
                    out.println("ACK:" + clientSocket.getLocalSocketAddress() + ":" + clientSocket.getRemoteSocketAddress());
                }
                else if(inputText.equals("EXIT"))
                {
                    System.out.println("ACK:EXIT:Goodbye!");
                    out.println("ACK:EXIT:Connection Closed.");
                    break;
                }
                else if(inputText.equals("REGI"))
                {
                    System.out.println("ACK:REGI:"+clientSocket.getLocalSocketAddress());
                    out.println("REGI:SUCCESS:1");
                    isRegistered = true;
                }
                else if(inputText.equals("DISP"))
                {   // Display Stock Market
                    if(isRegistered)
                    {
                    	/**
                    	 * 
                    	 *  ERROR FOUND -- State didn't exist - exception was thrown
                    	 *  Try, catch inserted
                    	 * 
                    	 */
                    	String [][] aStock = null;
                    	try
                    	{
                        aStock = mySMRef.getStockMarketState();
                    	}catch(Exception e){ }
                        if(aStock!=null)
                        {
                        	//objectOut.writeObject(mySMRef.getStockMarketState());
                        	for(int i = 0; i < aStock.length; i++)
                        	{
                        		for(int j = 0; j < aStock[i].length; j++)
                        		{
                        			out.println("STK:"+aStock[i][j]+":"+aStock[i][j]+":"+aStock[i][j]);
                        		}
                        	}
                        }
                        out.println("END:EOF");
                    }
                    else
                    {
                        out.println("ERR:Not Registered");
                    }
                }
                else if(inputText.equals("BUY"))
                {
                    if(isRegistered)
                    {
                        out.println("ACK:BUY:Not implemented yet!");
                    }
                    else
                    {
                        out.println("ERR:Not Registered");
                    }
                }
                else if(inputText.equals("SELL"))
                {
                    if(isRegistered)
                    {
                        out.println("ACK:SELL:Not implemented yet!");
                    }
                    else
                    {
                        out.println("ERR:Not Registered");
                    }
                }
                else if(inputText.equals("HELP"))
                {
                    out.println("Commands:");
                    out.println("REGI:");
                    out.println("BUY:");
                    out.println("SELL:");
                    out.println("EXIT:");
                    out.println("DISP:");
                }
                else
                {
                    System.out.println("DEBUG:"+inputText+":");
                }
            }
            out.close();
            in.close();

            clientSocket.close();
            isRegistered = false;
        }
        catch(IOException e)
        {
            System.out.println("Problem with socket: " + e);
        }
    }

}
