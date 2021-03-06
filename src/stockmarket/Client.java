package stockmarket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Client class
 * 
 *
 */
public class Client extends Socket{
	
	
	//Handles data output
	Sender out;
	//Handles data input
	Receiver in; //Data in
	
	
	
	
	
	/**
	 * Primary constructor
	 * @param ip
	 * @param port
	 * @throws IOException
	 */
	public Client(String ip,int port) throws IOException{ 
		super(ip,port);
		//Initialise the receiver
		in = new Receiver(this);
		//Start the receiver thread
		in.start();
		//Initialise output
		out = new Sender(this);
		//Start the sender thread
		out.start();
		
	
	}
	
	/**
	 * Stop the in/out threads
	 */
	@SuppressWarnings("deprecation")
	public void Stop(){ in.stop(); out.stop(); }
	
	/**
	 * Write some data to the server
	 * @param string
	 */
	public void Write(String string){ out.Send(string); }
	
	/**
	 * Read data from the server
	 * @return
	 */
	public String Read(){ return in.getData(); }
	
	/**
	 * Data sender class
	 * @author Adam
	 *
	 */
	public class Sender extends Thread
	{
		//Writer
		PrintWriter out;
		
		//Data
		ArrayList<String> data;
		
		/**
		 * Main constructor
		 * @param client
		 */
		public Sender(Client client)
		{
			try
			{
				out = new PrintWriter(client.getOutputStream(),true);
				data = new ArrayList<String>();
			}catch(Exception e){ System.out.println(e.getMessage()); }
		}
		
		/**
		 * Send some data
		 * @param _data
		 */
		public void Send(String _data)
		{
			//Add it to the queue
			data.add(_data);
		}
		
		/*
		 * The main run method
		 * (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run(){
			
			//Keep looping...
			while(true)
			{
				//Is there any data to send?
				if(!data.isEmpty())
				{
					//Cycle through and check for data to send
					for(int i =0; i < data.size();i++)
					{
						//If any - send the data
						out.println(data.get(i));
						//We don't need that data in the list anymore - so remove it
						data.remove(i);
					}
				} //Else do nothing, keep checking
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	
	/**
	 * Receiver class
	 * @author Adam
	 *
	 */
	public class Receiver extends Thread
	{
		//Buffered reader is used for receiving data from the server
		BufferedReader in;
		//Data
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
				data = "";
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				IS_CLIENT_ALIVE = client.isConnected();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		}
		
		public String getData(){ return data; }
		
		
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
			while(true)
			{
				//Check for errors
				try
				{
					data = "";
					String _data;
					//while((_data = in.readLine()) != null)
					//{
					//	System.out.println("Data received from server:" + _data);
					//	
					//	//data = _data;
					//	data = _data;
					//	
					//}
					
					while(!(_data = in.readLine()).equals(""))
		            {
						System.out.println("Data received from server:" + _data);
						
						//data = _data;
						data = _data;
		            }
					
					
				//Catch - and display the error
				}catch(Exception e){ System.out.println(e.getMessage()); }
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
}

