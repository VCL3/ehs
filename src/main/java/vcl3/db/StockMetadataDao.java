/**
 * Created by wliu on 2/20/18.
 */
package vcl3.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import vcl3.api.StockMetadata;

import java.util.UUID;

public interface StockMetadataDao {

    @SqlQuery("SELECT * FROM stockmetadata WHERE ticker=:ticker")
    @Mapper(StockMetadataMapper.class)
    StockMetadata getStockMetadataByTicker(@Bind("ticker") String ticker);

    @SqlUpdate("INSERT INTO stockmetadata (uuid, name, ticker, exchange) VALUES (:uuid, :name, :ticker, :exchange)")
    void createStockMetadata(@Bind("uuid") UUID uuid, @Bind("name") String name, @Bind("ticker") String ticker, @Bind("exchange") String exchange);

}
