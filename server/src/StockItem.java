import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kasun on 9/23/17.
 */
public class StockItem {
    private String symbol = "";
    private String securityName = "";
    private float price = 0f;
    private ArrayList<ItemHistory> history = new ArrayList<>();

    public StockItem() {
    }

    public StockItem(String symbol, String securityName, float price) {
        this.symbol = symbol;
        this.securityName = securityName;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSecurityName() {
        return securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public synchronized void updateHistory(Client client, float price, Date date) {
        history.add(new ItemHistory(client.getName(), price, date));
    }

    public ArrayList<ItemHistory> getHistory() {
        return history;
    }
}
