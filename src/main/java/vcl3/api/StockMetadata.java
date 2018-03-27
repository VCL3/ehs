/**
 * Created by wliu on 2/20/18.
 */
package vcl3.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.UUID;

@JsonDeserialize(builder = StockMetadata.Builder.class)
public class StockMetadata {

    private UUID uuid;
    private String name;
    private String ticker;
    private String exchange;

    private StockMetadata(Builder builder) {
        this.uuid = builder.uuid;
        this.name = builder.name;
        this.ticker = builder.ticker;
        this.exchange = builder.exchange;
    }

    @JsonProperty
    public UUID getUuid() {
        return this.uuid;
    }

    @JsonProperty
    public String getName() {
        return this.name;
    }

    @JsonProperty
    public String getTicker() {
        return this.ticker;
    }

    @JsonProperty
    public String getExchange() {
        return this.exchange;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {

        private UUID uuid;
        private String name;
        private String ticker;
        private String exchange;

        public Builder() {
        }

        public Builder(StockMetadata stockMetadata) {
            this.uuid = stockMetadata.uuid;
            this.name = stockMetadata.name;
            this.ticker = stockMetadata.ticker;
            this.exchange = stockMetadata.exchange;
        }

        @JsonSetter
        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        @JsonSetter
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @JsonSetter
        public Builder ticker(String ticker) {
            this.ticker = ticker;
            return this;
        }

        @JsonSetter
        public Builder exchange(String exchange) {
            this.exchange = exchange;
            return this;
        }

        public StockMetadata build() {
            return new StockMetadata(this);
        }
    }
}
