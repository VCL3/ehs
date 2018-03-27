package vcl3.db;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import vcl3.api.StockMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class StockMetadataMapper implements ResultSetMapper<StockMetadata> {

    public StockMetadata map(int index, ResultSet results, StatementContext ctx) throws SQLException {
        return new StockMetadata.Builder()
                .uuid(UUID.fromString(results.getString("uuid")))
                .name(results.getString("name"))
                .ticker(results.getString("ticker"))
                .exchange(results.getString("exchange"))
                .build();
    }

}
