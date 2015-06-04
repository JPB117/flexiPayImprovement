package com.icpak.rest.providers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ICPAKSerializationModule extends SimpleModule {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ICPAKSerializationModule() {
        super("ObjectSerializerModule", new Version(0, 1, 0, "alpha", "Groupid","ArtifactId"));
        this.addSerializer(Date.class, new DateSerializer());
    }

    /**
     * 
     * Date Serializer
     * 
     * @author duggan
     *
     */
    public class DateSerializer extends JsonSerializer<Date> {

    	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
    			"yyyy-MM-dd");

    	@Override
    	public void serialize(Date date, JsonGenerator gen,
    			SerializerProvider provider) throws IOException,
    			JsonProcessingException {
    		String formattedDate = dateFormat.format(date);
    		gen.writeString(formattedDate);
    	}
    }

}