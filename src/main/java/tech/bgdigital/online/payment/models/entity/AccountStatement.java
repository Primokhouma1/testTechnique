package tech.bgdigital.online.payment.models.entity;

import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "account_statements", indexes = {
        @Index(name = "fk_account_statement_call_funds1_idx", columnList = "call_funds_id"),
        @Index(name = "fk_account_statement_partners1_idx", columnList = "partners_id"),
        @Index(name = "fk_account_statement_transactions1_idx", columnList = "transactions_id")
})
@Entity
@SQLDelete(sql = "update account_statements set state = 'DISABLED' where id= ?")
public class AccountStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "type_operation", nullable = false)
    private String typeOperation;

    @Column(name = "amount", nullable = false, precision = 17, scale = 4)
    private BigDecimal amount;

    @Column(name = "balance_before", nullable = false, precision = 17, scale = 4)
    private BigDecimal balanceBefore;

    @Column(name = "balance_after", nullable = false, precision = 17, scale = 4)
    private BigDecimal balanceAfter;

    @Lob
    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "partners_id", nullable = false)
    private Partner partners;

    @ManyToOne
    @JoinColumn(name = "transactions_id")
    private Transaction transactions;

    @ManyToOne
    @JoinColumn(name = "call_funds_id")
    private CallFund callFunds;

    @Lob
    @Column(name = "origin", nullable = false)
    private String origin;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public CallFund getCallFunds() {
        return callFunds;
    }

    public void setCallFunds(CallFund callFunds) {
        this.callFunds = callFunds;
    }

    public Transaction getTransactions() {
        return transactions;
    }

    public void setTransactions(Transaction transactions) {
        this.transactions = transactions;
    }

    public Partner getPartners() {
        return partners;
    }

    public void setPartners(Partner partners) {
        this.partners = partners;
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

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}