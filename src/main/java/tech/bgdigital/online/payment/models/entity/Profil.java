package tech.bgdigital.online.payment.models.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "profils", indexes = {
        @Index(name = "code_UNIQUE", columnList = "code", unique = true),
        @Index(name = "fk_profils_partners1_idx", columnList = "partners_id")
})
@Entity
public class Profil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "code", nullable = false, length = 45)
    private String code;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Lob
    @Column(name = "state", nullable = false)
    private String state;

    @ManyToOne
    @JoinColumn(name = "partners_id")
    private Partner partners;

    public Partner getPartners() {
        return partners;
    }

    public void setPartners(Partner partners) {
        this.partners = partners;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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