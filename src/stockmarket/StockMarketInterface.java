package stockmarket;

public class StockMarketInterface {
	
	//The client used to connect to the stock market server
	Client c;
	//The unique id used to send commands
	String ID;
	
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
			ID ="";
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
	 * Buy shares
	 * @param number
	 * @param s
	 */
	public void Buy(double number,String company)
	{
		c.Write("BUY:" + company + ":" + Double.toString(number) + ":" + ID);
	}
	
	/**
	 * Display help messages
	 */
	public void Help()
	{ c.Write("HELP");  }
	
	
	/**
	 * Register with the stock market
	 */
	public void Register()
	{
		//REGI = string needed to register
		c.Write("REGI");
		//Sleep
		try {Thread.sleep(1000);}catch(Exception e){System.out.println(e.getMessage()); }
		//Get ID
		ID = c.Read().split(":")[2];
		//Print ID
		System.out.println("RECEIVED ID: " + ID); 
	}

}
