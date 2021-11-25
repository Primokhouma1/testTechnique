package tech.bgdigital.online.payment.services.manager.orabank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OraUrl {
    @JsonProperty("href")
    public String href;

    @JsonProperty("name")
    public String name ="";

    @JsonProperty("templated")
    public boolean templated = false;
}
