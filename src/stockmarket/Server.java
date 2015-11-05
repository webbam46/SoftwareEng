package stockmarket;
import java.net.*;
import java.io.*;

public class Server extends Thread
{

    protected ServerSocket STSSocket = null;

    public Server()
    {
    	initSTS();
    }

    public void initSTS()
    {
        try
        {
            STSSocket = new ServerSocket(5000);
            
            while(true)
            {
                System.out.println("Listening for connections.\n");
                new ClientConnect(STSSocket.accept());
            }
        }
        catch(IOException e)
        {
            System.out.println("Error in setting up socket " + e);
            System.exit(1);
        }
    }

    public static void main(String [] args)
    {
        Server mySTS = new Server();
        mySTS.initSTS();

    }


    class ClientConnect extends Thread
    {
        protected Socket clientSocket;
        protected BufferedReader in = null;
        protected PrintWriter out = null;

        private ClientConnect(Socket aSocket)
        {
            clientSocket = aSocket;
            start();
        }

        public void run()
        {
            System.out.println("New client has connected, new thread started.\n");
            System.out.println("Client IP is: " + clientSocket.getRemoteSocketAddress() + "\n\n");
            
            try
            {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                
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
                        break;
                    }
                    else
                    {
                        System.out.println("DEBUG:"+inputText+":");
                    }
                }
                out.close();
                in.close();

                clientSocket.close();
            }
            catch(IOException e)
            {
                System.out.println("Problem with socket: " + e);
            }
        }

    }
    

}
