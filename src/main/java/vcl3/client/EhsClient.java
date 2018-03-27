/**
 * Created by wliu on 1/31/18.
 */
package vcl3.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import vcl3.api.DayPrice;
import vcl3.api.Earning;
import vcl3.core.CommonHelper;
import vcl3.core.JsonHelper;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class EhsClient {

    private static final String earningsEndpoint = "https://api.iextrading.com/1.0/stock/%s/earnings";
    private static final String chartEndpoint = "https://www.quandl.com/api/v3/datasets/WIKI/%s.json?start_date=%s&end_date=%s&api_key=yhVpQxzRzVrDo_bvrzWG";

    private final Client client;

    public EhsClient(Client client) {
        this.client = client;
    }

    public static DateTime getPreviousTradingDateTime(DateTime dateTime) throws Exception {
        DateTime previousDay = dateTime.minusDays(1);
        // 6 is Saturday, 7 is Sunday
        while ((previousDay.getDayOfWeek() == 6) || (previousDay.getDayOfWeek() == 7)) {
            previousDay = previousDay.minusDays(1);
        }
        return previousDay;
    }

    public DayPrice getChartPriceDataOnDate(String ticker, String date) throws Exception {

        DateTime dateTime = DateTime.parse(date, CommonHelper.dateTimeFormatter);
        DateTime previousDay = getPreviousTradingDateTime(dateTime);
        String url = String.format(chartEndpoint, ticker, previousDay, date);

        Response response = this.client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get();
        JsonNode root = JsonHelper.OBJECT_MAPPER.readTree(response.readEntity(String.class));
        JsonNode dataNode = root.path("dataset").path("data");
        DayPrice.Builder builder = new DayPrice.Builder();
        if (dataNode.isArray()) {
            for (JsonNode priceNode : dataNode) {
                ArrayNode arrayNode = (ArrayNode) priceNode;
                if (arrayNode.get(0).asText().equals(date)) {
                    builder.openPrice(arrayNode.get(1).asDouble());
                    builder.highPrice(arrayNode.get(2).asDouble());
                    builder.lowPrice(arrayNode.get(3).asDouble());
                    builder.closePrice(arrayNode.get(4).asDouble());
                    builder.date(dateTime);
                } else {
                    builder.previousClosePrice(arrayNode.get(4).asDouble());
                }
            }
        }
        return builder.build();
    }

    public List<Earning> getEarningsData(String ticker) throws Exception {
        String url = String.format(earningsEndpoint, ticker);
        Response response = this.client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get();

        JsonNode root = JsonHelper.OBJECT_MAPPER.readTree(response.readEntity(String.class));
        JsonNode earningsArray = root.path("earnings");

        List<Earning> earnings = new ArrayList<>();

        if (earningsArray.isArray()) {
            for (JsonNode jsonNode : earningsArray) {
                Earning earning = JsonHelper.OBJECT_MAPPER.treeToValue(jsonNode, Earning.class);
                if (earning != null) {
                    String announceTime = jsonNode.get("announceTime").asText();
                    Boolean isPreMarket = false;
                    if (announceTime.equals("BTO")) {
                        isPreMarket = true;
                    }
                    // Other is "AMC"
                    earnings.add((new Earning.Builder(earning)).isPreMarket(isPreMarket).build());
                }
            }
        }

        return earnings;
    }

}
