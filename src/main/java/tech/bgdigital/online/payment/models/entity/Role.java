package tech.bgdigital.online.payment.models.entity;

import org.hibernate.annotations.SQLDelete;
import tech.bgdigital.online.payment.models.enumeration.ERole;

import javax.persistence.*;

@Table(name = "users", indexes = {
        @Index(name = "email_UNIQUE", columnList = "email", unique = true)
})
@Entity
@SQLDelete(sql = "update users set state = 'DELETED'  where id= ?")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    // === Getters ===
    public Integer getId() {
        return id;
    }

    public ERole getName() {
        return name;
    }

    // === Setters ===
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}
