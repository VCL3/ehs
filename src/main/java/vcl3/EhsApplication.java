package vcl3;

import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import com.intrence.core.authentication.JwtAuthenticator;
import com.intrence.core.authentication.User;
import com.intrence.core.persistence.dao.UserDao;
import com.intrence.core.persistence.jdbi.JDBI;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jdbi.args.JodaDateTimeArgumentFactory;
import io.dropwizard.jdbi.args.JodaDateTimeMapper;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.skife.jdbi.v2.DBI;
import vcl3.client.EhsClient;
import vcl3.core.JsonHelper;
import vcl3.db.StockMetadataDao;
import vcl3.db.WatchlistDao;
import vcl3.health.EhsHealthCheck;
import vcl3.resources.EhsResource;
import vcl3.resources.UserResource;

import javax.sql.DataSource;
import javax.ws.rs.client.Client;

public class EhsApplication extends Application<EhsConfiguration> {

    public static void main(final String[] args) throws Exception {
        new EhsApplication().run(args);
    }

    @Override
    public String getName() {
        return "ehs";
    }

    @Override
    public void initialize(final Bootstrap<EhsConfiguration> bootstrap) {
        bootstrap.setObjectMapper(JsonHelper.OBJECT_MAPPER);
    }

    @Override
    public void run(final EhsConfiguration configuration, final Environment environment) throws Exception {

        // DB
        final DataSource dataSource = configuration.getPostgresConfig().buildTransactionPooledDataSource(environment.metrics(), environment.healthChecks());

        DBI dbi = JDBI.build(dataSource, environment.metrics());
        dbi.registerArgumentFactory(new JodaDateTimeArgumentFactory());
        dbi.registerColumnMapper(new JodaDateTimeMapper());
        UserDao userDao = dbi.onDemand(UserDao.class);
        StockMetadataDao stockMetadataDao = dbi.onDemand(StockMetadataDao.class);
        WatchlistDao watchlistDao = dbi.onDemand(WatchlistDao.class);

        // Clients
        final Client client = new JerseyClientBuilder(environment)
                .using(configuration.getJerseyClientConfiguration())
                .using(environment)
                .build("JerseyClient");

        final EhsClient ehsClient = new EhsClient(client);

        // JWT
        final byte[] jwtTokenSecret = configuration.getJwtTokenSecret();

        final JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setRequireSubject() // the JWT must have a subject claim
                .setVerificationKey(new HmacKey(jwtTokenSecret)) // verify the signature with the public key
                .setRelaxVerificationKeyValidation() // relaxes key length requirement
                .build(); // create the JwtConsumer instance

        environment.jersey().register(new AuthDynamicFeature(
                new JwtAuthFilter.Builder<User>()
                        .setJwtConsumer(jwtConsumer)
                        .setRealm("realm")
                        .setPrefix("Bearer")
                        .setAuthenticator(new JwtAuthenticator(userDao))
                        .buildAuthFilter()));

        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        // Resources
        environment.jersey().register(new EhsResource(ehsClient, stockMetadataDao));
        environment.jersey().register(new UserResource(jwtTokenSecret, userDao, stockMetadataDao, watchlistDao));
        environment.healthChecks().register("math", new EhsHealthCheck());
    }

}
