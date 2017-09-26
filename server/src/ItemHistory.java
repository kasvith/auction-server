import java.util.Date;

/**
 * Created by kasun on 9/23/17.
 */
public class ItemHistory {
    private String clientName;
    private float price;
    private Date date;

    public ItemHistory(String clientName, float price, Date date) {
        this.clientName = clientName;
        this.price = price;
        this.date = date;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
