package stockmarket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client class
 * 
 *
 */
public class Client extends Socket {
	
	//Handles data output
	PrintWriter out = new PrintWriter(this.getOutputStream(),true);
	//Handles data input
	BufferedReader in = new BufferedReader(new InputStreamReader(this.getInputStream()));
	
	/**
	 * Primary constructor
	 * @param ip
	 * @param port
	 * @throws IOException
	 */
	public Client(String ip,int port) throws IOException{ 
		super(ip,port); // - Call the super constructor, which is the Socket constructor
	}
	
	/**
	 * Write a string to the server
	 * @param string
	 */
	public void Write(String string){
		//Check for errors
		try
		{
			//Print to the server
			out.println(string);
		//Catch the error
		}catch(Exception e){ System.out.println(e.getMessage()); }		
	}
}
