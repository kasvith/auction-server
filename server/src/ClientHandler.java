import java.io.*;
import java.net.Socket;

/**
 * Created by kasun on 9/24/17.
 */
public class ClientHandler implements Runnable {
    private Socket socket;
    private Client client;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader reader;
        BufferedWriter writer;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            writer.write("Welcome to Auction Server\nStart bidding after setting your username\n\n");
            writer.flush();

            registerClient(reader, writer);
            setBidItem(reader, writer);
            String line;

            // Server loop
            while (true) {
                writer.write("Enter your bid : ");
                writer.flush();
                line = reader.readLine();
                boolean exit = (line.equals("exit") || line.equals("-1"));
                if (exit)
                    break;
                // start bidding

                try {
                    float bid = Float.parseFloat(line);
                    StockManager.getInstance().bidOnItem(client, bid);
                } catch (NumberFormatException e) {
                    writer.write("Invalid number format !!!.\n");
                    writer.flush();
                }
            }
            // end server loop
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(101);
        } finally {
            try {

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(3);
            }

        }
    }

    private void registerClient(BufferedReader reader, BufferedWriter writer) throws IOException {
        writer.write("Enter your username(cannot be changed) : ");
        writer.flush();
        String username = reader.readLine();

        while (username.isEmpty()) {
            writer.write("Enter your username(cannot be changed) : ");
            writer.flush();
            username = reader.readLine();
        }

        client = new Client(username);
        ClientManager.getInstance().registerClient(client);

        System.out.println("Info : Client " + username + " connected on port : " + socket.getPort());
    }

    private void setBidItem(BufferedReader reader, BufferedWriter writer) throws IOException {
        writer.write("Enter your Security Symbol for bidding : ");
        writer.flush();
        String response = reader.readLine();
        StockItem stockItem = new StockItem();

        while ((stockItem = StockManager.getInstance().searchStock(response)) == null) {
            writer.write("-1");
            writer.write("\nEnter your Security Symbol for bidding : ");
            writer.flush();

            response = reader.readLine();
        }

        client.setSsn(stockItem.getSymbol());
        writer.write("Current Cost of Security of " + client.getSsn() + " is " + stockItem.getPrice() + "\n");
        writer.flush();
    }
}
