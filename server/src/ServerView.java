import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Created by kasun on 9/23/17.
 */
public class ServerView extends JFrame {
    private final int updateInterval = 500; // Milliseconds
    private String[] columns = {"Symbol", "Security Name", "Price"};
    private String[] ssnFilter = {"FB", "VRTU", "MSFT", "GOOGL", "YHOO", "XLNX", "TSLA", "TXN"};
    private JTable table;
    private StockTableModel stockTableModel;
    private Timer timer;
    private JLabel infoLabel;

    public ServerView() {
        setTitle("Stock Manager - Server");
        setPreferredSize(new Dimension(800, 400));
        setSize(new Dimension(800, 400));
        init();
    }

    public void init() {
        stockTableModel = new StockTableModel(columns, 0);
        table = new JTable(stockTableModel);
        table.setBounds(0, 0, 800, 400);
        JScrollPane jScrollPane = new JScrollPane(table);
        ListSelectionModel listSelectionModel = table.getSelectionModel();
        listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(jScrollPane, BorderLayout.CENTER);

        // Info panel
        JPanel utilsPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(utilsPanel, BoxLayout.X_AXIS);

        JButton manualRefresh = new JButton("Refresh");
        utilsPanel.add(manualRefresh);

        infoLabel = new JLabel();
        utilsPanel.add(infoLabel);

        utilsPanel.setLayout(boxLayout);
        add(utilsPanel, BorderLayout.SOUTH);
        // end of info panel

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    String symbol = (String) target.getValueAt(target.getSelectedRow(), 0);
                    HistoryView historyView = new HistoryView(
                            StockManager.getInstance().searchStock(symbol)
                    );
                    historyView.setVisible(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = table.getSelectedRow();

                try {
                    float val = Float.parseFloat((String) table.getValueAt(row, 2));
                    StockManager.getInstance().updatePrice((String) table.getValueAt(row, 0), val);
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(null, "Invalid Number", "Stock Manager", JOptionPane.ERROR_MESSAGE);
                    StockItem stockItem = StockManager.getInstance().searchStock((String) table.getValueAt(row, 0));
                    table.setValueAt(Float.toString(stockItem.getPrice()), row, 2);
                } catch (Exception ex) {
                    // Pass exceptions
                }
            }
        });

        refresh();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int clients = ClientManager.getInstance().getConnectedClients();
                infoLabel.setText((clients > 0 ? clients + " Clients" : ""));
            }
        }, 0, 10);
    }

    private void refresh() {
        stockTableModel.getDataVector().removeAllElements();
        try {
            for (String ssn : ssnFilter) {
                // Fill the display with the required data
                StockItem item = StockManager.getInstance().searchStock(ssn);
                String[] obj = {item.getSymbol(), item.getSecurityName(), Float.toString(item.getPrice())};
                stockTableModel.addRow(obj);

                int clients = ClientManager.getInstance().getConnectedClients();
                infoLabel.setText((clients > 0 ? clients + " Clients" : ""));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    class StockTableModel extends DefaultTableModel {
        /**
         * Constructs a default <code>DefaultTableModel</code>
         * which is a table of zero columns and zero rows.
         */
        public StockTableModel() {
        }

        /**
         * Constructs a <code>DefaultTableModel</code> with
         * <code>rowCount</code> and <code>columnCount</code> of
         * <code>null</code> object values.
         *
         * @param rowCount    the number of rows the table holds
         * @param columnCount the number of columns the table holds
         * @see #setValueAt
         */
        public StockTableModel(int rowCount, int columnCount) {
            super(rowCount, columnCount);
        }

        /**
         * Constructs a <code>DefaultTableModel</code> with as many columns
         * as there are elements in <code>columnNames</code>
         * and <code>rowCount</code> of <code>null</code>
         * object values.  Each column's name will be taken from
         * the <code>columnNames</code> vector.
         *
         * @param columnNames <code>vector</code> containing the names
         *                    of the new columns; if this is
         *                    <code>null</code> then the model has no columns
         * @param rowCount    the number of rows the table holds
         * @see #setDataVector
         * @see #setValueAt
         */
        public StockTableModel(Vector columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        /**
         * Constructs a <code>DefaultTableModel</code> with as many
         * columns as there are elements in <code>columnNames</code>
         * and <code>rowCount</code> of <code>null</code>
         * object values.  Each column's name will be taken from
         * the <code>columnNames</code> array.
         *
         * @param columnNames <code>array</code> containing the names
         *                    of the new columns; if this is
         *                    <code>null</code> then the model has no columns
         * @param rowCount    the number of rows the table holds
         * @see #setDataVector
         * @see #setValueAt
         */
        public StockTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        /**
         * Constructs a <code>DefaultTableModel</code> and initializes the table
         * by passing <code>data</code> and <code>columnNames</code>
         * to the <code>setDataVector</code> method.
         *
         * @param data        the data of the table, a <code>Vector</code>
         *                    of <code>Vector</code>s of <code>Object</code>
         *                    values
         * @param columnNames <code>vector</code> containing the names
         *                    of the new columns
         * @see #getDataVector
         * @see #setDataVector
         */
        public StockTableModel(Vector data, Vector columnNames) {
            super(data, columnNames);
        }

        /**
         * Constructs a <code>DefaultTableModel</code> and initializes the table
         * by passing <code>data</code> and <code>columnNames</code>
         * to the <code>setDataVector</code>
         * method. The first index in the <code>Object[][]</code> array is
         * the row index and the second is the column index.
         *
         * @param data        the data of the table
         * @param columnNames the names of the columns
         * @see #getDataVector
         * @see #setDataVector
         */
        public StockTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        /**
         * Returns true regardless of parameter values.
         *
         * @param row    the row whose value is to be queried
         * @param column the column whose value is to be queried
         * @return true
         * @see #setValueAt
         */
        @Override
        public boolean isCellEditable(int row, int column) {
            switch (column) {
                case 2:
                    return true;
                default:
                    return false;
            }
        }
    }
}
