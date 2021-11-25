package tech.bgdigital.online.payment.services.manager.orabank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class OraAmount {
    @JsonProperty("currencyCode")
    public String currencyCode = "XOF";
    @JsonProperty("value")
    public BigDecimal amount;
}
