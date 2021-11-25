package tech.bgdigital.online.payment.services.manager.orabank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ora3ds {
    @JsonProperty("acsUrl")
    public String  acsUrl;

    @JsonProperty("acsPaReq")
    public String  acsPaReq;

    @JsonProperty("acsMd")
    public String  acsMd;

    @JsonProperty("summaryText")
    public String  summaryText;
}
