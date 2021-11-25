package tech.bgdigital.online.payment.services.manager.orabank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginOraOut{
    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("refresh_token")
    public String refreshToken;

    @JsonProperty("expires_in")
    public Integer expiresIn;

    @JsonProperty("refresh_expires_in")
    public Integer refreshExpiresIn;

    @JsonProperty("token_type")
    public String tokenType;
}