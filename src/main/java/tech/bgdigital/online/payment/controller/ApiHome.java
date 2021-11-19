package tech.bgdigital.online.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiHome {
    @RequestMapping(value = "/",method = RequestMethod.GET)
    String home() throws JsonProcessingException {

        return this.getResponse();
    }
    static class ResponseHome{
        public String name = "Online Payment Service";
        public String version = "1.0.0";
        public String autor = "PSN";
    }
    public String getResponse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString( this.getResponse());
    }
}
