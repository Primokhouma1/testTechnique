package tech.bgdigital.online.payment.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bgdigital.online.payment.models.entity.Task;
import tech.bgdigital.online.payment.models.entity.User;
import tech.bgdigital.online.payment.models.enumeration.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByDueDate(LocalDateTime dueDate);
    List<Task> findByStatusAndDueDate(TaskStatus status, LocalDateTime dueDate);
}
