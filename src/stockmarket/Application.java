package stockmarket;

import java.io.IOException;

public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		 * The server will be started here
		 */
		(new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				 	Server mySTS = new Server();
			        mySTS.initSTS();
			        mySTS.listenForClients();
					
					
				
			}
		})).start();
		
		try {
			Client c = new Client("127.0.0.1",5000);
			c.Write("HELP");
			c.Write("REGI");
			c.Write("DISP");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
