package tech.bgdigital.online.payment.models.dto.bankservice;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class CardDebitIn {
    @Size(min = 10, max = 100, message = "Transaction doit avoir 10 caractères minimum et maximum 100 caractères ")
    public String transactionNumber;

    public BigDecimal amount;

    public String callBackUrl;

    public String redirectUrl;
    public String cancelUrl;

    public String customerCardholderName;

    public String customerAddress;

    public String customerCvv;

    public String customerExpiredCard;

    public String customerPan;
    public String customerPhone;
}
