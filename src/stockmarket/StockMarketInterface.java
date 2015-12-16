package stockmarket;

import java.util.ArrayList;
import java.util.Scanner;

public class StockMarketInterface {
	
	//The client used to connect to the stock market server
	Client client;
	
	/*
	 * Contains stock market server information
	 */
	class ServerInformation{
		//Static variables hold stock market connection data
		public static final  String IP = "127.0.0.1"; //IP
		public static final int PORT = 5000; //PORT
		//Static variables for requesting functions from the server
		public static final String REGISTER = "REGI"; //REGISTER
		public static final String BUY = "BUY"; //BUY
		public static final String SELL = "SELL"; //SELL
		public static final String EXIT = "EXIT"; //EXIT
		public static final String DISPLAY = "DISP"; //DISPLAY
		public static final String CASH = "CASH"; //CASH
		public static final String HELP = "HELP"; //HELP
	}
	//Holds the unique id used to send commands
	private String ID = "";
	
	//Holds whether exit was requested
	private boolean exit_requested = false;
	
	//Share structure
	class Share
	{
		//Company
		private String company; 
		//Shares owned in company
		private double amount;
		//The price per share when last brought/sold
		private double pricepershare;
		
		/**
		 * Primary constructor
		 * @param company
		 * @param amount
		 * @param pricepershare
		 */
		public Share(String company,double amount,double pricepershare){
			this.company = company; this.amount = amount; this.pricepershare = pricepershare;
		}
		
		//GET/SET company name
		public String getCompany(){return company;} public void setCompany(String value){ company = value; }
		//GET/SET shares owned
		public double getAmount(){return amount;} public void setAmount(double value){ amount = value; }
		//GET/SET price-per-share
		public double getPricePerShare(){ return pricepershare; } public void setPricePerShare(double value){ pricepershare = value; }
		//Used to display share info
		public void Display(){
			System.out.println("SHARE");
			System.out.println("Company: " + company);
			System.out.println("Amount: " + amount);
			System.out.println("Price-per-Share: " + pricepershare);
		}
	}
	//Contains owned shares
	private ArrayList<Share> owned_shares = new ArrayList<Share>();
	
	
	
	/*
	 *  Virtual user - A 'BOT' to automatically BUY/SELL shares
	 */
	class VirtualUser extends Thread{
		
		//If true - the bot is allowed to start buying/selling shares
		private boolean isAllowed = false;
		//Running flag
		private boolean isRunning = true;
		//Holds company names
		private String[] names = null;
		
		/**
		 * Only a default constructor is required
		 */
		public VirtualUser(boolean allow){
			isAllowed = allow;
			names = this.getCompanyNames();
			this.start();
		}
		
		/**
		 * Main run method
		 */
		public void run(){
			System.out.println("Started virtual user.");
			//Keep looping
			while(isRunning){
				
				//If allowed - start buying/selling shares
				if(isAllowed == true && requesting_menu == false){
					System.out.println("Virtual user is trying to buy/sell shares");
					
					//double pershare = this.getPricePerShare(names[1]);
					//System.out.println(pershare);
					
					//Cycle through each company
					for(int i = 0; i < names.length;i++){
						String name = names[i];
						//Do we own a share for this company?
						if(hasShares(name)){
							//System.out.println("Has shares for company: " + name);
							
							//We need to see if the price-per-share has increased, then we can sell the share
							double pershare = this.getPricePerShare(name);
							Share _s = getShare(name);
							if(pershare > _s.getPricePerShare()){
								Sell(name,1);
								System.out.println("SOLD Share");
							}
							
						}
						//We don't own a share for this company.. we can buy one
						else
						{
							Buy(name,1);
							
							//System.out.println("Does not have shares for company : " + name);
							
							
						}
						
						
					}
					
					
					
					Wait(10000);
				}
				
				
			}
		}
		
		
		/**
		 * We may need to get company names
		 * @return
		 */
		public String[] getCompanyNames(){
			client.Write("COMPANYNAMES");
			Wait(1000);
			String _companies = client.Read();
			
			System.out.println("Got companies: " + _companies);
			if(_companies!=null){
				return _companies.split(":");
			}else{  return null;}
			
		}
		
		/**
		 * We may need the price-per-share value for a company
		 * @param company
		 * @return
		 */
		public double getPricePerShare(String company){
			try
			{
				client.Write("PRICEPERSHARE:" + company);
				Wait(1000);
				String _data = client.Read();
				System.out.println("Got price-per-share: " + _data);
				if(_data!=null&&_data!=" "){
				
			
					return Double.parseDouble(_data);
				}else{ return -1; }
			}catch(Exception e){ System.out.println("Error getting value"); return -1; }
		}
		
		/**
		 * Used to allow the bot to operate
		 */
		public void Allow(boolean flag){ isAllowed = flag; }
		
	}
	//We need an object for a virtual user
	private VirtualUser vu;
	
	//String holds the help menu
	private boolean requesting_menu = false;
	
	/**
	 * Main constructor
	 */
	public StockMarketInterface(){
		//Check for errors
		try
		{
			//Initialise the client
			client = new Client(ServerInformation.IP,ServerInformation.PORT);
			//ID may be initially empty
			ID = "";
			//We can register with the system
			this.Register();
			//Wait..
			Wait(1000);
			//Initialise the virtual user
			vu = new VirtualUser(false);
		}
		//Catch any errors..
		catch(Exception e){ System.out.println(e.getMessage()); }
		
	}
	
	/**
	 * Register with the stock market system
	 */
	public void Register(){
		//Write appropriate string to the client
		client.Write(ServerInformation.REGISTER);
		//Wait for response
		Wait(1000);
		//Gather the user ID
		ID = client.Read().split(":")[2];
		//Print the user ID - ensure that it is valid
		System.out.println("Received ID: " + ID);
		
	}
	
	/**
	 * Used to start the stock market interface
	 */
	public void Start(){
		/*
		 * Main header
		 */
		System.out.println("****************************************");
		System.out.println("-------------STOCK MARKET-----------------");
		System.out.println("****************************************");
		/*
		 * While loop holds while the user is using the system
		 */
		while(this.exit_requested == false){
			//Display the help menu
			this.Help();
			//Request user input
			String userInput = this.getInput("Enter option:");		
			
			/*
			 *  Check the user input.. which option was chosen?
			 * 
			 */
			switch(userInput){
				//Register chosen
				case ServerInformation.REGISTER: Register(); break;
				//Buy function chosen
				case ServerInformation.BUY: Buy(); break;
				//Sell function chosen
				case ServerInformation.SELL: Sell(); break;
				//Exit chosen
				case ServerInformation.EXIT: Exit(); break;
				//Display chosen
				case ServerInformation.DISPLAY: Display();  break;
				//Cash chosen
				case ServerInformation.CASH: Cash(); break;
				//Auto on chosen
				case "AUTO_ON": vu.Allow(true); Wait(1000); break;
				//Auto off chosen
				case "AUTO_OFF": vu.Allow(false); Wait(10000); break;
				//Owned chosen
				case "OWNED": this.Owned(); Wait(1000); break;
 			}
		}
	}
	
	/**
	 * Displays the help menu
	 */
	public void Help(){
		//Write appropriate string to the client
		client.Write(ServerInformation.HELP);
		requesting_menu = true;
	
		//Wait for response
		Wait(1000);
		requesting_menu = false;
		
	}
	
	/**
	 * Is called to exit the system
	 */
	public void Exit(){
		//Write appropriate string to the client
		client.Write(ServerInformation.EXIT);
		//Wait for response
		Wait(1000);
		//Set the exit request flag
		this.exit_requested = true;
		
	}
	
	/**
	 * Checks if own shares for specified company
	 * @param company
	 * @return
	 */
	public boolean hasShares(String company){
		for(int i = 0; i < owned_shares.size();i++){
			if(owned_shares.get(i).getCompany() == company){
				return true;
			}
		} return false;
	}
	
	/**
	 * Return an owned share for the specified company
	 * @param company
	 * @return
	 */
	public Share getShare(String company)
	{  
		for(int i = 0 ;i < owned_shares.size();i++){
			if(owned_shares.get(i).getCompany() == company){
				return owned_shares.get(i);
			}
		} return null;
		
	}
	
	/**
	 * Function is called to buy shares
	 */
	public void Buy(){
		try
		{
			/*
		 	* Get user input for company, and amount
		 	*/
			String company = this.getInput("Enter Company:");
			double amount = Double.parseDouble(this.getInput("Enter Amount:"));
			System.out.println("BUY details entered");
			System.out.println("COMPANY: " + company);
			System.out.println("AMOUNT: " + amount);
			Buy(company,amount);
		}catch(Exception e){ System.out.println("Error parsing input, unable to buy share"); }
	}
	public void Buy(String company,double amount){
		//Add to owned shares array
		double price_per_share = vu.getPricePerShare(company);
		
		
		boolean found = false;
		for(int i = 0 ; i < owned_shares.size();i++){
			if(owned_shares.get(i).getCompany() == company){
				double last_amount = owned_shares.get(i).getAmount();
				double new_amount = last_amount + amount;
				owned_shares.get(i).setAmount(new_amount);
				owned_shares.get(i).setPricePerShare(price_per_share);
			}
		}
		
		if(!found){
			owned_shares.add(new Share(company,amount,price_per_share));
		}
		
		//Write appropriate string to the client
		client.Write(this.createToken(ServerInformation.BUY + ":" + company + ":" + Double.toString(amount)));
		//Wait for response
		Wait(1000);
			
	}
	
	/**
	 * Function is called to sell shares
	 */
	public void Sell(){
		try
		{
			/*
		 	* Get user input for company, and amount
		 	*/
			String company = this.getInput("Enter Company:");
			double amount = Double.parseDouble(this.getInput("Enter Amount:"));
			System.out.println("SELL details entered");
			System.out.println("COMPANY: " + company);
			System.out.println("AMOUNT: " + amount);
			Sell(company,amount);
		}catch(Exception e){ System.out.println("Error parsing input, unable to sell share"); }
	}
	public void Sell(String company,double amount){
		
		double price_per_share = vu.getPricePerShare(company);
		
		//We need to check if the user owns the share that they are trying to sell...
		boolean valid = false;
		for(int i = 0 ; i < owned_shares.size();i++){
			if(owned_shares.get(i).getCompany().equals(company)){
				double last_amount = owned_shares.get(i).getAmount();
				System.out.println("last amount: " + last_amount);
				if(amount <= last_amount && last_amount > 0){
					
				
					double new_amount = last_amount - amount;
					owned_shares.get(i).setAmount(new_amount);
					owned_shares.get(i).setPricePerShare(price_per_share);
					
					if(new_amount == 0){
						owned_shares.remove(i);
					}
					
					valid = true;
				}
			}
		}
		
		
			//Write appropriate string to the client
			if(valid  == true){client.Write(this.createToken(ServerInformation.SELL + ":" + company + ":" + Double.toString(amount)));
			//Wait for response
			Wait(1000);
			}else{ System.out.println("Could not sell share"); }
			
	}
	
	/**
	 * Function is used to display shares
	 */
	public void Display(){
		//Write appropriate string to the client
		//client.Write(this.createToken(ServerInformation.DISPLAY));
		client.Write("PRICEPERSHARE:AVIVA");
		Wait(1000);
		String _data = client.Read();
		System.out.println("Got data: " + _data);
		
		//Wait for response
		//Wait(1000);
	}
	
	/**
	 * Function is used to display cash balance
	 */
	public void Cash(){
		//Write appropriate string to the client
		client.Write(this.createToken(ServerInformation.CASH));
		//Wait for response
		Wait(1000);
	}
	
	/**
	 * Function is used to list owned shares
	 */
	public void Owned(){
		
		//Display a title
		System.out.println("---------------");
		System.out.println("OWNED SHARES");
		System.out.println("---------------");
		
		//First check if the user owns any shares
		if(this.owned_shares.isEmpty()){ System.out.println("You don't own any shares"); }
		else
		{
			/*
			 * Cycle through the owned_shares array
			 */
			for(int i = 0; i < this.owned_shares.size();i++){
					//Display share
				this.owned_shares.get(i).Display();
			}
		}
		
		System.out.println("---------------");
	}
	
	/**
	 * Get user input
	 * @return
	 */
	public String getInput(String message)
	{
		if(message!=null){
			System.out.println(message);
		}
		
		
		//System.out.println("Waiting for input...");
		Scanner user_input = new Scanner(System.in);
		String input = user_input.next();
		System.out.println("Received input...: " + input);
		return input;
	}
	
	private String createToken(String message){
		return message + ":" + ID;
	}
	
	
	/**
	 * Used to wait
	 */
	public void Wait(long ms){
		try{ Thread.sleep(ms); }catch(Exception e){ System.out.println(e.getMessage()); }
	}

}
