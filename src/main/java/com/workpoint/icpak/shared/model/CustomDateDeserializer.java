package com.workpoint.icpak.shared.model;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomDateDeserializer extends JsonDeserializer<Date>
{
    @Override
    public Date deserialize(JsonParser jsonparser,
            DeserializationContext deserializationcontext) throws IOException {

        String date = jsonparser.getText();
        if(date==null || date.isEmpty()){
        	return null;
        }
        return new Date(Long.parseLong(date));
    }


}
