/**
 * Created by wliu on 2/1/18.
 */
package vcl3.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.joda.time.DateTime;

@JsonDeserialize(builder = Earning.Builder.class)
public class Earning {

    private Double estimatedEPS;
    private Double actualEPS;
    private DateTime EPSReportDate;
    private String fiscalPeriod;
    private Boolean isPreMarket;

    private Earning(Builder builder) {
        this.estimatedEPS = builder.estimatedEPS;
        this.actualEPS = builder.actualEPS;
        this.EPSReportDate = builder.EPSReportDate;
        this.fiscalPeriod = builder.fiscalPeriod;
        this.isPreMarket = builder.isPreMarket;
    }

    @JsonProperty
    public Double getEstimatedEPS() {
        return this.estimatedEPS;
    }

    @JsonProperty
    public Double getActualEPS() {
        return this.actualEPS;
    }

    @JsonProperty("EPSReportDate")
    public DateTime getEPSReportDate() {
        return this.EPSReportDate;
    }

    @JsonProperty
    public String getFiscalPeriod() {
        return this.fiscalPeriod;
    }

    @JsonProperty
    public Boolean getIsPreMarket() {
        return this.isPreMarket;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {

        private Double estimatedEPS;
        private Double actualEPS;
        private DateTime EPSReportDate;
        private String fiscalPeriod;
        private Boolean isPreMarket;

        public Builder() {
        }

        public Builder(Earning earning) {
            this.estimatedEPS = earning.estimatedEPS;
            this.actualEPS = earning.actualEPS;
            this.EPSReportDate = earning.EPSReportDate;
            this.fiscalPeriod = earning.fiscalPeriod;
            this.isPreMarket = earning.isPreMarket;
        }

        @JsonSetter
        public Builder estimatedEPS(Double estimatedEPS) {
            this.estimatedEPS = estimatedEPS;
            return this;
        }

        @JsonSetter
        public Builder actualEPS(Double actualEPS) {
            this.actualEPS = actualEPS;
            return this;
        }

        @JsonSetter
        public Builder EPSReportDate(DateTime EPSReportDate) {
            this.EPSReportDate = EPSReportDate;
            return this;
        }

        @JsonSetter
        public Builder fiscalPeriod(String fiscalPeriod) {
            this.fiscalPeriod = fiscalPeriod;
            return this;
        }

        @JsonSetter
        public Builder isPreMarket(Boolean isPreMarket) {
            this.isPreMarket = isPreMarket;
            return this;
        }

        public Earning build() {
            return new Earning(this);
        }
    }


}
