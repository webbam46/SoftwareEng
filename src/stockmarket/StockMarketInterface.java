package stockmarket;

public class StockMarketInterface {
	
	//The client used to connect to the stock market server
	Client c;
	//The unique id used to send commands
	int ID;
	
	/**
	 * Main constructor
	 */
	public StockMarketInterface()
	{
		//Handles any errors
		try
		{
			//Init the client
			c = new Client("127.0.0.1",5000);
			//Just keep as 0 for now
			ID =0 ;
			//We can register when the interface is initialised
			Register();
			
		}catch(Exception e){ System.out.println(e.getMessage()); }
		
	}
	
	/**
	 * Display the stock market
	 */
	public void Display()
	{
		//DISP -- Display stock market
		c.Write("DISP");
	}
	
	
	
	/**
	 * Register with the stock market
	 */
	public void Register()
	{
		//REGI = string needed to register
		c.Write("REGI");
	}

}
