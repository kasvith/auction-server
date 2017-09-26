import java.util.ArrayList;

/**
 * Created by kasun on 9/23/17.
 */
public class ClientManager {
    public static ClientManager clientManager;
    private ArrayList<Client> clients = new ArrayList<>();

    public static ClientManager getInstance() {
        if (clientManager == null)
            clientManager = new ClientManager();

        return clientManager;
    }

    public synchronized void registerClient(String name) {
        clients.add(new Client(name));
    }

    public synchronized void registerClient(Client client) {
        clients.add(client);
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public int getConnectedClients() {
        return clients.size();
    }
}
