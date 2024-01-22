package lt.github.vli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import lt.github.vli.entities.Task;
import lt.github.vli.entities.TaskResponseEntity;
import lt.github.vli.exceptions.TaskDoesNotExistException;
import lt.github.vli.repositories.TaskRepository;
import lt.github.vli.services.TaskService;


@SpringBootTest
public class TaskServiceTests {

	@Mock
	TaskRepository repo;
	
	@InjectMocks
	TaskService service;
	
	private Long id;
	private Task task;
	private Task task2;
	
	@BeforeEach
	public void setUp() {
		id = 1L;
		task = new Task("Exercise");
		task2 = new Task("Prepare Dinner");
	}
	
	@Test
	public void testReturnsTrueIfTaskServiceNotNull() {
		Assertions.assertNotNull(service);
	}
	
	@Test
	public void testTo_getListOfTask() {
		when(repo.findAll()).thenReturn(List.of(task,task2));
		List<Task> tasksList = service.getAll();
		
		verify(repo).findAll();
		Assertions.assertEquals(tasksList.size(), 2);
		Assertions.assertEquals(task, tasksList.get(0));
		Assertions.assertEquals(task2, tasksList.get(1));		
	}
	@Test
	void testTo_FindTaskById() {
		when(repo.findById(id)).thenReturn(Optional.of(task));
		Task foundTask = service.findById(id);
		
		verify(repo).findById(id);
		Assertions.assertEquals(task, foundTask);
	}

	@Test
	void testTo_FindTaskById_ifIdNull() {
		Long idNull = null;
		Assertions.assertThrows(NullPointerException.class, () -> {
			service.findById(idNull);
		}, "ID == NULL");
		verify(repo, never()).findById(idNull);
	}
	
	@Test
	void testTo_FindTaskById_ifIdNonExist() {
		Long nonExistingID = 2L;
		when(repo.findById(nonExistingID)).thenThrow(TaskDoesNotExistException.class);
		Assertions.assertThrows(TaskDoesNotExistException.class, () -> {
			service.findById(nonExistingID);
		}, "Task does not EXIST");
	}

	@Test
	public void testTo_DeleteTask() {
		when(repo.existsById(id)).thenReturn(true).thenReturn(false);
		TaskResponseEntity response  =  service.delete(id);
		verify(repo).deleteById(id);
		verify(repo, times(2)).existsById(id);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals("Task DELETED", response.getMessage());
	}

	@Test
	public void testTo_DeleteTask_WithNonExistingId() {
		when(repo.existsById(id)).thenReturn(false);
		Assertions.assertThrows(TaskDoesNotExistException.class, () -> {
			service.delete(id);
		}, "Task does not EXIST");
		verify(repo, never()).deleteById(id);
	}

	@Test
	public void testTo_DeleteTask_WithIdAsNull() {
		Long idNull = null;
		Assertions.assertThrows(NullPointerException.class, () -> {
			service.delete(idNull);
		}, "ID = NULL");
		verify(repo, never()).deleteById(idNull);
	}
	@Test
	public void testTo_SaveTask() {
		when(repo.save(task)).thenReturn(task);
		TaskResponseEntity response = service.save(task);
		Task responseTask = response.getBody();
		verify(repo).save(task);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals("Task SAVED", response.getMessage());
	}

	@Test
	public void testTo_SaveTask_AsNull() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			service.save(null);
		}, "TASK == NULL");
	}

	@Test
	public void testTo_UpdateTask_WithTaskAsNull() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			service.update(1L,null);
		}, "TASK || ID == NULL");
	}
	
	@Test
	public void testTo_UpdateTask_WithIdAsNull() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			service.update(null,task);
		}, "TASK || ID == NULL");
	}
	@Test
	public void testTo_UpdateTask_WithTaskIdAsNull() {
		task.setId(null);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			service.update(1L,task);
		}, "Task Id and ID does not match");
	}

	@Test
	public void testTo_UpdateTask() {
		Task taskFromDB = new Task(1L, "Exercise");
		task.setId(id);
		when(repo.findById(id)).thenReturn(Optional.of(taskFromDB));
		when(repo.save(task)).thenReturn(task);
		TaskResponseEntity response = service.update(1L,task);
		Task responseTask = response.getBody();
		verify(repo).save(task);
		verify(repo).findById(id);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals("Task UPDATED", response.getMessage());
		Assertions.assertEquals(task.getTaskDescription(), responseTask.getTaskDescription());
	}
	
	
}
