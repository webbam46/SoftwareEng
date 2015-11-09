package stockmarket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class StockMarket implements Runnable
{

    private static StockMarket ref;
    private String[][] stockData; 
    private String filename = "stocks.csv";
    private String delims = ",";
    private String[] tokens;

    private Random rnd;

    private final long PERIOD = 5000L;
    private long lastTime;
    private long currentTime;

    private StockMarket()
    {
        stockData = new String[10][4];
        populateStockData();
        lastTime = System.currentTimeMillis() - PERIOD;
        rnd = new Random();
    }


    public static StockMarket getStockMarket()
    {
        if(ref == null)
        {
            ref = new StockMarket();
        }
        return ref;
        
    }

    private void populateStockData()
    {
        String line;
        int count = 0;

        try
        {
            FileReader fileReader = new FileReader(filename);
            BufferedReader in = new BufferedReader(fileReader);
            
            while((line = in.readLine()) != null)
            {
                tokens = line.split(delims);
                for(int i = 0; i < tokens.length; i++)
                {
                    stockData[count][i] = tokens[i];
                }
                count++;
            }
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong: " + e);
        }

        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 3; j++)
                System.out.println("populate: "+i+":"+j+" with: "+stockData[i][j]);

    }

    public String[][] getStockMarketState()
    {
        return stockData;
    }

    public void run()
    {
        while(true)
        {
            currentTime = System.currentTimeMillis();


            if((currentTime - lastTime) >= PERIOD)
            {
                lastTime = currentTime;

                System.out.println("Stock Market updated each 5s.");
                updateStockPrice();
            }
        }

    }

    private void updateStockPrice()
    {
        double change = 0.0;

        for(int i = 0; i < stockData.length; i++)
        {
            if(rnd.nextBoolean())
            {
                change = rnd.nextInt(11)*rnd.nextDouble();
                double aVal = Double.parseDouble(stockData[i][1]);

                if(rnd.nextBoolean())
                {
                      aVal += change;
                      stockData[i][3] = ""+change;
                }
                else
                {
                      aVal -= change;
                      stockData[i][3] = ""+(change * -1);
                }
                      stockData[i][1] = ""+aVal;

                System.out.println("UPD:"+stockData[i][0]+":"+stockData[i][1]+":"+stockData[i][3]);
            }
        }
    }
}
