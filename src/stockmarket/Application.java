package stockmarket;

import java.io.IOException;

public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * 
		 * 
		 * 
		 * Un-comment to run on same project,
		 * or copy over to new project to run on local-host
		 */
		
		
		/*
		 * The server will be started here
		 */
		//(new Thread(new Runnable() {
		//	
		//	@Override
		//	public void run() {
		//		// TODO Auto-generated method stub
		//		
		//		 	Server mySTS = new Server();
		//	        mySTS.initSTS();
		//	        mySTS.listenForClients();	
		//	}
		//})).start();
		
		//Stock market interface
		StockMarketInterface stockMarketInt = new StockMarketInterface();
		//Start the interface
		stockMarketInt.Start();
	}

}
