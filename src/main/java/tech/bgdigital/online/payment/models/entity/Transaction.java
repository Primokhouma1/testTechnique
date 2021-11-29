package tech.bgdigital.online.payment.models.entity;

import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Table(name = "transactions",
indexes = {
        @Index(name = "partener_trx_ref_UNIQUE", columnList = "partener_trx_ref, partners_id", unique = true),
})
@Entity
@SQLDelete(sql = "update transactions set state = 'DISABLED' where id= ?")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "services_id", nullable = false)
    private Service services;

    @ManyToOne(optional = false)
    @JoinColumn(name = "partners_id", nullable = false)
    private Partner partners;

    @Lob
    @Column(name = "type_operation", nullable = false)
    private String typeOperation;

    @Column(name = "service_code", nullable = false)
    private String serviceCode;

    @Column(name = "service_name", nullable = false)
    private String serviceName;
    //set cllient infos
    @Column(name = "customer_card_expiry", nullable = true)
    private String customerCardExpiry;

    @Column(name = "customer_card_cardholder_name", nullable = true)
    private String customerCardCardholderName;

    @Column(name = "customer_card_type", nullable = true)
    private String customerCardType;

    @Column(name = "customer_card_pan", nullable = true)
    private String customerCardPan;
    //set cllient infos

    @Lob
    @Column(name = "status", nullable = false)
    private String status;

    @Lob
    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "amount_trx", nullable = false, precision = 17, scale = 4)
    private BigDecimal amountTrx;

    @Column(name = "fee_amount_fixe_partener", nullable = false, precision = 17, scale = 4)
    private BigDecimal feeAmountFixePartener;

    @Column(name = "fee_amount_percent_partner", nullable = false, precision = 17, scale = 4)
    private BigDecimal feeAmountPercentPartner;

    @Column(name = "fee_percent_partner", nullable = false, precision = 17, scale = 4)
    private BigDecimal feePercentPartner;

    @Column(name = "comm_amount_fixe_plateform", nullable = false, precision = 17, scale = 4)
    private BigDecimal commAmountFixePlateform;

    @Column(name = "comm_amount_percent_plateform", nullable = false, precision = 17, scale = 4)
    private BigDecimal commAmountPercentPlateform;

    @Column(name = "comm_percent_plateform", nullable = false, precision = 17, scale = 4)
    private BigDecimal commPercentPlateform;

    @Column(name = "comm_amount_fixe_psp", nullable = false, precision = 17, scale = 4)
    private BigDecimal commAmountFixePsp;

    @Column(name = "comm_amount_percent_psp", nullable = false, precision = 17, scale = 4)
    private BigDecimal commAmountPercentPsp;

    @Column(name = "comm_percent_psp", nullable = false, precision = 17, scale = 4)
    private BigDecimal commPercentPsp;

    @Column(name = "part_amount_partner", nullable = false, precision = 17, scale = 4)
    private BigDecimal partAmountPartner;

    @Column(name = "part_amount_plateform", nullable = false, precision = 17, scale = 4)
    private BigDecimal partAmountPlateform;

    @Column(name = "part_amount_psp", nullable = false, precision = 17, scale = 4)
    private BigDecimal partAmountPsp;

    @Column(name = "success_at")
    private Date successAt;

    @Column(name = "canceled_at")
    private Date canceledAt;

    @Column(name = "failed_at")
    private Date failedAt;

    @Column(name = "partener_trx_ref", nullable = false)
    private String partenerTrxRef;

    @Column(name = "trx_ref", nullable = false)
    private String trxRef;

    @Column(name = "partner_name")
    private String partnerName;

    @Column(name = "customer_address")
    private String customerAddress;

    @Lob
    @Column(name = "message_success")
    private String messageSuccess;

    @Lob
    @Column(name = "message_error")
    private String messageError;

    @Lob
    @Column(name = "request_json")
    private String requestJson;

    @Lob
    @Column(name = "response_json")
    private String responseJson;

    @Lob
    @Column(name = "status_code")
    private String statusCode;

    @Lob
    @Column(name = "callback_url")
    private String callbackUrl;

    @Lob
    @Column(name = "callback_json")
    private String callbackJson;

    @Column(name = "callback_sented_at")
    private Date callbackSentedAt;

    @Column(name = "callback_failed_at")
    private Date callbackFailedAt;

    @Column(name = "call_back_retry_number")
    private Integer callBackRetryNumber;

    @Column(name = "callback_sended")
    private Boolean callbackSended;

    @Lob
    @Column(name = "redirect_url")

    private String redirectUrl;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public Boolean getCallbackSended() {
        return callbackSended;
    }

    public void setCallbackSended(Boolean callbackSended) {
        this.callbackSended = callbackSended;
    }

    public Integer getCallBackRetryNumber() {
        return callBackRetryNumber;
    }

    public void setCallBackRetryNumber(Integer callBackRetryNumber) {
        this.callBackRetryNumber = callBackRetryNumber;
    }

    public Date getCallbackFailedAt() {
        return callbackFailedAt;
    }

    public void setCallbackFailedAt(Date callbackFailedAt) {
        this.callbackFailedAt = callbackFailedAt;
    }

    public Date getCallbackSentedAt() {
        return callbackSentedAt;
    }

    public void setCallbackSentedAt(Date callbackSentedAt) {
        this.callbackSentedAt = callbackSentedAt;
    }

    public String getCallbackJson() {
        return callbackJson;
    }

    public void setCallbackJson(String callbackJson) {
        this.callbackJson = callbackJson;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public void setMessageSuccess(String messageSuccess) {
        this.messageSuccess = messageSuccess;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getTrxRef() {
        return trxRef;
    }

    public void setTrxRef(String trxRef) {
        this.trxRef = trxRef;
    }

    public String getPartenerTrxRef() {
        return partenerTrxRef;
    }

    public void setPartenerTrxRef(String partenerTrxRef) {
        this.partenerTrxRef = partenerTrxRef;
    }

    public Date getFailedAt() {
        return failedAt;
    }

    public void setFailedAt(Date failedAt) {
        this.failedAt = failedAt;
    }

    public Date getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(Date canceledAt) {
        this.canceledAt = canceledAt;
    }

    public Date getSuccessAt() {
        return successAt;
    }

    public void setSuccessAt(Date successAt) {
        this.successAt = successAt;
    }

    public BigDecimal getPartAmountPsp() {
        return partAmountPsp;
    }

    public void setPartAmountPsp(BigDecimal partAmountPsp) {
        this.partAmountPsp = partAmountPsp;
    }

    public BigDecimal getPartAmountPlateform() {
        return partAmountPlateform;
    }

    public void setPartAmountPlateform(BigDecimal partAmountPlateform) {
        this.partAmountPlateform = partAmountPlateform;
    }

    public BigDecimal getPartAmountPartner() {
        return partAmountPartner;
    }

    public void setPartAmountPartner(BigDecimal partAmountPartner) {
        this.partAmountPartner = partAmountPartner;
    }

    public BigDecimal getCommPercentPsp() {
        return commPercentPsp;
    }

    public void setCommPercentPsp(BigDecimal commPercentPsp) {
        this.commPercentPsp = commPercentPsp;
    }

    public BigDecimal getCommAmountPercentPsp() {
        return commAmountPercentPsp;
    }

    public void setCommAmountPercentPsp(BigDecimal commAmountPercentPsp) {
        this.commAmountPercentPsp = commAmountPercentPsp;
    }

    public BigDecimal getCommAmountFixePsp() {
        return commAmountFixePsp;
    }

    public void setCommAmountFixePsp(BigDecimal commAmountFixePsp) {
        this.commAmountFixePsp = commAmountFixePsp;
    }

    public BigDecimal getCommPercentPlateform() {
        return commPercentPlateform;
    }

    public void setCommPercentPlateform(BigDecimal commPercentPlateform) {
        this.commPercentPlateform = commPercentPlateform;
    }

    public BigDecimal getCommAmountPercentPlateform() {
        return commAmountPercentPlateform;
    }

    public void setCommAmountPercentPlateform(BigDecimal commAmountPercentPlateform) {
        this.commAmountPercentPlateform = commAmountPercentPlateform;
    }

    public BigDecimal getCommAmountFixePlateform() {
        return commAmountFixePlateform;
    }

    public void setCommAmountFixePlateform(BigDecimal commAmountFixePlateform) {
        this.commAmountFixePlateform = commAmountFixePlateform;
    }

    public BigDecimal getFeePercentPartner() {
        return feePercentPartner;
    }

    public void setFeePercentPartner(BigDecimal feePercentPartner) {
        this.feePercentPartner = feePercentPartner;
    }

    public BigDecimal getFeeAmountPercentPartner() {
        return feeAmountPercentPartner;
    }

    public void setFeeAmountPercentPartner(BigDecimal feeAmountPercentPartner) {
        this.feeAmountPercentPartner = feeAmountPercentPartner;
    }

    public BigDecimal getFeeAmountFixePartener() {
        return feeAmountFixePartener;
    }

    public void setFeeAmountFixePartener(BigDecimal feeAmountFixePartener) {
        this.feeAmountFixePartener = feeAmountFixePartener;
    }

    public BigDecimal getAmountTrx() {
        return amountTrx;
    }

    public void setAmountTrx(BigDecimal amountTrx) {
        this.amountTrx = amountTrx;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    public Partner getPartners() {
        return partners;
    }

    public void setPartners(Partner partners) {
        this.partners = partners;
    }

    public Service getServices() {
        return services;
    }

    public void setServices(Service services) {
        this.services = services;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerCardExpiry() {
        return customerCardExpiry;
    }

    public void setCustomerCardExpiry(String customerCardExpiry) {
        this.customerCardExpiry = customerCardExpiry;
    }

    public String getCustomerCardCardholderName() {
        return customerCardCardholderName;
    }

    public void setCustomerCardCardholderName(String customerCardCardholderName) {
        this.customerCardCardholderName = customerCardCardholderName;
    }

    public String getCustomerCardType() {
        return customerCardType;
    }

    public void setCustomerCardType(String customerCardType) {
        this.customerCardType = customerCardType;
    }

    public String getCustomerCardPan() {
        return customerCardPan;
    }

    public void setCustomerCardPan(String customerCardPan) {
        this.customerCardPan = customerCardPan;
    }
}