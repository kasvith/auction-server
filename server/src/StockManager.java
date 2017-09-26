import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by kasun on 9/23/17.
 */
public class StockManager {
    // Singleton
    public static StockManager stockManager;
    private HashMap<String, StockItem> stock = new HashMap<>();

    public StockManager() {
        loadStocks("stocks.csv");
    }

    public static StockManager getInstance() {
        if (stockManager != null)
            return stockManager;

        stockManager = new StockManager();
        return stockManager;
    }

    public void loadStocks(String fileName) {
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(fileName));
            String line;
            br.readLine(); // Skip the header

            // read the rest
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length > 0) {
                    stock.put(data[0], new StockItem(data[0], data[1], Float.parseFloat(data[data.length - 1])));
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(101);
        }
    }

    public StockItem searchStock(String symbol) {
        if (!stock.containsKey(symbol))
            return null;

        return stock.get(symbol);
    }

    public synchronized void bidOnItem(Client client, float price) throws Exception {
        if (!stock.containsKey(client.getSsn()))
            throw new Exception("Item not found");

        StockItem stockItem = stock.get(client.getSsn());
        stockItem.updateHistory(client, price, new Date());
    }

    public synchronized void updatePrice(String ssn, float price) throws Exception {
        if (!stock.containsKey(ssn))
            throw new Exception("Item not found");

        stock.get(ssn).setPrice(price);
    }
}
