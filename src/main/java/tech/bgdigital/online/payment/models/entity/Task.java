package tech.bgdigital.online.payment.models.entity;

import org.hibernate.annotations.SQLDelete;
import tech.bgdigital.online.payment.models.enumeration.TaskStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "task", indexes = {
        @Index(name = "fk_user_idx", columnList = "assigned_user_id")
})
@Entity
@SQLDelete(sql = "update profils set state = 'DELETED'  where id= ?")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status; // Enum pour l'état de la tâche

    private LocalDateTime dueDate;

    @Column(name = "assigned_user_id")
    private Long assignedUserId;

    // Constructeurs
    public Task() {}

    public Task(String title, String description, TaskStatus status, LocalDateTime dueDate, Long assignedUserId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.assignedUserId = assignedUserId;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }
}
