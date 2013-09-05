package com.yunat.ccms.core.support.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.springframework.stereotype.Component;

/**
 * Used to serialize Java.util.Date, which is not a common JSON type, so we have
 * to create a custom serialize method;.
 */

@Component
public class JsonDateDeserialize extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext arg1) throws IOException, JsonProcessingException {
		String val = jsonParser.getText();

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(val);
        } catch(ParseException pex) {
        }
        return date;

	}
}