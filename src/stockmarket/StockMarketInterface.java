package stockmarket;

import java.util.ArrayList;
import java.util.Scanner;

import stockmarket.StockMarketInterface.Share;

public class StockMarketInterface {
	
	/**
	 * Share class
	 * @author Computing
	 *
	 */
	class Share
	{
		// Constructor
		public Share(double number,String company,Double value)
		{ System.out.println("New owned share: " + number + " " + company); this.number = number; this.company = company;
		this.value = value;
		}
		
		//Display share
		public void Display()
		{
			System.out.println("SHARE");
			System.out.println("Number: " + number);
			System.out.println("Company: " + company);
			System.out.println("Value-per-share: " + value);
			System.out.println("Total value: " + getTotalValue());
			System.out.println("--------------");
		}
		
		/**
		 * Check share against existing share value
		 * if value is higher - we can sell the share
		 */
		public boolean CheckHigher(double new_value)
		{
			double new_total = number * new_value;
			System.out.println("Checking share value");
			System.out.println("OLD: " + getTotalValue());
			System.out.println("NEW: " + new_total);
			
			if((getTotalValue())  < new_total){ return true; }
			else{ return false; }
		}
		
		/**
		 * Check share against existing share value
		 * if value is lower - we can sell the share
		 */
		public boolean CheckLower(double new_value)
		{
			double new_total = number * new_value;
			System.out.println("Checking share value");
			System.out.println("OLD: " + getTotalValue());
			System.out.println("NEW: " + new_total);
			
			if(getTotalValue() > new_total){ return true; }
			else{ return false; }
		}
		
		//Number owned
		double number;
		//Company
		String company;
		//value
		double value;
		
		public double getNumber(){ return number; }
		public String getCompany(){ return company; }
		public double getValue(){ return value; }
		public void setNumber(double val){number=val;}
		public void setCompany(String val){company=val;}
		public void setValue(double val){value=val;}
		
		public double getTotalValue(){ return number * value; }
		
	}
	
	//The client used to connect to the stock market server
	Client c;
	//The unique id used to send commands
	String ID;
	//Holds shares owned
	ArrayList<Share> owned_shares = new ArrayList<Share>();
	//If true - the system will automatically buy/sell shares
	public static boolean AUTO_FUNC = false;
	//Holds current money owned
	public double money = 100000;
	
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
			//Wait..
			Wait(1000);
			
		}catch(Exception e){ System.out.println(e.getMessage()); }
	}
	
	/**
	 * Start the user interface
	 */
	public void Start()
	{
		System.out.println("****************************************");
		System.out.println("-------------STOCK MARKET-----------------");
		System.out.println("****************************************");
		Check();
		while(true)
		{
			//Display help menu
			Help(); 
			//Wait for user input
			String input = getInput();
			
			/**
			 * Check user input
			 */
			//EXIT SERVER
			if(input.equals("EXIT")){ this.Exit(); break; }
			//DISPLAY
			if(input.equals("DISP")){ this.Display(); }
			//BUY SHARES
			if(input.equals("BUY")){ 
				if(AUTO_FUNC==false)
				{
				this.Buy();
				}else
					{
						System.out.println("Turn off AUTO before using this option");
					}
			}
			//SELL SHARES
			if(input.equals("SELL")){ 
				if(AUTO_FUNC==false)
				{
				this.Sell();
				}else{ System.out.println("Turn off AUTO before using this option"); }
			}
			//CURRENTLY OWNED shares
			if(input.equals("CURRENT")){ this.displayCurrent(); }
			//TURN AUTO ON
			if(input.equals("AUTO_ON")){ 
				System.out.println("AUTO TURNED ON");
				AUTO_FUNC = true;
			}
			//TURN AUTO OFf
			if(input.equals("AUTO_OFF")){
				System.out.println("AUTO TURNED OFF");
				AUTO_FUNC  =false;
			}
			
		}
		System.out.println("****************************************");
	}
	
	/**
	 * We can use a different thread to automatically buy/sell shares - if required
	 */
	public void Check()
	{
		Thread _auto = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true)
				{
					if(StockMarketInterface.AUTO_FUNC)
					{
						try{Thread.sleep(5000);
							System.out.println("Trying to buy/sell shares");
							
							if(Buy(1,"Microsoft"))
							{ System.out.println("AUTOMATICALLY BROUGHT SHARE");
							System.out.println("MONEY: " + money);
							}
							
							
							Thread.sleep(20000);
							
							

							if(Sell(1,"Microsoft"));
							{ System.out.println("AUTOMATICALLY SOLD SHARE"); 
								System.out.println("MONEY: " + money);
							}
					
						}catch(Exception e){ System.out.println(e.getMessage()); 
						System.out.println(e.getMessage()); }
						
					}
				}
				
			}
		});
		
		_auto.start();
		
	}
	
	/**
	 * Get user input
	 * @return
	 */
	public String getInput()
	{
		//System.out.println("Waiting for input...");
		Scanner user_input = new Scanner(System.in);
		String input = user_input.next();
		System.out.println("Received input...: " + input);
		return input;
	}
	
	public void Wait(long ms)
	{ try{ Thread.sleep(ms); }catch(Exception e){ System.out.println(e.getMessage()); } } 
	
	/**
	 * Display the stock market
	 */
	public void Display()
	{
		//DISP -- Display stock market
		c.Write("DISP:" + ID); 
	}
	
	/**
	 * Buy shares
	 * 
	 */
	public void Buy()
	{
		
		System.out.println("Enter number: ");
		double num = Double.parseDouble(this.getInput());
		System.out.println("Enter company: ");
		String company = this.getInput();
		Buy(num,company);
		
		
	}
	/**
	 * Buy shares
	 * @param number
	 * @param company
	 */
	public boolean Buy(double number,String company)
	{
		boolean exists = false;
		
		c.Write("BUY:" + company + ":" + Double.toString(number) + ":" + ID);
		Wait(1000);
		String buy_msg = c.Read();
		String share_value = buy_msg.split(":")[4].split("@")[1];
		double _share_value = Double.parseDouble(share_value);
		System.out.println("BUYMSG" + share_value);
		
		//Can we buy the shares with our money?
		if( ((money - (number * _share_value)) > 0 ))
		{
			//Cycle shares
			
			for(Share s : owned_shares)
			{
				if(s.getCompany().equals(company))
				{
					//Only sell if value is higher
					if(s.CheckLower(_share_value))
					{
						exists =true;
						s.setNumber(s.getNumber() + number);
						//Decrease money
						money -= (number * _share_value);
						s.setValue(_share_value);
						return true;
					}
				}
			}
			
			if(!exists){ owned_shares.add(new Share(number,company,_share_value));
			money -= (number * _share_value); return true; }
		}else{ System.out.println("Cannot buy share.. not enough money"); }
		return false;
		
	}
	/**
	 * Sell shares
	 */
	public void Sell()
	{	
		System.out.println("Enter number: ");
		double num = Double.parseDouble(this.getInput());
		System.out.println("Enter company: ");
		String company = this.getInput();
		Sell(num,company);
	}
	/**
	 * Sell shares
	 * @param number
	 * @param company
	 */
	public boolean Sell(double number,String company)
	{
		//If given number is above 0
		if(number>0)
		{
				c.Write("SELL:" + company + ":" + Double.toString(number) + ":" + ID);
				Wait(1000);
				String buy_msg = c.Read();
				String share_value = buy_msg.split(":")[4].split("@")[1];
				double _share_value = Double.parseDouble(share_value);
				
				//If own shares
				if(owned_shares.size()>0)
				{
					//Cycle shares
					for(Share s : owned_shares)
					{
						//Does the company exist?
						if(s.getCompany().equals(company))
						{
							//If stocks owned
							if(s.getNumber()> 0 && number <= s.getNumber())
							{
								//Only sell if value is higher
								if(s.CheckHigher(_share_value))
								{
									//Print debug message
									System.out.println("Accepted, selling " + number + " shares for company " + company + " with value " + _share_value);
									//Update share value number
									s.setNumber(s.getNumber() - number);
									//Update money
									money += (number * _share_value);
									s.Display();
									return true;
								}else{  }
							
							}else{  }
					
						}else{   }
					}
			}else{  }
			
		}
		return false;
	}
	
	/**
	 * Display currently owned shares
	 */
	public void displayCurrent()
	{
		if(!owned_shares.isEmpty())
		{
			System.out.println("Enter company: ");
			String company = this.getInput();
			
			for(Share s : owned_shares)
			{
				if(s.getCompany().equals(company))
				{
					s.Display();
				}
			}
		}else{ System.out.println("You don't own any shares"); }
	}
	
	/**
	 * Display help messages
	 */
	public void Help()
	{ c.Write("HELP"); Wait(1000); System.out.println("AUTO_ON:");
	System.out.println("AUTO_OFF:");
	System.out.println(" -- CURRENT MONEY: " + money);}
	
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
	
	/**
	 * Exit the server.. close the connection
	 */
	public void Exit(){ c.Write("EXIT"); Wait(1000); c.Stop(); }
	
	
	
}
