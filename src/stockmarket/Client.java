package stockmarket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client class
 * 
 *
 */
public class Client extends Socket{
	
	
	//Handles data input
	PrintWriter out;
	//Handles data output
	Receiver in; //Data in
	
	
	
	
	
	/**
	 * Primary constructor
	 * @param ip
	 * @param port
	 * @throws IOException
	 */
	public Client(String ip,int port) throws IOException{ 
		super(ip,port);
		//Initialise output
		out = new PrintWriter(this.getOutputStream(),true);
		
		//Initialise the receiver
		in = new Receiver(this);
		//Start the receiver thread
		in.start();
	
	}
	
	
	public void Write(String string){ out.println(string); }


	
	/**
	 * Receiver class
	 * @author Adam
	 *
	 */
	public class Receiver extends Thread
	{
		//Buffered reader is used for receiving data from the server
		BufferedReader in;
		//Holds the last piece of data received by the server
		String data;
		//Boolean holds if the client is alive
		boolean IS_CLIENT_ALIVE = false;
		
		/*
		 * Primary constructor
		 */
		public Receiver(Client client)
		{
			//Initialise the bufferedreader
			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				IS_CLIENT_ALIVE = client.isConnected();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		}
		
		
		/**
		 * Check if the connection is still alive
		 * @param c
		 */
		public void checkClient(Client c){ IS_CLIENT_ALIVE = c.isConnected(); }
		
		/*
		 * The main run method
		 * (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run()
		{
			//Keep looping and checking for data
			while(IS_CLIENT_ALIVE)
			{
				//Check for errors
				try
				{
					String _data;
					while((_data = in.readLine()) != null)
					{
						System.out.println("Data received from server:" + _data);
						
						data = _data;
					}
					
					
				//Catch - and display the error
				}catch(Exception e){ System.out.println(e.getMessage()); }
			}
			this.stop();
		}
	}
}

