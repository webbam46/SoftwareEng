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
		public Share(double number,String company)
		{ System.out.println("New owned share: " + number + " " + company); this.number = number; this.company = company; }
		
		//Display share
		public void Display()
		{
			System.out.println("SHARE");
			System.out.println("Number: " + number);
			System.out.println("Company: " + company);
			System.out.println("--------------");
		}
		
		//Number owned
		double number;
		//Company
		String company;
		
		public double getNumber(){ return number; }
		public String getCompany(){ return company; }
		public void setNumber(double val){number=val;}
		public void setCompany(String val){company=val;}
	}
	
	//The client used to connect to the stock market server
	Client c;
	//The unique id used to send commands
	String ID;
	//Holds shares owned
	ArrayList<Share> owned_shares = new ArrayList<Share>();
	
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
			if(input.equals("BUY")){ this.Buy(); }
			//SELL SHARES
			if(input.equals("SELL")){ this.Sell(); }
			//CURRENTLY OWNED shares
			if(input.equals("CURRENT")){ this.displayCurrent(); }
			
			
		}
		System.out.println("****************************************");
		
		
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
	public void Buy(double number,String company)
	{
		boolean exists = false;
		
			c.Write("BUY:" + company + ":" + Double.toString(number) + ":" + ID);
			
			for(Share s : owned_shares)
			{
				if(s.getCompany().equals(company))
				{
					exists =true;
					s.setNumber(s.getNumber() + number);
				}
			}
			
			if(!exists){ owned_shares.add(new Share(number,company));}
		
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
	public void Sell(double number,String company)
	{
			
		
			c.Write("SELL:" + company + ":" + Double.toString(number) + ":" + ID);
			
			for(Share s : owned_shares)
			{
				if(s.getCompany().equals(company))
				{
					s.setNumber(s.getNumber() - number);
				}
			}
	
		
	}
	
	
	
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
	
	/**
	 * Exit the server.. close the connection
	 */
	public void Exit(){ c.Write("EXIT"); Wait(1000); c.Stop(); }
	
	
	
}
