/**
 * Created by wliu on 1/31/18.
 */
package vcl3.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.joda.time.DateTime;
import vcl3.core.CommonHelper;

import java.text.DecimalFormat;

@JsonDeserialize(builder = DayPrice.Builder.class)
public class DayPrice {

    private Double openPrice;
    private Double closePrice;
    private Double previousClosePrice;
    private Double changePercent;
    private Double lowPrice;
    private Double highPrice;
    private DateTime date;

    private DayPrice(Builder builder) {
        this.openPrice = builder.openPrice;
        this.closePrice = builder.closePrice;
        this.previousClosePrice = builder.previousClosePrice;
        if (builder.changePercent == null) {
            this.changePercent = calculateChangePercent(this.previousClosePrice, this.closePrice);
        } else {
            this.changePercent = builder.changePercent;
        }
        this.lowPrice = builder.lowPrice;
        this.highPrice = builder.highPrice;
        this.date = builder.date;
    }

    @JsonProperty
    public Double getOpenPrice() {
        return this.openPrice;
    }

    @JsonProperty
    public Double getClosePrice() {
        return this.closePrice;
    }

    @JsonProperty
    public Double getPreviousClosePrice() {
        return this.previousClosePrice;
    }

    @JsonProperty
    public Double getChangePercent() {
        return this.changePercent;
    }

    @JsonProperty
    public Double getLowPrice() {
        return this.lowPrice;
    }

    @JsonProperty
    public Double getHighPrice() {
        return this.highPrice;
    }

    @JsonProperty
    public DateTime getDate() {
        return this.date;
    }

    public static Double calculateChangePercent(Double prev, Double close) {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        String formattedChangePercent = decimalFormat.format((close - prev) / prev * 100);
        return new Double(formattedChangePercent);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {

        private Double openPrice;
        private Double closePrice;
        private Double previousClosePrice;
        private Double changePercent;
        private Double lowPrice;
        private Double highPrice;
        private DateTime date;

        public Builder() {
        }

        public Builder(DayPrice dayPrice) {
            this.openPrice = dayPrice.openPrice;
            this.closePrice = dayPrice.closePrice;
            this.previousClosePrice = dayPrice.previousClosePrice;
            this.changePercent = dayPrice.changePercent;
            this.lowPrice = dayPrice.lowPrice;
            this.highPrice = dayPrice.highPrice;
            this.date = dayPrice.date;
        }

        @JsonSetter
        public Builder openPrice(Double openPrice) {
            this.openPrice = openPrice;
            return this;
        }

        @JsonSetter
        public Builder closePrice(Double closePrice) {
            this.closePrice = closePrice;
            return this;
        }

        @JsonSetter
        public Builder previousClosePrice(Double previousClosePrice) {
            this.previousClosePrice = previousClosePrice;
            return this;
        }

        @JsonSetter
        public Builder changePercent(Double changePercent) {
            this.changePercent = changePercent;
            return this;
        }

        @JsonSetter
        public Builder lowPrice(Double lowPrice) {
            this.lowPrice = lowPrice;
            return this;
        }

        @JsonSetter
        public Builder highPrice(Double highPrice) {
            this.highPrice = highPrice;
            return this;
        }

        @JsonSetter
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        public DayPrice build() {
            return new DayPrice(this);
        }
    }


}
