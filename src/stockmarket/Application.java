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
				
					Server s = new Server();
					
					
				
			}
		})).start();
		
		try {
			Client c = new Client("127.0.0.1",5000);
			c.Write("HELO");
			c.Write("EXIT");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
