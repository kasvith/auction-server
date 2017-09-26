import javax.swing.*;

public class Server {
    public static void main(String[] args) {
        ServerView ui = new ServerView();
        ui.setVisible(true);
        ui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ui.setResizable(false);

        try {
            ServerManager manager = new ServerManager();
            manager.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(132);
        }
    }
}
