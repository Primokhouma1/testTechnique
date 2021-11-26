package tech.bgdigital.online.payment.services.manager.orabank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class OraLink {
    @JsonProperty("self")
    public OraUrl self = new OraUrl();

    @JsonProperty("cnp:3ds")
    public OraUrl cnp3ds = new OraUrl();

    @JsonProperty("cnp:cancel")
    public OraUrl cnpCancel = new OraUrl();

    @JsonProperty("curies")
    public List<OraUrl> curies ;
}
