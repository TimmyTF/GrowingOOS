package endtoend;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tflomin
 * Date: 20.06.13
 * Time: 11:50
 */
public class TestByteParse {
    @Test
    public void test() {
        String[] singleRow = {"EQBR", "GAZP,LKOH, SBER,  MSNG", "1, 2, 3,4,5, 6,  7,   8", ""};
        List<Byte> result = new ArrayList<Byte>();
        String[] periods = singleRow[2].split(",");
        for (String periodAsAString : periods)
            result.add(Byte.valueOf(periodAsAString.trim()));
        for (Byte ticker : result) {
            System.out.println("'" + ticker + "'");
        }
    }

    @Test
    public void testNumberOfInvocationsInAForEachLoop() {
        List<Byte> result = new ArrayList<Byte>();
        for (String periodAsAString : getStringArray())
            result.add(Byte.valueOf(periodAsAString.trim()));
        for (Byte ticker : result) {
            System.out.println("'" + ticker + "'");
        }
    }

    private String[] getStringArray() {
        System.out.println("Called...");
        String[] singleRow = {"EQBR", "GAZP,LKOH, SBER,  MSNG", "1, 2, 3,4,5, 6,  7,   8", ""};
        return singleRow[2].split(",");
    }
}
