package tech.bgdigital.online.payment.models.entity;

import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Table(name = "tarif_frais", indexes = {
        @Index(name = "services_id_partners_id_UNIQUE", columnList = "services_id, partners_id", unique = true),
        @Index(name = "fk_tarif_frais_services1_idx", columnList = "services_id"),
        @Index(name = "fk_tarif_frais_partners_idx", columnList = "partners_id")
})
@Entity
@SQLDelete(sql = "update tarif_frais set state = 'DISABLED' where id= ?")
public class TarifFrai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "fee_fixed_partner", nullable = false, precision = 17, scale = 4)
    private BigDecimal feeFixedPartner;

    @Column(name = "fee_percent_partner", nullable = false, precision = 17, scale = 4)
    private BigDecimal feePercentPartner;

    @ManyToOne(optional = false)
    @JoinColumn(name = "partners_id", nullable = false)
    private Partner partners;

    @ManyToOne(optional = false)
    @JoinColumn(name = "services_id", nullable = false)
    private Service services;

    public Service getServices() {
        return services;
    }

    public void setServices(Service services) {
        this.services = services;
    }

    public Partner getPartners() {
        return partners;
    }

    public void setPartners(Partner partners) {
        this.partners = partners;
    }

    public BigDecimal getFeePercentPartner() {
        return feePercentPartner;
    }

    public void setFeePercentPartner(BigDecimal feePercentPartner) {
        this.feePercentPartner = feePercentPartner;
    }

    public BigDecimal getFeeFixedPartner() {
        return feeFixedPartner;
    }

    public void setFeeFixedPartner(BigDecimal feeFixedPartner) {
        this.feeFixedPartner = feeFixedPartner;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}