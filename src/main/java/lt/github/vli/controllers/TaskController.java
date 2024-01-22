package lt.github.vli.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lt.github.vli.entities.Task;
import lt.github.vli.entities.TaskResponseEntity;
import lt.github.vli.services.TaskService;


@RestController
@RequestMapping("/api/task/v1")
public class TaskController {
	
	@Autowired
	TaskService service;
	
	@GetMapping()
	public List<Task> getAllTasks(){
		return service.getAll();
	}
	
	@GetMapping("/{id}")
	public Task getTaskById(@PathVariable Long id) {
		return service.findById(id);
	}
	
	@PostMapping()
	public TaskResponseEntity addNewTask(@RequestBody Task task) {
		return service.save(task);
	}
	
	@PutMapping("{id}")
	public TaskResponseEntity updateTask(@PathVariable Long id, @RequestBody Task task) {
		return service.update(id,task);
	}
	
	@DeleteMapping("{id}")
	public TaskResponseEntity deleteTask(@PathVariable Long id) {
		return service.delete(id);
	}
	
}
