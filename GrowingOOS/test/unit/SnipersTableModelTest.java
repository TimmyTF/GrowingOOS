package unit;

import auctionsniper.AuctionSniper;
import auctionsniper.SniperSnapshot;
import auctionsniper.ui.SnipersTableModel;
import auctionsniper.ui.UserRequestListener;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertEquals;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * User: tflomin
 * Date: 06.08.13
 * Time: 15:32
 */
@RunWith(JMock.class)
public class SnipersTableModelTest {
    private final Mockery context = new Mockery();
    private TableModelListener listener = context.mock(TableModelListener.class);
    private final SnipersTableModel model = new SnipersTableModel();
    private static final String ITEM_ID = "item 0";
    private final AuctionSniper sniper = new AuctionSniper(new UserRequestListener.Item(ITEM_ID, 234), null);

    @Before
    public void attachModelListener() {
        model.addTableModelListener(listener);
    }

    @Test
    public void hasEnoughColumns() {
        assertThat(model.getColumnCount(), equalTo(SnipersTableModel.Column.values().length));
    }

    @Test
    public void setsUpColumnHeadings() {
        for (SnipersTableModel.Column column: SnipersTableModel.Column.values()) {
            assertEquals(column.name, model.getColumnName(column.ordinal()));
        }
    }

    @Test public void
    notifiesListenersWhenAddingASniper() {
        context.checking(new Expectations() { {
            one(listener).tableChanged(with(anInsertionAtRow(0)));
        }});

        assertEquals(0, model.getRowCount());

        model.sniperAdded(sniper);

        assertEquals(1, model.getRowCount());
        assertRowMatchesSnapshot(0, SniperSnapshot.joining(ITEM_ID));
    }

    @Test public void
    setsSniperValuesInColumns() {
        SniperSnapshot bidding = sniper.getSnapshot().bidding(555, 666);
        context.checking(new Expectations() {{
            allowing(listener).tableChanged(with(anyInsertionEvent()));

            one(listener).tableChanged(with(aChangeInRow(0)));
        }});

        model.sniperAdded(sniper);
        model.sniperStateChanged(bidding);

        assertRowMatchesSnapshot(0, bidding);
    }

    Matcher<TableModelEvent> anyInsertionEvent() {
        return hasProperty("type", equalTo(TableModelEvent.INSERT));
    }

    private Matcher<TableModelEvent> aChangeInRow(int row) {
        return samePropertyValuesAs(new TableModelEvent(model, row));
    }

    private void assertColumnEquals(SnipersTableModel.Column column, Object expected) {
        final int rowIndex = 0;
        final int columnIndex = column.ordinal();
        assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
    }

    private Matcher<TableModelEvent> aRowChangedEvent() {
        return samePropertyValuesAs(new TableModelEvent(model, 0));
    }

    Matcher<TableModelEvent> anInsertionAtRow(final int row) {
        return samePropertyValuesAs(new TableModelEvent(model, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
        assertEquals(snapshot.itemId, cellValue(row, SnipersTableModel.Column.ITEM_IDENTIFIER));
        assertEquals(snapshot.lastPrice, cellValue(row, SnipersTableModel.Column.LAST_PRICE));
        assertEquals(snapshot.lastBid, cellValue(row, SnipersTableModel.Column.LAST_BID));
        assertEquals(SnipersTableModel.textFor(snapshot.state), cellValue(row, SnipersTableModel.Column.SNIPER_STATE));
    }

    private Object cellValue(int rowIndex, SnipersTableModel.Column column) {
        return model.getValueAt(rowIndex, column.ordinal());
    }
}
