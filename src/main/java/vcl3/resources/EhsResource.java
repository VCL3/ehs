/**
 * Created by wliu on 1/31/18.
 */
package vcl3.resources;

import org.joda.time.DateTime;
import vcl3.api.DayPrice;
import vcl3.api.Earning;
import vcl3.api.StockMetadata;
import vcl3.client.EhsClient;
import vcl3.core.CommonHelper;
import vcl3.core.JsonHelper;
import vcl3.db.StockMetadataDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;


@Path("/ehs/v1/stock")
@Produces(MediaType.APPLICATION_JSON)
public class EhsResource {

    private final String jsonRes = "[{\"openPrice\":167.64,\"closePrice\":168.11,\"changePercent\":0.731,\"lowPrice\":165.28,\"highPrice\":168.5,\"date\":\"2017-11-01T16:00:00.000Z\"},{\"openPrice\":149.1,\"closePrice\":150.05,\"changePercent\":0.806,\"lowPrice\":148.41,\"highPrice\":150.22,\"date\":\"2017-07-31T16:00:00.000Z\"},{\"openPrice\":147.54,\"closePrice\":147.51,\"changePercent\":0.621,\"lowPrice\":146.84,\"highPrice\":148.09,\"date\":\"2017-05-01T16:00:00.000Z\"},{\"openPrice\":121.15,\"closePrice\":121.35,\"changePercent\":-0.23,\"lowPrice\":120.62,\"highPrice\":121.39,\"date\":\"2017-01-30T16:00:00.000Z\"}]";

    private final EhsClient ehsClient;
    private final StockMetadataDao stockMetadataDao;

    public EhsResource(EhsClient ehsClient, StockMetadataDao stockMetadataDao) {
        this.ehsClient = ehsClient;
        this.stockMetadataDao = stockMetadataDao;
    }

    @GET
    @Path("/status")
    public Response checkStatus() {
        return Response.ok("Status: Live").build();
    }

    @GET
    @Path("/{ticker}/metadata")
    public Response getStockMetadata(@PathParam("ticker") String ticker) throws Exception {
        StockMetadata stockMetadata = this.stockMetadataDao.getStockMetadataByTicker(ticker.toUpperCase());
        if (stockMetadata != null) {
            return Response.ok(stockMetadata).build();
        } else {
            throw new NotFoundException(ticker.toUpperCase() + " cannot be found");
        }
    }

    @GET
    @Path("/{ticker}/current")
    public Response getCurrentStockPrice(@PathParam("ticker") String ticker) throws Exception {
        DayPrice dayPrice = (new DayPrice.Builder()).date(DateTime.now()).previousClosePrice(17.24).closePrice(13.10).build();
        return Response.ok(dayPrice).build();
    }

    @GET
    @Path("/{ticker}/{date}")
    public Response getStockPriceOnDate(@PathParam("ticker") String ticker,
                                        @PathParam("date") String date) throws Exception {

        DayPrice dayPrice = this.ehsClient.getChartPriceDataOnDate(ticker, date);
        return Response.ok(dayPrice).build();
    }

    @GET
    @Path("/{ticker}/earnings")
    public Response getStockEarnings(@PathParam("ticker") String ticker) throws Exception {
        List<Earning> earnings = this.ehsClient.getEarningsData(ticker);
        return Response.ok(earnings).build();
    }

    @GET
    @Path("/{ticker}")
    public Response getStockData(@PathParam("ticker") String ticker) throws Exception {
        StockMetadata stockMetadata = this.stockMetadataDao.getStockMetadataByTicker(ticker.toUpperCase());
        DayPrice currentPrice = (new DayPrice.Builder()).date(DateTime.now()).previousClosePrice(17.24).closePrice(13.10).build();
//        List<Earning> earnings = this.ehsClient.getEarningsData(ticker);
//        List<DayPrice> dayPrices = new ArrayList<>();
//        for (Earning earning : earnings) {
//            DayPrice dayPrice = this.ehsClient.getChartPriceDataOnDate(ticker, earning.getEPSReportDate().toString(CommonHelper.dateTimeFormatter));
//            if (dayPrice != null) {
//                dayPrices.add(dayPrice);
//            }
//        }
//        return Response.ok(dayPrices).build();
        return Response.ok(String.format("{\"metadata\":%s, \"currentPrice\":%s, \"stockPrices\":%s}", JsonHelper.toJson(stockMetadata), JsonHelper.toJson(currentPrice), jsonRes)).build();
    }

    @GET
    @Path("/selections")
    public Response getStockSelections() throws Exception {
        DateTime dateTimeNow = DateTime.now();
        List<Map<String, List<String>>> stockSelections = new ArrayList<>();

        stockSelections.add(new HashMap<String, List<String>>() {{
                put(dateTimeNow.toString(CommonHelper.dateTimeFormatter), Arrays.asList("aapl", "fb"));
            }});
        stockSelections.add(new HashMap<String, List<String>>()
        {{
            put(dateTimeNow.plusDays(1).toString(CommonHelper.dateTimeFormatter), Arrays.asList("aapl", "nvda", "txn"));
        }});
        stockSelections.add(new HashMap<String, List<String>>()
        {{
            put(dateTimeNow.plusDays(2).toString(CommonHelper.dateTimeFormatter), Arrays.asList("aapl"));
        }});

        return Response.ok(stockSelections).build();
    }
}
