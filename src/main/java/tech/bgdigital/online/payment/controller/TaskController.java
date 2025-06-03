package tech.bgdigital.online.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.bgdigital.online.payment.models.entity.Task;
import tech.bgdigital.online.payment.models.enumeration.TaskStatus;
import tech.bgdigital.online.payment.models.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	@Autowired
	private TaskRepository taskRepository;

	// GET /api/tasks
	@GetMapping
	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	// POST /api/tasks
	@PostMapping
	public ResponseEntity<Task> createTask(@RequestBody Task task) {
		return ResponseEntity.ok(taskRepository.save(task));
	}

	// GET /api/tasks/{id}
	@GetMapping("/{id}")
	public ResponseEntity<Task> getTaskById(@PathVariable String id) {
		return taskRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// PUT /api/tasks/{id}
	@PutMapping("/{id}")
	public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task taskDetails) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		if (optionalTask.isPresent()) {
			Task task = optionalTask.get();
			task.setTitle(taskDetails.getTitle());
			task.setDescription(taskDetails.getDescription());
			task.setStatus(taskDetails.getStatus());
			task.setDueDate(taskDetails.getDueDate());
			task.setAssignedUserId(taskDetails.getAssignedUserId());
			return ResponseEntity.ok(taskRepository.save(task));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// DELETE /api/tasks/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTask(@PathVariable String id) {
		if (taskRepository.existsById(id)) {
			taskRepository.deleteById(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// GET /api/tasks/filter?status=...&dueDate=...
	@GetMapping("/filter")
	public List<Task> filterTasks(
			@RequestParam(required = false) TaskStatus status,
			@RequestParam(required = false) String dueDate
	) {
		LocalDateTime parsedDueDate = (dueDate != null) ? LocalDateTime.parse(dueDate) : null;
		if (status != null && parsedDueDate != null) {
			return taskRepository.findByStatusAndDueDate(status, parsedDueDate);
		} else if (status != null) {
			return taskRepository.findByStatus(status);
		} else if (parsedDueDate != null) {
			return taskRepository.findByDueDate(parsedDueDate);
		} else {
			return taskRepository.findAll();
		}
	}
}
