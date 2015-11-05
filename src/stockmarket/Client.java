package stockmarket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Client class
 * 
 *
 */
public class Client extends Socket {
	
	//Handles data output
	DataOutputStream out = new DataOutputStream(this.getOutputStream());
	//Handles data input
	BufferedReader in = new BufferedReader(new InputStreamReader(this.getInputStream()));
	
	/**
	 * Primary constructor
	 * @param ip
	 * @param port
	 * @throws IOException
	 */
	public Client(String ip,int port) throws IOException{ 
		super(ip,port);
		
	}
	
	/**
	 * Write a string to the server
	 * @param string
	 */
	public void Write(String string){
		//Use exception handling to check for errors
		try {
			//Write the string to the server
			out.writeChars(string);
		//Catch the errors
		} catch (IOException e) {
			//Print the error
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	

}
