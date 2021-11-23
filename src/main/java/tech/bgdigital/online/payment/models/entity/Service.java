package tech.bgdigital.online.payment.models.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "services", indexes = {
        @Index(name = "code_UNIQUE", columnList = "code", unique = true)
})
@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Lob
    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "comm_fixe_psp", nullable = false, precision = 17, scale = 4)
    private BigDecimal commFixePsp;

    @Column(name = "comm_percent_psp", nullable = false, precision = 17, scale = 4)
    private BigDecimal commPercentPsp;

    @Column(name = "psp_name", nullable = false)
    private String pspName;

    public String getPspName() {
        return pspName;
    }

    public void setPspName(String pspName) {
        this.pspName = pspName;
    }

    public BigDecimal getCommPercentPsp() {
        return commPercentPsp;
    }

    public void setCommPercentPsp(BigDecimal commPercentPsp) {
        this.commPercentPsp = commPercentPsp;
    }

    public BigDecimal getCommFixePsp() {
        return commFixePsp;
    }

    public void setCommFixePsp(BigDecimal commFixePsp) {
        this.commFixePsp = commFixePsp;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}