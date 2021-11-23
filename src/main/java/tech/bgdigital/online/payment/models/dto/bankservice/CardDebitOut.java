package tech.bgdigital.online.payment.models.dto.bankservice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
@ApiModel
public class CardDebitOut {
    @ApiModelProperty(position = 1, required = true, value = "1")
    public String service;
    @ApiModelProperty(position = 2, required = true, value = "1")
    public BigDecimal amount;
    public String customerFirstName;
    public String customerLastName;
    public String customerAddress;
    public String customerCvv;
    public String customerExpiredCard;
    public String customerExpiredPan;
}
