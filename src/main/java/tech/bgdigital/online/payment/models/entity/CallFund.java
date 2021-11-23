package tech.bgdigital.online.payment.models.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "call_funds")
@Entity
public class CallFund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "partners_id", nullable = false)
    private Partner partners;

    @Lob
    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "amount", nullable = false, precision = 17, scale = 4)
    private BigDecimal amount;

    @Lob
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "success_at")
    private Date successAt;

    @Column(name = "canceled_at")
    private Date canceledAt;

    @Column(name = "failed_at")
    private Date failedAt;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Partner getPartners() {
        return partners;
    }

    public void setPartners(Partner partners) {
        this.partners = partners;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}