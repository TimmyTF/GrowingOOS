package auctionsniper.ui;

import auctionsniper.Main;
import auctionsniper.SniperSnapshot;

import javax.swing.*;
import java.awt.*;

/**
 * User: tflomin
 * Date: 16.07.13
 * Time: 17:32
 */
public class MainWindow extends JFrame {
    private final SnipersTableModel snipers = new SnipersTableModel();

    private static final String APPLICATION_TITLE = "Auction Sniper"; // TODO
    private static final String SNIPERS_TABLE_NAME = "Snipers table"; // TODO

    public MainWindow() {
        super(APPLICATION_TITLE);
        setName(Main.MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    public void showStatusText(String statusText) {
        snipers.setStatusText(statusText);
    }

    public void sniperStatusChanged(SniperSnapshot sniperSnapshot) {
        snipers.sniperStateChanged(sniperSnapshot);
    }
}
