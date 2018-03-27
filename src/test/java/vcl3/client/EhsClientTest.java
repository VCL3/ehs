/**
 * Created by wliu on 2/1/18.
 */
package vcl3.client;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import vcl3.core.CommonHelper;

public class EhsClientTest {

    @Test
    public void testEhsClient() throws Exception {

//        EhsClient ehsClient = new EhsClient();
    }

    @Test
    public void testGetPreviousTradingDay() throws Exception {
        String date = "2018-01-29";
        DateTime dateTime = DateTime.parse(date, CommonHelper.dateTimeFormatter);
        DateTime prev = EhsClient.getPreviousTradingDateTime(dateTime);
        Assert.assertEquals(DateTime.parse("2018-01-26", CommonHelper.dateTimeFormatter), prev);
    }

}
