package tech.bgdigital.online.payment.models.entity;

import tech.bgdigital.online.payment.models.enumeration.State;

import javax.persistence.*;
import java.util.Date;

@Table(name = "transaction_items", indexes = {
        @Index(name = "fk_transaction_items_transactions1_idx", columnList = "transactions_id")
})
@Entity
public class TransactionItem {
    public TransactionItem() {
    }
    public TransactionItem(String name, String value, Transaction transactions) {
        this.name = name;
        this.value = value;
        this.transactions = transactions;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.state= State.ACTIVED;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "value")
    private String value;

    @Lob
    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    @JoinColumn(name = "transactions_id", nullable = false)
    private Transaction transactions;

    public Transaction getTransactions() {
        return transactions;
    }

    public void setTransactions(Transaction transactions) {
        this.transactions = transactions;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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