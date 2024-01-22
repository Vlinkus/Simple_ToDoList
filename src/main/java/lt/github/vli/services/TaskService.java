package lt.github.vli.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lt.github.vli.entities.Task;
import lt.github.vli.entities.TaskResponseEntity;
import lt.github.vli.exceptions.TaskDoesNotExistException;
import lt.github.vli.repositories.TaskRepository;

@Service
public class TaskService {
	
	private final TaskRepository repository;
	
	public TaskService(TaskRepository taskRepository) {
		super();
		this.repository = taskRepository;
	}
	
	public List<Task> getAll(){
		return repository.findAll();
	}
	
	public TaskResponseEntity save(Task task) {
		if(task == null)
			throw new NullPointerException("TASK == NULL");
		task.setId(null);
		Task savedTask = repository.save(task);
		return savedTask != null ? new TaskResponseEntity(savedTask, HttpStatus.OK, "Task SAVED")
				: new TaskResponseEntity(HttpStatus.BAD_REQUEST, "Task save CANCELED");	
	}
	
	public TaskResponseEntity delete(Long id) {
		if(id == null)
			throw new NullPointerException("ID == NULL");
		boolean exists = repository.existsById(id);
		if(exists) {
			repository.deleteById(id);
			exists = repository.existsById(id);
		} else {
			throw new TaskDoesNotExistException("Task does not EXIST");
		}
		return exists ? new TaskResponseEntity(HttpStatus.BAD_REQUEST, "Task delete CANCELED")
				: new TaskResponseEntity(HttpStatus.OK, "Task DELETED");
	}
	
	public Task findById(Long id) {
		if(id == null)
			throw new NullPointerException("ID == NULL");
		return repository.findById(id)
				.orElseThrow(() -> new TaskDoesNotExistException("Task does not EXIST"));
	}
	
	public TaskResponseEntity update(Long id,Task task) {
		Task updatedTask = null;
		if(task == null || id == null)
			throw new NullPointerException("TASK || ID == NULL");
		if(id != task.getId())
			throw new IllegalArgumentException("Task Id and ID does not match");
		Task taskDB = findById(task.getId());
		if(taskDB != null)
			updatedTask = repository.save(task);
		return updatedTask != null ? new TaskResponseEntity(updatedTask, HttpStatus.OK, "Task UPDATED")
				: new TaskResponseEntity(HttpStatus.BAD_REQUEST, "Task update CANCELED");	
	}
		
}
