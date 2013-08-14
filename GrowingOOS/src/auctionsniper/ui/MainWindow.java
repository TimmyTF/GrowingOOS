package auctionsniper.ui;

import auctionsniper.Main;
import auctionsniper.SniperPortfolio;
import auctionsniper.SniperSnapshot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * User: tflomin
 * Date: 16.07.13
 * Time: 17:32
 */
public class MainWindow extends JFrame {
    private SnipersTableModel snipers;

    public static final String APPLICATION_TITLE = "Auction Sniper";
    private static final String SNIPERS_TABLE_NAME = "Snipers table";

    private final Announcer<UserRequestListener> userRequests = Announcer.to(UserRequestListener.class);

    public MainWindow(SniperPortfolio portfolio){
        super("Auction Sniper");
        setName(Main.MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable(portfolio), makeControls());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable, JPanel controls) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(controls, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable(SniperPortfolio portfolio) {
        SnipersTableModel model = new SnipersTableModel();
        portfolio.addPortfolioListener(model);
        JTable snipersTable = new JTable(model);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    public void sniperStatusChanged(SniperSnapshot sniperSnapshot) {
        snipers.sniperStateChanged(sniperSnapshot);
    }

    private JPanel makeControls() {
        final JTextField itemIdField = itemIdField();
        final JFormattedTextField stopPriceField = stopPriceField();

        JPanel controls = new JPanel(new FlowLayout());
        controls.add(itemIdField);
        controls.add(stopPriceField);

        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(Main.JOIN_BUTTON_NAME);

        joinAuctionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userRequests.announce().joinAuction(new UserRequestListener.Item(itemId(), stopPrice()));
            }

            private String itemId() {
                return itemIdField.getText();
            }

            private int stopPrice() {
                return ((Number) stopPriceField.getValue()).intValue();
            }
        });
        controls.add(joinAuctionButton);

        return controls;
    }

    private JTextField itemIdField() {
        JTextField itemIdField = new JTextField();
        itemIdField.setColumns(10);
        itemIdField.setName(Main.NEW_ITEM_ID_NAME);
        return itemIdField;
    }

    private JFormattedTextField stopPriceField() {
        JFormattedTextField stopPriceField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        stopPriceField.setColumns(7);
        stopPriceField.setName(Main.NEW_ITEM_STOP_PRICE_NAME);
        return stopPriceField;
    }

    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userRequests.addListener(userRequestListener);
    }
}
