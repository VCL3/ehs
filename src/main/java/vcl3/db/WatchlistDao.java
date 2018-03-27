package vcl3.db;

import com.intrence.core.persistence.annotation.BindJson;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import java.util.UUID;

public interface WatchlistDao {

    @SqlQuery("SELECT stock_trackings FROM watchlist WHERE user_id=:user_id")
    String getStockTrackingsByUserId(@Bind("user_id") UUID userId);

    @SqlUpdate("INSERT INTO watchlist (user_id) VALUES (:user_id)")
    void createWatchlist(@Bind("user_id") UUID userId);

    @SqlUpdate("UPDATE watchlist SET stock_trackings=:stock_trackings WHERE user_id=:user_id")
    void updateWatchlist(@Bind("user_id") UUID userId, @BindJson("stock_trackings") String stockTrackingsJson);

}
