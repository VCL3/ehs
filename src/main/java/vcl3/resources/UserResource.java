package vcl3.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.intrence.core.authentication.AuthenticationUtil;
import com.intrence.core.authentication.User;
import com.intrence.core.persistence.dao.UserDao;
import io.dropwizard.auth.Auth;
import org.joda.time.DateTime;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import vcl3.api.StockMetadata;
import vcl3.api.StockTracking;
import vcl3.core.JsonHelper;
import vcl3.db.StockMetadataDao;
import vcl3.db.WatchlistDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonMap;
import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

@Path("/ehs/v1/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final byte[] jwtTokenSecret;
    private final UserDao userDao;
    private final StockMetadataDao stockMetadataDao;
    private final WatchlistDao watchlistDao;

    public UserResource(byte[] jwtTokenSecret, UserDao userDao, StockMetadataDao stockMetadataDao, WatchlistDao watchlistDao) {
        this.jwtTokenSecret = jwtTokenSecret;
        this.userDao = userDao;
        this.stockMetadataDao = stockMetadataDao;
        this.watchlistDao = watchlistDao;
    }

    private JsonWebSignature generateJWS(String userId) {
        final JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setSubject(userId);
        jwtClaims.setIssuedAt(NumericDate.now());
        jwtClaims.setExpirationTimeMinutesInTheFuture(30);

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(jwtClaims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(jwtTokenSecret));

        return jws;
    }

    @POST
    @Path("/login")
    public Response login(String credentials) throws Exception {

        JsonNode root = JsonHelper.OBJECT_MAPPER.readTree(credentials);
        String email = root.path("email").asText();
        String password = root.path("password").asText();

        User user = AuthenticationUtil.retrieveUserWithCredentials(this.userDao, email, password);

        if (user != null) {

            JsonWebSignature jws = generateJWS(user.getUuid().toString());

            try {
                return Response.ok(singletonMap("jwt", jws.getCompactSerialization())).build();
//                return Response.ok().cookie(new NewCookie("jwt", jws.getCompactSerialization())).build();
            } catch (JoseException e) {
                throw Throwables.propagate(e);
            }
        } else {
            throw new NotFoundException("Invalid username and password");
        }
    }

    @POST
    @Path("/signup")
    public Response signup(User user) {

        UUID userId = UUID.randomUUID();

        userDao.createUser(userId, user.getEmail(), user.getPassword(), user.getUsername(), user.getFirstName(), user.getLastName(), DateTime.now(), DateTime.now());

        JsonWebSignature jws = generateJWS(userId.toString());

        try {
            return Response.ok(singletonMap("jwt", jws.getCompactSerialization())).build();
        } catch (JoseException e) {
            throw Throwables.propagate(e);
        }
    }

    @GET
    @Path("/{userId}/watchlist")
    public Response getWatchlist(@Auth User user, @PathParam("userId") String userId) {

        String stockTrackingsJson = watchlistDao.getStockTrackingsByUserId(UUID.fromString(userId));

        List<StockTracking> stockTrackings = StockTracking.fromJsonArray(stockTrackingsJson);

        return Response.ok(stockTrackings).build();
    }

    @PUT
    @Path("/{userId}/watchlist/{ticker}")
    public Response addStockToWatchlist(@Auth User user, @PathParam("userId") String userId, @PathParam("ticker") String ticker) {

        UUID userUuid = UUID.fromString(userId);

        StockMetadata stockMetadata = stockMetadataDao.getStockMetadataByTicker(ticker);

        StockTracking stockTracking = new StockTracking.Builder()
                .stockMetadata(stockMetadata)
                .isWatched(true)
                .isAlertScheduled(false)
                .createdAt(DateTime.now())
                .scheduledAt(DateTime.now())
                .build();

        String stockTrackingsJson = watchlistDao.getStockTrackingsByUserId(userUuid);
        List<StockTracking> stockTrackings = StockTracking.fromJsonArray(stockTrackingsJson);
        stockTrackings.add(stockTracking);

        watchlistDao.updateWatchlist(userUuid, JsonHelper.toJson(stockTrackings));

        return Response.ok(stockTrackings).build();
    }

    @DELETE
    @Path("/{userId}/watchlist/{ticker}")
    public Response deleteStockFromWatchlist(@Auth User user, @PathParam("userId") String userId, @PathParam("ticker") String ticker) {

        UUID userUuid = UUID.fromString(userId);

        String stockTrackingsJson = watchlistDao.getStockTrackingsByUserId(userUuid);
        List<StockTracking> stockTrackings = StockTracking.fromJsonArray(stockTrackingsJson);
        Predicate<StockTracking> stockTrackingPredicate = stockTracking -> ticker.equals(stockTracking.getStockMetadata().getTicker());
        stockTrackings.removeIf(stockTrackingPredicate);

        watchlistDao.updateWatchlist(userUuid, JsonHelper.toJson(stockTrackings));

        return Response.ok(stockTrackings).build();
    }

}
