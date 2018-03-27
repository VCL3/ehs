package vcl3.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.joda.time.DateTime;
import vcl3.core.JsonHelper;

import java.io.IOException;
import java.util.List;

@JsonDeserialize(builder = StockTracking.Builder.class)
public class StockTracking {

    private StockMetadata stockMetadata;
    private Boolean isWatched;
    private Boolean isAlertScheduled;
    private DateTime createdAt;
    private DateTime scheduledAt;

    private StockTracking(Builder builder) {
        this.stockMetadata = builder.stockMetadata;
        this.isWatched = builder.isWatched;
        this.isAlertScheduled = builder.isAlertScheduled;
        this.createdAt = builder.createdAt;
        this.scheduledAt = builder.scheduledAt;
    }

    @JsonProperty
    public StockMetadata getStockMetadata() {
        return this.stockMetadata;
    }

    @JsonProperty
    public Boolean getIsWatched() {
        return this.isWatched;
    }

    @JsonProperty
    public Boolean getIsAlertScheduled() {
        return this.isAlertScheduled;
    }

    @JsonProperty
    public DateTime getCreatedAt() {
        return this.createdAt;
    }

    @JsonProperty
    public DateTime getScheduledAt() {
        return this.scheduledAt;
    }

    public static List<StockTracking> fromJsonArray(String json) {
        try {
            return JsonHelper.OBJECT_MAPPER.readValue(json, new TypeReference<List<StockTracking>>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {

        private StockMetadata stockMetadata;
        private Boolean isWatched;
        private Boolean isAlertScheduled;
        private DateTime createdAt;
        private DateTime scheduledAt;

        public Builder() {
        }

        public Builder(StockTracking stockTracking) {
            this.stockMetadata = stockTracking.stockMetadata;
            this.isWatched = stockTracking.isWatched;
            this.isAlertScheduled = stockTracking.isAlertScheduled;
            this.createdAt = stockTracking.createdAt;
            this.scheduledAt = stockTracking.scheduledAt;
        }

        @JsonSetter
        public Builder stockMetadata(StockMetadata stockMetadata) {
            this.stockMetadata = stockMetadata;
            return this;
        }

        @JsonSetter
        public Builder isWatched(Boolean isWatched) {
            this.isWatched = isWatched;
            return this;
        }

        @JsonSetter
        public Builder isAlertScheduled(Boolean isAlertScheduled) {
            this.isAlertScheduled = isAlertScheduled;
            return this;
        }

        @JsonSetter
        public Builder createdAt(DateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @JsonSetter
        public Builder scheduledAt(DateTime scheduledAt) {
            this.scheduledAt = scheduledAt;
            return this;
        }

        public StockTracking build() {
            return new StockTracking(this);
        }
    }

}
