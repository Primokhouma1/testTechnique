package tech.bgdigital.online.payment.models.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Table(name = "users", indexes = {
        @Index(name = "email_UNIQUE", columnList = "email", unique = true),
        @Index(name = "phone_UNIQUE", columnList = "phone", unique = true),
        @Index(name = "fk_users_partners1_idx", columnList = "partners_id"),
        @Index(name = "fk_users_profils1_idx", columnList = "profils_id")
})
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profils_id", nullable = false)
    private Profil profils;

    @ManyToOne
    @JoinColumn(name = "partners_id")
    private Partner partners;

    @Lob
    @Column(name = "type_user")
    private String typeUser;

    @Lob
    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, length = 70)
    private String email;

    @Column(name = "phone", nullable = false, length = 70)
    private String phone;

    @Column(name = "admin_parter", nullable = false)
    private Boolean adminParter = false;

    @Column(name = "sys_admin", nullable = false)
    private Boolean sysAdmin = false;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "otp", length = 10)
    private String otp;

    @Column(name = "expired_otp")
    private Instant expiredOtp;

    public Instant getExpiredOtp() {
        return expiredOtp;
    }

    public void setExpiredOtp(Instant expiredOtp) {
        this.expiredOtp = expiredOtp;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getSysAdmin() {
        return sysAdmin;
    }

    public void setSysAdmin(Boolean sysAdmin) {
        this.sysAdmin = sysAdmin;
    }

    public Boolean getAdminParter() {
        return adminParter;
    }

    public void setAdminParter(Boolean adminParter) {
        this.adminParter = adminParter;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public Partner getPartners() {
        return partners;
    }

    public void setPartners(Partner partners) {
        this.partners = partners;
    }

    public Profil getProfils() {
        return profils;
    }

    public void setProfils(Profil profils) {
        this.profils = profils;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}