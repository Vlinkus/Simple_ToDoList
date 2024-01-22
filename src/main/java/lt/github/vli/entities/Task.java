package lt.github.vli.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Task {
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	Long id;
	String taskDescription;
	
	public Task(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	
}
