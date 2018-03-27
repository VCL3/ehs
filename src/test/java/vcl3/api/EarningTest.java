/**
 * Created by wliu on 2/1/18.
 */
package vcl3.api;

import org.junit.Test;
import vcl3.core.JsonHelper;

public class EarningTest {

    @Test
    public void testDeserialization() throws Exception {
        String json = "{\n" +
                "            \"actualEPS\": 2.07,\n" +
                "            \"consensusEPS\": 1.87,\n" +
                "            \"estimatedEPS\": 1.87,\n" +
                "            \"announceTime\": \"AMC\",\n" +
                "            \"numberOfEstimates\": 10,\n" +
                "            \"EPSSurpriseDollar\": 0.2,\n" +
                "            \"EPSReportDate\": \"2017-11-02\",\n" +
                "            \"fiscalPeriod\": \"Q4 2017\",\n" +
                "            \"fiscalEndDate\": \"2017-09-30\"\n" +
                "        }";

        Earning earning = JsonHelper.fromJson(json, Earning.class);
        System.out.println(JsonHelper.toJson(earning));
    }
}
