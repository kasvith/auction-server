import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kasun on 9/26/17.
 */
public class HistoryView extends JFrame {
    StockItem item;
    DefaultTableModel tableModel;
    JTable table;

    public HistoryView(StockItem item) {
        this.item = item;
        setTitle(item.getSymbol() + " - " + item.getSecurityName() + " : Bid History");
        setPreferredSize(new Dimension(600, 400));
        setSize(new Dimension(600, 400));
        setResizable(false);
        init();
    }

    private void init() {
        String[] cols = {"Client", "Bid Price", "Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setBounds(0, 0, 600, 400);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        refresh();

        JLabel label = new JLabel("Refreshed in each 500ms");
        add(label, BorderLayout.SOUTH);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, 0, 500);
    }

    private void refresh() {
        tableModel.getDataVector().removeAllElements();

        for (ItemHistory history : item.getHistory()) {
            String[] obj = {history.getClientName(), Float.toString(history.getPrice()), history.getDate().toString()};
            tableModel.addRow(obj);
        }
    }
}
