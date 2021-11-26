package tech.bgdigital.online.payment.services.manager.orabank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse {
    @JsonProperty("authorizationCode")
    public String authorizationCode;
    @JsonProperty("success")
    public boolean success;
    @JsonProperty("resultCode")
    public String resultCode;
    @JsonProperty("resultMessage")
    public String resultMessage;
    @JsonProperty("mid")
    public String mid;
    @JsonProperty("rrn")
    public String rrn;
}
