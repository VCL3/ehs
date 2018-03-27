/**
 * Created by wliu on 6/5/17.
 */
package vcl3.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.text.SimpleDateFormat;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class JsonHelper {

    public static final ObjectMapper OBJECT_MAPPER = Jackson.newObjectMapper()
            .registerModule(new GuavaModule())
            .registerModule(new JodaModule())
            .setSerializationInclusion(JsonInclude.Include.ALWAYS)
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(WRITE_DATES_AS_TIMESTAMPS, false);

    public static <T> T fromJson(String json, Class<T> objectClass) {
        try {
            return OBJECT_MAPPER.readValue(json, objectClass);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not serialize Source to JSON string.", e);
        }
    }

}
