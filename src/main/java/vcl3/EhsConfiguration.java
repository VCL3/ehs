package vcl3;

import com.intrence.core.persistence.postgres.PostgresConfig;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.JerseyClientConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.UnsupportedEncodingException;

public class EhsConfiguration extends Configuration {

    @NotEmpty
    private final String jwtTokenSecret = "3C8AD1BAE964DB3446BE5CABA4E67BA4E67BA4E67";

    public byte[] getJwtTokenSecret() throws UnsupportedEncodingException {
        return jwtTokenSecret.getBytes("UTF-8");
    }

    @Valid
    @NotNull
    @JsonProperty
    private JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();

    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return jerseyClientConfiguration;
    }

    @Valid
    @NotNull
    @JsonProperty
    private PostgresConfig postgresConfig = new PostgresConfig.Builder().build();

    public PostgresConfig getPostgresConfig() {
        return postgresConfig;
    }
}
