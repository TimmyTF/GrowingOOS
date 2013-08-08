package auctionsniper.ui;

import auctionsniper.Main;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

import javax.swing.table.AbstractTableModel;

/**
 * User: tflomin
 * Date: 06.08.13
 * Time: 10:48
 */
public class SnipersTableModel extends AbstractTableModel {
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("item-54321", 0, 0, SniperState.JOINING);
    private String statusText = Main.STATUS_JOINING;
    private SniperSnapshot snapshot = STARTING_UP;
    private final static String[] STATUS_TEXT = {
        "Joining", "Bidding", "Winning", "Lost", "Won"
    };

    public enum Column {
        ITEM_IDENTIFIER("Item") {
            @Override public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.itemId;
            }
        },
        LAST_PRICE("Last Price") {
            @Override public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.lastPrice;
            }
        },
        LAST_BID("Last Bid") {
            @Override public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.lastBid;
            }
        },
        SNIPER_STATE("State") {
            @Override public Object valueIn(SniperSnapshot snapshot) {
                return SnipersTableModel.textFor(snapshot.state);
            }
        };

        public final String name;

        private Column(String name) {
            this.name = name;
        }

        public static Column at(int offset) {
            return values()[offset];
        }

        abstract public Object valueIn(SniperSnapshot snapshot);
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    public int getColumnCount() {
        return Column.values().length;
    }

    public int getRowCount() {
        return 1;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshot);
    }

    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        snapshot = newSnapshot;
        fireTableRowsUpdated(0, 0);
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }
}
