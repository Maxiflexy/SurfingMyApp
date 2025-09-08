package com.digicore;


import com.digicore.api.helper.response.ApiResponseJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OmniRootServiceApplication.class)
@WebAppConfiguration
public class AbstractTest {
    protected MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    protected void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @SuppressWarnings(value = "unchecked")
    protected ApiResponseJson<Object> jsonToObject(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, ApiResponseJson.class);
    }

    protected String objectToJson(Object object) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }


}
