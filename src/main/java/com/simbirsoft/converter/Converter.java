package com.simbirsoft.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Converter {

    public static String asJsonString(final Object object) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String jsonContent = mapper.writeValueAsString(object);
        return jsonContent;
    }
}
