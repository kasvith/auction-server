import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kasun on 9/24/17.
 */
public class ServerManager {
    public static final int SERVER_PORT = 2000;
    private ClientServer server;

    public ServerManager() throws IOException {
        server = new ClientServer(SERVER_PORT);
    }

    public void start() throws Exception {
        Thread mainLoop = new Thread(server);
        mainLoop.start();
    }

    private class ClientServer implements Runnable {
        private ExecutorService executorService;
        private ServerSocket serverSocket;

        public ClientServer(int serverPort) {
            try {
                serverSocket = new ServerSocket(serverPort);
                executorService = Executors.newCachedThreadPool(); // setup multithreaded server container
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(100);
            }
        }

        @Override
        public void run() {
            while (true) {
                // Accept clients
                try {
                    Socket socket = serverSocket.accept();
                    executorService.submit(new ClientHandler(socket));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(103);
                }
            }
        }
    }
}
