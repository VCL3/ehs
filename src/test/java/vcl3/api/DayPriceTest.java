package vcl3.api;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by wliu on 2/1/18.
 */
public class DayPriceTest {

    @Test
    public void testCalculateChangePercent() {
        Double open = 10.00;
        Double close = 12.00;
        Double changePercent = DayPrice.calculateChangePercent(open, close);
        Assert.assertTrue(20.0 == changePercent);

        open = 10.02;
        close = 13.14;
        changePercent = DayPrice.calculateChangePercent(open, close);
        Assert.assertTrue(31.138 == changePercent);
    }


}
