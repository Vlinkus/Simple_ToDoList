package lt.github.vli;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import lt.github.vli.controllers.TaskController;
import lt.github.vli.entities.Task;
import lt.github.vli.entities.TaskResponseEntity;
import lt.github.vli.repositories.TaskRepository;
import lt.github.vli.services.TaskService;

@WebMvcTest(TaskController.class)
public class TaskControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TaskService service;
	
	@MockBean
	private TaskRepository repository;
	
	
	private Long id;
	private Task task;
	private Task task2;
	
	@BeforeEach
	public void setUp() {
		id = 1L;
		task = new Task("Exercise");
		task2 = new Task("Work");
	}
	
	
	@Test
	public void testtoGet_ListOfTaskFromEndpoint() throws Exception {
		task.setId(id);
		task2.setId(2L);
		List<Task> taskList = Arrays.asList(task,task2);
		when(service.getAll()).thenReturn(taskList);
		// Perform the GET request and validate the response
		mockMvc.perform(MockMvcRequestBuilders.get("/api/task/v1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].taskDescription").value("Exercise"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].taskDescription").value("Work"));
		verify(service).getAll();
	}
	
	@Test
	public void testToFind_TaskById() throws Exception {
		task.setId(id);
		when(service.findById(id)).thenReturn(task);
		// Perform the GET request and validate the response
		mockMvc.perform(MockMvcRequestBuilders.get("/api/task/v1/{id}",1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.taskDescription").value("Exercise"));
		verify(service).findById(id);
	}
	
	@Test
	public void testToDelete_Task() throws Exception {
		task.setId(id);
		when(service.delete(id)).thenReturn(new TaskResponseEntity(HttpStatus.OK, "Task DELETED"));
		// Perform the GET request and validate the response
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/task/v1/{id}",1))
				.andExpect(MockMvcResultMatchers.status().isOk());
		verify(service).delete(id);
	}
	
	@Test
	public void testToSave_Task() throws Exception {
		String taskJson = new ObjectMapper().writeValueAsString(task);
		Task savedTask = new Task(id, task.getTaskDescription());
		when(service.save(task)).thenReturn(new TaskResponseEntity(savedTask, HttpStatus.OK, "Task SAVED"));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/task/v1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(taskJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("$.taskDescription").value("Exercise"));
		verify(service).save(task);
	}
	
	@Test
	public void testToUpdate_Task() throws Exception {
		task.setId(id);
		Task taskUpdate = new Task(id, task2.getTaskDescription());
		String taskJson = new ObjectMapper().writeValueAsString(taskUpdate);
		when(service.findById(id)).thenReturn(task);
		when(service.update(id,taskUpdate)).thenReturn(new TaskResponseEntity(taskUpdate, HttpStatus.OK, "Task SAVED"));
//		BDDMockito.given(service.update(ArgumentMatchers.any(),ArgumentMatchers.any())).will(invocation -> {
//			Task updatedTask =  invocation.getArgument(0);
//			return new TaskResponseEntity(updatedTask, HttpStatus.OK, "Task UPDATED");
//		});
		mockMvc.perform(MockMvcRequestBuilders.put("/api/task/v1/{id}",1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(taskJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("$.taskDescription").value("Work"));
		verify(service).update(id,taskUpdate);
	}
	
	

	
}
